package com.google.wjddidgns22;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

public class SubActivity3 extends AppCompatActivity{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);


        sendbt = (Button) findViewById(R.id.buttonchat);
        editdt = (EditText) findViewById(R.id.messagetext);
        listView = (ListView) findViewById(R.id.listviewmsg);

        Intent intent3 = getIntent();

        final String mynick = intent3.getExtras().getString("내닉네임");
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



        sendbt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 버튼 누르면 수행 할 명령
                msg = editdt.getText().toString();
                if(flag.equals("0")){
                    databaseReference.child("message").child(usernickname).child(mynick).push().setValue(mynick+" : "+msg);
                }
                else if (flag.equals("2")){
                    databaseReference.child("message").child(mynickname).child(usernickname).push().setValue(mynick+" : "+msg);
                }
                else{
                    databaseReference.child("message").child(usernickname).child("검색해서들어온채팅방").push().setValue(mynick+" : "+msg);
                }
               editdt.setText("");
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
}



