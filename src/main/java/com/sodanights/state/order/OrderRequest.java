package com.sodanights.state.order;

import lombok.Data;

@Data
public class OrderRequest {

    private Integer orderStatus;
    private String orderId;
}
