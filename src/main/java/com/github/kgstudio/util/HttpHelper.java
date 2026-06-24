package com.github.kgstudio.util;

import com.github.kgstudio.model.HeaderEntry;
import com.github.kgstudio.model.PlayStep;
import com.github.kgstudio.model.VariableEntry;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp 网络请求封装
 * 用于 Steps 编辑器中的测试功能
 */
public class HttpHelper {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    /**
     * 测试结果封装
     */
    public static class TestResult {
        public boolean success;
        public int statusCode;
        public long responseTimeMs;
        public int responseSize;
        public String responseBody;
        public String contentType;
        public Map<String, List<String>> responseHeaders;
        public long startTime;

        public TestResult() {
            this.success = false;
            this.statusCode = 0;
            this.responseTimeMs = 0;
            this.responseSize = 0;
            this.responseBody = "";
            this.contentType = "";
            this.responseHeaders = new HashMap<>();
        }
    }

    /**
     * 执行单次请求测试
     */
    public static TestResult executeTest(String siteUrl, PlayStep step,
                                          List<VariableEntry> customVars,
                                          Map<String, String> inheritedVars) {
        TestResult result = new TestResult();
        result.startTime = System.currentTimeMillis();

        try {
            // 1. 构建变量池（系统变量 + 自定义变量 + 继承变量）
            Map<String, String> varPool = new HashMap<>();
            varPool.put("host", siteUrl);
            varPool.put("play_id", "");
            varPool.put("vod_name", "");
            varPool.put("vod_episode", "1");
            if (inheritedVars != null) varPool.putAll(inheritedVars);
            if (customVars != null) {
                for (VariableEntry v : customVars) {
                    varPool.put(v.getName(), v.getValue());
                }
            }

            // 2. 替换 URL 中的变量
            String url = replaceVars(step.getUrl(), varPool);

            // 3. 替换 Body 中的变量
            String bodyStr = step.getBody();
            if (bodyStr != null) {
                bodyStr = replaceVars(bodyStr, varPool);
            }

            // 4. 构建 Request
            Request.Builder requestBuilder = new Request.Builder().url(url);

            // 5. 设置 Method + Body + Headers
            String method = step.getMethod() != null ? step.getMethod() : "get";
            if ("post".equalsIgnoreCase(method)) {
                RequestBody requestBody;
                if (bodyStr != null && bodyStr.trim().startsWith("{")) {
                    requestBody = RequestBody.create(bodyStr, JSON_MEDIA_TYPE);
                } else {
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    if (bodyStr != null && !bodyStr.isEmpty()) {
                        String[] pairs = bodyStr.split("&");
                        for (String pair : pairs) {
                            String[] kv = pair.split("=", 2);
                            if (kv.length == 2) {
                                formBuilder.add(kv[0], kv[1]);
                            } else if (kv.length == 1) {
                                formBuilder.add(kv[0], "");
                            }
                        }
                    }
                    requestBody = formBuilder.build();
                }
                requestBuilder.post(requestBody);
            } else {
                // GET，body 作为 URL 参数附加
                if (bodyStr != null && !bodyStr.isEmpty()) {
                    String separator = url.contains("?") ? "&" : "?";
                    requestBuilder.get();
                    // 重新构建带参数的 URL
                    String fullUrl = url + separator + bodyStr;
                    requestBuilder = new Request.Builder().url(fullUrl);
                } else {
                    requestBuilder.get();
                }
            }

            // 6. 添加请求头
            List<HeaderEntry> headers = step.getHeaders();
            if (headers != null) {
                for (HeaderEntry h : headers) {
                    String hName = replaceVars(h.getName(), varPool);
                    String hValue = replaceVars(h.getValue(), varPool);
                    requestBuilder.addHeader(hName, hValue);
                }
            }
            // 默认 UA
            if (!hasHeader(headers, "User-Agent")) {
                requestBuilder.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 12) AppleWebKit/537.36");
            }

            Request request = requestBuilder.build();

            // 7. 执行请求
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .eventListener(new EventListener() {
                        @Override
                        public void callEnd(Call call) {
                            result.responseTimeMs = System.currentTimeMillis() - result.startTime;
                        }
                    })
                    .build();

            Response response = client.newCall(request).execute();
            result.success = response.isSuccessful();
            result.statusCode = response.code();
            result.contentType = response.header("Content-Type", "");

            try (ResponseBody rb = response.body()) {
                if (rb != null) {
                    byte[] bytes = rb.bytes();
                    result.responseSize = bytes.length;
                    result.responseBody = new String(bytes, "UTF-8");
                }
            }

            // 收集响应头
            Headers respHeaders = response.headers();
            for (int i = 0; i < respHeaders.size(); i++) {
                String name = respHeaders.name(i);
                List<String> values = result.responseHeaders.computeIfAbsent(name, k -> new ArrayList<>());
                values.add(respHeaders.value(i));
            }

        } catch (Exception e) {
            result.responseTimeMs = System.currentTimeMillis() - result.startTime;
            result.success = false;
            result.statusCode = 0;
            result.responseBody = "请求失败: " + e.getMessage();
        }

        return result;
    }

    /**
     * 替换字符串中的 {var} 占位符
     */
    private static String replaceVars(String text, Map<String, String> varPool) {
        if (text == null) return "";
        String result = text;
        for (Map.Entry<String, String> entry : varPool.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    /**
     * 检查 headers 列表中是否已有某名称的请求头
     */
    private static boolean hasHeader(List<HeaderEntry> headers, String name) {
        if (headers == null) return false;
        for (HeaderEntry h : headers) {
            if (h.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }
}
