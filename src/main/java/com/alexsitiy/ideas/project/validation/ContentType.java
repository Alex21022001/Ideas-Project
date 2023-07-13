package com.alexsitiy.ideas.project.validation;

public enum ContentType {
    IMAGE_PNG("image/png");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getFormattedValue() {
        return "prefix/" + value;  // Add your desired prefix here
    }
}