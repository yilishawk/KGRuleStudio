package com.github.kgstudio.model;

import java.io.Serializable;

/**
 * 变量池条目（自定义变量）
 */
public class VariableEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name = "";
    private String value = "";

    public VariableEntry() {}

    public VariableEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
