package com.google.wjddidgns22;

import android.annotation.SuppressLint;
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
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
public class SubActivity4 extends AppCompatActivity {


    public EditText editText; //검색할 edittext
   // public TextView textView; //결과를 띄어줄 TextView
    public Button findbt; //찾기 버튼
    public Button matbt; //맛집

    public String topname;
    public ListView list_view;

    public String serch; //에딧텍스트에 입력한 단어
    Elements contents; // 타이틀

    Document doc = null;
    String Top10;//결과를 저장할 문자열변수

    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    String k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub4);

        Intent intent2 = getIntent();
        final String id = intent2.getExtras().getString("탑이름"); // 오벨리스크 이름
        topname = id;

        Toast.makeText(SubActivity4.this, id ,Toast.LENGTH_SHORT).show(); // 이름 보여주기

        editText = (EditText) findViewById(R.id.edtext);

        list_view = (ListView)findViewById(R.id.lview);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        list_view.setAdapter(adapter);



        matbt = (Button) findViewById(R.id.matjip);
        matbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {

                Array.clear();
                adapter.clear();

                new AsyncTask() {//AsyncTask객체 생성
                    @SuppressLint("WrongThread")
                    @Override
                    protected Object doInBackground(Object[] params) {

                        String url,urlnum;
                        //for (int i=1;i<102;i=i+10) { }
                        url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                        serch = "맛집";
                        //   urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                        //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....
                        //  int urlcnt = 1;

                        //  urlcnt= urlcnt+10;
                        try {
                            doc = Jsoup.connect(url+topname+serch).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                            contents = doc.select("div[class=blog section _blogBase _prs_blg]");
                            int contentsize = contents.size();
                            for (Element elem : contents){//li[id=sp_blog_1~10]
                                for (int i = 1;i<11;i++){
                                    String title = elem.select("li[id=sp_blog_"+i+"]").select("a[title]").text();
                                    Array.add(title);
                                }
                            }


                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }



                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        adapter.notifyDataSetChanged(); // 어댑터리스트 갱신
                        list_view.setSelection(adapter.getCount() - 1);

                        Toast.makeText(SubActivity4.this,"검색결과: "+Array.size(),Toast.LENGTH_LONG).show();
                    }
                }.execute();


            }

        });


/*
        findbt = (Button) findViewById(R.id.findbt); //검색버튼
        findbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨

            @Override
            public void onClick(View v) {
                adapter.clear();
                Array.clear();
                new AsyncTask() {//AsyncTask객체 생성
                    @SuppressLint("WrongThread")
                    @Override
                    protected Object doInBackground(Object[] params) {
                        String url,urlnum;
                        url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url

                        serch = editText.getText().toString();
                     //   urlnum = "&sm=tab_pge&srchby=all&st=sim&where=post&start=";
                        //&sm=tab_pge&srchby=all&st=sim&where=post&start=1  // 1~10, 11~20. 21~30.....
                      //  int urlcnt = 1;

                        int cnt = 0;//숫자를 세기위한 변수
                        try {
                            doc = Jsoup.connect(url+topname+serch).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                            contents= doc.select("div[class=blog section _blogBase _prs_blg]")
                                    .select("li[class=sh_blog_top]").select("id.sp_blog_1").select("a[title]"); //필요한 녀석만 꼬집어서 지정



                          //  urlcnt= urlcnt+10;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        for(Element element: contents) {
                            cnt++;
                            k = element.text();
                        //      Array.add(element.text()); // array에 저장
                        //    adapter.add(element.text()); //adapter에 저장

                        if(cnt == 10)//10위까지 파싱하므로
                            break;
                    }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                     //   adapter.notifyDataSetChanged(); // 어댑터리스트 갱신
                  //      list_view.setSelection(adapter.getCount() - 1);
                       // textView.setText(Top10);

                        Toast.makeText(SubActivity4.this,"검색결과: "+ Array.size()+k,Toast.LENGTH_LONG).show();
                    }
                }.execute();


            }
        });*/
    }


}