package com.qa.springust.global;

public enum BAND {
    TMG("The Mountain Goats"), TEG("The Extra Glenns"), TEL("The Extra Lens");

    private final String name;

    BAND(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
