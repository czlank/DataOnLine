package com.dataonline.pojo;

import com.dataonline.intfc.TypeOpt;

public class Type {
	private int opt          = TypeOpt.O_NULL.get();

    private int id      = -1;
    private int type    = 0;
    private double min	= 0.0;
    private double max  = 0.0;
    private String name = new String();
    
    public Type() {
        
    }

    public void setOpt(int opt) {
        this.opt |= opt;
    }

    public void clearOpt(int opt) {
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
    
	public void setMin(double min) {
        this.min = min;
    }

    public double getMin() {
        return min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMax() {
        return max;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return name;
    }
}
