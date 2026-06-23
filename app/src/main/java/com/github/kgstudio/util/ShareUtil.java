package com.github.kgstudio.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * 分享和导出工具
 */
public class ShareUtil {

    /**
     * 复制 JSON 到剪贴板
     */
    public static void copyToJsonClipboard(Context context, String json, String label) {
        try {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                android.content.ClipData cd = android.content.ClipData.newPlainText(label, json);
                cm.setPrimaryClip(cd);
                Toast.makeText(context, "JSON 已复制到剪贴板 (" + json.length() + " 字符)", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "复制失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 压缩 JSON（去除空白）
     */
    public static String compressJson(String json) {
        if (json == null) return "{}";
        return json.replaceAll("\\\\s+", "").trim();
    }

    /**
     * 美化 JSON（简单实现）
     */
    public static String prettyJson(String json) {
        if (json == null) return "{}";
        StringBuilder sb = new StringBuilder();
        int indent = 0;
        boolean inString = false;
        for (char c : json.toCharArray()) {
            if (c == '"') {
                sb.append(c);
                inString = !inString;
            } else if (!inString) {
                if (c == '{' || c == '[') {
                    sb.append(c).append('\n');
                    indent++;
                    for (int i = 0; i < indent; i++) sb.append("  ");
                } else if (c == '}' || c == ']') {
                    sb.append('\n');
                    indent--;
                    for (int i = 0; i < indent; i++) sb.append("  ");
                    sb.append(c);
                } else if (c == ',') {
                    sb.append(c).append('\n');
                    for (int i = 0; i < indent; i++) sb.append("  ");
                } else if (c == ':') {
                    sb.append(c).append(' ');
                } else if (c == ' ' || c == '\t' || c == '\n') {
                    // skip whitespace
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
