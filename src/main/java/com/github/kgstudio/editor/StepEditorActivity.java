package com.github.kgstudio.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.kgstudio.R;
import com.github.kgstudio.model.ExtractRule;
import com.github.kgstudio.model.HeaderEntry;
import com.github.kgstudio.model.PlayStep;
import com.github.kgstudio.model.SiteConfig;
import com.github.kgstudio.model.VariableEntry;
import com.github.kgstudio.util.HttpHelper;
import com.github.kgstudio.util.JsonGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Steps 编辑器 — 播放解析步骤可视化配置
 * 支持：变量池、Steps CRUD、实时JSON预览、单步测试、连续测试
 */
public class StepEditorActivity extends AppCompatActivity {

    private RecyclerView rvSteps;
    private StepListAdapter stepAdapter;
    private List<PlayStep> steps = new ArrayList<>();

    private LinearLayout llVarPool;
    private List<VariableEntry> customVars = new ArrayList<>();

    private TextView tvJsonPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_editor);

        setTitle("播放解析 Steps 编辑器");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvSteps = findViewById(R.id.rvSteps);
        rvSteps.setLayoutManager(new LinearLayoutManager(this));
        stepAdapter = new StepListAdapter(steps, this::onStepTest, this::onStepDelete, this::onStepMove);
        rvSteps.setAdapter(stepAdapter);

        llVarPool = findViewById(R.id.llVarPool);
        initVarPool();

        tvJsonPreview = findViewById(R.id.tvJsonPreview);

        findViewById(R.id.fabAddStep).setOnClickListener(v -> addStep());
        findViewById(R.id.fabAddVar).setOnClickListener(v -> addCustomVar());
        // 连续测试按钮
        findViewById(R.id.btnChainTest).setOnClickListener(v -> onChainTest());

        // 复制按钮
        findViewById(R.id.btnCopyJson).setOnClickListener(v -> {
            String json = tvJsonPreview.getText().toString();
            android.content.ClipboardManager cm = (android.content.ClipboardManager)
                    getSystemService(CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(android.content.ClipData.newPlainText("kg_steps", json));
                Toast.makeText(this, "JSON 已复制", Toast.LENGTH_SHORT).show();
            }
        });

        // 关闭按钮
        findViewById(R.id.btnCloseEditor).setOnClickListener(v -> finish());

        updateJsonPreview();
    }

    private void initVarPool() {
        String[] sysVars = {"host", "play_id", "vod_name", "vod_episode", "vod_play_url"};
        for (String sv : sysVars) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setText("📌 " + sv);
            tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tv.setTextSize(12);
            tv.setPadding(8, 4, 8, 4);
            llVarPool.addView(tv);
        }

        View divider = new View(this);
        LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        dp.setMargins(0, 12, 0, 12);
        divider.setLayoutParams(dp);
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        llVarPool.addView(divider);
    }

    private void addCustomVar() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_variable, null);
        EditText etName = dialogView.findViewById(R.id.etVarName);
        EditText etValue = dialogView.findViewById(R.id.etVarValue);

        new AlertDialog.Builder(this)
                .setTitle("添加自定义变量")
                .setView(dialogView)
                .setPositiveButton("添加", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String value = etValue.getText().toString().trim();
                    if (!name.isEmpty()) {
                        customVars.add(new VariableEntry(name, value));
                        refreshVarPoolDisplay();
                        updateJsonPreview();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void refreshVarPoolDisplay() {
        int count = llVarPool.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View v = llVarPool.getChildAt(i);
            if (v instanceof TextView && ((TextView) v).getText().toString().startsWith("🔑")) {
                llVarPool.removeView(v);
            }
        }
        for (VariableEntry ve : customVars) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setText("🔑 " + ve.getName() + " = " + ve.getValue());
            tv.setTextColor(getResources().getColor(android.R.color.black));
            tv.setTextSize(12);
            tv.setPadding(8, 4, 8, 4);
            llVarPool.addView(tv);
        }
    }

    private void addStep() {
        PlayStep step = new PlayStep("");
        step.setMethod("get");
        steps.add(step);
        stepAdapter.notifyItemInserted(steps.size() - 1);
        updateJsonPreview();
    }

    private void onStepDelete(int index) {
        steps.remove(index);
        stepAdapter.notifyItemRemoved(index);
        updateJsonPreview();
    }

    private void onStepMove(int from, int to) {
        PlayStep s = steps.remove(from);
        steps.add(to, s);
        stepAdapter.notifyDataSetChanged();
        updateJsonPreview();
    }

    /**
     * 测试单步请求
     */
    private void onStepTest(int index) {
        PlayStep step = steps.get(index);
        Map<String, String> inheritedVars = new HashMap<>();
        inheritedVars.put("host", "https://example.com");
        inheritedVars.put("play_id", "");
        inheritedVars.put("vod_name", "");
        inheritedVars.put("vod_episode", "1");

        // 继承前面所有步骤的 vars 结果（连续测试）
        for (int i = 0; i < index; i++) {
            PlayStep prevStep = steps.get(i);
            if (prevStep.getVars() != null) {
                for (ExtractRule er : prevStep.getVars()) {
                    inheritedVars.put(er.getKey(), "[继承自Step " + (i+1) + "]");
                }
            }
        }

        HttpHelper.TestResult result = HttpHelper.executeTest(
                "https://example.com", step, customVars, inheritedVars);

        showTestResult(result, step, index);
    }

    /**
     * 连续测试整个 Steps 链
     */
    private void onChainTest() {
        Map<String, String> varPool = new HashMap<>();
        varPool.put("host", "https://example.com");
        varPool.put("play_id", "");
        varPool.put("vod_name", "");
        varPool.put("vod_episode", "1");

        for (VariableEntry ve : customVars) {
            varPool.put(ve.getName(), ve.getValue());
        }

        StringBuilder sb = new StringBuilder("=== 连续测试 Steps ===\n");
        for (int i = 0; i < steps.size(); i++) {
            PlayStep step = steps.get(i);
            sb.append("Step ").append(i + 1).append(": ").append(step.getUrl()).append("\n");

            HttpHelper.TestResult result = HttpHelper.executeTest(
                    "https://example.com", step, customVars, varPool);

            sb.append("  状态: ").append(result.success ? "OK" : "FAIL").append("\n");
            sb.append("  耗时: ").append(result.responseTimeMs).append("ms\n");
            sb.append("  大小: ").append(result.responseSize).append(" bytes\n");

            // 提取 vars 模拟结果
            if (result.success && step.getVars() != null) {
                for (ExtractRule er : step.getVars()) {
                    varPool.put(er.getKey(), "[提取成功]");
                    sb.append("  vars[").append(er.getKey()).append("] = [提取成功]\n");
                }
            }
            sb.append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("连续测试结果")
                .setMessage(sb.toString())
                .setPositiveButton("关闭", null)
                .show();
    }

    private void showTestResult(HttpHelper.TestResult result, PlayStep step, int index) {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_step_test, null);

        TextView tvStatus = dialogView.findViewById(R.id.tvStatus);
        TextView tvUrl = dialogView.findViewById(R.id.tvRequestUrl);
        TextView tvTime = dialogView.findViewById(R.id.tvTime);
        TextView tvSize = dialogView.findViewById(R.id.tvSize);
        TextView tvBody = dialogView.findViewById(R.id.tvResponseBody);
        TextView tvVars = dialogView.findViewById(R.id.tvVarResults);

        String statusText = result.success ? "SUCCESS (" + result.statusCode + ")" : "FAILED (" + result.statusCode + ")";
        int color = result.success ? android.R.color.holo_green_dark : android.R.color.holo_red_dark;
        tvStatus.setText(statusText);
        tvStatus.setTextColor(getResources().getColor(color));

        tvUrl.setText("URL: " + step.getUrl());
        tvTime.setText("耗时: " + result.responseTimeMs + "ms");
        tvSize.setText("大小: " + result.responseSize + " bytes");

        String body = result.responseBody;
        if (body != null && body.length() > 2000) {
            body = body.substring(0, 2000) + "\n...(共" + result.responseBody.length() + "字符)";
        }
        tvBody.setText(body != null ? body : "(空)");

        // 显示 vars 提取结果
        StringBuilder varSb = new StringBuilder();
        if (step.getVars() != null) {
            for (ExtractRule er : step.getVars()) {
                varSb.append(er.getKey()).append(" = ");
                // 从响应体中尝试提取
                if (result.success && body != null) {
                    varSb.append("[待提取]");
                } else {
                    varSb.append("(无数据)");
                }
                varSb.append("\n");
            }
        } else {
            varSb.append("无 vars 配置");
        }
        tvVars.setText(varSb.toString());

        new AlertDialog.Builder(this)
                .setTitle("Step " + (index + 1) + " 测试结果")
                .setView(dialogView)
                .setPositiveButton("关闭", null)
                .show();
    }

    private void updateJsonPreview() {
        SiteConfig config = new SiteConfig();
        config.setPlaySteps(steps);
        config.setSiteName("Steps Editor");
        String json = JsonGenerator.toJson(config);
        tvJsonPreview.setText(json);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

