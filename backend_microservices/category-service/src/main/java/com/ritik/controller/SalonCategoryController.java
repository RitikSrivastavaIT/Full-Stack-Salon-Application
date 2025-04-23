package com.ritik.controller;

import com.ritik.dto.SalonDTO;
import com.ritik.modal.Category;
import com.ritik.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category){

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);
        Category newCategory = categoryService.saveCategory(category, salonDTO);
        return ResponseEntity.ok(newCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) throws Exception {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);

        categoryService.deleteCategoryById(id, salonDTO.getId());

        return ResponseEntity.ok("Category with id :: " + id + " deleted successfully");
    }
}
