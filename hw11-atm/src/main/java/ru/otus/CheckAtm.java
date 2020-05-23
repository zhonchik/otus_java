package ru.otus;


import java.util.ArrayList;

public class CheckAtm {
    public static void main(String... args) throws Exception {
        var atm = Atm.newBuilder()
                .addCell(new MoneyBundle(Denomination.D100, 5))
                .addCell(new MoneyBundle(Denomination.D500, 3))
                .addCell(new MoneyBundle(Denomination.D1000, 2))
                .build();
        System.out.println(atm);
        System.out.println(atm.getAmount());

        var money = new ArrayList<MoneyBundle>();
        money.add(new MoneyBundle(Denomination.D500, 55));
        money.add(new MoneyBundle(Denomination.D100, 333));
        System.out.println(atm.depositMoney(money));
        System.out.println(atm);
        System.out.println(atm.getAmount());

        System.out.println(atm.receiveMoney(4600));
        System.out.println(atm);
        System.out.println(atm.getAmount());

        try {
            atm.receiveMoney(1000000);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(atm);
        System.out.println(atm.getAmount());
    }
}
