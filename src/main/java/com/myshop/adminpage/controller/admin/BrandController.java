package com.myshop.adminpage.controller.admin;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.firebase.cloud.StorageClient;
import com.myshop.adminpage.Interface.CategoryServiceInf;
import com.myshop.adminpage.exception.BrandNotFoundException;
import com.myshop.adminpage.exception.CategoryNotFoundException;
import com.myshop.adminpage.model.Brand;
import com.myshop.adminpage.model.Category;
import com.myshop.adminpage.repository.BrandRepository;
import com.myshop.adminpage.service.admin.BrandService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Controller
@RequestMapping("/admin-brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryServiceInf categoryServiceInf;
    @Autowired
    private BrandRepository brandRepository;

    @GetMapping("")
    public String index(Model model, @Param("keyword") String keyword, @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(name = "orderBy", defaultValue = "id") String sortField, @RequestParam(name = "direction", defaultValue = "asc") String sortDir) {
        Page<Brand> brands = brandService.listByPage(keyword, pageNum, sortField, sortDir);

        CompletableFuture.runAsync(() -> {
            for (Brand brand : brands) {
                String photoUrl = brand.getLogo();
                if (photoUrl != null && !photoUrl.isEmpty()) {
                    String uploadDir = "BrandImages/" + brand.getId() + "/" + photoUrl;
                    Blob blob = StorageClient.getInstance().bucket().get(uploadDir);
                    if (blob != null) {
                        String imageUrl = null;
                        try {
                            imageUrl = String.format(
                                    "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                                    "myshop-ebeb4.appspot.com",
                                    URLEncoder.encode(uploadDir, StandardCharsets.UTF_8.toString())
                            );
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        brand.setLogo(imageUrl);
                    }
                }
            }
        });

        for (Brand brand : brands) {
            if (!brandRepository.findAllSeriesByBrandId(brand.getId()).isEmpty()) {
                brand.setHasSeries(true);
            } else {
                brand.setHasSeries(false);
            }
        }

        String defaultUrl = "/admin-brand";

//        List<Brand> listBrands = brands.getContent();
//
//        Brand test = listBrands.getFirst();
//
//        var hm = test.getCategories().getClass().getName();
//
//        System.out.println(hm);

        model.addAttribute("brands", brands);
        model.addAttribute("brand", new Brand());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", brands.getTotalPages());
        model.addAttribute("totalItems", brands.getTotalElements());
        model.addAttribute("defaultUrl", defaultUrl);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "/admin/brand/index";
    }

    @PostMapping("/save")
    public String saveBrand(@ModelAttribute Brand brand, @RequestParam("imageFile") MultipartFile multipartFile,
                            RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession,
                            @RequestParam(name = "redirectUrl", required = false) String redirectUrl) throws Exception {
        if (!multipartFile.isEmpty()) {
            String oldFileName = brand.getLogo();
            oldFileName = URLDecoder.decode(oldFileName, StandardCharsets.UTF_8);
            int lastIndex = oldFileName.lastIndexOf("/");
            if (lastIndex != -1) {
                oldFileName = oldFileName.substring(lastIndex + 1);
            }
            if (oldFileName.contains("?")) {
                oldFileName = oldFileName.substring(0, oldFileName.indexOf('?'));
            }

            String fileName = multipartFile.getOriginalFilename();
            brandService.save(brand);
            String uploadDir = "BrandImages/" + brand.getId();

            String newUploadDir = uploadDir + "/" + fileName;
            BlobId blobId = BlobId.of("myshop-ebeb4.appspot.com", newUploadDir);

            String oldUploadDir = uploadDir + "/" + oldFileName;
            // Xóa tệp tin cũ nếu có

            if (!oldUploadDir.equals(newUploadDir)) {
                brand.setLogo(fileName);
//                this.categoryServiceInf.save(category);
                if (oldFileName != null && !oldFileName.isEmpty()) {
                    Blob blob = StorageClient.getInstance().bucket().get(oldUploadDir);
                    if (blob != null) {
                        blob.delete();
                    }
//                StorageClient.getInstance().bucket().get(oldUploadDir).delete();
                }

                // Đặt content type và kiểm tra null
                String contentType = multipartFile.getContentType();
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Gán giá trị mặc định nếu null
                }

                // Upload file lên Firebase Storage
                StorageClient.getInstance().bucket().create(newUploadDir, multipartFile.getInputStream(), contentType);
                String publicUrl = String.format(
                        "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                        "myshop-ebeb4.appspot.com",
                        URLEncoder.encode(newUploadDir, StandardCharsets.UTF_8.toString())
                );
                brand.setLogo(publicUrl);  // Lưu URL công khai vào đối tượng Brand
                this.brandService.save(brand);
            } else {
                brand.setLogo(oldFileName);
                this.brandService.save(brand);
            }

        } else {
            if (brand.getLogo().isEmpty()) {
                brand.setLogo(null);
            }
            this.brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully!");

//        if (redirectUrl != null && !redirectUrl.isEmpty()) {
//            return "redirect:" + redirectUrl;
//        }

        return "redirect:/admin-brand";
    }

    @GetMapping("/createBrand")
    public String createBrand(Model model) {
//        List<Category> brandCategories = categoryServiceInf.findAll();
        List<Category> brandCategories = categoryServiceInf.listHierarchicalCategoriesInForm();

        model.addAttribute("brand", new Brand());
        model.addAttribute("brandCategories", brandCategories);

        return "/admin/brand/add";
    }


    @GetMapping("/deleteBrand/{id}")
    public String deleteBrand(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Brand> brand = this.brandService.findById(id);
        if (brand.isPresent()) {
            String fileImage = brand.get().getLogo();
            if (fileImage != null && !fileImage.isEmpty()) {
                fileImage = URLDecoder.decode(fileImage, StandardCharsets.UTF_8);
                int lastIndex = fileImage.lastIndexOf("/");
                if (lastIndex != -1) {
                    fileImage = fileImage.substring(lastIndex + 1);
                }
                if (fileImage.contains("?")) {
                    fileImage = fileImage.substring(0, fileImage.indexOf('?'));
                }
                String uploadDir = "BrandImages/" + id + "/" + fileImage;
                StorageClient.getInstance().bucket().get(uploadDir).delete();
            }
            brandService.deleteBrand(id);
            redirectAttributes.addFlashAttribute("message", "The brand has been deleted successfully!");
        } else {
            throw new BrandNotFoundException("Brand not found");
        }
        return "redirect:/admin-brand";
    }

    @GetMapping("/editBrand{id}")
    public String editCategory(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Brand> brand = brandService.findById(id);
            List<Category> brandCategories = categoryServiceInf.listHierarchicalCategoriesInForm();
//            List<Category> brandCategories = categoryServiceInf.findAll();
            if (brand.isPresent()) {
                model.addAttribute("brand", brand.get());
                model.addAttribute("brandCategories", brandCategories);
                return "/admin/brand/edit";
            }
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin-brand";
        }

        return "redirect:/admin-brand";
//
//        model.addAttribute("brand", brandService.getCategoryById(id));
//
//        return "/admin/brand/index";
    }

    @GetMapping("/updateStatus{id}/{status}")
    public String updateStatus(@PathVariable Integer id, @PathVariable boolean status) {
        try {
            brandService.updateStatus(id, status);
        } catch (CategoryNotFoundException e) {
            return "redirect:/admin-brand";
        }
        return "redirect:/admin-brand";
    }


}
