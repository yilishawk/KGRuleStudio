package com.github.kgstudio;

import android.app.Application;
import com.github.kgstudio.util.JsonGenerator;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局 Application，负责站点数据的持久化存储
 */
public class KGStudioApp extends Application {

    private static final String DATA_DIR = "kgstudio_sites";
    private static final String EXT = ".json";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 保存站点配置到文件
     */
    public void saveSite(String key, String jsonContent) {
        try {
            File dir = getDir(DATA_DIR, MODE_PRIVATE);
            File file = new File(dir, key + EXT);
            FileWriter writer = new FileWriter(file);
            writer.write(jsonContent);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取站点配置文件
     */
    public String readSite(String key) {
        try {
            File dir = getDir(DATA_DIR, MODE_PRIVATE);
            File file = new File(dir, key + EXT);
            if (!file.exists()) return null;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除站点配置
     */
    public void deleteSite(String key) {
        try {
            File dir = getDir(DATA_DIR, MODE_PRIVATE);
            File file = new File(dir, key + EXT);
            if (file.exists()) file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有站点 key 列表
     */
    public List<String> getAllSiteKeys() {
        List<String> keys = new ArrayList<>();
        try {
            File dir = getDir(DATA_DIR, MODE_PRIVATE);
            if (!dir.exists() || !dir.isDirectory()) return keys;
            File[] files = dir.listFiles((d, name) -> name.endsWith(EXT));
            if (files != null) {
                for (File f : files) {
                    String key = f.getName().replace(EXT, "");
                    keys.add(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }
}
