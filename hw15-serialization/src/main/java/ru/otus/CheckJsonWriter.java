package ru.otus;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.HashSet;

class CheckJsonWriter {
    public static void main(String... args) throws Exception {
        JsonWriter writer = new JsonWriter();
        TestObject object = new TestObject();
        object.setStringField("value");
        object.setIntField(1);
        object.setDoubleField(2.5);
        object.setBooleanField(true);
        object.setEnumField(EnumTestObject.VALUE_1);
        object.setBooleanArrayField(new boolean[]{true, false, true});
        object.setDoubleArrayField(new double[]{1.0, 2.0, 3.0});
        object.setStringSetField(new HashSet<>() {{
            add("a");
            add("b");
        }});
        object.setIntBoolMapField(new HashMap<>() {{
            put(1, true);
            put(2, false);
        }});

        String myJson = writer.toJson(object);
        System.out.println(myJson);

        Gson gson = new Gson();

        TestObject obj2 = gson.fromJson(myJson, TestObject.class);

        System.out.println(gson.toJson(object));
        System.out.println(object.equals(obj2));

        // same but with cyclic reference
        object.setCyclicReference(object);

        myJson = writer.toJson(object);
        System.out.println(myJson);

        obj2 = gson.fromJson(myJson, TestObject.class);

        System.out.println(gson.toJson(object));
        System.out.println(object.equals(obj2));
    }
}
