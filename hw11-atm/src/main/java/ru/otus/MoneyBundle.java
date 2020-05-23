package ru.otus;

/**
 * MoneyBundle represents group of bank notes with same denomination
 */
public class MoneyBundle {

    private final Denomination bankNoteDenomination;
    int bankNotesCount;

    public MoneyBundle(Denomination bankNoteDenomination, int bankNotesCount) {
        this.bankNoteDenomination = bankNoteDenomination;
        this.bankNotesCount = bankNotesCount;
    }

    public Denomination getBankNoteDenomination() {
        return bankNoteDenomination;
    }

    public int getBankNotesCount() {
        return bankNotesCount;
    }

    public void depositBankNotes(int count) {
        bankNotesCount += count;
    }

    public void receiveBankNotes(int count) throws Exception {
        if (count > bankNotesCount) {
            throw new Exception(String.format("%s has no such bank notes count: %d", this, count));
        }
        bankNotesCount -= count;
    }

    public String toString() {
        return String.format("Bundle(%s: %d)", bankNoteDenomination.name(), bankNotesCount);
    }
}
