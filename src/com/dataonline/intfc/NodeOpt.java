package com.dataonline.intfc;

public enum NodeOpt {
	O_NULL(0x00000000),
    O_ALL(0x00000001),
    O_ID(0x00000002),
    O_VALUE(0x00000004),
    O_NAME(0x00000008);
	
	private final int opt;

	NodeOpt(int opt) {
        this.opt = opt;
    }

    public int get() {
        return opt;
    }
}
