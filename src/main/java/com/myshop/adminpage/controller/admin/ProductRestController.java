package com.myshop.adminpage.controller.admin;

import com.myshop.adminpage.model.Brand;
import com.myshop.adminpage.model.Series;
import com.myshop.adminpage.service.admin.BrandService;
import com.myshop.adminpage.service.admin.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-product")
public class ProductRestController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private SeriesService seriesService;

//    @GetMapping("/brands")
//    @ResponseBody
//    public List<Brand> getBrandsByCategoryId(@RequestParam("categoryId") Integer categoryId) {
//        return brandService.findBrandsByCategory(categoryId);
//    }

    @GetMapping("/brands")
    @ResponseBody
    public ResponseEntity<List<Brand>> getBrandsByCategoryId(@RequestParam("categoryId") Integer categoryId) {
        List<Brand> brands = brandService.findBrandsByCategory(categoryId);
        return ResponseEntity.ok().body(brands);
    }

//    @GetMapping("/series")
//    @ResponseBody
//    public List<Series> getSeriesByBrandId(@RequestParam("brandId") Integer brandId){
//        return seriesService.findSeriesByBrandId(brandId);
//    }

    @GetMapping("/series")
    @ResponseBody
    public ResponseEntity<List<Series>> getSeriesByBrandId(@RequestParam("brandId") Integer brandId) {
        List<Series> series = seriesService.findSeriesByBrandId(brandId);
        return ResponseEntity.ok(series);
    }
}
