package com.dataonline.intfc;

public enum UserOpt {
    O_NULL(0x00000000),
    O_ALL(0x00000001),
    O_ID(0x00000002),
    O_TYPE(0x00000004),
    O_NAME(0x00000008),
    O_PASSWORD(0x00000010);

    private final int opt;

    UserOpt(int opt) {
        this.opt = opt;
    }

    public int get() {
        return opt;
    }
}
