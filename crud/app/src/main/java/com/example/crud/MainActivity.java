package com.example.crud;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.AppIDAuthorizationManager;
import com.ibm.cloud.appid.android.api.LoginWidget;
import com.ibm.cloud.appid.android.api.userprofile.UserProfileException;
import com.ibm.cloud.appid.android.api.userprofile.UserProfileResponseListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    //    private Button read, readAll, insert, delete, update;
    private final static String region = AppID.REGION_UK;
    private final static String authTenantId = "86fec9ae-0cb7-4360-8dd9-170b3bf0bca0";
    private AppID appId;
    private AppIDAuthorizationManager appIDAuthorizationManager;
    private TokensPersistenceManager tokensPersistenceManager;
    SharedPreferences sharedPreferences;
    private static final String USER_ID = "";
    private static final String USER_ADDRESS = "";
    private static final String  USER_NAME= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        read = (Button) findViewById(R.id.read_btn);
//        readAll = (Button) findViewById(R.id.read_all_btn);
//        insert = (Button) findViewById(R.id.insert_btn);LoginWidget
//        update = (Button) findViewById(R.id.update_btn);
//        delete = (Button) findViewById(R.id.delete_btn);
//
//        readAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (InternetConnection.checkConnection(getApplicationContext())) {
//                    Intent intent = new Intent(getApplicationContext(), ReadAllData.class);
//                    startActivity(intent);
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                }
//
//
//
//
//
//            }
//        });
//
//
//        insert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (InternetConnection.checkConnection(getApplicationContext())) {
//                    Intent intent = new Intent(getApplicationContext(), InsertData.class);
//                    startActivity(intent);
//
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                }
//
//
//
//
//
//            }
//        });
//
//
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (InternetConnection.checkConnection(getApplicationContext())) {
//                    Intent intent = new Intent(getApplicationContext(), UpdateData.class);
//                    startActivity(intent);
//
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                }
//
//
//
//
//            }
//        });
//
//
//        read.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (InternetConnection.checkConnection(getApplicationContext())) {
//                    Intent intent = new Intent(getApplicationContext(), ReadSingleData.class);
//                    startActivity(intent);
//
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                }
//
//
//
//
//            }
//        });
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (InternetConnection.checkConnection(getApplicationContext())) {
//                    Intent intent = new Intent(getApplicationContext(), DeleteData.class);
//                    startActivity(intent);
//
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                }
//
//
//
//            }
//        });
//
//
//
//


        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        if(sharedPreferences.getString("email", "").equals(""))
        {do_func();}
        else
        {startActivity(new Intent(this, home.class));}



    }

    private void refreshTokens(String refreshToken) {
        Log.d(logTag("refreshTokens"), "Trying to refresh tokens using a refresh token");
        boolean storedTokenAnonymous = tokensPersistenceManager.isStoredTokenAnonymous();
        AppIdSampleAuthorizationListener appIdSampleAuthorizationListener =
                new AppIdSampleAuthorizationListener(this, appIDAuthorizationManager, storedTokenAnonymous);
        appId.signinWithRefreshToken(this, refreshToken, appIdSampleAuthorizationListener);
    }

    /**
     * Continue as guest action
     * @param v
     */
    public void onAnonymousClick (View v) {
        Log.d(logTag("onAnonymousClick"),"Attempting anonymous authorization");

        final String storedAccessToken = tokensPersistenceManager.getStoredAnonymousAccessToken();
        AppIdSampleAuthorizationListener appIdSampleAuthorizationListener =
                new AppIdSampleAuthorizationListener(this, appIDAuthorizationManager, true);

        appId.signinAnonymously(getApplicationContext(), storedAccessToken, appIdSampleAuthorizationListener);
    }

    /**
     * Log in with identity provider authentication action
     * @param
     */
    public void clicked(){
        Log.d(logTag("onLoginClick"),"Attempting identified authorization");
        LoginWidget loginWidget = appId.getLoginWidget();
        final String storedAccessToken;
        storedAccessToken = tokensPersistenceManager.getStoredAccessToken();

        AppIdSampleAuthorizationListener appIdSampleAuthorizationListener =
                new AppIdSampleAuthorizationListener(this, appIDAuthorizationManager, false);

        loginWidget.launch(this, appIdSampleAuthorizationListener, storedAccessToken);
    }

    private String logTag(String methodName){
        return getClass().getCanonicalName() + "." + methodName;
    }


    public void do_func()
    {        appId = AppID.getInstance();

        appId.initialize(this, authTenantId, region);

        appIDAuthorizationManager = new AppIDAuthorizationManager(this.appId);
        tokensPersistenceManager = new TokensPersistenceManager(this, appIDAuthorizationManager);

        String storedRefreshToken = tokensPersistenceManager.getStoredRefreshToken();
        if (storedRefreshToken != null && !storedRefreshToken.isEmpty()) {
            refreshTokens(storedRefreshToken);
        }
        clicked();
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        AppID.getInstance().getUserProfileManager().getUserInfo(new UserProfileResponseListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    System.out.println("xxxxxxxxxxxx " + jsonObject);
                    editor.putString("email", jsonObject.getString("email"));
                    editor.putString("name", jsonObject.getString("name"));
                    editor.apply();
                } catch (Exception ignored) {}
            }

            @Override
            public void onFailure(UserProfileException e) {System.out.println("something went wrong!");}
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}