package com.shinemo.score.core.word;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author wenchao.li
 * @since 2019-07-25
 */
@Data
public class SensitiveWord {

    // 敏感词
    Map<String,String> sensitiveWordMap;

    // 一个词的敏感词
    Set<String> singleWord;
}
