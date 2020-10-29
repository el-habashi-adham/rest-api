package com.aeh.api.models;

import javax.persistence.*;

@Entity
@Table(name = "assets")
public class Entry {
    @Id
    private Integer denomination;

    @Column
    private Integer amount;

    public Entry() {}

    public Entry(Integer denomination, Integer amount) {
        this.denomination = denomination;
        this.amount = amount;
    }

    public Integer getDenomination() {
        return denomination;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setDenomination(Integer denomination) {
        this.denomination = denomination;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
