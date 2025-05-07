Giải thích về lỗi mới bạn gặp phải:

There was an unexpected error (type=Internal Server Error, status=500).
Cannot invoke "com.google.cloud.storage.Blob.delete(
com.google.cloud.storage.Blob$BlobSourceOption[])" because the return value of "com.google.cloud.storage.Bucket.get(String, com.google.cloud.storage.Storage$
BlobGetOption[])" is null
java.lang.NullPointerException: Cannot invoke "com.google.cloud.storage.Blob.delete(
com.google.cloud.storage.Blob$BlobSourceOption[])" because the return value of "com.google.cloud.storage.Bucket.get(String, com.google.cloud.storage.Storage$
BlobGetOption[])" is null
Nguyên nhân của lỗi:

Lỗi này xảy ra khi bạn cố gắng gọi phương thức delete() trên một đối tượng Blob nhưng đối tượng này lại là null. Cụ thể,
lệnh:

StorageClient.getInstance().bucket().get(uploadDir).delete();
Trong trường hợp này, phương thức bucket().get(uploadDir) trả về null vì không tìm thấy Blob tương ứng với đường dẫn
uploadDir trong bucket của bạn. Điều này dẫn đến NullPointerException khi bạn cố gắng gọi delete() trên null.

Hướng giải quyết:

Kiểm tra đường dẫn của Blob:
Trong mã của bạn, bạn đang lấy Blob với đường dẫn uploadDir, nhưng uploadDir có thể là một "thư mục" (prefix) chứ không
phải là đường dẫn đầy đủ của tệp tin cần xóa.
Bạn cần chắc chắn rằng đường dẫn bạn dùng để lấy Blob chính xác là đường dẫn của tệp tin cần xóa.
Lưu lại tên tệp tin cũ trước khi ghi đè:
Khi bạn upload tệp tin mới, bạn đã set category.setPhotoUrl(fileName); và lưu category. Điều này ghi đè lên tên tệp tin
cũ.
Bạn cần lưu lại tên tệp tin cũ trước khi ghi đè, để có thể xóa tệp tin cũ trong Firebase Storage.
Kiểm tra Blob có tồn tại trước khi xóa:
Trước khi gọi blob.delete();, bạn nên kiểm tra xem blob có khác null hay không.
Nếu blob là null, nghĩa là tệp tin cần xóa không tồn tại, và bạn không cần thực hiện xóa.
Cập nhật mã nguồn:

Dưới đây là cập nhật cho phương thức controller của bạn:

@PostMapping("/save")
public String addCategory(@ModelAttribute Category category, @RequestParam("imageFile") MultipartFile multipartFile)
throws Exception {
if (!multipartFile.isEmpty()) {
// Lưu lại tên tệp tin cũ trước khi ghi đè
String oldFileName = category.getPhotoUrl();

        String fileName = multipartFile.getOriginalFilename();
        category.setPhotoUrl(fileName); // Lưu tên tệp vào thuộc tính photoUrl

        // Lưu category trước khi upload file
        this.categoryServiceInf.save(category);

        String uploadDir = "CategoryImages/" + category.getId();

        // Xóa tệp tin cũ nếu có
        if (oldFileName != null && !oldFileName.isEmpty()) {
            String oldFilePath = uploadDir + "/" + oldFileName;
            Blob blob = StorageClient.getInstance().bucket().get(oldFilePath);
            if (blob != null) {
                blob.delete();
            }
        }

        // Tạo BlobId và BlobInfo để tải tệp tin mới lên Firebase Storage
        String newFilePath = uploadDir + "/" + fileName;
        BlobId blobId = BlobId.of("myshop-ebeb4.appspot.com", newFilePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(multipartFile.getContentType())
                .build();

        // Upload tệp tin mới lên Firebase Storage
        StorageClient.getInstance().bucket().create(blobInfo, multipartFile.getBytes());
    } else {
        // Nếu không có tệp tin mới, giữ nguyên photoUrl hiện tại
        this.categoryServiceInf.save(category);
    }
    return "redirect:/admin-category";

}
Giải thích chi tiết:

Lưu lại tên tệp tin cũ:

String oldFileName = category.getPhotoUrl();
Bạn cần lưu lại tên tệp tin cũ trước khi ghi đè, để có thể sử dụng nó khi xóa tệp tin cũ trong Firebase Storage.
Sau khi set tên tệp mới và lưu category:

String fileName = multipartFile.getOriginalFilename();
category.setPhotoUrl(fileName);
this.categoryServiceInf.save(category);
Bạn lưu tên tệp tin mới vào category và lưu category vào cơ sở dữ liệu.
Xóa tệp tin cũ nếu có:

if (oldFileName != null && !oldFileName.isEmpty()) {
String oldFilePath = uploadDir + "/" + oldFileName;
Blob blob = StorageClient.getInstance().bucket().get(oldFilePath);
if (blob != null) {
blob.delete();
}
}
Chúng ta xây dựng đường dẫn đầy đủ tới tệp tin cũ (oldFilePath) bằng cách kết hợp uploadDir và oldFileName.
Sau đó, chúng ta lấy Blob tương ứng với oldFilePath. Nếu blob khác null, tức là tệp tin tồn tại, chúng ta tiến hành xóa
nó.
Quan trọng: Chúng ta kiểm tra blob != null trước khi gọi blob.delete(); để tránh NullPointerException.
Tải lên tệp tin mới:

String newFilePath = uploadDir + "/" + fileName;
BlobId blobId = BlobId.of("myshop-ebeb4.appspot.com", newFilePath);
BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
.setContentType(multipartFile.getContentType())
.build();

// Upload tệp tin mới lên Firebase Storage
StorageClient.getInstance().bucket().create(blobInfo, multipartFile.getBytes());
Chúng ta xây dựng đường dẫn đầy đủ tới tệp tin mới (newFilePath) và sử dụng nó để tạo BlobId và BlobInfo.
Sau đó, chúng ta upload tệp tin mới lên Firebase Storage.
Lưu ý khác:

Về việc lưu tệp tin và đường dẫn:
Hãy đảm bảo rằng bạn đặt tên tệp tin một cách duy nhất (unique). Nếu bạn chỉ sử dụng
multipartFile.getOriginalFilename(), có thể xảy ra xung đột nếu người dùng tải lên các tệp tin trùng tên.
Bạn có thể sử dụng UUID hoặc timestamp để tạo tên tệp tin duy nhất.
Ví dụ:

String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
Kiểm tra các exception khác:
Nếu sau khi chỉnh sửa mà vẫn gặp lỗi, bạn có thể thêm các log hoặc sử dụng try-catch để bắt và log chi tiết lỗi.
Kiểm tra phân quyền và cấu hình Firebase:
Đảm bảo rằng tài khoản dịch vụ Firebase của bạn có quyền truy cập và xóa các tệp tin trong bucket Firebase Storage.
Kiểm tra xem cấu hình Firebase đã đúng chưa, bao gồm tên bucket và các thông số xác thực.
Ví dụ hoàn chỉnh (cập nhật):

@PostMapping("/save")
public String addCategory(@ModelAttribute Category category, @RequestParam("imageFile") MultipartFile multipartFile)
throws Exception {
if (!multipartFile.isEmpty()) {
// Lưu lại tên tệp tin cũ trước khi ghi đè
String oldFileName = category.getPhotoUrl();

        // Tạo tên tệp tin mới (có thể sử dụng UUID để đảm bảo duy nhất)
        String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
        category.setPhotoUrl(fileName); // Lưu tên tệp vào thuộc tính photoUrl

        // Lưu category trước khi upload file
        this.categoryServiceInf.save(category);

        String uploadDir = "CategoryImages/" + category.getId();

        // Xóa tệp tin cũ nếu có
        if (oldFileName != null && !oldFileName.isEmpty()) {
            String oldFilePath = uploadDir + "/" + oldFileName;
            Blob blob = StorageClient.getInstance().bucket().get(oldFilePath);
            if (blob != null) {
                blob.delete();
            }
        }

        // Tạo BlobId và BlobInfo để tải tệp tin mới lên Firebase Storage
        String newFilePath = uploadDir + "/" + fileName;
        BlobId blobId = BlobId.of("myshop-ebeb4.appspot.com", newFilePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(multipartFile.getContentType())
                .build();

        // Upload tệp tin mới lên Firebase Storage
        StorageClient.getInstance().bucket().create(blobInfo, multipartFile.getBytes());
    } else {
        // Nếu không có tệp tin mới, giữ nguyên photoUrl hiện tại
        this.categoryServiceInf.save(category);
    }
    return "redirect:/admin-category";

}
Kết luận:

Lỗi NullPointerException xảy ra do bạn cố gắng gọi delete() trên một Blob là null. Điều này là do bạn lấy Blob với đường
dẫn không chính xác hoặc tệp tin không tồn tại.
Bằng cách lưu lại tên tệp tin cũ, kiểm tra xem Blob có tồn tại trước khi xóa, và đảm bảo đường dẫn tới tệp tin là chính
xác, bạn sẽ khắc phục được lỗi này.
Ngoài ra, hãy xem xét việc đặt tên tệp tin một cách duy nhất để tránh xung đột và rắc rối khi quản lý tệp tin trên
Firebase Storage.
Hy vọng những giải thích và hướng dẫn trên sẽ giúp bạn khắc phục được lỗi. Nếu bạn còn thắc mắc hoặc cần hỗ trợ thêm,
hãy cho tôi biết!