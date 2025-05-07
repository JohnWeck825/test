package com.myshop.adminpage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String firstName;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false, unique = true)
    private String userName;

    @Column(length = 150, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;


    private boolean enabled;

    @Column(length = 200)
    private String photoUrl;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private java.util.Set<Role> roles = new java.util.LinkedHashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new LinkedHashSet<>();



    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
//    Phương thức trả về họ tên đầy đủ của người dùng bằng cách kết hợp hai thuộc tính firstName (tên) và lastName (họ).
//    @Transient: phương thức này không được ánh xạ vào cơ sở dữ liệu.

}
