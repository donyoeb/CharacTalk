package com.google.wjddidgns22;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Button buttonmap1; //위치
    private Button buttonmap2; //주변 캐릭터
    private Button buttonmap3; //유저클릭이벤트
    private Button buttonmap4; //탑 클릭이벤트
    private Button buttonmap5; //내 정보
    private Button buttonmap6; //번역

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference mReference1;
    private DatabaseReference mReference2;
    private ChildEventListener mChild;

    private String markerTitle; // 내 닉네임
    private String markerSnippet; //내 위치 한글화버전


    private int markerflag;
    private String mycharacter;


    private ArrayList<String> Users = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Usersch = new ArrayList<>();
    private ArrayList<Double> usersw = new ArrayList<>();
    private ArrayList<Double> usersk = new ArrayList<>();

    private ArrayList<String> topnameList = new ArrayList<>();   //오벨리스크 위치값 저장을 위한 리스트
    private ArrayList<String> topstateList = new ArrayList<>();
    private ArrayList<Double> topwList = new ArrayList<>();
    private ArrayList<Double> topkList = new ArrayList<>();

    private String nicknames;
    private String password;
    private String language;



    private ArrayList<String> Users_nick = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Users_pass = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Users_lang = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Users_char = new ArrayList<>();   //유저 저장을 위한 리스트

    private String user_pa;
    private String user_la;
    private String user_ch;



    private double x,y; // 위도경도

    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;

    private Marker currentMarker = null;



    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 2000;  // 1초 마다 업데이트
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000; // 0.5초

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocatiion;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    LatLng currentPosition;

    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

///////////////////////////////////////////////////////////////////////////////////////   오벨리스크 위치값 입력

        {
            topstateList.add("top");
            topnameList.add("상명대 천안");
            topwList.add(36.832912871889924);
            topkList.add(127.17812262475492);

            topstateList.add("top");
            topnameList.add("천안 안서동");
            topwList.add(36.830874);
            topkList.add(127.177122);

            topstateList.add("top");
            topnameList.add("천안 야우리");
            topwList.add(36.819307);
            topkList.add(127.15523);
        }



///////////////////////////////////////////////////////////////////////////////////////   오벨리스크 위치값  직접 입력



        for(int i = 0 ; i<topnameList.size();i++){
            String s = topnameList.get(i);
            double w = topwList.get(i);
            double k = topkList.get(i);

            databaseReference.child("top").child(s).child("위도").setValue(w);
            databaseReference.child("top").child(s).child("경도").setValue(k);
        }


        ////////////////////////////////////////////////////////////////////////////     오벨리스크 정보 데이터베이스에 입력

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main2, null);

        Log.d(TAG, "onCreate");
        mActivity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        final String mynick = intent.getExtras().getString("닉네임");
        final String mypass = intent.getExtras().getString("비밀번호");   //나중에 로그인서비스 적용할 때 사용
        final String mychar = intent.getExtras().getString("캐릭터");    // 사용자가 선택한 캐릭터 사용하기
        final String mylang = intent.getExtras().getString("사용언어");    // 사용자가 선택한 캐릭터 사용하기

        mycharacter = mychar;
        nicknames = mynick;
        password = mypass;
        language = mylang;

        Toast.makeText(MainActivity.this, "       ★로그인 정보★"+"" +
                "\n닉네임 : " + mynick +
                "\n패스워드 : " + password +
                "\n사용언어 : " +language +
                "\n캐릭터 : "+ mycharacter,
                Toast.LENGTH_SHORT).show();

        initDatabase();


        buttonmap1 = (Button) findViewById(R.id.buttonmap1); //위치버튼
        buttonmap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(MainActivity.this, SubActivity.class); // MainActivity -> SubActivity
                intent1.putExtra("위도", x);
                intent1.putExtra("경도", y);
                intent1.putExtra("ID", nicknames);
                intent1.putExtra("캐릭터",mycharacter);
                intent1.putExtra("비밀번호",password);
                intent1.putExtra("사용언어",language);


                startActivity(intent1); // 액티비티를 시작해보아요!

            }
        });


        buttonmap2 = (Button) findViewById(R.id.buttonmap2); //위치버튼
        buttonmap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Users.clear();
                usersk.clear();
                usersw.clear();
                //cnt = 0;  // 카운트를 0으로해서 저장값 초기화하려하기엔 오버되는 사람들의 데이터는 저장되어있고 초기화가 안됨 새로운방법 필요함!
                // #문제 1  리스트로 해결

                mReference = mDatabase.getReference("유저"); // 변경값을 확인할 child 이름
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot i : dataSnapshot.getChildren()) {

                            String userw = i.child("위치").child("위도").getValue().toString(); //i번째의 위도
                            String userk = i.child("위치").child("경도").getValue().toString(); //i번쨰의 경도
                            String user_ch = i.child("캐릭터").getValue().toString();

                            double tmpw = Double.parseDouble(userw);  //위도를 double화
                            double tmpk = Double.parseDouble(userk);  //경도를 double화

                            double result = CoordDistance(x, y, tmpw, tmpk);//반경구하는식
                            //x,y는 나의 위도경도 , tmpw,tmpk는 데이터베이스 안의 모든 아이디들의 각각의 위도경도

                            result = result * 1609.344; //마일을 미터로 계산
                            result = Math.round((result * 1000) / 1000.0); // 반경소수점자리 반올림

                            if (result < 150) {   //내 위치에서의 반경내의 데이터 출력
                                //100m 반경  ,오차범위까지 +50 정도 더 해주기
                                // children 내에 있는 데이터만큼 반복합니다.

                                String usernickname = i.getKey();
                                // 데이터베이스에서 "위치"의 key 가져오기 (ex 상명대위치)

                                if (result > 5) { //5m 반경 이상인 유저 정보만 배열에 저장
                                    Usersch.add(user_ch);
                                    Users.add(usernickname);
                                    usersk.add(tmpk);
                                    usersw.add(tmpw);


                                 }
                                //주변 반경 사람들 카운트해서 배열에 저장함


                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });


        buttonmap3 = (Button) findViewById(R.id.buttonmap3); // 유저 클릭이벤트 버튼
        buttonmap3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markerflag = 1;

            }
        });
        buttonmap4 = (Button) findViewById(R.id.buttonmap4); //탑 클릭이벤트 버튼
        buttonmap4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markerflag = 0;

            }
        });

        buttonmap5 = (Button) findViewById(R.id.buttonmap5); //내정보 버튼
        buttonmap5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        mReference1 = mDatabase.getReference("유저"); // 변경값을 확인할 child 이름
                        mReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot i : dataSnapshot.getChildren()) {
                                    String n = i.getKey();
                                    String p = i.child("비밀번호").getValue().toString();
                                    String l = i.child("언어").getValue().toString();


                                    Users_nick.add(n);
                                    Users_pass.add(p);
                                    Users_lang.add(l);

                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }


                        });
                for (int i=0;i<Users_nick.size();i++){

                            String n = Users_nick.get(i);
                            String p = Users_pass.get(i);
                            String l = Users_lang.get(i);

                            if (n.equals(nicknames)){ // 나의 닉네임에 맞는 패스워드와 언어 값 가져오기
                                user_pa = p;
                                user_la = l;

                            }


                }

                if (user_pa != null && user_la !=null){
                    Intent intent3 = new Intent(MainActivity.this, SubActivity6.class); // MainActivity -> SubActivity6
                    intent3.putExtra("닉네임",markerTitle);
                    intent3.putExtra("위치", markerSnippet);
                    intent3.putExtra("캐릭터",mycharacter);
                    intent3.putExtra("비밀번호",user_pa);
                    intent3.putExtra("사용언어",user_la);

                    startActivity(intent3); // 액티비티를 시작해보아요!
                }
                else {
                  Toast.makeText(MainActivity.this,"다시 실행해주세요.",Toast.LENGTH_SHORT).show();
                }

                Users_nick.clear();
                Users_pass.clear();
                Users_lang.clear();

            }
        });


       /* buttonmap6 = (Button) findViewById(R.id.buttonmap6); //번역버튼 클릭이벤트 버튼
        buttonmap6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent4 = new Intent(MainActivity.this, SubActivity7.class); // MainActivity -> SubActivity7

                startActivity(intent4); // 액티비티를 시작해보아요!
            }
        });
*/



    }
    @Override
    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }


        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }


    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mGoogleMap.setMyLocationEnabled(true);

        }

    }



    private void stopLocationUpdates() {

        Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady :");

        mGoogleMap = googleMap;


        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동

        setDefaultLocation();   // 초기 위치 가져오기


        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));   //줌 조절 여기있~  // 카메라 줌설정 넓게는 숫자내리기 가까이는 올리기




        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){

            @Override
            public boolean onMyLocationButtonClick() {

                Log.d( TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {

                if (mMoveMapByUser == true && mRequestingLocationUpdates){

                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }

                mMoveMapByUser = true;

            }
        });


        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {


            }
        });


    }


    @Override
    public void onLocationChanged(Location location) {  // 마커타이틀,스닛펫 설정

        currentPosition = new LatLng( location.getLatitude(), location.getLongitude());

        Log.d(TAG, "onLocationChanged : ");

        markerTitle = nicknames; //< 현재위치
        markerSnippet = getCurrentAddress(currentPosition);

        x = location.getLatitude();
        y = location.getLongitude();

        mGoogleMap.clear(); // 원이 중복되서 생기는걸 방지해줌 모든낙서 지우기

        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet);

        mCurrentLocatiion = location;

    }


    @Override
    protected void onStart() {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {


        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }else{

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }


    @Override
    public void onConnectionSuspended(int cause) {

        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더...
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());//GPS를 주소로 변환

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "위치정보 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "위치정보 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        mMoveMapByUser = false;

        mGoogleMap.setOnMarkerClickListener(markerClickListener);   // 마커클릭 리스너
        if (currentMarker != null) currentMarker.remove();   //마커 중복 출력 지우기


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 내위치 마커

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());  //currentlatlng = 현재위도경도값

        MarkerOptions markerOptions = new MarkerOptions();               //현재  마커 띄우기
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);  //markertitle = 닉네임
        markerOptions.snippet(markerSnippet);  //markersnippet = 특징
        markerOptions.draggable(true);
        // 반경 원
        CircleOptions circle = new CircleOptions().center(currentLatLng) //원점  생성
                .radius(150)      //반지름 단위 : m
                .strokeWidth(1);  //선너비 0f : 선없음
        // .fillColor(Color.parseColor("#E0F8F7")); //배경색

        //원추가
        this.mGoogleMap.addCircle(circle);                    //<<<<<<<<<<<<<<<<원이 중복으로 출력되는 문제 발생 -> clear로 해결

        databaseReference.child("유저").child(nicknames).child("캐릭터").setValue(mycharacter);
        databaseReference.child("유저").child(nicknames).child("비밀번호").setValue(password);
        databaseReference.child("유저").child(nicknames).child("언어").setValue(language);
        databaseReference.child("유저").child(nicknames).child("위치").child("위도").setValue(x);
        databaseReference.child("유저").child(nicknames).child("위치").child("경도").setValue(y);
//캐릭터 생성하면서 입력된 값 자동으로 데이터베이스에 입력 , 위치값 계속 초기화

        if (mycharacter.equals("라이언")){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));

        }
        else if(mycharacter.equals("무지")){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));

        }
        else if(mycharacter.equals("튜브")){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3));

        }
        else{
            Toast.makeText(MainActivity.this,"캐릭터 설정 오류",Toast.LENGTH_SHORT).show();

        }
        currentMarker = mGoogleMap.addMarker(markerOptions);


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 주변캐릭터 마커


        for(int i = 0; i<Users.size() ; i++){  //주변사람 위치값을 기준으로 마커 띄우기
            double a,b;
            a = usersw.get(i);
            b = usersk.get(i);
            //int flag = 0 ;
            MarkerOptions markerOptions1 = new MarkerOptions();


            LatLng user = new LatLng(a,b);

            String usernickname = Users.get(i);
            String usercharacter = Usersch.get(i);

            markerOptions1.position(user);
            markerOptions1.title(usernickname);  //markertitle = 닉네임
            markerOptions1.draggable(true);

            if (usercharacter.equals("라이언")){

                markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1));   // 마커 아이콘 변경1
            }
            else if (usercharacter.equals("무지")){
                markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));   // 마커 아이콘 변경2
            }
            else if(usercharacter.equals("튜브")){
                markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3));   // 마커 아이콘 변경2
            }
            else{
                return;
            }


           mGoogleMap.addMarker(markerOptions1);

        }

        Users.clear();
        Usersch.clear();
        usersk.clear();
        usersw.clear();
        // 주변사람 마커 계속해서 찍히는 현상 제거

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  오벨리스크 마커




        int topsize = topnameList.size();              //내 위치값 기준으로 주변의 오벨리스크 마커 띄우기
        for(int i = 0; i<topsize ; i++){
            double a,b;
            a = topwList.get(i);
            b = topkList.get(i);

            double result = CoordDistance(x, y, a, b);//반경구하는식
            //x,y는 나의 위도경도 , tmpw,tmpk는 데이터베이스 안의 모든 아이디들의 각각의 위도경도

            result = result * 1609.344; //마일을 미터로 계산
            result = Math.round((result * 1000) / 1000.0); // 반경소수점자리 반올림

            if (result < 200) {   //내 위치에서의 반경내의 데이터 출력
                //탑은 반경 200미터안에 있으면 보이는걸로 반경
                MarkerOptions markerOptions2 = new MarkerOptions();
                LatLng hi = new LatLng(a,b);
                markerOptions2.position(hi);
                markerOptions2.title(topstateList.get(i));
                markerOptions2.snippet(topnameList.get(i));
                markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.top));
                mGoogleMap.addMarker(markerOptions2);

            }


        }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   유저 마커

        if ( mMoveMapByAPI ) {

            Log.d( TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude() ) ;

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }



    }


    //  마커 클릭 리스너
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {    //탑 마커 클릭리스너
        @Override
        public boolean onMarkerClick(Marker marker) {

            String markerstate = marker.getTitle();  // 탑인지 유전지 구분하기 -> 탑은 top / 유저는 유저의 닉네임
            String markername = marker.getSnippet(); // 탑 이름 /유저는 null값
            LatLng location = marker.getPosition(); // 선택한 타겟위치

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
            for (int i=0;i<Users_nick.size();i++){

                String n = Users_nick.get(i);
                String l = Users_lang.get(i);
                String k = Users_char.get(i);

                if (n.equals(markerstate)){ // 클릭한 마커의 닉네임에 맞는 캐릭터와 언어 값 가져오기

                    user_la = l;
                    user_ch = k;
                }
            }
            if(markerflag == 1){  //캐릭터 클릭이벤트
                if (markername == null){
                    if (user_la==null){//||user_ch==null
                        Toast.makeText(MainActivity.this,"한번 더 눌러주세요.",Toast.LENGTH_SHORT).show();

                    }
                    else{

                        Intent intent2 = new Intent(MainActivity.this, SubActivity5.class);// MainActivity -> SubActivity5
                        intent2.putExtra("캐릭터닉네임",markerstate);
                        intent2.putExtra("나의닉네임",nicknames);
                        intent2.putExtra("사용언어",user_la);
                        intent2.putExtra("캐릭터",user_ch);
                        intent2.putExtra("깃발","0");


                        startActivity(intent2); // 액티비티를 시작해보아요!

                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"                                ★ 오류 ★\n1. 선택에 맞는 '갱신' 버튼을 눌러주세요." +
                            "\n2. 본인 캐릭터의 정보는 내정보 버튼을 통해 확인해주세요."  ,Toast.LENGTH_SHORT).show();

                }

            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////
            else if(markerflag == 0) {   // 탑 클릭이벤트

                /*
                #문제
                 탑 클릭시 이벤트 하려면 지금처럼 리스트로 해도되나? 혹은 데이터베이스에 넣어야하나 ???????
                 */

                if (markerstate.equals("top")) {
                    Intent intent5 = new Intent(MainActivity.this, SubActivity4.class); // MainActivity -> SubActivity4
                    intent5.putExtra("탑이름", markername); //탑 이름
                    startActivity(intent5); // 액티비티를 시작해보아요!
                }
                else{
                    Toast.makeText(MainActivity.this,"                                ★ 오류 ★\n1. 선택에 맞는 '갱신' 버튼을 눌러주세요." +
                            "\n2. 본인 캐릭터의 정보는 내정보 버튼을 통해 확인해주세요.",Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(MainActivity.this,"로딩중.. 잠시만 기다려주세요"   ,Toast.LENGTH_SHORT).show();
            }

            Users_nick.clear();
            Users_char.clear();
            Users_lang.clear();
            Users.clear();

            return false;
        }
    };


    public void setDefaultLocation() {

        mMoveMapByUser = false;


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));




        currentMarker = mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION,14)); // 줌조절은 위에있음

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if ( mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {


                if ( mGoogleApiClient.isConnected() == false) {

                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }



            } else {

                checkPermissions();
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");


                        if ( mGoogleApiClient.isConnected() == false ) {

                            Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }

                break;
        }
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


    public boolean onMarkerClick(Marker marker){
        return true;
    }
}

