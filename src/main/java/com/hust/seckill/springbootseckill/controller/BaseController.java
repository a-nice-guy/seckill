package com.hust.seckill.springbootseckill.controller;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
}
