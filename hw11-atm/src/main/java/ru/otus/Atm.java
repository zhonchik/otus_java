package ru.otus;

import java.util.Collections;
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

    public Currency depositCurrency(Currency currency) {
        var declinedCurrencyBuilder = Currency.newBuilder();
        for (var bundle : currency.getBundles()) {
            var denomination = bundle.getBankNoteDenomination();
            var cell = cells.get(denomination);
            if (cell == null) {
                declinedCurrencyBuilder.addBundle(bundle);
                continue;
            }
            cell.depositBankNotes(bundle.getBankNotesCount());
        }
        return declinedCurrencyBuilder.build();
    }

    public Currency receiveCurrency(int amount) throws Exception {
        var currencyToReceiveBuilder = Currency.newBuilder();
        for (var cell : cells.values()) {
            var denomination = cell.getBankNoteDenomination();
            var denominationValue = denomination.getValue();
            var count = Integer.min(amount / denominationValue, cell.getBankNotesCount());
            amount -= denominationValue * count;
            currencyToReceiveBuilder.addBundle(new CurrencyBundle(denomination, count));
        }

        if (amount > 0) {
            throw new Exception("Not enough money");
        }

        var currencyToReceive = currencyToReceiveBuilder.build();
        for (var bundle : currencyToReceive.getBundles()) {
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
