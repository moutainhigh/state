package com.sodanights.state.order;

import com.sodanights.state.client.InvalidStateTransitionException;
import com.sodanights.state.client.StateMachine;
import com.sodanights.state.client.StateMachineFactory;


public class OrderStateMachine {


    private static final StateMachineFactory<OrderRequest, OrderStatusEnum, OrderEvent.OrderEventEnum, OrderEvent> stateMachineFactory = new StateMachineFactory<OrderRequest, OrderStatusEnum, OrderEvent.OrderEventEnum, OrderEvent>(OrderStatusEnum.INVALID)
            .addTransition(OrderStatusEnum.INVALID,
                    OrderStatusEnum.SUBMIT_ORDER,
                    OrderEvent.OrderEventEnum.INIT_SUCCESS,
                    new OrderTransition()) //订单无效->订单初始化
            .installTopology();


    private static StateMachine<OrderStatusEnum, OrderEvent.OrderEventEnum, OrderEvent> getStateMachine(OrderRequest request, OrderStatusEnum orderType) {
        return stateMachineFactory.make(request, orderType);
    }

    private static StateMachine<OrderStatusEnum, OrderEvent.OrderEventEnum, OrderEvent> getStateMachine(OrderRequest request) {
        return stateMachineFactory.make(request);
    }

    public static OrderStatusEnum handler(OrderRequest request, OrderEvent orderEvent) throws InvalidStateTransitionException {
        StateMachine<OrderStatusEnum, OrderEvent.OrderEventEnum, OrderEvent> orderStateMachine = null;
        if (request.getOrderStatus() == null) {
            orderStateMachine = OrderStateMachine.getStateMachine(request);
        } else {
            orderStateMachine = OrderStateMachine.getStateMachine(request, OrderStatusEnum.getById(request.getOrderStatus()));
        }
        return orderStateMachine.doTransition(orderEvent.getOrderEvent(), orderEvent);
    }

    public static void main(String args[]){

        OrderRequest request = new OrderRequest();
        request.setOrderStatus(OrderStatusEnum.INVALID.getId());//订单当前状态 必须塞入否则会根据默认的状态去找
        OrderEvent orderEvent = new OrderEvent(OrderEvent.OrderEventEnum.INIT_SUCCESS);
        try {
            OrderStateMachine.handler(request,orderEvent);
        } catch (InvalidStateTransitionException e) {
            e.printStackTrace();
        }
        System.out.println("finish");
    }


}
