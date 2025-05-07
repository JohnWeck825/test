package com.myshop.adminpage.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        String password = "123456";
        String hash = "$10$.ri4tFmyMaJ48RwbY42/CuodTolUrFxswuvvgwUJKtZyMFqaPvd7u";
        System.out.println(new BCryptPasswordEncoder().encode(password));
//        System.out.println(new );
    }
}
