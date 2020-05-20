package ru.otus;

public class MutableCurrencyBundle extends CurrencyBundle {

    public MutableCurrencyBundle(int bankNoteDenomination, int bankNotesCount) {
        super(bankNoteDenomination, bankNotesCount);
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
}
