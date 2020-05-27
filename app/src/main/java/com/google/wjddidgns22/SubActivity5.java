package com.google.wjddidgns22;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity5 extends AppCompatActivity{


    ImageView user_img;
    TextView nicknametext;
    TextView languagetext;

    private String mynickname;
    private String usernickname;

    private Button chatbutton;
    private String fflag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub5);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sub5);

        //UI 객체생성
        nicknametext = (TextView) findViewById(R.id.nicktext);
        languagetext = (TextView) findViewById(R.id.langtext);
        user_img = (ImageView) findViewById(R.id.userimg);
        chatbutton = (Button)findViewById(R.id.chatbt);

        //데이터 가져오기
        final Intent intent = getIntent();
        String nick = intent.getStringExtra("캐릭터닉네임");
        String lang = intent.getStringExtra("사용언어");
        String character = intent.getStringExtra("캐릭터");
        String mynick = intent.getStringExtra("나의닉네임");
        final String flag = intent.getStringExtra("깃발");

        mynickname = mynick;
        usernickname = nick;


        nicknametext.setText(nick);
        languagetext.setText(lang);

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
        if (flag.equals("0") || flag.equals(null)){
            fflag = "0";
        }
        else{
            fflag ="1";
        }

        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(SubActivity5.this, SubActivity3.class);// MainActivity5 -> SubActivity3
                intent2.putExtra("내닉네임",mynickname);
                intent2.putExtra("유저닉네임",usernickname);
                intent2.putExtra("깃발",fflag);


                startActivity(intent2); // 액티비티를 시작해보아요!


            }
        });

    }


    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}



