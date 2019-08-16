package com.shinemo.score.core.word;

import com.csvreader.CsvReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 */
@Slf4j
public class SensitiveWordInit {

    private static volatile Map<String, String> sensitiveWord;

    private SensitiveWordInit() {
    }

    public static Map<String, String> getSensitiveWord() {

        if (sensitiveWord == null) {
            synchronized (SensitiveWordInit.class) {
                if (sensitiveWord == null) {
                    sensitiveWord = initKeyWord();
                    log.info("[sensitiveWord] sensitiveWord size:{}", sensitiveWord.size());
                }
            }
        }
        return sensitiveWord;
    }

    @SuppressWarnings("rawtypes")
    private static Map<String, String> initKeyWord() {
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
     * @param keyWordSet 敏感词
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Map<String, String> addSensitiveWordToHashMap(Set<String> keyWordSet) {
        Map<String, String> sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作

        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
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
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
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
