package com.shinemo.score.client.comment.domain;

import com.shinemo.client.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum  CalculationEnum implements BaseEnum<LikeTypeEnum> {

    all(1, "全量"),
    increment(2, "增量"),;

    private @Getter int id;
    private @Getter String name;

    public static CalculationEnum getById(int id) {
        CalculationEnum[] enums = CalculationEnum.values();
        for (CalculationEnum e : enums) {
            if (e.id == id) {
                return e;
            }
        }
        throw new IllegalArgumentException("not support");
    }

}
