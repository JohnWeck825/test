package com.myshop.adminpage.controller.admin;

import com.myshop.adminpage.Interface.CategoryServiceInf;
import com.myshop.adminpage.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class CategoryCustomController {

    @Autowired
    private CategoryServiceInf categoryServiceInf;

//    @GetMapping("/Category-edit/{id}")
//    public String editCategory(@PathVariable Integer id, Model model) {
//        Optional<Category> category = this.categoryServiceInf.findById(id);
//        if (category.isPresent()) {
//            model.addAttribute("category", category.get());
//            return "/admin/category/edit";
//        }
//        return "redirect:/admin/category";
////
////        model.addAttribute("category", this.categoryServiceInf.getCategoryById(id));
////
////        return "/admin/category/index";
//    }
}
