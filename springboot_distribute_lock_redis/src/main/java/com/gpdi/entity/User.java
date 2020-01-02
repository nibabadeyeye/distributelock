package com.gpdi.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("/用户实体类")
public class User {
    //用户id
    private int id;
    //用户账号
    private String userAccount;
    //用户密码
    private String password;
    //用户名称
    private String userName;

}
