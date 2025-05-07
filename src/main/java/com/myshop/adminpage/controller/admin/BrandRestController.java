package com.myshop.adminpage.controller.admin;

import com.myshop.adminpage.dto.BrandDto;
import com.myshop.adminpage.model.Brand;
import com.myshop.adminpage.service.admin.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-brand")
public class BrandRestController {
    @Autowired
    private BrandService brandService;

//    @PostMapping("/check-name-add-form")
//    public String checkNameAddForm(@RequestParam(value = "id", required = false) Integer id, @RequestParam("name") String name) {
//        return brandService.handlerExceptionAddBrand(id, name);
//    }

    @PostMapping("/check-name-add-form")
    public ResponseEntity<String> checkNameAddForm(@RequestBody BrandDto brandDto) {
        String result = brandService.handlerExceptionAddBrand(brandDto.getId(), brandDto.getName());
        return ResponseEntity.ok().body(result);
    }

//    @PostMapping("editBrand{id}/check-name-edit-form")
//    public String checkNameEditForm(@PathVariable Integer id, @RequestParam("name") String name) {
//        return brandService.handlerExceptionAddBrand(id, name);
//    }

    @PostMapping("editBrand{id}/check-name-edit-form")
    public ResponseEntity<String> checkNameEditForm(@PathVariable Integer id, @RequestBody BrandDto brandDto) {
        String result = brandService.handlerExceptionAddBrand(id, brandDto.getName());
        return ResponseEntity.ok().body(result);
    }


}
