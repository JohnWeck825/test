package com.myshop.adminpage.service.admin;

import com.myshop.adminpage.Interface.UserServiceInf;
import com.myshop.adminpage.model.CustomUserDetails;
import com.myshop.adminpage.model.Role;
import com.myshop.adminpage.model.User;
import com.myshop.adminpage.model.UserRole;
import com.myshop.adminpage.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.*;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserServiceInf userServiceInf;


    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Integer userId = userService.findByUsername(username);
//
//        if (userId == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        Collection<GrantedAuthority> grantedAuthoritiesSet = new LinkedHashSet<>();
//        Set<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
////        Set<UserRole> userRoles = null;
//        for (UserRole userRole : userRoles) {
//            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
//        }
//
//        System.out.println("User roles" + grantedAuthoritiesSet);
//
//        return new CustomUserDetails(null , grantedAuthoritiesSet);

//        User user = userService.findByUsername(username);
//        Integer userId = user.getId();
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        Collection<GrantedAuthority> grantedAuthoritiesSet = new LinkedHashSet<>();
////        Set<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
//        Set<Role> userRoles = user.getRoles();
//
////        Set<UserRole> userRoles = null;
//        for (Role userRole : userRoles) {
//            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(userRole.getName()));
//        }

//        Start 1
        User user = userServiceInf.findByUsername(username);
        Integer userId = user.getId();
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<GrantedAuthority> grantedAuthoritiesSet = new LinkedHashSet<>();
        Set<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
//        Set<UserRole> userRoles = null;
        for (UserRole userRole : userRoles) {
            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
        }

        System.out.println("User roles" + grantedAuthoritiesSet);

        return new CustomUserDetails(user , grantedAuthoritiesSet);
//        End 1

    }

//    public UserDetails loadUserByEmail(String email) throws UserNotFoundException {
//        User user = userService.findUserByEmail(email);
//
//        if (user == null) {
//            throw new UserNotFoundException("User not found");
//        }
//        Collection<GrantedAuthority> grantedAuthoritiesSet = new LinkedHashSet<>();
//        Set<UserRole> userRoles = user.getUserRoles();
//        for(UserRole userRole : userRoles){
//            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
//        }
//        return new CustomUserDetails(user, grantedAuthoritiesSet);
//    }

}
