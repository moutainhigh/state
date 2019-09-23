package com.sodanights.state.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class OrderEvent {

	private OrderEventEnum orderEvent;

	
	public static enum OrderEventEnum {

		INIT_SUCCESS,//订单初始化成功
		INVALID_ORDER, //无效订单
		SUBMIT_ORDER_UPDATE_SUB_STATUS,//更新submit_order的子状态
		SUBMIT_PAY, //提交支付
		PAY_SUCCESS, //支付成功
		ORDER_CLOSE, //订单关闭
		TRADE_FINISH, //交易完成
		REFUND, //退款
		SUBMIT_REFUND,//提交退款 流转到退款中
		;
	}
}


