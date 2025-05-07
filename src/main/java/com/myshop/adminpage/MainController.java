package com.myshop.adminpage;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping("")
    public String homePage() {
        return "client/index";
    }

    @RequestMapping("/login")
    public String loginPageForAdmin() {
        return "/admin/login";
    }

//    @GetMapping("/admin")
//    public String adminPage2() {
//        return "admin/index";
//    }

//    @GetMapping("/login")
//    public String loginPage() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (!(auth instanceof AnonymousAuthenticationToken)) {
//            return homePage();
//        }
//        return "login";
//    }
}
