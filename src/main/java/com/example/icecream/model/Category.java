package com.example.icecream.model;

import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Геттер для id
    public Long getId() {
        return id;
    }

    // Сеттер для id
    public void setId(Long id) {
        this.id = id;
    }

    // Геттер для name
    public String getName() {
        return name;
    }

    // Сеттер для name
    public void setName(String name) {
        this.name = name;
    }
}
