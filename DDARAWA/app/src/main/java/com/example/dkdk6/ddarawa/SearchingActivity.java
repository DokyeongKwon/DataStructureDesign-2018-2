package com.example.dkdk6.ddarawa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchingActivity extends AppCompatActivity {
    Double startX, startY;
    String cityName;
    int fineDustLevel;
    int dateStartHour,dateStartMinute,dateEndHour,dateEndMinute, dateType;
    int[] dateCategory={0,0,0};
    ArrayList<ResultNode> graphResult = new ArrayList<ResultNode>();
    ArrayList<Place> placesNodeList= new ArrayList<Place>();
    ArrayList<CategoryPriority> category_list;
    int lunchStart = 12;
    int lunchEnd = 14;
    int dinnerStart = 18;
    int dinnerEnd = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        /*데이트 장소 정보 읽어오기*/
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        category_list = new ArrayList<CategoryPriority>();
        String[] splitData = result.split("&");
        cityName = splitData[0];
        fineDustLevel = Integer.parseInt(splitData[1]);
        String[] timeData1 = splitData[2].split(":");
        dateStartHour = Integer.parseInt(timeData1[0]);
        dateStartMinute = Integer.parseInt(timeData1[1]);
        String[] timeData2 = splitData[3].split(":");
        dateEndHour = Integer.parseInt(timeData2[0]);
        dateEndMinute = Integer.parseInt(timeData2[1]);
        String[] category = splitData[4].split(":");
        for (int a=0; a<3; a++){
            dateCategory[a]=Integer.parseInt(category[a]);
            if(dateCategory[a]!=0){
                Double weight = 10000.0;
                int priority= 0;
                CategoryPriority temp = new CategoryPriority(a,weight,priority); //initial
                category_list.add(temp);
            }
        }
        dateType = Integer.parseInt(splitData[5]);
        startX = Double.parseDouble(splitData[6]);
        startY = Double.parseDouble(splitData[7]);
        /*Node 저장 먼저!*/
        String readData = readTxt();
        makeLocation(readData);

        for (int k=0; k<placesNodeList.size(); k++){
            Log.d("Place>", placesNodeList.get(k).title+">"+placesNodeList.get(k).adress
            +placesNodeList.get(k).x+placesNodeList.get(k).y+placesNodeList.get(k).startHour+":"+placesNodeList.get(k).startMinute
            +placesNodeList.get(k).endHour+":"+placesNodeList.get(k).breakStartHour+placesNodeList.get(k).breakEndHour+placesNodeList.get(k).restDay[0]
            +placesNodeList.get(k).tel+placesNodeList.get(k).review);
        }
        resultGraph();
        /*Intent 넘겨주기*/
        Intent intent2 = new Intent(SearchingActivity.this, ResultActivity.class);
        if(graphResult==null)
        {
            Log.e("BBaa","BBaa");
        }

        intent2.putExtra("keys",graphResult);
        startActivity(intent2);
        finish();
    }

    public void resultGraph() {
        ResultNode start = new ResultNode(true, false, -1, null,null, null);
        ResultNode end = new ResultNode(false, true, -1,  null,null, null);
        /*start node 선택*/
        graphResult.add(start);
        selectCategory();
        selectPlace();
        graphResult.add(end);
        printGraph();

    }
    public void selectCategory(){
        Log.d("Category",category_list.size()+"");
       if(dateCategory[0]==1){
           Log.d("Category","Case1");
           if(dateCategory[1]==1&&dateCategory[2]==1){
               if ((lunchStart <= dateStartHour && dateStartHour <= lunchEnd) ||(dinnerStart <= dateStartHour && dateStartHour <= dinnerEnd)) {
                 category_list.get(0).priority = 3;
                if(dateType == 0){
                   /*차분한 데이트*/
                    category_list.get(1).priority = 2;
                       category_list.get(2).priority = 1;
                   }else if(dateType == 1){
                       category_list.get(1).priority = 1;
                       category_list.get(2).priority = 2;
                   }
               }else{
                   /*식사시간이 겹치지 않는 경우, 3개 모두 선택 된 상황*/
                   if(dateType == 0){
                   /*차분한 데이트*/
                       category_list.get(1).priority = 3;
                       category_list.get(0).priority = 2;
                       category_list.get(2).priority = 1;
                   }else if(dateType==1){
                       category_list.get(1).priority = 1;
                       category_list.get(0).priority = 2;
                       category_list.get(2).priority = 3;
                   }
               }
           }else if(category_list.size()>1){
               /*밥 선택 후 놀거리, 카페 중 하나만 선택 한 상황*/
               if (lunchStart <= dateStartHour && dateStartHour <= lunchEnd) {
                   category_list.get(0).priority = 2;
                   category_list.get(1).priority = 1;
               }else if(dinnerStart <= dateStartHour && dateStartHour <= dinnerEnd) {
                 /*저녁 시간이 겹침*/
                   category_list.get(0).priority = 2;
                   category_list.get(1).priority = 1;
               }else{
                   /*식사시간이 겹치지 않는 경우, 3개 모두 선택 된 상황*/
                   category_list.get(0).priority = 1;
                   category_list.get(1).priority = 2;
               }
           }else{ /*밥만 선택 한 경우*/
               category_list.get(0).priority = 1;
           }
       }else{
           if(dateCategory[1]==1&&dateCategory[2]==1){
               if(dateType == 0){
                   /*차분한 데이트*/
                   category_list.get(0).priority = 2;
                   category_list.get(1).priority = 1;
               }else{
                   category_list.get(0).priority = 1;
                   category_list.get(1).priority = 2;
               }
           }else{
               /*
                * 놀거리 or 카페 하나만 선택 된 상황.
                */
               category_list.get(0).priority = 1;
           }

       }
        Log.d("Category Priority"," [ Total ] ");
        /*우선순위 프린트*/
        for(int f=0; f<category_list.size(); f++){
            if(category_list.get(f).category==0){
                Log.d("Category Priority","맛집 : "+ category_list.get(f).priority);
            }else if(category_list.get(f).category==1){
                Log.d("Category Priority","카페 : "+ category_list.get(f).priority);
            }else if(category_list.get(f).category==2){
                Log.d("Category Priority","놀거리 : "+ category_list.get(f).priority);
            }
        }
        for(int a=0; a<placesNodeList.size(); a++) {
            Log.d("SelectCategory : TAG", placesNodeList.get(a).tag);
        }
    }
    public void selectPlace(){
        if(category_list.size()==3){
            Log.d("Place Category", "THREE");
           category_three();
        }else if(category_list.size()==2){
            Log.d("Place Category", "TWO");
          category_two();
        }else if(category_list.size()==1){/*1개 선택 된 경우*/
            Log.d("Place Category", "ONE");
            for(int a=0; a<placesNodeList.size(); a++) {
                Log.d("SelectPlace : TAG", placesNodeList.get(a).tag);
            }
            category_one();
        }
    }
    public void category_one(){
            /*Date 시작시간이 영업시간보다 커야 한다.(작으면 X)
            * Date 시작 시간이 Break Time 시작 보다 작거나 Breake Time 끝보다 커야한다.
            * Date 끝 시간이 영업시간보다 작아야한다.
            * */
        if(category_list.get(0).category==0){
            Log.d("SelectPlace","category:0");
            for(int a=0; a<placesNodeList.size(); a++) {
                if(placesNodeList.get(a).tag.contains("100")==true){
                    placesNodeList.get(a).connected = true;
                    if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                        placesNodeList.get(a).connected = false;
                    }
                    if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                        placesNodeList.get(a).connected = false;
                    }
                }

            }
            /*가중치 계산하기*/

            for(int a=0; a<placesNodeList.size(); a++){
                if(placesNodeList.get(a).connected==true){
                    placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel);
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("100");

            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                    graphResult.add(node);
                    break;
                }
            }

        }else if(category_list.get(0).category==1){
            Log.d("SelectPlace","category:1");
            for(int a=0; a<placesNodeList.size(); a++) {
                if(placesNodeList.get(a).tag.contains("300")==true){
                    placesNodeList.get(a).connected = true;
                    if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                        placesNodeList.get(a).connected = false;
                    }
                    if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                        placesNodeList.get(a).connected = false;
                    }
                }
            }
            /*가중치 계산하기*/
            for(int a=0; a<placesNodeList.size(); a++) {
                if (placesNodeList.get(a).connected == true) {
                    if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                    } else if(dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                        Log.d("신나는", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                    }
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("300");
            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                    graphResult.add(node);
                    break;
                }
            }

        }else if(category_list.get(0).category==2){
            Log.d("SelectPlace","category:2");
            for(int a=0; a<placesNodeList.size(); a++) {
                if(placesNodeList.get(a).tag.contains("200")==true){
                    placesNodeList.get(a).connected = true;
                    if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                        placesNodeList.get(a).connected = false;
                    }
                    if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                        placesNodeList.get(a).connected = false;
                    }
                }

            }
            /*가중치 계산하기*/
            for(int a=0; a<placesNodeList.size(); a++){
                if (placesNodeList.get(a).connected == true) {
                    if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                        Log.d("차분한", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                    } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005")|| placesNodeList.get(a).tag.equals("2006"))) {
                        Log.d("신나는", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                    }
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("200");
            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                    graphResult.add(node);
                    break;
                }
            }

        }

    }
    public float findMinimum(String contain){
        float minimum = 10000.0f;
        for (int c = 0; c < placesNodeList.size(); c++) {
            if (placesNodeList.get(c).tag.contains(contain)&&(minimum >= placesNodeList.get(c).weight)&&(placesNodeList.get(c).connected == true)) {
                minimum = placesNodeList.get(c).weight;
            }
        }
        return minimum;
    }
    public void category_two() {
        /*우선순위를 정한다.*/
        Place firstNode=null;
        int index1 = 0, index2 = 0;

        for(int i=0; i<category_list.size(); i++){
            if(category_list.get(i).priority==2){
                index1 = i;
            }else if(category_list.get(i).priority==1){
                index2 = i;
            }
        }

        /*category에 맞게 첫 노드 찾기*/
        if(category_list.get(index1).category==0){
            Log.d("SelectPlace","category:0");
            for(int a=0; a<placesNodeList.size(); a++) {
                if(placesNodeList.get(a).tag.contains("100")==true){
                    placesNodeList.get(a).connected = true;
                    if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                        placesNodeList.get(a).connected = false;
                    }
                    if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                        placesNodeList.get(a).connected = false;
                    }
                }
            }
            /*가중치 계산하기*/

            for(int a=0; a<placesNodeList.size(); a++){
                if(placesNodeList.get(a).connected==true){
                    placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel);
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("100");

            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                    firstNode = placesNodeList.get(c);
                    graphResult.add(node);
                    break;
                }
            }
        }else if(category_list.get(index1).category==1){
            Log.d("SelectPlace","category:1");
            for(int a=0; a<placesNodeList.size(); a++) {
                if(placesNodeList.get(a).tag.contains("300")==true){
                    placesNodeList.get(a).connected = true;
                    if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                        placesNodeList.get(a).connected = false;
                    }
                    if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                        placesNodeList.get(a).connected = false;
                    }
                }
            }
            /*가중치 계산하기*/
            for(int a=0; a<placesNodeList.size(); a++) {
                if (placesNodeList.get(a).connected == true) {
                    if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                        Log.d("차분한", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                    } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                        Log.d("신나는", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                    }
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("300");
            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                    firstNode = placesNodeList.get(c);

                    graphResult.add(node);
                    break;
                }
            }

        }else if(category_list.get(index1).category==2) {
            Log.d("SelectPlace", "category:2");
            for (int a = 0; a < placesNodeList.size(); a++) {
                if (placesNodeList.get(a).tag.contains("200") == true) {
                    placesNodeList.get(a).connected = true;
                    if (dateStartHour < placesNodeList.get(a).startHour || dateEndHour > placesNodeList.get(a).endHour) {
                        placesNodeList.get(a).connected = false;
                    }
                    if (placesNodeList.get(a).breakStartHour < dateStartHour && placesNodeList.get(a).breakEndHour > dateStartHour) {
                        placesNodeList.get(a).connected = false;
                    }
                }

            }
            /*가중치 계산하기*/
            for (int a = 0; a < placesNodeList.size(); a++) {
                if (placesNodeList.get(a).connected == true) {
                    if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                        Log.d("차분한", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                    } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                        Log.d("신나는", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                    }
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("200");

            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                    firstNode = placesNodeList.get(c);
                    graphResult.add(node);
                    break;
                }
            }
        }

        if(firstNode!=null){
            /*두번째 노드 선택. -> 영업끝나는 시간만 고려.*/
            Log.d("SelectPlace","CASE2>>");
            if(category_list.get(index2).category==0){
                Log.d("SelectPlace","category:0");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("100")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
                Log.d("SelectPlace","category:0:Weight");
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++){
                    if(placesNodeList.get(a).connected==true){
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel);
                    }
                }
            /*최소 간선 찾기*/
                Log.d("SelectPlace","category:0:Min");
                float min = findMinimum("100");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index2).category==1){
                Log.d("SelectPlace","category:1");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("300")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("300");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index2).category==2) {
                Log.d("SelectPlace", "category:2");
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).tag.contains("200") == true) {
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;
                        }
                    }
                }
            /*최소 간선 찾기*/
                float min = findMinimum("200");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }
        }
        else if(firstNode==null){
            /*첫 노드가 선택되지 못한 경우 -> 이게 첫 노드*/
            if(category_list.get(index2).category==0){
                Log.d("SelectPlace","category:0");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("100")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                        if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/

                for(int a=0; a<placesNodeList.size(); a++){
                    if(placesNodeList.get(a).connected==true){
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel);
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("100");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                        firstNode = placesNodeList.get(c);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index2).category==1){
                Log.d("SelectPlace","category:1");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("300")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                        if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("300");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                        firstNode = placesNodeList.get(c);

                        graphResult.add(node);
                        break;
                    }
                }

            }else if(category_list.get(index2).category==2) {
                Log.d("SelectPlace", "category:2");
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).tag.contains("200") == true) {
                        placesNodeList.get(a).connected = true;
                        if (dateStartHour < placesNodeList.get(a).startHour || dateEndHour > placesNodeList.get(a).endHour) {
                            placesNodeList.get(a).connected = false;
                        }
                        if (placesNodeList.get(a).breakStartHour < dateStartHour && placesNodeList.get(a).breakEndHour > dateStartHour) {
                            placesNodeList.get(a).connected = false;
                        }
                    }

                }
            /*가중치 계산하기*/
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("200");

                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                        firstNode = placesNodeList.get(c);
                        graphResult.add(node);
                        break;
                    }
                }
            }
        }
        /*끝*/
    }
    public void category_three(){
        Place firstNode=null, secondNode = null;

        int index1 = 0, index2 = 0, index3 = 0;

        for(int i=0; i<category_list.size(); i++){
            if(category_list.get(i).priority==3){
                index1 = i;
            }else if(category_list.get(i).priority==2){
                index2 = i;
            }else if(category_list.get(i).priority==1){
                index3 = i;
            }
        }

        /*첫노드 선택*/
        if(category_list.get(index1).category==0){
            Log.d("SelectPlace","category:0");
            for(int a=0; a<placesNodeList.size(); a++) {
                if(placesNodeList.get(a).tag.contains("100")==true){
                    placesNodeList.get(a).connected = true;
                    if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                        placesNodeList.get(a).connected = false;
                    }
                    if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                        placesNodeList.get(a).connected = false;
                    }
                }
            }
            /*가중치 계산하기*/

            for(int a=0; a<placesNodeList.size(); a++){
                if(placesNodeList.get(a).connected==true){
                    placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel);
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("100");
            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                    firstNode = placesNodeList.get(c);
                    graphResult.add(node);
                    break;
                }
            }
        }else if(category_list.get(index1).category==1){
            Log.d("SelectPlace","category:1");
            for(int a=0; a<placesNodeList.size(); a++) {
                if(placesNodeList.get(a).tag.contains("300")==true){
                    placesNodeList.get(a).connected = true;
                    if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                        placesNodeList.get(a).connected = false;
                    }
                    if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                        placesNodeList.get(a).connected = false;
                    }
                }
            }
            /*가중치 계산하기*/
            for(int a=0; a<placesNodeList.size(); a++) {
                if (placesNodeList.get(a).connected == true) {
                    if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                        Log.d("차분한", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                    } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                        Log.d("신나는", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                    }
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("300");
            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                    firstNode = placesNodeList.get(c);

                    graphResult.add(node);
                    break;
                }
            }

        }else if(category_list.get(index1).category==2) {
            Log.d("SelectPlace", "category:2");
            for (int a = 0; a < placesNodeList.size(); a++) {
                if (placesNodeList.get(a).tag.contains("200") == true) {
                    placesNodeList.get(a).connected = true;
                    if (dateStartHour < placesNodeList.get(a).startHour || dateEndHour > placesNodeList.get(a).endHour) {
                        placesNodeList.get(a).connected = false;
                    }
                    if (placesNodeList.get(a).breakStartHour < dateStartHour && placesNodeList.get(a).breakEndHour > dateStartHour) {
                        placesNodeList.get(a).connected = false;
                    }
                }

            }
            /*가중치 계산하기*/
            for (int a = 0; a < placesNodeList.size(); a++) {
                if (placesNodeList.get(a).connected == true) {
                    if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                        Log.d("차분한", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                    } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                        Log.d("신나는", "데이트");
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                    }
                }
            }

            /*최소 간선 찾기*/
            float min = findMinimum("200");

            for (int c = 0; c < placesNodeList.size(); c++) {
                if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                    ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                    Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                    firstNode = placesNodeList.get(c);
                    graphResult.add(node);
                    break;
                }
            }
        }
        /*두번째 노드 선택->영업종료전으로*/
        if(firstNode!=null){
            /*두번째 노드 선택. -> 영업끝나는 시간만 고려.*/
            Log.d("SelectPlace","CASE2>>");
            if(category_list.get(index2).category==0){
                Log.d("SelectPlace","category:0");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("100")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
                Log.d("SelectPlace","category:0:Weight");
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++){
                    if(placesNodeList.get(a).connected==true){
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel);
                    }
                }
            /*최소 간선 찾기*/
                Log.d("SelectPlace","category:0:Min");
                float min = findMinimum("100");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                        secondNode =  placesNodeList.get(c);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index2).category==1){
                Log.d("SelectPlace","category:1");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("300")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("300");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                        secondNode =  placesNodeList.get(c);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index2).category==2) {
                Log.d("SelectPlace", "category:2");
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).tag.contains("200") == true) {
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;
                        }
                    }
                }
            /*최소 간선 찾기*/
                float min = findMinimum("200");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                        secondNode =  placesNodeList.get(c);
                        graphResult.add(node);
                        break;
                    }
                }
            }
        }
        else if(firstNode==null){
            /*첫 노드가 선택되지 못한 경우 -> 이게 첫 노드*/
            if(category_list.get(index2).category==0){
                Log.d("SelectPlace","category:0");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("100")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                        if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/

                for(int a=0; a<placesNodeList.size(); a++){
                    if(placesNodeList.get(a).connected==true){
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel);
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("100");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                        firstNode = placesNodeList.get(c);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index2).category==1){
                Log.d("SelectPlace","category:1");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("300")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                        if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("300");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                        firstNode = placesNodeList.get(c);

                        graphResult.add(node);
                        break;
                    }
                }

            }else if(category_list.get(index2).category==2) {
                Log.d("SelectPlace", "category:2");
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).tag.contains("200") == true) {
                        placesNodeList.get(a).connected = true;
                        if (dateStartHour < placesNodeList.get(a).startHour || dateEndHour > placesNodeList.get(a).endHour) {
                            placesNodeList.get(a).connected = false;
                        }
                        if (placesNodeList.get(a).breakStartHour < dateStartHour && placesNodeList.get(a).breakEndHour > dateStartHour) {
                            placesNodeList.get(a).connected = false;
                        }
                    }

                }
            /*가중치 계산하기*/
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("200");

                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                        firstNode = placesNodeList.get(c);
                        graphResult.add(node);
                        break;
                    }
                }
            }
        }
        /*마지막 노드 선택->영업종료전으로*/
        if(secondNode!=null){
            /*마지막 노드 선택. -> 영업끝나는 시간만 고려.*/
            if(category_list.get(index3).category==0){
                Log.d("SelectPlace","category:0");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("100")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
                Log.d("SelectPlace","category:0:Weight");
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++){
                    if(placesNodeList.get(a).connected==true){
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(secondNode)) / (fineDustLevel);
                    }
                }
            /*최소 간선 찾기*/
                Log.d("SelectPlace","category:0:Min");
                float min = findMinimum("100");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index3).category==1){
                Log.d("SelectPlace","category:1");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("300")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(secondNode)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(secondNode)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("300");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index3).category==2) {
                Log.d("SelectPlace", "category:2");
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).tag.contains("200") == true) {
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(secondNode)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(secondNode)) / (fineDustLevel) / 4;
                        }
                    }
                }
            /*최소 간선 찾기*/
                float min = findMinimum("200");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }
        }else if(firstNode==null && secondNode==null){
            /*이게 첫 노드*/
            if(category_list.get(index3).category==0){
                Log.d("SelectPlace","category:0");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("100")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                        if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/

                for(int a=0; a<placesNodeList.size(); a++){
                    if(placesNodeList.get(a).connected==true){
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel);
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("100");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                        firstNode = placesNodeList.get(c);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index3).category==1){
                Log.d("SelectPlace","category:1");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("300")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateStartHour<placesNodeList.get(a).startHour || dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                        if(placesNodeList.get(a).breakStartHour<dateStartHour && placesNodeList.get(a).breakEndHour>dateStartHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("300");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }

            }else if(category_list.get(index3).category==2) {
                Log.d("SelectPlace", "category:2");
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).tag.contains("200") == true) {
                        placesNodeList.get(a).connected = true;
                        if (dateStartHour < placesNodeList.get(a).startHour || dateEndHour > placesNodeList.get(a).endHour) {
                            placesNodeList.get(a).connected = false;
                        }
                        if (placesNodeList.get(a).breakStartHour < dateStartHour && placesNodeList.get(a).breakEndHour > dateStartHour) {
                            placesNodeList.get(a).connected = false;
                        }
                    }

                }
            /*가중치 계산하기*/
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(startX, startY)) / (fineDustLevel) / 4;
                        }
                    }
                }
            /*최소 간선 찾기*/
                float min = findMinimum("200");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }
        }else if(firstNode!=null&& secondNode==null){
            /*firstNode로부터 거리 계산*/
            if(category_list.get(index3).category==0){
                Log.d("SelectPlace","category:0");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("100")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
                Log.d("SelectPlace","category:0:Weight");
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++){
                    if(placesNodeList.get(a).connected==true){
                        placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel);
                    }
                }
            /*최소 간선 찾기*/
                Log.d("SelectPlace","category:0:Min");
                float min = findMinimum("100");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 1: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index3).category==1){
                Log.d("SelectPlace","category:1");
                for(int a=0; a<placesNodeList.size(); a++) {
                    if(placesNodeList.get(a).tag.contains("300")==true){
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for(int a=0; a<placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("3001") || placesNodeList.get(a).tag.equals("3002") || placesNodeList.get(a).tag.equals("3003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 &&(placesNodeList.get(a).tag.equals("3004") || placesNodeList.get(a).tag.equals("3005"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;
                        }
                    }
                }

            /*최소 간선 찾기*/
                float min = findMinimum("300");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 2: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }else if(category_list.get(index3).category==2) {
                Log.d("SelectPlace", "category:2");
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).tag.contains("200") == true) {
                        placesNodeList.get(a).connected = true;
                        if(dateEndHour>placesNodeList.get(a).endHour){
                            placesNodeList.get(a).connected = false;
                        }
                    }
                }
            /*가중치 계산하기*/
                for (int a = 0; a < placesNodeList.size(); a++) {
                    if (placesNodeList.get(a).connected == true) {
                        if (dateType == 0 && (placesNodeList.get(a).tag.equals("2001") || placesNodeList.get(a).tag.equals("2002") || placesNodeList.get(a).tag.equals("2003"))) {
                            Log.d("차분한", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;

                        } else if (dateType == 1 && (placesNodeList.get(a).tag.equals("2004") || placesNodeList.get(a).tag.equals("2005") || placesNodeList.get(a).tag.equals("2006"))) {
                            Log.d("신나는", "데이트");
                            placesNodeList.get(a).weight = (placesNodeList.get(a).computeDistance(firstNode)) / (fineDustLevel) / 4;
                        }
                    }
                }
            /*최소 간선 찾기*/
                float min = findMinimum("200");
                for (int c = 0; c < placesNodeList.size(); c++) {
                    if (placesNodeList.get(c).connected == true && placesNodeList.get(c).weight == min) {
                        ResultNode node = new ResultNode(false, false, 1, null, placesNodeList.get(c), null);
                        Log.d("Case 3: Select Data", placesNodeList.get(c).title);
                        graphResult.add(node);
                        break;
                    }
                }
            }
        }
    }
    public void printGraph(){
        Log.d("Graph", "[전체 연결 경로]");
        for(int l = 0; l < placesNodeList.size(); l++){
            if(placesNodeList.get(l).tag.contains("100")&&placesNodeList.get(l).connected==true){
                Log.d("Graph>","밥집 : "+ placesNodeList.get(l).title+" Weight : "+placesNodeList.get(l).weight);
            }else if(placesNodeList.get(l).tag.contains("300")&&placesNodeList.get(l).connected==true){
                Log.d("Graph>","카페 : "+ placesNodeList.get(l).title+" Weight : "+placesNodeList.get(l).weight + "Tag : "+placesNodeList.get(l).tag);
            }else if(placesNodeList.get(l).tag.contains("200")&&placesNodeList.get(l).connected==true){
                Log.d("Graph>","놀거리 : "+ placesNodeList.get(l).title+" Weight : "+placesNodeList.get(l).weight);
            }
        }
        if(graphResult.size()<=2){
            Toast.makeText(getApplicationContext(), "데이트 장소를 찾을 수 없습니다. 정보를 다시 입력해주세요!", Toast.LENGTH_LONG).show();
        }else{
            Log.d("Graph", "[최종 연결 경로]");
            for(int k=0; k<graphResult.size(); k++){
                if(graphResult.get(k).startNode==true){
                    Log.d("Graph>", k+": STARTNODE");
                }else if(graphResult.get(k).endNode==true){
                    Log.d("Graph>", k+ ":END");
                }else{
                    Log.d("Graph>", k+ ":"+graphResult.get(k).myplace.title);
                }
            }
        }
    }
    private String readTxt() {
        InputStream inputStream = getResources().openRawResource(R.raw.a);
        String data = null;
        if(cityName.equals("강남구")){
            inputStream = getResources().openRawResource(R.raw.a);
        }else if(cityName.equals("강동구")){
            inputStream = getResources().openRawResource(R.raw.b);
        }else if(cityName.equals("강북구")){
             inputStream = getResources().openRawResource(R.raw.c);
        }else if(cityName.equals("강서구")){
             inputStream = getResources().openRawResource(R.raw.d);
        }else if(cityName.equals("관악구")){
             inputStream = getResources().openRawResource(R.raw.e);
        }else if(cityName.equals("광진구")){
             inputStream = getResources().openRawResource(R.raw.f);
        }else if(cityName.equals("구로구")){
             inputStream = getResources().openRawResource(R.raw.g);
        }else if(cityName.equals("금천구")){
             inputStream = getResources().openRawResource(R.raw.h);
        }else if(cityName.equals("노원구")){
             inputStream = getResources().openRawResource(R.raw.i);
        }else if(cityName.equals("도봉구")){
             inputStream = getResources().openRawResource(R.raw.j);
        }else if(cityName.equals("동대문구")){
             inputStream = getResources().openRawResource(R.raw.k);
        }else if(cityName.equals("동작구")){
            inputStream = getResources().openRawResource(R.raw.l);
        }else if(cityName.equals("마포구")){
             inputStream = getResources().openRawResource(R.raw.m);
        }else if(cityName.equals("서대문구")){
             inputStream = getResources().openRawResource(R.raw.n);
        }else if(cityName.equals("서초구")){
             inputStream = getResources().openRawResource(R.raw.o);
        }else if(cityName.equals("성동구")){
             inputStream = getResources().openRawResource(R.raw.p);
        }else if(cityName.equals("성북구")){
             inputStream = getResources().openRawResource(R.raw.q);
        }else if(cityName.equals("송파구")){
             inputStream = getResources().openRawResource(R.raw.r);
        }else if(cityName.equals("양천구")){
            inputStream = getResources().openRawResource(R.raw.s);
        }else if(cityName.equals("영등포구")){
            inputStream = getResources().openRawResource(R.raw.t);
        }else if(cityName.equals("용산구")){
            inputStream = getResources().openRawResource(R.raw.u);
        }else if(cityName.equals("은평구")){
            inputStream = getResources().openRawResource(R.raw.v);
        }else if(cityName.equals("종로구")){
            inputStream = getResources().openRawResource(R.raw.w);
        }else if(cityName.equals("중구")){
            inputStream = getResources().openRawResource(R.raw.x);
        }else if(cityName.equals("중랑구")){
            inputStream = getResources().openRawResource(R.raw.y);
        }

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
    private void makeLocation(String data) {
        String[] lines = data.split("\n");

        for (int k = 0; k < lines.length; k++) {
            if(k>=1){
                Place tempPlace = new Place();
                String[] infoStream = lines[k].split("&");
                tempPlace.title = infoStream[0];
                Log.d("Title",infoStream[0]);
                tempPlace.adress = infoStream[1];
                Log.d("Title",infoStream[1]);
                Double tempX = Double.parseDouble(infoStream[2]);
                tempPlace.x = tempX;
                Double tempY = Double.parseDouble(infoStream[3]);
                tempPlace.y = tempY;
                tempPlace.tag = infoStream[4];
                String[] time1 = infoStream[5].split(":");
                tempPlace.startHour = Integer.parseInt(time1[0]);
                tempPlace.startMinute = Integer.parseInt(time1[1]);
                String[] time2 = infoStream[6].split(":");
                tempPlace.endHour = Integer.parseInt(time2[0]);
                tempPlace.endMinute = Integer.parseInt(time2[1]);
                if(infoStream[7].equals("0")||infoStream[8].equals("0")){
                    tempPlace.breakStartHour = 0;
                    tempPlace.breakStartMinute = 0;
                    tempPlace.breakEndHour = 0;
                    tempPlace.breakEndMinute = 0;
                }else{
                    String[] b_t1 = infoStream[7].split(":");
                    tempPlace.breakStartHour = Integer.parseInt(b_t1[0]);
                    tempPlace.breakStartMinute = Integer.parseInt(b_t1[1]);
                    String[] b_t2 = infoStream[8].split(":");
                    tempPlace.breakEndHour = Integer.parseInt(b_t2[0]);
                    tempPlace.breakEndMinute = Integer.parseInt(b_t2[1]);
                }
                if(infoStream[9].contains(",")){
                /*휴무일이 여럿*/
                    String[] rest_temp = infoStream[9].split(",");
                    for(int c=0; c<rest_temp.length; c++){
                        int temp = Integer.parseInt(rest_temp[c]);
                        tempPlace.restDay[temp] = 1;
                    }

                }else{
                    if(!(infoStream[9].equals("0"))){
                        int restDay = Integer.parseInt(infoStream[9]);
                        restDay = restDay-1;
                        tempPlace.restDay[restDay] = 1;
                    }
                }
                tempPlace.tel=infoStream[10];
                tempPlace.review = infoStream[11];
                placesNodeList.add(tempPlace);
            }

        }
    }
}
