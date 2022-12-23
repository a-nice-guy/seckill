package com.hust.seckill.springbootseckill.service.model;

import lombok.*;

/**
 * 核心领域用户模型
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserModel {

    private Integer id;

    private String name;

    private Byte gender;

    private Integer age;

    private String telephone;

    private String registerMode;

    private String thirdPartyId;

    private String encryptPassword;
}
