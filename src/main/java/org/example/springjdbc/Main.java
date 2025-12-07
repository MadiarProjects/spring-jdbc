package org.example.springjdbc;

import org.example.springjdbc.exceptions.NotFoundException;
import org.example.springjdbc.model.Category;

import java.util.List;
import java.util.Optional;

public class Main {
    static List<Category> categories=List.of(
    new Category(1,"")
    );

    public static void main(String[] args) {
//        Optional<Category> optional=findById(1);
//        if (optional.isPresent()){
//            Category category= optional.get();
//            System.out.println(category.getName());
//        }
        Category category2=findById(2).orElseThrow(()->new NotFoundException(""));
        System.out.println(category2.getName());
    }
    static Optional<Category> findById(long id){
        return categories.stream()
                .filter(category -> category.getId()==id)
                .findFirst();
//
//        for (Category category:categories){
//            if (category.getId()==id){
//                return Optional.of(category);
//            }
//        }
//        return Optional.empty();
    }
}
