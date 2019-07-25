package com.shinemo.score.core.word;

import com.csvreader.CsvReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @version 1.0
 * @Description: 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 * @Project：test
 * @Author : chenming
 * @Date ： 2014年4月20日 下午2:27:06
 */
@Slf4j
public class SensitiveWordInit {

    private static volatile SensitiveWord sensitiveWord;

    private SensitiveWordInit() {
    }

    public static SensitiveWord getSensitiveWord() {

        if (sensitiveWord == null) {
            synchronized (SensitiveWordInit.class) {
                if (sensitiveWord == null) {
                    sensitiveWord = initKeyWord();
                    log.info("[sensitiveWord] sensitiveWord size:{}", sensitiveWord.getSensitiveWordMap().size());
                }
            }
        }
        return sensitiveWord;
    }

    @SuppressWarnings("rawtypes")
    private static SensitiveWord initKeyWord() {
        try {
            log.info("----- load SensitiveWord --------");
            //读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            //将敏感词库加入到HashMap中
            return addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            log.error("初始化敏感词失败", e);
        }
        return null;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = {
     * isEnd = 0
     * 国 = {<br>
     * isEnd = 1
     * 人 = {isEnd = 0
     * 民 = {isEnd = 1}
     * }
     * 男  = {
     * isEnd = 0
     * 人 = {
     * isEnd = 1
     * }
     * }
     * }
     * }
     * 五 = {
     * isEnd = 0
     * 星 = {
     * isEnd = 0
     * 红 = {
     * isEnd = 0
     * 旗 = {
     * isEnd = 1
     * }
     * }
     * }
     * }
     *
     * @param keyWordSet 敏感词库
     * @author chenming
     * @date 2014年4月20日 下午3:04:20
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static SensitiveWord addSensitiveWordToHashMap(Set<String> keyWordSet) {
        Map<String, String> sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        Set<String> singleWord = new HashSet<>();

        SensitiveWord sensitiveWord = new SensitiveWord();
        sensitiveWord.setSensitiveWordMap(sensitiveWordMap);
        sensitiveWord.setSingleWord(singleWord);

        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            // 一个字的敏感词
            if (key.length() == 1) {
                singleWord.add(key);
            }
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
        return sensitiveWord;
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     *
     * @return
     * @throws Exception
     * @author chenming
     * @date 2014年4月20日 下午2:31:18
     * @version 1.0
     */
    @SuppressWarnings("resource")
    private static Set<String> readSensitiveWordFile() {

        Set<String> set = new HashSet<>();
        // 从csv中读取
        InputStream input = SensitiveWordInit.class.getResourceAsStream("/sensitive.csv");
        try {

            //字符编码
            String ENCODING = "UTF-8";
            CsvReader csvReader = new CsvReader(input, ',', Charset.forName(ENCODING));
            csvReader.readHeaders();

            // 读取每行的内容
            while (csvReader.readRecord()) {
                // 文字获取
                String word = csvReader.get("SENSITIVE_WORD").trim();
                set.add(word);
            }
        } catch (Exception e) {
            log.error("load sensitive csv has error", e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                log.error("load sensitive csv has error", e);
            }
        }
        return set;
    }

}
