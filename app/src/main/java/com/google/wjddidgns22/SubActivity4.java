package com.google.wjddidgns22;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
public class SubActivity4 extends AppCompatActivity {

    private ArrayList<String> blogtitle = new ArrayList<>();   // 저장을 위한 리스트

    public int pagecnt=1;
    public EditText editText; //검색할 edittex

    public Button findbt; //찾기 버튼
    public Button matbt; //맛집
    public Button gobt; //가볼곳
    public Button datebt; //데이트

    public String topname;
    public ListView list_view;

    public String serch; //에딧텍스트에 입력한 단어
    Elements contents; // 타이틀

    Document doc = null;

        private MyAdapter adapter = new MyAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub4);

        Intent intent2 = getIntent();
        final String id = intent2.getExtras().getString("탑이름"); // 오벨리스크 이름
        topname = id;

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle(id) ;

        editText = (EditText) findViewById(R.id.edtext);
        list_view = (ListView) findViewById(R.id.rview);


        findbt = (Button) findViewById(R.id.findbt);
        findbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {

                for (pagecnt = 1; pagecnt <= 31; pagecnt = pagecnt + 10) { // i로 페이지값 넘기면서 값 가져오기

                    new AsyncTask() {//AsyncTask객체 생성

                        @SuppressLint("WrongThread")
                        @Override
                        protected Object doInBackground(Object[] params) {

                            String url, urlnum;

                            url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                            serch = editText.getText().toString();
                            urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                            //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....

                            try {
                                doc = Jsoup.connect(url + topname + serch + urlnum + pagecnt).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                                contents = doc.select("div[class=blog section _blogBase _prs_blg]");

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
                                    String img = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                    String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                    String day = elem.select("li[id=sp_blog_" + i + "]").select("dd[class=txt_inline]").text();

                                    adapter.addItem(title, img, "등록일 : " + day);
                                }
                            }
                        }
                    }.execute();

                }
                list_view.setAdapter(adapter); // 리스트뷰에 어뎁터 넣어서 출력
                list_view.setSelection(adapter.getCount()-1);

            }
        });



        adapter = new MyAdapter();
        list_view.setAdapter(adapter); // 리스트뷰에 어뎁터 넣어서 출력

        matbt = (Button) findViewById(R.id.bt1);
        matbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {

                    new AsyncTask() {//AsyncTask객체 생성

                        @SuppressLint("WrongThread")
                        @Override
                        protected Object doInBackground(Object[] params) {

                            String url, urlnum;
                             url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                            serch = "맛집";
                            urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                            //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....

                            try {
                                doc = Jsoup.connect(url + topname + serch + urlnum + pagecnt).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                                contents = doc.select("div[class=blog section _blogBase _prs_blg]");

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
                                    String img = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                    String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                    String day = elem.select("li[id=sp_blog_" + i + "]").select("dd[class=txt_inline]").text();

                                    adapter.addItem(title, img, "등록일 : " + day);

                                    blogtitle.add(title);
                                }
                            }

                            adapter.notifyDataSetChanged(); // 어댑터 갱신
                            list_view.setSelection(adapter.getCount()-1);
                        }
                    }.execute();



            }

        });

        gobt = (Button) findViewById(R.id.bt2);
        gobt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {
                for (pagecnt = 1; pagecnt <= 31; pagecnt = pagecnt + 10) { // i로 페이지값 넘기면서 값 가져오기

                    new AsyncTask() {//AsyncTask객체 생성

                        @SuppressLint("WrongThread")
                        @Override
                        protected Object doInBackground(Object[] params) {

                            String url, urlnum;

                            url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                            serch = "가볼 곳";
                            urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                            //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....

                            try {
                                doc = Jsoup.connect(url + topname + serch + urlnum + pagecnt).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                                contents = doc.select("div[class=blog section _blogBase _prs_blg]");

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
                                    String img = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                    String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                    String day = elem.select("li[id=sp_blog_" + i + "]").select("dd[class=txt_inline]").text();

                                    adapter.addItem(title, img, "등록일 : " + day);
                                }
                            }
                        }
                    }.execute();

                }
                list_view.setAdapter(adapter); // 리스트뷰에 어뎁터 넣어서 출력
                list_view.setSelection(adapter.getCount()-1);
            }
        });


        datebt = (Button) findViewById(R.id.bt3);
        datebt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {
                for (pagecnt = 1; pagecnt <= 31; pagecnt = pagecnt + 10) { // i로 페이지값 넘기면서 값 가져오기

                    new AsyncTask() {//AsyncTask객체 생성

                        @SuppressLint("WrongThread")
                        @Override
                        protected Object doInBackground(Object[] params) {

                            String url, urlnum;

                            url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                            serch = "데이트";
                            urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                            //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....

                            try {
                                doc = Jsoup.connect(url + topname + serch + urlnum + pagecnt).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                                contents = doc.select("div[class=blog section _blogBase _prs_blg]");

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
                                    String img = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                    String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                    String day = elem.select("li[id=sp_blog_" + i + "]").select("dd[class=txt_inline]").text();

                                    adapter.addItem(title, img, "등록일 : " + day);
                                }
                            }
                        }
                    }.execute();

                }

                list_view.setAdapter(adapter); // 리스트뷰에 어뎁터 넣어서 출력

                list_view.setSelection(adapter.getCount()-1);
            }
        });


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idinlist) {

                String s = blogtitle.get((int) idinlist);
               Toast.makeText(SubActivity4.this,s,Toast.LENGTH_SHORT).show();
            }
        });
    }


}