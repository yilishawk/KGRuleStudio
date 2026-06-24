package com.github.kgstudio;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.kgstudio.adapter.SiteListAdapter;
import com.github.kgstudio.util.JsonGenerator;
import java.io.File;
import java.util.List;

/**
 * 主界面 — 站点列表管理
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SiteListAdapter adapter;
    private KGStudioApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (KGStudioApp) getApplication();

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SiteListAdapter(this::onItemClick, this::onDeleteClick);
        recyclerView.setAdapter(adapter);

        loadSites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new) {
            createNewSite();
            return true;
        } else if (id == R.id.action_import) {
            importFromClipboard();
            return true;
        } else if (id == R.id.action_example) {
            showExampleChoice();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 新建站点
     */
    private void createNewSite() {
        new AlertDialog.Builder(this)
                .setTitle("新建站点")
                .setMessage("请输入站点标识（英文，如 mysite）")
                .setCancelable(false)
                .setPositiveButton("确定", (dialog, which) -> {
                    android.widget.EditText et = new android.widget.EditText(this);
                    ((AlertDialog) dialog).setView(et);
                    et.setHint("输入站点标识");
                    String key = et.getText().toString().trim();
                    if (key.isEmpty()) {
                        Toast.makeText(this, "站点标识不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (app.readSite(key) != null) {
                        Toast.makeText(this, "站点 " + key + " 已存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 保存空配置
                    app.saveSite(key, JsonGenerator.buildEmptyExample());
                    startActivity(new Intent(this, SiteEditActivity.class)
                            .putExtra("SITE_KEY", key));
                    loadSites();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 从剪贴板导入 JSON
     */
    private void importFromClipboard() {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cm != null && cm.hasPrimaryClip()) {
            ClipData cd = cm.getPrimaryClip();
            if (cd != null && cd.getItemCount() > 0) {
                String json = cd.getItemAt(0).getText().toString();
                if (json.trim().startsWith("{")) {
                    new AlertDialog.Builder(this)
                            .setTitle("导入站点")
                            .setMessage("检测到有效 JSON，是否导入？")
                            .setPositiveButton("导入", (dialog, which) -> {
                                String key = "imported_" + System.currentTimeMillis();
                                app.saveSite(key, json);
                                Toast.makeText(this, "导入成功: " + key, Toast.LENGTH_SHORT).show();
                                loadSites();
                            })
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    Toast.makeText(this, "剪贴板内容不是有效的 JSON", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "剪贴板为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示示例选择
     */
    private void showExampleChoice() {
        new AlertDialog.Builder(this)
                .setTitle("示例模板")
                .setItems(new CharSequence[]{"基础示例", "Steps 示例"}, (dialog, which) -> {
                    String json;
                    String key;
                    if (which == 0) {
                        json = JsonGenerator.buildEmptyExample();
                        key = "example_basic";
                    } else {
                        json = JsonGenerator.buildStepsExample();
                        key = "example_steps";
                    }
                    app.saveSite(key, json);
                    Toast.makeText(this, "示例已添加: " + key, Toast.LENGTH_SHORT).show();
                    loadSites();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 点击站点 → 编辑
     */
    private void onItemClick(String key) {
        startActivity(new Intent(this, SiteEditActivity.class)
                .putExtra("SITE_KEY", key));
    }

    /**
     * 长按删除
     */
    private void onDeleteClick(String key) {
        new AlertDialog.Builder(this)
                .setTitle("删除站点")
                .setMessage("确定删除站点 " + key + " 吗？")
                .setPositiveButton("删除", (d, w) -> {
                    app.deleteSite(key);
                    loadSites();
                    Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 加载站点列表
     */
    private void loadSites() {
        List<String> keys = app.getAllSiteKeys();
        adapter.setSites(keys);
    }
}
