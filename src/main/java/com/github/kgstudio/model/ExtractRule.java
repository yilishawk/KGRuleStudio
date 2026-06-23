package com.github.kgstudio.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 变量提取规则
 * 对应 KG.java step.vars 中的一个条目
 * 例如: key="url", rules=["json:data.url", "xpath://@href"]
 */
public class ExtractRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("key")
    private String key = "";

    @SerializedName("rules")
    private List<String> rules = new ArrayList<>();

    public ExtractRule() {}

    public ExtractRule(String key, String rule) {
        this.key = key;
        if (rule != null && !rule.isEmpty()) {
            // 支持 || 分隔的多规则
            String[] parts = rule.split("\\|\\|");
            for (String p : parts) {
                String trimmed = p.trim();
                if (!trimmed.isEmpty()) {
                    rules.add(trimmed);
                }
            }
        }
    }

    public ExtractRule(String key, List<String> rules) {
        this.key = key;
        this.rules = rules != null ? rules : new ArrayList<>();
    }

    // ── Getters & Setters ──
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public List<String> getRules() { return rules; }
    public void setRules(List<String> rules) { this.rules = rules != null ? rules : new ArrayList<>(); }

    /**
     * 添加一条兜底规则
     */
    public void addRule(String rule) {
        if (rule != null && !rule.isEmpty()) {
            rules.add(rule.trim());
        }
    }

    /**
     * 移除一条规则
     */
    public void removeRule(int index) {
        if (rules != null && index >= 0 && index < rules.size()) {
            rules.remove(index);
        }
    }

    /**
     * 合并为 KG 格式的字符串（用 || 连接）
     */
    public String toKgString() {
        if (rules == null || rules.isEmpty()) return "";
        return String.join("||", rules);
    }

    @Override
    public String toString() {
        return "ExtractRule{key=" + key + ", rules=" + rules + "}";
    }
}
