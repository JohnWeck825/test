package com.myshop.adminpage.repository;

import com.myshop.adminpage.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    @Query("select ur.role from UserRole ur where ur.user.id = :id")
    Set<Role> findUserRoles(@Param("id") Integer id);
}
