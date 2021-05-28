package model.entities;

public enum Status {

    OPENED(0, "відкрито"),
    PENDING(1, "очікує підтвердження"),
    COOKING(2, "готується"),
    DELIVERING(3, "доставляється"),
    DONE(4, "виконано");

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
