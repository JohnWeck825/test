1. Tạo entity cho bảng trung gian BrandCategory:

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; 

}

2. Thay đổi entity Brand:

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    private String logo;

    private boolean active;
    
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BrandCategory> brandCategories = new HashSet<>();

    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Series> series = new LinkedHashSet<>();

}

3. Thay đổi entity Category:

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 128, nullable = false, unique = true)
    private String alias;

    private boolean enabled;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BrandCategory> brandCategories = new HashSet<>();

}
Giải thích:

Entity BrandCategory:
Entity này đại diện cho bảng trung gian brand_categories.
Có hai field brand và category để liên kết với các entity Brand và Category.
@OnDelete(action = OnDeleteAction.CASCADE) được đặt trên field brand để khi một Brand bị xóa, các bản ghi tương ứng
trong brand_categories cũng sẽ bị xóa.
Entity Brand và Category:
Bỏ @ManyToMany và @JoinTable.
Thêm @OneToMany với mappedBy = "brand" hoặc mappedBy = "category" và cascade = CascadeType.ALL, orphanRemoval = true, để
quản lý các thực thể BrandCategory liên quan.
orphanRemoval = true để xóa BrandCategory khi bị xóa khỏi Set brandCategories
Lưu ý:

Đảm bảo bạn đã import các annotation cần thiết: javax.persistence.*, org.hibernate.annotations.OnDelete,
org.hibernate.annotations.OnDeleteAction.
Việc sử dụng ON DELETE CASCADE cần được cân nhắc kỹ lưỡng, vì nó có thể dẫn đến việc xóa dữ liệu không mong muốn.
Khi bạn xóa 1 Brand thì các BrandCategory liên quan đến Brand đó sẽ bị xóa, khi đó quan hệ ManyToMany giữa Brand và
Categoty cũng sẽ bị xóa.
Tóm tắt:

Để thiết lập ON DELETE CASCADE cho mối quan hệ @ManyToMany trong Spring Boot, bạn cần tạo một entity riêng cho bảng
trung gian và thiết lập @OnDelete trên entity này.