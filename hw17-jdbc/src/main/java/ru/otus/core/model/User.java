package ru.otus.core.model;

import ru.otus.annotations.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class User {
    @Id private long id;
    private String name;
    private Integer age;

    public User(long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String toString() {
        return String.format("User{id=%d, name='%s', age=%d}", id, name, age);
    }
}
