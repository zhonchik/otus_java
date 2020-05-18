package ru.otus;

import ru.otus.annotations.Log;

public interface LoggedInterface {
    @Log
    void calculation_1(int param);

    void calculation_2(int param);

    @Log
    void calculation_3(int param);
}
