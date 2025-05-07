package com.myshop.adminpage.Interface;

import com.myshop.adminpage.model.Category;
import com.myshop.adminpage.model.User;
import org.springframework.data.domain.Page;

public interface UserServiceInf {
    User findUserByEmail(String email);
    User findByUsername(String username);
    User save(User user);
    Page<User> listByPage(String keyword, Integer pageNum, String sortField, String sortDir);
}
