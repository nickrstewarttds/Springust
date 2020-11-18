package com.qa.springust.global;

public enum MUSICIAN {
    GUITARIST("John Darnielle", 6, "Guitarist"), BASSIST("Peter Hughes", 4, "Bassist"),
    SAXOPHONIST("Matt Douglas", 0, "Saxophonist"), DRUMMER("Jon Wurster", 0, "Drummer");

    private final String name;
    private final Integer strings;
    private final String type;

    MUSICIAN(String name, Integer strings, String type) {
        this.name = name;
        this.strings = strings;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Integer getStrings() {
        return strings;
    }

    public String getType() {
        return type.toLowerCase();
    }

}
