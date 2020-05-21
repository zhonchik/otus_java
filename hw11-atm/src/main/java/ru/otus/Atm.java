package ru.otus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Atm {
    List<MutableCurrencyBundle> cells = new ArrayList<>();
    Map<Denomination, MutableCurrencyBundle> denominationToCell = new HashMap<>();

    public static Builder newBuilder() {
        return new Atm().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder addCell(MutableCurrencyBundle cell) throws Exception {
            var denomination = cell.getBankNoteDenomination();
            if (denominationToCell.containsKey(denomination)) {
                throw new Exception(String.format("ATM already has cell with denomination of %s", denomination));
            }
            Atm.this.cells.add(cell);
            Atm.this.denominationToCell.put(cell.getBankNoteDenomination(), cell);
            return this;
        }

        public Atm build() {
            Atm.this.cells.sort(Comparator.<MutableCurrencyBundle>comparingInt(
                    c -> c.getBankNoteDenomination().getValue()
            ).reversed());
            return Atm.this;
        }
    }

    public Currency depositCurrency(Currency currency) {
        var declinedCurrencyBuilder = Currency.newBuilder();
        for (var bundle : currency.getBundles()) {
            var denomination = bundle.getBankNoteDenomination();
            var cell = denominationToCell.get(denomination);
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
        for (var cell : cells) {
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
            var cell = denominationToCell.get(bundle.getBankNoteDenomination());
            cell.receiveBankNotes(bundle.getBankNotesCount());
        }

        return currencyToReceive;
    }

    public int getAmount() {
        int amount = 0;
        for (var cell : cells) {
            amount += cell.getBankNoteDenomination().getValue() * cell.getBankNotesCount();
        }
        return amount;
    }

    public String toString() {
        return String.format("Atm(%s)", cells);
    }
}
