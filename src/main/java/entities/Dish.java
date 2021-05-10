package entities;

import java.util.Objects;

public final class Dish implements Entity {

    private int id;

    private String nameEng;

    private String nameUkr;

    private int price;

    private String descriptionEng;

    private String descriptionUkr;

    private String imagePath;

    private Category category;

    public Dish() {
    }

    public Dish(String nameEng,
                String nameUkr,
                int price,
                String descriptionEng,
                String descriptionUkr,
                String imagePath,
                Category category) {
        this.nameEng = nameEng;
        this.nameUkr = nameUkr;
        this.price = price;
        this.descriptionEng = descriptionEng;
        this.descriptionUkr = descriptionUkr;
        this.imagePath = imagePath;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", nameEng='" + nameEng + '\'' +
                ", nameUkr='" + nameUkr + '\'' +
                ", price=" + price +
                ", descriptionEng='" + descriptionEng + '\'' +
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
        return id == dish.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
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

    public String getDescriptionEng() {
        return descriptionEng;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
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
