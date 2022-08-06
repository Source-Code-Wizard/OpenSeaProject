package com.example.webapplication;

import jdk.jfr.Enabled;

import javax.persistence.*;

@Entity
@Table(name ="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String caterogoryName;

    public Category(Long categoryId, String caterogoryName) {
        this.categoryId = categoryId;
        this.caterogoryName = caterogoryName;
    }

    public Category() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCaterogoryName() {
        return caterogoryName;
    }

    public void setCaterogoryName(String caterogoryName) {
        this.caterogoryName = caterogoryName;
    }

    public Long getId() {
        return categoryId;
    }
}
