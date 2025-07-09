package com.zaidan.testng.model;

import java.util.Objects;

public class ContentItem {
    private final String name;
    private final ContentType type;

    public ContentItem(String name, ContentType type) {
        this.name = name;
        this.type = type;
    }
    public String getName() { return name; }
    public ContentType getType() { return type; }

    @Override
    public boolean equals(Object o) { /* implementasi equals/hashCode */
        if (this == o) return true;
        if (!(o instanceof ContentItem)) return false;
        ContentItem that = (ContentItem) o;
        return name.equals(that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "ContentItem{name='" + name + "', type='" + type + "'}";
    }

}
