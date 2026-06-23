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

public class DetailConfigFragment extends Fragment {

    private EditText etDetailMethod, etDetailBody, etDtName, etDtPic, etDtRemarks;
    private EditText etDtActor, etDtDirector, etDtContent, etDtFrom, etDtList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_detail_config, container, false);

        etDetailMethod = v.findViewById(R.id.etDetailMethod);
        etDetailBody = v.findViewById(R.id.etDetailBody);
        etDtName = v.findViewById(R.id.etDtName);
        etDtPic = v.findViewById(R.id.etDtPic);
        etDtRemarks = v.findViewById(R.id.etDtRemarks);
        etDtActor = v.findViewById(R.id.etDtActor);
        etDtDirector = v.findViewById(R.id.etDtDirector);
        etDtContent = v.findViewById(R.id.etDtContent);
        etDtFrom = v.findViewById(R.id.etDtFrom);
        etDtList = v.findViewById(R.id.etDtList);

        AutoCompleteTextView actv = v.findViewById(R.id.actvDetailMethod);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"get", "post"});
        actv.setAdapter(adapter);

        return v;
    }

    public SiteConfig getConfig() {
        SiteConfig config = new SiteConfig();
        config.setDetailMethod(etDetailMethod.getText().toString());
        config.setDetailBody(etDetailBody.getText().toString());
        config.setDtName(etDtName.getText().toString());
        config.setDtPic(etDtPic.getText().toString());
        config.setDtRemarks(etDtRemarks.getText().toString());
        config.setDtActor(etDtActor.getText().toString());
        config.setDtDirector(etDtDirector.getText().toString());
        config.setDtContent(etDtContent.getText().toString());
        config.setDtFrom(etDtFrom.getText().toString());
        config.setDtList(etDtList.getText().toString());
        return config;
    }
}
