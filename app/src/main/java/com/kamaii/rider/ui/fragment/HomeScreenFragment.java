package com.kamaii.rider.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentCabBookingsBinding;
import com.kamaii.rider.databinding.FragmentHomeScreenBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.model.CommonServiceModel;
import com.kamaii.rider.model.ThirdCateModel;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.service.MyService;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.SliderAdapter;
import com.kamaii.rider.ui.models.AddressModel;
import com.kamaii.rider.ui.models.CategoryModel;
import com.kamaii.rider.ui.models.Description;
import com.kamaii.rider.ui.models.GuideModel;
import com.kamaii.rider.ui.models.HomePageModel;
import com.kamaii.rider.ui.models.MonthlyOnlineModel;
import com.kamaii.rider.ui.models.OrderTargetModel;
import com.kamaii.rider.ui.models.SiderModel;
import com.kamaii.rider.ui.models.SubCateModel;
import com.kamaii.rider.ui.models.WeeklyHoursModel;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.FileUtility;
import com.kamaii.rider.utils.SpinnerDialog;
import com.kamaii.rider.utils.SpinnerDialogFour;
import com.kamaii.rider.utils.SpinnerDialogSubCate;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.turf.TurfConversion;
import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.prefs.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class HomeScreenFragment extends Fragment {

    private BaseActivity baseActivity;
    private FragmentHomeScreenBinding binding;
    private ArrayList<ArtistBooking> artistBookingsList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private String TAG = CabBookingsFragment.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> parmsartist = new HashMap<>();
    private LayoutInflater myInflater;
    Context mContext;
    public static HashMap<String, File> paramsFile;
    public static Uri uri = null;
    private ArrayList<SiderModel> tvideoModelArrayList = new ArrayList<>();
    private ArrayList<HomePageModel> homePageArrayList = new ArrayList<>();
    private HashMap<String, String> parms = new HashMap<>();

    private HashMap<String, String> paramsUpdate;
    private ArtistDetailsDTO artistDetailsDTO;
    private HashMap<String, String> paramsDecline;
    private String bookingid = "";
    String currentdate = "";
    LocalBroadcastManager bm;
    private SharedPreferences firebase;
    String devicetoken = "";
    ProgressDialog progressDialog;
    String dcatid = "", dsubcatid = "", dthirdcatid = "", dclatititute = "", dclongitiute = "";
    CountDownTimer cT;
    private GoogleMap googleMap;
    boolean accept = false;
    private HashMap<String, String> parmsearning = new HashMap<>();

    String yt_image_link = "";
    String share_profile_link = "";
    String refer_img_link = "";
    String cat_image_link = "";
    String online_msg = "";
    String offline_msg = "";

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
    HashMap<String, String> incentiveMap = new HashMap<>();


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //  binding.mapView.onCreate(savedInstanceState);
        paramsDecline = new HashMap<>();
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        devicetoken = firebase.getString(Consts.DEVICE_TOKEN, "");
        myInflater = LayoutInflater.from(getActivity());
        parmsartist.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.USER_ID, userDTO.getUser_id());
        parmsearning.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.DEVICE_TOKEN, devicetoken);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        checkaddress();
        //  binding.pulsator.start();
        incentiveMap.put(Consts.ARTIST_ID, userDTO.getUser_id());

        binding.stopRingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(MyService.class)) {
                    Intent intent = new Intent(getContext(), MyService.class);
                    getContext().stopService(intent);
                }
            }
        });

        Date todayyyy = new Date();
        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentdate = formatt.format(todayyyy);

        //   baseActivity.ivLogo.setVisibility(View.VISIBLE);

        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);

        //    binding.mapView.onResume();
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        binding.partnerName.setText("Hello " + userDTO.getName() + "!");

        binding.bLinearOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.navItemIndex = 52;

                baseActivity.loadHomeFragment(new MyEarning(), "8");

            }
        });
        binding.bLinearTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.navItemIndex = 53;

                baseActivity.loadHomeFragment(new MyEarning(), "8");

            }
        });


        baseActivity.headerNameTV.setText("");
        baseActivity.ivLogo.setVisibility(View.VISIBLE);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        // binding.rvBooking.setLayoutManager(mLayoutManager);

        binding.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    //adapterAllBookings.filter(newText.toString());

                } else {


                }
                return false;
            }
        });
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getentries();
            //    getBookings();
            getEarning();
            getSlider();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), LENGTH_SHORT).show();

        }


        mContext = getActivity();


        paramsFile = new HashMap<>();

        getArtist("0");


        baseActivity.swOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (artistDetailsDTO != null) {

                        paramsUpdate = new HashMap<>();
                        paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                        if (artistDetailsDTO.getCab_booking_flag().equalsIgnoreCase("1")) {
                            Toast.makeText(getActivity(), "Your booking is running", Toast.LENGTH_SHORT).show();

                            baseActivity.swOnOff.setChecked(true);
                            return;
                        } else {

                            if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                                paramsUpdate.put(Consts.IS_ONLINE, "0");
                                isOnline("0");
                            } else {
                                Log.e("SHIVAMONLINE", "-1");
                                paramsUpdate.put(Consts.IS_ONLINE, "1");
                                check_rider_leave();
//                                isOnline("1");
                            }
                        }


                    } else {
                        baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
                        binding.txtoffonline.setText("You're Offline");
                        baseActivity.swOnOff.setChecked(false);
                        baseActivity.tvOnOff.setTextColor(Color.WHITE);
                        binding.layfindtrip.setVisibility(View.GONE);
                        binding.todaysStatusLinear.setVisibility(View.GONE);

                        binding.txtoffonline.setTextColor(Color.BLACK);
                        binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));
                        Toast.makeText(getActivity(), getResources().getString(R.string.incomplete_profile_msg), Toast.LENGTH_SHORT).show();


                    }

                } else {


                    if (baseActivity.swOnOff.isChecked()) {
                        baseActivity.swOnOff.setChecked(false);
                    } else {
                        baseActivity.swOnOff.setChecked(true);
                    }
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

                }

            }

        });


        binding.llDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().stopService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                accept = true;
                decline(bookingid);
            }
        });

        binding.llAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    //    binding.mapView.setVisibility(View.GONE);
                    //    binding.rvBooking.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().stopService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                booking("1");
            }
        });
        return binding.getRoot();
    }

    public void check_rider_leave() {
        Log.e("SHIVAMONLINE", "RAM");
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.checkRiderLeave(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        String s = responseBody.string();
                        Log.e("online-offline res ", "4" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        if (sstatus == 1) {
                            isOnline("1");
                        } else {
                            baseActivity.swOnOff.setChecked(false);
                            Toast.makeText(getActivity(), message,
                                    LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again 4",
                        LENGTH_LONG).show();
            }
        });

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

                    /*    orderTargetList = new Gson().fromJson(response.getJSONArray("today_order_target").toString(), getpetDTO);
                        weeklyOnlineList = new Gson().fromJson(response.getJSONArray("weekly_online_hrs_target").toString(), getpetDTO1);
                        monthlylyOnlineList = new Gson().fromJson(response.getJSONArray("monthly_online_hrs_target").toString(), getpetDTO2);

                    */
                        binding.endhours.setText(total_hour_per_day + " Hours");

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
                        binding.hrsProgressbar.setProgress(Integer.parseInt(rider_online_percentage));

                        binding.todayEligibleAmountDigit.setText(Html.fromHtml("&#8377;" + total_eligible_amount));
                        binding.progressEligibleTxt.setText(today_booking_msg);

                        binding.totalCancelBooking.setText(rider_total_cancel);
                        binding.todaysKm.setText(rider_today_km);
                        binding.timeAm.setText(rider_start_time);
                        binding.timePm.setText(rider_end_time);

                      /*  setFirstChart();
                        setSecondChart();
                        setMonthlyChart();
                        setupData();
*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void booking(String req) {


        progressDialog.show();
        //   progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.booking_operation(bookingid, req, userDTO.getUser_id(), "", "", "");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        //Log.e("ERROR_RES",""+s);
                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            binding.layaccpet.setVisibility(View.GONE);

                            accept = true;
                            //    getBookingscopy("0");

                        } else {

                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                        LENGTH_LONG).show();


            }
        });


    }


    public void getArtistcpoy() {

        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getArtistByid(userDTO.getUser_id(), userDTO.getUser_id(), devicetoken);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                        LENGTH_LONG).show();

            }
        });

    }

    public void getImageClick() {
        //  Toast.makeText(baseActivity, "3", LENGTH_SHORT).show();

        binding.homescreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cat_image_link.equalsIgnoreCase("0")) {

                    getFragment(cat_image_link);
                } else {
                    //  addServices();
                }
            }
        });

        binding.homescreenImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!yt_image_link.equalsIgnoreCase("0")) {

                    getFragment(yt_image_link);
                } else {

                    baseActivity.navItemIndex = 32;
                    baseActivity.loadHomeFragment(new TVideoFragment(), "20");

                    // startActivity(new Intent(getActivity(), YoutubePlaylistActivity.class));
                }
            }
        });

        binding.homescreenImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!share_profile_link.equalsIgnoreCase("0")) {

                    getFragment(share_profile_link);
                } else {
                    baseActivity.navItemIndex = 33;
                    baseActivity.loadHomeFragment(new PromotionFragment(), "90");
                }
            }
        });

        binding.homescreenImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!refer_img_link.equalsIgnoreCase("0")) {

                    getFragment(refer_img_link);
                } else {
                    baseActivity.navItemIndex = 34;
                    baseActivity.loadHomeFragment(new AddReferFragment(), "notification");
                }
            }
        });
        //   Toast.makeText(baseActivity, "know more", LENGTH_SHORT).show();
        binding.knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 38;
                baseActivity.loadHomeFragment(new SafetyFragment(), "200");
            }
        });
    }

    public void getFragment(String value) {
        switch (value) {

            case "1":
                baseActivity.navItemIndex = 23;
                baseActivity.loadHomeFragment(new MainserviceFragment(), "profile");
                break;
            case "2":
                baseActivity.navItemIndex = 24;
                baseActivity.loadHomeFragment(new TVideoFragment(), "tvideo");
                break;
            case "3":
                baseActivity.navItemIndex = 25;
                baseActivity.loadHomeFragment(new AddReferFragment(), "notification");
                break;
            case "4":
                baseActivity.navItemIndex = 26;
                baseActivity.loadHomeFragment(new MyEarning(), "earn");
                break;
            case "5":
                baseActivity.navItemIndex = 27;
                baseActivity.loadHomeFragment(new PromotionFragment(), "promotion");
                break;
            case "6":
                baseActivity.navItemIndex = 28;
                baseActivity.loadHomeFragment(new ContactUs(), "history");
                break;
            case "7":
                baseActivity.navItemIndex = 29;
                baseActivity.loadHomeFragment(new NewBookings(), "jobs");
                break;
            case "8":
                baseActivity.navItemIndex = 30;
                baseActivity.loadHomeFragment(new Wallet(), "wallet");
                break;
            case "9":
                baseActivity.navItemIndex = 31;
                baseActivity.loadHomeFragment(new PaidFrag(), "history");
                break;

        }
    }


    public void getArtist(String value) {

        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getArtistByid(userDTO.getUser_id(), userDTO.getUser_id(), devicetoken);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);


                                if (value.equalsIgnoreCase("0")) {
                                    showDatfirst();

                                } else {
                                    showDataddd();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                        LENGTH_LONG).show();


            }
        });


    }

    public void isOnline(String onoff) {

        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }
        Log.e("SHIVAMONLINE", "0");
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.onlineOffline(userDTO.getUser_id(), onoff, devicetoken, "", "", "");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            getArtist("1");


                        } else {


                            baseActivity.swOnOff.setChecked(false);
                            //  Toast.makeText(getActivity(), message,LENGTH_LONG).show();
                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                        LENGTH_LONG).show();


            }
        });


    }


    public void getBookings() {

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCab(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("Rider_res", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistBookingsList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);


                                Collections.reverse(artistBookingsList);
                                //          binding.mapView.setVisibility(View.VISIBLE);
                                showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                        LENGTH_LONG).show();


            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void getEarning() {

        //  Toast.makeText(baseActivity, "-1", LENGTH_SHORT).show();

        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_TODAYS_EARNING, parmsearning, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("earning", "" + response.toString());

                progressDialog.dismiss();
                if (flag) {
                    Log.e("Vider", "2");

                    try {
                        Log.e("earning", "" + response.toString());

                        String total_booking = response.getString("total_booking");
                        String total_earning = response.getString("total_earning");

                        binding.totalBooking.setText(total_booking);

                        double tot_earn = Double.parseDouble(total_earning);
                        binding.todaysEarn.setText(String.valueOf(new DecimalFormat("##.##").format(tot_earn)));
                        //   getSlider();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Vider", "4");
                }
            }
        });
    }


    public void gettvideo() {

        //     Toast.makeText(baseActivity, "2", LENGTH_SHORT).show();

        Log.e("Vider", "1");// ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_RIDER_HOMEPAGE_DATA, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.e("SliderRES", "" + response.toString());

                if (flag) {
                    Log.e("Vider", "2");

                    try {
                        Log.e("Vider", "3");

                        Log.e("SliderRES", "" + response.toString());
                        homePageArrayList = new ArrayList<>();
                        tvideoModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<SiderModel>>() {
                        }.getType();

                        tvideoModelArrayList = (ArrayList<SiderModel>) new Gson().fromJson(response.getJSONObject("data").getJSONArray("catelog").toString(), getpetDTO);
                        binding.pagerhome.setAdapter(new SliderAdapter(getActivity(), tvideoModelArrayList));

                        HomePageModel homePageModel = new HomePageModel();
                        JSONObject jsonObject = response.getJSONObject("data");
                        homePageModel.setCatelogMsg(jsonObject.getString("catelog_msg"));
                        homePageModel.setYoutubeimage(jsonObject.getString("yt_image"));
                        homePageModel.setRefer_img(jsonObject.getString("refer_img"));
                        homePageModel.setCat_image(jsonObject.getString("cat_image"));
                        homePageModel.setShare_profile(jsonObject.getString("share_profile"));
                        homePageModel.setTotalCat(jsonObject.getString("total_cat"));
                        homePageModel.setTotalItem(jsonObject.getString("total_item"));
                        homePageModel.setTotalVisitProfile(jsonObject.getString("total_visit_profile"));
                        homePageModel.setItemVisit(jsonObject.getString("item_visit"));

                        offline_msg = jsonObject.getString("catelog_msg_off");
                        online_msg = jsonObject.getString("catelog_msg_on");

                        yt_image_link = jsonObject.getString("yt_image_link");
                        share_profile_link = jsonObject.getString("share_profile_link");
                        refer_img_link = jsonObject.getString("refer_img_link");
                        cat_image_link = jsonObject.getString("cat_image_link");

                        binding.partnerTxt.setText(homePageModel.getCatelogMsg());
                        //   binding.totalCat.setText(homePageModel.getTotalCat());
                        //    binding.totalItems.setText(homePageModel.getTotalItem());
                        //   binding.totalVisitorProfile.setText(homePageModel.getTotalVisitProfile());
                        //   binding.totalItemVisit.setText(homePageModel.getItemVisit());


                        Glide.with(getActivity()).load(homePageModel.getCat_image()).into(binding.homescreenImage);
                        Log.e("SliderRES", "" + homePageModel.getCat_image());
                        Glide.with(getActivity()).load(homePageModel.getYoutubeimage()).into(binding.homescreenImage2);
                        Glide.with(getActivity()).load(homePageModel.getShare_profile()).into(binding.homescreenImage3);
                        Glide.with(getActivity()).load(homePageModel.getRefer_img()).into(binding.homescreenImage5);

                        Log.e("tvideoModelArrayList", "" + tvideoModelArrayList.toString());

                        // tvideoModelArrayList = homePageArrayList.get(0).getCatelog();

                        getImageClick();

                        binding.dots.attachViewPager(binding.pagerhome);

                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("Vider", "4");

                }
            }
        });


    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.pagerhome.getCurrentItem() < tvideoModelArrayList.size() - 1) {
                            binding.pagerhome.setCurrentItem(binding.pagerhome.getCurrentItem() + 1);
                        } else {
                            binding.pagerhome.setCurrentItem(0);
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }

    public void getSlider() {

        Log.e("Vider", "0");

        gettvideo();
        Log.e("Vider", "5");
    }

    public void showData() {


        if (artistBookingsList.size() == 0) {


            binding.rlSearch.setVisibility(View.GONE);
            binding.lay.setVisibility(View.VISIBLE);

            if (artistDetailsDTO != null) {
                if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                } else {
                }
            }
        } else {
            for (int i = 0; i < artistBookingsList.size(); i++) {

                if (artistBookingsList.get(0).getRider_booking_flag().equalsIgnoreCase("0")) {
                    binding.layaccpet.setVisibility(View.VISIBLE);
                    binding.lay.setVisibility(View.GONE);
                    try {
                        bookingid = artistBookingsList.get(0).getId();
                        int seocndserver = Integer.parseInt(artistBookingsList.get(0).getSecond());
                        if (seocndserver < 45) {
                            seocndserver = 47 - seocndserver;
                            long ssss = seocndserver * 1000;


                            cT = new CountDownTimer(ssss, 1000) {

                                public void onTick(long millisUntilFinished) {

                                    int va = (int) ((millisUntilFinished % 60000) / 1000);

                                    binding.txtarivaltimer.setText("" + String.format("%02d", va));

                                }

                                public void onFinish() {


                                }
                            };
                            cT.start();

                            if (artistBookingsList.get(0).getPay_type().equalsIgnoreCase("0")) {
                                binding.txtptype.setText("Online Payment");

                            } else if (artistBookingsList.get(0).getPay_type().equalsIgnoreCase("1")) {
                                binding.txtptype.setText("Cash");

                            } else if (artistBookingsList.get(0).getPay_type().equalsIgnoreCase("2")) {
                                binding.txtptype.setText("Wallet Payment");
                            }
                            binding.priceDigit.setText(Html.fromHtml("&#8377;" + artistBookingsList.get(0).getPrice()));

                            if (checkarss(artistBookingsList.get(0).getProduct().get(0).getCategory_id())) {
                                dcatid = artistBookingsList.get(0).getProduct().get(0).getCategory_id();
                                dsubcatid = artistBookingsList.get(0).getProduct().get(0).getSub_category_id();
                                dthirdcatid = artistBookingsList.get(0).getProduct().get(0).getSublevel_category();
                                dclatititute = artistBookingsList.get(0).getC_latitude();
                                dclongitiute = artistBookingsList.get(0).getC_longitude();

                                binding.tvLocation.setText(artistBookingsList.get(0).getAddress());

                                if (dsubcatid.equalsIgnoreCase("453") || dsubcatid.equalsIgnoreCase("242") || dsubcatid.equalsIgnoreCase("455")) {
                                    binding.tvroundtrip.setVisibility(View.GONE);
                                } else {

                                    if (artistBookingsList.get(0).getProduct().get(0).getRoundtrip().equalsIgnoreCase("1")) {
                                        binding.tvroundtrip.setVisibility(View.VISIBLE);
                                        binding.tvroundtrip.setText("Round Trip");
                                    } else if (artistBookingsList.get(0).getProduct().get(0).getRoundtrip().equalsIgnoreCase("0")) {
                                        binding.tvroundtrip.setVisibility(View.VISIBLE);
                                        binding.tvroundtrip.setText("One Way");
                                    } else {
                                        binding.tvroundtrip.setVisibility(View.GONE);
                                    }

                                }


                            } else {

                                dcatid = artistBookingsList.get(0).getProduct().get(0).getCategory_id();
                                dsubcatid = artistBookingsList.get(0).getProduct().get(0).getSub_category_id();
                                dthirdcatid = artistBookingsList.get(0).getProduct().get(0).getSublevel_category();
                                dclatititute = artistBookingsList.get(0).getC_latitude();
                                dclongitiute = artistBookingsList.get(0).getC_longitude();
                                String locationstatus = artistBookingsList.get(0).getLocation_status();
                                if (locationstatus.equalsIgnoreCase("1")) {
                                    binding.tvroundtrip.setVisibility(View.VISIBLE);
                                    binding.tvroundtrip.setText("Delivery");
                                } else if (locationstatus.equalsIgnoreCase("0")) {
                                    binding.tvroundtrip.setVisibility(View.VISIBLE);
                                    binding.tvroundtrip.setText("Pick Up");
                                } else {
                                    binding.tvroundtrip.setVisibility(View.GONE);
                                }
                                binding.tvLocation.setText(artistBookingsList.get(0).getAddress());
                            }

                            binding.txtcat.setText(artistBookingsList.get(0).getCategory_name());

                        } else {


                        }

                    } catch (Exception e) {

                    }

                } else {

                }
            }

            binding.lay.setVisibility(View.GONE);

        }


    }

    public boolean checkarss(String catid) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;

            } else {
                value = false;

            }

        }
        return value;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void decline(String bid) {

        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, bid);
        paramsDecline.put(Consts.DECLINE_BY, "1");
        paramsDecline.put(Consts.DECLINE_REASON, "");
        paramsDecline.put("cat_id", dcatid);
        paramsDecline.put("sub_id", dsubcatid);
        paramsDecline.put("third_id", dthirdcatid);
        paramsDecline.put("lat", dclatititute);
        paramsDecline.put("lang", dclongitiute);
        paramsDecline.put("passvalue", "0");

        progressDialog.show();

        new HttpsRequest(Consts.DECLINE_BOOKING_API, paramsDecline, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {

                    //    binding.rvBooking.setVisibility(View.GONE);


                    try {
                        MediaPlayer mediaPlayer = apiClient.getMediaPlayer(getActivity());
                        mediaPlayer.reset();
                        AssetFileDescriptor descriptor = getActivity().getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();


                        binding.layaccpet.setVisibility(View.GONE);


                        //     getBookingscopy("3");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }


            }
        });
    }

    public void showDatfirst() {

        if (artistDetailsDTO != null) {
            //   baseActivity.swOnOff.setVisibility(View.VISIBLE);
            //   baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
            //          binding.sliderLayout.setVisibility(View.VISIBLE);
            //          binding.relativeTwo.setVisibility(View.VISIBLE);
            //           binding.thirdRelative.setVisibility(View.VISIBLE);
//            binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //          binding.fifthLinear.setVisibility(View.VISIBLE);
//            binding.sixthLinear.setVisibility(View.VISIBLE);
            //           binding.seventhLinear.setVisibility(View.VISIBLE);
            //          binding.eightLinear.setVisibility(View.VISIBLE);

            //   binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_online));
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            //  binding.layfindtrip.setVisibility(View.VISIBLE);
            binding.todaysStatusLinear.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setPadding(0, 0, 0, 0);

            //      binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));
            // binding.pulsator.start();
            //    binding.pulsator.setVisibility(View.VISIBLE);

            //baseActivity.swOnOff.colo(getResources().getColor(R.color.white));


        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            binding.lay.setVisibility(View.VISIBLE);
            //       binding.sliderLayout.setVisibility(View.VISIBLE);
            //           binding.relativeTwo.setVisibility(View.VISIBLE);
            //           binding.thirdRelative.setVisibility(View.VISIBLE);
            //    binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //          binding.fifthLinear.setVisibility(View.VISIBLE);
//            binding.sixthLinear.setVisibility(View.VISIBLE);
            //           binding.seventhLinear.setVisibility(View.VISIBLE);
//            binding.eightLinear.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            binding.layfindtrip.setVisibility(View.GONE);
            binding.todaysStatusLinear.setVisibility(View.GONE);
            binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);

            //        binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            // binding.pulsator.stop();
            //    binding.pulsator.setVisibility(View.GONE);

            // baseActivity.swOnOff.setHighlightColor(getResources().getColor(R.color.white));
        }

    }

    public void showDataddd() {

        if (artistDetailsDTO != null) {
            //     baseActivity.swOnOff.setVisibility(View.VISIBLE);
            //    baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
            //       binding.layfindtrip.setVisibility(View.VISIBLE);
            binding.todaysStatusLinear.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            binding.txtoffonline.setText("You're Online");
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            //       binding.layfindtrip.setVisibility(View.VISIBLE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));

            // binding.pulsator.start();
            //      binding.pulsator.setVisibility(View.VISIBLE);
            try {
                final MediaPlayer mediaPlayer = apiClient.getMediaPlayer(getActivity());
                mediaPlayer.reset();
                AssetFileDescriptor descriptor = getActivity().getAssets().openFd("online.mpeg");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                mediaPlayer.prepare();
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();

                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        apiClient.releaseMediaPlayer();
                        //mediaPlayer.stop();
                    }
                }.start();

            } catch (Exception e) {
                e.printStackTrace();
            }

            //baseActivity.swOnOff.colo(getResources().getColor(R.color.white));

        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            binding.lay.setVisibility(View.VISIBLE);
            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            binding.txtoffonline.setText("You're Offline");
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            binding.layfindtrip.setVisibility(View.GONE);
            binding.todaysStatusLinear.setVisibility(View.GONE);

            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            // binding.pulsator.stop();
            //    binding.pulsator.setVisibility(View.GONE);
            try {
                final MediaPlayer mediaPlayer = apiClient.getMediaPlayer(getActivity());
                mediaPlayer.reset();
                AssetFileDescriptor descriptor = getActivity().getAssets().openFd("offline.mpeg");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                mediaPlayer.prepare();
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();


            } catch (Exception e) {
                e.printStackTrace();
            }
            // baseActivity.swOnOff.setHighlightColor(getResources().getColor(R.color.white));
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter actionReceiver = new IntentFilter();
        actionReceiver.addAction("nameOfTheActionShouldBeUniqueName");
        bm.registerReceiver(onJsonReceived, actionReceiver);
    }


    private BroadcastReceiver onJsonReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                getBookings();


            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        bm.unregisterReceiver(onJsonReceived);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public void opencamrea() {


        ImagePicker.Companion.with(getActivity())
                .crop()
                .cameraOnly()
                .compress(768)
                .maxResultSize(768, 768)
                .start(23);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

            } else if (requestCode == 3) {
                Bitmap profilePicture = (Bitmap) data.getExtras().get("data");

                uri = getImageUri(getActivity(), profilePicture);
                String filepath = FileUtility.getPath(getActivity(), uri);
                FileUtility fileUtility = new FileUtility();
                filepath = fileUtility.compressImage(getActivity(), filepath);
                File file = new File(filepath);
                paramsFile.put(Consts.IMAGE, file);

            }
            if (requestCode == 23) {


                Uri imageUri = data.getData();

                String path = FileUtility.getPath(getActivity(), imageUri);
                File file = new File(path);
                paramsFile.put(Consts.IMAGE, file);

            }


        } else {

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void getBookingscopy(final String position) {
        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCab(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                //       binding.tvNo.setVisibility(View.GONE);
                //   binding.rvBooking.setVisibility(View.VISIBLE);
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistBookingsList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);


                                Collections.reverse(artistBookingsList);


                                getArtistcpoy();


                                showData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            artistBookingsList = new ArrayList<>();

                            getArtistcpoy();


                            showData();

                        }


                    } else {

                        //   binding.rvBooking.setVisibility(View.GONE);
                        binding.rlSearch.setVisibility(View.GONE);
                        binding.lay.setVisibility(View.VISIBLE);

                        Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                        LENGTH_LONG).show();


            }
        });


    }

    public void checkaddress() {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getMapCategory(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        String status = object.getString("status");
                        BaseActivity.addressModelArrayList = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonElements = object.getJSONArray("data");
                            for (int i = 0; i < jsonElements.length(); i++) {
                                JSONObject obj = jsonElements.getJSONObject(i);


                                String id = obj.getString("id");
                                String cat_id = obj.getString("cat_id");
                                String statuss = obj.getString("status");


                                BaseActivity.addressModelArrayList.add(new AddressModel(id, cat_id, statuss));

                            }
                        } else {

                        }
                    } else {
                        Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }


}
