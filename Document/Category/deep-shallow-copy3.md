1. Quản Lý Giỏ Hàng (Shopping Cart)
   Tình Huống:
   Người dùng thêm sản phẩm vào giỏ hàng. Khi thêm sản phẩm, bạn muốn lưu thông tin sản phẩm tại thời điểm thêm vào giỏ
   hàng để đảm bảo rằng, dù sản phẩm gốc bị thay đổi (giá cả, mô tả, v.v.), giỏ hàng vẫn giữ nguyên thông tin ban đầu.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo một bản sao độc lập của sản phẩm khi thêm vào giỏ hàng.

Lý Do Sử Dụng Sao Chép Sâu:
Độc lập Dữ Liệu: Đảm bảo rằng thông tin sản phẩm trong giỏ hàng không bị ảnh hưởng bởi các thay đổi của sản phẩm gốc sau
này.
An Toàn Dữ Liệu: Ngăn ngừa các thay đổi không mong muốn từ các quá trình khác trong ứng dụng.
Ví Dụ Mã:
Entity Product:

@Entity
@Table(name = "products")
public class Product {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Getters và Setters

    /**
     * Sao chép sâu một Product.
     */
    public static Product deepCopy(Product original) {
        if (original == null) return null;

        Product copy = new Product();
        copy.setName(original.getName());
        copy.setDescription(original.getDescription());
        copy.setPrice(original.getPrice());

        // Sao chép sâu Category nếu cần (tùy thuộc vào yêu cầu)
        // Giả sử chúng ta chỉ tham chiếu Category, không sao chép sâu
        copy.setCategory(original.getCategory());

        return copy;
    }

}
Entity CartItem:

@Entity
@Table(name = "cart_items")
public class CartItem {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private Integer quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_copy_id")
    private Product productCopy;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Getters và Setters

}
Service CartService:

@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * Thêm sản phẩm vào giỏ hàng.
     *
     * @param cartId    ID của giỏ hàng
     * @param productId ID của sản phẩm
     * @param quantity  Số lượng
     * @return CartItem đã thêm
     */
    public CartItem addToCart(Integer cartId, Integer productId, Integer quantity) {
        // Tìm sản phẩm gốc
        Product originalProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));

        // Tạo bản sao sâu của sản phẩm
        Product productCopy = Product.deepCopy(originalProduct);

        // Tìm giỏ hàng
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Giỏ hàng với ID: " + cartId));

        // Tạo CartItem mới
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItem.setProductCopy(productCopy);
        cartItem.setCart(cart);

        // Lưu CartItem
        return cartItemRepository.save(cartItem);
    }

}
Giải Thích:
Sơ Đồ Các Thực Thể:
Product: Đại diện cho sản phẩm gốc trong cửa hàng.
CartItem: Đại diện cho sản phẩm trong giỏ hàng, bao gồm bản sao của sản phẩm gốc (productCopy) và số lượng (quantity).
Quá Trình Thêm Sản Phẩm Vào Giỏ Hàng:
Sao chép sâu Product: Khi thêm sản phẩm vào giỏ hàng, chúng ta tạo một bản sao sâu của sản phẩm gốc bằng phương thức
Product.deepCopy(originalProduct). Điều này đảm bảo rằng thông tin sản phẩm trong giỏ hàng không bị thay đổi nếu sản
phẩm gốc bị cập nhật.
Lưu CartItem: Thêm CartItem mới vào giỏ hàng với bản sao sản phẩm và số lượng được chỉ định.
Lợi Ích:
Dữ Liệu Bảo Toàn: Thông tin sản phẩm trong giỏ hàng không bị thay đổi bởi các cập nhật sau này của sản phẩm gốc.
Trải Nghiệm Người Dùng Tốt Hơn: Khi xem giỏ hàng, người dùng thấy thông tin chính xác về sản phẩm tại thời điểm thêm vào
giỏ hàng.

2. So Sánh Hai Sản Phẩm (Product Comparison)
   Tình Huống:
   Người dùng chọn hai hoặc nhiều sản phẩm để so sánh. Mục tiêu là hiển thị các thông tin chi tiết của từng sản phẩm
   cạnh nhau để người dùng dễ dàng đưa ra quyết định mua hàng.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) để thi hành so sánh sản phẩm, vì bạn thường chỉ cần đọc dữ liệu mà không cần tạo
bản sao độc lập.

Lý Do Sử Dụng Sao Chép Nông:
Hiệu Suất Tốt: Sao chép nông nhanh hơn so với sao chép sâu, đặc biệt khi chỉ cần thao tác đọc dữ liệu.
Dễ Dàng Quản Lý: Không cần tạo các bản sao riêng biệt cho các sản phẩm so sánh.
Ví Dụ Mã:
Service ProductComparisonService:

@Service
public class ProductComparisonService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * So sánh hai sản phẩm.
     *
     * @param productId1 ID của sản phẩm 1
     * @param productId2 ID của sản phẩm 2
     * @return Danh sách các sản phẩm được sao chép nông để so sánh
     */
    public List<Product> compareProducts(Integer productId1, Integer productId2) {
        Product product1 = productRepository.findById(productId1)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + productId1));

        Product product2 = productRepository.findById(productId2)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + productId2));

        // Sao chép nông sản phẩm cho việc so sánh
        Product copy1 = Product.CopyShallow(product1);
        Product copy2 = Product.CopyShallow(product2);

        return Arrays.asList(copy1, copy2);
    }

}
Phương Thức Sao Chép Nông Product.CopyShallow:

public static Product CopyShallow(Product original) {
if (original == null) return null;

    Product copy = new Product();
    copy.setId(original.getId());
    copy.setName(original.getName());
    copy.setDescription(original.getDescription());
    copy.setPrice(original.getPrice());
    copy.setCategory(original.getCategory()); // Chỉ sao chép tham chiếu

    return copy;

}
Giải Thích:
Quá Trình So Sánh:
Lấy Sản Phẩm Gốc: Tìm kiếm sản phẩm theo ID.
Sao chép Nông: Tạo bản sao nông của từng sản phẩm để sử dụng cho việc so sánh. Điều này chỉ bao gồm các giá trị thuộc
tính cơ bản và tham chiếu, không cần tạo bản sao độc lập.
Hiển Thị So Sánh:
Trực tiếp hiển thị các thông tin từ bản sao nông của sản phẩm, vì bạn không cần thay đổi dữ liệu trong quá trình so
sánh.
Lợi Ích:
Hiệu Suất Cao: Sao chép nông nhanh hơn và tiết kiệm bộ nhớ hơn khi chỉ cần đọc dữ liệu.
Dễ Dàng Tích Hợp: Mô hình đơn giản, dễ dàng tích hợp vào các thành phần hiển thị so sánh trên giao diện người dùng.

3. Lịch Sử Đơn Hàng (Order History)
   Tình Huống:
   Người dùng muốn xem lịch sử đơn hàng của mình. Mỗi đơn hàng bao gồm thông tin về thời gian, sản phẩm đã mua, số
   lượng, giá cả tại thời điểm mua.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) hoặc sao chép sâu (deep copy) tùy thuộc vào yêu cầu cụ thể về cách dữ liệu được
hiển thị và bảo vệ thông tin đơn hàng.

Lý Do Sử Dụng:
Sao chép nông: Nếu bạn chỉ cần hiển thị thông tin mà không thay đổi dữ liệu.
Sao chép sâu: Nếu bạn cần xử lý độc lập các đối tượng đơn hàng như thực hiện các thao tác phân tích, báo cáo.
Ví Dụ Mã:
Entity Order:

@Entity
@Table(name = "orders")
public class Order {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private Date orderDate;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    // Getters và Setters

    /**
     * Sao chép nông một Order.
     */
    public static Order copyShallow(Order original) {
        if (original == null) return null;

        Order copy = new Order();
        copy.setId(original.getId());
        copy.setOrderDate(original.getOrderDate());
        copy.setTotalAmount(original.getTotalAmount());
        copy.setUser(original.getUser());
        copy.setOrderItems(original.getOrderItems()); // Sao chép tham chiếu

        return copy;
    }

    /**
     * Sao chép sâu một Order.
     */
    public static Order copyDeep(Order original) {
        if (original == null) return null;

        Order copy = new Order();
        // Không sao chép id để Hibernate/JPA tự gán
        copy.setOrderDate(original.getOrderDate());
        copy.setTotalAmount(original.getTotalAmount());
        copy.setUser(original.getUser());

        // Sao chép sâu các OrderItem
        Set<OrderItem> copiedItems = new HashSet<>();
        for (OrderItem item : original.getOrderItems()) {
            OrderItem copiedItem = OrderItem.copyDeep(item);
            copiedItem.setOrder(copy); // Thiết lập lại quan hệ cha
            copiedItems.add(copiedItem);
        }
        copy.setOrderItems(copiedItems);

        return copy;
    }

}
Entity OrderItem:

@Entity
@Table(name = "order_items")
public class OrderItem {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private Integer quantity;

    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Getters và Setters

    /**
     * Sao chép nông một OrderItem.
     */
    public static OrderItem copyShallow(OrderItem original) {
        if (original == null) return null;

        OrderItem copy = new OrderItem();
        copy.setId(original.getId());
        copy.setQuantity(original.getQuantity());
        copy.setUnitPrice(original.getUnitPrice());
        copy.setOrder(original.getOrder());
        copy.setProduct(original.getProduct());

        return copy;
    }

    /**
     * Sao chép sâu một OrderItem.
     */
    public static OrderItem copyDeep(OrderItem original) {
        if (original == null) return null;

        OrderItem copy = new OrderItem();
        // Không sao chép id để Hibernate/JPA tự gán
        copy.setQuantity(original.getQuantity());
        copy.setUnitPrice(original.getUnitPrice());
        copy.setProduct(original.getProduct());

        // Không sao chép lại Order để tránh vòng lặp
        copy.setOrder(null); // Sẽ được thiết lập lại sau

        return copy;
    }

}
Service OrderService:

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Lấy lịch sử đơn hàng cho người dùng.
     *
     * @param userId ID của người dùng
     * @return Danh sách các Order đã được sao chép nông để hiển thị
     */
    public List<Order> getOrderHistory(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        // Sao chép nông để hiển thị
        return orders.stream()
                .map(Order::copyShallow)
                .collect(Collectors.toList());
    }

    /**
     * Tạo một bao gồm bản sao sâu các đơn hàng cho mục đích phân tích.
     *
     * @param userId ID của người dùng
     * @return Danh sách các Order đã được sao chép sâu
     */
    public List<Order> getOrderHistoryDeepCopy(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        // Sao chép sâu để phân tích hoặc xử lý
        return orders.stream()
                .map(Order::copyDeep)
                .collect(Collectors.toList());
    }

}
Giải Thích:
Sao Chép Nông:
Khi Nào Sử Dụng: Khi chỉ cần hiển thị thông tin đơn hàng cho người dùng mà không cần thay đổi hoặc phân tích dữ liệu.
Cách Thức: Tạo bản sao nông của các đối tượng Order và OrderItem, giữ các tham chiếu đến User và Product.
Sao Chép Sâu:
Khi Nào Sử Dụng: Khi cần thực hiện các thao tác phân tích, báo cáo, hoặc bất kỳ xử lý phức tạp nào yêu cầu các đối tượng
đơn hàng hoàn toàn độc lập.
Cách Thức: Tạo bản sao sâu của các đối tượng Order và OrderItem, thiết lập lại các mối quan hệ cha-con để đảm bảo tính
độc lập.
Lợi Ích:
Bảo Toàn Dữ Liệu: Sao chép nông giúp bạn dễ dàng hiển thị dữ liệu mà không cần lo lắng về việc tạo bản sao độc lập.
Phân Tích Dữ Liệu Hiệu Quả: Sao chép sâu cho phép bạn thực hiện các thao tác phân tích mà không ảnh hưởng đến dữ liệu
gốc.

3. Quản Lý Đơn Hàng (Order Management)
   Tình Huống:
   Quản trị viên của Tech-Shop muốn cập nhật trạng thái đơn hàng (ví dụ: từ "Đang xử lý" sang "Đã giao"). Việc cập nhật
   này cần đảm bảo tính nhất quán của dữ liệu và không làm ảnh hưởng đến các bản ghi khác trong hệ thống.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) khi chỉ cần cập nhật một phần thông tin của đơn hàng mà không ảnh hưởng đến các
thuộc tính liên kết.

Lý Do Sử dụng Sao Chép Nông:
Đơn Giản và Hiệu Quả: Khi cập nhật một thuộc tính cụ thể, không cần phải sao chép toàn bộ đối tượng.
Dễ Dàng Quản Lý: Tránh việc tạo các bản sao không cần thiết, tối ưu hóa hiệu suất.
Ví Dụ Mã:
Service OrderManagementService:

@Service
public class OrderManagementService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Cập nhật trạng thái đơn hàng.
     *
     * @param orderId ID của đơn hàng
     * @param status  Trạng thái mới
     * @return Đơn hàng đã được cập nhật
     */
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Order với ID: " + orderId));

        // Sao chép nông để cập nhật
        Order copyOrder = Order.copyShallow(order);
        copyOrder.setStatus(status); // Cập nhật trạng thái mới

        // Lưu lại đơn hàng
        return orderRepository.save(copyOrder);
    }

}
Giải Thích:
Quá Trình Cập Nhật:
Tìm kiếm Đơn hàng: Truy xuất đơn hàng theo ID từ cơ sở dữ liệu.
Sao chép Nông: Tạo bản sao nông của đơn hàng để thực hiện cập nhật trạng thái.
Cập nhật Trạng thái: Thay đổi thuộc tính status của bản sao copyOrder.
Lưu Đơn hàng: Lưu bản sao đã cập nhật lại vào cơ sở dữ liệu, thay đổi sẽ được phản ánh chỉ trên đơn hàng đó mà không ảnh
hưởng đến các đối tượng liên quan.
Lợi Ích:
Tính Nhất Quán: Chỉ cập nhật thuộc tính cần thiết mà không làm thay đổi các thông tin khác liên quan đến đơn hàng.
An Toàn Dữ Liệu: Bảo vệ dữ liệu liên quan khỏi bị thay đổi không mong muốn.

4. Phân Tích và Báo Cáo (Analytics and Reporting)
   Tình Huống:
   Phân tích dữ liệu về doanh số, lượt bán của các sản phẩm, hoặc hiệu suất của các danh mục sản phẩm. Để thực hiện phân
   tích, bạn cần tạo các bản sao của dữ liệu để xử lý mà không ảnh hưởng đến dữ liệu gốc trong cơ sở dữ liệu.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo các bản sao độc lập của các đối tượng dữ liệu nhằm đảm bảo rằng quá trình phân
tích không thay đổi hoặc ảnh hưởng đến dữ liệu gốc.

Lý Do Sử dụng Sao Chép Sâu:
Độc Lập Trong Phân Tích: Các phép toán và quá trình phân tích không làm thay đổi dữ liệu gốc.
Bảo Toàn Tính Nhất Quán Dữ Liệu: Đảm bảo rằng dữ liệu gốc luôn được giữ nguyên để thực hiện các hóa đơn, báo cáo chính
thức.
Ví Dụ Mã:
Service AnalyticsService:

@Service
public class AnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Phân tích doanh số theo danh mục sản phẩm.
     *
     * @return Bản đồ danh mục sản phẩm đến tổng doanh số
     */
    public Map<String, Double> analyzeSalesByCategory() {
        List<Order> orders = orderRepository.findAll();

        // Sao chép sâu các Order để phân tích
        List<Order> ordersCopy = orders.stream()
                .map(Order::copyDeep)
                .collect(Collectors.toList());

        // Phân tích dữ liệu
        Map<String, Double> salesByCategory = new HashMap<>();

        for (Order order : ordersCopy) {
            for (OrderItem item : order.getOrderItems()) {
                String categoryName = item.getProduct().getCategory().getName();
                Double totalPrice = item.getUnitPrice() * item.getQuantity();

                salesByCategory.put(categoryName,
                        salesByCategory.getOrDefault(categoryName, 0.0) + totalPrice);
            }
        }

        return salesByCategory;
    }

}
Giải Thích:
Quá Trình Phân Tích:
Lấy Dữ Liệu Đơn Hàng: Truy xuất tất cả các đơn hàng từ cơ sở dữ liệu.
Sao chép Sâu: Tạo bản sao sâu của từng đơn hàng để thực hiện phân tích. Điều này giúp đảm bảo rằng các phép toán trên
bản sao không ảnh hưởng đến dữ liệu gốc.
Phân Tích Doanh Số: Tính toán tổng doanh số theo từng danh mục sản phẩm bằng cách lặp qua từng đơn hàng và từng sản phẩm
trong đơn hàng.
Kết Quả Phân Tích:
Map <String, Double>: Bản đồ ánh xạ tên danh mục sản phẩm đến tổng doanh số tương ứng.
Lợi Ích:
Phân Tích An Toàn: Dữ liệu được sao chép sâu giúp đảm bảo tính chính xác và an toàn trong quá trình phân tích.
Hiển Thị Chính Xác: Kết quả phân tích không bị ảnh hưởng bởi các thay đổi trong dữ liệu gốc sau thời điểm phân tích.

5. Quản Lý Wish List
   Tình Huống:
   Người dùng thêm sản phẩm vào danh sách yêu thích (wish list) để có thể xem sau hoặc quyết định mua hàng. Bạn muốn lưu
   trữ thông tin sản phẩm tại thời điểm thêm vào wish list để đảm bảo các thông tin sản phẩm không bị thay đổi sau này.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo bản sao độc lập của sản phẩm khi thêm vào wish list.

Lý Do Sử dụng Sao Chép Sâu:
Độc Lập Dữ Liệu: Thông tin sản phẩm trong wish list không bị ảnh hưởng bởi các thay đổi sau này của sản phẩm gốc.
Lưu Trữ Thông Tin Chính Xác: Đảm bảo rằng, khi người dùng xem wishlist, các thông tin sản phẩm phản ánh chính xác ở thời
điểm đã thêm vào wish list.
Ví Dụ Mã:
Entity WishListItem:

@Entity
@Table(name = "wishlist_items")
public class WishListItem {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_copy_id")
    private Product productCopy;

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    private WishList wishList;

    // Getters và Setters

}
Service WishListService:

@Service
public class WishListService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private WishListItemRepository wishListItemRepository;

    /**
     * Thêm sản phẩm vào wish list.
     *
     * @param wishlistId ID của wish list
     * @param productId  ID của sản phẩm
     * @return WishListItem đã thêm
     */
    public WishListItem addToWishList(Integer wishlistId, Integer productId) {
        // Tìm sản phẩm gốc
        Product originalProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));

        // Tạo bản sao sâu của sản phẩm
        Product productCopy = Product.deepCopy(originalProduct);

        // Tìm wish list
        WishList wishList = wishListRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Wish List với ID: " + wishlistId));

        // Tạo WishListItem mới
        WishListItem wishListItem = new WishListItem();
        wishListItem.setProductCopy(productCopy);
        wishListItem.setWishList(wishList);

        // Lưu WishListItem
        return wishListItemRepository.save(wishListItem);
    }

}
Giải Thích:
Quá Trình Thêm Sản Phẩm Vào Wish List:
Sao chép Sâu Product: Khi thêm sản phẩm vào wish list, một bản sao sâu của sản phẩm được tạo ra để lưu trữ thông tin tại
thời điểm thêm vào wish list.
Lưu WishListItem: Tạo một WishListItem mới chứa bản sao sản phẩm và liên kết với wish list của người dùng.
Mối Quan Hệ giữa Các Thực Thể:
WishList: Đại diện cho danh sách yêu thích của người dùng.
WishListItem: Đại diện cho từng sản phẩm trong wish list, lưu trữ bản sao của sản phẩm (productCopy) để bảo đảm tính
nhất quán.
Lợi Ích:
Bảo Toàn Thông Tin Sản Phẩm: Các thông tin về sản phẩm trong wish list không bị thay đổi bởi các cập nhật sau này của
sản phẩm gốc.
Trải Nghiệm Người Dùng Tốt Hơn: Người dùng có thể xem thông tin chính xác về sản phẩm tại thời điểm thêm vào wish list.

6. Xử Lý Truy Vấn Đặc Biệt (Custom Queries)
   Tình Huống:
   Một số chức năng trong ứng dụng yêu cầu xử lý dữ liệu theo cách riêng biệt mà không làm ảnh hưởng đến dữ liệu gốc,
   như tạo báo cáo tùy chỉnh hoặc thực hiện các phép tính phân tích.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo bản sao dữ liệu khi thực hiện các truy vấn đặc biệt, đảm bảo rằng các phép toán
không làm thay đổi dữ liệu gốc.

Ví Dụ Mã:
Service CustomQueryService:

@Service
public class CustomQueryService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Lấy danh sách sản phẩm dựa trên điều kiện tùy chỉnh và tạo bản sao sâu để phân tích.
     *
     * @param condition Điều kiện truy vấn
     * @return Danh sách sản phẩm đã được sao chép sâu
     */
    public List<Product> getCustomQueriedProducts(CustomCondition condition) {
        // Thực hiện truy vấn tùy chỉnh
        List<Product> products = productRepository.findByCustomCondition(condition);

        // Tạo bản sao sâu để phân tích
        return products.stream()
                .map(Product::deepCopy)
                .collect(Collectors.toList());
    }

}
Giải Thích:
Quá Trình Xử Lý:
Truy Vấn Tùy Chỉnh: Thực hiện một truy vấn tùy chỉnh dựa trên điều kiện cụ thể để lấy danh sách sản phẩm.
Sao chép Sâu: Tạo bản sao sâu của các sản phẩm để thực hiện các phép toán hoặc phân tích mà không làm thay đổi dữ liệu
gốc.
Mục Đích:
Phân Tích Dữ Liệu: Thực hiện các phép tính, báo cáo dựa trên bản sao của dữ liệu.
Bảo Toàn Dữ Liệu Gốc: Đảm bảo rằng dữ liệu gốc trong cơ sở dữ liệu không bị thay đổi trong quá trình phân tích.
Lợi Ích:
An Toàn và Bảo Mật: Các phân tích và xử lý dữ liệu không ảnh hưởng đến dữ liệu gốc, đảm bảo tính nhất quán và bảo mật.
Hiệu Suất Tốt: Phân tích trên bản sao giúp tối ưu hóa hiệu suất bằng cách không làm tải thêm lên dữ liệu gốc.

7. Tóm Tổng Các Trường Hợp Sử Dụng Sao Chép Nông và Sâu trong Tech-Shop
   7.1. Sao Chép Nông (Shallow Copy):
   So Sánh Sản Phẩm: Khi người dùng chọn sản phẩm để so sánh, sao chép nông giúp giữ các tham chiếu đến sản phẩm gốc mà
   không cần tạo các bản sao độc lập.
   Cập Nhật Đơn Hàng: Khi quản trị viên cập nhật trạng thái đơn hàng, sao chép nông cho phép cập nhật một thuộc tính cụ
   thể mà không làm thay đổi toàn bộ đối tượng.
   Hiển Thị Lịch Sử Đơn Hàng: Sao chép nông giúp hiển thị thông tin đơn hàng mà không cần phải tạo bản sao sâu, nhanh
   chóng và hiệu quả.
   7.2. Sao Chép Sâu (Deep Copy):
   Giỏ Hàng và Wish List: Đảm bảo rằng thông tin sản phẩm tại thời điểm thêm vào không bị ảnh hưởng bởi các thay đổi sau
   này của sản phẩm gốc.
   Phân Tích và Báo Cáo: Tạo bản sao sâu để thực hiện các phép toán phức tạp mà không ảnh hưởng đến dữ liệu gốc.
   Quản Lý Danh Mục: Khi tạo bản sao của các danh mục sản phẩm cần giữ nguyên cấu trúc cây nhưng độc lập với gốc.
8. Khuyến Nghị và Lưu Ý
   Hiểu Rõ Mục Đích Sao Chép:
   Đọc Dữ Liệu: Nếu chỉ cần đọc dữ liệu mà không thay đổi, sao chép nông có thể là đủ.
   Thao Tác Độc Lập: Nếu cần thao tác độc lập mà không ảnh hưởng đến dữ liệu gốc, sao chép sâu là cần thiết.
   Triển Khai Phù Hợp:
   Sao Chép Nông: Đơn giản, tiết kiệm thời gian và bộ nhớ, phù hợp cho các hoạt động chỉ đọc.
   Sao Chép Sâu: Cần quản lý cẩn thận để tránh các vấn đề về vòng lặp và đảm bảo tính nhất quán.
   Sử Dụng Các Thư Viện Hỗ Trợ:
   ModelMapper hoặc MapStruct: Có thể giúp tự động hóa việc sao chép nông và sâu với cấu hình phù hợp.
   Builder Pattern: Cung cấp tính linh hoạt cao để xây dựng về bản sao sâu với kiểm soát chặt chẽ hơn.
   Quản Lý ID và Khóa Chính:
   Tránh Sao Chép ID: Khi tạo bản sao mới để lưu vào cơ sở dữ liệu, hãy để hệ thống (Hibernate/JPA) tự động gán ID mới
   để tránh xung đột.
   Kiểm Thử Kỹ Lưỡng:
   Đảm Bảo Tính Chính Xác: Viết các unit tests để đảm bảo rằng bản sao được tạo đúng cách và không ảnh hưởng đến đối
   tượng gốc.
   Kiểm Soát Vòng Lặp: Đảm bảo rằng các mối quan hệ phức tạp không gây ra vòng lặp vô hạn trong quá trình sao chép sâu.
   Bảo Mật và Quyền Riêng Tư:
   Xử Lý Dữ Liệu Nhạy Cảm: Đảm bảo rằng bản sao của các đối tượng chứa thông tin nhạy cảm được xử lý đúng cách để bảo vệ
   quyền riêng tư của người dùng.
9. Kết Luận
   Việc sử dụng sao chép nông và sao chép sâu trong các tình huống cụ thể của ứng dụng Tech-Shop giúp bạn quản lý dữ
   liệu một cách hiệu quả, đảm bảo tính nhất quán và bảo vệ dữ liệu người dùng. Bằng cách hiểu rõ các lợi ích và hạn chế
   của từng loại sao chép, bạn có thể áp dụng chúng một cách linh hoạt để đáp ứng các yêu cầu của dự án.