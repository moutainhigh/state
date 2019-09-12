package com.shinemo.score.client.comment.domain;

import com.shinemo.client.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum  VerifyStatusEnum implements BaseEnum<VerifyStatusEnum> {


    wait(0, "待审核"),
    pass(3, "审核通过"),
    refuse(6, "审核拒绝"),
    ;

    private @Getter int id;
    private @Getter String name;

    public static VerifyStatusEnum getById(int id) {
        VerifyStatusEnum[] enums = VerifyStatusEnum.values();
        for (VerifyStatusEnum e : enums) {
            if (e.id == id) {
                return e;
            }
        }
        throw new IllegalArgumentException("not support");
    }

}
