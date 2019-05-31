package com.cure.core.modules.sys.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @title: ViewController
 * @description: 视图控制器
 * @author: dengmiao
 * @create: 2019-05-31 10:37
 **/
@Controller
public class ViewController {

    @RequestMapping("/{view}")
    public String renderView(@PathVariable String view) {
        return view;
    }
}
