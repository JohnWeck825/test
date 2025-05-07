package com.myshop.adminpage.service.admin;

import com.myshop.adminpage.exception.BrandNotFoundException;
import com.myshop.adminpage.exception.UserNotFoundException;
import com.myshop.adminpage.model.Brand;
import com.myshop.adminpage.model.Role;
import com.myshop.adminpage.model.User;
import com.myshop.adminpage.model.UserRole;
import com.myshop.adminpage.repository.RoleRepository;
import com.myshop.adminpage.repository.UserRepository;
import com.myshop.adminpage.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleRepository userRoleRepository;


    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> getById(Integer id) {
        return userRepository.getUserById(id);
    }
//    @Override
//    public Object findByUsername(String username) {
//        return userRepository.findByUserName(username);
//    }


    //
    public static final int USER_PER_PAGE = 5;


    //    public List<User> findAll() {
//        return userRepository.findAll();
//    }
//
    // Pagination
    @Transactional
    public Page<User> listByPage(String keyword, Integer pageNum, String sortField, String sortDir) {
//        Sort sort = Sort.by(sortField);
//        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE, sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.search(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

//    public Set<Role> finAllRoles(List<User> users) {

    /// /        List<Role> roles = null;
    /// /        Set<Role> role = null;
    /// /        for (User user : users) {
    /// /            role = roleRepository.findUserRole(user.getId());
    /// ///            System.out.println(roles);
    /// /            roles.add(role.iterator().next());
    /// ///            System.out.println(roles);
    /// /        }
    /// /
    /// /        System.out.println(roles);
    /// /        return roles;
//        Set<Role> roles = Set.of();
//        for (User user : users) {
//            roles = roleRepository.findUserRole(user.getId());
//            user.setRoles(roles);
//            System.out.println(roles);
//        }
//        System.out.println(roles);
//        return roles;
//    }

//    public void setRolesForUser(List<User> users) {
//        for (User user : users) {
//            Set<Role> roles = roleRepository.findUserRole(user.getId());
//            user.setRoles(roles);
//        }
//    }

    //
//    // Create User & Update User
    public void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public User save(User user) {
        if (user.getId() != null) {
            User existUser = userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public void save(User user, List<Integer> roleIds) {
        User savedUser = save(user);

        userRepository.deleteByUserId(savedUser.getId());

//        roleIds.forEach(roleId -> {
//            Role role = roleRepository.findById(roleId).get();
//            UserRole userRole = new UserRole();
//            userRole.setUser(savedUser);
//            userRole.setRole(role);
//            userRepository.save(userRole);
//        });

        if (roleIds != null) {
            roleIds.forEach(roleId -> {
                Role role = roleRepository.findById(roleId).orElse(null);
                if (role != null) {
//                    UserRole userRole = new UserRole();
//                    userRole.setUser(savedUser);
//                    userRole.setRole(role);
//                    userRoleRepository.save(userRole);
                    userRoleRepository.save(new UserRole(savedUser, role));
                }
            });
        }
    }

    public User saveAccount(User user) {
//        User oldUser = userRepository.findById(user.getId()).get();
//        if (user.getPassword().isEmpty()) {
//            user.setPassword(oldUser.getPassword());
//        } else {
//            encodePassword(user);
//        }
//        user.setRoles(oldUser.getRoles());
//        user.setEnabled(oldUser.isEnabled());
//        return userRepository.save(user);
        return null;
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Couldn't find any user with id =" + id));
    }

    //
//    public User saveUserAccount(User user) {
//        User existUser = userRepository.findById(user.getId()).get();
//        if (user.getPassword().isEmpty()) {
//            user.setPassword(existUser.getPassword());
//        } else {
//            encodePassword(user);
//        }
//        user.setRoles(existUser.getRoles());
//        user.setEnabled(existUser.isEnabled());
//        return userRepository.save(user);
//    }
//
//    // Get User
//    public User getUserById(Integer id) throws UserNotFoundException {
//        Optional<User> user = userRepository.findById(id);
//        if (user.isPresent()) {
//            return user.get();
//        } else {
//            throw new UserNotFoundException("Couldn't find any users with ID " + id);
//        }
//    }
//
    // Delete User
    public void deleteUser(Integer id) throws UserNotFoundException {
        try {
            getUserById(id);
            userRepository.deleteById(id);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("Couldn't find any users with ID " + id);
        }
    }


    // Update User Status
    public void updateStatus(Integer id, boolean status) throws UserNotFoundException {
        getUserById(id);
        userRepository.updateUserStatus(id, status);
    }

    // Check Unique
    public String handleUserException(Integer id, String userName, String email) {
        // Tìm kiếm người dùng trong cơ sở dữ liệu dựa trên email
        User userName1 = userRepository.getByUserName(userName);
        System.out.println(userName1.getUserName());
        User userEmail = userRepository.findUserByEmail(email);

        // Nếu không tìm thấy người dùng nào có email này, email được coi là duy nhất
        if (userName1 == null && userEmail == null) {
            return "OK";
        }
        // Kiểm tra xem đây có phải là quá trình tạo mới người dùng hay không
        boolean isCreating = (id == null);
        // Nếu đang tạo người dùng mới và đã tìm thấy một người dùng với email giống nhau, email không duy nhất
        if (isCreating) { // Tạo mới users
            if (userName1 != null) {
                return "User name existed";
            } else if (userEmail != null) {
                return "Email existed";
            }
        } else { // Cập nhật users
            // Nếu không phải tạo mới, tức là đang cập nhật thông tin người dùng hiện có,
            // kiểm tra xem ID của người dùng tìm được có khác với ID của người dùng đang cập nhật hay không
            if (userName1 != null && userName1.getId() != id) {
                // Nếu khác, nghĩa là email đã tồn tại và được sử dụng bởi người dùng khác
                return "User name existed";
            } else if (userEmail != null && userEmail.getId() != id) {
                return "Email existed";
            }
        }
        return "OK";
    }
//
////    Khai báo và Mục đích:
////    Integer id: Đây là ID của người dùng. Trong trường hợp tạo người dùng mới, giá trị này có thể là null.
////    String email: Đây là địa chỉ email cần được kiểm tra tính duy nhất.
////    Tìm Kiếm Người Dùng bằng Email:
////    User userByEmail = userRepository.findByEmail(email);: Phương thức này tìm kiếm trong cơ sở dữ liệu xem có người dùng nào sở hữu địa chỉ email cung cấp hay không. Nếu không tìm thấy, userByEmail sẽ là null.
////    Kiểm Tra Nếu userByEmail là null:
////    Nếu không tìm thấy người dùng nào với địa chỉ email cung cấp (userByEmail == null), điều này có nghĩa là email là duy nhất và chưa được sử dụng, do đó phương thức trả về true.
////    Xác Định Quá Trình Tạo Mới Hay Cập Nhật:
////    boolean isCreatingNew = (id == null);: Biến này xác định liệu đang trong quá trình tạo mới (id == null, tức là không có ID được cung cấp) hay cập nhật thông tin người dùng (ID tồn tại).
////    Trường Hợp Tạo Mới:
////    Nếu đây là quá trình tạo mới người dùng và đã tồn tại một người dùng với email cung cấp (isCreatingNew là true), điều này có nghĩa là email không duy nhất và phương thức trả về false.
////    Trường Hợp Cập Nhật:
////    Nếu đây là quá trình cập nhật thông tin cho người dùng hiện có (isCreatingNew là false), chúng ta cần kiểm tra ID của người dùng được tìm thấy có khớp với ID của người dùng đang được cập nhật hay không.
////    Nếu userByEmail.getId() != id, điều này có nghĩa là email đã được sử dụng bởi một người dùng khác, không phải người dùng hiện tại đang cập nhật, do đó phương thức trả về false.
////    Trả Về true:
////    Nếu tất cả các điều kiện kiểm tra trên đều không đúng, điều này có nghĩa là trong trường hợp cập nhật, email mới không trùng với bất kỳ người dùng nào khác ngoại trừ chính người dùng đang cập nhật. Do đó, email được coi là duy nhất và phương thức trả về true.
//
//
//
}
//
