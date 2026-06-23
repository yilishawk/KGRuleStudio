package com.github.kgstudio.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点配置模型 — 对应 KG Spider 的全部 ext JSON 字段
 */
public class SiteConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    // ── 站点基础信息 ──
    @SerializedName("site_name")
    private String siteName = "";
    
    @SerializedName("index_url")
    private String indexUrl = "";
    
    @SerializedName("index_rule")
    private String indexRule = "";
    
    @SerializedName("site_url")
    private String siteUrl = "";
    
    @SerializedName("host")
    private String host = "";
    
    private String ua = "Mozilla/5.0";
    
    private Map<String, String> headers = new HashMap<>();

    // ── 分类/列表 ──
    private List<SiteClass> classes = new ArrayList<>();
    private Map<String, FilterGroup> filters = new HashMap<>();
    
    @SerializedName("cate_method")
    private String cateMethod = "get";
    
    @SerializedName("cate_url")
    private String cateUrl = "";
    
    @SerializedName("cate_page_1")
    private String catePage1 = "";
    
    @SerializedName("cate_body")
    private String cateBody = "";
    
    @SerializedName("cate_item")
    private String cateItem = "";
    
    @SerializedName("cate_id")
    private String cateId = "";
    
    @SerializedName("cate_name")
    private String cateName = "";
    
    @SerializedName("cate_pic")
    private String catePic = "";
    
    @SerializedName("cate_remarks")
    private String cateRemarks = "";
    
    @SerializedName("cate_list_path")
    private String cateListPath = "list";
    
    @SerializedName("detail_url")
    private String detailUrl = "";

    // ── 搜索 ──
    @SerializedName("search_method")
    private String searchMethod = "get";
    
    @SerializedName("search_url")
    private String searchUrl = "";
    
    @SerializedName("search_body")
    private String searchBody = "";

    // ── 详情页 ──
    @SerializedName("detail_method")
    private String detailMethod = "get";
    
    @SerializedName("detail_body")
    private String detailBody = "";
    
    @SerializedName("dt_name")
    private String dtName = "";
    
    @SerializedName("dt_pic")
    private String dtPic = "";
    
    @SerializedName("dt_remarks")
    private String dtRemarks = "";
    
    @SerializedName("dt_actor")
    private String dtActor = "";
    
    @SerializedName("dt_director")
    private String dtDirector = "";
    
    @SerializedName("dt_content")
    private String dtContent = "";
    
    @SerializedName("dt_from")
    private String dtFrom = "";
    
    @SerializedName("dt_list")
    private String dtList = "";

    // ── 播放解析 ──
    @SerializedName("play_steps")
    private List<PlayStep> playSteps = new ArrayList<>();
    
    @SerializedName("play_jx")
    private String playJx = "";
    
    @SerializedName("play_jx_list")
    private String playJxList = "";
    
    @SerializedName("play_jx_title")
    private String playJxTitle = "";
    
    @SerializedName("play_jx_parse")
    private String playJxParse = "";
    
    @SerializedName("play_headers")
    private Map<String, String> playHeaders = new HashMap<>();

    // ── 图片反爬 ──
    @SerializedName("pic_jump")
    private boolean picJump = false;
    
    @SerializedName("pic_prefix")
    private String picPrefix = "";
    
    @SerializedName("pic_suffix")
    private String picSuffix = "";
    
    @SerializedName("pic_extract")
    private String picExtract = "";
    
    @SerializedName("pic_host")
    private String picHost = "";

    // ── 功能开关 ──
    @SerializedName("cdndefend")
    private boolean cdndefend = false;
    
    @SerializedName("danmaku")
    private boolean danmaku = false;

    // ── Constructors ──
    public SiteConfig() {}

    public SiteConfig(String name) {
        this.siteName = name;
    }

    // ── Getters & Setters ──
    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public String getIndexUrl() { return indexUrl; }
    public void setIndexUrl(String indexUrl) { this.indexUrl = indexUrl; }

    public String getIndexRule() { return indexRule; }
    public void setIndexRule(String indexRule) { this.indexRule = indexRule; }

    public String getSiteUrl() { return siteUrl; }
    public void setSiteUrl(String siteUrl) { this.siteUrl = siteUrl; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public String getUa() { return ua; }
    public void setUa(String ua) { this.ua = ua; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public List<SiteClass> getClasses() { return classes; }
    public void setClasses(List<SiteClass> classes) { this.classes = classes; }

    public Map<String, FilterGroup> getFilters() { return filters; }
    public void setFilters(Map<String, FilterGroup> filters) { this.filters = filters; }

    public String getCateMethod() { return cateMethod; }
    public void setCateMethod(String cateMethod) { this.cateMethod = cateMethod; }

    public String getCateUrl() { return cateUrl; }
    public void setCateUrl(String cateUrl) { this.cateUrl = cateUrl; }

    public String getCatePage1() { return catePage1; }
    public void setCatePage1(String catePage1) { this.catePage1 = catePage1; }

    public String getCateBody() { return cateBody; }
    public void setCateBody(String cateBody) { this.cateBody = cateBody; }

    public String getCateItem() { return cateItem; }
    public void setCateItem(String cateItem) { this.cateItem = cateItem; }

    public String getCateId() { return cateId; }
    public void setCateId(String cateId) { this.cateId = cateId; }

    public String getCateName() { return cateName; }
    public void setCateName(String cateName) { this.cateName = cateName; }

    public String getCatePic() { return catePic; }
    public void setCatePic(String catePic) { this.catePic = catePic; }

    public String getCateRemarks() { return cateRemarks; }
    public void setCateRemarks(String cateRemarks) { this.cateRemarks = cateRemarks; }

    public String getCateListPath() { return cateListPath; }
    public void setCateListPath(String cateListPath) { this.cateListPath = cateListPath; }

    public String getDetailUrl() { return detailUrl; }
    public void setDetailUrl(String detailUrl) { this.detailUrl = detailUrl; }

    public String getSearchMethod() { return searchMethod; }
    public void setSearchMethod(String searchMethod) { this.searchMethod = searchMethod; }

    public String getSearchUrl() { return searchUrl; }
    public void setSearchUrl(String searchUrl) { this.searchUrl = searchUrl; }

    public String getSearchBody() { return searchBody; }
    public void setSearchBody(String searchBody) { this.searchBody = searchBody; }

    public String getDetailMethod() { return detailMethod; }
    public void setDetailMethod(String detailMethod) { this.detailMethod = detailMethod; }

    public String getDetailBody() { return detailBody; }
    public void setDetailBody(String detailBody) { this.detailBody = detailBody; }

    public String getDtName() { return dtName; }
    public void setDtName(String dtName) { this.dtName = dtName; }

    public String getDtPic() { return dtPic; }
    public void setDtPic(String dtPic) { this.dtPic = dtPic; }

    public String getDtRemarks() { return dtRemarks; }
    public void setDtRemarks(String dtRemarks) { this.dtRemarks = dtRemarks; }

    public String getDtActor() { return dtActor; }
    public void setDtActor(String dtActor) { this.dtActor = dtActor; }

    public String getDtDirector() { return dtDirector; }
    public void setDtDirector(String dtDirector) { this.dtDirector = dtDirector; }

    public String getDtContent() { return dtContent; }
    public void setDtContent(String dtContent) { this.dtContent = dtContent; }

    public String getDtFrom() { return dtFrom; }
    public void setDtFrom(String dtFrom) { this.dtFrom = dtFrom; }

    public String getDtList() { return dtList; }
    public void setDtList(String dtList) { this.dtList = dtList; }

    public List<PlayStep> getPlaySteps() { return playSteps; }
    public void setPlaySteps(List<PlayStep> playSteps) { this.playSteps = playSteps; }

    public String getPlayJx() { return playJx; }
    public void setPlayJx(String playJx) { this.playJx = playJx; }

    public String getPlayJxList() { return playJxList; }
    public void setPlayJxList(String playJxList) { this.playJxList = playJxList; }

    public String getPlayJxTitle() { return playJxTitle; }
    public void setPlayJxTitle(String playJxTitle) { this.playJxTitle = playJxTitle; }

    public String getPlayJxParse() { return playJxParse; }
    public void setPlayJxParse(String playJxParse) { this.playJxParse = playJxParse; }

    public Map<String, String> getPlayHeaders() { return playHeaders; }
    public void setPlayHeaders(Map<String, String> playHeaders) { this.playHeaders = playHeaders; }

    public boolean isPicJump() { return picJump; }
    public void setPicJump(boolean picJump) { this.picJump = picJump; }

    public String getPicPrefix() { return picPrefix; }
    public void setPicPrefix(String picPrefix) { this.picPrefix = picPrefix; }

    public String getPicSuffix() { return picSuffix; }
    public void setPicSuffix(String picSuffix) { this.picSuffix = picSuffix; }

    public String getPicExtract() { return picExtract; }
    public void setPicExtract(String picExtract) { this.picExtract = picExtract; }

    public String getPicHost() { return picHost; }
    public void setPicHost(String picHost) { this.picHost = picHost; }

    public boolean isCdndefend() { return cdndefend; }
    public void setCdndefend(boolean cdndefend) { this.cdndefend = cdndefend; }

    public boolean isDanmaku() { return danmaku; }
    public void setDanmaku(boolean danmaku) { this.danmaku = danmaku; }

    @Override
    public String toString() {
        return "SiteConfig{name=" + siteName + "}";
    }
}
