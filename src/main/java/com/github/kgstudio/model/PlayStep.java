package com.github.kgstudio.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单个播放解析步骤
 * 对应 KG.java play.steps[i]
 */
public class PlayStep implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("url")
    private String url = "";

    @SerializedName("method")
    private String method = "get";

    @SerializedName("body")
    private String body = "";

    @SerializedName("headers")
    private List<HeaderEntry> headers = new ArrayList<>();

    @SerializedName("vars")
    private List<ExtractRule> vars = new ArrayList<>();

    public PlayStep() {}

    public PlayStep(String url) {
        this.url = url;
    }

    // ── Getters & Setters ──
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method != null ? method : "get"; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body != null ? body : ""; }

    public List<HeaderEntry> getHeaders() { return headers; }
    public void setHeaders(List<HeaderEntry> headers) { this.headers = headers != null ? headers : new ArrayList<>(); }

    public List<ExtractRule> getVars() { return vars; }
    public void setVars(List<ExtractRule> vars) { this.vars = vars != null ? vars : new ArrayList<>(); }

    /**
     * 添加请求头
     */
    public void addHeader(String name, String value) {
        if (headers == null) headers = new ArrayList<>();
        // 先尝试更新已有 key
        for (HeaderEntry h : headers) {
            if (h.getName().equalsIgnoreCase(name)) {
                h.setValue(value);
                return;
            }
        }
        headers.add(new HeaderEntry(name, value));
    }

    /**
     * 移除请求头
     */
    public void removeHeader(String name) {
        if (headers != null) {
            headers.removeIf(h -> h.getName().equalsIgnoreCase(name));
        }
    }

    /**
     * 添加提取规则
     */
    public void addVar(String key, String rule) {
        if (vars == null) vars = new ArrayList<>();
        vars.add(new ExtractRule(key, rule));
    }

    /**
     * 移除提取规则
     */
    public void removeVar(int index) {
        if (vars != null && index >= 0 && index < vars.size()) {
            vars.remove(index);
        }
    }

    /**
     * 获取所有可用变量名（用于变量池展示）
     */
    public List<String> getVarKeys() {
        List<String> keys = new ArrayList<>();
        if (vars != null) {
            for (ExtractRule v : vars) {
                keys.add(v.getKey());
            }
        }
        return keys;
    }

    @Override
    public String toString() {
        return "PlayStep{url=" + url + ", method=" + method + ", vars=" + (vars != null ? vars.size() : 0) + "}";
    }
}
