package com.jingnu.receipt.processor.models;

import jakarta.persistence.*;

// @Embeddable is useful when the object being embedded is not an entity in itself and doesn't require a unique identifier. For example, a Person entity may have an embedded Address object that doesn't require its own identifier.
// The embedded object is tightly coupled to the owning entity: If the embedded object has no meaning outside of the context of the owning entity, then @Embeddable can be a good choice. For example, a Customer entity may have an embedded BillingInfo object that is only relevant to that specific customer.
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