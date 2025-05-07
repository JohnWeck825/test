package com.myshop.adminpage.repository;

import com.myshop.adminpage.model.Role;
import com.myshop.adminpage.model.User;
import com.myshop.adminpage.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("UPDATE User u SET u.enabled = :status WHERE u.id = :id")
    @Modifying
    @Transactional
    void updateUserStatus(@Param("id") Integer id, @Param("status") boolean status);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findUserByEmail(@Param("email") String email);

//    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :userId")
//    Set<Role> findByIdWithRoles(Integer userId);

//    @Query("SELECT u.roles FROM User u  WHERE u.userName = :userName")
//    Set<Role> findRoles(@Param("userName") String userName);

    @Query("SELECT r from Role r inner join UserRole ur on r.id = ur.role.id where ur.user.userName = :userName")
    Set<Role> findRoles(@Param("userName") String userName);

    @Query("SELECT ur from UserRole ur inner join Role r on r.id = ur.role.id " +
            "inner join User u on ur.user.id = u.id " +
            "where u.userName = :userName")
    Set<UserRole> findUserRoles(@Param("userName") String userName);

//    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE u.use
//    rName = :username")
//    User findByUserName(@Param("username") String userName);

//    @Query("SELECT u FROM User u LEFT JOIN  UserRole r ON u.id = r.id WHERE u.userName = :username")
//    User findByUserName(@Param("username") String userName);

//    @Query("SELECT u.id FROM User u INNER JOIN  UserRole r ON u.id = r.user.id WHERE u.userName = :username")
//    Integer findByUserName(@Param("username") String userName);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId")
    void deleteByUserId(@Param("userId") Integer userId);

    @Query("SELECT u FROM User u INNER JOIN  UserRole r ON u.id = r.user.id WHERE u.userName = :username")
    User findByUserName(@Param("username") String userName);

    @Query("SELECT u FROM User u WHERE u.userName = :username")
    User getByUserName(@Param("username") String userName);

    @Query("SELECT u FROM User u WHERE concat(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName, ' ', u.userName, ' ', ' ') like %?1%")
    Page<User> search(String keyword, Pageable pageable);

    @Query("SELECT u.role FROM UserRole u WHERE u.id = :id")
    Set<UserRole> findAllRollByUserId(@Param("id") Integer userId);

    @Query("SELECT ur.role FROM UserRole ur WHERE ur.user.id = :id")
    Set<Role> findRolesByUserId(@Param("id") Integer id);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> getUserById(@Param("id") Integer id);
}
