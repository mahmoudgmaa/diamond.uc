package com.AZEM.diamonduc2.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "requests_table")
public class Requests {


    @PrimaryKey(autoGenerate = true)
    private int id;


    private String requestName;
    private String requestType;
    private String requestCondition;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Requests() {

    }

    public Requests(String requestName, String requestType, String requestCondition) {
        this.requestName = requestName;
        this.requestType = requestType;
        this.requestCondition = requestCondition;
    }


    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestCondition() {
        return requestCondition;
    }

    public void setRequestCondition(String requestCondition) {
        this.requestCondition = requestCondition;
    }
}
