package com.google.wjddidgns22;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity { // 채팅버튼 들어오면 기능들

    private Button subb1;
    private Button subb2;
    private Button subb3;
    private Button msgbt;


    private ArrayList<String> Users_nick = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Users_pass = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Users_lang = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Users_char = new ArrayList<>();   //유저 저장을 위한 리스트

    private ArrayList<String> insideuser = new ArrayList<>();   //유저 저장을 위한 리스트

    private String user_pa;
    private String user_la;
    private String user_ch;


    private ListView subl1;
    private EditText edt1;

    private String mynick;
    private String flag;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference mReference2;
    private ChildEventListener mChild;

    private int cnt=0;
    private int inaround=0;
    private String[] Users = new String[100];

    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        subl1 = (ListView) findViewById(R.id.sublist);
        edt1 = (EditText)findViewById(R.id.edittext1);

        subb1 = (Button)findViewById(R.id.subbutton1);
        subb2 = (Button)findViewById(R.id.subbutton2);
        subb3 = (Button)findViewById(R.id.subbutton3);
        msgbt = (Button)findViewById(R.id.messagebt) ;


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        subl1.setAdapter(adapter);

        final Intent intent1 = getIntent();
        final double x = intent1.getExtras().getDouble("위도");
        final double y = intent1.getExtras().getDouble("경도");
        final String id = intent1.getExtras().getString("ID");



        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("나의 닉네임 : "+ id) ;
        mynick = id;

        initDatabase();


        subb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inaround = 0;
                cnt=0;

                mReference = mDatabase.getReference("유저"); // 변경값을 확인할 child 이름
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        adapter.clear();

                        for (DataSnapshot i : dataSnapshot.getChildren()) {

                            String userw = i.child("위치").child("위도").getValue().toString(); //i번째의 위도
                            String userk = i.child("위치").child("경도").getValue().toString(); //i번쨰의 경도

                            double tmpw = Double.parseDouble(userw);  //위도를 double화
                            double tmpk = Double.parseDouble(userk);  //경도를 double화

                            double result = CoordDistance(x,y,tmpw,tmpk);//반경구하는식
                            //x,y는 나의 위도경도 , tmpw,tmpk는 데이터베이스 안의 모든 아이디들의 각각의 위도경도
                            result = result*1609.344; //마일을 미터로 계산
                            result = Math.round((result*1000)/1000.0); // 반경소수점자리 반올림

                            if (result<150) {   //내 위치에서의 반경내의 데이터 출력
                                //100m 반경  ,오차범위까지 +50 정도 더 해주기
                                String msg = i.getKey(); // 데이터베이스에서 "위치"의 key 가져오기 (유저 닉네임)

                                tmpk = tmpk*100;
                                tmpw = tmpw*100;
                                int k = (int)tmpk;
                                int w = (int)tmpw;
                                inaround = inaround+k+w;  //주변사람들과의 대화를 위해 저장

                                Users[cnt]=msg;
                                cnt++;
                                if(result<5){
                                    insideuser.add(msg);
                                }


                                int flg=0;
                                int listcnt=0;

                                while(listcnt <= insideuser.size()-1){

                                    if(msg.equals(insideuser.get(listcnt))){

                                        if (msg.equals(id)){
                                            flg = 1;        //내 아이디는 리스트에서 제외
                                        }
                                        else{
                                            Array.add(msg+"\t(in 5m)"); // 5m 이내 사람들 따로 표시하기
                                            adapter.add(msg+"\t(in 5m)");

                                            flg=1;
                                            break;

                                        }

                                    }
                                    listcnt++;
                                }

                                if(flg==0) {  // 5m 밖의 사람들 그냥 닉네임만 저장
                                    Array.add(msg); // array에 저장
                                    adapter.add(msg); //adapter에 저장
                                }


                            }
                        }
                        adapter.notifyDataSetChanged(); // 어댑터리스트 갱신
                        subl1.setSelection(adapter.getCount() - 1);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




                mReference2 = mDatabase.getReference("유저"); // 변경값을 확인할 child 이름
                mReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot i : dataSnapshot.getChildren()) {

                            String n = i.getKey();
                            String l = i.child("언어").getValue().toString();
                            String k = i.child("캐릭터").getValue().toString();

                            Users_nick.add(n);
                            Users_lang.add(l);
                            Users_char.add(k);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }

                });
            }

        });

        subb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cnt==0){
                    Toast.makeText(SubActivity.this, "주변 닉네임을 먼저 띄우세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent2 = new Intent(SubActivity.this, SubActivity2.class); // MainActivity -> SubActivity2
                    intent2.putExtra("ID", id);
                    intent2.putExtra("카운트", cnt);
                    intent2.putExtra("주변",inaround);

                    startActivity(intent2);
                }
            }
        });

        subb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String chatname = edt1.getText().toString();

                if (chatname.getBytes().length <= 0) {//빈값이 넘어올때의 처리
                    Toast.makeText(SubActivity.this, "채팅방 이름을 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else {// 깃발이 0일때는 1:1 대화 신청 , 1일땐 채팅방 이름 입력해서 들어가는 방 만들기용
                    flag = "1";
                    Intent intent3 = new Intent(SubActivity.this, SubActivity3.class); //  SubActivity -> subac3
                    intent3.putExtra("유저닉네임",chatname);
                    intent3.putExtra("내닉네임",mynick);
                    intent3.putExtra("깃발",flag);

                    startActivity(intent3);

                    flag="0";
                }
            }
        });

        subl1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idinlist) {

                String s = Users[position+1]; // 내닉네임 빼고 +1 부터

                for (int i=0;i<Users_nick.size();i++){

                    String n = Users_nick.get(i);
                    String l = Users_lang.get(i);
                    String k = Users_char.get(i);

                    if (n.equals(s)){ // 클릭한 마커의 닉네임에 맞는 캐릭터와 언어 값 가져오기

                        user_la = l;
                        user_ch = k;
                    }
                }

                if (mynick.equals(s)){
                    Toast.makeText(SubActivity.this, "본인의 정보는 '내정보' 버튼을 통해 확인 및 변경이 가능합니다!",Toast.LENGTH_SHORT).show();

                }
                else {// 깃발이 0일때는 1:1 대화 신청 , 1일땐 채팅방 이름 입력해서 들어가는 방 만들기용
                    flag ="0";
                    Intent intent = new Intent(SubActivity.this, SubActivity5.class);// MainActivity -> SubActivity5
                    intent.putExtra("캐릭터닉네임",s);
                    intent.putExtra("사용언어",user_la);
                    intent.putExtra("캐릭터",user_ch);
                    intent.putExtra("나의닉네임",mynick);
                    intent.putExtra("깃발",flag);


                    startActivity(intent);
                    flag="0";
                }


            }
        });


        msgbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(SubActivity.this, SubActivity8.class);// subActivity -> SubActivity8

                intent4.putExtra("나의닉네임",mynick);

                startActivity(intent4);
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

    double CoordDistance(double latitude1, double longitude1, double latitude2, double longitude2)
    {
        return 6371 * Math.acos(
                Math.sin(latitude1) * Math.sin(latitude2)
                        + Math.cos(latitude1) * Math.cos(latitude2) * Math.cos(longitude2 - longitude1))/100;
    }




}

