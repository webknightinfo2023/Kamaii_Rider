package com.kamaii.rider.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.DTO.WalletHistory;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.models.neworder.GetLogModel;
import com.kamaii.rider.utils.CustomButton;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;
import com.paykun.sdk.PaykunApiCall;
import com.paykun.sdk.eventbus.Events;
import com.paykun.sdk.eventbus.GlobalBus;
import com.paykun.sdk.helper.PaykunHelper;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddMoney extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private String TAG = AddMoney.class.getSimpleName();
    private Context mContext;
    private CustomEditText etAddMoney;
    private CustomTextView tv1000, tv1500, tv2000, llltxt;
    private LinearLayout cbAdd;
    int rs = 0;
    int final_rs = 0;
    private HashMap<String, String> parmas = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String amt = "";
    private String currency = "";
    private String razorpay_key = "";
    private String wallet_rate = "";
    private CustomTextView tvWallet;
    private ImageView ivBack;
    private Dialog dialog;
    private LinearLayout llPaypall, llStripe, llCancel, llPayuMoney, llrozopay;
    Button btn_pay;
    int amount = 0;
    CustomTextViewBold tvYesQuali,llltxtamount;
    private String productName = "WalletPartner";
    boolean from_home = false;
    HashMap<String, String> parms;
    String bank_details_flag = "";
    List<GetLogModel> keyList;
    HashMap<String,String> cancelLogParam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        mContext = AddMoney.this;


        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmas.put(Consts.USER_ID, userDTO.getUser_id());
        parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        llltxt = findViewById(R.id.llltxt);
        tvYesQuali = findViewById(R.id.tvYesQuali);
        llltxtamount = findViewById(R.id.llltxtamount);
        cancelLogParam = new HashMap<>();
        cancelLogParam.put(Consts.USER_ID, userDTO.getUser_id());

        getHistroy();
        setUiAction();
    }

    public void getHistroy() {
        ProjectUtils.showProgressDialog(AddMoney.this, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_WALLET_HISTORY_API, parms, AddMoney.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    Log.e("wallethistory",""+response.toString());
                    try {
                        bank_details_flag = response.getString("bank_status");
                        wallet_rate = response.getString("wallet_rate");
                        razorpay_key = response.getString("razorpay_key");

                        Log.e("wallet_rate"," -- " + wallet_rate+" razorpay_key "+razorpay_key);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    public void setUiAction() {
        tvWallet = findViewById(R.id.tvWallet);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent().hasExtra(Consts.AMOUNT)) {
            amt = getIntent().getStringExtra(Consts.AMOUNT);
            currency = getIntent().getStringExtra(Consts.CURRENCY);
           // wallet_rate = getIntent().getStringExtra("wallet_rate");
           /// razorpay_key = getIntent().getStringExtra("razorpay_key");
            from_home = getIntent().getBooleanExtra("from_home", false);

            Log.e("from_home", "" + from_home + " --" + amt + " -- " + currency + " -- " + wallet_rate+" razorpay_key "+razorpay_key);
            tvWallet.setText(currency + " " + amt);
            llltxtamount.setText(wallet_rate+"%");

        }

        etAddMoney = findViewById(R.id.etAddMoney);
        etAddMoney.setSelection(etAddMoney.getText().length());

        if (from_home) {

            etAddMoney.setText(amt);
            tvYesQuali.setText("Pay now");
        }
        cbAdd = findViewById(R.id.cbAdd);
        cbAdd.setOnClickListener(this);


        tv1000 = findViewById(R.id.tv1000);
        tv1000.setOnClickListener(this);

        tv1500 = findViewById(R.id.tv1500);
        tv1500.setOnClickListener(this);

        tv2000 = findViewById(R.id.tv2000);
        tv2000.setOnClickListener(this);

        tv1000.setText("+ " + currency + " 1000");
        tv1500.setText("+ " + currency + " 1500");
        tv2000.setText("+ " + currency + " 2000");

        // llltxtamount.setText("2%");
        llltxt.setText(" " + "convernience fees will be applied");
    }

    @Override
    public void onClick(View v) {
        if (etAddMoney.getText().toString().trim().equalsIgnoreCase("")) {


        } else {


        }

        switch (v.getId()) {
            case R.id.tv1000:
                rs = 1000;
                final_rs = rs;
                etAddMoney.setText(final_rs + "");
                etAddMoney.setSelection(etAddMoney.getText().length());
                break;
            case R.id.tv1500:
                rs = 1500;
                final_rs = rs;
                etAddMoney.setText(final_rs + "");
                etAddMoney.setSelection(etAddMoney.getText().length());
                break;
            case R.id.tv2000:
                rs = 2000;
                final_rs = rs;
                etAddMoney.setText(final_rs + "");
                etAddMoney.setSelection(etAddMoney.getText().length());
                break;
            case R.id.cbAdd:

                if (etAddMoney.getText().toString().length() > 0 && Float.parseFloat(etAddMoney.getText().toString().trim()) > 0) {
                    if (NetworkManager.isConnectToInternet(mContext)) {

                        parmas.put(Consts.AMOUNT, ProjectUtils.getEditTextValue(etAddMoney));

                        createLog();

                    } else {
                        ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
                    }
                } else {
                    ProjectUtils.showLong(mContext, getResources().getString(R.string.val_money));
                }
                break;
        }
    }

    private void createLog() {

        new HttpsRequest(Consts.CREATE_LOG,parmas,mContext).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag){
                    Log.e(TAG, " createLog backResponse: "+response.toString() );

                    try {
                        keyList = new ArrayList<>();

                        Type dto = new TypeToken<List<GetLogModel>>(){}.getType();
                        keyList = new Gson().fromJson(response.getJSONObject("data").getJSONArray("payment_param").toString(),dto);

                        Log.e(TAG, "keyList backResponse: "+keyList.toString() );
                        cancelLogParam.put("key1",keyList.get(0).getKey0());
                        parmas.put("key1",keyList.get(0).getKey0());
                        sendRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendRequest() {

        Log.e("log key 1 ",""+keyList.get(0).getKey0());

        parmas.put("key1",keyList.get(0).getKey0());
        Log.e("log key",""+keyList.get(1).getKey1());
        double extraadd = (Double.parseDouble(ProjectUtils.getEditTextValue(etAddMoney)) * Double.parseDouble(wallet_rate)) / 100;
        double total = Double.parseDouble(ProjectUtils.getEditTextValue(etAddMoney)) + extraadd;

        DecimalFormat newFormat = new DecimalFormat("####");
        float mainprice = Float.valueOf(newFormat.format(total));


        Checkout checkout = new Checkout();
        // with testing_key  checkout.setKeyID("rzp_test_cN1FC6C2mOYSwF");
        //  checkout.setKeyID("rzp_live_WxwhNFQFzmlsOU");
        checkout.setKeyID(razorpay_key);
        checkout.setImage(R.drawable.logo);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            Date d = new Date();
            System.out.println(d.getTime());

            options.put("name", "Kamaii Services Pvt Ltd");
            //   options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("id", "order_"+d.getTime());//from response of step 3.
//                            options.put("send_sms_hash", true);
            options.put("theme.color", "#3399cc");

            JSONObject notes = new JSONObject();
            notes.put("key1", keyList.get(0).getKey0());
            notes.put("key2", keyList.get(1).getKey1());
            notes.put("key3", keyList.get(2).getKey2());
            notes.put("key4", keyList.get(3).getKey3());
            notes.put("key5", keyList.get(4).getKey4());
            notes.put("key6", keyList.get(5).getKey5());
            notes.put("key7", keyList.get(6).getKey6());
            notes.put("key8", keyList.get(7).getKey7());
            notes.put("key9", keyList.get(8).getKey8());
            notes.put("key10", keyList.get(9).getKey9());
            notes.put("key11", keyList.get(10).getKey10());
            notes.put("key12", keyList.get(11).getKey11());
            notes.put("key13", keyList.get(12).getKey12());
            notes.put("key14", keyList.get(13).getKey13());
            notes.put("key15", keyList.get(14).getKey14());

            options.put("notes",notes);

            options.put("currency", "INR");
            options.put("amount", String.valueOf(mainprice * 100));//pass amount in currency subunits
            options.put("prefill.email", userDTO.getEmail_id());
            options.put("prefill.contact", userDTO.getMobile());
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }

    }


    public void addMoney() {
        Log.e(TAG, "addMoney: params:-- "+parmas.toString());
        new HttpsRequest(Consts.ADD_MONEY_API, parmas, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPaymentSuccess(String s) {
        addMoney();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG, "onPaymentError: "+"cancel request" );

        new HttpsRequest(Consts.CANCEL_REQUEST_LOG,cancelLogParam,mContext).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag){
                    Log.e(TAG, "onPaymentError cancel "+response.toString() );
                }else {

                }
            }
        });
    }
}
