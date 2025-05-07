package com.myshop.adminpage.service.admin;

import com.myshop.adminpage.Interface.UserServiceInf;
import com.myshop.adminpage.model.User;
import com.myshop.adminpage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementService implements UserServiceInf {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Transactional
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Page<User> listByPage(String keyword, Integer pageNum, String sortField, String sortDir) {
        return null;
    }
}
