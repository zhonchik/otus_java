package ru.otus;


import java.util.ArrayList;

public class CheckAtm {
    public static void main(String... args) throws Exception {
        var atm = Atm.newBuilder()
                .addCell(new CurrencyBundle(Denomination.D100, 5))
                .addCell(new CurrencyBundle(Denomination.D500, 3))
                .addCell(new CurrencyBundle(Denomination.D1000, 2))
                .build();
        System.out.println(atm);
        System.out.println(atm.getAmount());

        var money = new ArrayList<CurrencyBundle>();
        money.add(new CurrencyBundle(Denomination.D500, 55));
        money.add(new CurrencyBundle(Denomination.D100, 333));
        atm.depositCurrency(money);
        System.out.println(atm);
        System.out.println(atm.getAmount());

        System.out.println(atm.receiveCurrency(4600));
        System.out.println(atm);
        System.out.println(atm.getAmount());

        try {
            atm.receiveCurrency(1000000);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(atm);
        System.out.println(atm.getAmount());
    }
}
