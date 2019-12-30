package com.escapeland.app;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Schedule extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        context = this;

        setWindowsFlags();
        hideNavigation();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager(), 13, 31);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(null);

        //Auto select tab according to date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        int today_date = Integer.parseInt(formattedDate.split("-")[0]);
        String today_month = formattedDate.split("-")[1];
        if(today_month.equals("Dec") && today_date >= 13  && today_date <= 31){
            viewPager.setCurrentItem(today_date-13);
        }
    }

    public class TabAdapter extends FragmentStatePagerAdapter {
        int mStartDate;
        int mEndDate;
        int msize;

        String data;
        TabAdapter(FragmentManager fm, int StartDate, int EndDate) {
            super(fm);
            this.mStartDate = StartDate;
            this.mEndDate = EndDate;
            this.msize = EndDate - StartDate + 1;

        }

        @Override
        public Fragment getItem(int position) {
            String aaa = loadJSONFromAsset(context);
            data = "";
            try {
                JSONObject obj = new JSONObject(aaa);
                JSONArray arrobj = obj.getJSONArray(String.valueOf(position+1));
                for(int i=0; i<arrobj.length(); i++) {
                    data += arrobj.get(i).toString() + ",";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            return ScheduleFragment.newInstance(position+1);
            return ScheduleFragment.newInstance(data);

        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position + mStartDate);
        }

        @Override
        public int getCount() {
            return msize;
        }

        String loadJSONFromAsset(Context context) {
            String json = null;
            try {
                InputStream is = context.getAssets().open("schedule.json");

                int size = is.available();

                byte[] buffer = new byte[size];

                is.read(buffer);

                is.close();

                json = new String(buffer, "UTF-8");


            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;

        }
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

}
