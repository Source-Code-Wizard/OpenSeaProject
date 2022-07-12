package com.example.webapplication.User;

import com.example.webapplication.Administrator.Administrator;
//import com.example.webapplication.Seller.Seller;

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

    //@ManyToOne
   /* @JoinColumn(
            name="Administrator_id",
            referencedColumnName = "adminId"
    )*/
   /* @ManyToOne(fetch = FetchType.LAZY)
    private Administrator administrator;*/

   /* @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn( referencedColumnName = "Seller_id")
    private Seller seller;*/

   /* @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)*/
   /* @OneToOne(mappedBy = "user")
    private Seller seller;*/

    public User() {
    }

    public User(String username, String password, String name, String subname, String email,
                long phone_number, String address, String AFM, String attribute, String country)
    {
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

    public User(String username){
        Username=username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
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

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
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

    public String getAttribute() {
        return Attribute;
    }

    public void setAttribute(String attribute) {
        Attribute = attribute;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
