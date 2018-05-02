package com.example.vovak.weathertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vovak.weathertest.task.TaskSearchCity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vovak on 26.04.2018.
 */

public class SearchCityActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_CITY_ID = "extar_city_id";
    public static final String EXTRA_STYLE_NUMBER = "extar_style_number";
    public static final String KEY_SHARED_PREFERENCES_STYLE = "key_shared_preferences_style";
    private static final String TAG = "SearchCityActivity";
    private int mStyleNumber = 1;
    private Window mWindow;
    private EditText mEditText;
    private Button mButtonFindCity;
    private Button mButtonChooseColor1,mButtonChooseColor2,mButtonChooseColor3,mButtonChooseColor4;
    private Button mButtonChooseColor5,mButtonChooseColor6,mButtonChooseColor7;
    private ConstraintLayout mConstraintLayout;
    private String mCityName;
    private ImageView mImageViewCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_city_screen);

        initEditTextField();

        initView();
    }

    private void setColorInstatusBar() {
        mWindow = this.getWindow();
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        mWindow.setStatusBarColor(ContextCompat.getColor(this,R.color.color_1_first));
    }

    private void initView() {
        setColorInstatusBar();
        mConstraintLayout = findViewById(R.id.layout_search_screen);
        mButtonFindCity = findViewById(R.id.button_find_city);
        mButtonFindCity.setVisibility(View.INVISIBLE);
        mButtonFindCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mCityName.isEmpty()){
                    TaskSearchCity searchCity =
                            new TaskSearchCity(SearchCityActivity.this,mCityName,mStyleNumber);
                    searchCity.execute();
                }
            }
        });
        mButtonChooseColor1 = findViewById(R.id.button_choose_style_1);
        mButtonChooseColor1.setOnClickListener(this);
        mButtonChooseColor2 = findViewById(R.id.button_choose_style_2);
        mButtonChooseColor2.setOnClickListener(this);
        mButtonChooseColor3 = findViewById(R.id.button_choose_style_3);
        mButtonChooseColor3.setOnClickListener(this);
        mButtonChooseColor4 = findViewById(R.id.button_choose_style_4);
        mButtonChooseColor4.setOnClickListener(this);
        mButtonChooseColor5 = findViewById(R.id.button_choose_style_5);
        mButtonChooseColor5.setOnClickListener(this);
        mButtonChooseColor6 = findViewById(R.id.button_choose_style_6);
        mButtonChooseColor6.setOnClickListener(this);
        mButtonChooseColor7 = findViewById(R.id.button_choose_style_7);
        mButtonChooseColor7.setOnClickListener(this);
        mConstraintLayout.setBackground(getDrawable(R.drawable.gradient_style_1));
        int idColor = getResources().getColor(R.color.color_1_first);
        mButtonFindCity.setTextColor(idColor);
        Drawable mDrawable = getResources().getDrawable(R.drawable.ic_check_black_24dp);
        mDrawable.setColorFilter(new
                PorterDuffColorFilter(idColor, PorterDuff.Mode.SRC_IN));
        mImageViewCheck = (ImageView) findViewById(R.id.image_view_check);
        mImageViewCheck.bringToFront();
        mImageViewCheck.setColorFilter(idColor);
    }


    private void initEditTextField(){
        mEditText = findViewById(R.id.edit_text_search_city);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: "+charSequence);
                mCityName = charSequence.toString();
                if (mCityName.isEmpty()){
                    mButtonFindCity.setVisibility(View.INVISIBLE);
                }else {
                    mButtonFindCity.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Drawable drawable;
        int color;
        switch (view.getId()){
            case R.id.button_choose_style_1:
                drawable = getDrawable(R.drawable.gradient_style_1);
                color = getResources().getColor(R.color.color_1_first);
                mStyleNumber = 1;
                break;
            case R.id.button_choose_style_2:
                drawable = getDrawable(R.drawable.gradient_style_2);
                color = getResources().getColor(R.color.color_2_first);
                mStyleNumber = 2;
                break;
            case R.id.button_choose_style_3:
                drawable = getDrawable(R.drawable.gradient_style_3);
                color = getResources().getColor(R.color.color_3_first);
                mStyleNumber = 3;
                break;
            case R.id.button_choose_style_4:
                drawable = getDrawable(R.drawable.gradient_style_4);
                color = getResources().getColor(R.color.color_4_first);
                mStyleNumber = 4;
                break;
            case R.id.button_choose_style_5:
                drawable = getDrawable(R.drawable.gradient_style_5);
                color = getResources().getColor(R.color.color_5_first);
                mStyleNumber = 5;
                break;
            case R.id.button_choose_style_6:
                drawable = getDrawable(R.drawable.gradient_style_6);
                color = getResources().getColor(R.color.color_6_first);
                mStyleNumber = 6;
                break;
            case R.id.button_choose_style_7:
                drawable = getDrawable(R.drawable.gradient_style_7);
                color = getResources().getColor(R.color.color_7_first);
                mStyleNumber = 7;
                break;
            default:
                drawable = getDrawable(R.drawable.gradient_style_1);
                color = getResources().getColor(R.color.color_1_first);
                mStyleNumber = 1;
                break;
        }
        mWindow.setStatusBarColor(color);
        mConstraintLayout.setBackground(drawable);
        mButtonFindCity.setTextColor(color);
        mImageViewCheck.setColorFilter(color);
    }


    private Integer getCityIdFromJson(String nameCity){
        Integer id = null;
        String json;

        try {
            InputStream inputStream = getAssets().open("citylist.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("name");
                if(name.equalsIgnoreCase(nameCity)){
                    id = object.getInt("id");
                    return id;
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initEditTextField();
    }
}
