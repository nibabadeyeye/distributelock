package com.gpdi.service;

import com.gpdi.exception.CongestionException;
import com.gpdi.response.R;
import io.swagger.annotations.ApiModel;

@ApiModel("/订单管理接口")
public interface ProductService {

    //增加一个订单
    public R addOrder(String productId) throws CongestionException;
}
