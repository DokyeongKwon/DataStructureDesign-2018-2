package com.example.dkdk6.ddarawa;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkdk6 on 2018-09-23.
 */

public class MainActivity extends AppCompatActivity {
    private ArrayList<City> RcityList = new ArrayList<City>();
    String select_finedust="fineDust";
    City myLocation;
    String select="city";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RcityList = getIntent().getParcelableArrayListExtra("key");

        final EditText address = (EditText)findViewById(R.id.address);
        Button startBtn = (Button)findViewById(R.id.main_startBtn);
        final Geocoder geocoder = new Geocoder(this);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!address.getText().toString().equals(null)){
                    String temp = address.getText().toString();
                    List<Address> list = null;
                    String str = address.getText().toString();
                    try {
                        list = geocoder.getFromLocationName(
                                str, // 지역 이름
                                10); // 읽을 개수
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                    }

                    if (list != null) {
                        if (list.size() == 0) {
                            Toast.makeText(getApplicationContext(), "주소를 다시 입력하세요!", Toast.LENGTH_LONG).show();
                        } else {
                            //Log.d("ADDRESS", list.get(0).getAddressLine(0).toString());
                            selectCityAlgorithm(list.get(0).getAddressLine(0).toString());
                            Intent intent = new Intent(MainActivity.this, TagActivity.class);
                            intent.putExtra("city", select+":"+select_finedust);
                            startActivity(intent);
                            finish();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "주소를 입력하세요!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void selectCityAlgorithm(String r_ad){
        myLocation = new City();
        String myAddress = "";

        String[] splitData = r_ad.split(" ");
        if(!splitData[1].equals(null)){
            myAddress = splitData[2];
        }

       // Log.d("ADDRESS", myAddress);


        for(int a=0; a<RcityList.size(); a++){
            String temp1 = myAddress.trim();
            String temp2 = RcityList.get(a).CityName.trim();
            /*현재 내 위치 입력 완료!*/
            if(temp1.equals(temp2)){
                myLocation = RcityList.get(a);
                Log.d("Weight","Find");
                break;
            }else{
                Log.d("Weight","No");
                myLocation = RcityList.get(0);
            }
        }
        ArrayList<CityNode> cityNodes = new ArrayList<CityNode>();
        for(int b=0; b<RcityList.size(); b++){
            CityNode tempNode = new CityNode(myLocation, RcityList.get(b));
            cityNodes.add(tempNode);
            Log.d("Distance",b+":"+cityNodes.get(b).distance);
        }
        /*max거리 찾고, 유동인구 max찾고, weight계산하기.*/
        computeMaxDistance(cityNodes);
        computeMaxPopulation(cityNodes);
        computeWeight(cityNodes);
        /*이제 선택된 구 정보(String) 데리고 데이트 장소 정하러 고고!*/

    }

    public void computeMaxDistance(ArrayList<CityNode> r_node){
        float max = r_node.get(0).distance;
        for(int i=1; i<r_node.size(); i++){
            if(r_node.get(i).distance>max){
                max =r_node.get(i).distance;
            }
        }
        for(int i=0; i<r_node.size(); i++){
           r_node.get(i).maxDistance = max;
        }
        Log.d("MAX_DISTANCE", max+"");

    }

    public void computeMaxPopulation(ArrayList<CityNode> r_node){
        Double max = r_node.get(0).population;

        for(int i=1; i<r_node.size(); i++){
            if(r_node.get(i).population>max){
                max =r_node.get(i).population;
            }
        }
        for(int i=0; i<r_node.size(); i++){
            r_node.get(i).maxPopulation = max;
            Log.d("MAX_POPULATION", r_node.get(i).population+":"+max+"");
        }

    }

    public void computeWeight(ArrayList<CityNode> r_node){

        double weightD = 0.3;
        double weightP = 0.7;
        double minWeight = 0.0;

        for(int i=0; i<r_node.size(); i++){
                r_node.get(i).weight = ((r_node.get(i).distance/r_node.get(i).maxDistance)*0.3)+((r_node.get(i).population/r_node.get(i).maxPopulation)*0.7);
                Log.d("Weight",r_node.get(i).computCity.CityName+"::"+r_node.get(i).weight+"");
        }

        minWeight = r_node.get(0).weight;

        for(int i=0; i<r_node.size(); i++){
           if(minWeight>r_node.get(i).weight){
               minWeight =r_node.get(i).weight;
           }
        }

        for(int i=0; i<r_node.size(); i++){
            if(minWeight==r_node.get(i).weight){
                select = r_node.get(i).computCity.CityName;
                select_finedust=r_node.get(i).computCity.fineDust_Code;
            }
        }
    }

}
