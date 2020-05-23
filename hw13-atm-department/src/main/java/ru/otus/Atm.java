package ru.otus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class Atm {
    private final TreeMap<Denomination, MoneyBundle> cellsInitialState;
    private TreeMap<Denomination, MoneyBundle> cells;

    public Atm(List<MoneyBundle> cells) {
        this.cellsInitialState = new TreeMap<>(Collections.reverseOrder());
        this.cells = new TreeMap<>(Collections.reverseOrder());
        for (var cell : cells) {
            var denomination = cell.getBankNoteDenomination();
            var count = cell.getBankNotesCount();

            assert !this.cells.containsKey(denomination);

            this.cells.put(denomination, new MoneyBundle(denomination, count));
            cellsInitialState.put(denomination, new MoneyBundle(denomination, count));
        }
    }

    public List<MoneyBundle> depositMoney(List<MoneyBundle> money) {
        var declinedMoney = new ArrayList<MoneyBundle>();
        for (var bundle : money) {
            var denomination = bundle.getBankNoteDenomination();
            var cell = cells.get(denomination);
            if (cell == null) {
                declinedMoney.add(bundle);
                continue;
            }
            cell.depositBankNotes(bundle.getBankNotesCount());
        }
        return declinedMoney;
    }

    public List<MoneyBundle> receiveMoney(int amount) throws Exception {
        var moneyToReceive = new ArrayList<MoneyBundle>();
        for (var cell : cells.values()) {
            var denomination = cell.getBankNoteDenomination();
            var denominationValue = denomination.getValue();
            var count = Integer.min(amount / denominationValue, cell.getBankNotesCount());
            amount -= denominationValue * count;
            moneyToReceive.add(new MoneyBundle(denomination, count));
        }

        if (amount > 0) {
            throw new Exception("Not enough money");
        }

        for (var bundle : moneyToReceive) {
            var cell = cells.get(bundle.getBankNoteDenomination());
            cell.receiveBankNotes(bundle.getBankNotesCount());
        }

        return moneyToReceive;
    }

    public int getAmount() {
        int amount = 0;
        for (var cell : cells.values()) {
            amount += cell.getBankNoteDenomination().getValue() * cell.getBankNotesCount();
        }
        return amount;
    }

    public void restore() {
        cells = cellsInitialState;
    }

    public String toString() {
        return String.format("Atm(%s)", cells);
    }
}
