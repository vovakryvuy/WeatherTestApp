package com.example.vovak.weathertest.adapter_navigation_menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vovak.weathertest.R;
import com.example.vovak.weathertest.model_weather.Data;
import com.example.vovak.weathertest.model_weather.DataHistory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by vovak on 30.04.2018.
 */

public class AdapterHistoryMenu extends RecyclerView.Adapter<AdapterHistoryMenu.ViewHolder> {
    public List<String> mCityName;
    public List<String> mDate;
    public List<Integer> mNumberstyle;
    public Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewDate,mTextViewCity;
        public ImageView mImageViewMore;
        public CardView mCardView;

        public ViewHolder(View v) {
            super(v);
            mTextViewCity = (TextView) v.findViewById(R.id.text_view_card_history_city);
            mTextViewDate = (TextView) v.findViewById(R.id.text_view_card_history_date);
            mImageViewMore = (ImageView) v.findViewById(R.id.text_view_card_history_button);
            mCardView = (CardView) v.findViewById(R.id.card_view_history);
        }
    }

    public AdapterHistoryMenu(Context context) {
        this.mContext = context;
        mCityName = new ArrayList<>();
        mDate = new ArrayList<>();
        mNumberstyle = new ArrayList<>();
        getDateFromRealm();
    }

    private void getDateFromRealm() {
        Realm realm = Realm.getDefaultInstance();
        if(realm.where(DataHistory.class).findAll() != null){
            for (DataHistory dataHistory : realm.where(DataHistory.class).findAll()) {
                mCityName.add(dataHistory.getCityName());
                mDate.add(dataHistory.getCurrentDate());
                mNumberstyle.add(dataHistory.getNumberstyle());
            }
        }else {
            mCityName.add("");
            mDate.add("");
            mNumberstyle.add(0);
        }
    }

    @Override
    public AdapterHistoryMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_history_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextViewCity.setText(mCityName.get(position));
        holder.mTextViewDate.setText(mDate.get(position));
        holder.mCardView.setBackground(setStyleCard(mNumberstyle.get(position)));
    }

    @Override
    public int getItemCount() {
        return mCityName.size();
    }

    private Drawable setStyleCard(Integer idStyle){
        Drawable drawable;
        switch (idStyle){
            case 1:
                drawable = mContext.getDrawable(R.drawable.gradient_style_1);
                break;
            case 2:
                drawable = mContext.getDrawable(R.drawable.gradient_style_2);
                break;
            case 3:
                drawable = mContext.getDrawable(R.drawable.gradient_style_3);
                break;
            case 4:
                drawable = mContext.getDrawable(R.drawable.gradient_style_4);
                break;
            case 5:
                drawable = mContext.getDrawable(R.drawable.gradient_style_5);
                break;
            case 6:
                drawable = mContext.getDrawable(R.drawable.gradient_style_6);
                break;
            case 7:
                drawable = mContext.getDrawable(R.drawable.gradient_style_7);
                break;
            default:
                drawable = mContext.getDrawable(R.drawable.gradient_style_7);
                break;
        }
        return drawable;
    }
}
