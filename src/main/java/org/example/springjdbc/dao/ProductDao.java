package org.example.springjdbc.dao;

import org.example.springjdbc.model.Category;
import org.example.springjdbc.model.Product;

import java.util.List;

public interface ProductDao {
    List<Product>findAll();
    Product findById(long id);
    Product create (Product product);
    Product update(Product product);
    void deleteById(long id);
    void updateTag(long productId,long tagId);
}
