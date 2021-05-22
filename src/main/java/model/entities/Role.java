package model.entities;

public enum Role {
    MANAGER(0),
    CUSTOMER(1);

    int id;

    Role(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name();
    }
}
