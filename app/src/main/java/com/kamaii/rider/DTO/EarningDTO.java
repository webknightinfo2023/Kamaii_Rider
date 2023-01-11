package com.kamaii.rider.DTO;

import java.util.ArrayList;

/**
 * Created by VARUN on 01/01/19.
 */
public class EarningDTO {

    String onlineEarning = "";
    String cashEarning = "";
    float  walletAmount = 0;
    String totalEarning = "";
    String jobDone = "";
    String totalJob = "";
    String currency_symbol = "";
    String completePercentages = "";
    String referral_earning = "";
    String tot_ref = "";
    String online_hours = "";
    String totalincentive = "";
    String totaladjustamount = "";
    String totalearningamount = "";


    public String getTotalincentive() {
        return totalincentive;
    }

    public void setTotalincentive(String totalincentive) {
        this.totalincentive = totalincentive;
    }

    public String getTotaladjustamount() {
        return totaladjustamount;
    }

    public void setTotaladjustamount(String totaladjustamount) {
        this.totaladjustamount = totaladjustamount;
    }

    public String getTotalearningamount() {
        return totalearningamount;
    }

    public void setTotalearningamount(String totalearningamount) {
        this.totalearningamount = totalearningamount;
    }

    ArrayList<ChartData> chartData = new ArrayList<>();

    public String getOnline_hours() {
        return online_hours;
    }

    public void setOnline_hours(String online_hours) {
        this.online_hours = online_hours;
    }

    public String getTot_ref() {
        return tot_ref;
    }

    public void setTot_ref(String tot_ref) {
        this.tot_ref = tot_ref;
    }

    public String getReferral_earning() {
        return referral_earning;
    }

    public void setReferral_earning(String referral_earning) {
        this.referral_earning = referral_earning;
    }

    public String getOnlineEarning() {
        return onlineEarning;
    }

    public void setOnlineEarning(String onlineEarning) {
        this.onlineEarning = onlineEarning;
    }

    public String getCashEarning() {
        return cashEarning;
    }

    public void setCashEarning(String cashEarning) {
        this.cashEarning = cashEarning;
    }

    public float getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(float walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(String totalEarning) {
        this.totalEarning = totalEarning;
    }

    public String getJobDone() {
        return jobDone;
    }

    public void setJobDone(String jobDone) {
        this.jobDone = jobDone;
    }

    public String getTotalJob() {
        return totalJob;
    }

    public void setTotalJob(String totalJob) {
        this.totalJob = totalJob;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getCompletePercentages() {
        return completePercentages;
    }

    public void setCompletePercentages(String completePercentages) {
        this.completePercentages = completePercentages;
    }

    public ArrayList<ChartData> getChartData() {
        return chartData;
    }

    public void setChartData(ArrayList<ChartData> chartData) {
        this.chartData = chartData;
    }

    public class ChartData {
        String day = "";
        String count = "";

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
