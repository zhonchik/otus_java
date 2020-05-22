package ru.otus;

/**
 * CurrencyBundle represents group of bank notes with same denomination
 */
public class CurrencyBundle {

    private final Denomination bankNoteDenomination;
    int bankNotesCount;

    public CurrencyBundle(Denomination bankNoteDenomination, int bankNotesCount) {
        this.bankNoteDenomination = bankNoteDenomination;
        this.bankNotesCount = bankNotesCount;
    }

    public Denomination getBankNoteDenomination() {
        return bankNoteDenomination;
    }

    public int getBankNotesCount() {
        return bankNotesCount;
    }

    public String toString() {
        return String.format("Bundle(%s: %d)", bankNoteDenomination.name(), bankNotesCount);
    }
}
