package ru.otus;


import java.util.ArrayList;

public class CheckAtmDepartment {

    public static void main(String... args) throws Exception {

        var cells = new ArrayList<MoneyBundle>();
        cells.add(new MoneyBundle(Denomination.D100, 5));
        cells.add(new MoneyBundle(Denomination.D500, 3));
        cells.add(new MoneyBundle(Denomination.D1000, 2));

        var atmFabric = new AtmFabric(cells);

        System.out.println("\nCreating department");
        var atmDepartment = new AtmDepartment();
        System.out.println(String.format("Department total amount: %d", atmDepartment.getAmount()));

        var atm0 = atmFabric.createAtm();
        atmDepartment.addAtm(atm0);
        System.out.println(atmDepartment);
        System.out.println(String.format("Department total amount: %d", atmDepartment.getAmount()));

        var atm1 = atmFabric.createAtm();
        atmDepartment.addAtm(atm1);
        System.out.println(atmDepartment);
        System.out.println(String.format("Department total amount: %d", atmDepartment.getAmount()));

        var atm2 = atmFabric.createAtm();
        atmDepartment.addAtm(atm2);
        System.out.println(atmDepartment);
        System.out.println(String.format("Department total amount: %d", atmDepartment.getAmount()));

        System.out.println("\nDepositing money");
        var money = new ArrayList<MoneyBundle>();
        money.add(new MoneyBundle(Denomination.D500, 55));
        money.add(new MoneyBundle(Denomination.D100, 333));

        System.out.println(atm0.depositMoney(money));
        System.out.println(atmDepartment);
        System.out.println(String.format("Department total amount: %d", atmDepartment.getAmount()));

        System.out.println("\nReceiving money");
        System.out.println(atm2.receiveMoney(1200));
        System.out.println(atmDepartment);
        System.out.println(String.format("Department total amount: %d", atmDepartment.getAmount()));

        System.out.println("\nRestoring department");
        atmDepartment.restore();
        System.out.println(atmDepartment);
        System.out.println(String.format("Department total amount: %d", atmDepartment.getAmount()));
    }
}
