package com.example.dkdk6.ddarawa;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dkdk6 on 2018-10-06.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeoulFineDust {

        @SerializedName("ForecastWarningUltrafineParticleOfDustService")
        @Expose
        private ForecastWarningUltrafineParticleOfDustService forecastWarningUltrafineParticleOfDustService;
        public ForecastWarningUltrafineParticleOfDustService getForecastWarningUltrafineParticleOfDustService() {
            return forecastWarningUltrafineParticleOfDustService;
        }
        public void setForecastWarningUltrafineParticleOfDustService(ForecastWarningUltrafineParticleOfDustService forecastWarningUltrafineParticleOfDustService) {
            this.forecastWarningUltrafineParticleOfDustService = forecastWarningUltrafineParticleOfDustService;
        }
    public class ForecastWarningUltrafineParticleOfDustService {

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

        @SerializedName("APPLC_DT")
        @Expose
        private String aPPLCDT;
        @SerializedName("FA_ON")
        @Expose
        private String fAON;
        @SerializedName("POLLUTANT")
        @Expose
        private String pOLLUTANT;
        @SerializedName("CAISTEP")
        @Expose
        private String cAISTEP;
        @SerializedName("ALARM_CNDT")
        @Expose
        private String aLARMCNDT;
        @SerializedName("ALERTSTEP")
        @Expose
        private String aLERTSTEP;
        @SerializedName("CNDT1")
        @Expose
        private String cNDT1;

        public String getAPPLCDT() {
            return aPPLCDT;
        }

        public void setAPPLCDT(String aPPLCDT) {
            this.aPPLCDT = aPPLCDT;
        }

        public String getFAON() {
            return fAON;
        }

        public void setFAON(String fAON) {
            this.fAON = fAON;
        }

        public String getPOLLUTANT() {
            return pOLLUTANT;
        }

        public void setPOLLUTANT(String pOLLUTANT) {
            this.pOLLUTANT = pOLLUTANT;
        }

        public String getCAISTEP() {
            return cAISTEP;
        }

        public void setCAISTEP(String cAISTEP) {
            this.cAISTEP = cAISTEP;
        }

        public String getALARMCNDT() {
            return aLARMCNDT;
        }

        public void setALARMCNDT(String aLARMCNDT) {
            this.aLARMCNDT = aLARMCNDT;
        }

        public String getALERTSTEP() {
            return aLERTSTEP;
        }

        public void setALERTSTEP(String aLERTSTEP) {
            this.aLERTSTEP = aLERTSTEP;
        }

        public String getCNDT1() {
            return cNDT1;
        }

        public void setCNDT1(String cNDT1) {
            this.cNDT1 = cNDT1;
        }

    }
}
