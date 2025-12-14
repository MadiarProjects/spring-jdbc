package org.example.springjdbc.dao;

import lombok.RequiredArgsConstructor;
import org.example.springjdbc.exceptions.NotFoundException;
import org.example.springjdbc.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CategoryDaoImpl implements CategoryDao{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Category> findAll() {
        String sql= """
                select * from categories
                """;

        return jdbcTemplate.query(sql,this::mapRow);
//        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public Category findById(long id) {
        String sql= """
                select * from categories where id=?
                """;
        System.out.println(sql);
//        List<Category> list= jdbcTemplate.query(sql,this::mapRow,id);
//        if (list.isEmpty()){
//            throw new NotFoundException(e.getMessage);
//        }else {
//            return list.get(0);
//        }
        return  jdbcTemplate.query(sql,this::mapRow,id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        try{
//            return jdbcTemplate.queryForObject(sql,this::mapRow,id);
//
//        }catch (EmptyResultDataAccessException e){
//
//        }
    }

    @Override
    public Category create(Category category) {
//        String sql= """
//                insert into categories(name) values(?)
//                """;
//        jdbcTemplate.update(sql,category.getName());
//        return category;
        SimpleJdbcInsert insert=new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("categories")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> map=new HashMap<>();
//        map.put("столбец","значение");
        map.put("name",category.getName());
        long number=insert.executeAndReturnKey(map).longValue();
        category.setId(number);
        return category;
    }

    @Override
    public Category update(Category category) {
        String sql= """
                update categories set name=? where id=?
                """;
        int rowsAffected= jdbcTemplate.update(sql,category.getName(),category.getId());
        if (rowsAffected==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
         return category;
    }

    @Override
    public void deleteById(long id) {
        String sql= """
                delete from categories where id=?;
                """;
        String sqlDeleteProducts= """
                delete from products where category_id=?
                """;
        jdbcTemplate.update(sqlDeleteProducts,id);
        int rowsAffected= jdbcTemplate.update(sql,id);
        if (rowsAffected==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    private Category mapRow(ResultSet rs,int rowNum)throws SQLException {
            long id= rs.getLong("id");
            String name=rs.getString("name");
            return new Category(id,name);
    }
}
