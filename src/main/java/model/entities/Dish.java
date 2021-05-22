package model.entities;

import java.util.Objects;

public final class Dish implements Entity {

    private int id;

    private String name;

    private String nameUkr;

    private int price;

    private String description;

    private String descriptionUkr;

    private String imagePath;

    private Category category;

    public Dish() {
    }

    public Dish(String name, String nameUkr, int price, String description, String descriptionUkr,
                String imagePath, Category category) {
        this.name = name;
        this.nameUkr = nameUkr;
        this.price = price;
        this.description = description;
        this.descriptionUkr = descriptionUkr;
        this.imagePath = imagePath;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameUkr='" + nameUkr + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", descriptionUkr='" + descriptionUkr + '\'' +
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
                Objects.equals(name, dish.name) &&
                Objects.equals(nameUkr, dish.nameUkr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameUkr);
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

    public String getNameUkr() {
        return nameUkr;
    }

    public void setNameUkr(String nameUkr) {
        this.nameUkr = nameUkr;
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

    public String getDescriptionUkr() {
        return descriptionUkr;
    }

    public void setDescriptionUkr(String descriptionUkr) {
        this.descriptionUkr = descriptionUkr;
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
