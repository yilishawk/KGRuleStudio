package com.github.kgstudio.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * 分类条目
 * 对应 KG JSON 中 classes 数组的元素
 */
public class SiteClass implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("type_name")
    private String typeName = "";

    @SerializedName("type_id")
    private String typeId = "";

    public SiteClass() {}

    public SiteClass(String typeName, String typeId) {
        this.typeName = typeName;
        this.typeId = typeId;
    }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getTypeId() { return typeId; }
    public void setTypeId(String typeId) { this.typeId = typeId; }

    @Override
    public String toString() {
        return typeName + " (" + typeId + ")";
    }
}
