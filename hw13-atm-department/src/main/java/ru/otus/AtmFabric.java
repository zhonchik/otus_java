package ru.otus;

import java.util.List;

public class AtmFabric {

    private final List<MoneyBundle> cells;

    public AtmFabric(List<MoneyBundle> cells) {
        this.cells = cells;
    }

    public Atm createAtm() {
        return new Atm(cells);
    }
}
