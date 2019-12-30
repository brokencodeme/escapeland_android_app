package com.escapeland.app;

public class MeetsUpsContent {

    int meetsUpsImage;
    int meetsUpsBlurBg;


    public MeetsUpsContent(int meetsUpsBlurBg,int meetsUpsImage) {
        this.meetsUpsBlurBg = meetsUpsBlurBg;
        this.meetsUpsImage = meetsUpsImage;
    }


    public int getMeetsUpsBlurBg() {
        return meetsUpsBlurBg;
    }

    public void setMeetsUpsBlurBg(int meetsUpsBlurBg) {
        this.meetsUpsBlurBg = meetsUpsBlurBg;
    }

    public int getMeetsUpsImage() {
        return meetsUpsImage;
    }

    public void setMeetsUpsImage(int meetsUpsImage)
    {
        this.meetsUpsImage = meetsUpsImage;
    }
}
