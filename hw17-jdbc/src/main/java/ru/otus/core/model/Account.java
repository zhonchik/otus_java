package ru.otus.core.model;

import ru.otus.annotations.Id;

import java.math.BigDecimal;

public class Account {
    @Id
    private long no;
    private String type;
    private BigDecimal rest;

    public Account(long no, String type, BigDecimal rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public Account() {}

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getRest() {
        return rest;
    }

    public String toString() {
        return String.format("Account{no=%d, type='%s', rest=%s}", no, type, rest);
    }
}
