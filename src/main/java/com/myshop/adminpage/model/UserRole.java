package com.myshop.adminpage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }


//    @EmbeddedId
//    private UserRoleKey id;
//
//    @Embeddable
//    public static class UserRoleKey implements Serializable {
//        private Integer userId;
//        private Integer roleId;
//    }
}

//@EmbeddedId: Annotation này được sử dụng để đánh dấu userId là khóa chính kết hợp (composite key).
//JPA sẽ sử dụng lớp UserRoleId để tạo ra khóa chính.

//@Embeddable: Annotation này được sử dụng để đánh dấu lớp UserRoleId là một lớp nhúng (embedded class).

//UserRoleId sẽ được nhúng vào lớp UserRole và không được ánh xạ thành một bảng riêng biệt.
//UserRoleId:Lớp này chứa các thuộc tính tạo nên khóa chính composite (trong trường hợp này là userId và roleId).

//Cách thức hoạt động:
//JPA sẽ sử dụng lớp UserRoleId để tạo ra khóa chính cho bảng UserRole.
//Khóa chính của bảng UserRole sẽ được tạo bởi hai cột userId và roleId.
//JPA sẽ sử dụng các thuộc tính userId và roleId trong lớp UserRoleId để xác định giá trị khóa chính.

//3. Lưu trữ dữ liệu:
//Khi bạn lưu trữ một đối tượng UserRole vào cơ sở dữ liệu, JPA sẽ thực hiện các bước sau:
//Tạo khóa chính composite: JPA sẽ sử dụng các giá trị của userId và roleId trong userRoleId để tạo ra khóa chính composite cho bản ghi UserRole.
//Chuyển đổi khóa chính: JPA sẽ chuyển đổi đối tượng UserRoleId thành một luồng byte (serialization) bằng cách sử dụng cơ chế serialization của Java.
//Lưu luồng byte: JPA sẽ lưu luồng byte được tạo vào cột khóa chính trong bảng UserRole trong cơ sở dữ liệu.

//4. Tải dữ liệu:
//Khi bạn muốn tải một bản ghi UserRole từ cơ sở dữ liệu, JPA sẽ thực hiện các bước sau:
//Tải luồng byte: JPA lấy luồng byte từ cột khóa chính trong bảng UserRole.
//Khôi phục đối tượng: JPA khôi phục lại đối tượng UserRoleId từ luồng byte bằng cách sử dụng cơ chế deserialization của Java.
//Tạo đối tượng Entity: JPA sử dụng đối tượng UserRoleId được khôi phục để tạo đối tượng UserRole.

//Minh họa:
//Hãy tưởng tượng bạn có một bản ghi UserRole trong cơ sở dữ liệu với các giá trị sau:
//userId: 1
//roleId: 2

//JPA sẽ tạo một khóa chính composite với giá trị (userId: 1, roleId: 2).
//JPA sẽ chuyển đổi đối tượng UserRoleId (có giá trị userId: 1, roleId: 2) thành một luồng byte và lưu luồng byte này vào cột khóa chính trong bảng UserRole.
//Khi tải bản ghi UserRole từ cơ sở dữ liệu, JPA sẽ lấy luồng byte từ cột khóa chính, khôi phục lại đối tượng UserRoleId và tạo ra đối tượng UserRole tương ứng.

//Kết luận:
//Cơ chế Serializable là một phần quan trọng trong JPA khi làm việc với khóa chính composite.
//implements Serializable cho phép JPA chuyển đổi và khôi phục lại các đối tượng được sử dụng làm khóa chính composite một cách chính xác.
//Điều này đảm bảo rằng JPA có thể lưu trữ và tải dữ liệu một cách chính xác trong cơ sở dữ liệu.