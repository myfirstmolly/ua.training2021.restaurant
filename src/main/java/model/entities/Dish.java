package model.entities;

import java.util.Objects;

public final class Dish implements Entity {

    private int id;

    private String name;

    private int price;

    private String description;

    private String imagePath;

    private Category category;

    public Dish() {
    }

    public Dish(String name,
                int price,
                String description,
                String imagePath,
                Category category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id &&
                price == dish.price &&
                name.equals(dish.name) &&
                description.equals(dish.description) &&
                category.equals(dish.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, category);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
