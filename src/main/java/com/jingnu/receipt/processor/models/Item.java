package com.jingnu.receipt.processor.models;

import jakarta.persistence.*;

@Entity
@Table(name= "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String receipt_id;
    private String shortDescription;
    private String price;

    public Item() {}

    public Item(String receipt_id, String shortDescription, String price) {
        this.receipt_id = receipt_id;
        this.shortDescription = shortDescription;
        this.price = price;
    }

    public String getReceipt_id() { return receipt_id; }

    public void setReceipt_id(String receipt_id) { this.receipt_id = receipt_id; }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}