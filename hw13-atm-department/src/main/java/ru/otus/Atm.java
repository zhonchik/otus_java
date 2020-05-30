package ru.otus;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.TreeMap;

public class Atm {
    private AtmState currentState;
    private final Deque<AtmState> states = new ArrayDeque<>();

    public Atm(List<MoneyBundle> cells) {
        var cellsMap = new TreeMap<Denomination, MoneyBundle>(Collections.reverseOrder());
        for (var cell : cells) {
            var denomination = cell.getBankNoteDenomination();
            var count = cell.getBankNotesCount();

            assert !cellsMap.containsKey(denomination);

            cellsMap.put(denomination, new MoneyBundle(denomination, count));
        }
        currentState = new AtmState(cellsMap);
        saveState();
    }

    public List<MoneyBundle> depositMoney(List<MoneyBundle> money) {
        var declinedMoney = new ArrayList<MoneyBundle>();
        for (var bundle : money) {
            var denomination = bundle.getBankNoteDenomination();
            var cell = currentState.getCells().get(denomination);
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
        for (var cell : currentState.getCells().values()) {
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
            var cell = currentState.getCells().get(bundle.getBankNoteDenomination());
            cell.receiveBankNotes(bundle.getBankNotesCount());
        }

        return moneyToReceive;
    }

    public int getAmount() {
        int amount = 0;
        for (var cell : currentState.getCells().values()) {
            amount += cell.getBankNoteDenomination().getValue() * cell.getBankNotesCount();
        }
        return amount;
    }

    public void saveState() {
        states.push(new AtmState(currentState));
    }

    public void restore() {
        currentState = states.pop();
    }

    public String toString() {
        return String.format("Atm(%s)", currentState);
    }
}
