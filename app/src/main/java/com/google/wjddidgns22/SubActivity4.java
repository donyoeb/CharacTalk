package com.google.wjddidgns22;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
public class SubActivity4 extends AppCompatActivity {

    public EditText editText; //결과를 띄어줄 TextView
    public TextView textView; //결과를 띄어줄 TextView
    public Button findbt; //reload버튼
    public String topname;
    Elements contents_name;
    Elements contents_img;
    Document doc = null;
    String Top10;//결과를 저장할 문자열변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub4);


        Intent intent2 = getIntent();
        final String id = intent2.getExtras().getString("탑이름"); // 오벨리스크 이름
        topname = id;

        Toast.makeText(SubActivity4.this, id ,Toast.LENGTH_SHORT).show();
        editText = (EditText) findViewById(R.id.edtext);
        textView = (TextView) findViewById(R.id.textBox);
        findbt = (Button) findViewById(R.id.findbt);

        findbt.setOnClickListener(new View.OnClickListener() {//onclicklistener를 연결하여 터치시 실행됨
            @Override
            public void onClick(View v) {
                new AsyncTask() {//AsyncTask객체 생성
                    @Override
                    protected Object doInBackground(Object[] params) {
                        String url;
                        url = "https://search.naver.com/search.naver?where=post&sm=tab_jum&query=";  //네이버 블로그 url
                        String serch = editText.getText().toString();  //에딧텍스트에 입력한 단어

                        int cntli = 1; // 블로그 갯수
                        try {
                            doc = Jsoup.connect(url+topname+serch).get(); //url + 탑이름 + 검색단어로 페이지를 불러옴
                            contents_name = doc.select("div[class=blog section _blogBase _prs_blg]").
                                    select("li[class=sh_blog_top]"); //필요한 녀석만 꼬집어서 지정


                            cntli++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Top10 = "";
                        int cnt = 0;//숫자를 세기위한 변수
                        for(Element element: contents_name) {
                            cnt++;
                            Top10 += cnt+". "+element.text() + "\n";
                            if(cnt == 10)//10위까지 파싱하므로
                                break;
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        Log.i("TAG",""+Top10);
                        textView.setText(Top10);
                    }
                }.execute();
            }
        });
    }
}