package com.example.webapplication.User;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "Users")
@IdClass(UserCompositePK.class)
public class User implements Serializable { //Composite primary keys require Serializible
    @Id

    @SequenceGenerator(
            name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )
    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long userId;
    private String Username;
    private String password;
    private String name;
    private String subname;
    private String email;
    private long phone_number;
    @Id
    private String address;
    private String AFM;
    private String Attribute;
    @Id
    private String country;

    public User() {
    }

    public User(String username, String password, String name, String subname, String email, long phone_number, String address, String AFM, String attribute, String country) {
        Username = username;
        this.password = password;
        this.name = name;
        this.subname = subname;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.AFM = AFM;
        this.Attribute = attribute;
        this.country = country;
    }
}
