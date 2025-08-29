package com.example.SOARSpringBoot.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message= "Name cannot be empty")
    @Column(unique = true,nullable = false)
    private String name;

    @NotEmpty(message="Type cannot be empty")
    @Column(nullable = false)
    private String type;

    @NotEmpty(message="Value can't be null")
    @Min(value = 1,message="Value must be greater than 0")
    @Max(value = 999999,message = "value should not be more than 6 digits")
    @Column(nullable = false)
    private Long cost;

    @NotEmpty(message="Value can't be null")
    @Min(value = 1,message="Value must be greater than 0")
    @Column(nullable = false)
    private Integer count;

    public Inventory(String name, String type, Long cost, Integer count) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public Inventory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
