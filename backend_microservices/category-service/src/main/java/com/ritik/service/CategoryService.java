package com.ritik.service;

import com.ritik.dto.SalonDTO;
import com.ritik.modal.Category;

import java.util.Set;

public interface CategoryService {

    Category saveCategory(Category category, SalonDTO salonDTO);
    Set<Category> getAllCategoriesBySalonId(Long salonId);
    Category getCategoryById(Long id) throws Exception;
    void deleteCategoryById(Long id, Long SalonId) throws Exception;

}
