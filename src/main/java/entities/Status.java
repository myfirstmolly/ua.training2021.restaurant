package entities;

public enum Status {
    OPENED(0),
    PENDING(1),
    COOKING(2),
    DELIVERING(3),
    DONE(4);

    int value;

    Status(int value) {
        this.value = value;
    }

    public static boolean contains(String val) {
        for (Status v : values()) {
            if (v.name().equals(val))
                return true;
        }
        return false;
    }

    public int toInt() {
        return value;
    }
}
