package com.example.crud;

import androidx.annotation.NonNull;

import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Controller {



    public static final String TAG = "TAG";

    public static final String WAURL="https://script.google.com/macros/s/AKfycbyq1KmzLNqjZrNqq6RiaGQNhyvW_Xl5uuVEtZOleFe3TYuc3L3ZqhcH3xex5QH2CyUp/exec?";
    // EG : https://script.google.com/macros/s/AKfycbwXXXXXXXXXXXXXXXXX/exec?
//Make Sure '?' Mark is present at the end of URL
    private static Response response;

    public static JSONObject user_details(String email, String name, String address, String contact)
    {OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(WAURL+"action=user_details" + "&user_id=" + email + "&name=" + name + "&address=" + address + "&contact=" + contact).build();
        try {response = client.newCall(request).execute();
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject getpics()
    {try {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(WAURL+"action=getpics")
                .build();
        response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    } catch (@NonNull IOException | JSONException e) {
        Log.e(TAG, "" + e.getLocalizedMessage());
    }
        return null;
    }
    public static JSONObject readAllData() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=readAll")
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }


    public static JSONObject read_db2()
    {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=read_db2")
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject insertData(String p_name, String p_price, String p_desc, String p_address, String p_cinfo, String email, String userImage) {
        try {OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=insert&pname="+p_name+"&price="+p_price+"&address="+p_address+"&cinfo="+p_cinfo+"&desc="+p_desc+"&login_id="+email+"&image="+userImage).build();
            response = client.newCall(request).execute();

            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

//pname.getText().toString(), price.getText().toString(), address.getText().toString(), contact.getText().toString(), desc.getText().toString()
    public static JSONObject updateData(String pname, String price, String address, String contact, String desc, String img, String uid) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=update&id="+uid+"&name="+pname+"&price="+price+"&address="+address+"&contact="+contact+"&desc="+desc+"&img="+img.replaceAll("\\+","@"))
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject readData(String id) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=read&id="+id)
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject deleteData(String id) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=delete&id="+id)
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }


}