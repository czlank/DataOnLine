package com.dataonline.pojo;

import com.dataonline.intfc.NodeOpt;

public class Node {
	private int opt          = NodeOpt.O_NULL.get();
	
	private int id			 = -1;
	private int value        = 0;
	private String name 	 = new String();
	
	public Node() {
		
	}
	
	public void setOpt(int opt) {
        this.opt |= opt;
    }

    public void clearOpt(int opt) {
    	if (opt == NodeOpt.O_ALL.get()) {
    		this.opt = NodeOpt.O_NULL.get();
    		return;
    	}
    	
        this.opt &= ~(opt);
    }

    public int getOpt() {
        return opt;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
    
    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return name;
    }
}
