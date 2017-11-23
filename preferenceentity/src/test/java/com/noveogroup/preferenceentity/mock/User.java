package com.noveogroup.preferenceentity.mock;

@SuppressWarnings("WeakerAccess")
public class User {
    public static final int HASH_CONSTANT = 31;
    private final String name;
    private final int age;

    public User(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final User user = (User) o;
        return age == user.age && (name != null ? name.equals(user.name) : user.name == null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = HASH_CONSTANT * result + age;
        return result;
    }
}
