package model.entities;

public enum Status {

    OPENED(1, "відкрито"),
    PENDING(2, "очікує підтвердження"),
    COOKING(3, "готується"),
    DELIVERING(4, "доставляється"),
    DONE(5, "виконано");

    int id;
    String nameUkr;

    Status(int id, String nameUkr) {
        this.id = id;
        this.nameUkr = nameUkr;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public String getNameUkr() {
        return nameUkr;
    }

    public int getId() {
        return id;
    }

}
