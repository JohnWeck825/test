package com.myshop.adminpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyShopAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopAdminApplication.class, args);
    }

}
