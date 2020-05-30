package com.google.wjddidgns22;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
    private ArrayList<String> blogurl = new ArrayList<>();


    private String picksearch; //  블로그 선택
    private String resultsearch; //

    private int[] pagecnt = {1,11,21,31,41};


    public EditText editText; //검색할 edittex

    public Button findbt; //찾기 버튼
    public Button matbt; //맛집
    public Button gobt; //가볼곳
    public Button lookbt; //데이트

    public String topname;
    public ListView list_view;

    public String search; //에딧텍스트에 입력한 단어


    Elements contents;
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
                blogtitle.clear();
                blogurl.clear();

                adapter = new MyAdapter();
                list_view.setAdapter(adapter); // 리스트뷰에 어뎁터 넣어서 출력


                new AsyncTask() {//AsyncTask객체 생성

                    @SuppressLint("WrongThread")
                    @Override
                    protected Object doInBackground(Object[] params) {

                        String url, urlnum;
                        url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                        search = editText.getText().toString();
                        urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                        //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....

                        try {
                            for (int k = 0;k<5;k++){ // 블로그 5페이지까지 가져오기 총 50개
                                doc= Jsoup.connect(url + topname + search + urlnum + pagecnt[k]).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                                contents = doc.select("div[class=blog section _blogBase _prs_blg]");
                                for (Element elem : contents) {//li[id=sp_blog_1~10]  블로그 1번~10번 저장
                                    for (int i = 1; i < 11; i++) {
                                        String img = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                        String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                        String day = elem.select("li[id=sp_blog_" + i + "]").select("dd[class=txt_inline]").text();
                                        String contents_url = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a").attr("href");


                                        adapter.addItem(title, img, "등록일 : " + day);

                                        blogtitle.add(title);
                                        blogurl.add(contents_url);
                                    }
                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);


                        adapter.notifyDataSetChanged(); // 어댑터 갱신
                    }
                }.execute();
                list_view.setSelection(adapter.getCount()-1);

            }
        });




        matbt = (Button) findViewById(R.id.bt1);
        matbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {
                    blogtitle.clear();
                blogurl.clear();
                adapter = new MyAdapter();
                list_view.setAdapter(adapter); // 리스트뷰에 어뎁터 넣어서 출력

                        new AsyncTask() {//AsyncTask객체 생성

                            @SuppressLint("WrongThread")
                            @Override
                            protected Object doInBackground(Object[] params) {

                                String url, urlnum;
                                url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                                search = "맛집";
                                urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                                //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....

                                try {
                                    for (int k = 0;k<5;k++){ // 블로그 5페이지까지 가져오기 총 50개
                                        doc= Jsoup.connect(url + topname + search + urlnum + pagecnt[k]).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                                        contents = doc.select("div[class=blog section _blogBase _prs_blg]");
                                        for (Element elem : contents) {//li[id=sp_blog_1~10]  블로그 1번~10번 저장
                                            for (int i = 1; i < 11; i++) {
                                                String img = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                                String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                                String day = elem.select("li[id=sp_blog_" + i + "]").select("dd[class=txt_inline]").text();
                                                String contents_url = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a").attr("href");


                                                adapter.addItem(title, img, "등록일 : " + day);

                                                blogtitle.add(title);
                                                blogurl.add(contents_url);
                                            }
                                        }

                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);


                                adapter.notifyDataSetChanged(); // 어댑터 갱신
                            }
                        }.execute();
                        list_view.setSelection(adapter.getCount()-1);

            }

        });

        gobt = (Button) findViewById(R.id.bt2);
        gobt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {
                blogtitle.clear();
                blogurl.clear();
                adapter = new MyAdapter();
                list_view.setAdapter(adapter); // 리스트뷰에 어뎁터 넣어서 출력


                new AsyncTask() {//AsyncTask객체 생성

                    @SuppressLint("WrongThread")
                    @Override
                    protected Object doInBackground(Object[] params) {

                        String url, urlnum;
                        url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                        search = "가볼만한곳";
                        urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                        //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....

                        try {
                            for (int k = 0;k<5;k++){ // 블로그 5페이지까지 가져오기 총 50개
                                doc= Jsoup.connect(url + topname + search + urlnum + pagecnt[k]).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                                contents = doc.select("div[class=blog section _blogBase _prs_blg]");
                                for (Element elem : contents) {//li[id=sp_blog_1~10]  블로그 1번~10번 저장
                                    for (int i = 1; i < 11; i++) {
                                        String img = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                        String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                        String day = elem.select("li[id=sp_blog_" + i + "]").select("dd[class=txt_inline]").text();
                                        String contents_url = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a").attr("href");


                                        adapter.addItem(title, img, "등록일 : " + day);

                                        blogtitle.add(title);
                                        blogurl.add(contents_url);
                                    }
                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);


                        adapter.notifyDataSetChanged(); // 어댑터 갱신
                    }
                }.execute();
                list_view.setSelection(adapter.getCount()-1);

            }
        });


        lookbt = (Button) findViewById(R.id.bt3);
        lookbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {
                blogtitle.clear();
                blogurl.clear();
                adapter = new MyAdapter();
                list_view.setAdapter(adapter); // 리스트뷰에 어뎁터 넣어서 출력


                new AsyncTask() {//AsyncTask객체 생성

                    @SuppressLint("WrongThread")
                    @Override
                    protected Object doInBackground(Object[] params) {

                        String url, urlnum;
                        url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                        search = "볼거리";
                        urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                        //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....
                        try {
                            for (int k = 0;k<5;k++){   // 블로그 5페이지까지 가져오기 총 50개
                                doc= Jsoup.connect(url + topname + search + urlnum + pagecnt[k]).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                                contents = doc.select("div[class=blog section _blogBase _prs_blg]");
                                for (Element elem : contents) {//li[id=sp_blog_1~10]  블로그 1번~10번 저장
                                    for (int i = 1; i < 11; i++) {
                                        String img = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a img").attr("src");
                                        String title = elem.select("li[id=sp_blog_" + i + "]").select("a[title]").text();
                                        String day = elem.select("li[id=sp_blog_" + i + "]").select("dd[class=txt_inline]").text();
                                        String contents_url = elem.select("li[id=sp_blog_" + i + "]").select("div[class=thumb thumb-rollover] a").attr("href");


                                        adapter.addItem(title, img, "등록일 : " + day);

                                        blogtitle.add(title);
                                        blogurl.add(contents_url);
                                    }
                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);


                        adapter.notifyDataSetChanged(); // 어댑터 갱신
                    }
                }.execute();
                list_view.setSelection(adapter.getCount()-1);

            }
        });


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {    //클릭이벤트

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idinlist) {

                picksearch = blogtitle.get((int) idinlist);
                resultsearch = blogurl.get((int) idinlist);
               Toast.makeText(SubActivity4.this,picksearch,Toast.LENGTH_SHORT).show();

                String url = resultsearch;
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });
    }


}

