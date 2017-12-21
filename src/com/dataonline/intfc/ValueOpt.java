package com.dataonline.intfc;

public enum ValueOpt {
	O_NULL(0x00000000),
	O_ALL(0x00000001),
    O_LASTREC(0x00000002),
    O_ONEDAYRECS(0x00000004);
	
    private final int opt;

    ValueOpt(int opt) {
        this.opt = opt;
    }

    public int get() {
        return opt;
    }
}
