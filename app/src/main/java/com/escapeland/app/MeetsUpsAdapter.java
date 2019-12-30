package com.escapeland.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.escapeland.app.R;

import java.util.List;

public class MeetsUpsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<MeetsUpsContent> meetsUpsContentList;

    public MeetsUpsAdapter(Context context, List<MeetsUpsContent> meetsUpsContentList) {
        this.context = context;
        this.meetsUpsContentList = meetsUpsContentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.meets_ups_content, parent, false);
        return new CellFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
        bindDefaultFeedItem(position, holder);
    }

    private void bindDefaultFeedItem(int position, CellFeedViewHolder holder) {

        MeetsUpsContent images = meetsUpsContentList.get(position);
        holder.imageView.setImageDrawable(context.getResources().getDrawable(images.getMeetsUpsImage()));
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return meetsUpsContentList.size();
    }

    private static class CellFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        CellFeedViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.meetsup_image_view);

        }
    }
}
