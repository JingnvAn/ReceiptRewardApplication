package com.jingnu.receipt.processor.models;

import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
public class Item {
    private String shortDescription;
    private String price;

    public Item() {}

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
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if (!(obj instanceof Item other))
            return false;

        return this.price.equals(other.getPrice()) && this.getShortDescription().equals(other.getShortDescription());
    }
}