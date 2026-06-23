package com.github.kgstudio.editor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.kgstudio.R;
import com.github.kgstudio.model.SiteConfig;

public class PlayConfigFragment extends Fragment {

    private CheckBox cbDanmaku;
    private EditText etPlayJx, etPlayJxList, etPlayJxTitle, etPlayJxParse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_play_config, container, false);

        cbDanmaku = v.findViewById(R.id.cbDanmaku);
        etPlayJx = v.findViewById(R.id.etPlayJx);
        etPlayJxList = v.findViewById(R.id.etPlayJxList);
        etPlayJxTitle = v.findViewById(R.id.etPlayJxTitle);
        etPlayJxParse = v.findViewById(R.id.etPlayJxParse);

        Button btnOpenSteps = v.findViewById(R.id.btnOpenSteps);
        btnOpenSteps.setOnClickListener(vv -> {
            startActivity(new Intent(requireActivity(), StepEditorActivity.class));
        });

        return v;
    }

    public SiteConfig getConfig() {
        SiteConfig config = new SiteConfig();
        config.setDanmaku(cbDanmaku.isChecked());
        config.setPlayJx(etPlayJx.getText().toString());
        config.setPlayJxList(etPlayJxList.getText().toString());
        config.setPlayJxTitle(etPlayJxTitle.getText().toString());
        config.setPlayJxParse(etPlayJxParse.getText().toString());
        return config;
    }
}
