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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.kgstudio.R;
import com.github.kgstudio.model.FilterGroup;
import com.github.kgstudio.model.SiteClass;
import com.github.kgstudio.model.SiteConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tab 2: 分类/列表配置
 */
public class CategoryConfigFragment extends Fragment {

    private EditText etCateMethod, etCateUrl, etCatePage1, etCateBody;
    private EditText etCateItem, etCateId, etCateName, etCatePic, etCateRemarks;
    private EditText etCateListPath, etDetailUrl;
    private RecyclerView rvClasses;
    private ClassListAdapter classAdapter;
    private List<SiteClass> classes = new ArrayList<>();
    private Map<String, FilterGroup> filters = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_category_config, container, false);

        etCateMethod = v.findViewById(R.id.etCateMethod);
        etCateUrl = v.findViewById(R.id.etCateUrl);
        etCatePage1 = v.findViewById(R.id.etCatePage1);
        etCateBody = v.findViewById(R.id.etCateBody);
        etCateItem = v.findViewById(R.id.etCateItem);
        etCateId = v.findViewById(R.id.etCateId);
        etCateName = v.findViewById(R.id.etCateName);
        etCatePic = v.findViewById(R.id.etCatePic);
        etCateRemarks = v.findViewById(R.id.etCateRemarks);
        etCateListPath = v.findViewById(R.id.etCateListPath);
        etDetailUrl = v.findViewById(R.id.etDetailUrl);
        rvClasses = v.findViewById(R.id.rvClasses);

        AutoCompleteTextView actv = v.findViewById(R.id.actvCateMethod);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"get", "post"});
        actv.setAdapter(adapter);

        classAdapter = new ClassListAdapter(classes);
        rvClasses.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvClasses.setAdapter(classAdapter);

        FloatingActionButton fab = v.findViewById(R.id.fabAddClass);
        fab.setOnClickListener(vv -> addClass());

        return v;
    }

    private void addClass() {
        classes.add(new SiteClass("", ""));
        classAdapter.notifyDataSetChanged();
    }

    public SiteConfig getConfig() {
        SiteConfig config = new SiteConfig();
        config.setCateMethod(etCateMethod.getText().toString());
        config.setCateUrl(etCateUrl.getText().toString());
        config.setCatePage1(etCatePage1.getText().toString());
        config.setCateBody(etCateBody.getText().toString());
        config.setCateItem(etCateItem.getText().toString());
        config.setCateId(etCateId.getText().toString());
        config.setCateName(etCateName.getText().toString());
        config.setCatePic(etCatePic.getText().toString());
        config.setCateRemarks(etCateRemarks.getText().toString());
        config.setCateListPath(etCateListPath.getText().toString());
        config.setDetailUrl(etDetailUrl.getText().toString());
        config.setClasses(classes);
        config.setFilters(filters);
        return config;
    }
}
