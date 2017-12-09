package com.dataonline.pojo;

import com.dataonline.intfc.UserOpt;

public class User {
    private int opt          = UserOpt.O_NULL.get();

    private int id           = -1;
    private int type         = 0;
    private String name      = new String();
    private String password  = new String();
    private String nodes	 = new String();
    
    public User() {
        
    }

    public void setOpt(int opt) {
        this.opt |= opt;
    }

    public void clearOpt(int opt) {
    	if (opt == UserOpt.O_ALL.get()) {
    		this.opt = UserOpt.O_NULL.get();
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
    
    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    
    public void setNodes(String nodes) {
    	this.nodes = nodes;
    }
    
    public String getNodes() {
    	return nodes;
    }
}
