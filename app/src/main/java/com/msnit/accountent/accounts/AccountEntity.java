package com.msnit.accountent.accounts;

import java.io.Serializable;
import java.util.Date;

public class AccountEntity implements Serializable {
    private String id;
    private String name;
    private Date creationDate;
    private Date lastChange;
    private String currency;
    private int accountsCash;


    public AccountEntity() {
        // Default constructor required for Firestore
    }

    public AccountEntity(String id, String name, Date creationDate, Date lastChange, String currency, int accountsCash) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.lastChange = lastChange;
        this.currency = currency;
        this.accountsCash = accountsCash;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAccountsCash() {
        return accountsCash;
    }

    public void setAccountsCash(int accountsCash) {
        this.accountsCash = accountsCash;
    }
}

