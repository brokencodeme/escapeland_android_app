package com.escapeland.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escapeland.app.R;

import java.util.List;

public class AttractionContentsAdapters extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<AttractionContents> attractionContents;

    public AttractionContentsAdapters(Context context, List<AttractionContents> attractionContents) {
        this.context = context;
        this.attractionContents = attractionContents;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.attraction_items, parent, false);
        return new CellFeedViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CellFeedViewHolder viewHolder = (CellFeedViewHolder) holder;
        bindDefaultFeedItem(position, viewHolder);

    }

    private void bindDefaultFeedItem(int position, CellFeedViewHolder holder) {

        AttractionContents image = attractionContents.get(position);
        holder.imageView.setImageDrawable(context.getResources().getDrawable(image.getAttraction_images()));
    }
    @Override
    public int getItemCount() {
        return attractionContents.size();
    }

    private static class CellFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        CellFeedViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.attraction_imageview);

        }
    }
}
