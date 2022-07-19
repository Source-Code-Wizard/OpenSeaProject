package com.example.webapplication.Role;

import com.example.webapplication.User.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Role {

    @Id
    @SequenceGenerator(
            name= "role_sequence", sequenceName = "role_sequence",allocationSize = 1
    )
    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "role_sequence")
    private long id;

  /*  @ManyToMany(targetEntity = User.class)
    private Set<User> users = new HashSet<>();*/
    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Column
    private String name;

    public String getName() {
        return name;
    }

}
