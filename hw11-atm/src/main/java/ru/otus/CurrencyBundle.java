package ru.otus;

/**
 * CurrencyBundle represents group of bank notes with same denomination
 */
public class CurrencyBundle {

    int bankNoteDenomination = 0;
    int bankNotesCount = 0;

    public CurrencyBundle(int bankNoteDenomination, int bankNotesCount) {
        this.bankNoteDenomination = bankNoteDenomination;
        this.bankNotesCount = bankNotesCount;
    }

    public int getBankNoteDenomination() {
        return bankNoteDenomination;
    }

    public int getBankNotesCount() {
        return bankNotesCount;
    }

    public String toString() {
        return String.format("Bundle(%d: %d)", bankNoteDenomination, bankNotesCount);
    }
}
