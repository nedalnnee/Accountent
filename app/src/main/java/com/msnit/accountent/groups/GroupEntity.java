package com.msnit.accountent.groups;

import java.sql.Date;

public class GroupEntity {

    String title;
    Date lastChange;
    int accountsNum;

    public GroupEntity(String title, Date lastChange, int accountsNum) {
        this.title = title;
        this.lastChange = lastChange;
        this.accountsNum = accountsNum;
    }
}
