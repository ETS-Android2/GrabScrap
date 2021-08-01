package com.example.crud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.userprofile.UserProfileException;
import com.ibm.cloud.appid.android.api.userprofile.UserProfileResponseListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class dialog extends AppCompatDialogFragment {
    private EditText user_address;
    private EditText user_contact;
    private TextView user_name;
    private TextView user_email;
    String name, contact, email, address;
    //    ProgressDialog progressbar;
    private dialoglistener listener;
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        user_address = view.findViewById(R.id.user_address);
        user_contact = view.findViewById(R.id.user_contact);
        user_name = view.findViewById(R.id.user_name);
        user_email = view.findViewById(R.id.user_email);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        AppID.getInstance().getUserProfileManager().getUserInfo(new UserProfileResponseListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {

//                    String userid = jsonObject.getString("email");
                    user_email.setText(jsonObject.getString("email"));
                    user_name.setText(jsonObject.getString("name"));

                } catch (Exception ignored) {}
            }

            @Override
            public void onFailure(UserProfileException e) {System.out.println("something went wrong!");}
        });
        builder.setView(view).setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                name = user_name.getText().toString();
                contact = user_contact.getText().toString();
                address = user_address.getText().toString();
                email = user_email.getText().toString();
                listener.applytext(email, name, contact, address);
                func();
            }
        }).setCancelable(false);

        return builder.create();}

//    private static class asyncclass extends AsyncTask <Void, Void, Void>
//    {private final WeakReference<dialog> activityWeakReference;
//    asyncclass(dialog activity)
//    {activityWeakReference = new WeakReference<dialog>(activity);}
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog activity = activityWeakReference.get();
//            if(activity == null || activity.getActivity().isFinishing())
//            {return;}
//
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//        dialog activity = activityWeakReference.get();
//
//
//            activity.func();
//            JSONObject jsonObject = Controller.user_details(activity.email, activity.name, activity.address);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
//            dialog activity = activityWeakReference.get();
//            if(activity == null || activity.getActivity().isFinishing())
//            {return;}
////            activity.progressbar.dismiss();
//        }
//    }


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try {
            listener = (dialoglistener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "implementation to be done");
        }
    }

    public void func()
    {System.out.println(name + " " + contact + " " + address + " " + email);
    }

    public interface dialoglistener{
        void applytext(String login_id, String name, String contact, String address);
    }

}