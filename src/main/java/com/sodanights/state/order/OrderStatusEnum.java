package com.sodanights.state.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum OrderStatusEnum{
	
	INVALID("无效订单",-1),
	SUBMIT_ORDER("订单已提交，等待支付",0),
	PAY_ING("订单支付中",3),
	PAY_FINIST("支付完成",6),
	TRADE_SUCCESS("交易成功",12), // 终节点
	REFUND_ING("退款中",13),
	REFUND("已退款",15), // 终节点
	TRADE_CLOSE("交易关闭",18), // 终节点
	
	;
	
	
	private @Getter final String name;
	private @Getter final int id;

	@JsonCreator
	public static OrderStatusEnum getById(int id) {
		OrderStatusEnum[] enums = OrderStatusEnum.values();
	    for (OrderStatusEnum e : enums) {
	        if (e.id == id) {
	            return e;
	        }
	    }
	    throw new IllegalArgumentException("not support");
	}

}

