package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AtmDepartment {
    private final List<Atm> atmList = new ArrayList<>();

    public void addAtm(Atm atm) {
        atmList.add(atm);
    }

    public int getAmount() {
        return atmList.stream().map(Atm::getAmount).reduce(Integer::sum).orElse(0);
    }

    public void restore() {
        for (var atm : atmList) {
            atm.restore();
        }
    }

    public String toString() {
        return String.format(
                "AtmDepartment(%n\t%s%n)",
                atmList.stream().map(Atm::toString).collect(Collectors.joining("\n\t"))
        );
    }
}
