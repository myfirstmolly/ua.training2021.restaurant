package entities;

public enum Role {
    MANAGER(0),
    CUSTOMER(1);

    int value;

    Role(int i) {
        value = i;
    }

    public int toInt() {
        return value;
    }

}
