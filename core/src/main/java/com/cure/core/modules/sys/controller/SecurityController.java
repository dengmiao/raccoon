package com.cure.core.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.cure.common.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @title: SecurityController
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 20:54
 **/
@RestController
@RequestMapping("security")
public class SecurityController {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @GetMapping("needLogin")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Result<Object>> needLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if(savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            if(StrUtil.endWithIgnoreCase(targetUrl, ".html")) {
                redirectStrategy.sendRedirect(request, response, "");
            }
        }
        return new ResponseEntity<>(Result.error(HttpStatus.UNAUTHORIZED.value(),"您还未登录"), HttpStatus.UNAUTHORIZED);
    }
}
