package com.gpdi.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("/产品实体类")
public class Product {

    //产品Id
    private String id;
    //产品名称
    private String name;
    //剩余数量
    private int number;
    //产品名称
    private String description;
    //系统版本
    private int version;




}
