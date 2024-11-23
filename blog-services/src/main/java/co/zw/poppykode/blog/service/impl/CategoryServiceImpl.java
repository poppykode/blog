package co.zw.poppykode.blog.service.impl;

import co.zw.poppykode.blog.entity.Category;
import co.zw.poppykode.blog.exception.ResourceNotFoundException;
import co.zw.poppykode.blog.payload.CategoryDto;
import co.zw.poppykode.blog.repository.CategoryRepository;
import co.zw.poppykode.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto,Category.class);
        Category saveCategory = categoryRepository.save(category);
        return modelMapper.map(saveCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().
                map(category -> modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category category = getCategoryById(categoryId);
        category.setDescription(categoryDto.getDescription());
        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.delete(getCategoryById(categoryId));
    }
    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","Id", categoryId));
    }

}
