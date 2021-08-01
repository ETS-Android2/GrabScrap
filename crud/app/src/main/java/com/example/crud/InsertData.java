package com.example.crud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertData extends AppCompatActivity {

    private Button insert;
    ImageButton img_btn;
    String id;
    String prod_name, p_price, p_desc, p_address, p_cinfo;
    private EditText pname, price, desc, address, cinfo;
    ProgressDialog dialog;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap rbitmap;
    String userImage, bit;
    RequestQueue requestQueue;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_data);
        insert = (Button) findViewById(R.id.insert_btn);
        pname = (EditText) findViewById(R.id.pname2);
        price = (EditText) findViewById(R.id.price2);
        desc = (EditText) findViewById(R.id.desc2);
        address = (EditText) findViewById(R.id.address2);
        cinfo = (EditText) findViewById(R.id.cinfo2);
        img_btn = (ImageButton) findViewById(R.id.imageButton);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(validate_pname() && validate_price() && validate_address() && validate_contact() && validate_desc() && validate_image())
            {Toast.makeText(InsertData.this, "Successful", Toast.LENGTH_SHORT).show();
                prod_name = pname.getText().toString();
                p_price = price.getText().toString();
                p_desc = desc.getText().toString();
                p_address = address.getText().toString();
                p_cinfo = cinfo.getText().toString();
                dialog = new ProgressDialog(InsertData.this);
                InsertDataActivity task = new InsertDataActivity(InsertData.this);
                task.execute();
//                System.out.println(userImage + " kkkkkkkkkkkk");
            }
//                Toast.makeText(InsertData.this, pname.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    boolean validate_image()
    {if(userImage==null)
    {Toast.makeText(InsertData.this, "Select Image", Toast.LENGTH_SHORT).show();
    img_btn.setImageResource(R.drawable.add_pic_highlight);
    return false;}
    else
    {img_btn.setImageResource(R.drawable.add_pic);
    return true;}}

    boolean validate_pname()
    {if(!pname.getText().toString().equals(""))
    {pname.setError(null);
    return true;}
    else
    {pname.setError("Field can't be empty");
    return false;}}

    boolean validate_price()
    {if(price.getText().toString().equals(""))
    {price.setError("Field can't be empty");
    return false;}
    else
    {price.setError(null);
    return true;}}

    boolean validate_desc()
    {if(desc.getText().toString().length()>200)
    {desc.setError("Description too long");
    return false;}
    if(desc.getText().toString().equals(""))
    {desc.setError("Field can't be empty");
        return false;}
    else
    {desc.setError(null);
        return true;}}

    boolean validate_address()
    {if(address.getText().toString().equals(""))
    {address.setError("Field can't be empty");
        return false;}
    else
    {address.setError(null);
        return true;}}

    boolean validate_contact()
    {if(cinfo.getText().toString().length()==10)
    {cinfo.setError(null);
    return true;}
    else
    {cinfo.setError("Invalid contact number");
    return false;}}


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 1, stream);
//                byte[] bytes = stream.toByteArray();
//                bit = Base64.encodeToString(bytes, Base64.URL_SAFE);
////                encoded = new String(Base64.encodeBase64(bytes), "UTF-8");
//                Toast.makeText(this, bit, Toast.LENGTH_SHORT).show();
//                System.out.println("cccccccccccc " + bit);
//                Log.i("ssssssssss ", bit);
//                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                rbitmap = getResizedBitmap(bitmap2,250);//Setting the Bitmap to ImageView
//                userImage = getStringImage(rbitmap);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                rbitmap = getResizedBitmap(bitmap,250);//Setting the Bitmap to ImageView
                String userImage2 = getStringImage(rbitmap);
                System.out.println("kkkkk " + userImage);
                userImage = userImage2.replaceAll("\\+","@");
                img_btn.setImageBitmap(rbitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    private class InsertDataActivity extends AsyncTask < Void, Void, Void > {
        private final WeakReference<InsertData> activityWeakReference;
        InsertDataActivity(InsertData activity)
        {activityWeakReference = new WeakReference<InsertData>(activity);}

        String result = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            InsertData activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing())
            {return;}


            activity.dialog.setTitle("Hey Wait Please...");
            activity.dialog.setMessage("Inserting your values..");
            activity.dialog.show();

        }

        @Override
        protected Void doInBackground(Void...params) {
            InsertData activity = activityWeakReference.get();
            SharedPreferences sharedPreferences = activity.getSharedPreferences("user_details", MODE_PRIVATE);
//            JSONObject jsonObject = Controller.insertData(activity.prod_name, activity.p_price, activity.p_desc, activity.p_address, activity.p_cinfo, sharedPreferences.getString("email", ""), activity.userImage);


            StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://script.google.com/macros/s/AKfycbyq1KmzLNqjZrNqq6RiaGQNhyvW_Xl5uuVEtZOleFe3TYuc3L3ZqhcH3xex5QH2CyUp/exec",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            loading.dismiss();
//                            Toast.makeText(AddUser.this,response,Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(AddUser.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("action","insert");
                    params.put("pname", activity.prod_name);
                    params.put("price",activity.p_price);
                    params.put("address",activity.p_address);
                    params.put("cinfo",activity.p_cinfo);
                    params.put("desc",activity.p_desc);
                    params.put("login_id",sharedPreferences.getString("email", ""));
                    params.put("image", activity.userImage);
                    System.out.println("bbbbbbbbbb " + activity.userImage);
                    return params;
                }

            };

            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            stringRequest.setRetryPolicy(policy);


            RequestQueue requestQueue = Volley.newRequestQueue(InsertData.this);

            requestQueue.add(stringRequest);


//            try {
//                /**
//                 * Check Whether Its NULL???
//                 */
////                if (jsonObject != null) {
////
////                    result = jsonObject.getString("result");
//
////                }
//            } catch (JSONException je) {
//                Log.i(Controller.TAG, "" + je.getLocalizedMessage());
//            }

//            int socketTimeout = 0; // 30 seconds. You can change it
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////
//            stringRequest.setRetryPolicy(policy);
//
//
//
//            requestQueue.add(stringRequest);
//            System.out.println("reqqqq " + stringRequest);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            InsertData activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing())
            {return;}
            activity.dialog.dismiss();
            finishActivity(1);
//            Toast.makeText(activity.getApplicationContext(), activity.prod_name, Toast.LENGTH_LONG).show();
            startActivity(new Intent(InsertData.this, home.class));
        }
    }


}
