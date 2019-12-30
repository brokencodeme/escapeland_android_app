package com.escapeland.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.escapeland.app.R;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;

public class ArtistImagesActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    RecyclerView artist_view_pager;
    ArtistImageSwipeAdapter artistImageSwipeAdapter;
    List<ArtistImages> artistImagesList;
    List<ArtistSwipeContent> artistSwipeContents;
    ImageView bg_artist_swipe_imageview;
    TextView follow_his_or_her;
    String yoututbe_url = "https://www.youtube.com/channel/UCgJeCzkU4zF40NxVPrglo6w";
    String instagram_url = "https://www.instagram.com/aranyajohar/ ";
    String facebook_url = "https://www.facebook.com/helloaranya/";
    ImageView artist_swipe_imageviews;
    Button artist_back_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_container);

        artist_view_pager = (RecyclerView)findViewById(R.id.artist_view_pager);
        follow_his_or_her = (TextView)findViewById(R.id.follow_his_or_her);
        artist_back_button = findViewById(R.id.artist_back_button);
        setWindowsFlags();
        hideNavigation();
        setSystemVisibilityChangeTasks();

        addArtistImages();
        addArtistContent();
        scrollHandling();


        artist_back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("clicked", "clicked button");
                finish();
            }
        });

    }

    public void addArtistContent(){
        artistSwipeContents = new ArrayList<>();

        artistSwipeContents.add( new ArtistSwipeContent(
                "FOLLOW HER ON",
                R.drawable.aranya_blur_bg,
                "Aranya Johar",
                "Aranya is an Indian poet and a feminist. She uses social media to address issues like gender equality, mental health, and body positivity",
                "https://www.facebook.com/helloaranya/",
                "https://www.youtube.com/channel/UCgJeCzkU4zF40NxVPrglo6w",
                "https://www.instagram.com/aranyajohar/"
        ));

        artistSwipeContents.add( new ArtistSwipeContent(
                "FOLLOW HER ON",
                R.drawable.kaatusha_blur_bg,

                "Kaatusha ",
                "Katusha Svoboda's is an International Dj, Sound Producer, Singer, Composer & Songwriter ",
                "https://www.facebook.com/katushasvoboda/",
                "https://www.youtube.com/channel/UC4_DjsnkEpWex_VMtU0hLJA",
                "https://www.instagram.com/katushasvoboda/"
                ));

        artistSwipeContents.add( new ArtistSwipeContent(
                "FOLLOW HIM ON",
                R.drawable.knox_blur_bg,
                "Knox Artiste",
                        "Knox is a Singer, Songwriter, Music Producer, and Performer",
                        " https://www.facebook.com/KnoxArtiste/",
                        "https://www.youtube.com/user/KnoxArtiste",
                        "https://www.instagram.com/iamknoxartiste/"
        ));

        artistSwipeContents.add( new ArtistSwipeContent(
                "FOLLOW HIM ON",
                R.drawable.ritvik_blur_bg,
                "Ritviz",
                "Ritviz is a DJ, singer and Hindustani dance music producer.",
                "https://facebook.com",
                "https://youtube.com",
                "https://instagram.com"
        ));

        artistSwipeContents.add( new ArtistSwipeContent(
                "FOLLOW HIM ON",
                R.drawable.vaibhav_blur_bg,
                "Vaibhav Shetia",
                "V for Vaibhav, V for variety. From smokers, suicide and solitude, and design of cockroaches and humans, and cats, to vegetarians, pilots and bikers, comedian Vaibhav Sethia's stand-up sets cover all possible topics with his jokes and stories",
                "https://www.facebook.com/Vaibhav-Sethia-1126746454051774/",
                " https://www.youtube.com/user/vibesuap",
                "https://www.instagram.com/vaibhav_sethia/"
        ));

        artistSwipeContents.add( new ArtistSwipeContent(
                "FOLLOW HIM ON",
                R.drawable.yaha_blur_bg,
                "Yahya Bootwala",
                "Yahya is an Indian actor, writer and poet whose 2017 work Shayad Wo Pyaar Nahi went viral on social media",
                "https://www.facebook.com/YahyaBootwalaOfficial/",
                "https://www.youtube.com/channel/UCPh9O59DttbiKQX9VmMXS9g",
                "https://www.instagram.com/yahyabootwala/"
        ));
    }



    public void addArtistImages(){

        artistImagesList = new ArrayList<>();

        artistImagesList.add(new ArtistImages(R.drawable.aranya_swipe_img));
        artistImagesList.add(new ArtistImages(R.drawable.kaatusha_swipe_img));
        artistImagesList.add(new ArtistImages(R.drawable.knox_swipe_img));
        artistImagesList.add(new ArtistImages(R.drawable.ritvik_swipe_img));
        artistImagesList.add(new ArtistImages(R.drawable.vaibhav_swipe_img));
        artistImagesList.add(new ArtistImages(R.drawable.yaha_swipe_img));

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        artist_view_pager.setLayoutManager(linearLayoutManager);
        artist_view_pager.setHasFixedSize(true);
        artistImageSwipeAdapter = new ArtistImageSwipeAdapter(this, artistImagesList);
        artist_view_pager.setAdapter(artistImageSwipeAdapter);

    }

    public void scrollHandling(){

        // adding first value to container
        final ImageView bg_artist_swipe_imageview = findViewById(R.id.bg_artist_swipe_imageview);
        final TextView artist_name = findViewById(R.id.artist_name);
        final TextView artist_info = findViewById(R.id.artist_info);

        // setting inital values
        bg_artist_swipe_imageview.setBackgroundResource(artistSwipeContents.get(0).getArtist_blur_image());

        artist_name.setText(artistSwipeContents.get(0).getName());
        artist_info.setText(artistSwipeContents.get(0).getArtist_info());
        follow_his_or_her.setText(artistSwipeContents.get(0).getFollowHisOrHer());

        final SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(artist_view_pager);


        // creating recycleview object
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yoututbe_url = "";
                RecyclerView.ViewHolder recyclerView = artist_view_pager.findViewHolderForAdapterPosition(0);
                ImageView scrolling_img = recyclerView.itemView.findViewById(R.id.artist_swipe_imageview);
                scrolling_img.animate().setDuration(550).alpha(1).start();
            }
        }, 550);


        artist_view_pager.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState==RecyclerView.SCROLL_STATE_IDLE){

                    View scrollingView = snapHelper.findSnapView(linearLayoutManager);
                    int pos = linearLayoutManager.getPosition(scrollingView);

                    RecyclerView.ViewHolder viewHolder = artist_view_pager.findViewHolderForAdapterPosition(pos);
                    ImageView artist_swipe_imageview = viewHolder.itemView.findViewById(R.id.artist_swipe_imageview);
                    artist_swipe_imageview.animate().alpha(1).setDuration(550).start();

                    bg_artist_swipe_imageview.setBackgroundResource(artistSwipeContents.get(pos).getArtist_blur_image());
                    artist_name.setText(artistSwipeContents.get(pos).getName());
                    follow_his_or_her.setText(artistSwipeContents.get(pos).getFollowHisOrHer());
                    artist_info.setText(artistSwipeContents.get(pos).getArtist_info());

                    yoututbe_url = artistSwipeContents.get(pos).getGoogle_url();
                    instagram_url = artistSwipeContents.get(pos).getInstagram_url();
                    facebook_url = artistSwipeContents.get(pos).getFacebook_url();

                }else {

                    View scrollingView = snapHelper.findSnapView(linearLayoutManager);
                    int pos = linearLayoutManager.getPosition(scrollingView);

                    RecyclerView.ViewHolder viewHolder = artist_view_pager.findViewHolderForAdapterPosition(pos);
                    ImageView artist_swipe_imageview = viewHolder.itemView.findViewById(R.id.artist_swipe_imageview);

                    bg_artist_swipe_imageview.setBackgroundResource(artistSwipeContents.get(pos).getArtist_blur_image());
                    artist_swipe_imageview.animate().alpha(.4f).setDuration(550).start();

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.d("scrolled", "offset= "+dx + " "+" "+dy    );
            }
        });


        // socials buttons initialization

        Button youtube_icon_btn = findViewById(R.id.youtube_btn);
        Button facebook_icon_btn = findViewById(R.id.facebook_btn);
        Button instagram_icon_btn = findViewById(R.id.instagram_btn);
        youtube_icon_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.d("url", yoututbe_url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(yoututbe_url));
                startActivity(i);
            }
        });

        facebook_icon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(facebook_url));
                startActivity(i);
            }
        });

        instagram_icon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(instagram_url));
                startActivity(i);
            }
        });
    }
    public void setWindowsFlags(){
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES,WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
    }

    public void setSystemVisibilityChangeTasks(){

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){

            @Override
            public void onSystemUiVisibilityChange(int visibility)
            {
                if(visibility==0){
                    Log.d("visibility", " "+visibility+" data-"+View.SYSTEM_UI_FLAG_FULLSCREEN+" "+View.SYSTEM_UI_FLAG_HIDE_NAVIGATION+" "+View.SYSTEM_UI_FLAG_IMMERSIVE);
                    Handler softkeyHandler = new Handler();
                    softkeyHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
                        }
                    }, 2000);
                }
            }
        });
    }

    public void hideNavigation(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

}
