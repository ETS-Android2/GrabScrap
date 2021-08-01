package com.example.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter < MyDataModel > {

    public List < MyDataModel > modelList;
    Context context;
    private LayoutInflater mInflater;
    String to_search;
    // Constructors
    public MyArrayAdapter(Context context, List < MyDataModel > objects, String search) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
        to_search = search;
    }

    @Override
    public MyDataModel getItem(int position) {
        return modelList.get(position);
    }

    public MyDataModel send(int i)
    {return getItem(i);}
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        MyDataModel item = getItem(position);
        vh.product_name.setText(item.getName());
        vh.product_price.setText("₹" + item.getPrice());
//        vh.product_img.setText(item.getCinfo());
        byte[] bytes = Base64.decode(item.getimg().replaceAll("@", "+"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        vh.product_img.setImageBitmap(bitmap);
        vh.product_desc.setText(item.getDesc());
        vh.product_name.setBackgroundColor(android.R.color.white);
        vh.product_img.setBackgroundColor(android.R.color.white);
        vh.product_desc.setBackgroundColor(android.R.color.white);
        vh.product_price.setBackgroundColor(android.R.color.white);

//        vh.textViewId.setText(item());
//        vh.textViewName.setText(item.getName());


        return vh.rootView;
    }



    private static class ViewHolder {
        public final RelativeLayout rootView;
//
//        public final TextView prod;
//        public final TextView textViewName;
        public final TextView product_name;
        public final TextView product_price;
        public final TextView product_desc;
        public final ImageView product_img;

        private ViewHolder(RelativeLayout rootView, TextView product_name, TextView product_price, TextView product_desc, ImageView product_img) {
            this.rootView = rootView;
//            this.textViewId = textViewId;
//            this.textViewName = textViewName;
            this.product_name = product_name;
            this.product_price = product_price;
            this.product_desc = product_desc;
            this.product_img = product_img;

        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView product_name = (TextView) rootView.findViewById(R.id.row_view_prod_name);
            TextView product_price = (TextView) rootView.findViewById(R.id.row_view_price);
            TextView product_desc = (TextView) rootView.findViewById(R.id.row_view_desc);
//            TextView product_address = (TextView) rootView.findViewById(R.id.row_view_address);
            ImageView product_img = (ImageView) rootView.findViewById(R.id.row_view_image);

            return new ViewHolder(rootView, product_name, product_price, product_desc, product_img);
        }


    }
}
