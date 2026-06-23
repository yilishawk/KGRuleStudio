package com.github.kgstudio.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.kgstudio.R;
import com.github.kgstudio.model.SiteConfig;

public class FeatureConfigFragment extends Fragment {

    private CheckBox cbCdndefend, cbDanmaku;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_feature_config, container, false);

        cbCdndefend = v.findViewById(R.id.cbCdndefend);
        cbDanmaku = v.findViewById(R.id.cbDanmaku);

        return v;
    }

    public SiteConfig getConfig() {
        SiteConfig config = new SiteConfig();
        config.setCdndefend(cbCdndefend.isChecked());
        config.setDanmaku(cbDanmaku.isChecked());
        return config;
    }
}
