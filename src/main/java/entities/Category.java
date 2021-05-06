package entities;

import java.util.Objects;

public final class Category {

    private int id;

    private int nameEng;

    private int nameUkr;

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
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNameEng() {
        return nameEng;
    }

    public void setNameEng(int nameEng) {
        this.nameEng = nameEng;
    }

    public int getNameUkr() {
        return nameUkr;
    }

    public void setNameUkr(int nameUkr) {
        this.nameUkr = nameUkr;
    }

}
