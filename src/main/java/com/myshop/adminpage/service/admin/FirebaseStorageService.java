package com.myshop.adminpage.service.admin;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;

@Service
public class FirebaseStorageService {

    private final Storage storage;

    public FirebaseStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String getImageUrl(String filePath) {
        Blob blob = storage.get("myshop-ebeb4.appspot.com", filePath);  // Lấy ảnh từ bucket

        if (blob != null && blob.exists()) {
            // Trả về URL công khai của ảnh trên Firebase
            return String.format("https://storage.googleapis.com/%s/%s",
                    "myshop-ebeb4.appspot.com", filePath);
        } else {
            return "/images/default-image.jpg";  // URL ảnh mặc định nếu không tìm thấy
        }
    }
}
