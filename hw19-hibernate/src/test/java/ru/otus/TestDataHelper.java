package ru.otus;

import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;

import java.util.ArrayList;

public class TestDataHelper {

    public static final String TEST_USER_NAME_COLUMN = "name";
    public static final String TEST_USER_NAME = "Some name";
    public static final String TEST_USER_ANOTHER_NAME = "Another name";
    public static final String TEST_USER_YET_ANOTHER_NAME = "Yet another name";

    public static User getDefaultUser() {
        var phones = new ArrayList<PhoneDataSet>();
        phones.add(getDefaultPhone());
        return new User(0, TEST_USER_NAME, getDefaultAddress(), phones);
    }

    public static AddressDataSet getDefaultAddress() {
        return new AddressDataSet(0, "Some street");
    }

    public static PhoneDataSet getDefaultPhone() {
        return new PhoneDataSet(0, "Some phone");
    }
}
