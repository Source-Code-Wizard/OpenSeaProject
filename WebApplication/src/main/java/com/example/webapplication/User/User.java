package com.example.webapplication.User;

import com.example.webapplication.Message.Message;
import com.example.webapplication.Role.Role;
import com.example.webapplication.Seller.Seller;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@EqualsAndHashCode
@Getter
@Setter
@ToString
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<Message> inbox = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<Message> outbox = new ArrayList<>();

    public List<Message> getInbox() {
        return inbox;
    }

    public Long getUserId() {
        return userId;
    }

    public void setInbox(List<Message> inbox) {
        this.inbox = inbox;
    }

    public List<Message> getOutbox() {
        return outbox;
    }

    public void setOutbox(List<Message> outbox) {
        this.outbox = outbox;
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Seller seller;

    public User() {
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
    @ManyToMany(fetch = FetchType.EAGER,targetEntity = Role.class)
    private Set<Role> roles = new HashSet<>();

    public User(String _username){
        username=_username;
    }

}
