package com.example.dkdk6.ddarawa;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Population {

    @SerializedName("SPOP_LOCAL_RESD_DONG")
    @Expose
    public SPOPLOCALRESDDONG sPOPLOCALRESDDONG;

    public class RESULT {

        @SerializedName("CODE")
        @Expose
        public String cODE;
        @SerializedName("MESSAGE")
        @Expose
        public String mESSAGE;

    }

    public class Row {

        @SerializedName("STDR_DE_ID")
        @Expose
        public String sTDRDEID;
        @SerializedName("TMZON_PD_SE")
        @Expose
        public String tMZONPDSE;
        @SerializedName("ADSTRD_CODE_SE")
        @Expose
        public String aDSTRDCODESE;
        @SerializedName("TOT_LVPOP_CO")
        @Expose
        public String tOTLVPOPCO;
        @SerializedName("MALE_F0T9_LVPOP_CO")
        @Expose
        public String mALEF0T9LVPOPCO;
        @SerializedName("MALE_F10T14_LVPOP_CO")
        @Expose
        public String mALEF10T14LVPOPCO;
        @SerializedName("MALE_F15T19_LVPOP_CO")
        @Expose
        public String mALEF15T19LVPOPCO;
        @SerializedName("MALE_F20T24_LVPOP_CO")
        @Expose
        public String mALEF20T24LVPOPCO;
        @SerializedName("MALE_F25T29_LVPOP_CO")
        @Expose
        public String mALEF25T29LVPOPCO;
        @SerializedName("MALE_F30T34_LVPOP_CO")
        @Expose
        public String mALEF30T34LVPOPCO;
        @SerializedName("MALE_F35T39_LVPOP_CO")
        @Expose
        public String mALEF35T39LVPOPCO;
        @SerializedName("MALE_F40T44_LVPOP_CO")
        @Expose
        public String mALEF40T44LVPOPCO;
        @SerializedName("MALE_F45T49_LVPOP_CO")
        @Expose
        public String mALEF45T49LVPOPCO;
        @SerializedName("MALE_F50T54_LVPOP_CO")
        @Expose
        public String mALEF50T54LVPOPCO;
        @SerializedName("MALE_F55T59_LVPOP_CO")
        @Expose
        public String mALEF55T59LVPOPCO;
        @SerializedName("MALE_F60T64_LVPOP_CO")
        @Expose
        public String mALEF60T64LVPOPCO;
        @SerializedName("MALE_F65T69_LVPOP_CO")
        @Expose
        public String mALEF65T69LVPOPCO;
        @SerializedName("MALE_F70T74_LVPOP_CO")
        @Expose
        public String mALEF70T74LVPOPCO;
        @SerializedName("FEMALE_F0T9_LVPOP_CO")
        @Expose
        public String fEMALEF0T9LVPOPCO;
        @SerializedName("FEMALE_F10T14_LVPOP_CO")
        @Expose
        public String fEMALEF10T14LVPOPCO;
        @SerializedName("FEMALE_F15T19_LVPOP_CO")
        @Expose
        public String fEMALEF15T19LVPOPCO;
        @SerializedName("FEMALE_F20T24_LVPOP_CO")
        @Expose
        public String fEMALEF20T24LVPOPCO;
        @SerializedName("FEMALE_F25T29_LVPOP_CO")
        @Expose
        public String fEMALEF25T29LVPOPCO;
        @SerializedName("FEMALE_F30T34_LVPOP_CO")
        @Expose
        public String fEMALEF30T34LVPOPCO;
        @SerializedName("FEMALE_F35T39_LVPOP_CO")
        @Expose
        public String fEMALEF35T39LVPOPCO;
        @SerializedName("FEMALE_F40T44_LVPOP_CO")
        @Expose
        public String fEMALEF40T44LVPOPCO;
        @SerializedName("FEMALE_F45T49_LVPOP_CO")
        @Expose
        public String fEMALEF45T49LVPOPCO;
        @SerializedName("FEMALE_F50T54_LVPOP_CO")
        @Expose
        public String fEMALEF50T54LVPOPCO;
        @SerializedName("FEMALE_F55T59_LVPOP_CO")
        @Expose
        public String fEMALEF55T59LVPOPCO;
        @SerializedName("FEMALE_F60T64_LVPOP_CO")
        @Expose
        public String fEMALEF60T64LVPOPCO;
        @SerializedName("FEMALE_F65T69_LVPOP_CO")
        @Expose
        public String fEMALEF65T69LVPOPCO;
        @SerializedName("FEMALE_F70T74_LVPOP_CO")
        @Expose
        public String fEMALEF70T74LVPOPCO;

    }

    public class SPOPLOCALRESDDONG {

        @SerializedName("list_total_count")
        @Expose
        public Integer listTotalCount;
        @SerializedName("RESULT")
        @Expose
        public RESULT rESULT;
        @SerializedName("row")
        @Expose
        public List<Row> row = null;
    }

}