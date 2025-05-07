package com.myshop.adminpage.Interface;

import com.myshop.adminpage.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface CategoryServiceInf {
    List<Category> findAll();

    Category save(Category category);

    void deleteCategory(Integer id);

    Optional<Category> findById(Integer id);

    Category getCategoryById(Integer id);

    List<Category> searchByName(String keyword);

    Page<Category> listByPage(Integer pageNum);

    Page<Category> listByPage(String keyword, Integer pageNum, String sortField, String sortDir);

    Page<Category> listByPage2(String keyword, Integer pageNum, String sortField, String sortDir, Integer category_per_page);

    void updateStatus(Integer id, boolean status);

    List<Category> listHierarchicalCategoriesInForm();

    List<Category> listHierarchicalCategoriesInPage(List<Category> rootCategories);

    List<Category> listHierarchicalCategoriesInPage2();

    List<Category> getCategoryHierarchy();

    String handlerExceptionAddCategory(Integer id, String name, String slug);

    String handlerExceptionAddCategory(String name, String slug);

    String validateCategoryParent(Category category);

    Optional<Category> findByIdWithSubCategories(Integer id);

}
