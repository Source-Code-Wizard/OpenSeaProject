package com.example.webapplication.User;

import com.example.webapplication.Role.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Users")
public class User  { //Composite primary keys require Serializible
    @Id
    @SequenceGenerator(
            name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )
    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long userId;
    @Column(name="Username")
    private String username;
    private String password;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String subname;
    private String email;
    @Column(nullable = true)
    private String phone_number;
    @Column(nullable = true)
    private String address;
    @Column(nullable = true)
    private String AFM;
    @Column(nullable = true)
    private String country;
    @Column(nullable = true)
    private boolean isRegistered;

    public User() {
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    /* Administrator constructor*/
    public User(String username, String password, String name, String subname, String email,
                String phone_number, String address, String AFM, String country)
    {
        this.username = username;
        this.password = password;
        this.name = name;
        this.subname = subname;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.AFM = AFM;
        this.country = country;
        this.isRegistered=false;
    }
    @ManyToMany(targetEntity = Role.class)
    private Set<Role> roles = new HashSet<>();

    public User(String _username){
        username=_username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        username = _username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAFM() {
        return AFM;
    }

    public void setAFM(String AFM) {
        this.AFM = AFM;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
