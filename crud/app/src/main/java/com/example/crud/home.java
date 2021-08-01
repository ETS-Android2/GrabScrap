package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.userprofile.UserProfileException;
import com.ibm.cloud.appid.android.api.userprofile.UserProfileResponseListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class home extends AppCompatActivity implements dialog.dialoglistener {
    private Button read, readAlistView2ll, insert, delete, update;
    private NoticeHelper.AuthState authState;
    SwipeRefreshLayout refreshLayout;
    String name, contact, email, address;
    ProgressDialog progressbar;
    String fetching_mail;
    Boolean present = false;
    String to_search = "";
    Button logout, edit;
    ImageButton profile, refresh_btn;
    FloatingActionButton float_insert;
    ImageView img;
    EditText et;
    Activity context;
    private Context mContext;
    private ListView listView;
    private ArrayList< MyDataModel > list;
    private ArrayList< MyDataModel > list_copy;
    private MyArrayAdapter adapter;
    ShimmerFrameLayout frameLayout;
    ConstraintLayout layout1, layout2;
    TextInputEditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        profile = findViewById(R.id.profile_btn);
        search = findViewById(R.id.home_edittext2);
        search.setOnEditorActionListener(editorActionListener);
        to_search = "";
        layout1 = findViewById(R.id.home_layout);
        layout2 = findViewById(R.id.menu_cons);
        layout1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!(view instanceof EditText))
                {InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                search.clearFocus();}
                return false;
            }
        });

        layout2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!(view instanceof EditText))
                {InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                search.clearFocus();}
                return false;
            }
        });

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                et.setEnabled(false);
//            }
//        });
////        setimg();

//        refreshLayout = findViewById(R.id.refresh);
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                list = new ArrayList < > ();
//                adapter = new MyArrayAdapter(home.this, list);
//                listView = (ListView) findViewById(R.id.listView2);
//                listView.setAdapter(adapter);
//                frameLayout.stopShimmer();
//                new disp().execute();
//                refreshLayout.setRefreshing(false);
//            }
//        });

        refresh_btn = findViewById(R.id.refresh_btn);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list = new ArrayList < > ();
                adapter = new MyArrayAdapter(home.this, list, null);
                listView = (ListView) findViewById(R.id.listView2);
                listView.setAdapter(adapter);
                new disp().execute();
            }
        });

        frameLayout = findViewById(R.id.shimmer_layout);
        frameLayout.setShimmer(new Shimmer.AlphaHighlightBuilder().setAutoStart(false).build());
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!(view instanceof EditText))
                {InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    search.clearFocus();}
                return false;
            }
        });
        listView = findViewById(R.id.listView2);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!(view instanceof EditText))
                {InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                search.clearFocus();}
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapter.getItem(i).getName() + "  lllllllllllll");
                Intent intent = new Intent(home.this, expand.class);
                intent.putExtra("profile_name", adapter.getItem(i).getName());
                intent.putExtra("profile_price", adapter.getItem(i).getPrice());
                intent.putExtra("profile_desc", adapter.getItem(i).getDesc());
                intent.putExtra("profile_contact", adapter.getItem(i).getCinfo());
                intent.putExtra("profile_address", adapter.getItem(i).getAddress());
                intent.putExtra("profile_uid", adapter.getItem(i).getuid());
                intent.putExtra("profile_img", adapter.getItem(i).getimg());
                startActivity(intent);
//                Toast.makeText(home.this, adapter.getItem(i).getName(), Toast.LENGTH_SHORT).show();

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, profile_page.class));
            }
        });

        float_insert = findViewById(R.id.float_insert);
        float_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, InsertData.class));
            }
        });
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.apply();
//                startActivity(new Intent(home.this, MainActivity.class));
//            }
//        });
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        System.out.println(sharedPreferences.getString("address", "") + "thisssss");
        if(sharedPreferences.getString("email", "").equals(""))
        {System.out.println("nnnnnnnnnnnn");
            AppID.getInstance().getUserProfileManager().getUserInfo(new UserProfileResponseListener() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        fetching_mail = jsonObject.getString("email");
                        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", jsonObject.getString("email"));
                        editor.putString("name", jsonObject.getString("name"));
                        editor.apply();

                    } catch (Exception ignored) {}
                }

                @Override
                public void onFailure(UserProfileException e) {System.out.println("something went wrong!");}
            });
            check_new_user();}


//        readAll = (Button) findViewById(R.id.insert_btn);
//
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
//            }
//        });


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
//            }
//        });


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
//            }
//        });
        list = new ArrayList < > ();
        adapter = new MyArrayAdapter(this, list, null);
        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(adapter);
        new disp().execute();

    }


    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i)
            { case EditorInfo.IME_ACTION_SEARCH:
            {search_func();}
            }
            return false;
        }
    };

    @Override
    public void onBackPressed()
    {search.setText("");
        if(!to_search.equals(""))
        {to_search = "";
            list = list_copy;
            System.out.println("gggggggg " + list + " mmmmm " + list_copy);
            adapter = new MyArrayAdapter(this, list, search.getText().toString());
            listView = (ListView) findViewById(R.id.listView2);
            listView.setAdapter(adapter);
            if (list.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }}
        else
            finishAffinity();}


    void search_func()
    {list = new ArrayList < > ();
        adapter = new MyArrayAdapter(this, list, search.getText().toString());
        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(adapter);
        to_search = search.getText().toString();
        new disp().execute();
    }


    class disp extends AsyncTask<Void, Void, Void>
    {ProgressDialog dialog;
        int jIndex;
        int x;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            x = list.size();

            if (x == 0)
                jIndex = 0;
            else
                jIndex = x;

//            if(frameLayout.getVisibility()==View.GONE)
//            {frameLayout.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout.startShimmer();

//            }

//            dialog = new ProgressDialog(home.this);
//            dialog.setTitle("Hey Wait Please..." + x);
//            dialog.setMessage("Fetching all the Values");
//            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject jsonObject = Controller.readAllData();

            System.out.println("mmmmmm " + jsonObject);
            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {
                    /**
                     * Check Length...
                     */
                    if (jsonObject.length() > 0) {
                        /**
                         * Getting Array named "records" From MAIN Json Object
                         */
                        JSONArray array = jsonObject.getJSONArray("records");
                        System.out.println("nnnnnnnn " + array);
                        /**
                         * Check Length of Array...
                         */


                        int lenArray = array.length();
                        if (lenArray > 0) {
                            for (jIndex = lenArray-1; jIndex >= 0; jIndex--) {

                                /**
                                 * Creating Every time New Object
                                 * and
                                 * Adding into List
                                 */
                                MyDataModel model = new MyDataModel();

                                /**
                                 * Getting Inner Object from contacts array...
                                 * and
                                 * From that We will get Name of that Contact
                                 *
                                 */
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                System.out.println("lllllll");
//                                System.out.println(innerObject);
//                                System.out.println(array.getJSONObject(jIndex));
                                String pname = innerObject.getString("pname");
                                String price = innerObject.getString("price");
                                String address = innerObject.getString("address");
                                String contact_info = innerObject.getString("contact");
                                String description = innerObject.getString("desc");
                                String login_id = innerObject.getString("email");
                                String image = innerObject.getString("image");


                                //  String image = innerObject.getString(Keys.KEY_IMAGE);
                                /**
                                 * Getting Object from Object "phone"
                                 */
                                //JSONObject phoneObject = innerObject.getJSONObject(Keys.KEY_PHONE);
                                //String phone = phoneObject.getString(Keys.KEY_MOBILE);
                                if(!to_search.equals("") && pname.toLowerCase().contains(to_search.toLowerCase()))
                                {if(!(pname.equals("") && price.equals("") && address.equals("") && contact_info.equals("") && description.equals("") && login_id.equals("")))
                                {model.setpname(pname);
                                    model.price(price);
                                    model.address(address);
                                    model.cinfo(contact_info);
                                    model.desc(description);
                                    model.loginid(login_id);
                                    model.setimg(image);
                                    list.add(model);
                                    System.out.println("dddddddddddd " + image);
                                }}
                                if(to_search.equals(""))
                                {if(!(pname.equals("") && price.equals("") && address.equals("") && contact_info.equals("") && description.equals("") && login_id.equals("")))
                                {model.setpname(pname);
                                    model.price(price);
                                    model.address(address);
                                    model.cinfo(contact_info);
                                    model.desc(description);
                                    model.loginid(login_id);
                                    model.setimg(image);
                                    list.add(model);
                                    list_copy = list;}}
                                //                              model.setImage(image);

                                /**
                                 * Adding name and phone concatenation in List...
                                 */

                            }
                        }
                    }
                } else {

                }
            } catch (JSONException je) {
                Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
//            dialog.dismiss();
            frameLayout.stopShimmer();
            frameLayout.setVisibility(View.INVISIBLE);

            if (list.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
        }



    }


    public void setimg()
    {
//        Glide.with(home.this)
//    .load("https://drive.google.com/uc?id=1k_u8AaDudPlq786cUzHoCuSk5NdYfi95")
//    .into(img);
        String i = "";
        JSONObject jsonObject = Controller.readAllData();
        try {
            System.out.println("zzzzzzzz " + i + "   " + jsonObject);
            JSONArray array = null;
            array = jsonObject.getJSONArray("records");
            i = array.getJSONObject(0).get("image").toString();
            System.out.println("zzzzzzzz " + i + "   " + jsonObject);
            byte[] bytes = Base64.decode(array.getJSONObject(1).get("image").toString().replaceAll("@", "+"), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            img.setImageBitmap(bitmap);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//    Picasso.with(home.this).load("https://drive.google.com/drive/folders/19UaEU5nUREhQ1je6Y9hLDNuY_BkRe4xd").into(img);
//    img.setImageResource(LoadImageFromWebOperations("https://drive.google.com/uc?export=view&id=1tPsagSXMsCspeurxisi-1-PWoIFb1PWm"));
    }



    @Override
    public void applytext(String login_id, String name, String contact, String address) {
        this.name = name;
        email = login_id;
        this.contact = contact;
        this.address = address;
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address", this.address);
        editor.putString("contact", this.contact);
        editor.apply();
        progressbar = new ProgressDialog(home.this);
        progressbar.setTitle("Loading...");
        progressbar.setMessage("Setting up account!");
        progressbar.show();
        home.asyncclass task = new home.asyncclass(home.this);
        task.execute();
    }

    public void open_dialog()
    {dialog d = new dialog();
        d.setCancelable(false);
        d.show(getSupportFragmentManager(), "example dialog");}

    private static class asyncclass extends AsyncTask<Void, Void, Void>
    {private final WeakReference<home> activityWeakReference;
        asyncclass(home activity)
        {activityWeakReference = new WeakReference<home>(activity);}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            home activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing())
            {return;}
        }

        @Override
        protected Void doInBackground(Void... voids) {
            home activity = activityWeakReference.get();

            JSONObject jsonObject = Controller.user_details(activity.email, activity.name, activity.address, activity.contact);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            home activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing())
            {return;}
            activity.progressbar.dismiss();
        }
    }

    public void check_new_user()
    {progressbar = new ProgressDialog(home.this);
        System.out.println("iiiiiiiiiiiiiiiii");
        async2 a = new async2(this);
        a.execute();}

    private class async2 extends AsyncTask<Void, Integer, Integer>
    {private final WeakReference<home> activityWeakReference;
        async2(home activity)
        {activityWeakReference = new WeakReference<home>(activity);}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            home activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing())
            {return;}
            activity.progressbar = new ProgressDialog(home.this);
            activity.progressbar.setTitle("Loading...");
            activity.progressbar.setMessage("Setting up account!");
            activity.progressbar.show();

        }

        @Override
        protected Integer doInBackground(Void... voids) {
            home activity = activityWeakReference.get();

            JSONObject db = Controller.read_db2();
            System.out.println(db + "  aaaaaaaaaaaannnnnnnnnnndddddddddd");
            try {
                JSONArray array = null;
                if (db != null) {
                    array = db.getJSONArray("records");}
                done:
                for(int i=0;i<array.length();i++)
                {if(array.getJSONObject(i).get("user_id").equals(activity.fetching_mail))
                {activity.present = true;
//                SharedPreferences sharedPreferences = activity.getSharedPreferences("user_details", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                System.out.println(array.getJSONObject(i).get("address").toString() + " " + array.getJSONObject(i).get("contact").toString() + "         sssssssssssssssss");
//                editor.putString("address", array.getJSONObject(i).get("address").toString());
//                editor.putString("company", array.getJSONObject(i).get("contact").toString());
//                editor.apply();
                set_up_data(array.getJSONObject(i).get("address").toString(), array.getJSONObject(i).get("contact").toString());
                System.out.println(activity.present + " " + array.getJSONObject(0).get("user_id") + "  " + array.getJSONObject(1).get("user_id") + "   " + activity.fetching_mail + "       ttttthiiiissssssssss");
                break done;}}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;}

        @Override
        protected void onPostExecute(Integer unused) {
            super.onPostExecute(unused);
            home activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing())
            {return;}
            activity.progressbar.dismiss();
            if(activity.present==false)
            {activity.open_dialog();}
        }

    }

    public void set_up_data(String address, String contact)
    {SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address", address);
        editor.putString("contact", contact);
        editor.apply();
        System.out.println("set_up_dataaa " + address + " " + contact + " " + sharedPreferences.getString("address", "") + " " + sharedPreferences.getString("contact", ""));

    }

}