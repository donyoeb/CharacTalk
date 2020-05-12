package com.google.wjddidgns22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class Main2Activity extends AppCompatActivity {

    EditText etname;
    EditText etpass;
    Spinner etchar;
    EditText etlang;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        etname = findViewById(R.id.name);
        etpass = findViewById(R.id.pass);
        etchar = findViewById(R.id.character);
        etlang = findViewById(R.id.lang);



        Button btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stname = etname.getText().toString();
                String stpass = etpass.getText().toString();
                String stchar = etchar.getSelectedItem().toString();
                String stlang = etlang.getText().toString();




                if(stname.getBytes().length <= 0 || stpass.getBytes().length <=0 ){//빈값이 넘어올때의 처리

                    Toast.makeText(Main2Activity.this, "닉네임과 패스워드를 입력하세요.",Toast.LENGTH_SHORT).show();
                }

                else{

                 //   if (mypass.equals(stpass)){

                        Intent intententer = new Intent(Main2Activity.this, MainActivity.class); // 로그인화면 -> 맵화면
                        intententer.putExtra("닉네임",stname);
                        intententer.putExtra("비밀번호",stpass);
                        intententer.putExtra("캐릭터",stchar);
                        intententer.putExtra("사용언어",stlang);

                        startActivity(intententer);

                    //}
                  //  else{
                  //      Toast.makeText(Main2Activity.this,"비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                   // }


                }

            }
        });
    }
}
