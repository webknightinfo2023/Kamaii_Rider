package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.DTO.WalletHistory;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.AddMoney;
import com.kamaii.rider.ui.activity.BankDocument;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AdapterWalletHistory;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Wallet extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private LinearLayout llAddMoney;
    private CustomTextView tvAll, tvDebit, tvCredit;
    private CustomTextView tvAllSelect, tvDebitSelect, tvCreditSelect;
    private AdapterWalletHistory adapterWalletHistory;
    private ArrayList<WalletHistory> walletHistoryList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String TAG = Wallet.class.getSimpleName();
    private RecyclerView RVhistorylist;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private String status = "";
    HashMap<String, String> parms;
    HashMap<String, String> parmsGetWallet = new HashMap<>();
    private CustomTextViewBold tvWallet;
    private String amt = "";
    double amt1;
    private String currency = "";
    private BaseActivity baseActivity;
    Button btncashout;
    private Dialog dialog;
    CustomEditText etamount;
    CustomTextViewBold tvAdd;
    ImageView tvCancel;
    String bank_details_flag = "";
    String wallet_rate = "";
    String razorpay_key = "";
    CustomTextViewBold diposite_note;
    private HashMap<String, String> paramsRequest = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.ic_wallet));
        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());

        parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());
        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {
        tvWallet = v.findViewById(R.id.tvWallet);

        tvAll = v.findViewById(R.id.tvAll);
        tvAll.setOnClickListener(this);

        tvDebit = v.findViewById(R.id.tvDebit);
        tvDebit.setOnClickListener(this);

        tvCredit = v.findViewById(R.id.tvCredit);
        tvCredit.setOnClickListener(this);

        tvAllSelect = v.findViewById(R.id.tvAllSelect);
        tvDebitSelect = v.findViewById(R.id.tvDebitSelect);
        tvCreditSelect = v.findViewById(R.id.tvCreditSelect);

        llAddMoney = v.findViewById(R.id.llAddMoney);
        llAddMoney.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        tvNo = v.findViewById(R.id.tvNo);
        RVhistorylist = v.findViewById(R.id.RVhistorylist);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVhistorylist.setLayoutManager(mLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);


        btncashout = v.findViewById(R.id.btncashout);

        dialog = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailog_cashout);
        etamount = dialog.findViewById(R.id.etamount);
        tvCancel = dialog.findViewById(R.id.tvCancel);
        tvAdd = dialog.findViewById(R.id.tvAdd);
        diposite_note = dialog.findViewById(R.id.diposite_note);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btncashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bank_details_flag.equalsIgnoreCase("0")) {

                    Intent intent = new Intent(getActivity(), BankDocument.class);
                    intent.putExtra("from_wallet", true);
                    startActivity(intent);
                } else {
                    if (Double.parseDouble(amt) > 500) {

                        dialog.show();
                    } else {
                        Toast.makeText(getActivity(), "More than 500 is required for cashout", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etamount.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Enter Amount ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Double.parseDouble(etamount.getText().toString()) > amt1) {
                    //Toast.makeText(getActivity(), "Amount Should Be Grater Than 200", Toast.LENGTH_SHORT).show();
                    diposite_note.setText("You can not cashout above "+amt1+" , 500 will be your diposite amount.");
                    diposite_note.setVisibility(View.VISIBLE);
                    return;
                }

                if (Double.parseDouble(etamount.getText().toString()) > Double.parseDouble(amt)) {
                    Toast.makeText(getActivity(), "Amount should be less than wallet amount", Toast.LENGTH_SHORT).show();
                    return;
                }


                requestPayment();
                //bookDailog();
            }
        });
    }

    public void requestPayment() {
        paramsRequest.put(Consts.AMOUNT, etamount.getText().toString());
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.WALLET_REQUEST_API, paramsRequest, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    //  ProjectUtils.showLong(getActivity(), msg);

                    dialog.dismiss();
                    getWallet();
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    // ProjectUtils.showLong(getActivity(), msg);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAddMoney:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    Intent in = new Intent(getActivity(), AddMoney.class);
                    in.putExtra(Consts.AMOUNT, amt);
                    in.putExtra(Consts.CURRENCY, currency);
                    in.putExtra("wallet_rate", wallet_rate);
                    in.putExtra("razorpay_key", razorpay_key);
                    startActivity(in);
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.tvAll:
                setSelected(true, false, false);
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                getHistroy();
                break;
            case R.id.tvCredit:
                setSelected(false, false, true);
                status = "0";
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                parms.put(Consts.STATUS, status);
                getHistroy();
                break;
            case R.id.tvDebit:
                setSelected(false, true, false);
                status = "1";
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                parms.put(Consts.STATUS, status);
                getHistroy();
                break;
        }
    }

    public void getHistroy() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_WALLET_HISTORY_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    Log.e("wallethistory",""+response.toString());
                    tvNo.setVisibility(View.GONE);
                    RVhistorylist.setVisibility(View.VISIBLE);
                    try {
                        bank_details_flag = response.getString("bank_status");
                        wallet_rate = response.getString("wallet_rate");
                        razorpay_key = response.getString("razorpay_key");
                        walletHistoryList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<WalletHistory>>() {
                        }.getType();
                        walletHistoryList = (ArrayList<WalletHistory>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    RVhistorylist.setVisibility(View.GONE);

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getWallet();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            getHistroy();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );

    }

    public void getWallet() {
        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        Log.e("dhuiwqnoqwnoiqw", "" + response.toString());

                        amt = response.getJSONObject("data").getString("amount");
                        currency = response.getJSONObject("data").getString("currency_type");
                        tvWallet.setText(currency + " " + amt);

                        if (Double.parseDouble(amt) > 500) {
                            amt1 = Double.parseDouble(amt) - 500;
                            etamount.setText(String.valueOf(amt1));
                        }
                        diposite_note.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

    public void showData() {


        if (walletHistoryList.size() > 0) {
            tvNo.setVisibility(View.GONE);
            RVhistorylist.setVisibility(View.VISIBLE);

            adapterWalletHistory = new AdapterWalletHistory(Wallet.this, walletHistoryList);
            RVhistorylist.setAdapter(adapterWalletHistory);
        } else {
            tvNo.setVisibility(View.VISIBLE);
            RVhistorylist.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getHistroy();
    }

    public void setSelected(boolean firstBTN, boolean secondBTN, boolean thirdBTN) {
        if (firstBTN) {
            tvAllSelect.setVisibility(View.VISIBLE);
            tvDebitSelect.setVisibility(View.GONE);
            tvCreditSelect.setVisibility(View.GONE);

        }
        if (secondBTN) {
            tvDebitSelect.setVisibility(View.VISIBLE);
            tvAllSelect.setVisibility(View.GONE);
            tvCreditSelect.setVisibility(View.GONE);

        }
        if (thirdBTN) {
            tvCreditSelect.setVisibility(View.VISIBLE);
            tvAllSelect.setVisibility(View.GONE);
            tvDebitSelect.setVisibility(View.GONE);

        }
        tvAllSelect.setSelected(firstBTN);
        tvDebitSelect.setSelected(secondBTN);
        tvCreditSelect.setSelected(secondBTN);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}
