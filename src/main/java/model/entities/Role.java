package model.entities;

public enum Role {
    MANAGER(1),
    CUSTOMER(2);

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
