package ru.yandex.practicum.gym;

import java.util.Objects;

public class Coach {

    //фамилия
    private final String surname;
    //имя
    private final String name;
    //отчество
    private final String middleName;

    public Coach(String surname, String name, String middleName) {
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coach other = (Coach) o;
        return surname.equals(other.surname) &&
                name.equals(other.name) &&
                middleName.equals(other.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, middleName);
    }
}
