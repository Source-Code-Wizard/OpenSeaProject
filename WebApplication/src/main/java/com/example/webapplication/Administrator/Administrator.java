package com.example.webapplication.Administrator;


import com.example.webapplication.User.User;
import org.hibernate.mapping.Set;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "Administrator")
public class Administrator {
    @Id
    @SequenceGenerator(
            name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )
    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long adminId;

    @Column(
            name="AdminName",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String adminName;
    @Column(
            columnDefinition = "TEXT",
            nullable = false
    )
    private String password;

    // one to many unidirectional mapping
    // default fetch type for OneToMany: LAZY
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // we create to user-table a new column named:Administrator_id which refers to AdminTable.adminId column
    @JoinColumn(name = "Administrator_id", referencedColumnName = "adminId")
    private List<User> usersList = new ArrayList<>();

    public Administrator() {
    }

    public Administrator(String adminName, String password) {
        this.adminName = adminName;
        this.password = password;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
