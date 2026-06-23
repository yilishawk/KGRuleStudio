package com.github.kgstudio.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.kgstudio.R;
import com.github.kgstudio.model.SiteConfig;

public class SearchConfigFragment extends Fragment {

    private EditText etSearchMethod, etSearchUrl, etSearchBody;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_search_config, container, false);

        etSearchMethod = v.findViewById(R.id.etSearchMethod);
        etSearchUrl = v.findViewById(R.id.etSearchUrl);
        etSearchBody = v.findViewById(R.id.etSearchBody);

        AutoCompleteTextView actv = v.findViewById(R.id.actvSearchMethod);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"get", "post"});
        actv.setAdapter(adapter);

        return v;
    }

    public SiteConfig getConfig() {
        SiteConfig config = new SiteConfig();
        config.setSearchMethod(etSearchMethod.getText().toString());
        config.setSearchUrl(etSearchUrl.getText().toString());
        config.setSearchBody(etSearchBody.getText().toString());
        return config;
    }
}
