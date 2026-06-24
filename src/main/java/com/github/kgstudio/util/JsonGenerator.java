package com.github.kgstudio.util;

import com.github.kgstudio.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * SiteConfig ? JSON 双向转换
 * 输出格式完全兼容 KG.java 的 rule JSONObject 读取方式
 */
public class JsonGenerator {

    private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson GSON_COMPACT = new Gson();

    /**
     * SiteConfig → 格式化 JSON 字符串
     */
    public static String toJson(SiteConfig config) {
        if (config == null) return "{}";
        return GSON_PRETTY.toJson(config);
    }

    /**
     * SiteConfig → 紧凑 JSON 字符串（用于 TVBox ext 参数）
     */
    public static String toCompactJson(SiteConfig config) {
        if (config == null) return "{}";
        return GSON_COMPACT.toJson(config);
    }

    /**
     * JSON 字符串 → SiteConfig
     */
    public static SiteConfig fromJson(String json) {
        if (json == null || json.trim().isEmpty() || !json.trim().startsWith("{")) {
            return null;
        }
        try {
            return GSON_PRETTY.fromJson(json, SiteConfig.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 构建一个空的 KG-compatible ext JSON 示例
     */
    public static String buildEmptyExample() {
        SiteConfig config = new SiteConfig("示例站点");
        config.setSiteUrl("https://example.com");
        config.setHost("https://example.com");
        config.setUa("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

        List<SiteClass> classes = new ArrayList<>();
        classes.add(new SiteClass("动漫", "dongman"));
        classes.add(new SiteClass("电影", "dianying"));
        config.setClasses(classes);

        config.setCateMethod("get");
        config.setCateUrl("/index.php/vod/show/id/{tid}/page/{pg}.html");
        config.setCateItem(".myui-vodlist__item a");
        config.setCateId("a@href");
        config.setCateName("a@title");
        config.setCatePic("a@data-original");
        config.setCateRemarks("span@text");
        config.setDetailUrl("/detail/{id}.html");

        config.setSearchMethod("get");
        config.setSearchUrl("/index.php/vod/search/wd/{wd}.html");

        config.setDetailMethod("get");
        config.setDtName("h1@text");
        config.setDtPic(".myui-voddetail__img@data-original");
        config.setDtRemarks("p.text-muted@text");
        config.setDtActor("p.data@text\\s*：\\s*(.*)");
        config.setDtContent("p.desc@text\\s*简介：\\s*(.*)");
        config.setDtFrom("li[h3]");
        config.setDtList("ul.myui-content__list");

        return toJson(config);
    }

    /**
     * 构建 Steps 示例
     */
    public static String buildStepsExample() {
        SiteConfig config = new SiteConfig("示例站点");
        config.setSiteUrl("https://example.com");

        // Step 1: GET 请求提取 url
        PlayStep step1 = new PlayStep("https://api.example.com/parse?url={play_id}");
        step1.setMethod("get");
        step1.addHeader("Referer", "https://example.com/");
        step1.addVar("url", "json:data.url||xpath://@href");
        config.getPlaySteps().add(step1);

        // Step 2: POST 请求二次解析
        PlayStep step2 = new PlayStep("{url}");
        step2.setMethod("post");
        step2.setBody("token=abc&flag=qd");
        step2.addHeader("Content-Type", "application/x-www-form-urlencoded");
        step2.addVar("final_url", "json:data.url");
        config.getPlaySteps().add(step2);

        return toJson(config);
    }
}
