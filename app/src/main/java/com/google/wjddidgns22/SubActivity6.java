package com.google.wjddidgns22;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity6 extends AppCompatActivity { //내정보 변경

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private TextView user_now;
    private TextView user_name;
    private TextView my_char;
    private TextView now_pass;
    private TextView now_lang;

    private EditText user_pass;
    private EditText user_lang;

    private Button change_pass;
    private Button change_lang;

    private String character;

    ImageView user_img;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub6);


        Intent intent = getIntent();
        final String nickname = intent.getExtras().getString("닉네임");
        final String user_position = intent.getExtras().getString("위치");
        final String user_character = intent.getExtras().getString("캐릭터");
        final String user_pa = intent.getExtras().getString("비밀번호");
        final String user_la = intent.getExtras().getString("사용언어");

        ActionBar ab = getSupportActionBar();
        ab.setTitle("내 (" + nickname + ") 정보확인 및 수정하기"); //타이틀 설정

        character = user_character;


        user_img = (ImageView) findViewById(R.id.userimg);
        user_name = (TextView) findViewById(R.id.user_nickname); //현재 닉네임
        user_now = (TextView) findViewById(R.id.user_now); //현재 위치
        my_char = (TextView) findViewById(R.id.user_char);
        now_pass = (TextView) findViewById(R.id.now_pass); // 현재 패스워드
        now_lang = (TextView) findViewById(R.id.now_lang); // 현재 사용가능언어


        user_pass = (EditText) findViewById(R.id.user_passward); // 바꿀 비번 입력
        user_lang = (EditText) findViewById(R.id.user_language); // 바꿀 언어 입력

        change_pass = (Button) findViewById(R.id.change_pass); // 번호 바꾸기 버튼
        change_lang = (Button) findViewById(R.id.change_lang); // 언어 바꾸기 버튼

/////////////////////////////////////인텐트로 가져온 값 입력
        user_name.setText(nickname);
        user_now.setText(user_position);
        my_char.setText(user_character);
        now_pass.setText(user_pa);
        now_lang.setText(user_la);

///////////////////////////////////////////////////
        if (character.equals("라이언")) {
            user_img.setImageResource(R.drawable.marker1);
        }
        else if (character.equals("무지")) {
            user_img.setImageResource(R.drawable.marker2);
        }
        else if (character.equals("튜브")) {
            user_img.setImageResource(R.drawable.marker3);
        }
        else{
            return;
        }

///////////////////////////////////////////////
        change_pass.setOnClickListener(new View.OnClickListener() {   // 패스워드 변경 버튼
            @Override
            public void onClick(View view) {

                String pass = user_pass.getText().toString();
                databaseReference.child("유저").child(nickname).child("비밀번호").setValue(pass);

                now_pass.setText(pass);

                Toast.makeText(SubActivity6.this, "비밀번호 변경완료", Toast.LENGTH_SHORT).show();


            }
        });


        change_lang.setOnClickListener(new View.OnClickListener() {   // 사용언어 변경 버튼
            @Override
            public void onClick(View view) {

                String lang = user_lang.getText().toString();
                databaseReference.child("유저").child(nickname).child("언어").setValue(lang);

                now_lang.setText(lang);


                Toast.makeText(SubActivity6.this, "사용언어 변경완료", Toast.LENGTH_SHORT).show();

            }
        });



    }
}











