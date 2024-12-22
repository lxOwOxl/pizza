package com.example.pizza.enums;

public enum Crust {
    CRISPY("Giòn xốp"),
    TRADITIONAL("Truyền thống"),
    THICK("Dày"),
    CHEESE("Phô mai"),
    SAUSAGE("Xúc xích");

    private final String displayName;

    Crust(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
