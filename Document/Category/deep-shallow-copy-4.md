1. Wish List (Danh Sách Yêu Thích)
   Tình Huống:
   Người dùng muốn thêm sản phẩm vào danh sách yêu thích để xem sau hoặc quyết định mua hàng trong tương lai. Bạn muốn
   lưu trữ thông tin sản phẩm tại thời điểm thêm vào danh sách yêu thích để đảm bảo rằng, dù sản phẩm gốc bị thay đổi (
   giá cả, mô tả, v.v.), danh sách yêu thích vẫn giữ nguyên thông tin ban đầu.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo một bản sao độc lập của sản phẩm khi thêm vào danh sách yêu thích.

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

    /**
     * Sao chép sâu một WishListItem.
     */
    public static WishListItem deepCopy(WishListItem original) {
        if (original == null) return null;

        WishListItem copy = new WishListItem();
        copy.setProductCopy(Product.deepCopy(original.getProductCopy()));
        copy.setWishList(original.getWishList()); // Tham chiếu đến WishList gốc
        return copy;
    }

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
Lợi Ích:
Bảo Toàn Dữ Liệu: Thông tin sản phẩm trong wish list không bị thay đổi bởi các cập nhật sau này của sản phẩm gốc.
Trải Nghiệm Người Dùng Tốt Hơn: Người dùng có thể xem thông tin chính xác về sản phẩm tại thời điểm thêm vào wish list.

2. User Preferences and Profiles (Tùy Chỉnh và Hồ Sơ Người Dùng)
   Tình Huống:
   Người dùng có thể tùy chỉnh các thiết lập cá nhân như giao diện, ngôn ngữ, hoặc các chiến lược mua sắm. Bạn muốn lưu
   trữ các tùy chỉnh này một cách chính xác và linh hoạt.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) để tạo bản sao của đối tượng tùy chỉnh khi cần thao tác (ví dụ: cập nhật một số
thiết lập) mà không làm thay đổi đối tượng gốc.

Ví Dụ Mã:
Entity UserPreferences:

@Entity
@Table(name = "user_preferences")
public class UserPreferences {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private String theme;
    private String language;

    @OneToOne(mappedBy = "preferences")
    private User user;

    // Getters và Setters

    /**
     * Sao chép nông một UserPreferences.
     */
    public static UserPreferences shallowCopy(UserPreferences original) {
        if (original == null) return null;

        UserPreferences copy = new UserPreferences();
        copy.setId(original.getId());
        copy.setTheme(original.getTheme());
        copy.setLanguage(original.getLanguage());
        // Không sao chép user để tránh vòng lặp
        return copy;
    }

    /**
     * Sao chép sâu một UserPreferences nếu cần.
     */
    public static UserPreferences deepCopy(UserPreferences original) {
        if (original == null) return null;

        UserPreferences copy = new UserPreferences();
        copy.setTheme(original.getTheme());
        copy.setLanguage(original.getLanguage());
        // Không sao chép user để tránh vòng lặp
        return copy;
    }

}
Service UserPreferencesService:

@Service
public class UserPreferencesService {

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    /**
     * Cập nhật tùy chỉnh người dùng.
     *
     * @param userPreferencesId ID của tùy chỉnh
     * @param newTheme          Chủ đề mới
     * @param newLanguage       Ngôn ngữ mới
     * @return Tùy chỉnh đã cập nhật
     */
    public UserPreferences updateUserPreferences(Integer userPreferencesId, String newTheme, String newLanguage) {
        // Tìm tùy chỉnh gốc
        UserPreferences originalPreferences = userPreferencesRepository.findById(userPreferencesId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy UserPreferences với ID: " + userPreferencesId));

        // Sao chép nông để cập nhật
        UserPreferences updatedPreferences = UserPreferences.shallowCopy(originalPreferences);
        updatedPreferences.setTheme(newTheme);
        updatedPreferences.setLanguage(newLanguage);

        // Lưu cập nhật
        return userPreferencesRepository.save(updatedPreferences);
    }

}
Lợi Ích:
Hiệu Suất Tốt: Sao chép nông nhanh chóng và tiết kiệm tài nguyên khi chỉ thao tác với các thuộc tính cơ bản.
Tránh Thay Đổi Gốc: Đảm bảo rằng các thay đổi chỉ áp dụng cho bản sao mà không làm ảnh hưởng đến đối tượng gốc.

3. Product Bundles or Collections (Gói Sản Phẩm hoặc Bộ Sưu Tập)
   Tình Huống:
   Người quản trị có thể tạo các gói sản phẩm hoặc bộ sưu tập đặc biệt, bao gồm nhiều sản phẩm khác nhau. Khi tạo gói
   sản phẩm mới dựa trên các sản phẩm hiện có, bạn muốn chỉnh sửa hoặc thêm các thuộc tính mới mà không làm ảnh hưởng
   đến các sản phẩm gốc.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) để thêm sản phẩm vào gói, vì bạn chỉ cần tham chiếu đến các sản phẩm gốc mà không
cần tạo bản sao mới.

Ví Dụ Mã:
Entity ProductBundle:

@Entity
@Table(name = "product_bundles")
public class ProductBundle {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private String bundleName;
    private Double bundlePrice;

    @ManyToMany
    @JoinTable(
        name = "bundle_products",
        joinColumns = @JoinColumn(name = "bundle_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    // Getters và Setters

    /**
     * Sao chép nông một ProductBundle.
     */
    public static ProductBundle shallowCopy(ProductBundle original) {
        if (original == null) return null;

        ProductBundle copy = new ProductBundle();
        copy.setBundleName(original.getBundleName());
        copy.setBundlePrice(original.getBundlePrice());
        copy.setProducts(new HashSet<>(original.getProducts()));
        return copy;
    }

}
Service ProductBundleService:

@Service
public class ProductBundleService {

    @Autowired
    private ProductBundleRepository productBundleRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Tạo một gói sản phẩm mới dựa trên gói hiện có và thêm sản phẩm mới.
     *
     * @param originalBundleId ID của gói sản phẩm gốc
     * @param newBundleName    Tên gói sản phẩm mới
     * @param newProductId     ID của sản phẩm mới cần thêm vào gói
     * @return Gói sản phẩm mới đã được lưu
     */
    public ProductBundle createNewBundle(Integer originalBundleId, String newBundleName, Integer newProductId) {
        // Tìm gói sản phẩm gốc
        ProductBundle originalBundle = productBundleRepository.findById(originalBundleId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ProductBundle với ID: " + originalBundleId));

        // Sao chép nông gói sản phẩm gốc
        ProductBundle newBundle = ProductBundle.shallowCopy(originalBundle);
        newBundle.setBundleName(newBundleName);
        newBundle.setBundlePrice(originalBundle.getBundlePrice()); // Có thể điều chỉnh giá mới nếu cần

        // Thêm sản phẩm mới vào gói
        Product newProduct = productRepository.findById(newProductId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Product với ID: " + newProductId));
        newBundle.getProducts().add(newProduct);

        // Lưu gói sản phẩm mới
        return productBundleRepository.save(newBundle);
    }

}
Lợi Ích:
Tăng Tính Linh Hoạt: Cho phép tạo các gói sản phẩm mới dựa trên các gói hiện có mà không làm thay đổi các sản phẩm gốc.
Tiết Kiệm Thời Gian: Sử dụng sao chép nông giúp nhanh chóng tái sử dụng cấu trúc dữ liệu hiện có.

4. Review Management (Quản Lý Đánh Giá Sản Phẩm)
   Tình Huống:
   Người dùng có thể đánh giá sản phẩm. Khi quản trị viên xem xét và phê duyệt các đánh giá, bạn có thể cần thao tác với
   dữ liệu đánh giá mà không làm thay đổi các đối tượng đánh giá gốc.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) để tạo bản sao của đánh giá khi thực hiện các công tác đánh giá hoặc phân tích.

Ví Dụ Mã:
Entity ProductReview:

@Entity
@Table(name = "product_reviews")
public class ProductReview {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private String reviewText;
    private Integer rating;
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters và Setters

    /**
     * Sao chép nông một ProductReview.
     */
    public static ProductReview shallowCopy(ProductReview original) {
        if (original == null) return null;

        ProductReview copy = new ProductReview();
        copy.setId(original.getId());
        copy.setReviewText(original.getReviewText());
        copy.setRating(original.getRating());
        copy.setApproved(original.isApproved());
        copy.setProduct(original.getProduct()); // Tham chiếu đến sản phẩm gốc
        copy.setUser(original.getUser()); // Tham chiếu đến người dùng gốc
        return copy;
    }

}
Service ProductReviewService:

@Service
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository productReviewRepository;

    /**
     * Phê duyệt một đánh giá sản phẩm.
     *
     * @param reviewId ID của đánh giá
     * @return Đánh giá đã được cập nhật
     */
    public ProductReview approveReview(Integer reviewId) {
        // Tìm đánh giá gốc
        ProductReview originalReview = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ProductReview với ID: " + reviewId));

        // Sao chép nông và cập nhật trạng thái phê duyệt
        ProductReview updatedReview = ProductReview.shallowCopy(originalReview);
        updatedReview.setApproved(true);

        // Lưu lại đánh giá
        return productReviewRepository.save(updatedReview);
    }

}
Lợi Ích:
Quản Lý Trạng Thái: Đảm bảo rằng việc thao tác với trạng thái đánh giá không ảnh hưởng đến các thuộc tính hoặc mối quan
hệ khác của đánh giá gốc.
Bảo Toàn Thông Tin Đánh Giá: Các thay đổi chỉ áp dụng cho bản sao khi phê duyệt, giúp duy trì dữ liệu không bị lỗi.

5. Undo Operations or Versioning (Hoàn Nguyên Hoạt Động hoặc Phiên Bản Hóa)
   Tình Huống:
   Người dùng hoặc quản trị viên tiến hành các thao tác như chỉnh sửa thông tin sản phẩm, quản lý danh mục, và muốn có
   khả năng hoàn nguyên thao tác hoặc xem lại các phiên bản trước đó của dữ liệu.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để lưu trữ các phiên bản trước của các đối tượng trước khi thay đổi, giúp có thể hoàn
nguyên hoặc truy xuất các phiên bản cũ mà không ảnh hưởng đến dữ liệu hiện tại.

Ví Dụ Mã:
Entity ProductVersion:

@Entity
@Table(name = "product_versions")
public class ProductVersion {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private String name;
    private String alias;
    private String image;
    private Double price;
    private String description;

    private Date versionDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Getters và Setters

    /**
     * Sao chép sâu một Product để lưu phiên bản.
     */
    public static ProductVersion copyDeep(Product original) {
        if (original == null) return null;

        ProductVersion version = new ProductVersion();
        version.setName(original.getName());
        version.setAlias(original.getAlias());
        version.setImage(original.getImage());
        version.setPrice(original.getPrice());
        version.setDescription(original.getDescription());
        version.setVersionDate(new Date()); // Đặt ngày phiên bản mới
        version.setProduct(original); // Tham chiếu đến sản phẩm gốc
        return version;
    }

}
Service ProductVersionService:

@Service
public class ProductVersionService {

    @Autowired
    private ProductVersionRepository productVersionRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Lưu phiên bản trước khi cập nhật sản phẩm.
     *
     * @param productId ID của sản phẩm
     * @return Phiên bản sản phẩm đã lưu
     */
    public ProductVersion saveProductVersion(Integer productId) {
        // Tìm sản phẩm gốc
        Product originalProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Product với ID: " + productId));

        // Tạo bản sao sâu để lưu phiên bản
        ProductVersion productVersion = ProductVersion.copyDeep(originalProduct);

        // Lưu phiên bản
        return productVersionRepository.save(productVersion);
    }

    /**
     * Hoàn nguyên sản phẩm dựa trên phiên bản.
     *
     * @param versionId ID của phiên bản
     * @return Sản phẩm đã được hoàn nguyên
     */
    public Product revertToVersion(Integer versionId) {
        // Tìm phiên bản
        ProductVersion version = productVersionRepository.findById(versionId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ProductVersion với ID: " + versionId));

        // Tìm sản phẩm gốc
        Product product = productRepository.findById(version.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Product với ID: " + version.getProduct().getId()));

        // Sao chép sâu từ phiên bản để hoàn nguyên
        product.setName(version.getName());
        product.setAlias(version.getAlias());
        product.setImage(version.getImage());
        product.setPrice(version.getPrice());
        product.setDescription(version.getDescription());

        // Lưu lại sản phẩm đã hoàn nguyên
        return productRepository.save(product);
    }

}
Lợi Ích:
Theo Dõi Thay Đổi: Giúp bạn theo dõi và quản lý các thay đổi đối với sản phẩm, dễ dàng hoàn nguyên khi cần thiết.
Dễ Dàng Kiểm Soát Dữ Liệu: Đảm bảo rằng các phiên bản trước không bị mất và có thể truy xuất khi cần.

6. Promotions and Discounts (Khuyến Mại và Giảm Giá)
   Tình Huống:
   Người quản trị muốn tạo các chương trình khuyến mại hoặc giảm giá cho các sản phẩm cụ thể. Khi thiết lập chương
   trình, bạn có thể cần sao chép thông tin sản phẩm để thiết lập giám sát và quản lý chương trình mà không làm ảnh
   hưởng đến sản phẩm gốc.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) nếu bạn chỉ cần tham chiếu đến sản phẩm gốc hoặc sao chép sâu (deep copy) nếu bạn
cần lưu trữ thông tin độc lập về sản phẩm trong chương trình khuyến mại.

Ví Dụ Mã:
Entity Promotion:

@Entity
@Table(name = "promotions")
public class Promotion {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private String promotionName;
    private Double discountPercentage;
    private Date startDate;
    private Date endDate;

    @ManyToMany
    @JoinTable(
        name = "promotion_products",
        joinColumns = @JoinColumn(name = "promotion_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    // Getters và Setters

    /**
     * Sao chép nông một Promotion.
     */
    public static Promotion shallowCopy(Promotion original) {
        if (original == null) return null;

        Promotion copy = new Promotion();
        copy.setPromotionName(original.getPromotionName());
        copy.setDiscountPercentage(original.getDiscountPercentage());
        copy.setStartDate(original.getStartDate());
        copy.setEndDate(original.getEndDate());
        copy.setProducts(new HashSet<>(original.getProducts()));
        return copy;
    }

    /**
     * Sao chép sâu một Promotion nếu cần.
     */
    public static Promotion deepCopy(Promotion original) {
        if (original == null) return null;

        Promotion copy = new Promotion();
        copy.setPromotionName(original.getPromotionName());
        copy.setDiscountPercentage(original.getDiscountPercentage());
        copy.setStartDate(original.getStartDate());
        copy.setEndDate(original.getEndDate());

        // Sao chép sâu các sản phẩm nếu cần
        Set<Product> copiedProducts = new HashSet<>();
        for (Product product : original.getProducts()) {
            // Tùy thuộc vào yêu cầu, bạn có thể sao chép nông hoặc sâu từng sản phẩm
            Product copiedProduct = Product.shallowCopy(product);
            copiedProducts.add(copiedProduct);
        }
        copy.setProducts(copiedProducts);
        return copy;
    }

}
Service PromotionService:

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Tạo một chương trình khuyến mại mới dựa trên chương trình hiện có.
     *
     * @param originalPromotionId ID của chương trình khuyến mại gốc
     * @param newPromotionName    Tên chương trình khuyến mại mới
     * @return Chương trình khuyến mại mới đã được lưu
     */
    public Promotion createNewPromotion(Integer originalPromotionId, String newPromotionName) {
        // Tìm chương trình khuyến mại gốc
        Promotion originalPromotion = promotionRepository.findById(originalPromotionId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Promotion với ID: " + originalPromotionId));

        // Sao chép nông và thay đổi tên
        Promotion newPromotion = Promotion.shallowCopy(originalPromotion);
        newPromotion.setPromotionName(newPromotionName);

        // Lưu chương trình khuyến mại mới
        return promotionRepository.save(newPromotion);
    }

    /**
     * Tạo một chương trình khuyến mại mới với các sản phẩm sao chép sâu.
     *
     * @param originalPromotionId ID của chương trình khuyến mại gốc
     * @param newPromotionName    Tên chương trình khuyến mại mới
     * @return Chương trình khuyến mại mới đã được lưu
     */
    public Promotion createDeepCopiedPromotion(Integer originalPromotionId, String newPromotionName) {
        // Tìm chương trình khuyến mại gốc
        Promotion originalPromotion = promotionRepository.findById(originalPromotionId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Promotion với ID: " + originalPromotionId));

        // Sao chép sâu và thay đổi tên
        Promotion deepCopiedPromotion = Promotion.deepCopy(originalPromotion);
        deepCopiedPromotion.setPromotionName(newPromotionName);

        // Lưu chương trình khuyến mại mới
        return promotionRepository.save(deepCopiedPromotion);
    }

}
Lợi Ích:
Quản Lý Chương Trình Khuyến Mại: Các bản sao giúp quản lý chương trình khuyến mại một cách linh hoạt, dễ dàng tạo ra các
biến thể mới mà không làm ảnh hưởng đến chương trình gốc.
Theo Dõi Thay Đổi: Giúp theo dõi và quản lý các chương trình khuyến mại khác nhau một cách hiệu quả.

7. User Sessions and Caching (Phiên Người Dùng và Bộ Nhớ Đệm)
   Tình Huống:
   Trong quá trình người dùng tương tác với trang web, bạn có thể lưu trữ thông tin trạng thái phiên hoặc các dữ liệu bộ
   nhớ đệm để cải thiện hiệu suất và trải nghiệm người dùng.

Giải Pháp:
User Sessions: Sử dụng sao chép nông (shallow copy) để lưu trữ thông tin phiên, do thông tin trong session thường là các
thuộc tính đơn giản và không yêu cầu độc lập hoàn toàn.
Caching: Sử dụng sao chép nông (shallow copy) hoặc sao chép sâu (deep copy) tùy vào loại dữ liệu và cách bạn muốn quản
lý bộ nhớ đệm.
Ví Dụ Mã:
Thêm vào Session (Sao chép nông):

@Controller
public class UserController {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Thêm sản phẩm vào bộ nhớ đệm so sánh.
     *
     * @param productId ID của sản phẩm
     * @param session   Phiên người dùng
     * @return Redirect đến trang so sánh
     */
    @PostMapping("/add-to-compare")
    public String addToCompare(@RequestParam("productId") Integer productId, HttpSession session) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Product với ID: " + productId));

        // Sao chép nông sản phẩm
        Product copyProduct = Product.shallowCopy(product);

        // Lấy danh sách sản phẩm đã so sánh từ session
        List<Product> compareList = (List<Product>) session.getAttribute("compareList");
        if (compareList == null) {
            compareList = new ArrayList<>();
        }

        // Thêm sản phẩm vào danh sách so sánh
        compareList.add(copyProduct);
        session.setAttribute("compareList", compareList);

        return "redirect:/compare";
    }

    /**
     * Hiển thị trang so sánh sản phẩm.
     *
     * @param session Phiên người dùng
     * @param model   Mô hình để truyền dữ liệu đến view
     * @return Trang so sánh
     */
    @GetMapping("/compare")
    public String showCompare(HttpSession session, Model model) {
        List<Product> compareList = (List<Product>) session.getAttribute("compareList");
        model.addAttribute("compareList", compareList);
        return "compare";
    }

}
Lợi Ích:
Tăng Tốc Độ Truy Cập: Sử dụng bộ nhớ đệm để giảm số lần truy vấn cơ sở dữ liệu, cải thiện hiệu suất truy cập trang web.
Quản Lý Phiên Người Dùng: Giữ trạng thái người dùng một cách hiệu quả mà không cần tạo nhiều bản sao độc lập.

8. Personalized Recommendations (Gợi Ý Cá Nhân Hóa)
   Tình Huống:
   Ứng dụng cung cấp các gợi ý sản phẩm dựa trên hành vi mua sắm hoặc xem sản phẩm của người dùng. Bạn cần tạo các bản
   sao của dữ liệu sản phẩm để phân tích và tạo ra các đề xuất mà không làm thay đổi dữ liệu gốc.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo các bản sao độc lập của sản phẩm cho mục đích phân tích dữ liệu và tạo gợi ý.

Ví Dụ Mã:
Service RecommendationService:

@Service
public class RecommendationService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Tạo gợi ý sản phẩm dựa trên lịch sử xem của người dùng.
     *
     * @param userId ID của người dùng
     * @return Danh sách sản phẩm gợi ý đã được sao chép sâu
     */
    public List<Product> generateRecommendations(Integer userId) {
        // Lấy lịch sử xem sản phẩm của người dùng
        List<Product> viewedProducts = productRepository.findViewedProductsByUserId(userId);

        // Tạo bản sao sâu để xử lý gợi ý
        List<Product> recommendationCopies = viewedProducts.stream()
                .map(Product::deepCopy)
                .collect(Collectors.toList());

        // Phân tích dữ liệu để tạo gợi ý (giả sử có phương thức phân tích)
        // List<Product> recommendedProducts = analyzeRecommendations(recommendationCopies);

        // Trả về danh sách gợi ý
        return recommendationCopies; // Thay bằng recommendedProducts khi có phương thức phân tích
    }

}
Lợi Ích:
Cá Nhân Hóa: Cung cấp gợi ý cá nhân hóa dựa trên dữ liệu người dùng một cách hiệu quả và an toàn.
Tính Linh Hoạt: Có thể mở rộng và tùy chỉnh các thuật toán gợi ý mà không làm thay đổi dữ liệu gốc.

9. Managing Inventory and Stock (Quản Lý Tồn Kho)
   Tình Huống:
   Khi quản lý tồn kho, bạn có thể cần tạo các báo cáo hoặc dự tính mức tiêu thụ sản phẩm trong tương lai dựa trên dữ
   liệu cập nhật hiện tại.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo bản sao dữ liệu tồn kho hiện tại trước khi thực hiện các phép tính dự đoán hoặc
báo cáo.

Ví Dụ Mã:
Entity InventoryItem:

@Entity
@Table(name = "inventory_items")
public class InventoryItem {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Date lastUpdated;

    // Getters và Setters

    /**
     * Sao chép sâu một InventoryItem.
     */
    public static InventoryItem deepCopy(InventoryItem original) {
        if (original == null) return null;

        InventoryItem copy = new InventoryItem();
        // Không sao chép id để Hibernate/JPA tự gán
        copy.setProduct(original.getProduct()); // Tham chiếu đến sản phẩm gốc
        copy.setQuantity(original.getQuantity());
        copy.setLastUpdated(new Date(original.getLastUpdated().getTime()));
        return copy;
    }

}
Service InventoryService:

@Service
public class InventoryService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    /**
     * Tính toán dự tính tồn kho dựa trên dữ liệu hiện tại.
     *
     * @return Danh sách các mục tồn kho đã được sao chép sâu và dự tính
     */
    public List<InventoryItem> calculateInventoryForecast() {
        List<InventoryItem> currentInventory = inventoryItemRepository.findAll();

        // Sao chép sâu các mục tồn kho hiện tại
        List<InventoryItem> inventoryCopies = currentInventory.stream()
                .map(InventoryItem::deepCopy)
                .collect(Collectors.toList());

        // Thực hiện dự tính tồn kho (giả sử có phương thức tính toán)
        // for (InventoryItem item : inventoryCopies) {
        //     item.setQuantity(item.getQuantity() - forecastedSales(item.getProduct()));
        // }

        // Trả về danh sách dự tính tồn kho
        return inventoryCopies; // Thay bằng inventoryCopies đã được dự tính
    }

}
Lợi Ích:
Bảo Toàn Dữ Liệu hiện tại: Đảm bảo rằng các phép tính dự đoán không làm thay đổi dữ liệu tồn kho gốc.
Xác Định Chính Xác: Giúp quản lý tồn kho dựa trên dữ liệu chính xác và mới nhất.

10. Transaction Management (Quản Lý Giao Dịch)
    Tình Huống:
    Trong các giao dịch mua bán, bạn có thể cần sao chép thông tin đơn hàng hoặc sản phẩm để thực hiện các thao tác như
    xác nhận, thanh toán, hoặc hủy bỏ mà không làm thay đổi dữ liệu gốc trong phiên giao dịch.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) hoặc sao chép sâu (deep copy) tùy thuộc vào các yêu cầu cụ thể của phiên giao dịch.

Ví Dụ Mã:
Service TransactionService:

@Service
public class TransactionService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Xác nhận giao dịch đơn hàng.
     *
     * @param orderId ID của đơn hàng
     * @return Đơn hàng đã được xác nhận
     */
    @Transactional
    public Order confirmOrder(Integer orderId) {
        // Tìm đơn hàng gốc
        Order originalOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Order với ID: " + orderId));

        // Sao chép nông để xác nhận
        Order confirmedOrder = Order.shallowCopy(originalOrder);
        confirmedOrder.setStatus("Confirmed");

        // Lưu đơn hàng đã xác nhận
        return orderRepository.save(confirmedOrder);
    }

    /**
     * Hủy đơn hàng.
     *
     * @param orderId ID của đơn hàng
     * @return Đơn hàng đã được hủy
     */
    @Transactional
    public Order cancelOrder(Integer orderId) {
        // Tìm đơn hàng gốc
        Order originalOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Order với ID: " + orderId));

        // Sao chép sâu để hủy đơn hàng
        Order canceledOrder = Order.copyDeep(originalOrder);
        canceledOrder.setStatus("Canceled");

        // Lưu đơn hàng đã hủy
        return orderRepository.save(canceledOrder);
    }

}
Lợi Ích:
Quản Lý Giao Dịch Chính Xác: Cung cấp khả năng xác nhận hoặc hủy bỏ giao dịch mà không làm thay đổi dữ liệu đơn hàng
gốc.
Bảo Toàn Dữ Liệu: Đảm bảo rằng các trạng thái giao dịch được quản lý một cách chính xác và an toàn.

11. Personalized Notifications (Thông Báo Cá Nhân Hóa)
    Tình Huống:
    Hệ thống gửi các thông báo tùy chỉnh cho người dùng dựa trên hành vi hoặc sở thích của họ. Bạn muốn tạo các thông
    báo mà không làm thay đổi dữ liệu gốc của người dùng hoặc sản phẩm.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) để lưu trữ thông tin cần thiết cho thông báo mà không cần tạo các bản sao mới cho
dữ liệu gốc.

Ví Dụ Mã:
Entity UserNotification:

@Entity
@Table(name = "user_notifications")
public class UserNotification {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private String message;
    private Date sentDate;
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Getters và Setters

    /**
     * Sao chép nông một UserNotification.
     */
    public static UserNotification shallowCopy(UserNotification original) {
        if (original == null) return null;

        UserNotification copy = new UserNotification();
        copy.setMessage(original.getMessage());
        copy.setSentDate(original.getSentDate());
        copy.setRead(original.isRead());
        copy.setUser(original.getUser());
        copy.setProduct(original.getProduct());
        return copy;
    }

}
Service NotificationService:

@Service
public class NotificationService {

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Gửi thông báo cho người dùng về sản phẩm có khuyến mãi.
     *
     * @param userId    ID của người dùng
     * @param productId ID của sản phẩm
     * @return Thông báo đã được lưu
     */
    public UserNotification sendPromotionNotification(Integer userId, Integer productId) {
        // Tìm người dùng và sản phẩm
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy User với ID: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Product với ID: " + productId));

        // Tạo thông báo mới
        UserNotification notification = new UserNotification();
        notification.setMessage("Sản phẩm " + product.getName() + " hiện có chương trình khuyến mãi!");
        notification.setSentDate(new Date());
        notification.setRead(false);
        notification.setUser(user);
        notification.setProduct(product);

        // Sao chép nông nếu cần và lưu thông báo
        return userNotificationRepository.save(UserNotification.shallowCopy(notification));
    }

}
Lợi Ích:
Cá Nhân Hóa Thông Báo: Cung cấp thông báo tùy chỉnh dựa trên sở thích và hành vi của người dùng.
Tăng Tín Nhiệm và Trải Nghiệm Người Dùng: Thông báo chính xác và cá nhân hóa giúp nâng cao trải nghiệm người dùng và
tăng tỷ lệ tương tác.

12. Handling Bulk Operations (Xử Lý Các Thao Tác Hàng Loạt)
    Tình Huống:
    Bạn cần thực hiện các thao tác hàng loạt như xóa nhiều sản phẩm, cập nhật nhanh nhiều danh mục, hoặc xử lý dữ liệu
    lớn cho các báo cáo.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) hoặc sao chép sâu (deep copy) tùy thuộc vào mục đích của từng thao tác để tăng hiệu
suất và đảm bảo tính nhất quán dữ liệu.

Ví Dụ Mã:
Service BulkOperationService:

@Service
public class BulkOperationService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVersionRepository productVersionRepository;

    /**
     * Cập nhật hàng loạt giá sản phẩm.
     *
     * @param percentage Tỷ lệ tăng/giảm giá (%)
     * @return Danh sách sản phẩm đã được cập nhật
     */
    @Transactional
    public List<Product> bulkUpdatePrices(Double percentage) {
        List<Product> products = productRepository.findAll();

        List<Product> updatedProducts = new ArrayList<>();

        for (Product product : products) {
            // Sao chép sâu để lưu phiên bản trước khi cập nhật
            ProductVersion version = ProductVersion.copyDeep(product);
            productVersionRepository.save(version);

            // Cập nhật giá sản phẩm
            Product updatedProduct = Product.copyShallow(product);
            Double oldPrice = updatedProduct.getPrice();
            Double newPrice = oldPrice + (oldPrice * percentage / 100);
            updatedProduct.setPrice(newPrice);

            updatedProducts.add(updatedProduct);
        }

        // Lưu các sản phẩm đã cập nhật
        return productRepository.saveAll(updatedProducts);
    }

}
Lợi Ích:
Tăng Hiệu Suất: Xử lý các thao tác hàng loạt một cách nhanh chóng và hiệu quả.
Bảo Toàn Dữ Liệu: Lưu các phiên bản trước của sản phẩm trước khi thực hiện cập nhật, giúp dễ dàng rollback nếu cần.

13. Testing and Quality Assurance (Kiểm Thử và Đảm Bảo Chất Lượng)
    Tình Huống:
    Trong quá trình phát triển, bạn muốn kiểm thử các chức năng của ứng dụng mà không làm thay đổi dữ liệu thực tế. Bạn
    có thể tạo các bản sao của các đối tượng để sử dụng trong các bài kiểm thử (unit tests) mà không ảnh hưởng đến dữ
    liệu gốc.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo các đối tượng giả lập (mock objects) cho các bài kiểm thử, đảm bảo rằng các thử
nghiệm không làm thay đổi dữ liệu thực tế.

Ví Dụ Mã:
Unit Test với JUnit:

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testAddReview() {
        // Tạo sản phẩm gốc
        Product originalProduct = productRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Product"));

        // Tạo bản sao sâu để sử dụng trong kiểm thử
        Product testProduct = Product.deepCopy(originalProduct);

        // Thêm đánh giá vào bản sao
        ProductReview review = new ProductReview();
        review.setReviewText("Sản phẩm tốt!");
        review.setRating(5);
        review.setApproved(true);
        review.setProductCopy(testProduct);
        testProduct.getReviews().add(review);

        // Thực hiện kiểm thử
        Product updatedProduct = productService.addReview(testProduct, review);

        // Kiểm tra kết quả
        assertNotNull(updatedProduct);
        assertEquals(1, updatedProduct.getReviews().size());
        assertEquals("Sản phẩm tốt!", updatedProduct.getReviews().iterator().next().getReviewText());

        // Đảm bảo sản phẩm gốc không bị thay đổi
        assertEquals(0, originalProduct.getReviews().size());
    }

}
Lợi Ích:
Chính Xác Trong Kiểm Thử: Đảm bảo rằng các bài kiểm thử không ảnh hưởng đến dữ liệu thực tế, giúp duy trì tính nhất quán
và độ tin cậy của ứng dụng.
Tự Động Hóa: Giúp tự động hóa các quá trình kiểm thử một cách an toàn và hiệu quả.

14. Logging and Auditing (Ghi Nhận và Kiểm Tra)
    Tình Huống:
    Hệ thống cần ghi nhận các giao dịch, hành động của người dùng, hoặc các thay đổi quan trọng để phục vụ mục đích kiểm
    tra, bảo mật và phân tích sau này.

Giải Pháp:
Sử dụng sao chép nông (shallow copy) để ghi nhận các hành động mà không làm thay đổi các đối tượng gốc trong hệ thống.

Ví Dụ Mã:
Entity AuditLog:

@Entity
@Table(name = "audit_logs")
public class AuditLog {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private String action;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Getters và Setters

    /**
     * Sao chép nông một AuditLog.
     */
    public static AuditLog shallowCopy(AuditLog original) {
        if (original == null) return null;

        AuditLog copy = new AuditLog();
        copy.setAction(original.getAction());
        copy.setTimestamp(original.getTimestamp());
        copy.setUser(original.getUser());
        copy.setProduct(original.getProduct());
        return copy;
    }

}
Service AuditService:

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    /**
     * Ghi nhận một hành động vào log.
     *
     * @param action    Mô tả hành động
     * @param user      Người thực hiện hành động
     * @param product   Sản phẩm liên quan (có thể null)
     * @return Log đã được lưu
     */
    public AuditLog logAction(String action, User user, Product product) {
        AuditLog originalLog = new AuditLog();
        originalLog.setAction(action);
        originalLog.setTimestamp(new Date());
        originalLog.setUser(user);
        originalLog.setProduct(product);

        // Sao chép nông và lưu log
        return auditLogRepository.save(AuditLog.shallowCopy(originalLog));
    }

}
Lợi Ích:
Theo Dõi Chính Xác: Ghi nhận các hành động một cách chính xác và không làm thay đổi dữ liệu gốc.
An Toàn và Bảo Mật: Bảo vệ dữ liệu gốc khỏi các thay đổi ngoài ý muốn và cung cấp các thông tin cần thiết cho mục đích
bảo mật và kiểm tra.

15. Handling API Responses and External Integrations
    Tình Huống:
    Khi tích hợp với các dịch vụ bên ngoài hoặc API, bạn có thể cần sao chép dữ liệu nhận được để xử lý hoặc lưu trữ mà
    không làm thay đổi dữ liệu gốc.

Giải Pháp:
Sử dụng sao chép sâu (deep copy) để tạo bản sao dữ liệu nhận được từ các dịch vụ bên ngoài trước khi xử lý hoặc lưu trữ.

Ví Dụ Mã:
Service ExternalApiService:

@Service
public class ExternalApiService {

    @Autowired
    private ExternalApiClient externalApiClient;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Nhận dữ liệu sản phẩm từ API bên ngoài và lưu vào hệ thống.
     *
     * @return Danh sách sản phẩm đã được lưu
     */
    public List<Product> importProductsFromExternalApi() {
        // Gọi API bên ngoài để lấy danh sách sản phẩm
        List<ProductDto> externalProducts = externalApiClient.getProducts();

        List<Product> savedProducts = new ArrayList<>();

        for (ProductDto dto : externalProducts) {
            // Tạo Product từ DTO
            Product product = new Product();
            product.setName(dto.getName());
            product.setAlias(dto.getAlias());
            product.setImage(dto.getImage());
            product.setPrice(dto.getPrice());
            product.setDescription(dto.getDescription());

            // Sao chép sâu (nếu cần các đối tượng liên kết)
            // Ví dụ: product.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));

            // Lưu sản phẩm vào DB
            savedProducts.add(productRepository.save(product));
        }

        return savedProducts;
    }

}
Lợi Ích:
Bảo Toàn Dữ Liệu Bên Ngoài: Đảm bảo rằng dữ liệu từ bên ngoài không bị thay đổi trong quá trình xử lý hoặc lưu trữ.
Kiểm Soát Dữ Liệu Nhập Vào: Cho phép bạn kiểm soát và xác thực dữ liệu trước khi lưu trữ vào hệ thống nội bộ.
Tổng Kết và Khuyến Nghị
Xác Định Rõ Ràng Mục Tiêu Sao Chép:
Sao chép nông (shallow copy): Khi bạn chỉ cần sao chép các thuộc tính cơ bản hoặc muốn tham chiếu tới các đối tượng gốc
mà không cần thay đổi dữ liệu.
Sao chép sâu (deep copy): Khi bạn cần tạo các bản sao độc lập hoàn toàn để thực hiện các thao tác mà không ảnh hưởng đến
dữ liệu gốc.
Kiểm Soát Các Mối Quan Hệ:
Đảm bảo rằng các mối quan hệ giữa các entity được quản lý đúng cách khi sao chép, đặc biệt là khi sử dụng các mối quan
hệ như @ManyToOne, @OneToMany, và @ManyToMany.
Sử Dụng Thư Viện Hỗ Trợ Khi Cần Thiết:
Các thư viện như ModelMapper hoặc MapStruct có thể giúp tự động hóa quá trình sao chép đối tượng nông hoặc sâu với cấu
hình phù hợp.
Builder Pattern cung cấp tính linh hoạt cao để xây dựng bản sao sâu với kiểm soát chặt chẽ hơn.
Tuân Thủ Các Quy Tắc Quản Lý ID:
Tránh sao chép ID khi tạo các bản sao mới để Hibernate/JPA tự động gán ID mới, tránh xung đột khóa chính.
Kiểm Thử và Đảm Bảo Chất Lượng:
Viết các unit tests để kiểm tra quá trình sao chép đối tượng, đảm bảo rằng các thao tác sao chép hoạt động như mong đợi
và không làm thay đổi dữ liệu gốc.
Bảo Mật Dữ Liệu:
Đảm bảo rằng các bản sao được tạo ra không chứa thông tin nhạy cảm hoặc được xử lý đúng cách để bảo vệ quyền riêng tư
của người dùng.
Thông Thái Trong Sử Dụng Sao Chép:
Hiểu rõ sự khác biệt giữa sao chép nông và sao chép sâu để áp dụng đúng phương pháp vào từng tình huống cụ thể, tránh
các lỗi không mong muốn về dữ liệu.
Sử Dụng Các Phương Thức Tiện Ích:
Tạo các phương thức tiện ích để quản lý quá trình sao chép một cách nhất quán và tránh lặp lại mã.