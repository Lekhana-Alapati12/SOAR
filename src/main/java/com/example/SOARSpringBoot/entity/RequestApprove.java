package com.example.SOARSpringBoot.entity;

import com.example.SOARSpringBoot.entity.CardItem;
import com.example.SOARSpringBoot.entity.User;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "request_approve")
public class RequestApprove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "requestApprove")
    private List<CardItem> cardItems;

    @Column(name="remarks_dev",length = 255)
    private String remarks_dev;

    @Column(name="remarks_manager",length = 255)
    private String remarks_manager;

    @Column(name="status",nullable = false)
    private String status;

    @CreatedDate
    @Column(name="date")
    private Date date;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private Long totalCost;
    private Integer totalItems;

    public RequestApprove(List<CardItem> cardItems, String remarks_dev, String remarks_manager, String status, Date date, User user, Integer totalItems,Long totalCost) {
        this.cardItems = cardItems;
        this.remarks_dev = remarks_dev;
        this.remarks_manager = remarks_manager;
        this.status = status;
        this.date = date;
        this.user=user;
        this.totalItems = totalItems;
        this.totalCost=totalCost;
    }

    public Long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Long totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestApprove() {
    }

    public Long getId() {
        return id;
    }

    public List<CardItem> getCardItems() {
        return cardItems;
    }

    public void setCardItems(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    public String getRemarks_dev() {
        return remarks_dev;
    }

    public void setRemarks_dev(String remarks_dev) {
        this.remarks_dev = remarks_dev;
    }

    public String getRemarks_manager() {
        return remarks_manager;
    }

    public void setRemarks_manager(String remarks_manager) {
        this.remarks_manager = remarks_manager;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
