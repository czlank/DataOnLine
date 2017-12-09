package com.dataonline.pojo;

import java.util.Date;

public class Value {
    private Date date;
    private String value = new String();

    public Value() {
        
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
