package com.example.SOARSpringBoot.entity;

import javax.persistence.*;

@Entity
@Table(name = "card_items")
public class CardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="inventory_id")
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    private int quantity;

    @ManyToOne
    @JoinColumn(name="card_id")
    private RequestApprove requestApprove;


    public RequestApprove getRequestApprove() {
        return requestApprove;
    }

    public void setRequestApprove(RequestApprove requestApprove) {
        this.requestApprove = requestApprove;
    }



    public CardItem(Inventory inventory, User user, int quantity,RequestApprove requestApprove) {
        this.inventory = inventory;
        this.user = user;
        this.quantity = quantity;
        this.requestApprove=requestApprove;
    }

    public CardItem() {
    }

    public Long getId() {
        return id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
