package com.github.kgstudio.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.kgstudio.R;
import com.github.kgstudio.model.SiteConfig;

public class BaseConfigFragment extends Fragment {

    private EditText etSiteName, etIndexUrl, etIndexRule;
    private EditText etSiteUrl, etHost, etUa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_base_config, container, false);

        etSiteName = v.findViewById(R.id.etSiteName);
        etIndexUrl = v.findViewById(R.id.etIndexUrl);
        etIndexRule = v.findViewById(R.id.etIndexRule);
        etSiteUrl = v.findViewById(R.id.etSiteUrl);
        etHost = v.findViewById(R.id.etHost);
        etUa = v.findViewById(R.id.etUa);

        return v;
    }

    public SiteConfig getConfig() {
        SiteConfig config = new SiteConfig();
        config.setSiteName(etSiteName.getText().toString());
        config.setIndexUrl(etIndexUrl.getText().toString());
        config.setIndexRule(etIndexRule.getText().toString());
        config.setSiteUrl(etSiteUrl.getText().toString());
        config.setHost(etHost.getText().toString());
        config.setUa(etUa.getText().toString());
        return config;
    }
}
