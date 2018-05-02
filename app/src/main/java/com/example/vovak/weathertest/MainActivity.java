package com.example.vovak.weathertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {
    public  static final String KEY_SHARED_PREFERENCES_NAME = "key_shared_preferences_name";
    public  static final String KEY_SHARED_PREFERENCES_EMAIL = "key_shared_preferences_email";
    public static final String MY_PRESERENCES = "my_preferences";
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String NAME = "name";
    private static final String TAG = "LoginActivity";
    private ConstraintLayout mConstraintLayout;
    private LoginButton mLoginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setColorInstatusBar();
        viewHash();
        callbackManager = CallbackManager.Factory.create();
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.layout_login_screen);
        mConstraintLayout.setBackground(getDrawable(R.drawable.gradient_style_1));
        startNextScreen();
    }

    private void setColorInstatusBar() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.color_1_first));
    }

    private void viewHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.vovak.weathertest",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void startNextScreen(){
        if(isLoggedIn()){
            Intent intent = new Intent(this,SearchCityActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            initButtonFb();
        }
    }

    private void initButtonFb() {
       mLoginButton = findViewById(R.id.login_button);
       mLoginButton.setReadPermissions(Arrays.asList(EMAIL,PUBLIC_PROFILE));
       mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {
               Log.d(TAG, "onSuccess: AccesToken = "+loginResult.getAccessToken().getToken());
               Log.d(TAG, "onSuccess: AccesToken = "+loginResult.getRecentlyDeniedPermissions());
               GraphRequest request = GraphRequest.newMeRequest(
                       loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                           @Override
                           public void onCompleted(JSONObject object, GraphResponse response) {
                               Log.d(TAG, "onCompleted: "+response.toString());
                               try {
                                   String email = object.getString(EMAIL);
                                   String name = object.getString(NAME);
                                   Log.d(TAG, "onCompleted: "+email+ " "+name);
                                   saveDataSharedPreferences(name,email);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
               );
               Bundle parameters = new Bundle();
               parameters.putString("fields", "id,name,email");
               request.setParameters(parameters);
               request.executeAsync();
               if(isLoggedIn()){
                   startNextScreen();
               }
           }

           @Override
           public void onCancel() {
               Log.d(TAG, "onCancel: ");
           }

           @Override
           public void onError(FacebookException error) {
               Log.d(TAG, "onError: "+error.getMessage());
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null;
        Log.d(TAG, "onSuccess: isLoggendIn = "+isLoggedIn);
        return isLoggedIn;
    }

    private void saveDataSharedPreferences(String name, String email){
        SharedPreferences sharedPref = getSharedPreferences(MY_PRESERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_SHARED_PREFERENCES_NAME, name);
        editor.putString(KEY_SHARED_PREFERENCES_EMAIL, email);
        editor.apply();
    }

}
