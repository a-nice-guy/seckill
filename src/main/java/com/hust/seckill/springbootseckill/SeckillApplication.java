package com.hust.seckill.springbootseckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hust.seckill.springbootseckill"})
@MapperScan("com.hust.seckill.springbootseckill.dao")
//@ComponentScan("com.hust.seckill")
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
}
