package com.github.kgstudio.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 筛选配置组
 * 对应 KG JSON 中 filters 的一个 key 对应的值
 * 结构: { "key": [{"n": "全部", "v": ""}, {"n": "喜剧", "v": "comedy"}] }
 */
public class FilterGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("key")
    private String key = "";

    @SerializedName("one")
    private String one = "";

    private List<FilterOption> value = new ArrayList<>();

    public FilterGroup() {}

    public FilterGroup(String key, String one) {
        this.key = key;
        this.one = one;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getOne() { return one; }
    public void setOne(String one) { this.one = one; }

    public List<FilterOption> getValue() { return value; }
    public void setValue(List<FilterOption> value) { this.value = value; }

    public void addOption(String name, String val) {
        value.add(new FilterOption(name, val));
    }

    public void removeOption(int index) {
        if (value != null && index >= 0 && index < value.size()) {
            value.remove(index);
        }
    }
}
