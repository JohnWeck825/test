package com.myshop.adminpage.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

//    @PostConstruct
//    public void initFirebase() throws IOException {
//        FileInputStream serviceAccount = new FileInputStream("src/main/resources/myshop-ebeb4-firebase-adminsdk-boj7s-3edc0e863b.json");
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setStorageBucket("myshop-ebeb4.appspot.com")
//                .build();
//
//        FirebaseApp.initializeApp(options);
//    }

    @PostConstruct
    public void initFirebase() throws IOException {
        // Sử dụng ClassLoader để load tệp JSON từ classpath
        InputStream serviceAccount = getClass().getClassLoader()
                .getResourceAsStream("myshop-ebeb4-firebase-adminsdk-boj7s-3edc0e863b.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("myshop-ebeb4.appspot.com")
                .build();

        // Kiểm tra xem FirebaseApp đã được khởi tạo chưa
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

}
