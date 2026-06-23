package com.github.kgstudio.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.kgstudio.R;
import com.github.kgstudio.model.SiteConfig;

public class ImageConfigFragment extends Fragment {

    private CheckBox cbPicJump;
    private EditText etPicPrefix, etPicSuffix, etPicExtract, etPicHost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_image_config, container, false);

        cbPicJump = v.findViewById(R.id.cbPicJump);
        etPicPrefix = v.findViewById(R.id.etPicPrefix);
        etPicSuffix = v.findViewById(R.id.etPicSuffix);
        etPicExtract = v.findViewById(R.id.etPicExtract);
        etPicHost = v.findViewById(R.id.etPicHost);

        return v;
    }

    public SiteConfig getConfig() {
        SiteConfig config = new SiteConfig();
        config.setPicJump(cbPicJump.isChecked());
        config.setPicPrefix(etPicPrefix.getText().toString());
        config.setPicSuffix(etPicSuffix.getText().toString());
        config.setPicExtract(etPicExtract.getText().toString());
        config.setPicHost(etPicHost.getText().toString());
        return config;
    }
}
