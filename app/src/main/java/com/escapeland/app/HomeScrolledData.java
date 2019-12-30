package com.escapeland.app;

import android.net.Uri;

public class HomeScrolledData {

    boolean is_sliding_content_video;
    Uri video_data;
    int image_data;

    public HomeScrolledData(boolean is_sliding_content_video, Uri video_data, int image_data) {
        this.is_sliding_content_video = is_sliding_content_video;
        this.video_data = video_data;
        this.image_data = image_data;
    }

    public boolean getIs_sliding_content_video() {
        return is_sliding_content_video;
    }

    public void setIs_sliding_content_video(boolean is_sliding_content_video) {
        this.is_sliding_content_video = is_sliding_content_video;
    }

    public Uri getVideo_data() {
        return video_data;
    }

    public void setVideo_data(Uri video_data) {
        this.video_data = video_data;
    }

    public int getImage_data() {
        return image_data;
    }

    public void setImage_data(int image_data) {
        this.image_data = image_data;
    }
}
