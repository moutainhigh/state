package com.shinemo.score.client.common.domain;

import com.shinemo.client.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DeleteStatusEnum implements BaseEnum<DeleteStatusEnum> {

    NORMAL("正常",1),
	DELETE("删除",0),
	;

	private @Getter final String name;
	private @Getter final int id;

	public static DeleteStatusEnum getById(int id) {
		DeleteStatusEnum[] enums = DeleteStatusEnum.values();
		for (DeleteStatusEnum e : enums) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("not support");
	}

}