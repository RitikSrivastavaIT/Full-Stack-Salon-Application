package com.ritik.service.impl;

import com.ritik.dto.SalonDTO;
import com.ritik.modal.Category;
import com.ritik.repository.CategoryRepository;
import com.ritik.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setSalonId(salonDTO.getId());
        newCategory.setImage(category.getImage());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoriesBySalonId(Long salonId) {
        return categoryRepository.findBySalonId(salonId);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            throw new Exception("Category not found with id :: " + id);
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long id, Long SalonId) throws Exception {
        Category category = getCategoryById(id);
        if(!category.getSalonId().equals(SalonId)){
            throw new Exception("You don't have permission to delete the category with id :: " + id);
        }
        categoryRepository.deleteById(id);
    }
}