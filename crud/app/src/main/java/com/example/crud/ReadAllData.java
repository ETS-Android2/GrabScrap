package com.example.crud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ADJ on 5/17/2017.
 */
public class ReadAllData extends AppCompatActivity {

    private ListView listView;
    private ArrayList < MyDataModel > list;
    private MyArrayAdapter adapter;
    private Button readAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_all);

//        readAll = (Button) findViewById(R.id.readAll_btn1);
        list = new ArrayList < > ();
        adapter = new MyArrayAdapter(this, list, null);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        new ReadData1().execute();
//        readAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapter.getItem(i).getName() + "  lllllllllllll");
                Intent intent = new Intent(ReadAllData.this, expand.class);
                intent.putExtra("exapnd_pname", adapter.getItem(i).getName());
                intent.putExtra("exapnd_price", adapter.getItem(i).getPrice());
                intent.putExtra("exapnd_desc", adapter.getItem(i).getDesc());
                intent.putExtra("exapnd_contact", adapter.getItem(i).getCinfo());
                intent.putExtra("exapnd_address", adapter.getItem(i).getAddress());
                intent.putExtra("exapnd_img", adapter.getItem(i).getimg());
                startActivity(intent);
//                Toast.makeText(ReadAllData.this, adapter.getItem(i).getName(), Toast.LENGTH_SHORT).show();


            }
        });

    }


    class ReadData1 extends AsyncTask < Void, Void, Void > {

        ProgressDialog dialog;
        int jIndex;
        int x;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

            x = list.size();

            if (x == 0)
                jIndex = 0;
            else
                jIndex = x;

            dialog = new ProgressDialog(ReadAllData.this);
            dialog.setTitle("Hey Wait Please..." + x);
            dialog.setMessage("Fetching all the Values");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void...params) {
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
                                String image = innerObject.getString("image");


                                System.out.println("dddddddddddd " + pname + " " + price + " " + address + " " + contact_info + " " + description + " " + login_id);
                                //  String image = innerObject.getString(Keys.KEY_IMAGE);
                                /**
                                 * Getting Object from Object "phone"
                                 */
                                //JSONObject phoneObject = innerObject.getJSONObject(Keys.KEY_PHONE);
                                //String phone = phoneObject.getString(Keys.KEY_MOBILE);
                                if(!(pname.equals("") && price.equals("") && address.equals("") && contact_info.equals("") && description.equals("") && login_id.equals("")))
                                {model.setpname(pname);
                                model.price(price);
                                model.address(address);
                                model.cinfo(contact_info);
                                model.desc(description);
                                model.loginid(login_id);
                                model.setimg(image);}
                                //                              model.setImage(image);

                                /**
                                 * Adding name and phone concatenation in List...
                                 */
                                list.add(model);
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
            dialog.dismiss();
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
}
