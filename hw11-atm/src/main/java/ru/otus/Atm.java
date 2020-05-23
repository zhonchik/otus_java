package ru.otus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class Atm {
    private final TreeMap<Denomination, MoneyBundle> cells;

    public Atm(TreeMap<Denomination, MoneyBundle> cells) {
        this.cells = cells;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private final TreeMap<Denomination, MoneyBundle> cells = new TreeMap<>(Collections.reverseOrder());

        private Builder() {
        }

        public Builder addCell(MoneyBundle cell) throws Exception {
            var denomination = cell.getBankNoteDenomination();
            if (cells.containsKey(denomination)) {
                throw new Exception(String.format("ATM already has cell with denomination of %s", denomination));
            }
            cells.put(cell.getBankNoteDenomination(), cell);
            return this;
        }

        public Atm build() {
            return new Atm(cells);
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

    public String toString() {
        return String.format("Atm(%s)", cells);
    }
}
