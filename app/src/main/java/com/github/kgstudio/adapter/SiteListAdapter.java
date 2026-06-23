package com.github.kgstudio.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.kgstudio.R;
import com.github.kgstudio.util.JsonGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * 站点列表 Adapter
 */
public class SiteListAdapter extends RecyclerView.Adapter<SiteListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String key);
        void onDeleteClick(String key);
    }

    private OnItemClickListener listener;
    private List<String> sites = new ArrayList<>();

    public SiteListAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSites(List<String> sites) {
        this.sites = new ArrayList<>(sites);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_site, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String key = sites.get(position);
        holder.tvKey.setText(key);

        // 尝试读取 JSON 获取 site_name
        Context ctx = holder.itemView.getContext();
        if (ctx instanceof com.github.kgstudio.KGStudioApp) {
            String json = ((com.github.kgstudio.KGStudioApp) ctx).readSite(key);
            if (json != null) {
                com.github.kgstudio.model.SiteConfig config = JsonGenerator.fromJson(json);
                if (config != null && config.getSiteName() != null) {
                    holder.tvName.setText(config.getSiteName());
                } else {
                    holder.tvName.setText("未命名站点");
                }
            } else {
                holder.tvName.setText("读取失败");
            }
        } else {
            holder.tvName.setText(key);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(key);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onDeleteClick(key);
            return true;
        });

        // 双击复制 JSON
        holder.btnCopy.setOnClickListener(v -> {
            Context appCtx = v.getContext().getApplicationContext();
            if (appCtx instanceof com.github.kgstudio.KGStudioApp) {
                String json = ((com.github.kgstudio.KGStudioApp) appCtx).readSite(key);
                if (json != null) {
                    ClipboardManager cm = (ClipboardManager) appCtx
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    if (cm != null) {
                        cm.setPrimaryClip(ClipData.newPlainText("kg_rule", json));
                        android.widget.Toast.makeText(appCtx, "JSON 已复制",
                                android.widget.Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sites.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKey, tvName;
        TextView btnCopy;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKey = itemView.findViewById(R.id.tvSiteKey);
            tvName = itemView.findViewById(R.id.tvSiteName);
            btnCopy = itemView.findViewById(R.id.btnCopy);
        }
    }
}
