package com.example.pizza.enums;

public enum Crust {
    TRADITIONAL("Truyền thống"),
    THICK("Dày"),
    CHEESE("Phô mai");

    private final String displayName;

    Crust(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
