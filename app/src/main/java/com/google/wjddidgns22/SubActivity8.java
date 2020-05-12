package com.google.wjddidgns22;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity8 extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private String nickname;

    private String flag;

    private Integer cnt =0;
    private String[] Users = new String[100];//유저 저장을 위한 리스트
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub8);


        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("나에게 온 1:1 메시지") ;
        listView = (ListView) findViewById(R.id.mylist);

        Intent intent3 = getIntent();
        final String mynick = intent3.getExtras().getString("나의닉네임");
        nickname = mynick;

        initDatabase();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);

        mReference = mDatabase.getReference("message").child(nickname); // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adapter.clear();
                cnt =0;

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                        String msgname = i.getKey();

                        if (msgname.equals("검색해서들어온채팅방")){  //검색해서 들어온 채팅방은 1:1 채팅방이 아닌 누구나 들어올수있는 방임으로 제외

                        }
                        else{
                            Users[cnt] = msgname;     // 인텐트로 보내기위해 상대방 닉네임 저장.
                            cnt++;
                            Array.add(msgname);
                            adapter.add(msgname);       // 데이터베이스에있는 채팅방들 어뎁터에 저장해서 출력

                        }

                    }

                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idinlist) {
                String s = Users[(int)idinlist];

                flag ="2"; // 상대방 메시지 확인하는건 flag 2
                Intent intent = new Intent(SubActivity8.this, SubActivity3.class);// subActivity -> SubActivity3
                intent.putExtra("유저닉네임",s);
                intent.putExtra("내닉네임",nickname);
                intent.putExtra("깃발",flag);

                startActivity(intent);
                flag="0";
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
}