package com.myshop.adminpage.controller.admin;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.myshop.adminpage.exception.BrandNotFoundException;
import com.myshop.adminpage.exception.CategoryNotFoundException;
import com.myshop.adminpage.exception.UserNotFoundException;
import com.myshop.adminpage.model.Brand;
import com.myshop.adminpage.model.Role;
import com.myshop.adminpage.model.User;
import com.myshop.adminpage.model.UserRole;
import com.myshop.adminpage.repository.RoleRepository;
import com.myshop.adminpage.repository.UserRepository;
import com.myshop.adminpage.repository.UserRoleRepository;
import com.myshop.adminpage.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.firebase.cloud.StorageClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin-user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;


    @GetMapping("")
    public String index(Model model, @Param("keyword") String keyword, @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(name = "orderBy", defaultValue = "id") String sortField, @RequestParam(name = "direction", defaultValue = "asc") String sortDir) {
        Page<User> users = userService.listByPage(keyword, pageNum, sortField, sortDir);
//        List<Role> roles = (List<Role>) roleRepository.findAll();
//        List<User> userList = page.getContent();

        List<User> userList = users.getContent();

//        Set<UserRole> userRoles = Set.of();
//        Set<Role> roles = Set.of();
//        Set<Role> roles = userService.finAllRoles(userList);
//        System.out.println(roles.toString());

//        userService.setRolesForUser(userList);

        for(User user : userList) {
            Set<UserRole> roles = userRepository.findUserRoles(user.getUserName());
            user.setUserRoles(roles);
        }


        String defaultUrl = "/admin-user";

//        for (User user : users) {
//            roles = roleRepository.findUserRole(user.getId());
////            System.out.println(roles);
////            userRoles = userRoleRepository.findAllByUserId(user.getId());
//        }
        model.addAttribute("users", users);
//        model.addAttribute("roles", roles);
//        model.addAttribute("userRoles", userRoles);
        model.addAttribute("keyword", keyword);
        model.addAttribute("defaultUrl", defaultUrl);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("totalItems", users.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "/admin/users/index";

    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        List<Role> roles = (List<Role>) roleRepository.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("user", new User());
        return "/admin/users/add";
    }

    @GetMapping("/editUser{id}")
    public String editUser(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
//            Optional<User> user = userService.findById(id);
            Optional<User> user = userService.getById(id);

            List<Role> roles = (List<Role>) roleRepository.findAll();

            Set<Role> userRoles = roleRepository.findUserRoles(id);

            if (user.isPresent()) {
                model.addAttribute("roles", roles);
                model.addAttribute("userRoles", userRoles);
                model.addAttribute("user", user.get());
                return "/admin/users/edit";
            }
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_error", e.getMessage());
            return "redirect:/admin-user";
        }
        return "redirect:/admin-user";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(name = "userRolesId", required = false) List<Integer> roleIds,
                           @RequestParam("imageFile") MultipartFile multipartFile,
                           RedirectAttributes redirectAttributes) throws Exception {
        if (!multipartFile.isEmpty()) {
            String oldFileName = user.getPhotoUrl();
            oldFileName = URLDecoder.decode(oldFileName, StandardCharsets.UTF_8);
            int lastIndex = oldFileName.lastIndexOf("/");
            if (lastIndex != -1) {
                oldFileName = oldFileName.substring(lastIndex + 1);
            }
            if (oldFileName.contains("?")) {
                oldFileName = oldFileName.substring(0, oldFileName.indexOf('?'));
            }
            String fileName = multipartFile.getOriginalFilename();

            userService.save(user, roleIds);

            // Đường dẫn trong Firebase Storage, dùng thư mục UserImage và ID của người dùng
            String uploadDir = "UserImages/" + user.getId();

            String newUploadDir = uploadDir + "/" + fileName;
            BlobId blobId = BlobId.of("myshop-ebeb4.appspot.com", newUploadDir);

            String oldUploadDir = uploadDir + "/" + oldFileName;
            // Xóa têp tin cũ nếu có
            if (!oldUploadDir.equals(newUploadDir)) {
                user.setPhotoUrl(fileName);
                if (oldFileName != null && !oldFileName.isEmpty()) {
                    Blob blob = StorageClient.getInstance().bucket().get(oldUploadDir);
                    if (blob != null) {
                        blob.delete();
                    }
                }

                String contentType = multipartFile.getContentType();
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                StorageClient.getInstance().bucket().create(newUploadDir, multipartFile.getInputStream(), contentType);
                String publicUrl = String.format(
                        "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                        "myshop-ebeb4.appspot.com",
                        URLEncoder.encode(newUploadDir, StandardCharsets.UTF_8.toString())
                );
                user.setPhotoUrl(publicUrl);
                userService.save(user, roleIds);
            } else {
                user.setPhotoUrl(oldFileName);
                userService.save(user, roleIds);
            }
        } else {
            if (user.getPhotoUrl().isEmpty()) {
                user.setPhotoUrl(null);
            }
            userService.save(user, roleIds);
        }
//        redirectAttributes.addFlashAttribute("message_success", "The users has been saved successfully.");
        redirectAttributes.addFlashAttribute("message", "User saved successfully.");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
//        redirectAttributes.addFlashAttribute("errorMessage", errorMessage2);
//        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

        return "redirect:/admin-user";
    }

//    @GetMapping("/edit/{id}")
//    public String editUser(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
//        try {
//            User user = userService.getUserById(id);
//            List<Role> roles = (List<Role>) roleRepository.findAll();
//            model.addAttribute("roles", roles);
//            model.addAttribute("user", user);
//            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
//            return "users/user_form";
//        } catch (UserNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message_error", e.getMessage());
//            return "redirect:/users";
//        }
//    }
//
//    @GetMapping("{id}/enabled/{status}")
//    public String updateUserStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status, RedirectAttributes redirectAttributes) {
//        try {
//            userService.updateUserStatus(id, status);
//            redirectAttributes.addFlashAttribute("message_success", "The users has been " + (status ? "enabled" : "disabled") + " successfully.");
//        } catch (UserNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message_error", e.getMessage());
//        }
//        return "redirect:/users";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
//        try {
//            String uploadDir = "UserImages/" + id;
//            StorageClient.getInstance().bucket().get(uploadDir).delete();
//            userService.delete(id);
//            redirectAttributes.addFlashAttribute("message_success", "The users has been deleted successfully.");
//        } catch (UserNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message_error", e.getMessage());
//        }
//        return "redirect:/users";
//    }

//    @GetMapping("/export_csv")
//    public void exportToCSV(HttpServletResponse response) throws IOException {
//
//    }

    @GetMapping("/updateStatus{id}/{status}")
    public String updateStatus(@PathVariable Integer id, @PathVariable boolean status) {
        try {
            userService.updateStatus(id, status);
        } catch (UserNotFoundException e) {
            return "redirect:/admin-user";
        }
        return "redirect:/admin-user";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<User> user = this.userService.findById(id);
        if (user.isPresent()) {
            String fileImage = user.get().getPhotoUrl();
            if (fileImage != null && !fileImage.isEmpty()) {
                fileImage = URLDecoder.decode(fileImage, StandardCharsets.UTF_8);
                int lastIndex = fileImage.lastIndexOf("/");
                if (lastIndex != -1) {
                    fileImage = fileImage.substring(lastIndex + 1);
                }
                if (fileImage.contains("?")) {
                    fileImage = fileImage.substring(0, fileImage.indexOf('?'));
                }
                String uploadDir = "UserImages/" + id + "/" + fileImage;
                StorageClient.getInstance().bucket().get(uploadDir).delete();
            }
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "User has been deleted successfully!");
        } else {
            throw new UserNotFoundException("User not found");
        }
        return "redirect:/admin-user";
    }

}
