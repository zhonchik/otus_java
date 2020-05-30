package ru.otus;

import java.util.Collections;
import java.util.TreeMap;

public class AtmState {
    private final TreeMap<Denomination, MoneyBundle> cells;

    AtmState(TreeMap<Denomination, MoneyBundle> cells) {
        this.cells = new TreeMap<>(Collections.reverseOrder());
        for (var bundle: cells.values()) {
            this.cells.put(bundle.getBankNoteDenomination(), bundle.copy());
        }
    }

    AtmState(AtmState state) {
        this.cells = new AtmState(state.getCells()).getCells();
    }

    public TreeMap<Denomination, MoneyBundle> getCells() {
        return cells;
    }

    public String toString() {
        return cells.values().toString();
    }
}
