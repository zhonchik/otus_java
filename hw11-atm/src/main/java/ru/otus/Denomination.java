package ru.otus;

public enum Denomination {
    D50(50),
    D100(100),
    D200(200),
    D500(500),
    D1000(1000);
    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
