package com.google.wjddidgns22;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MyAdapter extends SubActivity4 {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Blog_item> sample;
    Bitmap bitmap;
    String k;
    public MyAdapter(Context context, ArrayList<Blog_item> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return sample.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public Blog_item getItem(int position) {
        return sample.get(position);
    }

    public View getView(final int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.blog_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.blogimg);
        TextView Name = (TextView) view.findViewById(R.id.blogtitle);
        TextView day = (TextView) view.findViewById(R.id.blogday);


        k = sample.get(position).getPoster();

        // imageView.setImageResource( sample.get(position).getPoster());
        Name.setText(sample.get(position).getTitle());
        day.setText(sample.get(position).getDay());

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try {
                    URL url = new URL(k);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);


                }


                catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();

        try {
            mThread.join();
            imageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        /////////////////
        return view;


    }
}