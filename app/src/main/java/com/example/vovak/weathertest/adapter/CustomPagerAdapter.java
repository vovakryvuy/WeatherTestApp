package com.example.vovak.weathertest.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vovak.weathertest.ChangeInterface;
import com.example.vovak.weathertest.R;
import com.example.vovak.weathertest.model_weather.GetDataFromRealm;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vovak on 27.04.2018.
 */

public class CustomPagerAdapter extends PagerAdapter implements ChangeInterface {
    private ImageView mImageView;
    private TextView mTextViewDescribe,mTextViewTemp,mTextViewDorN;
    private List<String> mDescribeList;
    private List<String> mTempList;
    private List<String> mUrlIconList;
    private List<String> mDescribeNightList;
    private List<String> mTempNightList;
    private List<String> mUrlIconNightList;

    boolean mTurn = true;
    private Context mContext;

    public CustomPagerAdapter(Context context) {
        this.mContext = context;
        mDescribeList = new ArrayList<>();
        mTempList = new ArrayList<>();
        mUrlIconList = new ArrayList<>();
        mDescribeNightList = new ArrayList<>();
        mTempNightList = new ArrayList<>();
        mUrlIconNightList = new ArrayList<>();
        getDataForView();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_pager,container,false);
        container.addView(view);
        bindDataView(position,view);
        return view;
    }

    private void bindDataView(final int position, View view) {
        mTextViewDescribe = view.findViewById(R.id.text_view_weather);
        mTextViewTemp = view.findViewById(R.id.text_view_temperature);
        mImageView = view.findViewById(R.id.image_icon_weather);
        mTextViewDorN = view.findViewById(R.id.text_view_day_and_night);
        mTextViewDorN.setText(mContext.getResources().getString(R.string.string_day));
        mTextViewDescribe.setText(mDescribeList.get(position));
        String temp = mTempList.get(position).substring(0,2);
        mTextViewTemp.setText(String.format("%s°C", temp));
        getImageOnURL(mImageView,mUrlIconList.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("Adapter", "onClick: view");
                turnCard(view,position);
            }
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mDescribeList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    private void getDataForView() {
        GetDataFromRealm dataFromRealm = new GetDataFromRealm();
        if(dataFromRealm.getDiscribeDay().isEmpty()){
            mTempList.add("wait");
            mDescribeList.add("Wait");
            mUrlIconList.add(null);
            mDescribeNightList.add("Wait");
            mTempNightList.add("wait");
            mUrlIconNightList.add(null);
        }else {
            mTempList = dataFromRealm.getTemperatureDay();
            mDescribeList = dataFromRealm.getDiscribeDay();
            mUrlIconList = dataFromRealm.getUrlIconDay();
            mDescribeNightList = dataFromRealm.getDiscribeNight();
            mTempNightList = dataFromRealm.getTemperatureNight();
            mUrlIconNightList = dataFromRealm.getUrlIconNight();
        }
    }

    private void getImageOnURL(ImageView imageView,String URL){
        Drawable drawableError = mContext.getDrawable(R.drawable.ic_error_outline_black_24dp);
        Drawable drawableHolder = mContext.getDrawable(R.drawable.ic_texture_black_24dp);
        Picasso.with(mContext)
                .load(URL)
                .error(drawableError)
                .into(imageView);

    }

    public void turnCard(final View view,final int position){
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setDuration(400);
        oa2.setDuration(400);
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                changeDataafterTurn(view,position);
                Log.d("Adapter", "onAnimationEnd: view");
                oa2.start();
            }
        });
        oa1.start();
    }

    private void changeDataafterTurn(View view,int position) {
        TextView textViewDescribe,textViewTemp,textViewDorN;
        ImageView imageViewIcon;

        textViewDescribe = (TextView) view.findViewById(R.id.text_view_weather);
        textViewTemp = (TextView) view.findViewById(R.id.text_view_temperature);
        textViewDorN = (TextView) view.findViewById(R.id.text_view_day_and_night);
        imageViewIcon = (ImageView) view.findViewById(R.id.image_icon_weather);

        if (mTurn){
            textViewDorN.setText(mContext.getResources().getString(R.string.string_night));
            textViewDescribe.setText(mDescribeNightList.get(position));
            String temp = mTempNightList.get(position).substring(0,2);
            textViewTemp.setText(String.format("%s°C", temp));
            getImageOnURL(imageViewIcon,mUrlIconNightList.get(position));
            mTurn = false;
        }else{
            textViewDorN.setText(mContext.getResources().getString(R.string.string_day));
            textViewDescribe.setText(mDescribeList.get(position));
            String temp = mTempList.get(position).substring(0,2);
            textViewTemp.setText(String.format("%s°C", temp));
            getImageOnURL(imageViewIcon,mUrlIconList.get(position));
            mTurn = true;
        }

    }

    @Override
    public void changeTurn(boolean turn) {
        mTurn = true;
    }
}
