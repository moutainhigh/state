package com.sodanights.state.order;

import com.sodanights.state.client.SingleArcTransition;

public class OrderTransition implements SingleArcTransition<OrderRequest,OrderEvent,OrderStatusEnum>{



    @Override
    public void transition(OrderRequest orderRequest,OrderEvent orderEvent,OrderStatusEnum orderStatus){

        orderRequest.setOrderStatus(orderStatus.getId());
        System.out.println(orderRequest.getOrderStatus());
        System.out.println(orderStatus);
        //然后再根据orderRequest 结合业务去更新订单 省略;
    }
}
