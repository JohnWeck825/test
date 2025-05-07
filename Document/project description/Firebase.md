Mapping API POST:

@PostMapping("/save")
Đây là một phương thức HTTP POST được sử dụng để xử lý yêu cầu thường được gửi từ máy khách đến máy chủ.
Tham số Đối tượng Model:

public String addCategory(@ModelAttribute Category category, @RequestParam("imageFile") MultipartFile multipartFile)
throws Exception {
Có hai tham số trong phương thức này:
category: đоạn này là một đối tượng Category được điều khiền về máy chủ từ máy khách.
multipartFile: là một tệp đính kèm gửi từ máy khách.
Kiểm tra Tệp Đính kèm:

if (!multipartFile.isEmpty()) {
Nếu tệp đính kèm (imageFile) không trống, tiến hành lưu trữ tệp đính kèm vào Firebase Storage.
Cập Nhật Tên File:

String oldFileName = category.getPhotoUrl();
String fileName = multipartFile.getOriginalFilename();
category.setPhotoUrl(fileName);
Lấy tên của hình ảnh cũ oldFileName từ category.getPhotoUrl().
Lấy tên của hình ảnh mới từ multipartFile.getOriginalFilename().
Cập nhật tên hình ảnh cũ thành tên mới vào category.
Lưu Danh mục:

categoryServiceInf.save(category);
Lưu danh mục cập nhật lại lên cơ sở dữ liệu.
Đường dẫn Ở Firebase Storage:

String uploadDir = "CategoryImages/" + category.getId();
Tạo đường dẫn đặt file trong Firebase Storage, sử dụng ID của danh mục.
Xóa Tệp Tin Cũ:

if(oldFileName != null && !oldFileName.isEmpty()) {
String oldUploadDir = uploadDir + "/" + oldFileName;
Blob blob = StorageClient.getInstance().bucket().get(oldUploadDir);
if (blob != null) {
blob.delete();
}
}
Nếu hình ảnh cũ tồn tại, xóa tệp tin cũ trên Firebase Storage.
Tạo BlobId và BlobInfo:

String newUploadDir = uploadDir + "/" + fileName;
BlobId blobId = BlobId.of("myshop-ebeb4.appspot.com", newUploadDir);
BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
.setContentType(multipartFile.getContentType())
.build();
Tạo BlobId và BlobInfo để tạo tệp trên Firebase Storage.
Upload File lên Firebase Storage:

StorageClient.getInstance().bucket().create(String.valueOf(blobInfo), multipartFile.getBytes());
Upload tệp mới lên Firebase Storage.
Không Có Tệp Đính kèm:

else {
if (category.getPhotoUrl().isEmpty()) {
category.setPhotoUrl(null);
}
categoryServiceInf.save(category);
}
Nếu không có tệp đính kèm, kiểm tra và cập nhật giá trị ảnh cũ của danh mục, sau đó lưu lại danh mục.
Chuyển Đi:

return "redirect:/admin-category";
Trả về trang quản trị danh mục sau khi xử lý xong.
Trong tổng quá trình, chúng ta lưu trữ hình ảnh của danh mục về Firebase Storage và cập nhật tên hình ảnh cũ thành tên
hình ảnh mới. Nếu không có tệp đính kèm, chúng ta sẽ kiểm tra và cập nhật giá trị ảnh cũ của danh mục. Cuối cùng, chúng
ta chuyển đi lại sang trang quản trị danh mục.