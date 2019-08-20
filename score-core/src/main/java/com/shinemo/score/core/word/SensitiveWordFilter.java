package com.shinemo.score.core.word;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 敏感词过滤
 */
@Slf4j
public class SensitiveWordFilter {

    private static Map<String, String> sensitiveWord;
    public static int minMatchTYpe = 1;      //最小匹配规则
    public static int maxMatchType = 2;      //最大匹配规则

    static {
        sensitiveWord = SensitiveWordInit.getSensitiveWord();
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt       文字
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，否则返回false
     */
    public static boolean isContaintSensitiveWord(String txt, int matchType) {

        // 全部小写,并去除所有的符号
        txt = txt.toLowerCase().replaceAll("[\\s*\\pP\\p{Punct}]", "");

        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
            if (matchFlag > 0) {    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     */
    public static Set<String> getSensitiveWord(String txt, int matchType) {

        Set<String> sensitiveWordList = new HashSet<>();
        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
            if (length > 0) {    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    /**
     * 替换敏感字字符
     *
     * @param replaceChar 替换字符，默认*
     */
    public static String replaceSensitiveWord(String txt, int matchType, String replaceChar) {

        // 全部小写,并去除所有的符号
        txt = txt.toLowerCase().replaceAll("[\\s*\\pP\\p{Punct}]", "");

        String resultTxt = txt;

        Set<String> set = getSensitiveWord(txt, matchType);     //获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     * @author chenming
     * @date 2014年4月20日 下午5:21:19
     * @version 1.0
     */
    private static String getReplaceChars(String replaceChar, int length) {
        StringBuilder resultReplace = new StringBuilder(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace.append(replaceChar);
        }

        return resultReplace.toString();
    }

    /**
     * 检查文字中是否包含敏感字符
     *
     * @return 如果存在，则返回敏感词字符的长度，不存在返回0
     */
    public static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWord;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if (SensitiveWordFilter.minMatchTYpe == matchType) {    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else {     //不存在，直接返回
                break;
            }
        }
        if (matchFlag < 1 || !flag) {        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }


    public static void main(String[] args) {
        System.out.println("敏感词的数量：" + sensitiveWord.size());
        String string = "wow,,,,习,,,近 平 hah,，，，含有习近平";
        System.out.println(string.toLowerCase());
        System.out.println(SensitiveWordFilter.isContaintSensitiveWord(string, 1));
//        System.out.println("待检测语句字数：" + string.length());
        long beginTime = System.currentTimeMillis();
////		Set<String> set = filter.getSensitiveWord(string, 1);
        System.out.println(SensitiveWordFilter.replaceSensitiveWord(string, 2, "*"));
        long endTime = System.currentTimeMillis();
//        System.out.println(result);
////		System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        System.out.println("总共消耗时间为：" + (endTime - beginTime));
    }


}
