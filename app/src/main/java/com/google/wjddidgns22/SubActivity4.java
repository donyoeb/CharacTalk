package com.google.wjddidgns22;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
public class SubActivity4 extends AppCompatActivity {

   // private ArrayList<Blog_item> Blog_item_DataList;  //어뎁터 클래스 생성

    public EditText editText; //검색할 edittex
    public Button findbt; //찾기 버튼
    public Button matbt; //맛집

    public String topname;
    public ListView list_view;

    public String serch; //에딧텍스트에 입력한 단어
    Elements contents; // 타이틀

    Document doc = null;

   private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub4);

        Intent intent2 = getIntent();
        final String id = intent2.getExtras().getString("탑이름"); // 오벨리스크 이름
        topname = id;

        Toast.makeText(SubActivity4.this, id, Toast.LENGTH_SHORT).show(); // 이름 보여주기

        editText = (EditText) findViewById(R.id.edtext);
        list_view = (ListView) findViewById(R.id.rview);


     //   final MyAdapter myAdapter = new MyAdapter(this,Blog_item_DataList);
       //  list_view.setAdapter((ListAdapter) myAdapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                new ArrayList<String>());
        list_view.setAdapter(adapter);



        matbt = (Button) findViewById(R.id.matjip);
        matbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {

                adapter.clear();

                new AsyncTask() {//AsyncTask객체 생성

                    @Override
                    protected Object doInBackground(Object[] params) {

                        String url, urlnum;
                        //for (int i=1;i<102;i=i+10) { }  //검색 여러번하기
                        url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                        serch = "맛집";
                        //   urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                        //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....
                        //  int urlcnt = 1;
                        //  urlcnt= urlcnt+10;  // 블로그 숫자 카운트

                        try {
                            doc = Jsoup.connect(url + topname + serch).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                            contents = doc.select("div[class=blog section _blogBase _prs_blg]");
                            int contentsize = contents.size();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        for (Element elem : contents) {//li[id=sp_blog_1~10]  블로그 1번~10번 저장
                            for (int i = 1; i < 11; i++) {
                               String img = elem.select("li[id=sp_blog_"+i+"]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                String title = elem.select("li[id=sp_blog_"+i+"]").select("a[title]").text();
                                String day = elem.select("li[id=sp_blog_"+i+"]").select("dd[class=txt_inline]").text();
                                adapter.add(img);
                                adapter.add(title);
                                adapter.add(day);

                               // Blog_item_DataList.add(new Blog_item(img,title,day));
                            }
                        }


                        adapter.notifyDataSetChanged(); // 어댑터리스트 갱신
                        list_view.setSelection(adapter.getCount() - 1);

                    }

                }.execute();

            }
        });

        /*
        findbt = (Button) findViewById(R.id.findbt);
        findbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {

                adapter.clear();

                new AsyncTask() {//AsyncTask객체 생성

                    @SuppressLint("WrongThread")
                    @Override
                    protected Object doInBackground(Object[] params) {

                        String url,urlnum;
                        //for (int i=1;i<102;i=i+10) { }  //검색 여러번하기
                        url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                        serch = editText.getText().toString();
                        //   urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                        //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....
                        //  int urlcnt = 1;
                        //  urlcnt= urlcnt+10;  // 블로그 숫자 카운트

                        try {
                            doc = Jsoup.connect(url+topname+serch).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                            contents = doc.select("div[class=blog section _blogBase _prs_blg]");
                            int contentsize = contents.size();

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }



                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        for (Element elem : contents){//li[id=sp_blog_1~10]
                            for (int i = 1;i<11;i++){
                                 String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                adapter.add(title);
                            }
                        }

                        adapter.notifyDataSetChanged(); // 어댑터리스트 갱신
                        list_view.setSelection(adapter.getCount() - 1);

                    }

                }.execute();

            }
        });*/

    }


}