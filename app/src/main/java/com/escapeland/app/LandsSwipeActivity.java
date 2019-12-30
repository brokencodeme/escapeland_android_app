package com.escapeland.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.internal.IAccountAccessor;

import java.util.ArrayList;
import java.util.List;

public class LandsSwipeActivity extends AppCompatActivity {

    private ViewPager lands_viewpager;
    private PagerAdapter pagerAdapter;
    ImageView blur_bg_lands_imageview;
    List<LandsSwipeLayout> landsSwipeLayouts;
    TextView lands_desp;
    TextView lands_name;
    Button swipe_btn, back_btn;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lands);

        setWindowsFlags();
        hideNavigation();
        blur_bg_lands_imageview = (ImageView) findViewById(R.id.blur_bg_lands_imageview);
        final List<Fragment> lands_fragments = new ArrayList<>();
        landsSwipeLayouts = new ArrayList<>();
        lands_viewpager = findViewById(R.id.lands_viewpager);
        lands_desp = findViewById(R.id.lands_desp);
        lands_name = findViewById(R.id.lands_name);
        back_btn = findViewById(R.id.back_button);



        lands_fragments.add(new LandPage2Fragment());
        lands_fragments.add(new LandPage1Fragment());
        lands_fragments.add(new FoodsLandFragment());
        lands_fragments.add(new GamersLandsFragment());
        lands_fragments.add(new ArtisticLandFragment());
        lands_fragments.add(new EntertainmentLandFragment());


        // set bg blur image
        landsSwipeLayouts.add(new LandsSwipeLayout("A place for all the Designers, Handi-Crafted and beautiful products. Hand-picked stalls from the streets of Vadodara.", "POP UP", R.drawable.pop_land_blur));
        landsSwipeLayouts.add(new LandsSwipeLayout(" Kids Land offer lots of fun and things to do for the younger members of the family", "kIDS", R.drawable.lands_two_blur));
        landsSwipeLayouts.add(new LandsSwipeLayout("Ditch all your diets and roll up your sleeves. Escapeland will be home for all the Foodholic people. There would be food and dessert brands and microbreweries too. Don’t miss out on those home chefs and bakers selling amazing stuff." +
                "P.s. come empty stomach!", "FOOD", R.drawable.foods_land_blur));
        landsSwipeLayouts.add(new LandsSwipeLayout("\n" +
                "Re-live your childhood or just bring the child in you out. Escapeland has a countless carnival, board, and adult games for you. Big Size Snake & Ladder, Chess, Ludo, are just a few of them", "GAMERS ", R.drawable.gamers_land_blur));
        landsSwipeLayouts.add(new LandsSwipeLayout("One can find several works of artists from local art. There would be live art, art for a cause, and a collaborative art section here. We try to bring in the artists who are unique and believe in spreading their thoughts through the arts and workshops", "ARTISTIC ", R.drawable.artistic_land_blur));
        landsSwipeLayouts.add(new LandsSwipeLayout("Duh! How we can forget live concerts we are bringing up the best artist for you. Stand-Up Comedian, Concerts, Open Mics kidsy,pop up land,entertainment am reframing\n", "ENTERTAINMENT", R.drawable.entertainment_land_blur));


        blur_bg_lands_imageview.setImageResource(landsSwipeLayouts.get(0).getLands_images());
        lands_desp.setText(landsSwipeLayouts.get(0).getLands_desp());
        lands_name.setText(landsSwipeLayouts.get(0).getLands_name());

        pagerAdapter = new LandsSwipeAdapter(getSupportFragmentManager(), lands_fragments);
        lands_viewpager.setAdapter(pagerAdapter);

        lands_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                blur_bg_lands_imageview.setImageResource(landsSwipeLayouts.get(position).getLands_images());
                lands_desp.setText(landsSwipeLayouts.get(position).getLands_desp());
                lands_name.setText(landsSwipeLayouts.get(position).getLands_name());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("land-clicked", "clicked button");
                finish();
            }
        });
        setUpPopUp();
    }


    public void setWindowsFlags(){
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

    public void setUpPopUp()
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        boolean land_popup_once_value = sharedPref.getBoolean("IS_LAND_POPUP_ONCE", true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.lands_custom_popup);

        if (land_popup_once_value )
        {
            dialog.show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("IS_LAND_POPUP_ONCE", false);
            editor.commit();
            boolean new_popup_value = sharedPref.getBoolean("IS_LAND_POPUP_ONCE", false);
        }

        TextView close_btn = dialog.findViewById(R.id.popup_close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
