package com.myshop.adminpage.controller.admin;

import com.myshop.adminpage.model.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class LoginTestController {
//    @RequestMapping("/login")
//    public String login() {
//        return "/admin/login";
//    }

    @GetMapping("/login")
    public String showLoginForm() {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            // Nếu đã đăng nhập, chuyển hướng đến trang admin
            return "redirect:/admin";
        } else {
            // Nếu chưa đăng nhập, hiển thị form đăng nhập
            return "/admin/login";
        }
    }

//    @GetMapping("/admin-category")
//    public String checkLogin() {
//        // Kiểm tra xem người dùng đã đăng nhập chưa
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
//            // Nếu đã đăng nhập, chuyển hướng đến trang admin
//            return "redirect:/admin";
//        } else {
//            // Nếu chưa đăng nhập, hiển thị form đăng nhập
//            return "/admin/login";
//        }
//    }

//    @RequestMapping("/admin")
//    //    @GetMapping("")
//    public String adminData(Model model) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("user", customUserDetails);
//        return "admin/fragments/navbar-header";
//    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", "Invalid username or password!");
        return "/admin/login";
    }

//    @GetMapping("/admin")
//    public String adminData2(Model model) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("user", customUserDetails);
//        return "admin/fragments/sidebar";
//    }

//    @GetMapping("/login")
//    public String login2() {
//        return "/admin/index";
//    }
}
