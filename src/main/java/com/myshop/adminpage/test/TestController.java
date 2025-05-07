package com.myshop.adminpage.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/admin/test")

public class TestController {
    @RequestMapping("/admin/test")
//    @GetMapping("")
    public String index() {
        return "/admin/test/index";
    }

    @RequestMapping("/admin/test2")
    public String index2() {
        return "/admin/test/index2";
    }

    @RequestMapping("/admin/test3")
    public String index3() {
        return "/admin/test/index3";
    }
}
