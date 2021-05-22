package model.entities;

public enum Status {
    OPENED(0),
    PENDING(1),
    COOKING(2),
    DELIVERING(3),
    DONE(4);

    int id;

    Status(int id) {
        this.id = id;
    }

    public static boolean contains(String val) {
        for (Status v : values()) {
            if (v.name().equals(val))
                return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }
}
