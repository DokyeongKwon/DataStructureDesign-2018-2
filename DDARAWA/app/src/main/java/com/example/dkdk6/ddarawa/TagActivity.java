package com.example.dkdk6.ddarawa;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dkdk6.ddarawa.Interface.fineDustService;
import com.example.dkdk6.ddarawa.Interface.seoulFineDustService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dkdk6 on 2018-09-23.
 */

public class TagActivity extends AppCompatActivity {
    Double myStartX, myStartY;
    String data;
    String dateCity="null";
    String fineDust_Code = "null";
    TextView dateTitle;
    FineDust fineDust;
    SeoulFineDust SeoulDust;
    CheckBox one,two,three;
    Button submit, date_start, date_end;
    int[] category ={0,0,0};
    int check_category=0;
    int date_type=0;
    Spinner type_s;
    String fine_result = "Blank", seoul_fine_result="Blank";
    int startHour, endHour, startMinute, endMinute, myHour, myMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        /*Date 받아오기*/
        data = getIntent().getStringExtra("city");
        Log.d("선택 행정구역 : ", data);

        String[] splitData = data.split(":");
        if(splitData.length>1){
            String temp1 = splitData[0].trim();
            dateCity = temp1;
            String temp2 = splitData[1].trim();
            fineDust_Code = temp2;
            myStartX = Double.parseDouble(splitData[2]);
            myStartY = Double.parseDouble(splitData[3]);
        }
        /*findViewById설정*/
        submit = (Button)findViewById(R.id.submit);
        one = (CheckBox)findViewById(R.id.check_1); //맛집
        two = (CheckBox)findViewById(R.id.check_2); //놀거리
        three = (CheckBox)findViewById(R.id.check_3); //카페
        date_start = (Button)findViewById(R.id.date_start_time);
        date_end = (Button)findViewById(R.id.date_end_time);
        type_s = (Spinner)findViewById(R.id.spinner1);

        /*Title 설정*/
        dateTitle = (TextView)findViewById(R.id.date_location);
        dateTitle.setText(dateCity);

        Log.d("Tag", dateCity+":"+fineDust_Code);

        /*Initialize*/
        for(int a = 0; a<3; a++){
            category[a] = 0;
        }


        /*데이트 시간 정보 받아오기*/
        date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);

            }
        });
        date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(2);

            }
        });

        /*데이트 타입 받아오기
        * 0 -> 차분한 데이트
        * 1 -> 신나는 데이트
        * */

        type_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                date_type=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        /*미세먼지 정보 받아오기*/


        fine_result = networking();
        seoul_fine_result = seoulNetworking();



        /*제출 버튼 클릭*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int a = 0; a<3; a++){
                    category[a] = 0;
                }

                /*데이트 카테고리 받아오기*/
                if(one.isChecked()==true){
                    category[0]=1;
                }else{
                    category[0]=0;
                }
                if(two.isChecked()==true){
                    category[1]=1;
                }else{
                    category[1]=0;
                }
                if(three.isChecked()==true){
                    category[2]=1;
                }else{
                    category[2]=0;
                }

                /*시간 차이 계산
                * 앞의 숫자보다 뒤의 숫자가 작으면?
                * 아니면?
                * */
                int diff_time = 0;
                if(startHour>endHour){
                    diff_time = 24-Math.abs((startHour-endHour));
                }else{
                   diff_time = endHour-startHour;
                }

                int diff_min = 0;
                if(startMinute>endMinute){
                    diff_min = 60-Math.abs(startMinute-endMinute);
                    diff_time = diff_time-1;
                }else{
                    diff_min = endMinute-startMinute;
                }


                int difference_result = (diff_time*60+diff_min)/60;

                Log.d("Time",diff_time+" ");

                for(int b=0; b<3; b++){
                    if(category[b]==1){
                        check_category++;
                    }
                }

                Log.d("Category:",check_category+"");

                if (check_category==0||date_type==0) {
                    Toast.makeText(getApplicationContext(), "모든 정보를 정확히 입력하세요!", Toast.LENGTH_LONG).show();
                }else if(difference_result<check_category){
                    Toast.makeText(getApplicationContext(), "데이트 시간이 부족해요!", Toast.LENGTH_LONG).show();
                }else{
                    if(fine_result.equals("점검중")){
                        /*점검중->서울 데이터 받아오기*/
                        if(seoul_fine_result.equals("좋음")){
                        /*좋음*/
                            fine_result = "2";
                        }else if(seoul_fine_result.equals("보통")){
                        /*보통*/
                            fine_result = "1";
                        }else if(seoul_fine_result.equals("나쁨")){
                        /*나쁨*/
                            fine_result = "0";
                        }else {
                            fine_result = "1";
                        }
                    }else if(fine_result.equals("좋음")){
                        /*좋음*/
                        fine_result = "2";
                    }else if(fine_result.equals("보통")){
                        /*보통*/
                        fine_result = "1";
                    }else if(fine_result.equals("나쁨")){
                          /*나쁨*/
                        fine_result = "0";
                    }else{
                        fine_result = "1";
                    }

                    /*Searching 시작*/
                    Intent intent = new Intent(TagActivity.this, SearchingActivity.class);
                    /*구 이름 & 미세먼지 & 데이트 시작 시간(07:10) & 데이트 종료 시간(07:10) & 데이트 카테고리 & 데이트 타입*/
                    check_category=0;

                    String result = dateCity+"&"+fine_result+"&"+startHour+":"+startMinute+"&"+endHour+":"+endMinute+"&"+category[0]
                            +":"+category[1]+":"+category[2]+"&"+(date_type-1)+"&"+
                            +myStartX+"&"+myStartY;
                    Log.d("Submit>",result);
                    intent.putExtra("result",result);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();

        myHour = c.get(Calendar.HOUR_OF_DAY);
        myMinute = c.get(Calendar.MINUTE);
        if(id==1){
            TimePickerDialog tpd;
            tpd = new TimePickerDialog(TagActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    Toast.makeText(getApplicationContext(),
                                            hourOfDay +"시 " + minute+"분 을 선택했습니다",
                                            Toast.LENGTH_SHORT).show();
                                    startHour = hourOfDay;
                                    startMinute = minute;
                                    date_start.setText(startHour+"시 "+startMinute+"분");
                                }
                            }, // 값설정시 호출될 리스너 등록
                            myHour,myMinute, false); // 기본값 시분 등록
            // true : 24 시간(0~23) 표시
            // false : 오전/오후 항목이 생김
            return tpd;
        }else{
            TimePickerDialog tpd =
                    new TimePickerDialog(TagActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    Toast.makeText(getApplicationContext(),
                                            hourOfDay +"시 " + minute+"분 을 선택했습니다",
                                            Toast.LENGTH_SHORT).show();
                                    endHour = hourOfDay;
                                    endMinute = minute;
                                    date_end.setText(endHour+"시 "+endMinute+"분");
                                }
                            }, // 값설정시 호출될 리스너 등록
                            myHour,myMinute, false); // 기본값 시분 등록
            // true : 24 시간(0~23) 표시
            // false : 오전/오후 항목이 생김
            return tpd;
        }
    }

    private String seoulNetworking(){
        Retrofit client = new Retrofit.Builder().baseUrl("http://openapi.seoul.go.kr:8088/5552496143646b643937696d4a4c77/json/ForecastWarningUltrafineParticleOfDustService/1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        seoulFineDustService ps = client.create(seoulFineDustService.class);
        Call<SeoulFineDust> call = ps.get_population("5");
        call.enqueue(new Callback<SeoulFineDust>() {
            @Override
            public void onResponse(Call<SeoulFineDust> call, Response<SeoulFineDust> response) {
                SeoulDust = response.body();
                Log.e("Retrofit_Seoul 초미세먼지 : ",SeoulDust.getForecastWarningUltrafineParticleOfDustService().getRow().get(0).getCAISTEP());
                seoul_fine_result = SeoulDust.getForecastWarningUltrafineParticleOfDustService().getRow().get(0).getCAISTEP();
            }

            @Override
            public void onFailure(Call<SeoulFineDust> call, Throwable t) {
                Log.e("Retrofit_Seoul","Fail");
            }
        });
        return seoul_fine_result;
    }
    private String networking(){
        Retrofit client = new Retrofit.Builder().baseUrl("http://openapi.seoul.go.kr:8088/7642674d49646b6438355969595277/json/ListAirQualityByDistrictService/1/5/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        fineDustService ps = client.create(fineDustService.class);
        Call<FineDust> call = ps.get_population(fineDust_Code.trim());
        call.enqueue(new Callback<FineDust>() {
            @Override
            public void onResponse(Call<FineDust> call, Response<FineDust> response) {
                fineDust = response.body();
                Log.e("Retrofit_", data+" 초미세먼지 : "+fineDust.getListAirQualityByDistrictService().getRow().get(0).getGRADE().toString());
                fine_result = fineDust.getListAirQualityByDistrictService().getRow().get(0).getGRADE().toString();
            }

            @Override
            public void onFailure(Call<FineDust> call, Throwable t) {
                Log.e("Retrofit_City","Fail");
            }
        });
        return fine_result;
    }


}
