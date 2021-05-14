package entities;

import java.util.Objects;

public final class Category implements Entity {

    private int id;

    private String nameEng;

    private String nameUkr;

    public Category() {
    }

    public Category(String nameEng, String nameUkr) {
        this.nameEng = nameEng;
        this.nameUkr = nameUkr;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", nameEng=" + nameEng +
                ", nameUkr=" + nameUkr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                Objects.equals(nameEng, category.nameEng) &&
                Objects.equals(nameUkr, category.nameUkr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameEng, nameUkr);
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

}
