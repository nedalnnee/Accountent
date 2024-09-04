package com.msnit.accountent.groups;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class GroupEntity implements Serializable {
    @ColumnInfo(name = "id")
    private String id;
    private String name;
    private String creatorName;
    private String creatorEmail;
    private Date creationDate;
    private Date lastChange;
    private int accountsNum;

    public GroupEntity(String id, String name, String creatorName, String creatorEmail, int accountsNum) {
        this.id = id;
        this.name = name;
        this.creatorName = creatorName;
        this.creatorEmail = creatorEmail;
        this.creationDate = new Date();
        this.accountsNum = accountsNum;
    }
}

