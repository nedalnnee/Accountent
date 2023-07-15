package com.msnit.accountent.transactions;

import java.util.Date;

public class TransactionEntity {
    private String id;
    private String name;
    private Date creationDate;
    private int amount;
    private String note;
    private String type;


    public TransactionEntity(String id, String name, Date creationDate, int amount, String note,String type) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.amount = amount;
        this.note = note;
        this.type = type;
    }
    public boolean isWithdrawal() {
        return type.equalsIgnoreCase("withdrawal");
    }

    public String getNote() {
        return note;
    }

    public TransactionEntity() {
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

    public int getAmount() {
        return amount;
    }
}



