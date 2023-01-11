package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentIncentiveBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.models.MonthlyOnlineModel;
import com.kamaii.rider.ui.models.OrderTargetModel;
import com.kamaii.rider.ui.models.SiderModel;
import com.kamaii.rider.ui.models.WeeklyHoursModel;
import com.kamaii.rider.utils.CustomTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IncentiveFragment extends Fragment {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<String> barDataList;
    ArrayList<OrderTargetModel> orderTargetList;
    ArrayList<WeeklyHoursModel> weeklyOnlineList;
    ArrayList<MonthlyOnlineModel> monthlylyOnlineList;
    FragmentIncentiveBinding binding;
    private BaseActivity baseActivity;

    String[] stringArray;
    int count = 0;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    HashMap<String, String> incentiveMap = new HashMap<>();
    String total_earning = "";
    String total_hour_per_day = "";
    String total_earn_today = "";
    String todays_incentive = "";
    String todays_order = "";
    String todays_earning = "";
    String online_payment = "";
    String cash_payment = "";
    String todays_login_hrs = "";
    String rider_online_percentage = "";
    String total_eligible_amount = "";
    String weekly_incentive_amount = "";
    String monthly_incentive_amount = "";
    String achived_order_target_amount = "";
    String weekly_inncetive_eligible = "";
    String monthly_incentive_eligible = "";
    String eligible = "";
    String today_booking_msg = "";
    String weekly_booking_msg = "";
    String monthly_booking_msg = "";
    String today_order_target_msg = "";
    String achived_order_target_eligible = "";
    String rider_today_km = "";
    String rider_total_cancel = "";
    String rider_start_time = "";
    String rider_end_time = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_incentive, container, false);


        //   barDataSet = new BarDataSet(barDataList,"ddad");
        //  barData = new BarData(barDataSet);
        baseActivity.ivLogo.setVisibility(View.VISIBLE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.headerNameTV.setText("");

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        incentiveMap.put(Consts.ARTIST_ID, userDTO.getUser_id());

        orderTargetList = new ArrayList<>();
        weeklyOnlineList = new ArrayList<>();
        getentries();


        return binding.getRoot();
    }

    private void setFirstChartData(ArrayList<OrderTargetModel> charts) {

        ArrayList<BarEntry> targetAchivedList = new ArrayList<BarEntry>();
        ArrayList<BarEntry> targetNotAchivedList = new ArrayList<BarEntry>();

        for (int i = 0; i < orderTargetList.size(); i++) {

            if (orderTargetList.get(i).getEligible().equalsIgnoreCase("1")) {

                targetAchivedList.add(new BarEntry(Integer.parseInt(orderTargetList.get(i).getDay()), Integer.parseInt(orderTargetList.get(i).getCount())));
            } else {
                targetNotAchivedList.add(new BarEntry(Integer.parseInt(orderTargetList.get(i).getDay()), Integer.parseInt(orderTargetList.get(i).getCount())));

            }

        }


        BarDataSet set1;
        BarDataSet set2;

        if (binding.chart123.getData() != null &&
                binding.chart123.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) binding.chart123.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) binding.chart123.getData().getDataSetByIndex(0);
            set1.setValues(targetAchivedList);
            set2.setValues(targetNotAchivedList);
            binding.chart123.getData().notifyDataChanged();
            binding.chart123.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(targetAchivedList, getResources().getString(R.string.incentive_weekly_complete_target_graph));
            set1.setColor(getResources().getColor(R.color.graph_green));
            set2 = new BarDataSet(targetNotAchivedList, getResources().getString(R.string.incentive_weekly_not_complete_target_graph));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            // data.setBarWidth(0.9f);

            binding.chart123.setData(data);
            binding.chart123.setFitBars(true); // make the x-axis fit exactly all bars

            binding.chart123.invalidate();
        }
    }

    public void getentries() {

        new HttpsRequest(Consts.GET_INCENTIVE_DATA_API, incentiveMap, getActivity()).stringPost("", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("INCENTIVE_RESPONSE", "" + response.toString());

                    try {
                        total_earning = response.getString("total_earning");
                        total_hour_per_day = response.getString("total_hour_per_day");
                        total_earn_today = response.getString("total_earn_today");
                        todays_incentive = response.getString("today_incentive");
                        todays_order = response.getString("today_order");
                        todays_earning = response.getString("today_earning");
                        online_payment = response.getString("online_payment");
                        cash_payment = response.getString("cash_payment");
                        todays_login_hrs = response.getString("rider_online_time");
                        rider_online_percentage = response.getString("rider_online_percentage");
                        total_eligible_amount = response.getString("total_eligible_amount");
                        weekly_incentive_amount = response.getString("weekly_incentive_amount");
                        monthly_incentive_amount = response.getString("monthly_incentive_amount");
                        achived_order_target_amount = response.getString("achived_order_target_amount");
                        weekly_inncetive_eligible = response.getString("weekly_inncetive_eligible");
                        monthly_incentive_eligible = response.getString("monthly_incentive_eligible");
                        eligible = response.getString("eligible");
                        today_booking_msg = response.getString("today_booking_msg");
                        weekly_booking_msg = response.getString("weekly_booking_msg");
                        monthly_booking_msg = response.getString("monthly_booking_msg");
                        today_order_target_msg = response.getString("today_order_target_msg");
                        achived_order_target_eligible = response.getString("achived_order_target_eligible");
                        rider_today_km = response.getString("rider_today_km");
                        rider_total_cancel = response.getString("rider_total_cancel");
                        rider_start_time = response.getString("rider_start_time");
                        rider_end_time = response.getString("rider_end_time");

                        Type getpetDTO = new TypeToken<List<OrderTargetModel>>() {
                        }.getType();
                        Type getpetDTO1 = new TypeToken<List<WeeklyHoursModel>>() {
                        }.getType();
                        Type getpetDTO2 = new TypeToken<List<MonthlyOnlineModel>>() {
                        }.getType();

                        orderTargetList = new Gson().fromJson(response.getJSONArray("today_order_target").toString(), getpetDTO);
                        weeklyOnlineList = new Gson().fromJson(response.getJSONArray("weekly_online_hrs_target").toString(), getpetDTO1);
                        monthlylyOnlineList = new Gson().fromJson(response.getJSONArray("monthly_online_hrs_target").toString(), getpetDTO2);

                        binding.endhours.setText(total_hour_per_day+" Hours");
                        setFirstChart();
                        setSecondChart();
                        setMonthlyChart();
                        setupData();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }

    private void setMonthlyChart() {

        binding.chart789.setDrawBarShadow(false);
        binding.chart789.setDrawValueAboveBar(true);
        binding.chart789.getDescription().setEnabled(false);
        binding.chart789.setMaxVisibleValueCount(60);
        binding.chart789.setPinchZoom(false);
        binding.chart789.setDrawGridBackground(false);

        final ArrayList<String> xAxisLabel = new ArrayList<>();

        for (int i = 0; i < monthlylyOnlineList.size(); i++) {

            xAxisLabel.add(monthlylyOnlineList.get(i).getDay());
        }

        stringArray = xAxisLabel.toArray(new String[0]);

        //  Log.e("VALUE",""+" List "+xAxisLabel.toString());

        XAxis xAxis = binding.chart789.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);


        YAxis leftAxis = binding.chart789.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(1f); // this replaces setStartAtZero(true)

        YAxis rightAxis = binding.chart789.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = binding.chart789.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        setMonthlyChartData(monthlylyOnlineList);

    }

    private void setMonthlyChartData(ArrayList<MonthlyOnlineModel> monthlylyOnlineList) {

        ArrayList<BarEntry> targetAchivedList = new ArrayList<BarEntry>();
        ArrayList<BarEntry> targetNotAchivedList = new ArrayList<BarEntry>();

        for (int i = 0; i < monthlylyOnlineList.size(); i++) {

            if (i < monthlylyOnlineList.size()) {

                if (monthlylyOnlineList.get(i).getEligible().equalsIgnoreCase("1")) {

                    targetAchivedList.add(new BarEntry(i + 1, Integer.parseInt(monthlylyOnlineList.get(i).getCount())));
                } else {
                    targetNotAchivedList.add(new BarEntry(i + 1, Integer.parseInt(monthlylyOnlineList.get(i).getCount())));

                }

            }
        }


        BarDataSet set1;
        BarDataSet set2;

        if (binding.chart789.getData() != null &&
                binding.chart789.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) binding.chart789.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) binding.chart789.getData().getDataSetByIndex(0);
            set1.setValues(targetAchivedList);
            set2.setValues(targetNotAchivedList);
            binding.chart789.getData().notifyDataChanged();
            binding.chart789.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(targetAchivedList, getResources().getString(R.string.incentive_weekly_complete_target_graph));
            set2 = new BarDataSet(targetNotAchivedList, getResources().getString(R.string.incentive_weekly_not_complete_target_graph));
            set1.setColor(getResources().getColor(R.color.graph_green));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            // data.setBarWidth(0.9f);

            binding.chart789.setData(data);
            binding.chart789.setFitBars(true); // make the x-axis fit exactly all bars

            binding.chart789.invalidate();
        }
    }

    private void setSecondChart() {

        binding.chart456.setDrawBarShadow(false);
        binding.chart456.setDrawValueAboveBar(true);
        binding.chart456.getDescription().setEnabled(false);
        binding.chart456.setMaxVisibleValueCount(60);
        binding.chart456.setPinchZoom(false);
        binding.chart456.setDrawGridBackground(false);

        final ArrayList<String> xAxisLabel = new ArrayList<>();

        for (int i = 0; i < weeklyOnlineList.size(); i++) {

            xAxisLabel.add(weeklyOnlineList.get(i).getDay());
        }

        stringArray = xAxisLabel.toArray(new String[0]);

        //  Log.e("VALUE",""+" List "+xAxisLabel.toString());

        XAxis xAxis = binding.chart456.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //   xAxis.setLabelRotationAngle(45);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                if (value == 1) {
                    return "Mon";
                }
                if (value == 2) {
                    return "Tue";
                }
                if (value == 3) {
                    return "Wed";
                }
                if (value == 4) {
                    return "Thu";
                }
                if (value == 5) {
                    return "Fri";
                }
                if (value == 6) {
                    return "Sat";
                }
                if (value == 7) {
                    return "Sun";
                } else {
                    return "";
                }
            }
        });


        YAxis leftAxis = binding.chart456.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = binding.chart456.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = binding.chart456.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        setSecondChartData(weeklyOnlineList);

    }

    private void setSecondChartData(ArrayList<WeeklyHoursModel> weeklyOnlineList) {

        ArrayList<BarEntry> targetAchivedList = new ArrayList<BarEntry>();
        ArrayList<BarEntry> targetNotAchivedList = new ArrayList<BarEntry>();

        final ArrayList<String> xAxisLabel = new ArrayList<>();

        for (int i = 0; i < weeklyOnlineList.size(); i++) {

            xAxisLabel.add(weeklyOnlineList.get(i).getDay());
        }

        for (int i = 0; i < weeklyOnlineList.size(); i++) {

            if (i < weeklyOnlineList.size()) {

                if (weeklyOnlineList.get(i).getEligible().equalsIgnoreCase("1")) {

                    targetAchivedList.add(new BarEntry(i + 1, Integer.parseInt(weeklyOnlineList.get(i).getCount())));
                } else {
                    targetNotAchivedList.add(new BarEntry(i + 1, Integer.parseInt(weeklyOnlineList.get(i).getCount())));

                }
            }


        }


        BarDataSet set1;
        BarDataSet set2;

        if (binding.chart456.getData() != null &&
                binding.chart456.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) binding.chart456.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) binding.chart456.getData().getDataSetByIndex(0);
            set1.setValues(targetAchivedList);
            set2.setValues(targetNotAchivedList);
            binding.chart456.getData().notifyDataChanged();
            binding.chart456.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(targetAchivedList, getResources().getString(R.string.incentive_weekly_complete_target_graph));
            set2 = new BarDataSet(targetNotAchivedList, getResources().getString(R.string.incentive_weekly_not_complete_target_graph));
            set1.setColor(getResources().getColor(R.color.graph_green));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            // data.setBarWidth(0.9f);

            binding.chart456.setData(data);
            binding.chart456.setFitBars(true); // make the x-axis fit exactly all bars

            binding.chart456.invalidate();
        }
    }

    private void setFirstChart() {

        binding.chart123.setDrawBarShadow(false);
        binding.chart123.setDrawValueAboveBar(true);
        binding.chart123.getDescription().setEnabled(false);
        binding.chart123.setMaxVisibleValueCount(60);
        binding.chart123.setPinchZoom(false);
        binding.chart123.setDrawGridBackground(false);

        final ArrayList<String> xAxisLabel1 = new ArrayList<>();

        for (int i = 0; i < orderTargetList.size(); i++) {

            xAxisLabel1.add(orderTargetList.get(i).getDay());
        }

        stringArray = xAxisLabel1.toArray(new String[0]);

        Log.e("VALUE", " List " + stringArray.toString());

        XAxis xAxis = binding.chart123.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        //  xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        Log.e("value", "formater" + xAxisLabel1.toString());

        // xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

        binding.chart123.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.e("value", "formater 2 " + value);
                if (value == 1) {
                    return "3 Order";
                }
                if (value == 2) {
                    return "6 Order";
                }
                if (value == 3) {
                    return "9 Order";
                }
                if (value == 4) {
                    return "12 Order";
                } else {
                    return "";
                }
            }
        });


        //  xAxis.setAxisMaximum(orderTargetList.size());

        final ArrayList<String> xAxisLabel123 = new ArrayList<>();
        xAxisLabel123.add("3");
        xAxisLabel123.add("6");
        xAxisLabel123.add("9");
        xAxisLabel123.add("12");
        xAxisLabel123.add("15");
        xAxisLabel123.add("18");

        YAxis leftAxis = binding.chart123.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = binding.chart123.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = binding.chart123.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        setFirstChartData(orderTargetList);

    }

    private void setupData() {

        binding.totalEarningDigit.setText(total_earning);
        binding.todaysIncentiveDig.setText(todays_incentive);
        binding.totalBooking.setText(todays_order);
        binding.todaysEarn.setText(todays_earning);
        binding.onlinePymentDig.setText(online_payment);
        binding.cashPaymentDig.setText(cash_payment);
        binding.totalHrsDigit.setText(todays_login_hrs);

        if (rider_total_cancel.equalsIgnoreCase("0") || rider_total_cancel.equalsIgnoreCase("1")) {
            binding.totalCancelBookingHead.setText(getResources().getString(R.string.incentive_order_cancel));
        }
        binding.totalCancelBooking.setText(rider_total_cancel);
        binding.todaysKm.setText(rider_today_km);
        binding.timeAm.setText(rider_start_time);
        binding.timePm.setText(rider_end_time);


        if (!total_eligible_amount.equalsIgnoreCase("0")) {

            binding.achivedTargetAmount.setText(total_earn_today + " / ");
            binding.totalEligibleAmt.setText(total_eligible_amount);
        }
        binding.hrsProgressbar.setProgress(Integer.parseInt(rider_online_percentage));

        binding.todayEligibleAmountDigit.setText(Html.fromHtml("&#8377;" + total_eligible_amount));
        binding.chart1AmountDigit.setText(Html.fromHtml("&#8377;" + achived_order_target_amount));

        binding.chart2AmountDigit.setText(Html.fromHtml("&#8377;" + weekly_incentive_amount));
        binding.chart3AmountDigit.setText(Html.fromHtml("&#8377;" + monthly_incentive_amount));


        Log.e("ELIGIBLE_FLAGS", "" + eligible + " order:-- " + achived_order_target_eligible + " weekly " + weekly_inncetive_eligible);

        binding.progressEligibleTxt.setText(today_booking_msg);

        binding.firstGraphEligibleTxt.setText(today_order_target_msg);

        binding.weeklyTxt.setText(weekly_booking_msg);

        binding.monthlyTxt.setText(monthly_booking_msg);


        if (eligible.equals("0")) {

            binding.todayEligibleAmountDigit.setTextColor(getResources().getColor(R.color.incentive_red_txt_background));
            binding.progressEligibleTxt.setTextColor(getResources().getColor(R.color.incentive_red_txt_background));
            binding.progressRelative.setBackground(getResources().getDrawable(R.drawable.intencive_red_not_eligible_bg));
        }

        if (achived_order_target_eligible.equalsIgnoreCase("0")) {

            binding.chart1AmountDigit.setTextColor(getResources().getColor(R.color.incentive_red_txt_background));
            binding.firstGraphEligibleTxt.setTextColor(getResources().getColor(R.color.incentive_red_txt_background));
            binding.firstGraphRelative.setBackground(getResources().getDrawable(R.drawable.intencive_red_not_eligible_bg));
        }

        if (weekly_inncetive_eligible.equalsIgnoreCase("0")) {
            binding.chart2AmountDigit.setTextColor(getResources().getColor(R.color.incentive_red_txt_background));
            binding.weeklyTxt.setTextColor(getResources().getColor(R.color.incentive_red_txt_background));
            binding.weeklyTxtRelative.setBackground(getResources().getDrawable(R.drawable.intencive_red_not_eligible_bg));
        }
        if (monthly_incentive_eligible.equalsIgnoreCase("0")) {

            binding.chart3AmountDigit.setTextColor(getResources().getColor(R.color.incentive_red_txt_background));
            binding.monthlyTxt.setTextColor(getResources().getColor(R.color.incentive_red_txt_background));
            binding.monthlyTxtRelative.setBackground(getResources().getDrawable(R.drawable.intencive_red_not_eligible_bg));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}