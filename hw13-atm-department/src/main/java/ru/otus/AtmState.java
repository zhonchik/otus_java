package ru.otus;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class AtmState {
    private final Map<Denomination, MoneyBundle> cells;

    AtmState(Map<Denomination, MoneyBundle> cells) {
        this.cells = new TreeMap<>(Collections.reverseOrder());
        for (var bundle: cells.values()) {
            this.cells.put(bundle.getBankNoteDenomination(), bundle.copy());
        }
    }

    AtmState(AtmState state) {
        this.cells = new AtmState(state.getCells()).getCells();
    }

    public Map<Denomination, MoneyBundle> getCells() {
        return cells;
    }

    public String toString() {
        return cells.values().toString();
    }
}
