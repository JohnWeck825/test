1. Shallow Copy:

Hiển thị danh sách sản phẩm: Khi người dùng duyệt danh mục sản phẩm, ứng dụng sẽ lấy dữ liệu sản phẩm từ database. Để
hiển thị thông tin sản phẩm trên trang web, không cần thiết phải tải toàn bộ thông tin chi tiết của từng sản phẩm (mô tả
dài, thông số kỹ thuật đầy đủ...). Lúc này, shallow copy được sử dụng để tạo ra các DTO (Data Transfer Object) chứa các
thông tin cần thiết cho việc hiển thị (tên, hình ảnh, giá...). DTO này sẽ tham chiếu đến đối tượng sản phẩm gốc trong
database. Việc này giúp giảm lượng dữ liệu truyền tải giữa server và client, tăng tốc độ tải trang.
Thêm sản phẩm vào giỏ hàng (ban đầu): Khi người dùng click nút "Thêm vào giỏ hàng", một shallow copy của sản phẩm được
tạo và thêm vào giỏ hàng của người dùng. Shallow copy ở đây giúp tiết kiệm tài nguyên và phản ánh thay đổi giá sản phẩm
nếu có. Tuy nhiên, khi người dùng tiến hành đặt hàng, cần phải chuyển sang deep copy (xem phần deep copy bên dưới).
Bộ lọc và sắp xếp sản phẩm: Khi người dùng sử dụng bộ lọc hoặc sắp xếp sản phẩm, shallow copy được sử dụng để tạo ra một
danh sách sản phẩm mới dựa trên tiêu chí lọc/sắp xếp. Danh sách này chỉ chứa các tham chiếu đến sản phẩm gốc, giúp tiết
kiệm tài nguyên và tăng hiệu suất.
So sánh sản phẩm (giai đoạn đầu): Khi người dùng chọn sản phẩm để so sánh, ban đầu có thể dùng shallow copy để hiển thị
thông tin cơ bản của sản phẩm trong bảng so sánh.

2. Deep Copy:

Đặt hàng: Khi người dùng xác nhận đặt hàng, một deep copy của các sản phẩm trong giỏ hàng được tạo ra để lưu trữ thông
tin chi tiết của đơn hàng. Điều này đảm bảo rằng nếu thông tin sản phẩm (giá, mô tả...) thay đổi sau khi đặt hàng, thông
tin trong đơn hàng vẫn giữ nguyên vẹn. Deep copy ở đây rất quan trọng để đảm bảo tính toàn vẹn dữ liệu của đơn hàng.
Lưu lịch sử mua hàng: Tương tự như đơn hàng, khi lưu lịch sử mua hàng của người dùng, deep copy của sản phẩm được sử
dụng để lưu trữ thông tin sản phẩm tại thời điểm mua.
So sánh sản phẩm (giai đoạn tùy chỉnh): Nếu trang web cho phép người dùng tùy chỉnh sản phẩm trước khi so sánh (ví dụ:
chọn màu sắc, dung lượng bộ nhớ), deep copy được sử dụng để tạo ra các bản sao của sản phẩm gốc. Người dùng có thể tùy
chỉnh các bản sao này mà không ảnh hưởng đến sản phẩm gốc.
Chỉnh sửa sản phẩm (trong admin panel): Khi quản trị viên chỉnh sửa thông tin sản phẩm, deep copy của sản phẩm được tạo
ra để quản trị viên có thể xem trước các thay đổi mà không ảnh hưởng đến sản phẩm đang hiển thị trên trang web. Sau khi
quản trị viên lưu thay đổi, bản sao sẽ được lưu vào database.
Sao lưu dữ liệu: Deep copy được sử dụng để sao lưu dữ liệu sản phẩm. Bản sao lưu sẽ chứa toàn bộ thông tin của sản phẩm,
đảm bảo dữ liệu được khôi phục hoàn toàn trong trường hợp mất mát dữ liệu.
Ví dụ cụ thể:

Người dùng thêm một chiếc iPhone 13 vào giỏ hàng. Lúc này, một shallow copy của iPhone 13 được tạo ra trong giỏ hàng.
Nếu giá của iPhone 13 thay đổi trên hệ thống, giá trong giỏ hàng cũng sẽ thay đổi theo. Tuy nhiên, khi người dùng đặt
hàng, một deep copy của iPhone 13 (với giá tại thời điểm đặt hàng) được tạo ra và lưu vào đơn hàng. Ngay cả khi giá
iPhone 13 thay đổi sau đó, giá trong đơn hàng vẫn giữ nguyên.

Hy vọng những ví dụ này giúp bạn hiểu rõ hơn về ứng dụng thực tế của shallow copy và deep copy trong một web tech-shop.

1. Shallow Copy:

Giỏ hàng (Shopping Cart): Khi một sản phẩm được thêm vào giỏ hàng, bạn có thể sử dụng shallow copy để tạo một mục trong
giỏ hàng. Mục này sẽ tham chiếu đến sản phẩm gốc trong danh mục sản phẩm. Việc này tiết kiệm bộ nhớ và hiệu năng vì bạn
không cần sao chép toàn bộ thông tin sản phẩm. Nếu giá của sản phẩm gốc thay đổi, giá trong giỏ hàng cũng sẽ tự động cập
nhật.

// Sản phẩm
Product iphone = new Product("iPhone 13", 1000);

// Thêm vào giỏ hàng (shallow copy)
CartItem cartItem = new CartItem(iphone, 1); // quantity = 1

// Thay đổi giá sản phẩm gốc
iphone.setPrice(1200);

System.out.println(cartItem.getProduct().getPrice()); // Output: 1200 (giá trong giỏ hàng cũng thay đổi)
Hiển thị danh sách sản phẩm: Khi hiển thị danh sách sản phẩm trên trang web, bạn có thể sử dụng shallow copy để tạo các
đối tượng DTO (Data Transfer Object) từ các entity sản phẩm. DTO chỉ chứa các thông tin cần thiết để hiển thị, và chúng
tham chiếu đến các đối tượng sản phẩm gốc.

2. Deep Copy:

Đặt hàng (Order): Khi khách hàng đặt hàng, bạn cần tạo một deep copy của các mục trong giỏ hàng để lưu trữ thông tin chi
tiết về đơn hàng. Việc này đảm bảo rằng nếu thông tin sản phẩm (như giá) thay đổi sau khi đặt hàng, thông tin trong đơn
hàng vẫn giữ nguyên.

// Giỏ hàng
List<CartItem> cartItems = ...;

// Tạo đơn hàng (deep copy)
Order order = new Order();
for (CartItem cartItem : cartItems) {
Product productCopy = new Product(cartItem.getProduct().getName(), cartItem.getProduct().getPrice()); // Deep copy sản
phẩm
OrderItem orderItem = new OrderItem(productCopy, cartItem.getQuantity());
order.addItem(orderItem);
}

// Thay đổi giá sản phẩm gốc
iphone.setPrice(1500);

System.out.println(order.getItems().get(0).getProduct().getPrice()); // Output: 1200 (giá trong đơn hàng không thay đổi)
Lưu trữ lịch sử sản phẩm: Nếu bạn cần lưu trữ lịch sử thay đổi của sản phẩm (ví dụ: giá, mô tả), bạn cần tạo deep copy
của sản phẩm tại mỗi thời điểm thay đổi.
Chức năng so sánh sản phẩm: Khi người dùng so sánh hai sản phẩm, bạn có thể tạo deep copy của các sản phẩm để người dùng
có thể tùy chỉnh (ví dụ: chọn cấu hình) mà không ảnh hưởng đến sản phẩm gốc.
Tóm lại:

Shallow copy hữu ích khi bạn cần tiết kiệm bộ nhớ và hiệu năng, và bạn muốn các thay đổi trên đối tượng gốc được phản
ánh trong bản sao.
Deep copy cần thiết khi bạn cần lưu trữ một bản sao độc lập của đối tượng, không bị ảnh hưởng bởi các thay đổi trên đối
tượng gốc. Việc lựa chọn giữa shallow copy và deep copy phụ thuộc vào yêu cầu cụ thể của ứng dụng.

Dưới đây là một số thao tác khác trong một web tech-shop có thể sử dụng shallow copy và deep copy:

Shallow Copy:

Phân trang sản phẩm: Khi hiển thị danh sách sản phẩm dài, thường sử dụng phân trang để hiển thị từng phần nhỏ. Shallow
copy có thể được sử dụng để tạo ra danh sách sản phẩm cho mỗi trang, chỉ chứa các tham chiếu đến sản phẩm gốc.
Lưu sản phẩm yêu thích: Khi người dùng lưu sản phẩm vào danh sách yêu thích, có thể sử dụng shallow copy để lưu trữ ID
của sản phẩm. Khi hiển thị danh sách yêu thích, ứng dụng sẽ lấy thông tin sản phẩm từ database dựa trên ID.
Tìm kiếm sản phẩm: Kết quả tìm kiếm sản phẩm có thể được biểu diễn bằng một danh sách shallow copy, chỉ chứa các tham
chiếu đến sản phẩm gốc.
Hiển thị sản phẩm liên quan: Khi hiển thị sản phẩm liên quan, có thể sử dụng shallow copy để hiển thị thông tin cơ bản
của các sản phẩm.
Gợi ý sản phẩm: Các gợi ý sản phẩm (ví dụ: "Sản phẩm bạn có thể thích") có thể được tạo bằng shallow copy, chỉ chứa
thông tin cần thiết để hiển thị.
Deep Copy:

Tạo phiếu giảm giá (coupon) áp dụng cho sản phẩm cụ thể: Khi tạo coupon áp dụng cho một sản phẩm cụ thể, cần deep copy
sản phẩm để lưu trữ thông tin sản phẩm tại thời điểm tạo coupon. Điều này đảm bảo rằng nếu thông tin sản phẩm thay đổi
sau này, coupon vẫn áp dụng đúng cho sản phẩm ban đầu.
Quản lý phiên bản sản phẩm: Nếu tech-shop có chức năng quản lý phiên bản sản phẩm, deep copy được sử dụng để tạo ra các
phiên bản mới của sản phẩm. Mỗi phiên bản sẽ là một bản sao độc lập của phiên bản gốc.
Xuất báo cáo: Khi xuất báo cáo về sản phẩm (ví dụ: báo cáo doanh số), deep copy được sử dụng để tạo ra một snapshot của
dữ liệu sản phẩm tại thời điểm xuất báo cáo.
Phân tích dữ liệu sản phẩm: Deep copy có thể được sử dụng để tạo ra các bản sao của dữ liệu sản phẩm cho mục đích phân
tích, mà không ảnh hưởng đến dữ liệu gốc.
Cá nhân hóa trải nghiệm người dùng: Deep copy có thể được sử dụng để tạo ra các bản sao của dữ liệu sản phẩm cho từng
người dùng, cho phép cá nhân hóa trải nghiệm người dùng (ví dụ: hiển thị các sản phẩm được đề xuất dựa trên lịch sử mua
hàng).
Lưu ý: Việc lựa chọn giữa shallow copy và deep copy phụ thuộc vào yêu cầu cụ thể của từng thao tác. Nên sử dụng shallow
copy khi có thể để tiết kiệm tài nguyên, và chỉ sử dụng deep copy khi cần thiết để đảm bảo tính toàn vẹn dữ liệu.