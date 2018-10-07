package com.example.dkdk6.ddarawa;

import android.location.Location;


import java.io.Serializable;

/**
 * Created by dkdk6 on 2018-10-07.
 */
@SuppressWarnings("serial")

public class Place implements Serializable {

    String title;
    String adress;
    Double x,y,originX, originY;
    Float distance;
    String tag;
    int startHour, startMinute, endHour, endMinute;
    int breakStartHour,breakStartMinute,breakEndHour,breakEndMinute;
    int[] restDay = {0,0,0,0,0,0,0};
    String tel;
    String review;
    Float weight = 10000.0f; //initialize
    boolean connected = false;

    public Place(){}

    public Float computeDistance(Double rx, Double ry){
        originX = rx;
        originY = ry;
        /*위경도 기반 거리 계산*/
        Location locationA = new Location("Origin");
        locationA.setLatitude(originX);
        locationA.setLongitude(originY);
        Location locationB = new Location("Computing");
        locationB.setLatitude(x);
        locationB.setLongitude(y);
        distance = locationA.distanceTo(locationB);
        return distance;
    }
    public Float computeDistance(Place c){
        originX = c.x;
        originY = c.y;
        /*위경도 기반 거리 계산*/
        Location locationA = new Location("Origin");
        locationA.setLatitude(originX);
        locationA.setLongitude(originY);
        Location locationB = new Location("Computing");
        locationB.setLatitude(x);
        locationB.setLongitude(y);
        distance = locationA.distanceTo(locationB);
        return distance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getOriginX() {
        return originX;
    }

    public void setOriginX(Double originX) {
        this.originX = originX;
    }

    public Double getOriginY() {
        return originY;
    }

    public void setOriginY(Double originY) {
        this.originY = originY;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getBreakStartHour() {
        return breakStartHour;
    }

    public void setBreakStartHour(int breakStartHour) {
        this.breakStartHour = breakStartHour;
    }

    public int getBreakStartMinute() {
        return breakStartMinute;
    }

    public void setBreakStartMinute(int breakStartMinute) {
        this.breakStartMinute = breakStartMinute;
    }

    public int getBreakEndHour() {
        return breakEndHour;
    }

    public void setBreakEndHour(int breakEndHour) {
        this.breakEndHour = breakEndHour;
    }

    public int getBreakEndMinute() {
        return breakEndMinute;
    }

    public void setBreakEndMinute(int breakEndMinute) {
        this.breakEndMinute = breakEndMinute;
    }

    public int[] getRestDay() {
        return restDay;
    }

    public void setRestDay(int[] restDay) {
        this.restDay = restDay;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
