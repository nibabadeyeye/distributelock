package com.gpdi.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("/封装响应工具类")
public class R {

    private int code;

    private String message;

    private Object object;


    public R(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(String message) {
        this.message = message;
    }

    public R(int code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }
}
