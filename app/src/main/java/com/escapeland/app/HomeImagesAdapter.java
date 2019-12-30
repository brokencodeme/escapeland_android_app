package com.escapeland.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class HomeImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<HomeScrolledData> homeScrolledData;

    HomeImagesAdapter(Context context, List<HomeScrolledData> homeScrolledData) {
        this.context = context;
        this.homeScrolledData = homeScrolledData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new CellFeedViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
        bindDefaultFeedItem(position, holder);
    }

    private void bindDefaultFeedItem(int position, CellFeedViewHolder holder) {
          HomeScrolledData scrollingData = homeScrolledData.get(position);
          holder.imageView.setImageResource(scrollingData.getImage_data());
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return homeScrolledData.size();
    }

    private static class CellFeedViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        CellFeedViewHolder(View view) {
            super(view);
           imageView = (ImageView) view.findViewById(R.id.home_sliding_imagae_view);

        }
    }
}
