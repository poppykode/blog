package co.zw.poppykode.blog.controller;


import co.zw.poppykode.blog.payload.CategoryDto;
import co.zw.poppykode.blog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto saveCategory = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(saveCategory, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long categoryId){
        return ResponseEntity.ok(categoryService.getCategory(categoryId));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>>  getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable("id") Long categoryId){
        return  ResponseEntity.ok(categoryService.updateCategory(categoryDto,categoryId));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId){
        return new ResponseEntity<>("category with id "+ categoryId +" was successfully deleted",HttpStatus.NO_CONTENT);
    }

}
