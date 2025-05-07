package com.myshop.adminpage.repository;

import com.myshop.adminpage.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @Query("SELECT r FROM UserRole r WHERE r.user.id = :userId")
    Set<UserRole> findAllByUserId(@Param("userId") Integer userId);
}
