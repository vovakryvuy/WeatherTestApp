package com.example.vovak.weathertest.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.vovak.weathertest.DetailWeatherActivity;
import com.example.vovak.weathertest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.example.vovak.weathertest.SearchCityActivity.EXTRA_CITY_ID;
import static com.example.vovak.weathertest.SearchCityActivity.EXTRA_STYLE_NUMBER;

/**
 * Created by vovak on 01.05.2018.
 */

public class TaskSearchCity extends AsyncTask<Void, Void, Integer> {
    private Context mContext;
    private String mNameCity;
    private Integer mIdcity;
    private ProgressDialog mProgressDialog;
    private Integer mNumberStyle;

    public TaskSearchCity(Context context, String nameCity,Integer numberStyle) {
        this.mContext = context;
        this.mNameCity = nameCity.replaceAll("\\s+","");
        this.mNumberStyle = numberStyle;
        this.mIdcity = null;
        this.mProgressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage(mContext.getResources()
                .getString(R.string.string_progres_dialog_wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        String json;
        try {
            InputStream inputStream = mContext.getAssets().open("citylist.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("name");
                if(name.equalsIgnoreCase(mNameCity)){
                    mIdcity = object.getInt("id");
                    return mIdcity;
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return mIdcity;
    }

    @Override
    protected void onPostExecute(Integer idCity) {
        super.onPostExecute(idCity);
        mProgressDialog.dismiss();
        if (mIdcity == null){
            Toast toast = Toast.makeText(mContext,
                    R.string.string_city_not_found,
                    Toast.LENGTH_LONG);
            toast.show();
        }else {
            Intent intent = new Intent(mContext,DetailWeatherActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(EXTRA_CITY_ID,idCity);
            intent.putExtra(EXTRA_STYLE_NUMBER,mNumberStyle);
            mContext.startActivity(intent);
        }
    }
}
