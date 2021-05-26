package model.dao.mapper;

import model.entities.Dish;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DishMapper implements Mapper<Dish> {

    private final String id;
    private final String name;
    private final String nameUkr;
    private final String price;
    private final String description;
    private final String descriptionUkr;
    private final String imagePath;
    private final CategoryMapper categoryMapper;

    public DishMapper(String id, String name, String nameUkr, String price, String description,
                      String descriptionUkr, String imagePath, CategoryMapper categoryMapper) {
        this.id = id;
        this.name = name;
        this.nameUkr = nameUkr;
        this.price = price;
        this.description = description;
        this.descriptionUkr = descriptionUkr;
        this.imagePath = imagePath;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Dish map(ResultSet rs) throws SQLException {
        Dish dish = new Dish();
        dish.setId(rs.getInt(id));
        dish.setName(rs.getString(name));
        dish.setNameUkr(rs.getString(nameUkr));
        dish.setPrice(rs.getInt(price));
        dish.setDescription(rs.getString(description));
        dish.setDescriptionUkr(rs.getString(descriptionUkr));
        dish.setImagePath(rs.getString(imagePath));
        dish.setCategory(categoryMapper.map(rs));
        return dish;
    }
}
