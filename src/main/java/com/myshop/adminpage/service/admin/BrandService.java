package com.myshop.adminpage.service.admin;

import com.myshop.adminpage.exception.BrandNotFoundException;
import com.myshop.adminpage.model.Brand;
import com.myshop.adminpage.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BrandService {

    private static final int BRAND_PER_PAGE = 5;

    @Autowired
    private BrandRepository brandRepository;

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Page<Brand> listByPage(String keyword, Integer pageNum, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        if (keyword != null) {
            return brandRepository.searchByName(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }

    public void deleteBrand(Integer id) throws BrandNotFoundException {
        getBrandById(id);
        brandRepository.deleteById(id);
    }

    public Set<Brand> findCategoriesOfBrand(Integer id) throws BrandNotFoundException {
        return brandRepository.getCategoriesOfBrand(id);
    }

    public void updateStatus(Integer id, boolean status) {
        getBrandById(id);
        brandRepository.updateStatus(id, status);
    }


    public Brand getBrandById(Integer id) throws BrandNotFoundException {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Couldn't find any brand with id =" + id));
    }

    public Optional<Brand> findById(Integer id) {
        return brandRepository.findById(id);
    }

    public String handlerExceptionAddBrand(Integer id, String name) {
        Brand brandName = brandRepository.findByName(name);
//        Brand brandId = brandRepository.findById(id).orElse(null);
        if (brandName == null) {
            return "OK";
        }
        boolean isCreating = (id == null);
//        if (isCreating) {
//            id = 0;
//        }
        if (isCreating) { //true
            if (brandName != null) {
                return "Brand name exists";
            }
        } else { // false
            if (brandName != null && brandName.getId() != id) {
                return "Brand name exists";
            }
        }

//        if (brandName != null) {
//            if (brandName.getId() != id) {
//                return "Brand name exists";
//            }
//            Brand brandSlug = brandRepository.findBySlug(slug);
//            if (brandSlug != null) {
//                if (brandSlug.getId() != id) {
//                    return "Brand slug exists";
//                }
//            }
//        } else {
//            Brand brandSlug = brandRepository.findBySlug(slug);
//            if (brandSlug != null && brandSlug.getId() != id) {
//                return "Brand slug exists";
//            }
//        }

        return "OK";
    }

    public List<Brand> findBrandsByCategory(Integer categoryId) {
        return brandRepository.findAllBrandsByCategoryId(categoryId);
    }


}
