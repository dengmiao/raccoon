package com.cure.core.modules.sys.controller;

import com.cure.common.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: SecurityController
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 20:54
 **/
@RestController
@RequestMapping("security")
public class SecurityController {

    @GetMapping("needLogin")
    public ResponseEntity<Result<Object>> needLogin(){
        return new ResponseEntity<>(Result.error(HttpStatus.UNAUTHORIZED.value(),"您还未登录"), HttpStatus.UNAUTHORIZED);
    }
}
