package com.google.wjddidgns22;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    private EditText etname;
    private EditText etpass;
    private Spinner etchar;
    private EditText etlang;
    private Button btnloginnew;
    private Button btnlogin;


    private String stname;
    private String stpass;
    private String stchar;
    private String stlang;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ArrayList<String> Users_nick = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Users_pass = new ArrayList<>();   //유저 저장을 위한 리스트

    private String mypass; //데이터베이스에 저장된 패스워드


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        etname = (EditText) findViewById(R.id.name);
        etpass = (EditText) findViewById(R.id.pass);
        etchar = (Spinner) findViewById(R.id.character);
        etlang = (EditText) findViewById(R.id.lang);

        Handler h = new Handler();

        initDatabase();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users_nick.clear();
                Users_pass.clear();

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    String nick = i.getKey();
                    String pass = i.child("비밀번호").getValue().toString();

                    Users_nick.add(nick);
                    Users_pass.add(pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final Toast toast = Toast.makeText(Main2Activity.this, "데이터베이스 정보 가져오는 중...",Toast.LENGTH_SHORT);
        new CountDownTimer(3000, 1000) {        //토스트 딜레이 걸어주기
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.show();}
        }.start();

        h.postDelayed(new Runnable(){

            public void run(){

                btnloginnew = (Button) findViewById(R.id.btnlogin1);
                btnloginnew.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        stname = etname.getText().toString();
                        stpass = etpass.getText().toString();
                        stchar = etchar.getSelectedItem().toString();
                        stlang = etlang.getText().toString();
                        if(stname.length() <= 0 || stpass.length() <= 0 ){//빈값이 넘어올때의 처리

                            Toast.makeText(Main2Activity.this, "닉네임과 패스워드를 입력하세요.",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            int flag = 0; //아이디 있는지 없는지 판단

                            for (int i = 0; i < Users_nick.size(); i++) {
                                String n = Users_nick.get(i);
                                String p = Users_pass.get(i);

                                if (n.equals(stname)) { //데이터베이스에 내 아이디가 있는경우
                                    flag = 1; // 아이디는 있어서 깃발 세워주기

                                    Toast.makeText(Main2Activity.this, "기존 사용자는 '로그인' 버튼을 눌러주세요 ",Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (flag == 0){

                                Intent intent1 = new Intent(Main2Activity.this, MainActivity.class); // 로그인화면 -> 맵화면
                                intent1.putExtra("닉네임", stname);
                                intent1.putExtra("비밀번호", stpass);
                                intent1.putExtra("캐릭터", stchar);
                                intent1.putExtra("사용언어", stlang);

                                startActivity(intent1);

                                finish();

                            }

                        }
                    }
                });

                btnlogin = (Button) findViewById(R.id.btnlogin2);
                btnlogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        stname = etname.getText().toString();
                        stpass = etpass.getText().toString();
                        stchar = etchar.getSelectedItem().toString();
                        stlang = etlang.getText().toString();
                        if(stname.length() <= 0 || stpass.length() <= 0 ){//빈값이 넘어올때의 처리

                            Toast.makeText(Main2Activity.this, "닉네임과 패스워드를 입력하세요.",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            int flag = 0; //아이디 있는지 없는지 판단

                            for (int i = 0; i < Users_nick.size(); i++) {
                                String n = Users_nick.get(i);
                                String p = Users_pass.get(i);

                                if (n.equals(stname)) { //데이터베이스에 내 아이디가 있는경우
                                    flag = 1; // 아이디는 있어서 깃발 세워주기
                                    mypass = p;  //내 아이디의 비밀번호 가져오기

                                    if (mypass.equals(stpass)) {//가져온 비번과 입력한 비번이 같으면 로그인완료

                                        Intent intent2 = new Intent(Main2Activity.this, MainActivity.class); // 로그인화면 -> 맵화면
                                        intent2.putExtra("닉네임", stname);
                                        intent2.putExtra("비밀번호", stpass);
                                        intent2.putExtra("캐릭터", stchar);
                                        intent2.putExtra("사용언어", stlang);

                                        startActivity(intent2);
                                        finish();
                                    }
                                    else{ //틀리면 로그인 x
                                        Toast.makeText(Main2Activity.this, "비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            if (flag == 0){
                                Toast.makeText(Main2Activity.this, "신규 로그인은 '신규가입' 버튼을 눌러주세요",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }

        }, 3000); //정지할 시간이 1초라면 1000을 입력한다.


    }



    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference().child("유저");

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
}
