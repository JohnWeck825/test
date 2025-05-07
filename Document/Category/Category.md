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

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy(value = "name asc")
    private Set<Category> children = new HashSet<>();

    @Column(name = "all_parent_ids", length = 256)
    private String allParentIDs;

    // Getters và Setters (được ẩn đi trong đoạn mã)

}

1. Tổng Quan Về Lớp Category
   Lớp Category đại diện cho một danh mục trong hệ thống, có thể là danh mục sản phẩm, bài viết, hoặc bất kỳ loại phân
   loại nào khác tùy thuộc vào ứng dụng của bạn. Lớp này bao gồm các thuộc tính như id, name, alias, image, trạng thái
   enabled, và mối quan hệ tự tham chiếu để biểu thị cấu trúc cây của các danh mục (parent-child).

2. Giải Thích Chi Tiết Các Thành Phần
   2.1. Annotation @Entity và @Table
   @Entity:
   Chỉ định rằng lớp này là một entity của JPA (Java Persistence API), tức là sẽ được ánh xạ đến một bảng trong cơ sở dữ
   liệu.
   @Table(name = "categories"):
   Xác định tên bảng trong cơ sở dữ liệu mà entity này sẽ được ánh xạ đến. Trong trường hợp này, bảng sẽ có tên là
   categories.
   2.2. Thuộc Tính id

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
@Id:
Đánh dấu trường này là khóa chính của bảng.
@GeneratedValue(strategy = GenerationType.IDENTITY):
Chiến lược tạo giá trị khóa chính tự động bởi cơ sở dữ liệu. IDENTITY thường được sử dụng cho các cột auto-increment
trong DB như MySQL.
private Integer id;:
Thuộc tính này lưu trữ giá trị khóa chính của mỗi bản ghi trong bảng categories.
2.3. Thuộc Tính name

@Column(length = 128, nullable = false, unique = true)
private String name;
@Column(...):
Tùy chỉnh cấu hình của cột trong bảng:
length = 128: Độ dài tối đa của chuỗi là 128 ký tự.
nullable = false: Không được phép để trống.
unique = true: Giá trị phải duy nhất trong bảng.
private String name;:
Tên của danh mục. Ví dụ: "Điện tử", "Thời trang", etc.
2.4. Thuộc Tính alias

@Column(length = 128, nullable = false, unique = true)
private String alias;
Có cấu hình tương tự như name.
private String alias;:
Một tên đại diện hoặc dường dẫn thân thiện cho danh mục. Thường dùng trong URL để tăng tính thân thiện với SEO. Ví dụ:
thay vì sử dụng id = 1, bạn có thể có đường dẫn /danh-muc/dien-tu.
2.5. Thuộc Tính image

@Column(length = 64, nullable = false)
private String image;
@Column(length = 64, nullable = false):
length = 64: Độ dài tối đa của chuỗi là 64 ký tự.
nullable = false: Không được phép để trống.
private String image;:
Đường dẫn hoặc tên file của hình ảnh đại diện cho danh mục. Ví dụ: "electronics.png".
2.6. Thuộc Tính enabled

private boolean enabled;
private boolean enabled;:
Trạng thái của danh mục, xác định xem danh mục này có đang hoạt động hay không.
true: Đang hoạt động, hiển thị cho người dùng.
false: Đang ẩn, không hiển thị.
2.7. Mối Quan Hệ Tự Tham Chiếu với parent

@OneToOne
@JoinColumn(name = "parent_id")
private Category parent;
@OneToOne:
Định nghĩa mối quan hệ một-một giữa các đối tượng Category. Tuy nhiên, trong trường hợp này, có vẻ như mối quan hệ này
nên là @ManyToOne vì có thể có nhiều danh mục con liên kết đến một danh mục cha.
@JoinColumn(name = "parent_id"):
Xác định cột parent_id trong bảng categories hoạt động như một khóa ngoại liên kết đến id của danh mục cha.
private Category parent;:
Thuộc tính này biểu thị danh mục cha của danh mục hiện tại. Nếu danh mục này là gốc (không có cha), parent sẽ là null.
Lưu ý: Thông thường, mối quan hệ giữa các danh mục cha và con là @ManyToOne (nhiều danh mục con có thể liên kết đến một
danh mục cha). Vì vậy, bạn có thể cân nhắc thay đổi @OneToOne thành @ManyToOne:

@ManyToOne
@JoinColumn(name = "parent_id")
private Category parent;
2.8. Mối Quan Hệ với children

@OneToMany(mappedBy = "parent")
@OrderBy(value = "name asc")
private Set<Category> children = new HashSet<>();
@OneToMany(mappedBy = "parent"):
Định nghĩa mối quan hệ một-nhiều, tức là một danh mục cha có thể có nhiều danh mục con.
mappedBy = "parent": Xác định rằng thuộc tính parent trong lớp Category là chủ sở hữu mối quan hệ này.
@OrderBy(value = "name asc"):
Sắp xếp các danh mục con theo tên theo thứ tự tăng dần khi truy xuất từ cơ sở dữ liệu.
private Set<Category> children = new HashSet<>();:
Tập hợp các danh mục con của danh mục hiện tại. Sử dụng Set để tránh các bản ghi trùng lặp và HashSet để tối ưu hóa việc
tra cứu.
2.9. Thuộc Tính allParentIDs

@Column(name = "all_parent_ids", length = 256)
private String allParentIDs;
@Column(name = "all_parent_ids", length = 256):
Xác định tên cột trong bảng là all_parent_ids với độ dài tối đa 256 ký tự.
private String allParentIDs;:
Thuộc tính này thường được sử dụng để lưu trữ tất cả các ID của các danh mục cha từ gốc đến hiện tại, thường dưới dạng
chuỗi phân cách (ví dụ: "1,3,5").
Ý nghĩa:
Giúp truy vấn nhanh các danh mục con hoặc phân cấp mà không cần phải thực hiện nhiều join.
Hỗ trợ các thao tác như kiểm tra xem một danh mục có nằm trong một phân cấp cụ thể hay không.
Lưu Ý: Việc lưu trữ toàn bộ các ID của cha mẹ trong một trường chuỗi có thể gây khó khăn trong việc duy trì khi danh mục
được di chuyển hoặc sửa đổi. Bạn có thể cân nhắc sử dụng các thư viện hỗ trợ cây như Hibernate Tree hoặc lưu trữ dưới
dạng các cấu trúc dữ liệu cây.

3. Một Số Đề Xuất và Cải Tiến
   3.1. Sử Dụng @ManyToOne Thay Vì @OneToOne cho parent
   Như đã đề cập ở phần 2.7, mối quan hệ cha-con trong danh mục thường là một-một nhiều (@ManyToOne), vì nhiều danh mục
   con có thể liên kết đến một danh mục cha.

Thay Đổi:

@ManyToOne
@JoinColumn(name = "parent_id")
private Category parent;
3.2. Xử Lý allParentIDs
Cập Nhật Tự Động:
Sử dụng các sự kiện của Hibernate như @PrePersist và @PreUpdate để tự động cập nhật giá trị của allParentIDs khi danh
mục được tạo hoặc cập nhật.
Ví Dụ:

@PrePersist
@PreUpdate
public void updateAllParentIDs() {
if (parent != null) {
this.allParentIDs = parent.getAllParentIDs() + "," + parent.getId();
} else {
this.allParentIDs = String.valueOf(this.id);
}
}
Lưu Ý:

Phải đảm bảo rằng id đã được tạo ra trước khi gọi phương thức này. Điều này có thể yêu cầu một số điều chỉnh trong quá
trình tạo entity.
3.3. Sử Dụng List Thay Vì Set Nếu Có Thứ Tự Cụ Thể Muốn Giữ
Nếu danh mục con cần có thứ tự cụ thể hoặc có thể chứa các giá trị trùng lặp (nếu cần), bạn có thể sử dụng List thay vì
Set.
Ví Dụ:

@OneToMany(mappedBy = "parent")
@OrderBy("name ASC")
private List<Category> children = new ArrayList<>();
3.4. Thêm Các Biến Vùng Cấu Hình Khác (Optional)
Biến Vùng Ngày Tạo và Ngày Cập Nhật:
Thêm các thuộc tính như createdAt và updatedAt để theo dõi thời gian tạo và cập nhật danh mục.
Ví Dụ:

@Column(name = "created_at", nullable = false, updatable = false)
@Temporal(TemporalType.TIMESTAMP)
@CreatedDate
private Date createdAt;

@Column(name = "updated_at")
@Temporal(TemporalType.TIMESTAMP)
@LastModifiedDate
private Date updatedAt;
Sử Dụng Auditing:
Kích hoạt tính năng auditing của Spring Data JPA để tự động quản lý các thuộc tính này.
Cấu Hình:

@EntityListeners(AuditingEntityListener.class)
public class Category {
// Các thuộc tính và phương thức
}
Và trong lớp cấu hình:

@EnableJpaAuditing
@Configuration
public class JpaConfig {
// Cấu hình nếu cần
}
3.5. Thêm Phương Thức Hữu Ích
Phương Thức Để Thêm/Loại Bỏ Con:
Thêm các phương thức để quản lý mối quan hệ cha-con dễ dàng hơn.
Ví Dụ:

public void addChild(Category child) {
children.add(child);
child.setParent(this);
}

public void removeChild(Category child) {
children.remove(child);
child.setParent(null);
}
3.6. Sử Dụng @Data, @Getter, @Setter của Lombok (Tùy Chọn)
Để giảm bớt mã lặp lại, bạn có thể sử dụng thư viện Lombok để tự động tạo getter, setter, constructors, etc.
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
// Các thuộc tính
}
Lưu Ý:

Cần thêm dependency Lombok vào dự án và cấu hình IDE để hỗ trợ.

4. Ví Dụ Hoàn Chỉnh Với Những Cải Tiến
   Dưới đây là một ví dụ hoàn chỉnh về lớp Category với các đề xuất cải tiến được áp dụng:

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name ASC")
    private Set<Category> children = new HashSet<>();

    @Column(name = "all_parent_ids", length = 256)
    private String allParentIDs;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @PrePersist
    @PreUpdate
    public void updateAllParentIDs() {
        if (parent != null) {
            this.allParentIDs = parent.getAllParentIDs() + "," + parent.getId();
        } else {
            this.allParentIDs = String.valueOf(this.id);
        }
    }

    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }

}
Giải Thích Các Cải Tiến:

@Data, @NoArgsConstructor từ Lombok: Giảm bớt mã getter, setter và constructor.
@EntityListeners(AuditingEntityListener.class) cùng với các thuộc tính createdAt và updatedAt: Quản lý tự động thời gian
tạo và cập nhật.
@ManyToOne thay vì @OneToOne: Phân loại mối quan hệ một-nhiều chính xác hơn.
Phương Thức addChild và removeChild: Quản lý mối quan hệ cha-con một cách dễ dàng và tránh lỗi.
cascade = CascadeType.ALL và orphanRemoval = true:
cascade = CascadeType.ALL: Tự động áp dụng các thao tác (persist, remove, etc.) lên các đối tượng con khi thực hiện trên
đối tượng cha.
orphanRemoval = true: Khi một danh mục con được loại bỏ khỏi tập hợp children, nó sẽ được xóa khỏi cơ sở dữ liệu.

1. So Sánh Hai Đoạn Mã
   Đoạn Mã 1: Sử Dụng @OneToOne và @OneToMany

@OneToOne
@JoinColumn(name = "parent_id")
private Category parent;

@OneToMany(mappedBy = "parent")
@OrderBy(value = "name asc")
private Set<Category> subCategories = new LinkedHashSet<>();
Đoạn Mã 2: Sử Dụng @ManyToOne và @OneToMany

@ManyToOne
@JoinColumn(name = "parent_id")
private Category parent;

@OneToMany(mappedBy = "parent")
@OrderBy(value = "name asc")
private Set<Category> subCategories = new LinkedHashSet<>();

2. Phân Tích Nguyên Nhân Gây Lỗi
   2.1. Mối Quan Hệ @OneToOne với @OneToMany
   Trong Đoạn Mã 1, bạn định nghĩa:

parent: Một đối tượng Category (mối quan hệ Một-Một).
subCategories: Một tập hợp các đối tượng Category (mối quan hệ Một-Nhiều).
Vấn Đề: Mối quan hệ Một-Một từ Category đến parent không tương thích với mối quan hệ Một-Nhiều từ parent đến
subCategories. JPA yêu cầu rằng hai bên của mối quan hệ phải đồng nhất về loại mối quan hệ. Sử dụng @OneToOne ở một bên
và @OneToMany ở bên kia dẫn đến sự không nhất quán, gây ra lỗi:

'One To One' attribute has incorrect opposite 'One To Many' attribute 'subCategories'.
'One To Many' attribute has incorrect opposite 'One To One' attribute 'parent'.
2.2. Mối Quan Hệ @ManyToOne với @OneToMany
Trong Đoạn Mã 2, bạn định nghĩa:

parent: Nhiều đối tượng Category có thể liên kết đến một đối tượng cha (mối quan hệ Nhiều-Một).
subCategories: Một tập hợp các đối tượng Category liên kết đến đối tượng cha (mối quan hệ Một-Nhiều).
Giải Thích:

@ManyToOne: Mỗi danh mục con (Category) chỉ có thể có một danh mục cha (parent), nhưng một danh mục cha có thể có nhiều
danh mục con.
@OneToMany: Mỗi danh mục cha (parent) có thể có nhiều danh mục con (subCategories).
Vì vậy, mối quan hệ @ManyToOne với @OneToMany là phù hợp và không gây ra lỗi như mối quan hệ @OneToOne với @OneToMany.

3. Cách Khắc Phục Lỗi và Triển Khai Đúng
   Để xây dựng cấu trúc cây danh mục (Category) đúng cách, bạn cần sử dụng mối quan hệ @ManyToOne và @OneToMany. Dưới
   đây là hướng dẫn chi tiết:

3.1. Sửa Đổi Entity Category
Entity Category Sử Dụng @ManyToOne và @OneToMany

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY) // Nhiều danh mục con có thể có một danh mục cha
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name ASC") // Sắp xếp danh mục con theo tên
    private Set<Category> subCategories = new LinkedHashSet<>();

    @Column(name = "all_parent_ids", length = 256)
    private String allParentIDs;

    // Getters và Setters
    // ...
    
    // Các phương thức tiện ích để quản lý mối quan hệ
    public void addSubCategory(Category subCategory) {
        subCategories.add(subCategory);
        subCategory.setParent(this);
    }

    public void removeSubCategory(Category subCategory) {
        subCategories.remove(subCategory);
        subCategory.setParent(null);
    }

}
3.2. Giải Thích Các Annotation và Thiết Lập
@ManyToOne trên thuộc tính parent:
Ý nghĩa: Nhiều danh mục con có thể liên kết đến một danh mục cha.
fetch = FetchType.LAZY: Tải danh mục cha một cách lười biếng (chỉ khi cần thiết).
@OneToMany trên thuộc tính subCategories:
mappedBy = "parent": Xác định rằng thuộc tính parent trong Category là chủ sở hữu mối quan hệ.
cascade = CascadeType.ALL: Áp dụng tất cả các thao tác (persist, merge, remove, etc.) lên các danh mục con khi thực hiện
trên danh mục cha.
orphanRemoval = true: Xóa danh mục con khỏi cơ sở dữ liệu khi nó được loại bỏ khỏi tập hợp subCategories.
@OrderBy("name ASC"): Sắp xếp danh mục con theo tên theo thứ tự tăng dần khi truy xuất.
3.3. Sử Dụng Methods Tiện Ích để Quản Lý Mối Quan Hệ
Việc thêm hoặc loại bỏ danh mục con nên được thực hiện thông qua các phương thức tiện ích để đảm bảo tính nhất quán của
mối quan hệ.

public void addSubCategory(Category subCategory) {
subCategories.add(subCategory);
subCategory.setParent(this);
}

public void removeSubCategory(Category subCategory) {
subCategories.remove(subCategory);
subCategory.setParent(null);
}
3.4. Sử Dụng @Data của Lombok (Tùy Chọn)
Để giảm bớt mã getter, setter, và các phương thức tiện ích khác, bạn có thể sử dụng Lombok:

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {
// Các thuộc tính như trên
// Các phương thức tiện ích như addSubCategory và removeSubCategory
}
Lưu Ý: Cần thêm dependency Lombok vào dự án và cấu hình IDE để hỗ trợ.

3.5. Cấu Hình Auditing (Optional)
Nếu bạn muốn theo dõi thời gian tạo và cập nhật danh mục, hãy thêm các thuộc tính như createdAt và updatedAt:

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
public class Category {

    // Các thuộc tính khác

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    // Getters và Setters
    // ...

}
Và kích hoạt auditing trong cấu hình Spring Boot:

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
// Các cấu hình khác nếu cần
}

4. Tổng Kết và Lời Khuyên
   4.1. Luôn Đồng Nhất Mối Quan Hệ giữa Hai Bên
   Khi bạn sử dụng @OneToMany từ bên cha, bên con phải sử dụng @ManyToOne.
   Mối quan hệ @OneToOne chỉ dùng khi mỗi đối tượng ở cả hai bên mối quan hệ chỉ liên kết với duy nhất một đối tượng ở
   bên kia.
   4.2. Đối Với Cấu Trúc Cây (Tree Structure)
   Cha - Con (Parent - Child Relationship):
   Bên Con (Category): Dùng @ManyToOne để liên kết đến cha (parent).
   Bên Cha (Category): Dùng @OneToMany để liên kết đến nhiều con (subCategories).
   4.3. Không Nên Mở Rộng Không Cần Thiết
   Tránh sử dụng @OneToOne khi mối quan hệ thực sự là @ManyToOne và @OneToMany. Điều này không chỉ gây lỗi mà còn khiến
   cấu trúc dữ liệu không chính xác.
   4.4. Kiểm Tra Các Mapper và Entity Khác
   Nếu bạn có thêm các entity khác liên quan đến Category, hãy đảm bảo rằng các mối quan hệ được định nghĩa chính xác để
   tránh xung đột.
   4.5. Triển Khai và Kiểm Tra
   Sau khi sửa đổi, thực hiện kiểm tra:

Chạy Ứng Dụng và Kiểm Tra Log:
Đảm bảo rằng không còn lỗi về mối quan hệ OneToOne và OneToMany.
Thực Hiện CRUD Operations:
Thêm danh mục cha và danh mục con để xác nhận rằng mối quan hệ hoạt động đúng.
Kiểm Tra Cơ Sở Dữ Liệu:
Kiểm tra các bảng và khóa ngoại để xác nhận rằng dữ liệu được lưu trữ đúng cách.
Ví Dụ Hoàn Chỉnh
Dưới đây là ví dụ hoàn chỉnh về entity Category với mối quan hệ @ManyToOne và @OneToMany:

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "all_parent_ids", length = 256)
    private String allParentIDs;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    // Phương thức tiện ích để quản lý mối quan hệ
    public void addSubCategory(Category subCategory) {
        subCategories.add(subCategory);
        subCategory.setParent(this);
    }

    public void removeSubCategory(Category subCategory) {
        subCategories.remove(subCategory);
        subCategory.setParent(null);
    }

    @PrePersist
    @PreUpdate
    public void updateAllParentIDs() {
        if (parent != null) {
            this.allParentIDs = parent.getAllParentIDs() + "," + parent.getId();
        } else {
            this.allParentIDs = String.valueOf(this.id);
        }
    }

}
Giải Thích Các Phần Mới
@EntityListeners(AuditingEntityListener.class): Để sử dụng các annotation @CreatedDate và @LastModifiedDate.
updateAllParentIDs: Cập nhật trường allParentIDs trước khi lưu hoặc cập nhật entity.
Phương Thức addSubCategory và removeSubCategory: Quản lý mối quan hệ cha-con một cách nhất quán.

5. Kết Luận
   Sử Dụng @ManyToOne và @OneToMany: Đối với cấu trúc cây như danh mục, mối quan hệ @ManyToOne từ con đến cha và
   @OneToMany từ cha đến con là phù hợp và đảm bảo tính nhất quán.
   Tránh Sử Dụng @OneToOne Kết Hợp với @OneToMany: Điều này gây ra sự không nhất quán và lỗi khi JPA cố gắng ánh xạ mối
   quan hệ một cách sai lệch.
   Sử Dụng Phương Thức Tiện Ích: Để quản lý mối quan hệ một cách nhất quán và tránh lỗi khi thêm hoặc loại bỏ danh mục
   con.

@OneToOne
@JoinColumn(name = "parent_id")
private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy(value = "name asc")
    private Set<Category> children = new HashSet<>();

chi tiết hơn về phần trên

Chắc chắn rồi! Hãy đi sâu hơn vào phần mã bạn đã chỉ định, tập trung vào hai trường quan trọng trong lớp Category:

@OneToOne
@JoinColumn(name = "parent_id")
private Category parent;

@OneToMany(mappedBy = "parent")
@OrderBy(value = "name asc")
private Set<Category> children = new HashSet<>();
Phần này thiết lập mối quan hệ giữa các đối tượng Category để thể hiện cấu trúc phân cấp (cây) của các danh mục. Hãy
phân tích từng annotation và cách chúng tương tác với nhau.

1. @OneToOne và @JoinColumn trên Trường parent

@OneToOne
@JoinColumn(name = "parent_id")
private Category parent;
a. @OneToOne
Định nghĩa: Annotation @OneToOne thiết lập mối quan hệ một-một giữa hai thực thể. Trong trường hợp này, một đối tượng
Category có thể có một đối tượng Category khác làm 'parent'.
Context Sử Dụng: Thông thường, mối quan hệ một-một được sử dụng khi hai thực thể có sự phụ thuộc chặt chẽ với nhau, như
giữa User và UserProfile.
b. @JoinColumn(name = "parent_id")
Định nghĩa: @JoinColumn xác định cột trong bảng categories sẽ được sử dụng để tạo ra mối quan hệ này. Ở đây, cột
parent_id sẽ chứa giá trị id của đối tượng Category cha.
Chi Tiết:
name = "parent_id": Tên cột trong bảng categories dùng để lưu trữ khóa ngoại (foreign key) tham chiếu đến id của danh
mục cha.
Owning Side: Trong mối quan hệ JPA, @JoinColumn đặt trên trường parent đánh dấu rằng đây là bên sở hữu (owning side) của
mối quan hệ. Bên sở hữu chịu trách nhiệm quản lý khóa ngoại trong cơ sở dữ liệu.
c. Thực tế Sử Dụng
Trong trường hợp cấu trúc phân cấp danh mục, mỗi danh mục thường có nhiều danh mục con, nhưng chỉ có một danh mục cha.
Vì vậy, mối quan hệ phù hợp hơn là Many-to-One (@ManyToOne) thay vì One-to-One (@OneToOne). Sử dụng @OneToOne ở đây có
thể dẫn đến việc mỗi danh mục chỉ có thể có một danh mục con duy nhất, điều này không phản ánh đúng mô hình thực tế.

Khuyến nghị: Thay vì @OneToOne, nên sử dụng @ManyToOne cho trường parent như sau:

@ManyToOne
@JoinColumn(name = "parent_id")
private Category parent;

2. @OneToMany và @OrderBy trên Trường children

@OneToMany(mappedBy = "parent")
@OrderBy(value = "name asc")
private Set<Category> children = new HashSet<>();
a. @OneToMany
Định nghĩa: @OneToMany thiết lập mối quan hệ một-nhiều giữa hai thực thể. Tại đây, một đối tượng Category (cha) có thể
có nhiều đối tượng Category (con).
mappedBy = "parent": Thuộc tính mappedBy chỉ định rằng mối quan hệ này được điều khiển bởi thuộc tính parent trong đối
tượng Category con. Điều này có nghĩa là bảng categories chỉ có một cột parent_id để quản lý mối quan hệ này, tránh việc
tạo thêm một bảng phụ.
Không phải Bên sở hữu: Vì mối quan hệ được điều khiển bởi parent, trường children là bên không sở hữu (non-owning side).
Bất kỳ thay đổi nào về mối quan hệ phải thông qua bên sở hữu (parent).
b. @OrderBy(value = "name asc")
Định nghĩa: @OrderBy xác định thứ tự sắp xếp các phần tử trong tập hợp khi truy vấn cơ sở dữ liệu.
value = "name asc": Sắp xếp các danh mục con theo trường name theo thứ tự tăng dần (ascending).
Hiệu quả: Điều này đảm bảo rằng khi bạn truy vấn danh mục cha và truy xuất các danh mục con, chúng sẽ luôn được sắp xếp
theo tên một cách nhất quán, giúp dễ dàng hiển thị hoặc xử lý.
c. Sử Dụng Set<Category> và HashSet
Set<Category>: Sử dụng Set để đảm bảo rằng không có danh mục con bị trùng lặp trong tập hợp. Điều này là hợp lý vì một
danh mục con chỉ nên thuộc về một danh mục cha duy nhất.
HashSet<>();: HashSet cung cấp hiệu suất tốt cho các thao tác thêm, xóa và kiểm tra sự tồn tại của phần tử. Tuy nhiên,
vì @OrderBy yêu cầu sắp xếp, có thể cân nhắc sử dụng SortedSet như TreeSet nếu bạn muốn duy trì thứ tự trong bộ nhớ ngay
lập tức. Tuy nhiên, với @OrderBy, Hibernate sẽ đảm nhiệm việc sắp xếp khi truy vấn.
d. Fetch Types và Cascade (Không được chỉ định trong mã hiện tại)
Fetch Type: Mặc định, @OneToMany có FetchType.LAZY, nghĩa là các danh mục con sẽ không được tải ngay lập tức khi bạn
truy xuất danh mục cha. Nếu bạn cần các danh mục con được tải cùng lúc, bạn có thể thay đổi thành FetchType.EAGER, nhưng
điều này có thể ảnh hưởng đến hiệu suất nếu bạn có nhiều danh mục con.

@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
Cascade: Không được chỉ định, nghĩa là các thao tác trên danh mục cha (như persist, merge, remove) sẽ không tự động áp
dụng cho các danh mục con. Nếu bạn muốn các thao tác này được thực hiện đồng thời trên cả cha và con, bạn có thể thêm
cascade:

@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)

3. Mối Quan Hệ Tổng Thể và Quản Lý Phân cấp
   a. Mô hình mối quan hệ
   Parent (parent): Đại diện cho một danh mục cha. Mỗi danh mục có một danh mục cha duy nhất.
   Children (children): Đại diện cho tập hợp các danh mục con. Một danh mục cha có thể có nhiều danh mục con.
   b. Quan hệ Chuẩn (Đề xuất)
   Theo mô hình phân cấp tiêu chuẩn, mỗi danh mục con chỉ thuộc về một danh mục cha, và mỗi danh mục cha có thể có nhiều
   danh mục con. Do đó, mối quan hệ phù hợp là:

Many-to-One (@ManyToOne) cho trường parent.
One-to-Many (@OneToMany) cho trường children.
Ví dụ:

@ManyToOne
@JoinColumn(name = "parent_id")
private Category parent;

@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
@OrderBy("name asc")
private Set<Category> children = new HashSet<>();
CascadeType.ALL: Cho phép tất cả các thao tác (persist, merge, remove, etc.) được áp dụng đồng thời từ cha đến con.
orphanRemoval = true: Nếu một danh mục con bị loại bỏ khỏi tập hợp children, nó cũng sẽ bị xóa khỏi cơ sở dữ liệu nếu
không còn tham chiếu nào khác.
c. Tự Giản hóa và Hiệu Suất
Truy vấn hiệu quả
Mô hình này hỗ trợ các truy vấn hiệu quả để:

Lấy danh mục cha từ một danh mục con.
Lấy tất cả danh mục con của một danh mục cha, sắp xếp theo tên.
Tránh Vòng Lặp Vô Hạn
Khi thiết lập mối quan hệ đôi bên (bi-directional), cần cẩn thận để tránh vòng lặp vô hạn khi chuyển đổi đối tượng thành
JSON hoặc khi sử dụng trong các thư viện serialization khác. Thông thường, sử dụng các annotation như
@JsonManagedReference và @JsonBackReference (trong Jackson) hoặc @JsonIdentityInfo để quản lý điều này.

Ví dụ:

@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
@OrderBy("name asc")
@JsonManagedReference
private Set<Category> children = new HashSet<>();

@ManyToOne
@JoinColumn(name = "parent_id")
@JsonBackReference
private Category parent;

4. Tóm Tắt và Khuyến Nghị
   Tóm tắt
   parent:
   Hiện tại sử dụng @OneToOne, nhưng nên thay đổi thành @ManyToOne để phản ánh đúng mối quan hệ nhiều-lên-một giữa
   con-cha.
   @JoinColumn(name = "parent_id") xác định cột khóa ngoại trong bảng categories.
   children:
   Sử dụng @OneToMany với mappedBy = "parent" để thiết lập mối quan hệ một-nhiều.
   @OrderBy("name asc") đảm bảo danh mục con được sắp xếp theo tên.
   Sử dụng Set để tránh trùng lặp danh mục con.
   Khuyến nghị
   Thay đổi @OneToOne thành @ManyToOne cho trường parent:

@ManyToOne
@JoinColumn(name = "parent_id")
private Category parent;
Cài đặt các thuộc tính Cascade và Fetch cho children nếu cần:

@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
@OrderBy("name asc")
private Set<Category> children = new HashSet<>();
Quản lý serialization để tránh vòng lặp vô hạn bằng cách sử dụng các annotation thích hợp (như @JsonManagedReference và
@JsonBackReference trong Jackson).
Thêm phương thức tiện ích để duy trì mối quan hệ đôi bên: Khi làm việc với mối quan hệ đôi bên, bạn nên bổ sung các
phương thức để thêm và loại bỏ danh mục con, đảm bảo rằng cả hai bên của mối quan hệ được cập nhật đồng nhất.

public void addChild(Category child) {
children.add(child);
child.setParent(this);
}

public void removeChild(Category child) {
children.remove(child);
child.setParent(null);
}

5. Ví dụ Toàn diện với @ManyToOne
   Dưới đây là một phiên bản cải tiến của phần mối quan hệ giữa parent và children bằng cách sử dụng @ManyToOne:

@Entity
@Table(name = "categories")
public class Category {
// ... các trường khác ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name asc")
    @JsonManagedReference
    private Set<Category> children = new HashSet<>();

    // Các phương thức tiện ích
    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }

    // Getters và Setters
    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    // ... các phương thức và trường khác ...

}
Giải thích các Thay đổi
@ManyToOne: Phù hợp hơn với mối quan hệ con-cha, cho phép mỗi danh mục có nhiều con, nhưng chỉ có một cha.
FetchType.LAZY: Giúp tối ưu hóa hiệu suất bằng cách chỉ tải danh mục cha khi cần thiết.
CascadeType.ALL & orphanRemoval = true: Đảm bảo rằng các thay đổi lên danh mục cha sẽ được áp dụng cho các danh mục con,
và các danh mục con bị loại bỏ khỏi tập hợp sẽ bị xóa khỏi cơ sở dữ liệu nếu không còn tham chiếu nào khác.
@JsonBackReference & @JsonManagedReference: Quản lý serialization để tránh vòng lặp vô hạn khi chuyển đổi đối tượng
thành JSON.
Phương thức addChild và removeChild: Đảm bảo rằng mối quan hệ đôi bên được duy trì một cách nhất quán, giúp tránh lỗi
khi thêm hoặc loại bỏ danh mục con.

6. Kết Luận
   Việc thiết lập mối quan hệ đúng đắn giữa parent và children trong mô hình phân cấp danh mục là rất quan trọng để đảm
   bảo tính chính xác và hiệu quả của hệ thống. Bằng cách sử dụng @ManyToOne kết hợp với @OneToMany, bạn có thể mô hình
   hóa một cách chính xác mối quan hệ nhiều-lên-một, phản ánh đúng thực tế mà mỗi danh mục có thể có nhiều danh mục con
   nhưng chỉ thuộc về một danh mục cha. Đồng thời, sử dụng các annotation bổ sung như @OrderBy, cascade, và fetch giúp
   tối ưu hóa hiệu suất và tính nhất quán dữ liệu.

Đoạn mã của bạn mô tả quan hệ phân cấp giữa các đối tượng thuộc lớp Category thông qua hai thuộc tính: parent và
children. Đây là cách thể hiện mối quan hệ "cha-con" (parent-child) trong một cấu trúc phân cấp, thường được sử dụng
trong các hệ thống như danh mục sản phẩm, phân loại bài viết, hay các hệ thống tổ chức dữ liệu phân cấp khác.

Phân tích chi tiết:
Thuộc tính parent:

@OneToOne
@JoinColumn(name = "parent_id")
private Category parent;
@OneToOne:
Mô tả quan hệ: Annotation này chỉ định rằng quan hệ giữa một danh mục và danh mục cha của nó là quan hệ "một-một" (
one-to-one). Điều này có nghĩa là:
Mỗi danh mục con chỉ có một danh mục cha.
Mỗi danh mục cha có thể không có hoặc có nhiều danh mục con, nhưng trong ngữ cảnh của trường parent, mỗi danh mục chỉ có
một cha mà thôi.
Lý do sử dụng @OneToOne: Thông thường, một danh mục con chỉ liên kết với một danh mục cha trong cấu trúc phân cấp. Do
đó, @OneToOne thích hợp hơn so với @ManyToOne nếu bạn muốn thiết kế mối quan hệ "một danh mục có duy nhất một cha".
@JoinColumn(name = "parent_id"):
@JoinColumn: Annotation này chỉ định rằng cột parent_id trong bảng categories sẽ là khoá ngoại (foreign key) để kết nối
với bảng chính (categories) qua khóa chính id.
Cột parent_id sẽ lưu trữ ID của danh mục cha (parent category).
Khi truy vấn một đối tượng Category, giá trị của trường parent_id sẽ được sử dụng để tham chiếu đến danh mục cha trong
bảng categories.
Mục đích của thuộc tính parent:
Thuộc tính parent về mặt logic biểu diễn "danh mục cha" (parent category) của một danh mục. Trong trường hợp hệ thống
quản lý danh mục phân cấp, mỗi danh mục có thể thuộc về một danh mục cha khác (tức là có thể là một "danh mục con").
Nếu một danh mục không có danh mục cha, điều này có thể biểu thị rằng danh mục đó là một danh mục gốc (root category).
Thuộc tính children:

@OneToMany(mappedBy = "parent")
@OrderBy(value = "name asc")
private Set<Category> children = new HashSet<>();
@OneToMany:
Mô tả quan hệ: Annotation này chỉ định rằng quan hệ giữa một danh mục và các danh mục con của nó là quan hệ "
một-nhiều" (one-to-many). Tức là:
Một danh mục cha có thể có nhiều danh mục con.
mappedBy = "parent":
Điều này chỉ ra rằng mối quan hệ này được quản lý bởi thuộc tính parent trong lớp Category. Nghĩa là, cột parent_id
trong bảng categories là cột khóa ngoại, và nó tham chiếu đến id của danh mục cha.
mappedBy giúp xác định rằng Category không cần một cột bổ sung trong cơ sở dữ liệu để quản lý mối quan hệ này. Thay vào
đó, quan hệ sẽ được quản lý thông qua thuộc tính parent của các danh mục con.
@OrderBy("name asc"):
Sắp xếp các danh mục con: Annotation này chỉ định rằng khi truy xuất các danh mục con từ cơ sở dữ liệu, chúng sẽ được
sắp xếp theo thứ tự tăng dần (asc) theo tên (name).
Điều này rất hữu ích khi bạn muốn hiển thị danh mục con theo một thứ tự cụ thể (ví dụ: theo thứ tự bảng chữ cái). Nếu
không có annotation này, thứ tự mặc định có thể không được đảm bảo.
private Set<Category> children = new HashSet<>();:
Set<Category>: Tập hợp các danh mục con được lưu trữ trong một Set (tập hợp). Một Set có đặc điểm là các phần tử trong
nó phải là duy nhất. Điều này đảm bảo rằng một danh mục cha không có hai danh mục con giống hệt nhau (theo tiêu chí bằng
nhau của đối tượng).
Bạn cũng có thể sử dụng List nếu cần bảo quản thứ tự theo thời điểm thêm vào hoặc nếu muốn cho phép trùng lặp, nhưng ở
đây Set giúp đảm bảo mỗi danh mục con là duy nhất.
new HashSet<>(): HashSet là một lớp triển khai của Set và sử dụng bảng băm (hash table) để lưu trữ các phần tử. HashSet
thường được sử dụng khi bạn cần một tập hợp không có thứ tự cụ thể và các phần tử là duy nhất.
Mục đích của thuộc tính children:
Thuộc tính children biểu diễn một tập hợp các danh mục con của danh mục hiện tại. Trong cấu trúc phân cấp, một danh mục
có thể có nhiều danh mục con. Ví dụ:
Danh mục cha: Điện tử (Electronics)
Danh mục con: Điện thoại (Phones), Máy tính (Computers), Máy ảnh (Cameras)
Khi bạn truy vấn một danh mục cụ thể, bạn có thể truy xuất danh sách các danh mục con bằng cách sử dụng thuộc tính
children. Các danh mục con sẽ được sắp xếp theo thứ tự bảng chữ cái nếu sử dụng @OrderBy("name asc").
Tóm tắt về mối quan hệ:
Quan hệ "Một-một" (One-to-One) với parent:
Một danh mục có thể có một danh mục cha duy nhất (hoặc không có cha nếu nó là danh mục gốc).
Cột parent_id trong bảng categories lưu trữ ID của danh mục cha.
Quan hệ "Một-nhiều" (One-to-Many) với children:
Một danh mục có thể có nhiều danh mục con.
Các danh mục con được ánh xạ ngược với danh mục cha thông qua cột parent_id.
Các danh mục con sẽ được sắp xếp theo thứ tự tăng dần của name khi truy vấn.
Ví dụ:
Giả sử bảng categories có các bản ghi sau:

id name parent_id
1 Electronics NULL
2 Phones 1
3 Computers 1
4 Cameras 1
5 Laptops 3
6 Desktops 3
Danh mục Electronics là danh mục gốc (vì parent_id là NULL).
Danh mục Phones, Computers, Cameras là các danh mục con của Electronics (vì parent_id = 1).
Danh mục Laptops, Desktops là các danh mục con của Computers (vì parent_id = 3).
Khi truy vấn danh mục Electronics, bạn có thể truy xuất danh sách các danh mục con (Phones, Computers, Cameras).

tôi chuẩn bị làm 1 project website "tech shop" sử dụng spring boot
tôi có 1 số câu hỏi về vấn đề này

Trong dự án Tech Shop của bạn, một Entity Category có thể được sử dụng để mô tả các danh mục sản phẩm (ví dụ: Laptop,
Điện thoại, Phụ kiện công nghệ,...). Dưới đây là các thuộc tính cơ bản và có thể cần thiết cho một entity Category trong
hệ thống:

1. ID
   Loại: Long (hoặc UUID)
   Chức năng: Đây là khóa chính (primary key) của bảng Category, giúp xác định duy nhất từng danh mục.
   Ví dụ: 1, 2, 3
2. Name
   Loại: String
   Chức năng: Tên của danh mục, ví dụ: Laptop, Điện thoại, Phụ kiện, v.v.
   Ví dụ: "Laptop", "Điện thoại", "Phụ kiện"
3. Description (Mô tả)
   Loại: String
   Chức năng: Mô tả chi tiết về danh mục, có thể giúp người dùng hiểu thêm về các sản phẩm trong danh mục này.
   Ví dụ: "Danh mục các loại laptop, máy tính xách tay từ nhiều thương hiệu nổi tiếng".
4. Image (Hình ảnh)
   Loại: String (đường dẫn đến ảnh)
   Chức năng: Đường dẫn hoặc URI đến hình ảnh đại diện của danh mục (nếu cần).
   Ví dụ: /images/categories/laptop.jpg.
5. Slug
   Loại: String
   Chức năng: Chuỗi URL thân thiện với SEO, dùng để hiển thị trong URL thay vì ID (thường là phiên bản viết thường và
   không dấu của Name).
   Ví dụ: "laptop", "dien-thoai", "phu-kien"
6. Parent Category (Danh mục cha)
   Loại: Category (self-referencing entity)
   Chức năng: Nếu danh mục là một danh mục con, thuộc tính này sẽ chứa danh mục cha. Điều này giúp tạo ra cấu trúc phân
   cấp danh mục (category hierarchy).
   Ví dụ: Phụ kiện có thể là danh mục con của Điện thoại hoặc Laptop.
7. Active (Kích hoạt)
   Loại: Boolean
   Chức năng: Xác định xem danh mục có khả dụng trên website không (ví dụ: có thể tạm ẩn danh mục mà không cần xóa).
   Ví dụ: true, false.
8. CreatedAt (Ngày tạo)
   Loại: LocalDateTime
   Chức năng: Thời điểm danh mục được tạo.
   Ví dụ: 2024-09-20 14:30:00.
9. UpdatedAt (Ngày cập nhật)
   Loại: LocalDateTime
   Chức năng: Thời điểm danh mục được cập nhật lần cuối.
   Ví dụ: 2024-09-22 10:15:00.
10. Products (Danh sách sản phẩm)
    Loại: List<Product>
    Quan hệ: OneToMany với Product (1 danh mục có nhiều sản phẩm).
    Chức năng: Danh sách các sản phẩm thuộc về danh mục này.
    Ví dụ: Danh mục "Laptop" có thể chứa các sản phẩm như "Dell XPS 13", "MacBook Pro".
    Entity Category ví dụ:

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String image;

    @Column(unique = true)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    // Getters và Setters

}
Một số lưu ý:
Slug: Bạn có thể tự động tạo slug từ name khi người dùng nhập tên danh mục.
Parent Category: Nếu bạn muốn hỗ trợ danh mục con, thì cần có thuộc tính `parentCategory