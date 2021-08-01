package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class edit extends AppCompatActivity {

    EditText pname, price, address, contact, desc;
    ImageButton edit, delete, img;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap rbitmap;
    String userImage;
    String uid = "";
    int pic_change = 0;
    int click = 0;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);



        click = 0;
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        pname = findViewById(R.id.edit_pname);
        price = findViewById(R.id.edit_price);
        address = findViewById(R.id.edit_address);
        contact = findViewById(R.id.edit_contact);
        desc = findViewById(R.id.edit_desc);
        edit = findViewById(R.id.edit_edit);
        img = findViewById(R.id.edit_image);
        delete = findViewById(R.id.edit_delete);
        byte[] bytes = Base64.decode(b.get("profile_img").toString().replaceAll("@", "+"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(bitmap);
        img.setEnabled(false);
        userImage = b.get("profile_img").toString();
        pname.setText(b.get("profile_name").toString());
        price.setText(b.get("profile_price").toString());
        address.setText(b.get("profile_address").toString());
        contact.setText(b.get("profile_contact").toString());
        desc.setText(b.get("profile_desc").toString());
        price.setText(b.get("profile_price").toString());
        uid = b.get("profile_uid").toString();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new delete_async().execute();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("xxxxxxxxx " + edit);
                if(click==0)
                {click = 1;
                edit.setImageResource(R.drawable.save);
                pname.setEnabled(true);
                price.setEnabled(true);
                address.setEnabled(true);
                contact.setEnabled(true);
                desc.setEnabled(true);
                img.setEnabled(true);}
                else if(click==1)
                {   click = 0;
                    if(validate_pname() && validate_price() && validate_address() && validate_contact() && validate_desc() && validate_image())
                    {new update().execute();}
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    boolean validate_image()
    {if(userImage==null)
    {Toast.makeText(edit.this, "Select Image", Toast.LENGTH_SHORT).show();
        img.setImageResource(R.drawable.add_pic_highlight);
        return false;}
    else
    {img.setImageResource(R.drawable.add_pic);
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

    boolean validate_contact() {
    if (contact.getText().toString().length() == 10) {
        contact.setError(null);
        return true;
    } else
    {contact.setError("Invalid contact number");
        return false;}
    }



    class delete_async extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog dialog2;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog2 = new ProgressDialog(edit.this);
            dialog2.setTitle("deleting...");
            dialog2.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            dialog2.dismiss();
//            Toast.makeText(edit.this, "Deleted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(edit.this, profile_page.class));

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Controller.deleteData(uid);
            return null;

        }
    }


    class update extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(edit.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("Inserting your values..");
            dialog.show();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            EditText pname2 = findViewById(R.id.edit_pname);
//            Controller.updateData(pname.getText().toString(), price.getText().toString(), address.getText().toString(), contact.getText().toString(), desc.getText().toString(), userImage, uid);
                String img_bit= "";
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
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
                            Toast.makeText(edit.this,error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    String t = "";
                    SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("action","update");
                    params.put("name", pname2.getText().toString());
                    params.put("price",price.getText().toString());
                    params.put("address",address.getText().toString());
                    params.put("contact",contact.getText().toString());
                    params.put("desc",desc.getText().toString());
                    params.put("login_id",sharedPreferences.getString("email", ""));
                    params.put("img", userImage);
                    params.put("id", uid);
                    System.out.println("bbbbbbbbbb " + userImage + "\n\n\n" + uid + " " + pname.getText().toString());
//                    System.out.println("vvvvvvvvvv " + uid + " " + pname.getText().toString());
                    return params;
                }

            };

            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            stringRequest.setRetryPolicy(policy);


            RequestQueue requestQueue = Volley.newRequestQueue(edit.this);

            requestQueue.add(stringRequest);
            System.out.println("oooooooooo " + requestQueue);



            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            dialog.dismiss();
//            finishActivity(1);

//            finishActivity(1);
            startActivity(new Intent(edit.this, profile_page.class));

        }


    }

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
            System.out.println("ppppp " + filePath);
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
                pic_change = 1;
                img.setImageBitmap(rbitmap);
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
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(edit.this, profile_page.class));
    }
}