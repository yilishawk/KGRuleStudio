package com.github.kgstudio.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.kgstudio.R;
import com.github.kgstudio.model.PlayStep;
import java.util.ArrayList;
import java.util.List;

/**
 * Steps ап╠М Adapter
 */
public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.VH> {

    public interface OnTestListener { void onTest(int index); }
    public interface OnDeleteListener { void onDelete(int index); }
    public interface OnMoveListener { void onMove(int from, int to); }

    private final List<PlayStep> steps;
    private final OnTestListener testListener;
    private final OnDeleteListener deleteListener;
    private final OnMoveListener moveListener;
    private static final String[] METHODS = {"get", "post"};

    public StepListAdapter(List<PlayStep> steps, OnTestListener tl,
                           OnDeleteListener dl, OnMoveListener ml) {
        this.steps = steps != null ? steps : new ArrayList<>();
        this.testListener = tl;
        this.deleteListener = dl;
        this.moveListener = ml;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(position, steps.get(position));
    }

    @Override
    public int getItemCount() { return steps.size(); }

    class VH extends RecyclerView.ViewHolder {
        EditText etUrl, etBody;
        Spinner spMethod;
        TextView tvStepTitle, btnTest, btnDelete, btnUp, btnDown;

        VH(@NonNull View itemView) {
            super(itemView);
            etUrl = itemView.findViewById(R.id.etStepUrl);
            etBody = itemView.findViewById(R.id.etStepBody);
            spMethod = itemView.findViewById(R.id.spMethod);
            tvStepTitle = itemView.findViewById(R.id.tvStepTitle);
            btnTest = itemView.findViewById(R.id.btnTest);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUp = itemView.findViewById(R.id.btnUp);
            btnDown = itemView.findViewById(R.id.btnDown);

            android.widget.ArrayAdapter<String> adapter =
                    new android.widget.ArrayAdapter<>(itemView.getContext(),
                            android.R.layout.simple_spinner_item, METHODS);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMethod.setAdapter(adapter);
        }

        void bind(int position, PlayStep step) {
            this.stepRef = step;
            this.posRef = position;

            tvStepTitle.setText("Step " + (position + 1));
            etUrl.setText(step.getUrl() != null ? step.getUrl() : "");
            etBody.setText(step.getBody() != null ? step.getBody() : "");
            int methodIdx = ("post".equalsIgnoreCase(step.getMethod())) ? 1 : 0;
            spMethod.setSelection(methodIdx);

            btnTest.setOnClickListener(v -> {
                if (testListener != null) testListener.onTest(position);
            });
            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) deleteListener.onDelete(position);
            });
            btnUp.setOnClickListener(v -> {
                if (position > 0 && moveListener != null) {
                    moveListener.onMove(position, position - 1);
                }
            });
            btnDown.setOnClickListener(v -> {
                if (position < steps.size() - 1 && moveListener != null) {
                    moveListener.onMove(position, position + 1);
                }
            });
            etUrl.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus && stepRef != null) {
                    stepRef.setUrl(etUrl.getText().toString());
                }
            });
            etBody.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus && stepRef != null) {
                    stepRef.setBody(etBody.getText().toString());
                }
            });
            spMethod.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {
                    if (stepRef != null) stepRef.setMethod(METHODS[pos]);
                }
                @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            });
        }

        PlayStep stepRef;
        int posRef;
    }
}
