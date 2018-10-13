package com.example.dkdk6.ddarawa;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dkdk6 on 2018-09-29.
 */

public class City implements Parcelable{
    String CityName = "temp";
    Double x_axis, y_axis;
    String fineDust_Code;
    String population_Code;
    String subCityName;
    String fineDust;
      Double population;
    public City(){}
    protected City(Parcel in) {
        CityName = in.readString();
        x_axis = in.readDouble();
        y_axis = in.readDouble();
        fineDust_Code = in.readString();
        population_Code = in.readString();
        subCityName = in.readString();
        fineDust = in.readString();
        population = in.readDouble();
    }
    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(CityName);
        parcel.writeDouble(x_axis);
        parcel.writeDouble(y_axis);
        parcel.writeString(fineDust_Code);
        parcel.writeString(population_Code);
        parcel.writeString(subCityName);
        parcel.writeString(fineDust);
        parcel.writeDouble(population);
    }
}

