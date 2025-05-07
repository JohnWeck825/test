package com.myshop.adminpage.utils;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.InputStream;
import java.util.*;

public class FirebaseStorageUtil {
    private static final String BUCKET_NAME = "myshop-ebeb4.appspot.com";

    public static void uploadFile(String path, String fileName, InputStream inputStream) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(BUCKET_NAME, path + "/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        storage.create(blobInfo, inputStream);
    }

    public static List<String> listFolder(String folderPath) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Page<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(folderPath + "/"));
        List<String> fileNames = new ArrayList<>();
        for (Blob blob : blobs.iterateAll()) {
            fileNames.add(blob.getName());
        }
        return fileNames;
    }

    public static void deleteFile(String filePath) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
        storage.delete(blobId);
    }

    public static void removeFolder(String folderPath) {
        List<String> files = listFolder(folderPath);
        for (String file : files) {
            deleteFile(file);
        }
    }
}
