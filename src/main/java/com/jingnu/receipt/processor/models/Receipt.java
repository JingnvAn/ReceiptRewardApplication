package com.jingnu.receipt.processor.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "receipts")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;
    private String retailer;
    private String purchaseDate;
    private String purchaseTime;
    private String total;
    private Integer points;

    public Receipt() {}

    public Receipt(List<Item> items, String retailer, String purchaseDate, String purchaseTime, String total, Integer points) {
        this.items = items;
        this.retailer = retailer;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
        this.total = total;
        this.points = points;
    }
    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }
    public Integer getPoints() { return this.points; }

    public void setPoints(Integer points) { this.points = points; }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

