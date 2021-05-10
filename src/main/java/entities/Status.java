package entities;

public enum Status {
    OPENED(0),
    COOKING(1),
    DELIVERING(2),
    DONE(3);

    int value;

    Status(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }
}
