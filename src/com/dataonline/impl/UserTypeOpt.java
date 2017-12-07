package com.fota.impl.base;

public enum UserTypeOpt {
    ADMINISTRATOR(0),
    COMMON(1);
    
    private final int type;

    UserTypeOpt(int type) {
        this.type = type;
    }

    public int get() {
        return type;
    }
}
