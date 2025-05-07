package com.myshop.adminpage.controller.admin;

import com.myshop.adminpage.dto.SeriesDto;
import com.myshop.adminpage.service.admin.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-series")
public class SeriesRestController {
    @Autowired
    private SeriesService seriesService;

//    @PostMapping("/check-name-slug-add-form")
//    public String checkNameSlugAddForm(@RequestParam(value = "id", required = false) Integer id, @RequestParam("name") String name, @RequestParam("slug") String slug) {
//        return seriesService.handlerExceptionAddSeries(id, name, slug);
//    }

    @PostMapping("/check-name-slug-add-form")
    public ResponseEntity<String> checkNameSlugAddForm(@RequestBody SeriesDto seriesDto) {
        String result = seriesService.handlerExceptionAddSeries(seriesDto.getId(), seriesDto.getName(), seriesDto.getSlug());
        return ResponseEntity.ok().body(result);
    }

//    @PostMapping("/editSeries{id}/check-name-slug-edit-form")
//    public String checkNameAndSlugEditForm(@PathVariable Integer id, @RequestParam("name") String name, @RequestParam("slug") String slug) {
//        return seriesService.handlerExceptionAddSeries(id, name, slug);
//    }

    @PostMapping("/editSeries{id}/check-name-slug-edit-form")
    public ResponseEntity<String> checkNameAndSlugEditForm(@PathVariable Integer id, @RequestBody SeriesDto seriesDto) {
        String result = seriesService.handlerExceptionAddSeries(id, seriesDto.getName(), seriesDto.getSlug());
        return ResponseEntity.ok().body(result);
    }

//    @GetMapping("/editSeries{id}/check-name-slug-edit-form")
//    public String checkNameAndSlugEditForm(@PathVariable Integer id, @RequestParam("name") String name) {
//        return seriesService.handlerExceptionAddBrand2(id, name);
//    }

}
