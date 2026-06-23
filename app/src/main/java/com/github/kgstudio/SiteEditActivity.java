package com.github.kgstudio;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import com.github.kgstudio.editor.*;
import com.github.kgstudio.model.SiteConfig;
import com.github.kgstudio.util.JsonGenerator;
import java.io.File;
import java.io.FileWriter;

public class SiteEditActivity extends AppCompatActivity {

    private String siteKey;
    private TabLayout tabLayout;
    private Fragment[] fragments = new Fragment[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_edit);

        siteKey = getIntent().getStringExtra("SITE_KEY");

        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(siteKey != null ? siteKey : "编辑站点");
        }

        tabLayout = findViewById(R.id.tabLayout);
        setupTabs();
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("基础"));
        tabLayout.addTab(tabLayout.newTab().setText("分类"));
        tabLayout.addTab(tabLayout.newTab().setText("搜索"));
        tabLayout.addTab(tabLayout.newTab().setText("详情"));
        tabLayout.addTab(tabLayout.newTab().setText("播放"));
        tabLayout.addTab(tabLayout.newTab().setText("图片"));
        tabLayout.addTab(tabLayout.newTab().setText("功能"));

        fragments[0] = new BaseConfigFragment();
        fragments[1] = new CategoryConfigFragment();
        fragments[2] = new SearchConfigFragment();
        fragments[3] = new DetailConfigFragment();
        fragments[4] = new PlayConfigFragment();
        fragments[5] = new ImageConfigFragment();
        fragments[6] = new FeatureConfigFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragments[0])
                .commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragments[tab.getPosition()])
                        .commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.site_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_save) {
            saveAndExport();
            return true;
        } else if (id == R.id.action_export_json) {
            exportToJsonFile();
            return true;
        } else if (id == R.id.action_load) {
            loadFromJson();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndExport() {
        SiteConfig config = fragments[0] instanceof BaseConfigFragment
                ? ((BaseConfigFragment) fragments[0]).getConfig() : new SiteConfig(siteKey);

        if (fragments[1] instanceof CategoryConfigFragment) {
            SiteConfig c = ((CategoryConfigFragment) fragments[1]).getConfig();
            config.setClasses(c.getClasses());
            config.setFilters(c.getFilters());
            config.setCateMethod(c.getCateMethod());
            config.setCateUrl(c.getCateUrl());
            config.setCatePage1(c.getCatePage1());
            config.setCateBody(c.getCateBody());
            config.setCateItem(c.getCateItem());
            config.setCateId(c.getCateId());
            config.setCateName(c.getCateName());
            config.setCatePic(c.getCatePic());
            config.setCateRemarks(c.getCateRemarks());
            config.setCateListPath(c.getCateListPath());
            config.setDetailUrl(c.getDetailUrl());
        }

        if (fragments[2] instanceof SearchConfigFragment) {
            SiteConfig c = ((SearchConfigFragment) fragments[2]).getConfig();
            config.setSearchMethod(c.getSearchMethod());
            config.setSearchUrl(c.getSearchUrl());
            config.setSearchBody(c.getSearchBody());
        }

        if (fragments[3] instanceof DetailConfigFragment) {
            SiteConfig c = ((DetailConfigFragment) fragments[3]).getConfig();
            config.setDetailMethod(c.getDetailMethod());
            config.setDetailBody(c.getDetailBody());
            config.setDtName(c.getDtName());
            config.setDtPic(c.getDtPic());
            config.setDtRemarks(c.getDtRemarks());
            config.setDtActor(c.getDtActor());
            config.setDtDirector(c.getDtDirector());
            config.setDtContent(c.getDtContent());
            config.setDtFrom(c.getDtFrom());
            config.setDtList(c.getDtList());
        }

        if (fragments[4] instanceof PlayConfigFragment) {
            SiteConfig c = ((PlayConfigFragment) fragments[4]).getConfig();
            config.setDanmaku(c.isDanmaku());
            config.setPlayJx(c.getPlayJx());
            config.setPlayJxList(c.getPlayJxList());
            config.setPlayJxTitle(c.getPlayJxTitle());
            config.setPlayJxParse(c.getPlayJxParse());
        }

        if (fragments[5] instanceof ImageConfigFragment) {
            SiteConfig c = ((ImageConfigFragment) fragments[5]).getConfig();
            config.setPicJump(c.isPicJump());
            config.setPicPrefix(c.getPicPrefix());
            config.setPicSuffix(c.getPicSuffix());
            config.setPicExtract(c.getPicExtract());
            config.setPicHost(c.getPicHost());
        }

        if (fragments[6] instanceof FeatureConfigFragment) {
            SiteConfig c = ((FeatureConfigFragment) fragments[6]).getConfig();
            config.setCdndefend(c.isCdndefend());
            config.setDanmaku(c.isDanmaku());
        }

        String json = JsonGenerator.toCompactJson(config);

        if (siteKey != null) {
            ((KGStudioApp) getApplication()).saveSite(siteKey, json);
        }

        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("kg_rule", json));
        }

        Toast.makeText(this, "已保存并复制 JSON (" + json.length() + " 字符)", Toast.LENGTH_LONG).show();
    }

    private void exportToJsonFile() {
        saveAndExport();
        String json = ((KGStudioApp) getApplication()).readSite(siteKey);
        if (json != null) {
            try {
                File dir = getExternalFilesDir(null);
                if (dir == null) dir = getFilesDir();
                File file = new File(dir, siteKey + ".json");
                FileWriter writer = new FileWriter(file);
                writer.write(json);
                writer.flush();
                writer.close();
                Toast.makeText(this, "已导出: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "导出失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadFromJson() {
        android.widget.EditText et = new android.widget.EditText(this);
        et.setHint("粘贴 JSON 配置");
        et.setMinLines(5);
        new android.app.AlertDialog.Builder(this)
                .setTitle("加载 JSON")
                .setView(et)
                .setPositiveButton("加载", (dialog, which) -> {
                    String json = et.getText().toString().trim();
                    SiteConfig config = JsonGenerator.fromJson(json);
                    if (config != null) {
                        Toast.makeText(this, "加载成功: " + config.getSiteName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}

