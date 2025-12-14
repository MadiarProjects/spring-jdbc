package org.example.springjdbc.dao;

import lombok.AllArgsConstructor;
import org.example.springjdbc.model.Category;
import org.example.springjdbc.model.Product;
import org.example.springjdbc.model.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class ProductDaoImpl implements ProductDao {
    private final JdbcTemplate jdbcTemplate;
    private final CategoryDao categoryDao;

    @Override
    public List<Product> findAll() {
        String sql= """
                 select 
                    p.id          as product_id,
                    p.name        as product_name,
                    p.price       as product_price,
                    c.id          as category_id,
                    c.name        as category_name
                from products p
                join categories c on p.category_id = c.id
                 order by product_id;
                """;

        System.out.println(sql);
        return jdbcTemplate.query(sql,this::mapRow);
    }

    @Override
    public Product findById(long id) {
        String sql= """
                select 
                    p.id          as product_id,
                    p.name        as product_name,
                    p.price       as product_price,
                    c.id          as category_id,
                    c.name        as category_name
                from products p
                join categories c on p.category_id = c.id
                where p.id=?;
                """;
        return  jdbcTemplate.query(sql,this::mapRow,id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Product create(Product product) {
        SimpleJdbcInsert insert=new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("products")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> map=new HashMap<>();
//        map.put("столбец","значение");
        map.put("name",product.getName());
        map.put("price",product.getPrice());
        map.put("category_id",product.getCategory().getId());
        long number=insert.executeAndReturnKey(map).longValue();
        product.setId(number);
        String sqlForTags = "insert into products_tags (product_id, tag_id) values (?, ?)";
        product.getTags().forEach(tag -> {
            try {
                jdbcTemplate.update(sqlForTags, product.getId(), tag.getId());
            }catch (Exception e){
             throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        });
        return product;
    }

    @Override
    @Transactional
    public Product update(Product product) {
        String sqlForTags = "insert into products_tags (product_id, tag_id) values (?, ?)";
        product.getTags().forEach(tag -> {
            jdbcTemplate.update(sqlForTags, product.getId(), tag.getId());
        });
        String sql= """
                update products set name=?,price = ? where id=?
                """;
        int rowsAffected= jdbcTemplate.update(sql,product.getName(),product.getPrice(),product.getId());
        if (rowsAffected==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return product;
    }
    @Override
    public void updateTag(long productId,long tagId){
        String sql= """
                insert into products_tags (product_id,tag_id)values (?,?)
                """;
        jdbcTemplate.update(sql,productId,tagId);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        String sql= """
                delete from products where id=?;
                """;
        String sqlDeleteCategory= """
                delete from products_tags where product_id=?
                """;
        jdbcTemplate.update(sqlDeleteCategory,id);
        int rowsAffected= jdbcTemplate.update(sql,id);
        if (rowsAffected==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

//    private List<Tag> mapRowForTags(ResultSet rs,int rowNum)throws SQLException{
//        long tagId=rs.getLong("tag_id");
//        String tagName=rs.getString("tag_name");
//        List<Tag>tags=new ArrayList<>();
//        tags.add(new Tag(tagId,tagName));
//        while (rs.next()){
//            tags.add(new Tag(rs.getLong("tag_id"),rs.getString("tag_name")));
//        }
//        return tags;
//    }
//    private Product mapRowUpdateTag(ResultSet rs,int rowNum)throws SQLException{
//        String sqlForProduct= """
//                """;
//        String sqlForTag
//        return new Product();
//    }

    private Product mapRow(ResultSet rs, int rowNum)throws SQLException {
        long id= rs.getLong("product_id");
        String name=rs.getString("product_name");
        double price=rs.getDouble("product_price");
        Category category=new Category(rs.getLong("category_id"),rs.getString("category_name"));
        String sqlForTags= """
                select 
                t.id as tag_id,
                t.name as tag_name
                from  products_tags pt 
                join tags t on pt.tag_id = t.id
                where pt.product_id=?
                """;
        SqlRowSet srs=jdbcTemplate.queryForRowSet(sqlForTags,id);
        List<Tag>tags=new ArrayList<>();
        while (srs.next()){
            tags.add(new Tag(srs.getLong("tag_id"),srs.getString("tag_name")));
        }
        return new Product(id,name,price,category,tags);
    }
}
