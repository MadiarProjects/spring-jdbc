package org.example.springjdbc.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.springjdbc.dao.CategoryDao;
import org.example.springjdbc.model.Category;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryDao categoryDao;
    @GetMapping
    public List<Category>findAll(){
        return categoryDao.findAll();
    }
    @GetMapping("/{id}")
    public Category findById(@PathVariable long id){
        return categoryDao.findById(id);
    }
    @PostMapping
    public Category create(@RequestBody Category category){
        return categoryDao.create(category);
    }
    @PutMapping
    public Category update(@RequestBody Category category){
        return categoryDao.update(category);
    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id){
        categoryDao.deleteById(id);
    }
}
