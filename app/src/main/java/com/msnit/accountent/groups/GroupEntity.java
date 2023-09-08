package com.msnit.accountent.groups;

import com.msnit.accountent.accounts.AccountEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GroupEntity implements Serializable {
    private String id;
    private String name;
    private String creatorName;
    private String creatorEmail;
    private Date creationDate;
    private Date lastChange;
    private int accountsNum;


    public GroupEntity(String id, String name, String creatorName, String creatorEmail, Date creationDate, int accountsNum) {
        this.id = id;
        this.name = name;
        this.creatorName = creatorName;
        this.creatorEmail = creatorEmail;
        this.creationDate = creationDate;
        this.accountsNum = accountsNum;
    }
}

