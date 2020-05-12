package com.google.wjddidgns22;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity7 extends AppCompatActivity {

    private Button btTranslate;
    private EditText etSource;
    private TextView tvResult;

    private String stext;

    private Spinner insert_lang; // 입력 국가 언어 스피너
    private Spinner result_lang; // 출력 국가 언어 스피너

    private String sourcelang1;
    private String targetlang1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub7);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("번역 기능") ; //타이틀 설정


        etSource = (EditText) findViewById(R.id.et_source);
        tvResult = (TextView) findViewById(R.id.tv_result);
        btTranslate = (Button) findViewById(R.id.bt_translate);

        insert_lang = (Spinner)findViewById(R.id.insert_lang);
        result_lang = (Spinner)findViewById(R.id.result_lang);


        //1 = 한국   / 2= 영어  / 3= 일본어 / 4= 중국어
        insert_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                switch(i){
                    case 0:
                        sourcelang1 = "ko";
                        break;
                    case 1:
                        sourcelang1 = "en";
                        break;
                    case 2:
                        sourcelang1 = "ja";
                        break;
                    case 3:
                        sourcelang1 = "zh-CN";
                        break;
                }
               // ko , en , ja, zh-CN
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        result_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(i){
                    case 0:
                        targetlang1 = "ko";
                        break;
                    case 1:
                        targetlang1 = "en";
                        break;
                    case 2:
                        targetlang1= "ja";
                        break;
                    case 3:
                        targetlang1 = "zh-CN";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });




        //번역 실행버튼 클릭이벤트
        btTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //소스에 입력된 내용이 있는지 체크
                if(etSource.getText().toString().length() == 0) {
                    Toast.makeText(SubActivity7.this, "번역할 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    etSource.requestFocus();
                    return;
                }

                Toast.makeText(SubActivity7.this,sourcelang1 +"  ->  "+ targetlang1,Toast.LENGTH_SHORT).show();


                if(sourcelang1 ==targetlang1){
                    Toast.makeText(SubActivity7.this,"서로 다른 언어를 입력해주세요.",Toast.LENGTH_SHORT).show();

                    return;
                }
                //실행버튼을 클릭하면 AsyncTask를 이용 요청하고 결과를 반환받아서 화면에 표시
                NaverTranslateTask asyncTask = new NaverTranslateTask();

               stext = etSource.getText().toString(); // 에딧텍스트 내용 가져오기
                asyncTask.execute(stext);

            }
        });


    }

    public class NaverTranslateTask extends AsyncTask<String, Void, String> {

        String clientId = "7s1AF5z_u63yZufU9dM6";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "FAANnMijUu";//애플리케이션 클라이언트 시크릿값";

        // ko , en , ja, zh-CN   언어설정
        String sourceLang;
        String targetLang;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sourceLang = sourcelang1;
            targetLang = targetlang1;

        }

        //AsyncTask 메인처리
        @Override
        protected String doInBackground(String... strings) {

            String sourceText = strings[0];

            try {
                String text = URLEncoder.encode(sourceText, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                String postParams = "source="+sourceLang+"&target="+targetLang+"&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else { // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                return response.toString();

            } catch (Exception e) {
                Log.d("error", e.getMessage());
                return null;
            }
        }

        //번역된 결과를 받아서 처리
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //JSON데이터를 자바객체로 변환
            //Gson을 사용
            Gson gson = new GsonBuilder().create();
            JsonParser parser = new JsonParser();
            JsonElement rootObj = parser.parse(s.toString())
            //원하는 데이터 까지 찾아 들어간다.
                    .getAsJsonObject().get("message")
                    .getAsJsonObject().get("result");
            //안드로이드 객체에 담기
            TranslatedItem items = gson.fromJson(rootObj.toString(), TranslatedItem.class);

            //번역결과를 결과 텍스트뷰에 넣는다.
            tvResult.setText(items.getTranslatedText());
        }

        //자바용 그릇
        private class TranslatedItem {
            String translatedText;

            public String getTranslatedText() {
                return translatedText;
            }
        }
    }


}


