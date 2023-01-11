package com.kamaii.rider.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.LocationService;
import com.kamaii.rider.databinding.FragmentCabBookingsBinding;
import com.kamaii.rider.interfacess.FindPlaceInterface;
import com.kamaii.rider.ui.activity.AddMoney;
import com.kamaii.rider.ui.activity.CheckSignInActivity;
import com.kamaii.rider.ui.adapter.AdapterNewCabBooking;
import com.kamaii.rider.ui.adapter.SliderAdapter;
import com.kamaii.rider.ui.models.ArtistBookingNew;
import com.kamaii.rider.ui.models.HomePageModel;
import com.kamaii.rider.ui.models.MonthlyOnlineModel;
import com.kamaii.rider.ui.models.OfflineReasons;
import com.kamaii.rider.ui.models.OrderTargetModel;
import com.kamaii.rider.ui.models.SiderModel;
import com.kamaii.rider.ui.models.WeeklyHoursModel;
import com.kamaii.rider.ui.models.neworder.AllBooking;
import com.kamaii.rider.ui.models.neworder.NewOrderModel;
import com.kamaii.rider.ui.models.neworder.PendingData;
import com.kamaii.rider.utils.CustomTextViewBold;
//import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.turf.TurfConversion;
import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.service.MyService;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AdapterCabBookings;
import com.kamaii.rider.ui.models.AddressModel;
import com.kamaii.rider.utils.FileUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import static androidx.core.app.ActivityCompat.requestPermissions;
//import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class CabBookingsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, FindPlaceInterface {

    private BaseActivity baseActivity;
    private FragmentCabBookingsBinding binding;
    private ArrayList<NewOrderModel> artistBookingsListnew = new ArrayList<>();
    private List<AllBooking> artistBookingsListnewcopy;
    private ArrayList<PendingData> artistBookingsList = new ArrayList<>();
    private ArrayList<PendingData> artistBookingsListcopy = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private String TAG = CabBookingsFragment.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> parmsartist = new HashMap<>();
    private LayoutInflater myInflater;
    private AdapterNewCabBooking adapterAllBookings;
    Context mContext;
    public static HashMap<String, File> paramsFile;
    public static Uri uri = null;
    private ArrayList<SiderModel> tvideoModelArrayList = new ArrayList<>();
    private ArrayList<HomePageModel> homePageArrayList = new ArrayList<>();
    private HashMap<String, String> parms = new HashMap<>();
    boolean map_flag = false;
    Location mylocation;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    private HashMap<String, String> paramsUpdate;
    private ArtistDetailsDTO artistDetailsDTO;
    private HashMap<String, String> paramsDecline;
    private HashMap<String, String> paramsPayment = new HashMap<>();
    private String bookingid = "";
    String currentdate = "";
    LocalBroadcastManager bm;
    private SharedPreferences firebase;
    String devicetoken = "";
    ProgressDialog progressDialog;
    String dcatid = "", dsubcatid = "", dthirdcatid = "", dclatititute = "", dclongitiute = "";
    CountDownTimer cT;
    boolean accept = false;
    private HashMap<String, String> parmsearning = new HashMap<>();

    String yt_image_link = "";
    String share_profile_link = "";
    String refer_img_link = "";
    String cat_image_link = "";
    String online_msg = "";
    String offline_msg = "";
    int radio_pos = -1;
    String switch_step_title = "";
    String switch_step_txt = "";
    String category_step_title = "";
    String category_step_txt = "";
    String service_step_title = "";
    String service_step_txt = "";
    String promotion_step_title = "";
    String promotion_step_txt = "";
    String tvideo_step_title = "";
    String tvideo_step_txt = "";
    String refer_step_title = "";
    String refer_step_txt = "";
    FusedLocationProviderClient fusedLocationProviderClient;
    double live_latitude = 0.0;
    double live_longitude = 0.0;
    String live_address_str = "";
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    int count_data = 0;
    String payment_flag = "";
    double payment_amt = 0;
    public Dialog paymentDialog500;
    public Dialog paymentDialog1000;
    PendingData pendingData;
    ImageView tvfinishcancel500;
    CustomTextViewBold paymentDialog500_btn, paymentDialog1000_btn, dialog_paytype500, tvtotalfinish500, dialog_paytype1000, tvtotalfinish1000;
    CustomTextViewBold offline_cancel, offline_confirm;
    ListView offlinedialog_msg;
    private String productName = "RiderPayment";
    private HashMap<String, String> parmas = new HashMap<>();

    HashMap<String, String> parmsGetWallet = new HashMap<>();
    private String currency = "";
    private String wallet_rate = "";
    String final_user_address = "";
    String landmark_name = "";
    public static String street_address = "";
    public static double user_latitude = 0.0;
    public static double user_longitude = 0.0;
    public static boolean online_flag = false;
    String online_pic_img = "";
    private HashMap<String, File> onlineImageFile;
    String check_rider_camera_flag = "";
    String show_button = "";
    Dialog offlinedialog;
    OfflineAdapter offlineAdapter;
    List<OfflineReasons> OfflineAdapterList;
    String offline_reason_id = "";
    Intent locationIntent;
    LocationService locationService;
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
    String getLatitude_from_backend = "";
    String getLongitude_from_backend = "";
    HashMap<String, String> incentiveMap = new HashMap<>();
    boolean refresh_booking_flag = false;
    MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cab_bookings, container, false);
        Log.e("SliderRES-0", " response ");
        prefrence = SharedPrefrence.getInstance(getActivity());
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        mediaPlayer = apiClient.getMediaPlayer(getActivity());
        paramsDecline = new HashMap<>();
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        devicetoken = firebase.getString(Consts.DEVICE_TOKEN, "");
        myInflater = LayoutInflater.from(getActivity());
        parmsartist.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.USER_ID, userDTO.getUser_id());
        parmas.put(Consts.USER_ID, userDTO.getUser_id());
        parmsearning.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.DEVICE_TOKEN, devicetoken);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        // Places.initialize(getApplicationContext(), API_KEY);
        incentiveMap.put(Consts.ARTIST_ID, userDTO.getUser_id());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationService = new LocationService();
        locationIntent = new Intent(getActivity(), LocationService.class);

        binding.stopRingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if (isMyServiceRunning(MyService.class)) {
                Log.e(TAG, "onClick: ring " + "ring 1");
                Intent intent = new Intent(getContext(), MyService.class);
                getContext().stopService(intent);

                if (mediaPlayer.isPlaying()) {

                    Log.e(TAG, "onClick: ring " + "ring 2");
                    apiClient.releaseMediaPlayer();
                }
                //}

            }
        });

        paymentDialog500 = new Dialog(getActivity(), R.style.Theme_Dialog);
        paymentDialog1000 = new Dialog(getActivity(), R.style.Theme_Dialog);

        paymentDialog500.requestWindowFeature(Window.FEATURE_NO_TITLE);
        paymentDialog500.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        paymentDialog500.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        paymentDialog500.setContentView(R.layout.payment_dialog_500);
        paymentDialog500.setCancelable(false);

        paymentDialog500_btn = paymentDialog500.findViewById(R.id.tvfinishsubmit500);
        dialog_paytype500 = paymentDialog500.findViewById(R.id.dialog_paytype500);
        tvtotalfinish500 = paymentDialog500.findViewById(R.id.tvtotalfinish500);
        tvfinishcancel500 = paymentDialog500.findViewById(R.id.tvfinishcancel500);

        offlinedialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        offlinedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        offlinedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        offlinedialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        offlinedialog.setContentView(R.layout.dialog_offline_confirm);
        offlinedialog.setCancelable(false);

        offlinedialog_msg = offlinedialog.findViewById(R.id.offline_list);
        offline_cancel = offlinedialog.findViewById(R.id.offline_cancel);
        offline_confirm = offlinedialog.findViewById(R.id.offline_confirm);

        OfflineAdapterList = new ArrayList<>();


        paymentDialog1000.requestWindowFeature(Window.FEATURE_NO_TITLE);
        paymentDialog1000.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        paymentDialog1000.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        paymentDialog1000.setContentView(R.layout.payment_dialog_1000);
        paymentDialog1000.setCancelable(false);
        paymentDialog1000_btn = paymentDialog1000.findViewById(R.id.tvfinishsubmit1000);
        dialog_paytype1000 = paymentDialog1000.findViewById(R.id.dialog_paytype1000);
        tvtotalfinish1000 = paymentDialog1000.findViewById(R.id.tvtotalfinish1000);

        //checkaddress();


        checkPermissions();

        if (NetworkManager.isConnectToInternet(getActivity())) {
            getPaymentStatus();
            getentries();

            getOfflineResons();
            getEarning();
            getSlider();

        } else {
            //      Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), LENGTH_SHORT).show();
        }
        //  binding.pulsator.start();


        Date todayyyy = new Date();
        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentdate = formatt.format(todayyyy);

        baseActivity.ivLogo.setVisibility(View.VISIBLE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);


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
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.rvBooking.setLayoutManager(mLayoutManager);

        binding.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    //  adapterAllBookings.filter(newText.toString());

                } else {


                }
                return false;
            }
        });


        mContext = getActivity();


        paramsFile = new HashMap<>();

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
                            radio_pos = -1;
                            if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                                Log.e("online", "1");


                                //offlinedialog_msg.setText("qqwdws");
                                offline_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        offlinedialog.dismiss();
                                        baseActivity.swOnOff.setChecked(true);
                                    }
                                });
                                offline_confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (offline_reason_id.equalsIgnoreCase("") || offline_reason_id.isEmpty()) {

                                            Toast.makeText(getActivity().getApplicationContext(), "Please select the offline reason", Toast.LENGTH_SHORT).show();
                                        } else {

                                            isOnline("0");
                                            offlinedialog.dismiss();
                                        }

                                    }
                                });
                                offlinedialog.show();
                            } else {
                                Log.e("online", "2");

                                online_flag = true;


                                /**/

                                if (check_rider_camera_flag.equalsIgnoreCase("1")) {

//                                    Toast.makeText(baseActivity, "sdsjdgsdgshgd", Toast.LENGTH_SHORT).show();
                                    check_rider_leave();

                                }



                                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                                startActivityForResult(intent, 23);
*/


                                /*Intent intent = new Intent(getActivity(), PhotographerActivity.class);
                                startActivity(intent);*/

                            }
                        }


                    } else {
                        baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
                        String version = "";
                        try {
                            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                            version = pInfo.versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        binding.txtoffonline.setText("You're Offline ( " + version + " )");
                        baseActivity.swOnOff.setChecked(false);
                        baseActivity.tvOnOff.setTextColor(Color.WHITE);
                        binding.layfindtrip.setVisibility(View.GONE);
                        binding.todaysStatusLinear.setVisibility(View.GONE);
                        //      binding.reachedbtnLinear.setVisibility(View.GONE);

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

                    binding.rvBooking.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().getApplicationContext().stopService(intent);
                    baseActivity.loadHomeFragment(new CabBookingsFragment(), "frag");
                 /*   Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());

//                    Log.e("lat_lang", "" + artistBookingsListcopy.get(0).getArt_lat() + " " + artistBookingsListcopy.get(0).getArt_long() + " time " + artistBookingsListcopy.get(0).getBooking_time());
                  //  Collections.reverse(artistBookingsListcopy);
                    try {
                        Log.e("lat_lang", "" + pendingData.getLat() + " " + pendingData.getLong() + " time " );
                        addresses = geocoder.getFromLocation(Double.parseDouble(pendingData.getLat()), Double.parseDouble(pendingData.getLong()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        //Toast.makeText(context, "Address :" + address, Toast.LENGTH_SHORT).show();

                        //    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + pendingData.getLat() + "," + pendingData.getLong());

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    booking("1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return binding.getRoot();
    }

    public void reloadFragment() {
        baseActivity.loadHomeFragment(new CabBookingsFragment(), "cab");
    }

    public void getentries() {

        new HttpsRequest(Consts.GET_INCENTIVE_DATA_API, incentiveMap, getActivity().getApplicationContext()).stringPost("", new Helper() {
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
                     /*   getLatitude_from_backend = response.getString("lat");
                        getLongitude_from_backend = response.getString("long");*/

                        binding.endhours.setText(total_hour_per_day + " Hours");

                        binding.totalEarningDigit.setText(total_earning);
                        binding.todaysIncentiveDig.setText(todays_incentive);
                        binding.totalBooking.setText(todays_order);
                        binding.todaysEarn.setText(todays_earning);
                        binding.onlinePymentDig.setText(online_payment);
                        binding.cashPaymentDig.setText(cash_payment);
                        binding.totalHrsDigit.setText(todays_login_hrs);

                        if (rider_total_cancel.equalsIgnoreCase("0") || rider_total_cancel.equalsIgnoreCase("1")) {
                            binding.totalCancelBookingHead.setText(getActivity().getApplicationContext().getResources().getString(R.string.incentive_order_cancel));
                        }
                        binding.totalCancelBooking.setText(rider_total_cancel);
                        binding.todaysKm.setText(rider_today_km);
                        binding.timeAm.setText(rider_start_time);
                        binding.timePm.setText(rider_end_time);
                        binding.hrsProgressbar.setProgress(Integer.parseInt(rider_online_percentage));
                        binding.progressEligibleTxt.setText(today_booking_msg);

                        binding.todayEligibleAmountDigit.setText(Html.fromHtml("&#8377;" + total_eligible_amount));

                        getArtist("0");

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

    private void getOfflineResons() {

        new HttpsRequest(Consts.GET_OFFLINE_REASON_API, getActivity()).stringGettwo("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {

                        Log.e("GET_OFFLINE_REASON_API", "" + response.toString());
                        JSONObject jsonObject = new JSONObject(response.toString());

                        OfflineAdapterList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<OfflineReasons>>() {
                        }.getType();

                        OfflineAdapterList = (ArrayList<OfflineReasons>) new Gson().fromJson(jsonObject.getJSONArray("reasons").toString(), getpetDTO);

                        offlineAdapter = new OfflineAdapter(getActivity(), OfflineAdapterList);
                        offlinedialog_msg.setAdapter(offlineAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    private void getPaymentStatus() {

        paramsPayment.put(Consts.USER_ID, userDTO.getUser_id());
        new HttpsRequest(Consts.PAYMENT_STATUS, paramsPayment, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    Log.e("version_res", "flag " + response.toString());

                    try {
                        payment_flag = response.getString("payment_status");
                        currency = response.getString("currency_type");
                        wallet_rate = response.getString("wallet_rate");

                        String amt = response.getString("amount");

                        Log.e("version_res", "flag " + payment_flag + "amount " + amt);

                        payment_amt = Double.parseDouble(amt);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (payment_flag.equalsIgnoreCase("0")) {
                        if (payment_amt >= 700 && payment_amt <= 999) {

                            dialog_paytype500.setText("Hello " + userDTO.getName());
                            tvtotalfinish500.setText(Html.fromHtml("&#x20B9;" + String.valueOf(payment_amt)));
                            paymentDialog500.show();

                            getBookings("1");
                            paymentDialog500_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (payment_amt > 0) {

                                        Intent in = new Intent(getActivity(), AddMoney.class);
                                        in.putExtra(Consts.AMOUNT, String.valueOf(payment_amt));
                                        in.putExtra(Consts.CURRENCY, currency);
                                        in.putExtra("wallet_rate", wallet_rate);
                                        in.putExtra("from_home", true);
                                        startActivity(in);
                                    }
                                }
                            });

                            tvfinishcancel500.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    paymentDialog500.dismiss();
                                }
                            });
                        } else if (payment_amt >= 1000) {

                            isOnline("0");
                            dialog_paytype1000.setText("Hello " + userDTO.getName());
                            tvtotalfinish1000.setText(Html.fromHtml("&#x20B9;" + String.valueOf(payment_amt)));
                           if (baseActivity.active){

                               paymentDialog1000.show();
                           }

                            paymentDialog1000_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (payment_amt > 0) {
                                        Intent in = new Intent(getActivity(), AddMoney.class);
                                        in.putExtra(Consts.AMOUNT, String.valueOf(payment_amt));
                                        in.putExtra(Consts.CURRENCY, currency);
                                        in.putExtra("wallet_rate", wallet_rate);
                                        in.putExtra("from_home", true);
                                        startActivity(in);
                                    }
                                }
                            });

                        }


                    } else {
                        Log.e("SHIVAMCHECKING", "1");
                        getBookings("2");

                    }
                }
            }
        });
    }

    public void booking(String req) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        //   progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);

        Log.e("Live_ADDRESS", " lat " + live_latitude);
        Log.e("Live_ADDRESS", " bookingid " + bookingid);
        Log.e("Live_ADDRESS", " req " + req);
        Log.e("Live_ADDRESS", " userDTO.getUser_id() " + userDTO.getUser_id());
        Log.e("Live_ADDRESS", " long " + live_longitude);
        Log.e("Live_ADDRESS", " address " + live_address_str);

        Call<ResponseBody> callone = api.booking_operation(bookingid, req, userDTO.getUser_id(), String.valueOf(live_latitude), String.valueOf(live_longitude), live_address_str);
        callone.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //       progressDialog.dismiss();
                Log.e("SliderRES-1", " response " + response.toString());

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("Live_ADDRESS", " response " + s);

                        Log.e("tracker123456", "1" + s);
                        JSONObject object = new JSONObject(s);

                        //Log.e("ERROR_RES",""+s);
                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            binding.layaccpet.setVisibility(View.GONE);
                            binding.stopRingBtn.setVisibility(View.VISIBLE);

                            accept = true;
                            getBookings("3");

                        } else {

                        }


                    } else {
                        Log.e("tracker123456", "1 else");

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

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again",
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

                        Log.e("tracker123456", "2" + s);

                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                                getEarning();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                        }


                    } else {
                        Log.e("tracker123456", "2 else");

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

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again",
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
                        Log.e("tracker123456", "3" + s);

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
                        Log.e("tracker123456", "3 else");

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

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again",
                        LENGTH_LONG).show();
            }
        });
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
                            ImagePicker.Companion.with(getActivity())
                                    .compress(768)
                                    .cameraOnly()    //User can only capture image using Camera
                                    .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                                    .start(23);

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

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again",
                        LENGTH_LONG).show();
            }
        });

    }

    private void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (paymentDialog1000 != null && paymentDialog1000.isShowing()) {
            paymentDialog1000.dismiss();
        }

    }

    @Override
    public void onDestroyView() {
        dismissProgressDialog();
        super.onDestroyView();
    }

    public void isOnline(String onoff) {

        //  if (getActivity().isActivityTransitionRunning()){

        if (baseActivity.active){

            progressDialog.show();
        }
        // }
        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }
        Log.e("SHIVAMONLINE", "4");
        Log.e("online-offline params", " uid " + userDTO.getUser_id() + " lat " + String.valueOf(live_latitude) + " longi " + String.valueOf(live_longitude));
        Log.e("online-offline params", " offline_reason_id:-- " + offline_reason_id);

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.onlineOffline(userDTO.getUser_id(), onoff, devicetoken, offline_reason_id, String.valueOf(live_latitude), String.valueOf(live_longitude));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("online-offline res ", "4" + s);

                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");


                        if (sstatus == 1) {

                            if (object.isNull("data")) {

                            } else {
                                JSONObject object1 = object.getJSONObject("data");

                                String latitude = object1.getString("lat");
                                String longitude = object1.getString("long");
                                Log.e("online-offline location", latitude + " longi:-- " + longitude);

                                if (latitude.equalsIgnoreCase("0") || longitude.equalsIgnoreCase("0")) {
                                    binding.reachedbtnLinear.setVisibility(View.GONE);

                                } else {

                                    mediaPlayer = apiClient.getMediaPlayer(getActivity());

                                    AssetFileDescriptor descriptor = null;
                                    try {
                                        mediaPlayer.reset();
                                        descriptor = getActivity().getApplicationContext().getAssets().openFd("online_offline.mp3");
                                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                                        descriptor.close();

                                        mediaPlayer.prepare();
                                        //    mediaPlayer.setVolume(1f, 1f);
                                        mediaPlayer.setLooping(false);
                                        mediaPlayer.start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                    binding.reachedbtnLinear.setVisibility(View.VISIBLE);
                                }
                            }

                            if (onoff.equalsIgnoreCase("1")) {
                                if (!isMyServiceRunning(locationService.getClass())) {
                                    if (!prefrence.getBooleanValue(Consts.TESTING_ACCOUNT)) {

                                        baseActivity.startService(locationIntent);
                                    }
                                }
                            } else {
                                baseActivity.stopService(locationIntent);
                            }
                            getArtist("1");
                        } else {
                            baseActivity.swOnOff.setChecked(false);
                            //  Toast.makeText(getActivity(), message,LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("online-offline error", response.errorBody().string());

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

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again",
                        LENGTH_LONG).show();


            }
        });


    }

    public void getBookings(String tracking) {
        Log.e("SliderRES-2", "getBookings-param");
        Log.e("SHIVAMCHECKING", "2");
        //    progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Log.e("SHIVAMCHECKING", "3");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        //Call<ResponseBody> callone = api.getAllBookingArtistCabtest(userDTO.getUser_id());
        Call<ResponseBody> callone = api.getAllBookingArtistCabtestrider(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //         progressDialog.dismiss();
                Log.e("SHIVAMCHECKING", "4");
                try {
                    if (response.isSuccessful()) {
                        Log.e("SHIVAMCHECKING", "5");
                        Log.e("SliderRES-3", "getBooking-response");
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e(TAG, "onResponse: strinnnnng "+s );
                        Log.e("tracker123456-error" + tracking, "6" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            Log.e("SHIVAMCHECKING", "6");
                            try {
                                Log.e("SHIVAMCHECKING", "7");
                                Log.e("rider_123", "1");
                                artistBookingsListnew = new ArrayList<>();
                                artistBookingsList = new ArrayList<>();

                                pendingData = new Gson().fromJson(object.getJSONObject("pending_data").toString(), PendingData.class);
                                Log.e("SHIVAMCHECKING", "8");
                                Log.e("rider_123", " artistBookingsList " + artistBookingsList.size());

                                if (pendingData.getTotalOrder().equalsIgnoreCase("0")) {
                                    Log.e("rider_123", "222");

                                    Type getpetDTO = new TypeToken<List<NewOrderModel>>() {
                                    }.getType();
                                    Log.e("SHIVAMCHECKING", "9");
                                    artistBookingsListnew = (ArrayList<NewOrderModel>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                    //                              Log.e("rider_123", " list "+artistBookingsListnew.get(0).getAllOrder());
                                    Log.e("SHIVAMCHECKING", "10");
                                    setMainAdapter();
                                    showDataOrders();
                                } else {
                                    Log.e("rider_123", "333" + artistBookingsList.size());
                                    artistBookingsListcopy = artistBookingsList;
                                    showData();
                                }
                                //    Collections.reverse(artistBookingsList);
                                //
                           /*     Log.e("rider_123", " full address " + artistBookingsListcopy.get(0).getFulladdress());
                                Log.e("rider_123", " desti address " + artistBookingsListcopy.get(0).getDestinationaddress());*/

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                            //    baseActivity.getVersion();
                            String showbutton_tracker = object.getString("showbutton_tracker");
                            String latitude = object.getString("lat");
                            String longitude = object.getString("long");

                            Log.e(TAG, "onResponse: latitude " + latitude + " longitude :-- " + longitude);
                            if (showbutton_tracker.equalsIgnoreCase("1")) {
                                //  binding.reachedbtnLinear.setVisibility(View.VISIBLE);

                                binding.gotoBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        binding.gotoBtn.setEnabled(false);
                                        binding.gotoBtn.setClickable(false);
                                        binding.reachedbtnLinear.setVisibility(View.GONE);
                                        Log.e(TAG, "onResponse: latitude 1234 " + latitude + " longitude :-- " + longitude);

                                        if (latitude.equalsIgnoreCase("0")) {

                                            Toast.makeText(baseActivity, "Please contact to administrator", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                            mapIntent.setPackage("com.google.android.apps.maps");
                                            startActivity(mapIntent);
                                        }

                                    }
                                });

                                binding.reachedBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        binding.reachedBtn.setEnabled(false);
                                        binding.reachedBtn.setClickable(false);
                                        binding.reachedbtnLinear.setVisibility(View.GONE);
                                        getReachedLocation();
                                    }
                                });
                            } else if (showbutton_tracker.equalsIgnoreCase("2")) {
                                binding.reachedbtnLinear.setVisibility(View.VISIBLE);
                                binding.reachedbtnLinear.setAlpha(0.3f);


                            } else {
                                // binding.reachedbtnLinear.setVisibility(View.GONE);
                            }

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
                Log.e(TAG, "onFailure: newtag ", t);
                Toast.makeText(getActivity(), "Server Did Not Responding and Try again",
                        LENGTH_LONG).show();


            }
        });


    }

    private void getReachedLocation() {

        Log.e("reached ", " getReachedLocation called");
        Log.e("reached params", " uid " + userDTO.getUser_id() + " lat " + String.valueOf(live_latitude) + " longi " + String.valueOf(live_longitude));
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.e("reached params", " baseurl:--" + retrofit.baseUrl());

        apiRest apiRest = retrofit.create(apiRest.class);
        Call<ResponseBody> call = apiRest.setReachedLocation(userDTO.getUser_id(), String.valueOf(live_latitude), String.valueOf(live_longitude));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {

                        ResponseBody responseBody = response.body();
                        String res = responseBody.string();
                        Log.e("reached", res);

                        JSONObject object = new JSONObject(res);
                        int status = object.getInt("status");
                        String message = object.getString("message");

                        if (status == 1) {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        } else if (status == 3) {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            prefrence.clearAllPreferences();
                            Intent intent = new Intent(mContext, CheckSignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Log.e("reached error", response.errorBody().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.toString());
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void getEarning() {

        //  Toast.makeText(baseActivity, "-1", LENGTH_SHORT).show();

        //      progressDialog.show();
        new HttpsRequest(Consts.GET_TODAYS_EARNING, parmsearning, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                // Log.e("earning", "" + response.toString());

                //             progressDialog.dismiss();
                if (flag) {
                    Log.e("Vider", "2");

                    try {
                        Log.e("earning", "" + response.toString());

                        String total_booking = response.getString("total_booking");
                        String total_earning = response.getString("total_earning");
                        check_rider_camera_flag = response.getString("check_rider");
                        show_button = response.getString("show_button");

                        Log.e("TOTAL_BOOKING123", " show_button " + show_button);


                        if (show_button.equalsIgnoreCase("1")) {
                            binding.reachedbtnLinear.setVisibility(View.VISIBLE);
                        } else {
                            binding.reachedbtnLinear.setVisibility(View.GONE);
                        }


                        if (!total_booking.equalsIgnoreCase("1") || total_booking.equalsIgnoreCase("0")) {
                            Log.e("TOTAL_BOOKING123", "if");

                            binding.totalBookingHead.setText(getActivity().getResources().getString(R.string.today_bookings));
                        }
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
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        Log.e(TAG, "gettvideo: " + parms.toString());
        Log.e("Vider", "1");// ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_RIDER_HOMEPAGE_DATA, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.e("SliderRES-4", "" + response.toString());

                if (flag) {
                    Log.e("Vider", "2");

                    try {
                        Log.e("Vider", "3");

                        String areaname = response.getString("area_name");
                        binding.areaTxt.setText("Delivery Area: " + areaname);
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

    private void getMyLocation() {

        Log.e("Live_ADDRESS", "getmylocation called");

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location != null) {
                    //Toast.makeText(mContext, "2", Toast.LENGTH_LONG).show();

                    live_latitude = location.getLatitude();
                    Log.e("Live_ADDRESS", "" + live_latitude);
                    live_longitude = location.getLongitude();
                    Log.e("Live_ADDRESS", "" + live_longitude);

                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                    try {
                        // Toast.makeText(mContext, "8", Toast.LENGTH_LONG).show();

                        List<Address> addresses = geocoder.getFromLocation(live_latitude, live_longitude, 1);
                        Address obj = addresses.get(0);
                        String add = obj.getAddressLine(0);
                        add = add + "\n" + obj.getCountryName();
                        add = add + "\n" + obj.getCountryCode();
                        add = add + "\n" + obj.getAdminArea();
                        add = add + "\n" + obj.getPostalCode();
                        add = add + "\n" + obj.getSubAdminArea();
                        add = add + "\n" + obj.getLocality();
                        add = add + "\n" + obj.getSubThoroughfare();

                        if (live_address_str.equals("")) {

                            live_address_str = obj.getAddressLine(0);
                        }
                        Log.e("Live_ADDRESS", "" + live_address_str);
                        //   Log.e("IGA123214", "Address" + add);
                        // Toast.makeText(this, "Address=>" + add,
                        // Toast.LENGTH_SHORT).show();

                        // TennisAppActivity.showDialog(add);

                        //  etLocationD.setText(obj.getAddressLine(0));
                        //  drag_location_etx.setText(obj.getAddressLine(0));
                        // parms.put(Consts.LOCATION, obj.getAddressLine(0));


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.e("onRequestPer", "onRequestPermissionsResult: result called");
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // adapterAllBookings.printBluetooth();
            }
        } else if (requestCode == 23) {

            Toast.makeText(baseActivity, "Allow Camera and access files and media permission", Toast.LENGTH_SHORT).show();
            if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

    }

    public void checkPermissions() {

        //   Log.e("Live_ADDRESS","check permission");

        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        checkPermissions();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (!map_flag) {


            mylocation = location;
            // Toast.makeText(mContext, "1", Toast.LENGTH_LONG).show();

            if (mylocation != null) {
                //Toast.makeText(mContext, "2", Toast.LENGTH_LONG).show();

                live_latitude = mylocation.getLatitude();
                //     Log.e("Live_ADDRESS", " 12 " + live_latitude);
                live_longitude = mylocation.getLongitude();
                //     Log.e("Live_ADDRESS", " 34 " + live_longitude);

                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                try {
                    // Toast.makeText(mContext, "8", Toast.LENGTH_LONG).show();

                    List<Address> addresses = geocoder.getFromLocation(live_latitude, live_longitude, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();

                    if (live_address_str.equals("")) {

                        //            Log.e("Live_ADDRESS", "" + live_address_str);

                        live_address_str = obj.getAddressLine(0);

                    }
                    //   Log.e("IGA123214", "Address" + add);
                    // Toast.makeText(this, "Address=>" + add,
                    // Toast.LENGTH_SHORT).show();

                    // TennisAppActivity.showDialog(add);

                    //  etLocationD.setText(obj.getAddressLine(0));
                    //  drag_location_etx.setText(obj.getAddressLine(0));
                    // parms.put(Consts.LOCATION, obj.getAddressLine(0));


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void getLocation() {

        findPlace();
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

    @SuppressLint("RestrictedApi")
    public void showData() {


        Log.e("rider_123", "3");

        if (pendingData.getTotalOrder().equalsIgnoreCase("0")) {
            Log.e("rider_123", "4");

            //     baseActivity.getVersion();
            Log.e("tracker", "1");

            if (artistBookingsListnew.size() == 0) {
                binding.rvBooking.setVisibility(View.GONE);
                binding.rlSearch.setVisibility(View.GONE);
                binding.lay.setVisibility(View.VISIBLE);

            } else {
                binding.rvBooking.setVisibility(View.VISIBLE);
                binding.rlSearch.setVisibility(View.GONE);
                binding.lay.setVisibility(View.VISIBLE);

            }

        } else {
            Log.e("tracker", "2");

            int seocndserver = Integer.parseInt(pendingData.getSeconds());
            Log.e("finalConvertedF", "abcd5" + " " + seocndserver);

            //    if (seocndserver < 45) {
            // seocndserver = 47 - seocndserver;
            long ssss = seocndserver * 1000;

            Log.e("finalConvertedF", "abcd2");

            cT = new CountDownTimer(ssss, 1000) {

                public void onTick(long millisUntilFinished) {

                    int va = (int) ((millisUntilFinished) / 1000);

                    binding.txtarivaltimer.setText("" + String.format("%02d", va));

                }

                public void onFinish() {

                    if (accept == false) {

                        try {
                            Intent intent = new Intent(getContext(), MyService.class);
                            getActivity().stopService(intent);
                            autodecline("");
                            //    ((BaseActivity) getActivity()).loadHomeFragment(new CabBookingsFragment(), "cab");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            cT.start();


            binding.layaccpet.setVisibility(View.VISIBLE);
            binding.stopRingBtn.setVisibility(View.GONE);

            binding.txtdistancetime.setText(pendingData.getTotalMin() + " Min");
            binding.totalorder.setText(pendingData.getTotalOrder());
            binding.txtpayment.setText(pendingData.getTotalAmount());

            //binding.rvBooking.setVisibility(View.VISIBLE);
            binding.pulsator.setVisibility(View.GONE);
            //
            binding.lay.setVisibility(View.GONE);


        }


    }

    public void showDataOrders() {


        Log.e("rider_123", "3");

        if (artistBookingsListnew.size() == 0) {
            Log.e("rider_123", "order");

            binding.rvBooking.setVisibility(View.GONE);
            binding.rlSearch.setVisibility(View.GONE);
            binding.lay.setVisibility(View.VISIBLE);

        } else {
            Log.e("rider_123", "order 123");

            binding.rvBooking.setVisibility(View.VISIBLE);
            binding.pulsator.setVisibility(View.GONE);
            binding.lay.setVisibility(View.GONE);
        }


    }

    public void setMainAdapter() {

        if (!baseActivity.version_flag) {
            Log.e("SHIVAMCHECKING", "11");

            // adapterAllBookings = new AdapterCabBookings(CabBookingsFragment.this, artistBookingsList, userDTO, myInflater, live_address_str, live_latitude, live_longitude, this);
            adapterAllBookings = new AdapterNewCabBooking(CabBookingsFragment.this, artistBookingsListnew, userDTO, myInflater, live_address_str, live_latitude, live_longitude, this, refresh_booking_flag, baseActivity);
            binding.rvBooking.setAdapter(adapterAllBookings);

        }
    }

    public void autodecline(String bid) {

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

        //   progressDialog.show();

        new HttpsRequest(Consts.DECLINE_BOOKING_API2, paramsDecline, getActivity()).stringPosttwo(TAG, new Helper() {
            @SuppressLint("RestrictedApi")
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  progressDialog.dismiss();
                if (flag) {

                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.scrollviewHomescreen.setVisibility(View.VISIBLE);


                    try {
                        mediaPlayer = apiClient.getMediaPlayer(getActivity());
                        mediaPlayer.reset();
                        AssetFileDescriptor descriptor = getActivity().getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        //   mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();


                        binding.layaccpet.setVisibility(View.GONE);
                        binding.stopRingBtn.setVisibility(View.VISIBLE);


                        getBookingscopy("3");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }


            }
        });
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
        paramsDecline.put(Consts.DECLINE_BY, "3");
        paramsDecline.put(Consts.DECLINE_REASON, "Busy");
        paramsDecline.put("cat_id", dcatid);
        paramsDecline.put("sub_id", dsubcatid);
        paramsDecline.put("third_id", dthirdcatid);
        paramsDecline.put("lat", dclatititute);
        paramsDecline.put("lang", dclongitiute);
        paramsDecline.put("passvalue", "0");

        //      progressDialog.show();

        new HttpsRequest(Consts.DECLINE_BOOKING_API, paramsDecline, getActivity()).stringPosttwo(TAG, new Helper() {
            @SuppressLint("RestrictedApi")
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //           progressDialog.dismiss();
                if (flag) {

                    binding.rvBooking.setVisibility(View.GONE);


                    try {
                        mediaPlayer = apiClient.getMediaPlayer(getActivity());
                        mediaPlayer.reset();
                        AssetFileDescriptor descriptor = getActivity().getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        //  mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();


                        binding.layaccpet.setVisibility(View.GONE);
                        binding.stopRingBtn.setVisibility(View.VISIBLE);


                        getBookingscopy("3");


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
            baseActivity.swOnOff.setVisibility(View.VISIBLE);
            baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
            //   binding.sliderLayout.setVisibility(View.VISIBLE);
            //   binding.relativeTwo.setVisibility(View.VISIBLE);
            //       binding.thirdRelative.setVisibility(View.VISIBLE);
//            binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //    binding.fifthLinear.setVisibility(View.VISIBLE);
            //   binding.sixthLinear.setVisibility(View.VISIBLE);
            //    binding.seventhLinear.setVisibility(View.VISIBLE);
            //    binding.eightLinear.setVisibility(View.VISIBLE);

            //   binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            String version = "";
            try {
                PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_online) + " ( " + version + " ) ");
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);

            if (!prefrence.getBooleanValue(Consts.TESTING_ACCOUNT)) {

                if (!isMyServiceRunning(locationService.getClass())) {
                    baseActivity.startService(locationIntent);
                }
            }
            //  binding.layfindtrip.setVisibility(View.VISIBLE);
            binding.todaysStatusLinear.setVisibility(View.VISIBLE);
            //    binding.reachedbtnLinear.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setPadding(0, 0, 0, 0);

            //      binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));
            // binding.pulsator.start();
            //    binding.pulsator.setVisibility(View.VISIBLE);

            //baseActivity.swOnOff.colo(getResources().getColor(R.color.white));


        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            binding.lay.setVisibility(View.VISIBLE);
            //      binding.sliderLayout.setVisibility(View.VISIBLE);
            //       binding.relativeTwo.setVisibility(View.VISIBLE);
            //    binding.thirdRelative.setVisibility(View.VISIBLE);
            //    binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //          binding.fifthLinear.setVisibility(View.VISIBLE);
            //    binding.sixthLinear.setVisibility(View.VISIBLE);
            //         binding.seventhLinear.setVisibility(View.VISIBLE);
            //         binding.eightLinear.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            String version = "";
            try {
                PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline) + " ( " + version + " )");
            baseActivity.swOnOff.setChecked(false);
            baseActivity.stopService(locationIntent);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            binding.layfindtrip.setVisibility(View.GONE);
            binding.todaysStatusLinear.setVisibility(View.GONE);
            //   binding.reachedbtnLinear.setVisibility(View.GONE);

            binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);

            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            binding.pulsator.setVisibility(View.GONE);

        }

    }

    public void findPlace() {


        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(getActivity().getApplicationContext());

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

    }

    public void showDataddd() {

        if (artistDetailsDTO != null) {
            baseActivity.swOnOff.setVisibility(View.VISIBLE);
            baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
            binding.todaysStatusLinear.setVisibility(View.VISIBLE);
            //      binding.reachedbtnLinear.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            String version = "";
            try {
                PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            binding.txtoffonline.setText("You're Online ( " + version + " )");
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));


            try {
                mediaPlayer = apiClient.getMediaPlayer(getActivity());
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
                        //mediaPlayer.stop();
                        apiClient.releaseMediaPlayer();
                    }
                }.start();

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            binding.lay.setVisibility(View.VISIBLE);
            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            String version = "";
            try {
                PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            binding.txtoffonline.setText("You're Offline ( " + version + " )");
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            binding.layfindtrip.setVisibility(View.GONE);
            binding.todaysStatusLinear.setVisibility(View.GONE);
            //      binding.reachedbtnLinear.setVisibility(View.GONE);

            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            binding.pulsator.setVisibility(View.GONE);
            try {
                mediaPlayer = apiClient.getMediaPlayer(getActivity());

                mediaPlayer.reset();
                AssetFileDescriptor descriptor = getActivity().getAssets().openFd("offline.mpeg");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                mediaPlayer.prepare();
                //  mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();

                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        apiClient.releaseMediaPlayer();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
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

                getBookings("4");

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

        Log.e("onRequestPe", "onRequestPermissionsResult: result called 10");

        ImagePicker.Companion.with(getActivity())
//                .crop()
                .crop()
                .cameraOnly()
                //.compress(768)
                .maxResultSize(620, 620)
                .start(23);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

                Place place = Autocomplete.getPlaceFromIntent(data);

                final_user_address = place.getAddress();
                landmark_name = place.getName();


                street_address = landmark_name + final_user_address;
                user_latitude = place.getLatLng().latitude;
                user_longitude = place.getLatLng().longitude;

                AdapterCabBookings.location_etx.setText(street_address);
                Log.e("FIND_ADDRESS_CALLED", " " + CabBookingsFragment.street_address + CabBookingsFragment.user_latitude + CabBookingsFragment.user_longitude);

            } else if (requestCode == 3) {
                Bitmap profilePicture = (Bitmap) data.getExtras().get("data");

                uri = getImageUri(getActivity(), profilePicture);
                String filepath = FileUtility.getPath(getActivity(), uri);
                FileUtility fileUtility = new FileUtility();
                filepath = fileUtility.compressImage(getActivity(), filepath);
                File file = new File(filepath);
                paramsFile.put(Consts.IMAGE, file);
                AdapterCabBookings.img_upload.setVisibility(View.VISIBLE);
                AdapterCabBookings.img_upload.setImageURI(uri);
                AdapterCabBookings.tvcamera.setEnabled(true);
                AdapterCabBookings.tvcamera.setClickable(true);
                AdapterCabBookings.btnclick_flag = true;
            }
            if (requestCode == 23) {
                Log.e("Image_response", " 1 ");


                if (online_flag) {

                    Log.e(TAG, "onActivityResult: tracker" + " onlin ma gayu 1");
                    Uri imageUri1 = data.getData();

                   /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Uri imageUri1 = getImageUri(getApplicationContext(),bitmap);
                    Log.e("Image_response"," 1 "+ imageUri1.toString());
*/

                    String path = FileUtility.getPath(getActivity(), imageUri1);
                    File file = new File(path);

                    onlineImageFile = new HashMap<>();
                    onlineImageFile.put("image", file);
                    // parmas.put("image",file.getAbsolutePath());
                    Log.e("Image_response", " onlineImageFile " + onlineImageFile.toString());

                    Log.e("Image_response", " onlineImageFile file " + file.getAbsolutePath());


                    new HttpsRequest(Consts.ONLINE_RIDER_IMAGE, parmas, onlineImageFile, getActivity()).imagePosttwo("Tag", new Helper() {
                        @Override
                        public void backResponse(boolean flag, String msg, JSONObject response) {
                            if (flag) {
                                Log.e("Image_response", response.toString());
                                paramsUpdate.put(Consts.IS_ONLINE, "1");
                                online_flag = false;
                                Log.e("SHIVAMONLINE", "-11");
//                                check_rider_leave();
                                isOnline("1");

                            }
                        }
                    });


                } else {
                    Log.e("Image_r", " 11 ");

                    Log.e(TAG, "onActivityResult: tracker" + " onlin ma nathi gayu 222222222");
                    Log.e(TAG, "onActivityResult: tracker" + " onlin ma nathi gayu 3");
                    Log.e(TAG, "onActivityResult: tracker" + " onlin ma nathi gayu 4");

                    Uri imageUri = data.getData();
                    final InputStream imageStream;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = encodeImage(selectedImage);
                        prefrence.setValue("pickup_img", encodedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String path = FileUtility.getPath(getActivity(), imageUri);
                    File file = new File(path);
                    paramsFile.put(Consts.IMAGE, file);
                    AdapterCabBookings.img_upload.setVisibility(View.VISIBLE);
                    AdapterCabBookings.img_upload.setImageURI(imageUri);
                    AdapterCabBookings.tvcamera.setEnabled(true);
                    AdapterCabBookings.tvcamera.setClickable(true);
                    AdapterCabBookings.btnclick_flag = true;
                    Log.e(TAG, "onActivityResult: base64" + prefrence.getValue("pickup_img"));

                }
            }

        } else {

        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void getBookingscopy(final String position) {
        //      progressDialog.show();

        Log.e("pickup_tracker", " getBookingscopy ");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCabtestrider(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //            progressDialog.dismiss();
                //       binding.tvNo.setVisibility(View.GONE);
                binding.rvBooking.setVisibility(View.VISIBLE);
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("tracker123456-normal", "6" + s);

                        Log.e("pickup_tracker", " getBookingscopy called " + s);

                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistBookingsListnew = new ArrayList<>();
                                artistBookingsListnewcopy = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<NewOrderModel>>() {
                                }.getType();

                                artistBookingsListnew = (ArrayList<NewOrderModel>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                artistBookingsListnewcopy = artistBookingsListnew.get(0).getAllBooking();
                                Log.e("pickup_tracker", " copy size " + artistBookingsListnew.size());

                                getArtistcpoy();
                                Log.e("pickup_tracker", " map " + artistBookingsListnewcopy.get(0).getKm());

                                Log.e("pickup_tracker", " stored value " + prefrence.getValue(Consts.MAP_TRACKER));

                              /*  if (prefrence.getValue(Consts.MAP_TRACKER).equalsIgnoreCase("1")) {

                                    Log.e("pickup_tracker", " o km ");
                                    Log.e("pickup_tracker", " lat "+artistBookingsListnewcopy.get(0).getUserLat()+" long "+artistBookingsListnewcopy.get(0).getUserLong());
                                    if (artistBookingsListnewcopy.get(0).getAppFlag().equalsIgnoreCase("1")) {
                                        Log.e("pickup_tracker", " pickup ");

                                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsListnewcopy.get(0).getUserLat() + "," + artistBookingsListnewcopy.get(0).getUserLong());
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        baseActivity.startActivity(mapIntent);

                                    } else if (artistBookingsListnewcopy.get(0).getAppFlag().equalsIgnoreCase("2")) {
                                        Log.e("pickup_tracker", " deliver nimesh");
                                        Log.e("pickup_tracker", " deliver lat "+artistBookingsListnewcopy.get(0).getUserLat()+" long "+artistBookingsListnewcopy.get(0).getUserLong());

                                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsListnewcopy.get(0).getUserLat() + "," + artistBookingsListnewcopy.get(0).getUserLong());
                                        Log.e("pickup_tracker", " deliver nimesh 1");

                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        Log.e("pickup_tracker", " deliver nimesh 2");

                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        Log.e("pickup_tracker", " deliver nimesh 3");

                                        baseActivity.startActivity(mapIntent);

                                        Log.e("pickup_tracker", " deliver nimesh 4");

                                    }

                                }*/
                                setMainAdapter();
                                showDataOrders();
                                //   showData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            //artistBookingsList = new ArrayList<>();

                            getArtistcpoy();
                            Log.e("pickup_tracker", " getBookingscopy called else");


                            //     showData();

                        }


                    } else {

                        binding.rvBooking.setVisibility(View.GONE);
                        binding.rlSearch.setVisibility(View.GONE);
                        binding.lay.setVisibility(View.VISIBLE);

                        Log.e("tracker123456", "6 else");

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

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again",
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
                        Toast.makeText(getActivity(), "Server Did Not Responding and Try again",
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

    class OfflineAdapter extends BaseAdapter {

        Context context;
        List<OfflineReasons> arrayList;


        public OfflineAdapter(Context context, List<OfflineReasons> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(context).inflate(R.layout.offline_dialog_recycler_layout, null);

            ImageView offline_recycle_radio;
            offline_recycle_radio = view.findViewById(R.id.offline_recycle_radio);
            CustomTextViewBold offline_recycle_msg;
            offline_recycle_msg = view.findViewById(R.id.offline_recycle_msg);

            offline_recycle_msg.setText(arrayList.get(position).getRider_msg());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radio_pos = position;
                    notifyDataSetChanged();
                }
            });

            if (position == radio_pos) {

                offline_recycle_radio.setImageResource(R.drawable.ic_radio_btn_selected);
                offline_reason_id = arrayList.get(position).getRider_msg();
            } else {
                offline_recycle_radio.setImageResource(R.drawable.ic_radio_btn_unselected);
                // offline_recycle_radio.setChecked(false);
            }
            return view;
        }
    }
}
