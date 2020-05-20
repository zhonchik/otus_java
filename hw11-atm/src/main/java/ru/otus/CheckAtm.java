package ru.otus;


public class CheckAtm {
    public static void main(String... args) throws Exception {
        var atm = Atm.newBuilder()
                .addCell(new MutableCurrencyBundle(100, 5))
                .addCell(new MutableCurrencyBundle(500, 3))
                .addCell(new MutableCurrencyBundle(1000, 2))
                .build();
        System.out.println(atm);
        System.out.println(atm.getAmount());

        atm.depositCurrency(Currency.newBuilder()
                .addBundle(new CurrencyBundle(500, 55))
                .addBundle(new CurrencyBundle(100, 333))
                .build()
        );
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
