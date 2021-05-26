package model.entities;

import java.util.Objects;

public final class Category implements Entity {

    private int id;

    private String name;

    private String nameUkr;

    public Category() {
    }

    public Category(String name, String nameUkr) {
        this.name = name;
        this.nameUkr = nameUkr;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                Objects.equals(name, category.name) &&
                Objects.equals(nameUkr, category.nameUkr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameUkr);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameUkr='" + nameUkr + '\'' +
                '}';
    }

}
