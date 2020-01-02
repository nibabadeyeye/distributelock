package com.gpdi.controller;

import com.gpdi.response.R;
import com.gpdi.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @ApiOperation("/增加订单")
    @RequestMapping("/addOrder")
    public R addOrder(String id) throws  Exception{
        return orderService.addOrder(id);
    }

    @Autowired
    private ProductService orderService;
}
