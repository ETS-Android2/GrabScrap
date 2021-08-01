package com.example.crud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class profile_page extends AppCompatActivity {
    TextView name, email, address, company;
    Button log_out;
    ImageButton info_btn, refresh;
    private ListView listView;
    private ArrayList< MyDataModel > list;
    private MyArrayAdapter adapter;
    ShimmerFrameLayout frameLayout2;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        System.out.println(sharedPreferences.getString("address", "") + " " + sharedPreferences.getString("contact", ""));
        refresh = findViewById(R.id.profile_refresh);
        name = findViewById(R.id.name_tag_edit);
        log_out = findViewById(R.id.log_out_btn);
        email = findViewById(R.id.mail_tag_edit);
        address = findViewById(R.id.address_tag_edit);
        company = findViewById(R.id.company_tag_edit);
        info_btn = findViewById(R.id.info_btn);
        name.setText(sharedPreferences.getString("name", ""));
        email.setText(sharedPreferences.getString("email", ""));
        address.setText(sharedPreferences.getString("address", ""));
        company.setText(sharedPreferences.getString("contact", ""));

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            list = new ArrayList < > ();
            adapter = new MyArrayAdapter(profile_page.this, list, null);
            listView = (ListView) findViewById(R.id.profile_list_view);
            listView.setAdapter(adapter);
            new profile_async().execute();
            }
        });

        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile_page.this, terms_cons.class));
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(profile_page.this, "Logging Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(profile_page.this, MainActivity.class));
            }
        });

        list = new ArrayList < > ();
        adapter = new MyArrayAdapter(this, list, null);
        listView = (ListView) findViewById(R.id.profile_list_view);
        listView.setAdapter(adapter);

        frameLayout2 = findViewById(R.id.shimmer_edit);

        new profile_async().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapter.getItem(i).getName() + "  lllllllllllll");
                Intent intent = new Intent(profile_page.this, edit.class);
                intent.putExtra("profile_name", adapter.getItem(i).getName());
                intent.putExtra("profile_price", adapter.getItem(i).getPrice());
                intent.putExtra("profile_desc", adapter.getItem(i).getDesc());
                intent.putExtra("profile_contact", adapter.getItem(i).getCinfo());
                intent.putExtra("profile_address", adapter.getItem(i).getAddress());
                intent.putExtra("profile_uid", adapter.getItem(i).getuid());
                intent.putExtra("profile_img", adapter.getItem(i).getimg());
                startActivity(intent);
//                Toast.makeText(profile_page.this, adapter.getItem(i).getName(), Toast.LENGTH_SHORT).show();


            }
        });

    }


    class profile_async extends AsyncTask<Void, Void, Void>
    {
        int jIndex, x;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            frameLayout2.startShimmer();
            frameLayout2.setVisibility(View.VISIBLE);

            x = list.size();
            if(x==0)
                jIndex = 0;
            else
                jIndex = x;

        }

        @Nullable
        @Override
        protected Void doInBackground(Void...params) {
            JSONObject jsonObject = Controller.readAllData();
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

                        /**
                         * Check Length of Array...
                         */


                        int lenArray = array.length();
                        if (lenArray > 0) {
                            for (; jIndex < lenArray; jIndex++) {

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
                                String uid = innerObject.getString("uid");
                                String img = innerObject.getString("image");
                                System.out.println(pname + " " + price + " " + address + " " + contact_info + " " + description + " " + login_id);
                                //  String image = innerObject.getString(Keys.KEY_IMAGE);
                                /**
                                 * Getting Object from Object "phone"
                                 */
                                //JSONObject phoneObject = innerObject.getJSONObject(Keys.KEY_PHONE);
                                //String phone = phoneObject.getString(Keys.KEY_MOBILE);

                                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                if(!(pname.equals("") && price.equals("") && address.equals("") && contact_info.equals("") && description.equals("") && login_id.equals("")) && login_id.equals(sharedPreferences.getString("email", "")))
                                {model.setpname(pname);
                                model.price(price);
                                model.address(address);
                                model.cinfo(contact_info);
                                model.desc(description);
                                model.loginid(login_id);
                                model.setuid(uid);
                                model.setimg(img);
                                list.add(model);}
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            frameLayout2.setVisibility(View.INVISIBLE);
            frameLayout2.stopShimmer();
            /**
             * Checking if List size if more than zero then
             * Update ListView
             **/
            if (list.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(profile_page.this, home.class));
        super.onBackPressed();
    }
}
