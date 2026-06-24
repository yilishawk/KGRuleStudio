package com.github.kgstudio.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * 筛选选项
 */
public class FilterOption implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("n")
    private String n = "";

    @SerializedName("v")
    private String v = "";

    public FilterOption() {}

    public FilterOption(String n, String v) {
        this.n = n;
        this.v = v;
    }

    public String getN() { return n; }
    public void setN(String n) { this.n = n; }

    public String getV() { return v; }
    public void setV(String v) { this.v = v; }
}
