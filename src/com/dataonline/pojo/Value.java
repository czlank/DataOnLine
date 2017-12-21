package com.dataonline.pojo;

import java.util.Date;

import com.dataonline.intfc.ValueOpt;

public class Value {
	private int opt          = ValueOpt.O_NULL.get();
	
    private Date date;
    private String value = new String();

    public Value() {
        
    }
    
    public void setOpt(int opt) {
        this.opt |= opt;
    }

    public void clearOpt(int opt) {
    	if (opt == ValueOpt.O_ALL.get()) {
    		this.opt = ValueOpt.O_NULL.get();
    		return;
    	}
    	
        this.opt &= ~(opt);
    }

    public int getOpt() {
        return opt;
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
