package com.example.dkdk6.ddarawa;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by dkdk6 on 2018-10-07.
 */
@SuppressWarnings("serial")

public class ResultNode implements Serializable {
    boolean startNode = false;
    boolean endNode = false;
    int category = -1;
    Place myplace, nextplace, prevPlace;
    double weight = 0.0;

    public ResultNode(boolean start, boolean end, int category, Place p, Place m, Place n){
        this.startNode = start;
        this.endNode = end;
        this.category = category;
        this.prevPlace = p;
        this.myplace=m;
        this.nextplace =n;
    }

    public boolean isStartNode() {
        return startNode;
    }

    public void setStartNode(boolean startNode) {
        this.startNode = startNode;
    }

    public boolean isEndNode() {
        return endNode;
    }

    public void setEndNode(boolean endNode) {
        this.endNode = endNode;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Place getMyplace() {
        return myplace;
    }

    public void setMyplace(Place myplace) {
        this.myplace = myplace;
    }

    public Place getNextplace() {
        return nextplace;
    }

    public void setNextplace(Place nextplace) {
        this.nextplace = nextplace;
    }

    public Place getPrevPlace() {
        return prevPlace;
    }

    public void setPrevPlace(Place prevPlace) {
        this.prevPlace = prevPlace;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
