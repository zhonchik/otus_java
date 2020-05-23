package ru.otus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class Atm {
    private final TreeMap<Denomination, CurrencyBundle> cells = new TreeMap<>(Collections.reverseOrder());

    public static Builder newBuilder() {
        return new Atm().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder addCell(CurrencyBundle cell) throws Exception {
            var denomination = cell.getBankNoteDenomination();
            if (cells.containsKey(denomination)) {
                throw new Exception(String.format("ATM already has cell with denomination of %s", denomination));
            }
            Atm.this.cells.put(cell.getBankNoteDenomination(), cell);
            return this;
        }

        public Atm build() {
            return Atm.this;
        }
    }

    public List<CurrencyBundle> depositCurrency(List<CurrencyBundle> currency) {
        var declinedCurrency = new ArrayList<CurrencyBundle>();
        for (var bundle : currency) {
            var denomination = bundle.getBankNoteDenomination();
            var cell = cells.get(denomination);
            if (cell == null) {
                declinedCurrency.add(bundle);
                continue;
            }
            cell.depositBankNotes(bundle.getBankNotesCount());
        }
        return declinedCurrency;
    }

    public List<CurrencyBundle> receiveCurrency(int amount) throws Exception {
        var currencyToReceive = new ArrayList<CurrencyBundle>();
        for (var cell : cells.values()) {
            var denomination = cell.getBankNoteDenomination();
            var denominationValue = denomination.getValue();
            var count = Integer.min(amount / denominationValue, cell.getBankNotesCount());
            amount -= denominationValue * count;
            currencyToReceive.add(new CurrencyBundle(denomination, count));
        }

        if (amount > 0) {
            throw new Exception("Not enough money");
        }

        for (var bundle : currencyToReceive) {
            var cell = cells.get(bundle.getBankNoteDenomination());
            cell.receiveBankNotes(bundle.getBankNotesCount());
        }

        return currencyToReceive;
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
