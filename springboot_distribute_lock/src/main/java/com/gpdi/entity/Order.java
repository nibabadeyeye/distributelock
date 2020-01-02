package com.gpdi.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("/订单实体类")
public class Order {

    //订单id
    private String id;
    //产品id
    private String productId;
    //用户d
    private int userId;
    //交易时间
    private String orderTime;
    //订单状态
    private int status;

}
