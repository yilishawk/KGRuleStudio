package com.github.kgstudio.model;

import java.io.Serializable;

/**
 * 请求头键值对
 */
public class HeaderEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name = "";
    private String value = "";

    public HeaderEntry() {}

    public HeaderEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}
