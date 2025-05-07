package com.myshop.adminpage.repository;

import com.myshop.adminpage.model.Brand;
import com.myshop.adminpage.model.Category;
import com.myshop.adminpage.model.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    @Query("SELECT b FROM Brand b WHERE concat(b.id, ' ', b.name) LIKE %?1%")
    Page<Brand> searchByName(String keyword, Pageable pageable);

    Brand findByName(String name);

    @Query("UPDATE Brand b SET b.active = :status WHERE b.id = :id")
    @Modifying
    @Transactional
    void updateStatus(@Param("id") Integer id, @Param("status") boolean status);

    @Query("SELECT b.categories FROM Brand b WHERE b.id = :id")
    Set<Brand> getCategoriesOfBrand(@Param("id") Integer id);

    @Query("SELECT s from Series s join s.brand b where b.id = :id")
    List<Series> findAllSeriesByBrandId(@Param("id") Integer id);

    @Query("SELECT b from Brand b join b.categories c where c.id = :id")
    List<Brand> findAllBrandsByCategoryId(@Param("id") Integer id);

    @Query("SELECT distinct b from Brand b join b.categories c where c in :categories")
    List<Brand> findDistinctByCategories(Set<Category> categories);
}
