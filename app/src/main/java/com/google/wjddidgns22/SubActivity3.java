package com.google.wjddidgns22;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity3 extends AppCompatActivity{  //1:1 대화 및 메시지함

    private Button sendbt;
    private EditText editdt;
    public String msg;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    private String mynickname;
    private String usernickname;
    private String flag;
    private String mynick;


    private Spinner insert_lang; // 입력 국가 언어 스피너
    private Spinner result_lang; // 출력 국가 언어 스피너


    private String sourcelang1;
    private String targetlang1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);


        sendbt = (Button) findViewById(R.id.buttonchat);
        editdt = (EditText) findViewById(R.id.messagetext);
        listView = (ListView) findViewById(R.id.listviewmsg);
        insert_lang = (Spinner)findViewById(R.id.insert_lang);
        result_lang = (Spinner)findViewById(R.id.result_lang);


        Intent intent3 = getIntent();

        mynick = intent3.getExtras().getString("내닉네임");
        final String usernick = intent3.getExtras().getString("유저닉네임");

        final String fl = intent3.getExtras().getString("깃발");

        flag = fl;
        mynickname = mynick;
        usernickname = usernick;
        if (flag.equals("0")){

            ActionBar ab = getSupportActionBar() ;
            ab.setTitle(usernickname+" 와의 대화방");
        }
        else if (flag.equals("2")){

            ActionBar ab = getSupportActionBar() ;
            ab.setTitle(usernickname+" 와의 대화방");
        }
        else{

            ActionBar ab = getSupportActionBar() ;
            ab.setTitle("입장한 채팅방 : "+usernickname);

        }

        initDatabase();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);
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


        sendbt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                String k = editdt.getText().toString();


                //소스에 입력된 내용이 있는지 체크
                if(k.length() == 0) {
                    Toast.makeText(SubActivity3.this, "번역할 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    editdt.requestFocus();
                    return;
                }


                if (sourcelang1.equals(targetlang1)){
                    msg = k;

                    if (flag.equals("0")) {   //데이터베이스에 추가
                        databaseReference.child("message").child(usernickname).child(mynick).push().setValue(mynick + " : " +
                                msg);
                    } else if (flag.equals("2")) {
                        databaseReference.child("message").child(mynickname).child(usernickname).push().setValue(mynick + " : " +
                                msg);
                    } else {
                        databaseReference.child("message").child(usernickname).child("검색해서들어온채팅방").push().setValue(mynick + " : " +
                                msg);
                    }

                    editdt.setText("");

                }
                else{
                    //실행버튼을 클릭하면 AsyncTask를 이용 요청하고 결과를 반환받아서 화면에 표시
                    SubActivity3.NaverTranslateTask asyncTask = new SubActivity3.NaverTranslateTask();

                    msg = k; // 에딧텍스트 내용 가져오기
                    asyncTask.execute(msg);

                    editdt.setText("");
                    Toast.makeText(SubActivity3.this,sourcelang1 +"  ->  "+ targetlang1,Toast.LENGTH_SHORT).show();


                }
            }
        });

        mReference = mDatabase.getReference("message"); // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                if(flag.equals("0")){
                    for (DataSnapshot messageData : dataSnapshot.child(usernickname).child(mynick).getChildren()) {

                        // child 내에 있는 데이터만큼 반복합니다.

                        String msg2 = messageData.getValue().toString();
                        Array.add(msg2);
                        adapter.add(msg2);
                    }
                }
                else if (flag.equals("2")) {
                    for (DataSnapshot messageData : dataSnapshot.child(mynickname).child(usernickname).getChildren()) {

                        // child 내에 있는 데이터만큼 반복합니다.

                        String msg2 = messageData.getValue().toString();
                        Array.add(msg2);
                        adapter.add(msg2);
                    }
                }
                else {
                    for (DataSnapshot messageData : dataSnapshot.child(usernickname).child("검색해서들어온채팅방").getChildren()) {

                        // child 내에 있는 데이터만큼 반복합니다.

                        String msg2 = messageData.getValue().toString();
                        Array.add(msg2);
                        adapter.add(msg2);
                    }
                }

                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference();


        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
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
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                String postParams = "source=" + sourceLang + "&target=" + targetLang + "&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
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
            SubActivity3.NaverTranslateTask.TranslatedItem items = gson.fromJson(rootObj.toString(), SubActivity3.NaverTranslateTask.TranslatedItem.class);

            if (flag.equals("0")) {   //데이터베이스에 추가
                databaseReference.child("message").child(usernickname).child(mynick).push().setValue(mynick + " : " + items.getTranslatedText());
            } else if (flag.equals("2")) {
                databaseReference.child("message").child(mynickname).child(usernickname).push().setValue(mynick + " : " + items.getTranslatedText());
            } else {
                databaseReference.child("message").child(usernickname).child("검색해서들어온채팅방").push().setValue(mynick + " : " + items.getTranslatedText());
            }

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



