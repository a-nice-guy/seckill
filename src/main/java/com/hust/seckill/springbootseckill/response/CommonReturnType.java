package com.hust.seckill.springbootseckill.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommonReturnType {

    //返回值为状态信息加数据
    private String status;
    private Object data;

    public static CommonReturnType create(Object result){
        return creat(result,"success");
    }

    public static CommonReturnType create(Object result, String status){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(status);
        commonReturnType.setData(result);
        return commonReturnType;
    }
}
