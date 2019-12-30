package com.escapeland.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.escapeland.app.R;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment {

    ImageView meetsups_bg;
    TextView hash_tag;
    LinearLayoutManager linearLayoutManager;
    RecyclerView attraction_recycleview;
    AttractionContentsAdapters attractionContentsAdapters;
    List<AttractionContents> attractionContents;
    ImageView attraction_blur_bg_view;
    CardView attraction_cardview;
    TextView artist_textivew;
    ImageView lands_btn_view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_category, container,false);

        attraction_recycleview = view.findViewById(R.id.recyleview_attraction);
        attraction_blur_bg_view = view.findViewById(R.id.attraction_blur_bg_view);
        attraction_cardview = view.findViewById(R.id.attraction_cardview);

        setUpAttractionView();
        setUpBlurBackground();


        setOnPageScrolledView();
        setUpClickListner(view);



        return view ;
    }


    public void setUpBlurBackground(){

    }

    public void setUpAttractionView(){
        attractionContents = new ArrayList<>();

        attractionContents.add(new AttractionContents(R.drawable.attraction_one));
        attractionContents.add(new AttractionContents(R.drawable.attraction_two));
        attractionContents.add(new AttractionContents(R.drawable.attraction_three));
        attractionContents.add(new AttractionContents(R.drawable.attraction_five));
        attractionContents.add(new AttractionContents(R.drawable.attraction_four));
        attractionContents.add(new AttractionContents(R.drawable.attraction_six));


        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        attractionContentsAdapters = new AttractionContentsAdapters(getContext(), attractionContents);

        attraction_recycleview.setLayoutManager(linearLayoutManager);
        attraction_recycleview.setAdapter(attractionContentsAdapters);
        attraction_recycleview.setHasFixedSize(true);

    }

    public void setOnPageScrolledView(){

        // adding snap helper

        final SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(attraction_recycleview);


        // setup inital content to attaction viewattraction_imageview

        attraction_blur_bg_view.setBackgroundResource(attractionContents.get(0).getAttraction_images());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView.ViewHolder recyclerView = attraction_recycleview.findViewHolderForAdapterPosition(0);
                CardView attraction_cardview = recyclerView.itemView.findViewById(R.id.attraction_cardview);
                attraction_cardview.setAlpha(1);
                attraction_cardview.animate().scaleX(1).scaleY(1).setDuration(550).start();
            }
        }, 550);

        attraction_recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState== RecyclerView.SCROLL_STATE_IDLE){

                    View view = snapHelper.findSnapView(linearLayoutManager);
                    int pos = linearLayoutManager.getPosition(view);
                    RecyclerView.ViewHolder viewHolder = attraction_recycleview.findViewHolderForAdapterPosition(pos);
                    CardView attraction_cardview = viewHolder.itemView.findViewById(R.id.attraction_cardview);
                    attraction_blur_bg_view.setBackgroundResource(attractionContents.get(pos).getAttraction_images());
                    attraction_cardview.setAlpha(1);
                    attraction_cardview.animate().scaleX(1).scaleY(1).setDuration(350).start();

                }
                else {

                    View view = snapHelper.findSnapView(linearLayoutManager);
                    int pos = linearLayoutManager.getPosition(view);

                    RecyclerView.ViewHolder viewHolder = attraction_recycleview.findViewHolderForAdapterPosition(pos);
                    CardView attraction_cardview = viewHolder.itemView.findViewById(R.id.attraction_cardview);
                    attraction_blur_bg_view.setBackgroundResource(attractionContents.get(pos).getAttraction_images());
                    attraction_cardview.setAlpha(.4f);
                    attraction_cardview.animate().scaleX(0.7f).scaleY(0.7f).setDuration(550).start();

                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void setUpClickListner(View view){
        ImageView artist_imageview = view.findViewById(R.id.artist_imageview);
        ImageView calendeer_image_view = view.findViewById(R.id.calendeer_image_view);
        lands_btn_view = view.findViewById(R.id.lands_btn);

        lands_btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LandsSwipeActivity.class));
            }
        });

        calendeer_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Schedule.class));

            }
        });

        artist_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ArtistImagesActivity.class));
            }
        });
    }


}
