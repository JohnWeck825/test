1. Giới Thiệu Về Sao Chép Nông và Sao Chép Sâu
   Sao chép nông (Shallow Copy): Tạo một bản sao của đối tượng chính, nhưng các đối tượng liên kết (nested objects) chỉ
   được sao chép bằng cách tham chiếu đến cùng một địa chỉ trong bộ nhớ. Điều này có nghĩa là thay đổi đối với các đối
   tượng liên kết ở bản sao sẽ ảnh hưởng đến đối tượng gốc và ngược lại.
   Sao chép sâu (Deep Copy): Tạo một bản sao hoàn toàn độc lập của đối tượng chính cùng với tất cả các đối tượng liên
   kết. Các thay đổi đối với bản sao sẽ không ảnh hưởng đến đối tượng gốc và ngược lại.
2. Ví Dụ Ứng Dụng Thực Tế Trong Tech-Shop
   2.1. Quản Lý Danh Mục Sản Phẩm (Category)
   Giả sử bạn có một danh mục sản phẩm trong Tech-Shop, chẳng hạn như "Điện Thoại". Danh mục này có thể có các danh mục
   con như "Smartphone", "Feature Phone",...

2.1.1. Sao Chép Nông (Shallow Copy)
Ví dụ Tình Huống:
Bạn muốn tạo một bản sao của danh mục "Điện Thoại" để sử dụng làm mẫu cho danh mục mới, ví dụ như "Máy Tính Bảng", nhưng
vẫn giữ nguyên các danh mục con hiện có.

// Giả sử bạn có một danh mục gốc
Category originalCategory = categoryRepository.findById(1).orElseThrow(); // ID=1: "Điện Thoại"

// Sao chép nông danh mục gốc
Category copiedCategory = Category.CopyFull(originalCategory, "Máy Tính Bảng");

// Thiết lập các thuộc tính bổ sung nếu cần
copiedCategory.setAlias("may-tinh-bang");
copiedCategory.setImage("tablet.png");

// Lưu bản sao vào cơ sở dữ liệu
categoryRepository.save(copiedCategory);
Giải Thích:

Sao chép Nông: Bản sao copiedCategory sẽ có cùng parent và children với originalCategory. Điều này nghĩa là cả hai danh
mục sẽ chia sẻ cùng một tập hợp danh mục con.
Hậu quả:
Thay đổi trong copiedCategory (ví dụ: thêm hoặc xóa danh mục con) sẽ ảnh hưởng trực tiếp đến originalCategory và ngược
lại vì cả hai chia sẻ cùng một bộ sưu tập subCategories.
Có thể gây ra lỗi hoặc dữ liệu không nhất quán nếu không được quản lý cẩn thận.
Khi Nào Sử Dụng:

Khi bạn muốn tạo một bản sao nhanh chóng của danh mục mà không cần phải thay đổi mối quan hệ cha-con.
Khi các danh mục con có thể được chia sẻ giữa nhiều danh mục cha.
2.1.2. Sao Chép Sâu (Deep Copy)
Ví dụ Tình Huống:
Bạn muốn tạo một bản sao hoàn toàn mới của danh mục "Điện Thoại", bao gồm cả các danh mục con, để có thể điều chỉnh hoặc
nâng cấp riêng biệt mà không ảnh hưởng đến danh mục gốc.

// Giả sử bạn có một danh mục gốc
Category originalCategory = categoryRepository.findById(1).orElseThrow(); // ID=1: "Điện Thoại"

// Sao chép sâu danh mục gốc
Category deepCopiedCategory = Category.DeepCopyFull(originalCategory, "Máy Tính Bảng");

// Thiết lập các thuộc tính bổ sung nếu cần
deepCopiedCategory.setAlias("may-tinh-bang");
deepCopiedCategory.setImage("tablet.png");

// Lưu bản sao vào cơ sở dữ liệu
categoryRepository.save(deepCopiedCategory);
Giải Thích:

Sao chép Sâu: Bản sao deepCopiedCategory sẽ là một đối tượng hoàn toàn độc lập với originalCategory, bao gồm cả các danh
mục con được sao chép độc lập.
Hậu quả:
Thay đổi trong deepCopiedCategory hoặc các danh mục con của nó sẽ không ảnh hưởng đến originalCategory và ngược lại.
Đảm bảo tính nhất quán dữ liệu và tránh xung đột mối quan hệ cha-con.
Khi Nào Sử Dụng:

Khi bạn cần tạo các bản sao hoàn toàn mới của danh mục để thực hiện các thay đổi mà không muốn ảnh hưởng đến danh mục
gốc.
Khi các danh mục con cần được sao chép độc lập để được quản lý riêng biệt.
2.2. Quản Lý Sản Phẩm (Product) Trong Danh Mục
Giả sử Tech-Shop có các sản phẩm được phân loại dưới các danh mục. Bạn muốn sao chép danh mục để tạo ra các phiên bản
đặc biệt hoặc cho các mục đích quản lý khác.

2.2.1. Sao Chép Nông Kiểu Đây Cũng Gây Nhiều Thay Đổi
Nếu bạn sao chép nông một danh mục chứa các sản phẩm, bản sao sẽ chia sẻ cùng một tập hợp sản phẩm với danh mục gốc.
Điều này có nghĩa là các thay đổi trong danh mục con của bản sao cũng sẽ ảnh hưởng đến danh mục gốc.

// Sao chép nông danh mục gốc
Category copiedCategory = Category.CopyFull(originalCategory, "Dịch Vụ Đặc Biệt");

// Thiết lập các thuộc tính bổ sung nếu cần
copiedCategory.setAlias("dich-vu-dac-biet");
copiedCategory.setImage("special-service.png");

// Lưu bản sao vào cơ sở dữ liệu
categoryRepository.save(copiedCategory);
Hậu quả:

Các sản phẩm trong danh mục gốc và bản sao sẽ là chung (shared). Việc thêm hoặc xóa sản phẩm trong một danh mục sẽ ảnh
hưởng đến danh mục kia.
Không phù hợp nếu bạn muốn quản lý các sản phẩm độc lập cho từng danh mục.
2.2.2. Sao Chép Sâu Để Tạo Độc Lập
Nếu bạn sao chép sâu một danh mục, việc này bao gồm cả việc sao chép các sản phẩm liên kết. Tuy nhiên, thường bạn không
muốn sao chép các sản phẩm mà chỉ cần tạo bản sao danh mục mới mà không liên kết với sản phẩm hiện có.

Giải Pháp:

Sao chép Nông Cho Danh Mục: Sao chép tệp danh mục mà không bao gồm các sản phẩm hoặc liên kết riêng biệt.
Thêm các Sản Phẩm Mới cho Bản Sao: Sau khi sao chép danh mục sâu, thêm các sản phẩm mới cần thiết cho bản sao mà không
ảnh hưởng đến danh mục gốc.
Ví Dụ:

public static Category DeepCopyCategoryWithoutProducts(Category category) {
Category copyCate = new Category();
copyCate.setName("Copy of " + category.getName());
copyCate.setAlias(category.getAlias() + "-copy");
copyCate.setImage(category.getImage());
copyCate.setEnabled(category.isEnabled());
copyCate.setParent(category.getParent());

    // Sao chép các danh mục con mà không sao chép sản phẩm
    Set<Category> copiedSubCategories = new LinkedHashSet<>();
    for (Category subCate : category.getSubCategories()) {
        Category copiedSubCate = Category.DeepCopyFull(subCate, subCate.getName());
        copiedSubCate.setParent(copyCate);
        copiedSubCategories.add(copiedSubCate);
    }
    copyCate.setSubCategories(copiedSubCategories);
    copyCate.setHasChildren(!copiedSubCategories.isEmpty());

    return copyCate;

}
Kết Quả:

Bản sao copyCate sẽ có các danh mục con được sao chép sâu nhưng không bao gồm các sản phẩm.
Bạn có thể thêm các sản phẩm mới vào copyCate mà không ảnh hưởng đến danh mục gốc.

3. So Sánh Trong Bối Cảnh Tech-Shop
   3.1. Sao Chép Nông
   Ưu Điểm:
   Nhanh chóng và dễ dàng.
   Tiết kiệm thời gian khi bạn cần tạo các bản sao với cấu trúc danh mục giống nhau.
   Nhược Điểm:
   Các mối quan hệ cha-con và sản phẩm được chia sẻ giữa bản sao và gốc, có thể dẫn đến sự cố dữ liệu không nhất quán.
   Không thể quản lý độc lập các danh mục và sản phẩm liên quan.
   Ứng Dụng Thực Tế:
   Tạo các danh mục tạm thời hoặc mẫu để thể hiện cấu trúc cho người dùng.
   Sử dụng trong các trường hợp không cần quản lý độc lập danh mục con hoặc sản phẩm.
   3.2. Sao Chép Sâu
   Ưu Điểm:
   Các bản sao hoàn toàn độc lập, không ảnh hưởng đến dữ liệu gốc.
   Quản lý danh mục và sản phẩm riêng biệt, dễ dàng thực hiện các thay đổi mà không lo ngại về việc ảnh hưởng đến dữ
   liệu khác.
   Nhược Điểm:
   Phức tạp hơn trong việc triển khai và quản lý, đặc biệt là khi có nhiều mối quan hệ cha-con.
   Có thể tiêu tốn nhiều tài nguyên hơn nếu cây danh mục phức tạp và sâu.
   Ứng Dụng Thực Tế:
   Tạo các danh mục phiên bản mới dựa trên danh mục gốc nhưng có sự thay đổi hoặc nâng cấp riêng biệt.
   Sao chép các danh mục để phục vụ cho các mục đích khác nhau như khuyến mãi, thử nghiệm cấu trúc mới mà không ảnh
   hưởng đến danh mục chính.
4. Mã Ví Dụ Minh Họa
   4.1. Sao Chép Nông Trong Tech-Shop
   Entity Category:

@Entity
@Table(name = "categories")
public class Category {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 128, nullable = false, unique = true)
    private String alias;

    @Column(length = 64, nullable = false)
    private String image;

    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name ASC")
    private Set<Category> subCategories = new LinkedHashSet<>();

    private boolean hasChildren;

    // Các phương thức CopyFull
    public static Category CopyFull(Category category) {
        Category copyCate = new Category();
        copyCate.setId(category.getId());
        copyCate.setName(category.getName());
        copyCate.setAlias(category.getAlias());
        copyCate.setImage(category.getImage());
        copyCate.setEnabled(category.isEnabled());
        copyCate.setParent(category.getParent());
        copyCate.setSubCategories(category.getSubCategories());
        copyCate.setHasChildren(category.getSubCategories().size() > 0);
        return copyCate;
    }

    public static Category CopyFull(Category category, String name) {
        Category copyCate = CopyFull(category);
        copyCate.setName(name);
        return copyCate;
    }

    // Getters và Setters
    // ...

}
Service CategoryService:

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Sao chép nông một danh mục với tên mới.
     */
    public Category copyCategoryShallow(Integer originalCategoryId, String newName) {
        // Tìm danh mục gốc
        Category originalCategory = categoryRepository.findById(originalCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category với ID: " + originalCategoryId));

        // Sao chép nông và thay đổi tên
        Category copiedCategory = Category.CopyFull(originalCategory, newName);

        // Đặt lại ID để Hibernate/JPA tự gán khi lưu vào DB
        copiedCategory.setId(null);

        // Lưu bản sao vào DB
        return categoryRepository.save(copiedCategory);
    }

}
Controller CategoryController:

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Endpoint để sao chép nông danh mục với tên mới.
     *
     * @param originalCategoryId ID của danh mục gốc
     * @param newName            Tên mới cho danh mục sao chép
     * @return Danh mục mới đã được tạo
     */
    @PostMapping("/copy-shallow/{id}")
    public ResponseEntity<Category> copyCategoryShallow(
            @PathVariable("id") Integer originalCategoryId,
            @RequestParam("name") String newName) {

        Category newCategory = categoryService.copyCategoryShallow(originalCategoryId, newName);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

}
Giải Thích:

Sao chép Nông: Phương thức CopyFull sao chép các thuộc tính của danh mục gốc sang bản sao, bao gồm cả tập hợp
subCategories. Tuy nhiên, subCategories chỉ là các tham chiếu đến các danh mục con gốc, không tạo bản sao mới.
Đặt lại ID: Đặt id của bản sao thành null để Hibernate/JPA tự gán một id mới khi lưu vào cơ sở dữ liệu, tránh xung đột
khóa chính.
Lưu Bản Sao: Khi gọi categoryRepository.save(copiedCategory), bản sao sẽ được lưu với một id mới, nhưng các danh mục con
vẫn liên kết đến danh mục gốc.
Hậu Quả:

Chia sẻ Danh mục con: Bản sao và gốc cùng chia sẻ cùng một tập hợp danh mục con. Thay đổi danh mục con của bản sao sẽ
ảnh hưởng đến gốc và ngược lại.
4.2. Sao Chép Sâu Trong Tech-Shop
Entity Category: (đã bao gồm phương thức DeepCopyFull như đã trình bày ở các phần trước)

@Entity
@Table(name = "categories")
public class Category {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 128, nullable = false, unique = true)
    private String alias;

    @Column(length = 64, nullable = false)
    private String image;

    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name ASC")
    private Set<Category> subCategories = new LinkedHashSet<>();

    private boolean hasChildren;

    // Phương thức sao chép sâu
    public static Category DeepCopyFull(Category category, String newName) {
        if (category == null) {
            return null;
        }

        Category copyCate = new Category();
        copyCate.setName(newName);
        copyCate.setAlias(category.getAlias() + "-copy");
        copyCate.setImage(category.getImage());
        copyCate.setEnabled(category.isEnabled());

        // Không sao chép cha để tránh vòng lặp
        copyCate.setParent(null);

        // Sao chép các danh mục con sâu
        Set<Category> copiedSubCategories = new LinkedHashSet<>();
        for (Category subCate : category.getSubCategories()) {
            Category copiedSubCate = DeepCopyFull(subCate, subCate.getName());
            copiedSubCate.setParent(copyCate);
            copiedSubCategories.add(copiedSubCate);
        }
        copyCate.setSubCategories(copiedSubCategories);
        copyCate.setHasChildren(!copiedSubCategories.isEmpty());

        return copyCate;
    }

    // Getters và Setters
    // ...

}
Service CategoryService:

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Sao chép sâu một danh mục với tên mới, bao gồm cả các danh mục con.
     */
    public Category copyCategoryDeep(Integer originalCategoryId, String newName) {
        // Tìm danh mục gốc
        Category originalCategory = categoryRepository.findById(originalCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category với ID: " + originalCategoryId));

        // Sao chép sâu và thay đổi tên
        Category deepCopiedCategory = Category.DeepCopyFull(originalCategory, newName);

        // Đặt lại ID để Hibernate/JPA tự gán khi lưu vào DB
        deepCopiedCategory.setId(null);

        // Lưu bản sao vào DB
        return categoryRepository.save(deepCopiedCategory);
    }

}
Controller CategoryController:

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Endpoint để sao chép sâu danh mục với tên mới.
     *
     * @param originalCategoryId ID của danh mục gốc
     * @param newName            Tên mới cho danh mục sao chép
     * @return Danh mục mới đã được tạo
     */
    @PostMapping("/copy-deep/{id}")
    public ResponseEntity<Category> copyCategoryDeep(
            @PathVariable("id") Integer originalCategoryId,
            @RequestParam("name") String newName) {

        Category newCategory = categoryService.copyCategoryDeep(originalCategoryId, newName);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

}
Giải Thích:

Sao Chép Sâu: Phương thức DeepCopyFull tạo một bản sao hoàn toàn độc lập của danh mục gốc cùng với tất cả các danh mục
con, đảm bảo rằng bản sao không chia sẻ bất kỳ tham chiếu nào với gốc.
Đặt lại ID: Đặt id thành null để Hibernate/JPA tự động gán id mới khi lưu bản sao vào cơ sở dữ liệu.
Lưu Bản Sao: Khi lưu bản sao sâu, các danh mục con cũng được lưu tự động do cascade = CascadeType.ALL.
Hậu Quả:

Độc Lập Hoàn Toàn: Bản sao và gốc là hai đối tượng hoàn toàn độc lập. Thay đổi trong bản sao sẽ không ảnh hưởng đến gốc
và ngược lại.
Quản Lý Dữ Liệu Tốt Hơn: Bạn có thể quản lý các danh mục và danh mục con một cách riêng biệt, thuận tiện cho việc tổ
chức và phân loại sản phẩm.

4. So Sánh ModelMapper và Builder Pattern Trong Bối Cảnh Tech-Shop
   4.1. ModelMapper
   Khả Năng Sao Chép:

Sao Chép Nông: Mặc định, ModelMapper thực hiện sao chép nông. Các thuộc tính đơn giản được sao chép giá trị, còn các
thuộc tính liên kết chỉ được sao chép bằng cách tham chiếu đến gốc.
Sao Chép Sâu: Cần cấu hình đặc biệt để ModelMapper thực hiện sao chép sâu. Điều này có thể phức tạp và đòi hỏi bạn phải
thiết lập các mappings rõ ràng để tránh các vấn đề như đạn kính vòng lặp.
Ưu Điểm:

Tự Động Hóa: Giảm thiểu lượng mã lặp lại khi sao chép các thuộc tính.
Dễ Sử Dụng Cho Các Cấu Trúc Đơn Giản: Thích hợp cho các đối tượng không có mối quan hệ phức tạp hoặc vòng lặp.
Nhược Điểm:

Phức Tạp Trong Cấu Hình: Khi cần sao chép sâu, bạn cần phải cấu hình thêm, có thể dẫn đến lỗi nếu không cẩn thận.
Khó Kiểm Soát Tùy Ý: Không linh hoạt bằng Builder Pattern trong việc tùy chỉnh quá trình sao chép.
Ví Dụ Minh Họa Sao Chép Sâu Với ModelMapper:

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
private final ModelMapper modelMapper;

    public CategoryMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // Thiết lập thêm nếu cần
    }

    public Category copyDeep(Category category) {
        return modelMapper.map(category, Category.class);
    }

}
Lưu Ý:

Cần kiểm tra kỹ các mappings để đảm bảo sao chép sâu đúng cách và tránh các vòng lặp.
Có thể cần sử dụng các TypeMap hoặc PropertyMap để tùy chỉnh quá trình sao chép cho các đối tượng phức tạp.
4.2. Builder Pattern
Khả Năng Sao Chép:

Sao Chép Nông: Dễ dàng thực hiện bằng cách thiết lập các thuộc tính từ đối tượng gốc.
Sao Chép Sâu: Có thể dễ dàng tùy chỉnh quá trình sao chép, bao gồm cả tạo bản sao cho các đối tượng liên kết, đảm bảo
tính độc lập hoàn toàn.
Ưu Điểm:

Kiểm Soát Chi Tiết: Cho phép bạn linh hoạt trong việc sao chép, đặc biệt là với các mối quan hệ phức tạp.
Tránh Vấn Đề Vòng Lặp: Bạn có thể thiết kế quá trình sao chép sao cho không tạo ra các vòng lặp không mong muốn.
Mã Rõ Ràng và Dễ Bảo Trì: Phương thức sao chép sâu bằng Builder Pattern thường rõ ràng hơn và dễ dàng bảo trì khi cấu
trúc đối tượng thay đổi.
Nhược Điểm:

Viết Mã Thủ Công: Cần phải tự viết logic sao chép sâu, tăng lượng mã và nguy cơ lỗi.
Phức Tạp Hơn cho Cấu Trúc Phức Tạp: Với nhiều đối tượng liên kết hoặc mối quan hệ phức tạp, việc triển khai Builder
Pattern để sao chép sâu có thể rất phức tạp.
Ví Dụ Minh Họa Sao Chép Sâu Với Builder Pattern:

public class Category {
// Các thuộc tính và phương thức khác...

    public static class Builder {
        private Category category;

        public Builder() {
            this.category = new Category();
        }

        public Builder withName(String name) {
            category.setName(name);
            return this;
        }

        public Builder withAlias(String alias) {
            category.setAlias(alias);
            return this;
        }

        public Builder withImage(String image) {
            category.setImage(image);
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            category.setEnabled(enabled);
            return this;
        }

        public Builder withParent(Category parent) {
            if (parent != null) {
                Category parentCopy = Category.copyDeep(parent);
                category.setParent(parentCopy);
            } else {
                category.setParent(null);
            }
            return this;
        }

        public Builder withSubCategories(Set<Category> subCategories) {
            if (subCategories != null) {
                Set<Category> subCategoriesCopy = new LinkedHashSet<>();
                for (Category subCat : subCategories) {
                    Category copiedSubCat = Category.copyDeep(subCat);
                    copiedSubCat.setParent(this.category);
                    subCategoriesCopy.add(copiedSubCat);
                }
                category.setSubCategories(subCategoriesCopy);
                category.setHasChildren(!subCategoriesCopy.isEmpty());
            } else {
                category.setSubCategories(new LinkedHashSet<>());
                category.setHasChildren(false);
            }
            return this;
        }

        public Builder withHasChildren(boolean hasChildren) {
            category.setHasChildren(hasChildren);
            return this;
        }

        public Category build() {
            return this.category;
        }
    }

    /**
     * Sao chép sâu một Category với tên mới.
     */
    public static Category DeepCopyFull(Category original, String newName) {
        if (original == null) return null;

        return new Builder()
                .withName(newName != null ? newName : original.getName())
                .withAlias(original.getAlias() + "-copy")
                .withImage(original.getImage())
                .withEnabled(original.isEnabled())
                .withParent(original.getParent()) // Cloned đã trong Builder
                .withSubCategories(original.getSubCategories())
                .build();
    }

    // Phương thức sao chép sâu khi không cần thay đổi tên
    public static Category DeepCopyFull(Category original) {
        return DeepCopyFull(original, original.getName());
    }

    // Getters và Setters
    // ...

}
Sử Dụng Builder Pattern trong Service:

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Sao chép sâu một danh mục với tên mới.
     */
    public Category copyCategoryDeepWithBuilder(Integer originalCategoryId, String newName) {
        // Tìm danh mục gốc
        Category originalCategory = categoryRepository.findById(originalCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category với ID: " + originalCategoryId));

        // Sao chép sâu và thay đổi tên
        Category deepCopiedCategory = Category.DeepCopyFull(originalCategory, newName);

        // Đặt lại ID để Hibernate/JPA tự gán khi lưu vào DB
        deepCopiedCategory.setId(null);

        // Lưu bản sao vào DB
        return categoryRepository.save(deepCopiedCategory);
    }

}
4.3. So Sánh ModelMapper và Builder Pattern
Tiêu Chí ModelMapper Builder Pattern
Tự Động hóa Rất cao, giảm mã lặp lại Thấp, cần viết mã thủ công
Kiểm Soát Sao Chép Ít kiểm soát, khó thực hiện sao chép sâu Cao, dễ dàng tùy chỉnh quá trình sao chép sâu
Quản Lý Vòng Lặp Khó khăn, cần cấu hình đặc biệt để tránh vòng lặp Dễ dàng kiểm soát và ngăn ngừa vòng lặp
Hiệu Suất Tốt với cấu hình đúng Tốt, nhưng có thể phức tạp hơn với nhiều thuộc tính
Linh Hoạt Thấp hơn trong việc tùy chỉnh so với Builder Pattern Cao, dễ dàng thêm các logic tùy chỉnh
Dễ Bảo Trì Tự động hóa giúp giảm lỗi lặp lại Mã rõ ràng nhưng cần bảo trì khi cấu trúc thay đổi
Ứng Dụng Cụ Thể Từ các DTO tới Entity, sao chép nông Tạo các bản sao sâu, quản lý mối quan hệ phức tạp
Kết Luận:

ModelMapper rất hữu ích khi bạn cần sao chép nông hoặc định tuyến ánh xạ giữa các lớp khác nhau (như DTO và Entity). Tuy
nhiên, khi cần sao chép sâu hoặc xử lý các mối quan hệ phức tạp, Builder Pattern thường linh hoạt hơn và dễ dàng kiểm
soát hơn.
Builder Pattern thích hợp hơn khi bạn cần tạo các bản sao độc lập và tùy chỉnh, đặc biệt trong các cấu trúc dữ liệu phức
tạp như cây phân cấp danh mục sản phẩm của Tech-Shop.

5. Lời Khuyên Khi Sử Dụng Sao Chép Trong Tech-Shop
   Xem Xét Nhu Cầu Sao Chép:
   Sao Chép Nông: Khi bạn chỉ cần tạo bản sao đơn giản, không cần giữ nguyên các mối quan hệ phức tạp hoặc khi mối quan
   hệ giữa gốc và bản sao là chia sẻ.
   Sao Chép Sâu: Khi bạn cần bản sao hoàn toàn độc lập, với các mối quan hệ mới, không ảnh hưởng đến gốc.
   Tuân Thủ Kiến Trúc Dữ Liệu:
   Đảm bảo rằng các mối quan hệ giữa các entity được thiết lập đúng cách để dễ dàng quản lý sao chép.
   Quản Lý ID và Khóa Chính:
   Tránh sao chép ID để Hibernate/JPA tự động gán ID mới, đảm bảo tính duy nhất của mỗi bản ghi trong cơ sở dữ liệu.
   Kiểm Tra và Kiểm Thử:
   Đảm bảo rằng các phương thức sao chép hoạt động đúng như mong đợi thông qua việc viết các kiểm thử (unit tests) và
   kiểm tra dữ liệu trong cơ sở dữ liệu sau khi sao chép.
   Sử Dụng Thư Viện Hỗ Trợ Khi Cần Thiết:
   Nếu dự án của bạn yêu cầu sao chép sâu và phức tạp, cân nhắc sử dụng các thư viện như ModelMapper kết hợp với
   customizations hoặc Builder Pattern để đạt được hiệu suất tốt nhất.
6. Tổng Kết
   Trong bối cảnh dự án Tech-Shop, việc lựa chọn giữa sao chép nông và sao chép sâu phụ thuộc vào mục tiêu và cách bạn
   quản lý dữ liệu danh mục sản phẩm của mình. Sao chép nông mang lại sự đơn giản và tốc độ, phù hợp cho các tình huống
   không yêu cầu độc lập giữa bản sao và gốc. Sao chép sâu, dù phức tạp hơn, lại cung cấp sự linh hoạt và kiểm soát cao
   hơn, đảm bảo tính độc lập hoàn toàn giữa các bản sao, rất quan trọng trong việc quản lý danh mục sản phẩm và các mối
   quan hệ phức tạp trong ứng dụng thương mại điện tử.