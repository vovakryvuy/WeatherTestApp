package com.example.vovak.weathertest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.vovak.weathertest.adapter.CustomPagerAdapter;
import com.example.vovak.weathertest.adapter.PageTransformer;
import com.example.vovak.weathertest.adapter_navigation_menu.AdapterHistoryMenu;
import com.example.vovak.weathertest.model_response.ModelDayWeatherForecast;
import com.example.vovak.weathertest.model_response.ModelWeather;
import com.example.vovak.weathertest.model_weather.Data;
import com.example.vovak.weathertest.model_weather.DataHistory;
import com.example.vovak.weathertest.model_weather.DayWeather;
import com.example.vovak.weathertest.model_weather.GetDataFromRealm;
import com.example.vovak.weathertest.model_weather.NightWeather;
import com.example.vovak.weathertest.retrofit.ServiceRetrofit;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Share;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vovak on 26.04.2018.
 */

public class DetailWeatherActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private static final String TAG = "DetailWeatherActivity";
    private ChangeInterface changeInterface;

    private Window mWindow;
    private Integer mStyleNumber = 1;
    private String mStringShare;
    private Integer mCurrentPage = 1;
    private Integer mCityId;
    private String mCityName = "";
    private TextView mTextViewDate,mTextViewUserName,mTextViewUserEmail;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mConstraintLayout;
    private CustomPagerAdapter mCustomPagerAdapter;
    private Realm mRealm;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen);
        setColorInstatusBar();
        Intent intent = getIntent();
        mCityId = intent.getIntExtra(SearchCityActivity.EXTRA_CITY_ID,0);
        mStyleNumber = intent.getIntExtra(SearchCityActivity.EXTRA_STYLE_NUMBER,1);
        mRealm = Realm.getDefaultInstance();
        requestWetherDetail(mCityId);
        initView();
    }

    private void setColorInstatusBar() {
        mWindow = this.getWindow();
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        mWindow.setStatusBarColor(ContextCompat.getColor(this,R.color.color_1_first));
    }

    private void initView() {
        mTextViewDate = (TextView) findViewById(R.id.text_view_date);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_menu_navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.layout_detail_screen);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        changeInterface = (ChangeInterface) mCustomPagerAdapter;
        mCustomPagerAdapter.notifyDataSetChanged();
        PageTransformer pageTransformer = new PageTransformer(
                30,
                32,
                0.5f,
                this);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setPageTransformer(false,pageTransformer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(new AdapterHistoryMenu(this));
        ImageView buttonCloseNavigationMenu = findViewById(R.id.button_close_menu);
        buttonCloseNavigationMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getBackground().setAlpha(0);

        mTextViewUserName = (TextView) findViewById(R.id.text_view_menu_name_user);
        mTextViewUserEmail = (TextView) findViewById(R.id.text_view_menu_email_user);
        CardView loginButton = findViewById(R.id.logout_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                if (!isLoggedIn()){
                    Intent intent = new Intent(DetailWeatherActivity.this,MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        setColorStyle(mStyleNumber);
        setInformationAboutUser();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                ShareDialog shareDialog = new ShareDialog(this);
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setQuote(mStringShare)
                            .setContentUrl(Uri.parse("https://weather.com/"))
                            .build();
                    shareDialog.show(linkContent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean isLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null;
        Log.d(TAG, "onSuccess: isLoggendIn = "+isLoggedIn);
        return isLoggedIn;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.munu_tool_bar, menu);
        MenuItem item = menu.findItem(R.id.action_share);

        return super.onCreateOptionsMenu(menu);
    }

    private void setColorStyle(Integer mStyleNumber) {
        Drawable drawable = getDrawable(R.drawable.gradient_style_1);
        int color = getResources().getColor(R.color.color_1_first);
        switch (mStyleNumber){
            case 1:
                drawable = getDrawable(R.drawable.gradient_style_1);
                color = getResources().getColor(R.color.color_1_first);
                break;
            case 2:
                drawable = getDrawable(R.drawable.gradient_style_2);
                color = getResources().getColor(R.color.color_2_first);
                break;
            case 3:
                drawable = getDrawable(R.drawable.gradient_style_3);
                color = getResources().getColor(R.color.color_3_first);
                break;
            case 4:
                drawable = getDrawable(R.drawable.gradient_style_4);
                color = getResources().getColor(R.color.color_4_first);
                break;
            case 5:
                drawable = getDrawable(R.drawable.gradient_style_5);
                color = getResources().getColor(R.color.color_5_first);
                break;
            case 6:
                drawable = getDrawable(R.drawable.gradient_style_6);
                color = getResources().getColor(R.color.color_6_first);
                break;
            case 7:
                drawable = getDrawable(R.drawable.gradient_style_7);
                color = getResources().getColor(R.color.color_7_first);
                break;
            default:
                Log.d(TAG, "setColorStyle: THIS NUMBER NO STYLE");
                break;
        }
        mWindow.setStatusBarColor(color);
        mToolbar.setBackgroundColor(color);
        mConstraintLayout.setBackground(drawable);
    }

    private void setInformationAboutUser() {
        SharedPreferences preferences =  getSharedPreferences(MainActivity.MY_PRESERENCES,Context.MODE_PRIVATE);
        String userName = preferences.getString(MainActivity.KEY_SHARED_PREFERENCES_NAME,"");
        String userEmail = preferences.getString(MainActivity.KEY_SHARED_PREFERENCES_EMAIL,"");
        mTextViewUserName.setText(userName);
        mTextViewUserEmail.setText(userEmail);
    }

    public void deleteAllInform(){
       SharedPreferences preferences =  getSharedPreferences(MainActivity.MY_PRESERENCES,Context.MODE_PRIVATE);
       preferences.edit().remove(MainActivity.KEY_SHARED_PREFERENCES_EMAIL).apply();
       preferences.edit().remove(MainActivity.KEY_SHARED_PREFERENCES_NAME).apply();
       mRealm.where(Data.class).findAll().deleteAllFromRealm();
    }


    private void requestWetherDetail(Integer cityId) {
        if(cityId != 0)
                ServiceRetrofit.getApi().getCityByName(cityId,"metric", ServiceRetrofit.API)
                        .enqueue(new Callback<ModelDayWeatherForecast>() {
                    @Override
                    public void onResponse(Call<ModelDayWeatherForecast> call, Response<ModelDayWeatherForecast> response) {
                        Log.d(TAG, "onResponse: "+response.message());
                        parseWetherForecastDay(response.body());
                    }

                    @Override
                    public void onFailure(Call<ModelDayWeatherForecast> call, Throwable t) {
                        Log.d(TAG, "onResponse: "+t.getMessage());
                    }
                });
    }

    private void parseWetherForecastDay(ModelDayWeatherForecast dayWeatherForecast){
        Data data;
        List<ModelWeather> modelWeatherList = dayWeatherForecast.getItems();
        RealmList<DayWeather> dayWeatherList;
        RealmList<NightWeather> nightWeatherList;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd/MM/yyyy");
        data= new Data();
        dayWeatherList = new RealmList<>();
        nightWeatherList = new RealmList<>();

        mCityName = dayWeatherForecast.getCity();
        data.setNameCity(mCityName);
        mToolbar.setTitle("city - " + mCityName);



        for (ModelWeather modelWeather : modelWeatherList) {
            Calendar calendar = modelWeather.getDate();
            if (calendar.getTime().getHours() == 12){
                DayWeather dayWeather = new DayWeather();
                dayWeather.setDate(dateFormat.format(calendar.getTime()));
                dayWeather.setMaxTemp(modelWeather.getTempMax());
                dayWeather.setMinTemp(modelWeather.getTempMin());
                dayWeather.setDescribe(modelWeather.getDesctiption());
                dayWeather.setUrlIcon(modelWeather.getIconUrl());
                dayWeatherList.add(dayWeather);
            }
            if (calendar.getTime().getHours() == 0){
                NightWeather nightWeather = new NightWeather();
                nightWeather.setDate(dateFormat.format(calendar.getTime()));
                nightWeather.setMaxTemp(modelWeather.getTempMax());
                nightWeather.setMinTemp(modelWeather.getTempMin());
                nightWeather.setDescribe(modelWeather.getDesctiption());
                nightWeather.setUrlIcon(modelWeather.getIconUrl());
                nightWeatherList.add(nightWeather);
            }
        }

        if (!dayWeatherList.isEmpty() && !nightWeatherList.isEmpty()){
            data.setDayWeatherList(dayWeatherList);
            data.setNightWeatherList(nightWeatherList);
            mTextViewDate.setText(dateFormat.format(modelWeatherList
                    .get(0).getDate().getTime()));
            saveDataInRealm(data);
            saveDataHistoryInRealm();
            mCustomPagerAdapter.notifyDataSetChanged();
            mStringShare = "Місто: "+mCityName+", дата: " +
                    dateFormat.format(modelWeatherList.get(mCurrentPage).getDate().getTime())+
                    ", день: "+dayWeatherList.get(mCurrentPage).getMaxTemp()+
                    " ("+dayWeatherList.get(mCurrentPage).getDescribe()+"), ніч "+
                    nightWeatherList.get(mCurrentPage).getMaxTemp()+" ("+
                    nightWeatherList.get(mCurrentPage).getDescribe()+")";
        }
    }

    private void saveDataInRealm(final Data dataIn) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(realm.where(Data.class).findAll() == null) {
                     realm.createObject(Data.class,
                             UUID.randomUUID().toString());
                    Log.d(TAG, "execute: Create Data in Realm");
                }
                realm.copyToRealmOrUpdate(dataIn);
                Log.d(TAG, "execute: Realm save Data");
            }
        });
        mViewPager.setAdapter(new CustomPagerAdapter(this));
    }

    private void saveDataHistoryInRealm(){
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm.where(DataHistory.class).findAll() == null){
                    realm.createObject(DataHistory.class);
                }
                DataHistory dataHistory = new DataHistory();
                dataHistory.setCityName(mCityName);
                dataHistory.setIdCity(mCityId);
                dataHistory.setCurrentDate(dateFormat
                        .format(Calendar.getInstance().getTime()));
                dataHistory.setNumberstyle(mStyleNumber);
                realm.insert(dataHistory);

                if(realm.where(DataHistory.class).findAll().size() > 10){
                   realm.where(DataHistory.class).findAll().deleteLastFromRealm();
                }
            }
        });
        Log.d(TAG, "saveDataHistoryInRealm: ");
    }

    private void setTextDate() {
        GetDataFromRealm  getDataFromRealm = new GetDataFromRealm();
        if (!getDataFromRealm.getDate().isEmpty())
            mTextViewDate.setText(String.valueOf(new GetDataFromRealm()
                    .getDate().get(mViewPager.getCurrentItem())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mRealm.isClosed())
            mRealm.close();

    }

    public static int dpToPixels(int dp, Context  context){
        return (int) (dp * (context.getResources().getDisplayMetrics().density));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        mTextViewDate.setText(String.valueOf(
                new GetDataFromRealm()
                        .getDate()
                        .get(position)
        ));
        changeInterface.changeTurn(true);
        mCurrentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
