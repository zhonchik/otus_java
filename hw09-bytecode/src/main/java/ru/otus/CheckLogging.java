package ru.otus;


public class CheckLogging {
    public static void main(String... args) throws Exception {
        LoggedInterface logged = LoggedProxy.createLogged();
        logged.calculation_1(5);
        logged.calculation_2(7);
        logged.calculation_3(9);
    }

}
