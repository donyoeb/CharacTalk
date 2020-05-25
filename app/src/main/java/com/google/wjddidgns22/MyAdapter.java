package com.google.wjddidgns22;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {

    private ArrayList<Blog_item> Blog_item_DataList = new ArrayList<Blog_item>() ;
    String k ;

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Blog_item> sample;

    Bitmap bitmap;


    public MyAdapter() {
    }

    public int getCount() {
        return Blog_item_DataList.size();
    }

    public long getItemId(int position) {

        return position;
    }

    public Blog_item getItem(int position) {

        return Blog_item_DataList.get(position);
    }

    public View getView(final int position, View converView, ViewGroup parent) {
        final Context context = parent.getContext();
        if (converView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context
            .LAYOUT_INFLATER_SERVICE);
            converView = inflater.inflate(R.layout.blog_item,parent,false);
        }

        final ImageView img = (ImageView) converView.findViewById(R.id.blogimg);

        TextView Name = (TextView) converView.findViewById(R.id.blogtitle);
        TextView day = (TextView) converView.findViewById(R.id.blogday);

        Blog_item blog_item = Blog_item_DataList.get(position);

        k = blog_item.getPoster();

        Name.setText(blog_item.getTitle());
        day.setText(blog_item.getDay());

        Thread mThread = new Thread(){
          @Override
          public void run(){
              try{
                  URL url = new URL(k);
                  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                  conn.setDoInput(true);
                  conn.connect();

                  InputStream is = conn.getInputStream();
                  bitmap = BitmapFactory.decodeStream(is);

              } catch (MalformedURLException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
        };
        mThread.start();
        try {
            mThread.join();
            img.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /////////////////
        return converView;

    }

    public void addItem(String title , String img , String day){
        Blog_item item = new Blog_item(title,img,day);

        item.setTitle(title);
        item.setPoster(img);
        item.setDay(day);

        Blog_item_DataList.add(item);
    }
}