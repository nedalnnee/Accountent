package com.msnit.accountent.groups;

import com.msnit.accountent.accounts.AccountEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class GroupEntity implements Serializable {
    private String id;
    private String name;
    private String creator;
    private Date creationDate;
    private Date lastChange;
    private int accountsNum;

    public GroupEntity() {
    }

    public GroupEntity(String id, String name, String creator, Date creationDate,  int accountsNum) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.creationDate = creationDate;
        this.accountsNum = accountsNum;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public int getAccountsNum() {
        return accountsNum;
    }
}

