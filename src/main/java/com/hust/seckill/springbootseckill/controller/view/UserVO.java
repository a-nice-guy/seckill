package com.hust.seckill.springbootseckill.controller.view;

import lombok.*;

/**
 * 供UI使用的用户模型
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserVO {

    private Integer id;

    private String name;

    private Byte gender;

    private Integer age;

    private String telephone;
}
