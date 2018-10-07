package com.example.dkdk6.ddarawa;

/**
 * Created by dkdk6 on 2018-09-30.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FineDust {
    @SerializedName("ListAirQualityByDistrictService")
    @Expose
    private ListAirQualityByDistrictService listAirQualityByDistrictService;

    public ListAirQualityByDistrictService getListAirQualityByDistrictService() {
        return listAirQualityByDistrictService;
    }

    public void setListAirQualityByDistrictService(ListAirQualityByDistrictService listAirQualityByDistrictService) {
        this.listAirQualityByDistrictService = listAirQualityByDistrictService;
    }
    public class ListAirQualityByDistrictService {

        @SerializedName("list_total_count")
        @Expose
        private Integer listTotalCount;
        @SerializedName("RESULT")
        @Expose
        private RESULT rESULT;
        @SerializedName("row")
        @Expose
        private List<Row> row = null;

        public Integer getListTotalCount() {
            return listTotalCount;
        }

        public void setListTotalCount(Integer listTotalCount) {
            this.listTotalCount = listTotalCount;
        }

        public RESULT getRESULT() {
            return rESULT;
        }

        public void setRESULT(RESULT rESULT) {
            this.rESULT = rESULT;
        }

        public List<Row> getRow() {
            return row;
        }

        public void setRow(List<Row> row) {
            this.row = row;
        }

    }
    public class RESULT {

        @SerializedName("CODE")
        @Expose
        private String cODE;
        @SerializedName("MESSAGE")
        @Expose
        private String mESSAGE;

        public String getCODE() {
            return cODE;
        }

        public void setCODE(String cODE) {
            this.cODE = cODE;
        }

        public String getMESSAGE() {
            return mESSAGE;
        }

        public void setMESSAGE(String mESSAGE) {
            this.mESSAGE = mESSAGE;
        }

    }
    public class Row {

        @SerializedName("MSRDATE")
        @Expose
        private String mSRDATE;
        @SerializedName("MSRADMCODE")
        @Expose
        private String mSRADMCODE;
        @SerializedName("MSRSTENAME")
        @Expose
        private String mSRSTENAME;
        @SerializedName("MAXINDEX")
        @Expose
        private String mAXINDEX;
        @SerializedName("GRADE")
        @Expose
        private String gRADE;
        @SerializedName("POLLUTANT")
        @Expose
        private String pOLLUTANT;
        @SerializedName("NITROGEN")
        @Expose
        private String nITROGEN;
        @SerializedName("OZONE")
        @Expose
        private String oZONE;
        @SerializedName("CARBON")
        @Expose
        private String cARBON;
        @SerializedName("SULFUROUS")
        @Expose
        private String sULFUROUS;
        @SerializedName("PM10")
        @Expose
        private String pM10;
        @SerializedName("PM25")
        @Expose
        private String pM25;

        public String getMSRDATE() {
            return mSRDATE;
        }

        public void setMSRDATE(String mSRDATE) {
            this.mSRDATE = mSRDATE;
        }

        public String getMSRADMCODE() {
            return mSRADMCODE;
        }

        public void setMSRADMCODE(String mSRADMCODE) {
            this.mSRADMCODE = mSRADMCODE;
        }

        public String getMSRSTENAME() {
            return mSRSTENAME;
        }

        public void setMSRSTENAME(String mSRSTENAME) {
            this.mSRSTENAME = mSRSTENAME;
        }

        public String getMAXINDEX() {
            return mAXINDEX;
        }

        public void setMAXINDEX(String mAXINDEX) {
            this.mAXINDEX = mAXINDEX;
        }

        public String getGRADE() {
            return gRADE;
        }

        public void setGRADE(String gRADE) {
            this.gRADE = gRADE;
        }

        public String getPOLLUTANT() {
            return pOLLUTANT;
        }

        public void setPOLLUTANT(String pOLLUTANT) {
            this.pOLLUTANT = pOLLUTANT;
        }

        public String getNITROGEN() {
            return nITROGEN;
        }

        public void setNITROGEN(String nITROGEN) {
            this.nITROGEN = nITROGEN;
        }

        public String getOZONE() {
            return oZONE;
        }

        public void setOZONE(String oZONE) {
            this.oZONE = oZONE;
        }

        public String getCARBON() {
            return cARBON;
        }

        public void setCARBON(String cARBON) {
            this.cARBON = cARBON;
        }

        public String getSULFUROUS() {
            return sULFUROUS;
        }

        public void setSULFUROUS(String sULFUROUS) {
            this.sULFUROUS = sULFUROUS;
        }

        public String getPM10() {
            return pM10;
        }

        public void setPM10(String pM10) {
            this.pM10 = pM10;
        }

        public String getPM25() {
            return pM25;
        }

        public void setPM25(String pM25) {
            this.pM25 = pM25;
        }

    }
}
