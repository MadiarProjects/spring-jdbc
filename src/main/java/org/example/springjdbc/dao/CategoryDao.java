package org.example.springjdbc.dao;

import org.example.springjdbc.model.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();
    Category findById(long id);
    Category create (Category category);
    Category update(Category category);
    void deleteById(long id);
}
