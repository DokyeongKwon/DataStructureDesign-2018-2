package com.example.dkdk6.ddarawa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by dkdk6 on 2018-09-23.
 */

public class TagActivity extends AppCompatActivity {
    String data;
    String dateCity="null";
    String fineDust_Code = "null";
    TextView dateTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        /*Date 받아오기*/
        data = getIntent().getStringExtra("city");
        Log.d("Tag", data);

        String[] splitData = data.split(":");
        if(splitData.length>1){
            String temp1 = splitData[0].trim();
            dateCity = temp1;
            String temp2 = splitData[1].trim();
            fineDust_Code = temp2;
        }
        /*Title 설정*/
        dateTitle = (TextView)findViewById(R.id.date_location);
        dateTitle.setText("오늘의 데이트 장소! "+dateCity);
        Log.d("Tag", dateCity+":"+fineDust_Code);
    }
}
