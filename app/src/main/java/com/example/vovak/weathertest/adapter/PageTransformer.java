package com.example.vovak.weathertest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.vovak.weathertest.ChangeInterface;

/**
 * Created by vovak on 28.04.2018.
 */

public class PageTransformer implements ViewPager.PageTransformer{
    private int baseElevation;
    private int raisingElevation;
    private float smallerScale;

    public PageTransformer(int baseElevation, int raisingElevation, float smallerScale, Context context) {
        this.baseElevation = baseElevation;
        this.raisingElevation = raisingElevation;
        this.smallerScale = smallerScale;
    }

    @Override
    public void transformPage(View page, float position) {
        float absPosition = Math.abs(position);

        if (absPosition >= 1) {
            page.setElevation(baseElevation);
            page.setScaleY(smallerScale);
        } else {
            page.setElevation(((1 - absPosition) * raisingElevation + baseElevation));
            page.setScaleY((smallerScale - 1) * absPosition + 1);
        }
    }
}
