1. Phân Tích Chi Tiết Các Phương Thức CopyFull
   1.1. Phương Thức CopyFull(Category category)

   public static Category CopyFull(Category category) {
   Category copyCate = new Category();
   copyCate.setId(category.getId());
   copyCate.setName(category.getName());
   copyCate.setAlias(category.getAlias());
   copyCate.setImage(category.getImage());
   copyCate.setEnabled(category.isEnabled());
   copyCate.setParent(category.getParent());
   copyCate.setChildren(category.getChildren());
   copyCate.setHasChildren(category.getChildren().size() > 0);
   return copyCate;
   }


Mục Đích:
Tạo một bản sao (copy) đầy đủ của đối tượng Category hiện tại, bao gồm tất cả các thuộc tính và mối quan hệ liên quan.
Cách Hoạt Động:
Tạo Đối Tượng Mới:
Category copyCate = new Category();
Tạo một instance mới của Category.
Sao Chép Các Thuộc Tính:
ID: copyCate.setId(category.getId());
Sao chép giá trị id từ đối tượng gốc sang bản sao.
Tên: copyCate.setName(category.getName());
Sao chép name.
Alias: copyCate.setAlias(category.getAlias());
Sao chép alias.
Hình Ảnh: copyCate.setImage(category.getImage());
Sao chép image.
Trạng Thái: copyCate.setEnabled(category.isEnabled());
Sao chép enabled.
Sao Chép Quan Hệ Cha-Con:
Cha: copyCate.setParent(category.getParent());
Sao chép tham chiếu đến đối tượng cha.
Con: copyCate.setChildren(category.getChildren());
Sao chép tập hợp các đối tượng con.
hasChildren: copyCate.setHasChildren(category.getChildren().size() > 0);
Đặt giá trị hasChildren dựa trên số lượng đối tượng con.
Trả Về Bản Sao:
return copyCate;
Ý Nghĩa:
Shallow Copy (Sao Chép Nông):
Các thuộc tính đơn giản như id, name, alias, v.v., được sao chép giá trị.
Các thuộc tính liên quan đến mối quan hệ (parent và children) được sao chép bằng cách tham chiếu trực tiếp đến các đối
tượng gốc, không tạo bản sao mới cho các đối tượng này.
1.2. Phương Thức CopyFull(Category category, String name)

public static Category CopyFull(Category category, String name) {
Category copyCate = CopyFull(category);
copyCate.setName(name);
return copyCate;
}
Mục Đích:
Tạo một bản sao đầy đủ của đối tượng Category hiện tại nhưng với tên (name) được thay đổi.
Cách Hoạt Động:
Gọi Phương Thức Sao Chép Đầu Tiên:
Category copyCate = CopyFull(category);
Sử dụng phương thức CopyFull(Category category) để tạo một bản sao đầy đủ.
Thay Đổi Tên:
copyCate.setName(name);
Đặt lại tên (name) của bản sao thành giá trị mới được cung cấp thông qua tham số name.
Trả Về Bản Sao Với Tên Mới:
return copyCate;
Ý Nghĩa:
Tái Sử Dụng và Tùy Chỉnh:
Phương thức này tái sử dụng phương thức CopyFull đầu tiên để tạo bản sao và sau đó thay đổi một thuộc tính quan trọng (
tên) mà không ảnh hưởng đến bản gốc.

2. Ví Dụ Cụ Thể Ứng Dụng Các Phương Thức CopyFull
   Để minh họa cụ thể cách hoạt động của các phương thức, chúng ta hãy xét một tình huống thực tế trong ứng dụng của
   bạn.

2.1. Giả Định Các Dữ Liệu Ban Đầu
Giả sử chúng ta có các đối tượng Category như sau:

Category A
id: 1
name: "Điện tử"
alias: "dien-tu"
image: "electronics.png"
enabled: true
parent: null (là danh mục gốc)
children: [Category B, Category C]
hasChildren: true
Category B
id: 2
name: "Điện thoại"
alias: "dien-thoai"
image: "phones.png"
enabled: true
parent: Category A
children: []
hasChildren: false
Category C
id: 3
name: "Laptop"
alias: "laptop"
image: "laptops.png"
enabled: true
parent: Category A
children: []
hasChildren: false
2.2. Sử Dụng Phương Thức CopyFull(Category category)
Giả sử bạn muốn tạo một bản sao đầy đủ của Category A (Điện tử):

Category originalCategory = categoryRepository.findById(1).orElseThrow();
Category copiedCategory = Category.CopyFull(originalCategory);

// Thay đổi một số thuộc tính của bản sao nếu cần
copiedCategory.setId(4); // Giả sử tạo mới với ID khác
copiedCategory.setAlias("dien-tu-sao-chep");
Kết Quả:
copiedCategory sẽ có các thuộc tính giống như originalCategory nhưng với các thuộc tính id và alias được thay đổi.
parent: null (giống originalCategory)
children: [Category B, Category C] (chứa tham chiếu đến các danh mục con của originalCategory)
hasChildren: true
Lưu Ý:
copiedCategory.getChildren() sẽ chứa Category B và Category C như trong bản gốc. Tuy nhiên, đây chỉ là bản sao nông (
shallow copy), nghĩa là children trong copiedCategory vẫn là tham chiếu đến Category B và Category C của bản gốc. Điều
này có nghĩa là thay đổi trong children của bản sao sẽ ảnh hưởng đến danh mục con của bản gốc.
2.3. Sử Dụng Phương Thức CopyFull(Category category, String name)
Giả sử bạn muốn tạo một bản sao của Category A nhưng với tên mới "Thiết bị gia dụng":

Category originalCategory = categoryRepository.findById(1).orElseThrow();
Category copiedCategory = Category.CopyFull(originalCategory, "Thiết bị gia dụng");

// Thay đổi các thuộc tính bổ sung nếu cần
copiedCategory.setId(4); // Giả sử tạo mới với ID khác
copiedCategory.setAlias("thiet-bi-gia-dung");
Kết Quả:
copiedCategory sẽ có các thuộc tính giống như originalCategory nhưng với name và alias được thay đổi.
name: "Thiết bị gia dụng"
alias: "thiet-bi-gia-dung"
parent: null (giống originalCategory)
children: [Category B, Category C]
hasChildren: true
Lưu Ý:
copiedCategory.getChildren() vẫn chứa những danh mục con của bản gốc (Category B và Category C).
Thay đổi name của bản sao không ảnh hưởng đến bản gốc.
2.4. Hạn Chế và Rủi Ro Tiềm Ẩn
2.4.1. Shallow Copy vs. Deep Copy
Shallow Copy (Sao Chép Nông):
Các thuộc tính liên quan đến đối tượng khác (như parent và children) chỉ được sao chép bằng cách tham chiếu trực tiếp
đến đối tượng gốc.
Rủi Ro: Thay đổi trong mối quan hệ cha-con của bản sao sẽ ảnh hưởng đến bản gốc và ngược lại.
Deep Copy (Sao Chép Sâu):
Tạo các bản sao mới cho các đối tượng liên quan (parent và children), đảm bảo sự độc lập giữa bản sao và bản gốc.
Lợi Ích: Đảm bảo rằng sự thay đổi trong bản sao không ảnh hưởng đến bản gốc.
Thách Thức: Cần xử lý đệ quy để sao chép toàn bộ cây danh mục, tránh đạn kính vòng lặp và đảm bảo tính nhất quán của dữ
liệu.
2.4.2. Tránh Đặt Lại ID Trên Bản Sao
Vấn Đề: Trong JPA, việc đặt lại id trên đối tượng mới có thể gây ra các vấn đề về quản lý bản ghi và khóa chính.
Giải Pháp: Thay vì sao chép id, hãy để Hibernate/JPA tự động gán id mới khi bạn lưu bản sao vào cơ sở dữ liệu. Ví Dụ:

public static Category CopyFull(Category category) {
Category copyCate = new Category();
// Không sao chép id
// copyCate.setId(category.getId());
copyCate.setName(category.getName());
copyCate.setAlias(category.getAlias());
copyCate.setImage(category.getImage());
copyCate.setEnabled(category.isEnabled());
copyCate.setParent(category.getParent());
copyCate.setChildren(new LinkedHashSet<>(category.getChildren()));
copyCate.setHasChildren(category.getChildren().size() > 0);
return copyCate;
}
Lý Do: Để đảm bảo rằng bản sao mới được lưu với một khóa chính duy nhất và không trùng lặp với bất kỳ bản ghi nào trong
cơ sở dữ liệu.
2.5. Ví Dụ Thực Tế với Deep Copy
Nếu bạn cần một bản sao hoàn toàn độc lập (deep copy), bạn có thể triển khai phương thức sao chép sâu như sau:

public static Category DeepCopyFull(Category category) {
if (category == null) {
return null;
}

    Category copyCate = new Category();
    // Không sao chép id để Hibernate/JPA tự gán
    copyCate.setName(category.getName());
    copyCate.setAlias(category.getAlias());
    copyCate.setImage(category.getImage());
    copyCate.setEnabled(category.isEnabled());
    // Sao chép cha (nếu cần)
    // copyCate.setParent(DeepCopyFull(category.getParent())); // Có thể gây đạn kính vòng lặp

    // Sao chép các danh mục con
    Set<Category> copiedChildren = new LinkedHashSet<>();
    for (Category child : category.getChildren()) {
        Category copiedChild = DeepCopyFull(child);
        copiedChild.setParent(copyCate); // Thiết lập cha cho bản sao
        copiedChildren.add(copiedChild);
    }
    copyCate.setChildren(copiedChildren);
    copyCate.setHasChildren(copiedChildren.size() > 0);

    return copyCate;

}
Lưu Ý:

Đạn Kính Vòng Lặp: Nếu cây danh mục có các mối quan hệ vòng lặp (ví dụ: A → B → C → A), phương thức này sẽ tạo ra lặp vô
tận. Để tránh điều này, bạn cần quản lý các đối tượng đã sao chép bằng cách sử dụng một bản đồ tạm thời.
Không Sao Chép parent Trực Tiếp: Trong ví dụ trên, việc sao chép parent có thể gây ra vòng lặp. Thay vào đó, bạn có thể
chỉ cần thiết lập parent cho các bản sao con dựa trên bản sao cha mới.
2.6. Sử Dụng Lombok để Giảm Bớt Mã Lặp Lại
Nếu bạn sử dụng Lombok, bạn có thể đơn giản hóa mã của bạn bằng cách sử dụng các annotation như @Data, @Builder, v.v. để
tự động tạo getter, setter, và các phương thức tiện ích khác.

Ví Dụ:

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    // Các thuộc tính khác...

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name ASC")
    private Set<Category> subCategories = new LinkedHashSet<>();

    private boolean hasChildren;

    // Phương thức tiện ích...

}
Ưu Điểm:
Giảm Bớt Mã Lặp Lại: Lombok tự động tạo getter, setter, constructor, và các phương thức khác, giúp mã ngắn gọn và dễ bảo
trì hơn.

3. Tổng Kết và Khuyến Nghị
   3.1. Mục Tiêu của Các Phương Thức CopyFull:
   CopyFull(Category category): Tạo một bản sao nông (shallow copy) của đối tượng Category, bao gồm các thuộc tính và
   mối quan hệ liên quan.
   CopyFull(Category category, String name): Tạo một bản sao nông của đối tượng Category với tên (name) được thay đổi.
   3.2. Khi Nào Nên Sử Dụng Mỗi Phương Thức:
   Sử Dụng CopyFull(Category category):
   Khi bạn muốn tạo một bản sao nhanh chóng của một đối tượng Category mà không cần thay đổi bất kỳ thuộc tính nào.
   Khi bạn không lo lắng về việc bản sao và bản gốc chia sẻ các đối tượng con hoặc cha (ví dụ: trong các tình huống
   không cần sự độc lập giữa các đối tượng).
   Sử Dụng CopyFull(Category category, String name):
   Khi bạn muốn tạo một bản sao của Category nhưng với tên (name) khác (ví dụ: tạo một biến thể mới dựa trên danh mục
   gốc).
   Khi bạn cần thay đổi một số thuộc tính nhất định của bản sao mà không ảnh hưởng đến bản gốc.
   3.3. Khuyến Nghị:
   Xác Định Nhu Cầu Sao Chép:
   Nếu bạn cần một bản sao hoàn toàn độc lập (deep copy), hãy triển khai một phương thức sao chép sâu để tránh các vấn
   đề về tham chiếu chung.
   Nếu sao chép nông (shallow copy) đủ cho nhu cầu của bạn, hãy đảm bảo rằng bạn hiểu rõ các rủi ro liên quan đến việc
   chia sẻ tham chiếu đối tượng.
   Tránh Sao Chép ID Nếu Không Cần Thiết:
   Trong hầu hết các trường hợp, khi tạo bản sao để lưu vào cơ sở dữ liệu, bạn nên để Hibernate/JPA tự động gán id mới
   cho bản sao để tránh xung đột khóa chính.
   Sử Dụng Phương Thức Tiện Ích Để Quản Lý Mối Quan Hệ:
   Khi tạo bản sao, hãy đảm bảo rằng các mối quan hệ cha-con được quản lý đúng cách để duy trì tính nhất quán của cấu
   trúc cây.
   Sử Dụng Thư Viện Hỗ Trợ Nếu Cần Thiết:
   Các thư viện như ModelMapper hoặc MapStruct có thể giúp tự động hóa và đơn giản hóa quá trình tạo bản sao đối tượng,
   giảm thiểu khả năng lỗi.
   Kiểm Tra và Xử Lý Các Trường Hợp Đặc Biệt:
   Đảm bảo rằng các thuộc tính phức tạp (như mối quan hệ nhiều-nhiều, vòng lặp) được xử lý đúng cách trong quá trình sao
   chép để tránh các lỗi không mong muốn.
4. Ví Dụ Thực Tế Đã Hoàn Chỉnh
   Dưới đây là một ví dụ hoàn chỉnh minh họa cách sử dụng các phương thức CopyFull, bao gồm cả việc tránh sao chép ID và
   quản lý mối quan hệ cha-con một cách đúng đắn.

4.1. Entity Category với Các Phương Thức Sao Chép

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
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

    /**
     * Sao chép nông một Category.
     * Không sao chép ID để tránh xung đột khi lưu vào DB.
     */
    public static Category CopyFull(Category category) {
        if (category == null) {
            return null;
        }

        Category copyCate = new Category();
        // Không sao chép id để Hibernate/JPA tự gán
        copyCate.setName(category.getName());
        copyCate.setAlias(category.getAlias());
        copyCate.setImage(category.getImage());
        copyCate.setEnabled(category.isEnabled());

        // Sao chép cha (chỉ tham chiếu, không tạo bản sao mới)
        copyCate.setParent(category.getParent());

        // Sao chép các danh mục con (tham chiếu)
        copyCate.setSubCategories(new LinkedHashSet<>(category.getSubCategories()));
        copyCate.setHasChildren(category.getSubCategories().size() > 0);

        return copyCate;
    }

    /**
     * Sao chép nông một Category với tên đã thay đổi.
     */
    public static Category CopyFull(Category category, String name) {
        Category copyCate = CopyFull(category);
        copyCate.setName(name);
        return copyCate;
    }

}
4.2. Ví Dụ Sử Dụng Phương Thức Sao Chép Trong Service
Giả sử bạn có một service để quản lý danh mục và muốn sao chép một danh mục hiện có để tạo một danh mục mới dựa trên bản
gốc nhưng với tên khác.

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Tạo một bản sao của danh mục hiện có với tên mới.
     *
     * @param originalCategoryId ID của danh mục gốc
     * @param newName            Tên mới cho danh mục sao chép
     * @return Danh mục mới đã được lưu vào cơ sở dữ liệu
     */
    public Category copyCategory(Integer originalCategoryId, String newName) {
        // Tìm danh mục gốc theo ID
        Category originalCategory = categoryRepository.findById(originalCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category với ID: " + originalCategoryId));

        // Tạo bản sao với tên mới
        Category copiedCategory = Category.CopyFull(originalCategory, newName);

        // Lưu bản sao vào DB
        return categoryRepository.save(copiedCategory);
    }

}
4.3. Sử Dụng Controller Để Gọi Service

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Endpoint để sao chép một danh mục với tên mới.
     *
     * @param originalCategoryId ID của danh mục gốc
     * @param newName            Tên mới cho danh mục sao chép
     * @return Danh mục mới đã được tạo
     */
    @PostMapping("/copy/{id}")
    public ResponseEntity<Category> copyCategory(
            @PathVariable("id") Integer originalCategoryId,
            @RequestParam("name") String newName) {

        Category newCategory = categoryService.copyCategory(originalCategoryId, newName);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

}
4.4. Quy Trình Hoạt Động
Gửi Yêu Cầu tới API:
Bạn gửi một yêu cầu POST tới endpoint /categories/copy/1 với tham số name=Thiết bị gia dụng.
Ví Dụ: Sử dụng cURL:

curl -X POST "http://localhost:8080/categories/copy/1?name=Thiết%20bị%20gia%20dụng"
Service Thực Thi Phép Sao Chép:
Tìm Danh Mục Gốc: Service tìm danh mục có id=1 (Category A: "Điện tử").
Tạo Bản Sao: Gọi CopyFull(originalCategory, "Thiết bị gia dụng"):
copyCate.setName("Thiết bị gia dụng");
Lưu Bản Sao: Service lưu bản sao vào cơ sở dữ liệu. Hibernate/JPA tự động gán id mới cho bản sao.
Kết Quả:
Bản Sao Mới:
id: 4 (giả sử mới được gán)
name: "Thiết bị gia dụng"
alias: "dien-tu-sao-chep" (nếu bạn thay đổi)
image: "electronics.png"
enabled: true
parent: null (giống bản gốc)
children: [Category B, Category C] (chứa tham chiếu đến các danh mục con của bản gốc)
hasChildren: true
4.5. Quy Trình Test
Để đảm bảo rằng các phương thức sao chép hoạt động như mong đợi, bạn có thể thực hiện các bước kiểm thử sau:

Tạo Dữ Liệu Ban Đầu:
Tạo danh mục gốc và các danh mục con trong cơ sở dữ liệu.
Sao Chép Danh Mục:
Gửi yêu cầu sao chép danh mục thông qua API hoặc trực tiếp gọi phương thức trong unit test.
Kiểm Tra Kết Quả:
Kiểm tra xem bản sao đã được tạo đúng với các thuộc tính cần thiết.
Đảm bảo rằng các mối quan hệ cha-con được sao chép đúng cách.
Kiểm tra xem bản sao có id mới và không bị trùng với bất kỳ bản ghi nào trong cơ sở dữ liệu.
Ví Dụ Unit Test Sử Dụng JUnit và Mockito:

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCopyCategory() {
        // Giả sử đã có Category với ID=1 trong DB
        Integer originalId = 1;
        String newName = "Thiết bị gia dụng";

        // Gọi phương thức sao chép
        Category newCategory = categoryService.copyCategory(originalId, newName);

        // Kiểm tra rằng bản sao đã được tạo
        assertNotNull(newCategory);
        assertNotNull(newCategory.getId()); // ID mới được gán
        assertEquals(newName, newCategory.getName());
        assertNotEquals(originalId, newCategory.getId());

        // Kiểm tra các thuộc tính khác
        assertEquals("dien-tu", newCategory.getAlias());
        assertEquals("electronics.png", newCategory.getImage());
        assertTrue(newCategory.isEnabled());

        // Kiểm tra mối quan hệ cha-con
        assertNull(newCategory.getParent());
        assertFalse(newCategory.getSubCategories().isEmpty());
        assertTrue(newCategory.isHasChildren());

        // Kiểm tra rằng các danh mục con vẫn liên kết đến danh mục cha gốc
        for (Category child : newCategory.getSubCategories()) {
            assertEquals(originalId, child.getParent().getId());
        }
    }

}

5. Cải Tiến Phương Thức Sao Chép
   Để nâng cao độ an toàn và tính linh hoạt của các phương thức sao chép, bạn có thể áp dụng một số cải tiến sau:

5.1. Thực Hiện Deep Copy (Sao Chép Sâu)
Nếu bạn cần tạo một bản sao hoàn toàn độc lập, bao gồm việc tạo bản sao mới cho các danh mục con và thiết lập mối quan
hệ cha-con mới, bạn có thể triển khai sao chép sâu:

public static Category DeepCopyFull(Category category) {
if (category == null) {
return null;
}

    Category copyCate = new Category();
    // Không sao chép id để Hibernate/JPA tự gán
    copyCate.setName(category.getName());
    copyCate.setAlias(category.getAlias());
    copyCate.setImage(category.getImage());
    copyCate.setEnabled(category.isEnabled());

    // Không sao chép cha để tránh đạn kính vòng lặp
    copyCate.setParent(null);

    // Sao chép từng danh mục con
    Set<Category> copiedChildren = new LinkedHashSet<>();
    for (Category child : category.getSubCategories()) {
        Category copiedChild = DeepCopyFull(child);
        copiedChild.setParent(copyCate);
        copiedChildren.add(copiedChild);
    }
    copyCate.setSubCategories(copiedChildren);
    copyCate.setHasChildren(copiedChildren.size() > 0);

    return copyCate;

}
Lưu Ý:

Tránh Sao Chép Cha: Trong sao chép sâu, bạn không nên sao chép lại cha (parent) để tránh tạo ra các đối tượng vòng lặp.
Đảm Bảo Đủ Điều Kiện Dừng (Base Case): Đảm bảo rằng khi đạt đến danh mục không có con (leaf node), phương thức sẽ ngừng
đệ quy.
Độc Lập Đối Tượng: Bản sao sâu sẽ hoàn toàn độc lập với bản gốc.
5.2. Sử Dụng Thư Viện Hỗ Trợ
Các thư viện như ModelMapper hoặc MapStruct có thể giúp tự động hóa quá trình sao chép đối tượng, giảm thiểu mã lặp lại
và xử lý các mối quan hệ phức tạp.

Ví Dụ Sử Dụng ModelMapper:

import org.modelmapper.ModelMapper;

public class CategoryMapper {
private static final ModelMapper modelMapper = new ModelMapper();

    public static Category copyFull(Category category) {
        return modelMapper.map(category, Category.class);
    }

    public static Category copyFull(Category category, String name) {
        Category copyCate = copyFull(category);
        copyCate.setName(name);
        return copyCate;
    }

}
Lưu Ý:

Cấu Hình Chi Tiết: Bạn có thể cần cấu hình chi tiết hơn nếu các mối quan hệ phức tạp hoặc đòi hỏi xử lý đặc biệt.
Hiệu Suất: Các thư viện này thường được tối ưu hóa để xử lý hiệu quả việc sao chép đối tượng.
5.3. Sử Dụng Builder Pattern
Sử dụng Builder Pattern để quản lý việc tạo bản sao một cách linh hoạt và dễ bảo trì.

public class CategoryBuilder {
private Category category;

    public CategoryBuilder(Category original) {
        category = new Category();
        category.setName(original.getName());
        category.setAlias(original.getAlias());
        category.setImage(original.getImage());
        category.setEnabled(original.isEnabled());
        category.setParent(original.getParent());
        category.setHasChildren(original.getSubCategories().size() > 0);
    }

    public CategoryBuilder withName(String name) {
        category.setName(name);
        return this;
    }

    public Category build() {
        return category;
    }

}

// Sử dụng
Category copiedCategory = new CategoryBuilder(originalCategory)
.withName("Thiết bị gia dụng")
.build();
Lợi Ích:

Tính Linh Hoạt: Cho phép bạn dễ dàng tùy chỉnh các thuộc tính khi tạo bản sao.
Dễ Dàng Bảo Trì: Giảm thiểu việc lặp lại mã và giúp mã rõ ràng hơn.

6. Những Lưu Ý Khi Sao Chép Đối Tượng trong JPA/Hibernate
   6.1. Quản Lý Các Mối Quan Hệ
   Cascade Operations:
   Đảm bảo rằng các mối quan hệ như @OneToMany với cascade = CascadeType.ALL được cấu hình đúng để tự động lưu hoặc xóa
   các danh mục con khi thao tác trên danh mục cha.
   Orphan Removal:
   Thiết lập orphanRemoval = true để tự động xóa các đối tượng con khi chúng bị loại bỏ khỏi tập hợp subCategories.
   6.2. Avoid Circular References (Đạn Kính Vòng Lặp)
   Khi sao chép các đối tượng có quan hệ cha-con vòng lặp, cần cẩn thận để tránh tạo ra các vòng lặp vô tận trong quá
   trình sao chép hoặc serialization.
   6.3. Lazy Loading và FetchType
   FetchType.LAZY: Trong mối quan hệ @ManyToOne và @OneToMany, Hibernate/JPA thường sử dụng FetchType.LAZY để không tải
   dữ liệu liên quan ngay khi truy vấn đối tượng cha.
   Đảm Bảo Tính Hiệu Quả:
   Khi sao chép đối tượng và các mối quan hệ, hãy đảm bảo rằng các đối tượng liên quan đã được tải (eagerly fetched)
   hoặc xử lý đúng cách để tránh LazyInitializationException.
   6.4. Spring Transaction Management
   Đảm bảo rằng các thao tác sao chép và lưu đối tượng được thực hiện trong cùng một giao dịch để duy trì tính nhất quán
   của dữ liệu.
7. Kết Luận
   Các phương thức CopyFull bạn đã triển khai có mục đích tạo bản sao của đối tượng Category, giúp tiết kiệm thời gian
   và tăng tính linh hoạt trong quản lý dữ liệu.

Ưu Điểm:
Tái Sử Dụng Dữ Liệu: Giúp dễ dàng tạo các phiên bản mới của các danh mục dựa trên các danh mục hiện có.
Quản Lý Mối Quan Hệ: Đảm bảo rằng các mối quan hệ cha-con được sao chép đúng cách, giúp duy trì cấu trúc cây phân cấp.
Nhược Điểm:
Shallow Copy: Các mối quan hệ cha-con được sao chép bằng cách tham chiếu, có thể gây ra sự thay đổi không mong muốn giữa
bản sao và bản gốc.
Quản Lý ID: Nếu bạn không cẩn thận trong việc quản lý id, có thể gây ra xung đột khi lưu vào cơ sở dữ liệu.
Khuyến Nghị:
Xác Định Rõ Ràng Mục Đích Sao Chép: Hiểu rõ nhu cầu của bạn để quyết định nên sử dụng shallow copy hay deep copy.
Triển Khai Deep Copy Nếu Cần: Nếu cần sự độc lập hoàn toàn giữa bản sao và bản gốc, hãy triển khai deep copy với các
kiểm soát cần thiết.
Sử Dụng Thư Viện Hỗ Trợ: Các thư viện như ModelMapper hoặc MapStruct có thể giúp bạn sao chép các đối tượng một cách
hiệu quả và ít lỗi hơn.
Kiểm Tra và Kiểm Thử: Luôn kiểm tra kỹ lưỡng các phương thức sao chép để đảm bảo rằng chúng hoạt động như mong đợi và
không gây ra các vấn đề về dữ liệu.