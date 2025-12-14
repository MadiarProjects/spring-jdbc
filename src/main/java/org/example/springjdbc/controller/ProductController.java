package org.example.springjdbc.controller;

import lombok.AllArgsConstructor;
import org.example.springjdbc.dao.ProductDao;
import org.example.springjdbc.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductDao productDao;

    @GetMapping
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable long id) {
        return productDao.findById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productDao.create(product);
    }

    @PutMapping
    public Product update(@RequestBody Product product) {
        return productDao.update(product);
    }

    @PutMapping("/{productId}/tag/{tagId}")
    public void updateTag(@PathVariable long productId,@PathVariable long tagId ){
        productDao.updateTag(productId,tagId);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        productDao.deleteById(id);
    }
}
