1. Sao Chép Nông vs. Sao Chép Sâu
   1.1. Sao Chép Nông (Shallow Copy)
   Định Nghĩa: Tạo một đối tượng mới và sao chép giá trị các thuộc tính của đối tượng gốc sang đối tượng mới. Nếu thuộc
   tính là các kiểu cơ bản (primitives), giá trị được sao chép, còn nếu thuộc tính là đối tượng tham chiếu, chỉ tham
   chiếu đến đối tượng gốc được sao chép.
   Hậu Quả: Các thay đổi trên các đối tượng liên kết (nested objects) trong đối tượng sao chép sẽ ảnh hưởng đến đối
   tượng gốc và ngược lại.
   Ví Dụ:

Category original = new Category();
original.setName("Original");

Category shallowCopy = Category.CopyFull(original);
shallowCopy.setName("Shallow Copy");

// original.getName() vẫn là "Original"
// nhưng nếu shallowCopy.setParent(...) thì original sẽ cũng bị ảnh hưởng nếu parent là tham chiếu chung
1.2. Sao Chép Sâu (Deep Copy)
Định Nghĩa: Tạo một đối tượng mới và độc lập hoàn toàn, bao gồm cả việc sao chép các đối tượng liên kết, để đảm bảo rằng
thay đổi trên bản sao không ảnh hưởng đến đối tượng gốc.
Hậu Quả: Tăng tính bảo mật và độc lập của các đối tượng, nhưng có thể phức tạp hơn để triển khai, nhất là với các cấu
trúc dữ liệu phức tạp và có mối quan hệ vòng lặp.
Ví Dụ:

Category original = new Category();
original.setName("Original");
Category deepCopy = Category.DeepCopyFull(original);
deepCopy.setName("Deep Copy");

// original.getName() vẫn là "Original", và thay đổi trong deepCopy không ảnh hưởng đến original

2. ModelMapper
   ModelMapper là một thư viện Java được thiết kế để đơn giản hóa việc sao chép dữ liệu giữa các đối tượng, hỗ trợ cả
   sao chép nông và sao chép sâu tùy thuộc vào cấu hình và cách sử dụng.

2.1. Sao Chép Nông với ModelMapper
Mặc định, ModelMapper thực hiện sao chép nông khi ánh xạ các đối tượng. Điều này có nghĩa là các thuộc tính tham chiếu
sẽ được sao chép theo kiểu gọi "same reference".

Ví Dụ:

import org.modelmapper.ModelMapper;

public class CategoryMapper {
private static final ModelMapper modelMapper = new ModelMapper();

    public static Category copyShallow(Category category) {
        return modelMapper.map(category, Category.class);
    }

}
Khi gọi copyShallow, các thuộc tính tham chiếu như parent và subCategories sẽ trỏ đến cùng một đối tượng với đối tượng
gốc, do đó thay đổi trong bản sao sẽ ảnh hưởng đến đối tượng gốc.
2.2. Sao Chép Sâu với ModelMapper
Để thực hiện sao chép sâu với ModelMapper, bạn cần cấu hình nó để ánh xạ các thuộc tính nested (nội bộ) một cách đệ quy,
tạo ra các bản sao mới cho các đối tượng liên kết.

Cách Thiết Lập:

Cấu hình ModelMapper để đọc và ghi các thuộc tính nested.
Sử dụng Mapping Strategies thích hợp.
Ví Dụ:

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class CategoryMapper {
private static final ModelMapper modelMapper = new ModelMapper();

    static {
        // Thiết lập ModelMapper để sử dụng Strategy phù hợp
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static Category copyDeep(Category category) {
        // Sử dụng cùng một ModelMapper để ánh xạ đệ quy
        return modelMapper.map(category, Category.class);
    }

}
Lưu Ý:

Với cấu hình STRICT, ModelMapper sẽ đảm bảo ánh xạ chính xác từng thuộc tính, giúp giảm thiểu lỗi khi ánh xạ các đối
tượng nested.
Định nghĩa các bản đồ (mappings) cho các mối quan hệ phức tạp nếu cần thiết để đảm bảo sao chép sâu.
Ví Dụ Minh Họa Sao Chép Sâu với ModelMapper
Giả sử bạn có cấu trúc như sau:

Category parent = new Category();
parent.setId(1);
parent.setName("Parent");

Category child = new Category();
child.setId(2);
child.setName("Child");
child.setParent(parent);

parent.getSubCategories().add(child);
Sao Chép Nông:

Category shallowCopy = CategoryMapper.copyShallow(parent);
shallowCopy.setName("Shallow Copy");

shallowCopy.getSubCategories().iterator().next().setName("Modified Child");

// Kết quả:
// parent.getName() = "Parent"
// parent.getSubCategories().iterator().next().getName() = "Modified Child"
// shallowCopy.getName() = "Shallow Copy"
// shallowCopy.getSubCategories().iterator().next().getName() = "Modified Child"
// Vì `child` được sao chép nông, `child` trong cả `parent` và `shallowCopy` trỏ đến cùng đối tượng.
Sao Chép Sâu:

Category deepCopy = CategoryMapper.copyDeep(parent);
deepCopy.setName("Deep Copy");

deepCopy.getSubCategories().iterator().next().setName("Modified Child");

// Kết quả:
// parent.getName() = "Parent"
// parent.getSubCategories().iterator().next().getName() = "Child"
// deepCopy.getName() = "Deep Copy"
// deepCopy.getSubCategories().iterator().next().getName() = "Modified Child"
// Vì `child` được sao chép sâu, các thay đổi trên bản sao không ảnh hưởng đến đối tượng gốc.

3. Builder Pattern
   Builder Pattern là một mẫu thiết kế (design pattern) giúp tạo ra các đối tượng phức tạp một cách có kiểm soát bằng
   cách sử dụng một bộ xây dựng (builder) để thiết lập từng thuộc tính. Builder Pattern chủ yếu được sử dụng để tạo ra
   các đối tượng mới, không phải để sao chép đối tượng, nhưng nó vẫn có thể được sử dụng để thực hiện việc sao chép sâu
   bằng cách thiết lập tất cả các thuộc tính một cách có kiểm soát.

3.1. Sao Chép Nông với Builder Pattern
Nếu bạn chỉ sử dụng Builder Pattern để thiết lập các thuộc tính của đối tượng mới bằng cách sao chép các tham chiếu từ
đối tượng gốc, bạn chỉ thực hiện sao chép nông.

Ví Dụ:

public class Category {
// Các thuộc tính và các phương thức khác...

    public static class Builder {
        private Category category;

        public Builder() {
            category = new Category();
        }

        public Builder withId(Integer id) {
            category.setId(id);
            return this;
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
            category.setParent(parent);
            return this;
        }

        public Builder withSubCategories(Set<Category> subCategories) {
            category.setSubCategories(subCategories);
            return this;
        }

        public Builder withHasChildren(boolean hasChildren) {
            category.setHasChildren(hasChildren);
            return this;
        }

        public Category build() {
            return category;
        }
    }

    // Phương thức sao chép nông sử dụng Builder
    public static Category copyShallow(Category original) {
        return new Builder()
                .withId(original.getId())
                .withName(original.getName())
                .withAlias(original.getAlias())
                .withImage(original.getImage())
                .withEnabled(original.isEnabled())
                .withParent(original.getParent())
                .withSubCategories(new LinkedHashSet<>(original.getSubCategories()))
                .withHasChildren(original.isHasChildren())
                .build();
    }

}
3.2. Sao Chép Sâu với Builder Pattern
Để thực hiện sao chép sâu, bạn cần sử dụng Builder Pattern kết hợp với việc tạo các bản sao mới cho các đối tượng liên
quan (nested objects). Điều này đòi hỏi bạn xây dựng bản sao của từng đối tượng liên quan một cách đệ quy hoặc có kiểm
soát.

Ví Dụ:

public class Category {
// Các thuộc tính và các phương thức khác...

    public static class Builder {
        private Category category;

        public Builder() {
            category = new Category();
        }

        // Các phương thức thiết lập khác...

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
                    subCategoriesCopy.add(Category.copyDeep(subCat));
                }
                category.setSubCategories(subCategoriesCopy);
            } else {
                category.setSubCategories(new LinkedHashSet<>());
            }
            return this;
        }

        public Category build() {
            return category;
        }
    }

    // Phương thức sao chép sâu sử dụng Builder
    public static Category copyDeep(Category original) {
        if (original == null) {
            return null;
        }

        Builder builder = new Builder()
                .withName(original.getName())
                .withAlias(original.getAlias())
                .withImage(original.getImage())
                .withEnabled(original.isEnabled())
                .withHasChildren(original.isHasChildren());

        // Sao chép cha
        builder.withParent(original.getParent());

        // Sao chép các danh mục con
        builder.withSubCategories(original.getSubCategories());

        return builder.build();
    }

}
Giải Thích:

Sao Chép Cha (parent): Phương thức withParent trong Builder sẽ gọi copyDeep để tạo bản sao sâu của parent.
Sao Chép Các Danh Mục Con (subCategories): Phương thức withSubCategories sẽ tạo bản sao sâu cho từng danh mục con và
thêm chúng vào tập hợp subCategories của bản sao mới.
Điều Điều Kiện: Đảm bảo rằng khi sao chép, bạn tránh tạo ra các mối quan hệ vòng lặp vô tận bằng cách kiểm soát quá
trình sao chép.
3.3. Ví Dụ Minh Họa Sao Chép Sâu với Builder Pattern
Cấu Trúc Ban Đầu:

Category parent = new Category();
parent.setName("Parent");

Category child1 = new Category();
child1.setName("Child 1");
child1.setParent(parent);

Category child2 = new Category();
child2.setName("Child 2");
child2.setParent(parent);

parent.getSubCategories().add(child1);
parent.getSubCategories().add(child2);
Sao Chép Sâu:

Category deepCopy = Category.copyDeep(parent);
deepCopy.setName("Deep Copied Parent");

// deepCopy sẽ có tên "Deep Copied Parent", không ảnh hưởng đến parent gốc
// deepCopy.getParent() cũng được sao chép sâu (nếu parent của parent tồn tại)
// deepCopy.getSubCategories() chứa các bản sao mới của child1 và child2
Kết Quả:

parent: "Parent"
deepCopy: "Deep Copied Parent", với subCategories là các bản sao mới của "Child 1" và "Child 2"

4. So Sánh ModelMapper và Builder Pattern về Sao Chép
   4.1. ModelMapper:
   Khả Năng Sao Chép:
   Sao Chép Nông: Mặc định thực hiện sao chép nông, chỉ sao chép giá trị các thuộc tính cơ bản và tham chiếu các đối
   tượng liên kết.
   Sao Chép Sâu: Có thể cấu hình để thực hiện sao chép sâu bằng cách tạo các bản sao mới cho các đối tượng liên kết, tuy
   nhiên cần cẩn thận để tránh các vấn đề như đạn kính vòng lặp.
   Ưu Điểm:
   Tự Động Hóa: Giúp tự động hóa việc ánh xạ các thuộc tính giữa các đối tượng.
   Dễ Sử Dụng: Cấu hình đơn giản cho các trường hợp cơ bản.
   Nhược Điểm:
   Kiểm Soát Phức Tạp: Khi cần sao chép sâu hoặc xử lý các mối quan hệ phức tạp, cần phải cấu hình chi tiết hơn.
   Hiệu Suất: Có thể gây ra chậm hơn nếu không được cấu hình đúng cách, nhất là với các cấu trúc dữ liệu lớn và phức
   tạp.
   4.2. Builder Pattern:
   Khả Năng Sao Chép:
   Sao Chép Nông: Có thể dễ dàng thực hiện sao chép nông bằng cách thiết lập các thuộc tính từ đối tượng gốc.
   Sao Chép Sâu: Cần phải triển khai logic sao chép sâu trong Builder, thường đòi hỏi sự kiểm soát thủ công và quản lý
   đệ quy hoặc các trạng thái trung gian.
   Ưu Điểm:
   Tính Linh Hoạt Cao: Cho phép kiểm soát chi tiết quá trình tạo và sao chép đối tượng.
   Bảo Trì Dễ Dàng: Mã rõ ràng, dễ hiểu và dễ bảo trì hơn trong các trường hợp phức tạp.
   Nhược Điểm:
   Viết Mã Thủ Công: Cần phải viết các phương thức sao chép sâu một cách thủ công, điều này có thể dẫn đến mã lặp lại và
   lỗi nếu không cẩn thận.
   Phức Tạp Trong Các Cấu Trúc Phức Tạp: Đối với các đối tượng có mối quan hệ phức tạp hoặc vòng lặp, việc triển khai
   sao chép sâu có thể rất phức tạp.
5. Khuyến Nghị Tổng Quan
   5.1. Khi Nào Sử Dụng ModelMapper:
   Sao Chép Nông Đơn Giản: Khi bạn chỉ cần sao chép các thuộc tính cơ bản mà không cần phải tạo bản sao đệ quy cho các
   đối tượng liên kết.
   Ánh Xạ Đơn Giản: Khi bạn muốn ánh xạ giữa các lớp khác nhau (DTOs và Entities) một cách tự động.
   Tiết Kiệm Thời Gian: Giúp giảm bớt lượng mã lặp lại trong việc sao chép dữ liệu giữa các đối tượng.
   5.2. Khi Nào Sử Dụng Builder Pattern:
   Sao Chép Sâu: Khi bạn cần kiểm soát chi tiết quá trình sao chép, đặc biệt là để tạo các bản sao độc lập hoàn toàn.
   Cấu Trúc Phức Tạp: Khi đối tượng của bạn có các mối quan hệ phức tạp hoặc yêu cầu đặc biệt trong quá trình sao chép.
   Tính Linh Hoạt và Kiểm Soát: Khi bạn muốn có sự linh hoạt cao trong việc thiết lập các thuộc tính của đối tượng mới.
6. Ví Dụ Cụ Thể về Sao Chép Sâu
   6.1. Sử Dụng ModelMapper cho Sao Chép Sâu
   Để thực hiện sao chép sâu với ModelMapper, bạn có thể cần một cấu hình tùy chỉnh hoặc sử dụng các ánh xạ (mappings)
   để định nghĩa cách sao chép các đối tượng nested.

Ví Dụ:

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class CategoryMapper {
private static final ModelMapper modelMapper = new ModelMapper();

    static {
        // Thiết lập ModelMapper để sử dụng Matching Strategy phù hợp
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static Category copyDeep(Category category) {
        return modelMapper.map(category, Category.class);
    }

}
Ưu Điểm:

Tự Động: ModelMapper tự động ánh xạ các thuộc tính và đối tượng liên kết.
Nhược Điểm:

Quản Lý Vòng Lặp: Nếu có mối quan hệ vòng lặp (ví dụ: cha → con → cha), bạn cần cấu hình thêm để tránh đạn kính vòng
lặp.
Kiểm Soát Chi Tiết: Khó để kiểm soát chính xác quá trình sao chép sâu, nhất là với các cấu trúc phức tạp.
6.2. Sử Dụng Builder Pattern cho Sao Chép Sâu
Ví Dụ:

public class Category {
// Các thuộc tính và phương thức khác...

    public static class Builder {
        private Category category;

        public Builder() {
            category = new Category();
        }

        public Builder withName(String name) {
            category.setName(name);
            return this;
        }

        public Builder withAlias(String alias) {
            category.setAlias(alias);
            return this;
        }

        // Các phương thức thiết lập khác...

        public Builder withParent(Category parent) {
            if (parent != null) {
                Category parentCopy = Category.copyDeep(parent); // Gọi lại bản sao sâu
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
                    Category copiedChild = Category.copyDeep(subCat); // Gọi lại bản sao sâu
                    copiedChild.setParent(this.category); // Thiết lập cha cho bản sao con
                    subCategoriesCopy.add(copiedChild);
                }
                category.setSubCategories(subCategoriesCopy);
            } else {
                category.setSubCategories(new LinkedHashSet<>());
            }
            return this;
        }

        public Category build() {
            return category;
        }
    }

    // Phương thức sao chép sâu sử dụng Builder
    public static Category copyDeep(Category original) {
        if (original == null) return null;

        Builder builder = new Builder()
                .withName(original.getName())
                .withAlias(original.getAlias())
                .withImage(original.getImage())
                .withEnabled(original.isEnabled())
                .withHasChildren(original.isHasChildren());

        // Sao chép cha
        builder.withParent(original.getParent());

        // Sao chép các danh mục con
        builder.withSubCategories(original.getSubCategories());

        return builder.build();
    }

}
Ưu Điểm:

Kiểm Soát Chi Tiết: Cho phép bạn quản lý cách sao chép từng thuộc tính và mối quan hệ một cách rõ ràng.
Tránh Đạn Kính Vòng Lặp: Bằng cách kiểm soát quá trình sao chép và thiết lập lại mối quan hệ cha-con, bạn có thể tránh
được các vòng lặp không mong muốn.
Nhược Điểm:

Phức Tạp Hơn: Cần viết thêm và kiểm soát quá trình sao chép, đặc biệt là với các cấu trúc dữ liệu phức tạp.
Mã Thủ Công: Có thể dẫn đến việc viết mã lặp lại và cần phải duy trì khi cấu trúc đối tượng thay đổi.

7. Tổng Kết
   7.1. ModelMapper:
   Sao Chép Nông: Được hỗ trợ mặc định, dễ dàng thực hiện và tiết kiệm thời gian trong các trường hợp đơn giản.
   Sao Chép Sâu: Cần cấu hình đặc biệt và quản lý một cách cẩn thận để tránh các vấn đề như đạn kính vòng lặp, nhưng có
   thể đạt được sao chép sâu với mức độ giới hạn nhất định.
   7.2. Builder Pattern:
   Sao Chép Nông: Có thể thực hiện bằng cách sử dụng Builder để thiết lập các thuộc tính dựa trên đối tượng gốc.
   Sao Chép Sâu: Có thể thực hiện bằng cách thêm logic sao chép sâu vào các phương thức Builder, cung cấp kiểm soát chi
   tiết và tránh sự phụ thuộc giữa bản sao và đối tượng gốc.
   7.3. Khuyến Nghị:
   Đối Với Các Cấu Trúc Đơn Giản: Sử dụng ModelMapper để sao chép nông một cách nhanh chóng và hiệu quả.
   Đối Với Các Cấu Trúc Phức Tạp hoặc Khi Cần Sao Chép Sâu: Sử dụng Builder Pattern để kiểm soát chi tiết quá trình sao
   chép, đảm bảo tính nhất quán và tránh các vấn đề về tham chiếu.
   Sử Dụng Thư viện Hỗ Trợ Khi Cần: Nếu quyết định sử dụng ModelMapper cho sao chép sâu, hãy đảm bảo rằng bạn cấu hình
   đúng cách và kiểm tra kỹ lưỡng để tránh các lỗi không mong muốn.
   Xem Xét Các Yêu Cầu Cụ Thể của Dự Án: Quyết định sử dụng mô hình sao chép nào phụ thuộc vào yêu cầu cụ thể của dự án,
   cấu trúc dữ liệu và mức độ phức tạp của các đối tượng.