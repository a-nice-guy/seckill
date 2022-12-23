package com.hust.seckill.service.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotBlank(message = "用户名不可以为空")
    private String name;
    @NotNull(message = "性别不可以不填")
    private Byte gender;
    @NotNull(message = "年龄不可以不填")
    @Min(value = 0 , message = "年龄必须是正数")
    @Max(value = 150 , message = "年龄必须小于150岁")
    private Integer age;
    @NotBlank(message = "手机号不可以为空")
    private String telephone;

    private String registerMode;

    private String thirdPartyId;
    @NotBlank(message = "密码不可以为空")
    private String encryptPassword;
}
