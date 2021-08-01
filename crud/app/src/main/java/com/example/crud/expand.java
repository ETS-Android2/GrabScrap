package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class expand extends AppCompatActivity {
TextView pname, price, desc, address;
ImageButton image;
Button contact;
    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        pname = findViewById(R.id.expand_pname);
        price = findViewById(R.id.expand_price);
        desc = findViewById(R.id.expand_desc);
        address = findViewById(R.id.expand_address);
        contact = findViewById(R.id.expand_contact);
        image = findViewById(R.id.expand_image);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        pname.setText(b.get("profile_name").toString());
        price.setText("₹" + b.get("profile_price").toString());
        desc.setText(b.get("profile_desc").toString());
        address.setText(b.get("profile_address").toString());
        contact.setText(b.get("profile_contact").toString());
        contact.setPaintFlags(contact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        byte[] bytes = Base64.decode(b.get("profile_img").toString().replaceAll("@", "+"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        image.setImageBitmap(bitmap);
        image.setEnabled(false);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(contact.getText().toString()))));
            }
        });

    }
}