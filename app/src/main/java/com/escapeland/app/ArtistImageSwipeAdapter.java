package com.escapeland.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//import com.bumptech.glide.request.RequestOptions;
import com.escapeland.app.ArtistImages;
import com.escapeland.app.R;

import java.util.List;


class ArtistImageSwipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<ArtistImages> artistImages;

    ArtistImageSwipeAdapter(Context context,  List<ArtistImages> artistImages) {
        this.context = context;
        this.artistImages = artistImages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.artist_items, parent, false);
        return new CellFeedViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
        bindDefaultFeedItem(position, holder);
    }

    private void bindDefaultFeedItem(int position, CellFeedViewHolder holder) {

        ArtistImages images = artistImages.get(position);
        holder.imageView.setImageResource(images.getArtistImages());
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return artistImages.size();
    }

    private static class CellFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        CellFeedViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.artist_swipe_imageview);

        }
    }


}
