package ru.yandex.practicum.gym;

import java.util.Objects;

public class Group {
    //название группы
    private final String title;
    //тип (взрослая или детская)
    private final Age age;
    //длительность (в минутах)
    private final int duration;

    public Group(String title, Age age, int duration) {
        this.title = title;
        this.age = age;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public Age getAge() {
        return age;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Group other = (Group) o;
        return title.equals(other.title) &&
                age.equals(other.age) &&
                duration == other.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, age, duration);
    }
}
