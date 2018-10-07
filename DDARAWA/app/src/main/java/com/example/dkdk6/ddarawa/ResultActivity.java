package com.example.dkdk6.ddarawa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by dkdk6 on 2018-09-23.
 */

public class ResultActivity extends AppCompatActivity {
    ArrayList<ResultNode> graphResult;
    LinearLayout one, two, three;
    TextView t_1,t_2,t_3;
    ImageView i1,i2,i3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graphResult = (ArrayList<ResultNode>)getIntent().getSerializableExtra("keys");
        Log.d("Result", graphResult.size()+"");
        setContentView(R.layout.activity_result);



        one = (LinearLayout)findViewById(R.id.firstLayout);
        two = (LinearLayout)findViewById(R.id.secondLayout);
        three = (LinearLayout)findViewById(R.id.thirdLayout);
        one.setVisibility(View.GONE);
        two.setVisibility(View.GONE);
        three.setVisibility(View.GONE);

        t_1=(TextView)findViewById(R.id.firstText);
        t_2=(TextView)findViewById(R.id.secondText);
        t_3=(TextView)findViewById(R.id.thirdText);

        i1 = (ImageView)findViewById(R.id.iv1);
        i2 = (ImageView)findViewById(R.id.iv2);
        i3 = (ImageView)findViewById(R.id.iv3);

        if(graphResult.size()==3){
            one.setVisibility(View.GONE);
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.GONE);

            t_2.setText(graphResult.get(1).myplace.title);
            i2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this, popupActivity.class);
                    intent.putExtra("data", graphResult.get(1).myplace);
                    startActivityForResult(intent, 1);
                }
            });
        }
        else if(graphResult.size()==4){
            one.setVisibility(View.VISIBLE);
            two.setVisibility(View.GONE);
            three.setVisibility(View.VISIBLE);

            t_3.setText(graphResult.get(2).myplace.title);
            t_1.setText(graphResult.get(1).myplace.title);

            i1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this, popupActivity.class);
                    intent.putExtra("data", graphResult.get(1).myplace);
                    startActivityForResult(intent, 1);
                }
            });
            i3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this, popupActivity.class);
                    intent.putExtra("data", graphResult.get(3).myplace);
                    startActivityForResult(intent, 1);
                }
            });

        }else if(graphResult.size()==5){
            one.setVisibility(View.VISIBLE);
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.VISIBLE);
            t_1.setText(graphResult.get(1).myplace.title);
            t_2.setText(graphResult.get(2).myplace.title);
            t_3.setText(graphResult.get(3).myplace.title);

            i1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this, popupActivity.class);
                    intent.putExtra("data", graphResult.get(1).myplace);
                    startActivityForResult(intent, 1);
                }
            });
            i2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this, popupActivity.class);
                    intent.putExtra("data", graphResult.get(2).myplace);
                    startActivityForResult(intent, 1);
                }
            });
            i3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this, popupActivity.class);
                    intent.putExtra("data", graphResult.get(3).myplace);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }
}
