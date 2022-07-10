package com.example.webapplication.User;

import java.io.Serializable;
import java.util.Objects;

/* composite PKs must have their own class implementation!*/
public class UserCompositePK implements Serializable {
    private String address;
    private String country;
    private Long userId;

    public UserCompositePK() {
    }

    public UserCompositePK(String address, String country, Long userId) {
        this.address = address;
        this.country = country;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCompositePK userId1 = (UserCompositePK) o;
        return address.equals(userId1.address) && country.equals(userId1.country) && userId.equals(userId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, country, userId);
    }
}

