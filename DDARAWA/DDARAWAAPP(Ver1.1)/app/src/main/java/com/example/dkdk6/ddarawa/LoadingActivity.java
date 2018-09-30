package com.example.dkdk6.ddarawa;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadingActivity extends AppCompatActivity implements Serializable {

    private ArrayList<City> cityList = new ArrayList<City>();
    Population population;
    TextView t;
    String dateForm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        /*매달 1일이라면 -> 유동인구 정보 업데이트!*/
        t = (TextView) findViewById(R.id.title);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        String getTime = sdf.format(date);
        String[] splitData = getTime.split(":");
        dateForm = splitData[0] + splitData[1] + splitData[2];
        if (splitData[2].equals("01")) {
            Toast.makeText(getApplicationContext(), "정보 업데이트 중입니다. 잠시만 기다려주세요!", Toast.LENGTH_LONG).show();
            String readData = readTxt();
            makeCity(readData);
            fillCityPopulation();
        } else {
            String readData = readTxt();
            makeCity(readData);
            fillCityPopulation();
        }
    }

    private String readTxt() {
        String data = null;
        //InputStream inputStream = getResources().openRawResource(R.raw.city_data);
        InputStream inputStream = getResources().openRawResource(R.raw.city_data_temp);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            data = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void makeCity(String data) {
        String[] lines = data.split("\n");
        for (int k = 0; k < lines.length; k++) {
            City tempCity = new City();
            String[] infoStream = lines[k].split(":");
            for (int j = 0; j < infoStream.length; j++) {
                infoStream[j].trim();
            }
            tempCity.CityName = infoStream[0];
            Double tempX = Double.parseDouble(infoStream[1]);
            tempCity.x_axis = tempX;
            Double tempY = Double.parseDouble(infoStream[2]);
            tempCity.y_axis = tempY;
            tempCity.fineDust_Code = infoStream[3];
            tempCity.population_Code = infoStream[4];
            //tempCity.population = Double.parseDouble(infoStream[5]);
            cityList.add(tempCity);
        }

    }

    private void fillCityPopulation() {
        /*Http통신해서 내용 받아온 뒤 채워줘야함.*/
        Retrofit client = new Retrofit.Builder().baseUrl("http://openapi.seoul.go.kr:8088/5479654f78646b643131366e7a716a43/json/SPOP_LOCAL_RESD_DONG/1/500/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        PopulationService ps = client.create(PopulationService.class);
        Call<Population> call = ps.get_population();
        call.enqueue(new Callback<Population>() {
            @Override
            public void onResponse(Call<Population> call, Response<Population> response) {
                population = response.body();
                for (int k = 0; k < cityList.size(); k++) {
                    for (int j = 0; j < population.sPOPLOCALRESDDONG.row.size(); j++) {
                        String temp = population.sPOPLOCALRESDDONG.row.get(j).aDSTRDCODESE.toString().trim();
                        String temp2 = cityList.get(k).population_Code.trim();
                        if (temp.equals(temp2)) {
                            Double maleTemp = 0.0;
                            Double feMaleTemp = 0.0;
                            maleTemp = maleTemp + Double.parseDouble(population.sPOPLOCALRESDDONG.row.get(j).mALEF20T24LVPOPCO.toString())
                                    + Double.parseDouble(population.sPOPLOCALRESDDONG.row.get(j).mALEF25T29LVPOPCO.toString())
                                    + Double.parseDouble(population.sPOPLOCALRESDDONG.row.get(j).mALEF30T34LVPOPCO.toString())
                                    + Double.parseDouble(population.sPOPLOCALRESDDONG.row.get(j).fEMALEF35T39LVPOPCO.toString());

                            feMaleTemp = feMaleTemp + Double.parseDouble(population.sPOPLOCALRESDDONG.row.get(j).fEMALEF20T24LVPOPCO.toString())
                                    + Double.parseDouble(population.sPOPLOCALRESDDONG.row.get(j).fEMALEF25T29LVPOPCO.toString())
                                    + Double.parseDouble(population.sPOPLOCALRESDDONG.row.get(j).fEMALEF30T34LVPOPCO.toString())
                                    + Double.parseDouble(population.sPOPLOCALRESDDONG.row.get(j).fEMALEF35T39LVPOPCO.toString());
                            Double totalPeople = Math.floor(maleTemp) + Math.floor(feMaleTemp);
                            cityList.get(k).population = totalPeople;
                            //Log.e("TRIM","Success"+cityList.get(k).CityName);
                        }
                    }
                }

                for (int d=0; d<cityList.size(); d++){
                    Log.e("FINAL", cityList.get(d).population+"<"+d);
                }

                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                intent.putParcelableArrayListExtra("key",cityList);
                //intent.putExtra("key",);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<Population> call, Throwable t) {
                Log.d("Networking", "fail");
            }
        });
    }
}
