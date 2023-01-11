package com.kamaii.rider.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;

import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.FloatingWidgetService;
import com.kamaii.rider.Glob;
import com.kamaii.rider.LocationService;
import com.kamaii.rider.MyDeviceWeakUpReceiver;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.preferences.TextSizeFix;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.fragment.AddReferFragment;
import com.kamaii.rider.ui.fragment.AddVehicleFragment;
import com.kamaii.rider.ui.fragment.AppUpdateFragment;
import com.kamaii.rider.ui.fragment.AppointmentFrag;
import com.kamaii.rider.ui.fragment.ArtistProfile;
import com.kamaii.rider.ui.fragment.CabBookingsFragment;
import com.kamaii.rider.ui.fragment.ChatList;
import com.kamaii.rider.ui.fragment.ContactUs;
import com.kamaii.rider.ui.fragment.CustomerBooking;
import com.kamaii.rider.ui.fragment.HomeScreenFragment;
import com.kamaii.rider.ui.fragment.IncentiveFragment;
import com.kamaii.rider.ui.fragment.JobsFrag;
import com.kamaii.rider.ui.fragment.MainserviceFragment;
import com.kamaii.rider.ui.fragment.MyEarning;
import com.kamaii.rider.ui.fragment.NewBookings;
import com.kamaii.rider.ui.fragment.Notification;
import com.kamaii.rider.ui.fragment.OrderDetailsFragment;
import com.kamaii.rider.ui.fragment.PaidFrag;
import com.kamaii.rider.ui.fragment.PaymentFragment;
import com.kamaii.rider.ui.fragment.ProfileSetting;
import com.kamaii.rider.ui.fragment.PromotionFragment;
import com.kamaii.rider.ui.fragment.ReferCustomerFragment;
import com.kamaii.rider.ui.fragment.StoredImagesFragment;
import com.kamaii.rider.ui.fragment.TVideoFragment;
import com.kamaii.rider.ui.fragment.Tickets;
import com.kamaii.rider.ui.fragment.Wallet;
import com.kamaii.rider.ui.models.AddressModel;
import com.kamaii.rider.ui.models.neworder.NewOrderModel;
import com.kamaii.rider.ui.models.neworder.PendingData;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.CustomTypeFaceSpan;
import com.kamaii.rider.utils.FontCache;
import com.kamaii.rider.utils.ProjectUtils;
import com.paykun.sdk.PaykunApiCall;
import com.paykun.sdk.eventbus.Events;
import com.paykun.sdk.helper.PaykunHelper;
import com.wooplr.spotlight.utils.SpotlightSequence;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static android.widget.Toast.LENGTH_LONG;
import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private String profile_step_title = "";
    private String profile_step_txt = "";
    public boolean active = false;
    public boolean offline = false;

    HashMap<String, String> responseHashMap;
    private ArrayList<HashMap<String, String>> offlineDataParms = new ArrayList<>();

    private InstallStateUpdatedListener installStateUpdatedListener;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(BaseActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }


    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 123;
    private String TAG = BaseActivity.class.getSimpleName();
    HashMap<String, String> parms = new HashMap<>();
    private FrameLayout frame;
    private View contentView;
    public NavigationView navigationView;
    public RelativeLayout header;
    public DrawerLayout drawer;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    public View navHeader;
    public ImageView menuLeftIV, ivSearch;
    Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    public static final String TAG_MAIN = "main";
    public static final String TAG_BOOKING = "booking";
    public static final String TAG_CHAT = "chat";
    public static final String TAG_PROFILE = "profile";
    public static final String TAG_PROMOTION = "promotion";
    public static final String TAG_VEHICLE = "vehicle";

    public static final String TAG_NOTIFICATION = "notification";
    public static final String TAG_DISCOUNT = "discount";
    public static final String TAG_HISTORY = "history";
    public static final String TAG_PROFILE_SETINGS = "profile_settings";
    public static final String TAG_TICKETS = "tickets";
    public static final String TAG_UPDATE = "update";
    public static final String TAG_TRIP = "trip";
    public static final String TAG_EARN = "earn";
    public static final String TAG_APPOINTMENT = "appointment";
    public static final String TAG_BOOKINGS_ALL = "jobs";
    public static final String TAG_BOOKINGS_cab = "cab";
    public static final String TAG_WALLET = "wallet";
    public static final String TAG_DOCUMENT = "document";
    public static final String TAG_TVIDEO = "tvideo";
    public static String CURRENT_TAG = TAG_MAIN;
    public static int navItemIndex = 0;
    public static String sversion = "";
    private Handler mHandler;
    private static final float END_SCALE = 0.8f;
    InputMethodManager inputManager;
    CustomerBooking customerBooking = new CustomerBooking();
    private boolean shouldLoadHomeFragOnBackPress = true;
    public CustomTextViewBold headerNameTV, tvOnOff;
    public ImageView ivLogo, ivEditPersonal;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private ImageView img_profile;
    private CustomTextViewBold tvName, dno, dyes;
    private CustomTextView tvEmail, tvOther, tvEnglish;
    public int location_check = 0;
    private LinearLayout llProfileClick;
    String type = "";
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> parmsApprove = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    public Switch swOnOff;
    public ImageView editImage;
    private HashMap<String, String> paramsUpdate = new HashMap<>();
    LinearLayout laychat, layudoc;
    private Dialog dialogfinish;
    private SharedPreferences firebase;
    protected PowerManager.WakeLock mWakeLock;
    private ArtistDetailsDTO artistDetailsDTO;
    String devicetoken = "";
    public static ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();
    private HashMap<String, String> parmsversion = new HashMap<>();
    private Dialog dailograting, dialogTearmsCondition;
    CustomTextViewBold tvupdateapp, gujaratibtn, englishbtn;
    ImageView img_sub_cat, update_dialog_close_img;
    int check_flag = 0;
    LocationService locationService;
    boolean logoutflag = false;
    public String offline_msg = "";
    Intent mServiceIntent;
    Context ctx;
    boolean payable_flag = false;
    int update_request_code = 0001;
    public boolean version_flag = false;
    //List<ResolveInfo> pkgAppsList;
    List<ApplicationInfo> pkgAppsList;
    List<String> pkgAppsStringList = new ArrayList<>();
    public Context getCtx() {
        return ctx;
    }

    MyDeviceWeakUpReceiver receiver;

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        offline = getIntent().getBooleanExtra("offline", false);

//        if (offline){
//
//            loadHomeFragment(new StoredImagesFragment(),"stored");
//        }
    }

    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextSizeFix.adjustFontScale(getResources().getConfiguration(), 1.0f, BaseActivity.this);
        setContentView(R.layout.activity_base);
        ctx = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        header = findViewById(R.id.header);
        menuLeftIV = findViewById(R.id.menuLeftIV);
        ivLogo = findViewById(R.id.ivLogo);
        ivEditPersonal = findViewById(R.id.ivEditPersonal);
        payable_flag = getIntent().getBooleanExtra("payable_flag", false);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        } else {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");
        this.mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        mContext = BaseActivity.this;
        mHandler = new Handler();
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        devicetoken = firebase.getString(Consts.DEVICE_TOKEN, "");
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parmsApprove.put(Consts.USER_ID, userDTO.getUser_id());

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
  //      pkgAppsList = getCtx().getPackageManager().queryIntentActivities( mainIntent, 0);

        locationService = new LocationService(getCtx());
        mServiceIntent = new Intent(getCtx(), locationService.getClass());

        if (!prefrence.getBooleanValue(Consts.TESTING_ACCOUNT)) {

            if (!isMyServiceRunning(locationService.getClass())) {
                startService(mServiceIntent);
            }
        }

        if (getIntent().hasExtra(Consts.SCREEN_TAG)) {
            type = getIntent().getStringExtra(Consts.SCREEN_TAG);
        }
        Glob.BUBBLE_VALUE = "0";
        askForSystemOverlayPermission();
        dialogfinish = new Dialog(BaseActivity.this, R.style.Theme_Dialog);
        dialogfinish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogfinish.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogfinish.setContentView(R.layout.dailog_approve);
        dialogfinish.setCancelable(false);

        laychat = dialogfinish.findViewById(R.id.laychat);
        layudoc = dialogfinish.findViewById(R.id.layudoc);
        dno = dialogfinish.findViewById(R.id.dno);
        dyes = dialogfinish.findViewById(R.id.dyes);
        dailograting = new Dialog(BaseActivity.this, R.style.Theme_Dialog);


        dailograting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailograting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailograting.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dailograting.setContentView(R.layout.dailog_update);
        dailograting.setCancelable(false);

        tvupdateapp = dailograting.findViewById(R.id.tvupdateapp);
        update_dialog_close_img = dailograting.findViewById(R.id.update_dialog_close_img);
        img_sub_cat = dailograting.findViewById(R.id.update_img_sub_cat);

        getVersion();
        setUpGClient();

        frame = (FrameLayout) findViewById(R.id.frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        contentView = findViewById(R.id.content);
        headerNameTV = findViewById(R.id.headerNameTV);
        tvOnOff = findViewById(R.id.tvOnOff);
        swOnOff = findViewById(R.id.swOnOff);
        editImage = findViewById(R.id.ivEditPersonal);
        menuLeftIV = (ImageView) findViewById(R.id.menuLeftIV);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);

        navHeader = navigationView.getHeaderView(0);
        img_profile = navHeader.findViewById(R.id.img_profile);
        tvName = navHeader.findViewById(R.id.tvName);
        tvEmail = navHeader.findViewById(R.id.tvEmail);
        tvEnglish = navHeader.findViewById(R.id.tvEnglish);
        tvOther = navHeader.findViewById(R.id.tvOther);
        llProfileClick = navHeader.findViewById(R.id.llProfileClick);

        tvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language("en");

            }
        });
        llProfileClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swOnOff.setVisibility(View.GONE);
                ivLogo.setVisibility(View.GONE);
                tvOnOff.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                navItemIndex = 5;
                CURRENT_TAG = TAG_PROFILE;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, new ArtistProfile());
                fragmentTransaction.commitAllowingStateLoss();
                drawer.closeDrawers();
            }
        });
        tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                language("ar");

            }
        });
        Glide.with(mContext).
                load(userDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_profile);
        tvEmail.setText(userDTO.getEmail_id());
        tvName.setText(userDTO.getName());

        if (payable_flag) {

            loadHomeFragment(new PaymentFragment(), "pay");
        } else {
            if (savedInstanceState == null) {
                // Toast.makeText(mContext, "hhhh", Toast.LENGTH_SHORT).show();
                if (type != null) {
                    if (type.equalsIgnoreCase(Consts.CHAT_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CHAT;
                        loadHomeFragment(new ChatList(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.TICKET_COMMENT_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_TICKETS;
                        loadHomeFragment(new Tickets(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.TICKET_STATUS_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_TICKETS;
                        loadHomeFragment(new Tickets(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.WALLET_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_WALLET;
                        loadHomeFragment(new Wallet(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_BOOKINGS_ALL;
                        loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.START_BOOKING_ARTIST_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_BOOKING;
                        loadHomeFragment(new CustomerBooking(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.BRODCAST_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        loadHomeFragment(new Notification(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.ADMIN_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        loadHomeFragment(new Notification(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.BOOK_ARTIST_NOTIFICATION)) {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_BOOKINGS_ALL;
                        loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.SEND_RIDER_NOTIFICATION)) {
                        //   Toast.makeText(mContext, "jgj hkjkh kjgjgk", Toast.LENGTH_SHORT).show();
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 33;
                        CURRENT_TAG = TAG_BOOKINGS_ALL;
                        loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.JOB_NOTIFICATION)) {

                        navItemIndex = 0;
                        CURRENT_TAG = TAG_BOOKINGS_ALL;
                        loadHomeFragment(new JobsFrag(), CURRENT_TAG);
                    } else if (type.equalsIgnoreCase(Consts.SEND_RESPONSE_NOTIFICATION)) {

                        Log.e("rider1234567890", " 222 " + "SEND_RESPONSE_NOTIFICATION");

                        //    navItemIndex = 10;
                        //   CURRENT_TAG = TAG_SERVER_RESPONSE;
                        // loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                        // sendServerResponse();

                    } else if (type.equalsIgnoreCase(Consts.DELETE_JOB_NOTIFICATION)) {

                        navItemIndex = 0;
                        CURRENT_TAG = TAG_BOOKINGS_ALL;
                        loadHomeFragment(new JobsFrag(), CURRENT_TAG);
                    } else {
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_BOOKINGS_cab;
                        loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                    }
                } else {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_BOOKINGS_cab;
                    loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                }


            }
        }


        menuLeftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerOpen();
            }
        });

        setUpNavigationView();
        Menu menu = navigationView.getMenu();

        changeColorItem(menu, R.id.nav_bookings_and_job);
        changeColorItem(menu, R.id.nav_earning_wallet);
        changeColorItem(menu, R.id.nav_personal);
        changeColorItem(menu, R.id.nav_other);
        changeColorItem(menu, R.id.nav_refer);
        changeColorItem(menu, R.id.nav_madeindia);
        changeColorItem(menu, R.id.nav_features);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyCustomFont(subMenuItem);
                }
            }
            applyCustomFont(mi);
        }


        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawerView, float slideOffset) {


                                         // Scale the View based on current slide offset
                                         final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                         final float offsetScale = 1 - diffScaledOffset;
                                         contentView.setScaleX(offsetScale);
                                         contentView.setScaleY(offsetScale);

                                         // Translate the View, accounting for the scaled width
                                         final float xOffset = drawerView.getWidth() * slideOffset;
                                         final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                                         final float xTranslation = xOffset - xOffsetDiff;
                                         contentView.setTranslationX(xTranslation);

                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {

                                     }

                                     @Override
                                     public void onDrawerOpened(View drawerView) {

                                     }
                                 }
        );
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        if (userDTO.getIs_profile() == 1) {
            if (userDTO.getApproval_status() == 0) {
                getApproveStatus();
            }
        }

        if (userDTO.getIs_profile() == 0) {

            if (NetworkManager.isConnectToInternet(mContext)) {
                getCategory();
            } else {
                //   ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }

        }


        getArtist();


    }

    public void sendServerResponse() {


        responseHashMap = new HashMap<>();
        responseHashMap.put(Consts.ARTIST_ID, userDTO.getUser_id());

        new HttpsRequest(Consts.SERVER_RESPONSE_API, responseHashMap, BaseActivity.this).stringPosttwo("TAG_SERVER_RESPONSE", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.e("SERVER_RESPONSE", " response " + response.toString());

            }
        });
    }

    public void getSpotLight(View view, String title, String txt, String id) {

        SpotlightSequence.getInstance(BaseActivity.this, null)
                .addSpotlight(view, title, txt, id)
                .startSequence();


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public void getVersion() {

        parmsversion.put(Consts.USER_ID, userDTO.getUser_id());

        Log.e(TAG, "getVersion: "+parmsversion.toString());

        new HttpsRequest(Consts.GET_VERSION, parmsversion, BaseActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Log.e("version_res", "" + response.toString());
                        int version = -1;
                        String update_image = "";
                        JSONObject jsonObjectmain = new JSONObject(String.valueOf(response));
                        String tracker = jsonObjectmain.getString("tracker");

                        JSONArray jsonArray = jsonObjectmain.getJSONArray("data");
                        String pname = getPackageName();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            String sversion = obj.getString("version_code");
                            String serverpname = obj.getString("package_name");
                            if (serverpname.equalsIgnoreCase(pname)) {
                                try {
                                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    version = pInfo.versionCode;
                                    update_image = obj.getString("update_image");
                                    Log.e("UPDATEIMAGE", "" + version + " sversion " + sversion);
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }

                                if (sversion.equalsIgnoreCase(String.valueOf(version))) {
                                } else {
                                    if (tracker.equalsIgnoreCase("1")) {
                                        version_flag = true;
                                        dailograting.show();
                                        String thumbnailMq = "http://img.youtube.com/vi/" + "iUJ4Omb8lro" + "/mqdefault.jpg";

                                        Glide.with(BaseActivity.this).load(update_image).placeholder(R.mipmap.ic_launcher).into(img_sub_cat);

                                        img_sub_cat.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/iUJ4Omb8lro"));
                                                try {
                                                    startActivity(webIntent);
                                                } catch (ActivityNotFoundException ex) {
                                                }


                                            }
                                        });

                                  /*  update_dialog_close_img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dailograting.dismiss();
                                        }
                                    });
*/
                                        tvupdateapp.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {


                                                final String appPackageName = getApplication().getPackageName();
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }


                                            }
                                        });

                                    }

                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }
            }
        });
    }

    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    public void getArtist() {

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
                                try {

                                    // ProjectUtils.showToast(getActivity(), msg);
                                    artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);

                                    if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                                        prefrence.setValue(Consts.IS_ONLINE, "1");
                                    } else {
                                        prefrence.setValue(Consts.IS_ONLINE, "0");

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                        }
                    } else {
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

    public void changeColorItem(Menu menu, int id) {
        MenuItem tools = menu.findItem(id);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);

    }

    public void applyCustomFont(MenuItem mi) {
        Typeface customFont = FontCache.getTypeface("Poppins-Regular.otf", BaseActivity.this);
        SpannableString spannableString = new SpannableString(mi.getTitle());
        spannableString.setSpan(new CustomTypeFaceSpan("", customFont), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(spannableString);
    }

    public void showImage() {
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        Glide.with(mContext).
                load(userDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_profile);
        tvName.setText(userDTO.getName());
    }

    public void loadHomeFragment(final Fragment fragment, final String TAG) {

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                swOnOff.setVisibility(View.GONE);
                ivLogo.setVisibility(View.GONE);
                tvOnOff.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    public void drawerOpen() {

        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                ivLogo.setVisibility(View.GONE);
                switch (menuItem.getItemId()) {
                    case R.id.nav_jobs:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MAIN;
                        fragmentTransaction.replace(R.id.frame, new JobsFrag());

                        break;
                    case R.id.nav_chat:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_CHAT;
                        fragmentTransaction.replace(R.id.frame, new ChatList());
                        break;

                    case R.id.nav_language:
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        startActivity(new Intent(BaseActivity.this, SelectLanguageActivity.class).putExtra("pathway", true).putExtra("lang", Locale.getDefault().getLanguage()));
                        break;
                    case R.id.nav_bookings:
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_BOOKINGS_ALL;
                        tvOnOff.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        fragmentTransaction.replace(R.id.frame, new OrderDetailsFragment());
                        break;
                    case R.id.nav_appointment:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_APPOINTMENT;
                        fragmentTransaction.replace(R.id.frame, new AppointmentFrag());
                        break;
                    case R.id.nav_notification:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new Notification());
                        break;

                    case R.id.nav_addrefer:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new AddReferFragment());
                        break;
                    case R.id.nav_viewerefer:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new ReferCustomerFragment());
                        break;

                    case R.id.nav_service:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_PROFILE;
                        fragmentTransaction.replace(R.id.frame, new MainserviceFragment());
                        break;
                    case R.id.nav_profile:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_PROFILE;
                        fragmentTransaction.replace(R.id.frame, new ArtistProfile());
                        break;

                    case R.id.nav_vahicle:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 99;
                        CURRENT_TAG = TAG_VEHICLE;
                        fragmentTransaction.replace(R.id.frame, new AddVehicleFragment());
                        break;

                    case R.id.nav_promotion:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 90;
                        CURRENT_TAG = TAG_PROMOTION;
                        fragmentTransaction.replace(R.id.frame, new PromotionFragment());
                        break;
                    case R.id.nav_earing:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_EARN;
                        fragmentTransaction.replace(R.id.frame, new MyEarning());
                        break;
                    case R.id.nav_incentive:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 1000;
                        CURRENT_TAG = TAG_EARN;
                        fragmentTransaction.replace(R.id.frame, new IncentiveFragment());
                        break;
                    case R.id.nav_history:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 11;
                        CURRENT_TAG = TAG_HISTORY;
                        fragmentTransaction.replace(R.id.frame, new PaidFrag());
                        break;
                    case R.id.nav_payment:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 1111;
                        CURRENT_TAG = TAG_HISTORY;
                        fragmentTransaction.replace(R.id.frame, new PaymentFragment());
                        break;
                    case R.id.nav_wallet:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 12;
                        CURRENT_TAG = TAG_WALLET;
                        fragmentTransaction.replace(R.id.frame, new Wallet());
                        break;
                    case R.id.nav_profilesetting:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        navItemIndex = 13;
                        CURRENT_TAG = TAG_PROFILE_SETINGS;
                        fragmentTransaction.replace(R.id.frame, new ProfileSetting());
                        break;
                    case R.id.nav_tickets:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        navItemIndex = 14;
                        CURRENT_TAG = TAG_TICKETS;
                        fragmentTransaction.replace(R.id.frame, new Tickets());
                        break;


                    case R.id.nav_update:
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);

                        navItemIndex = 15;
                        CURRENT_TAG = TAG_UPDATE;
                        fragmentTransaction.replace(R.id.frame, new AppUpdateFragment());
                        break;

                    case R.id.nav_contact:
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 25;
                        CURRENT_TAG = TAG_HISTORY;
                        fragmentTransaction.replace(R.id.frame, new ContactUs());
                        break;

                    case R.id.nav_madeindia:

                        break;

                    case R.id.nav_signout:
                        confirmLogout();
                        break;

                    case R.id.nav_home:
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        fragmentTransaction.replace(R.id.frame, new HomeScreenFragment());
                        //fragmentTransaction.replace(R.id.frame, new IncentiveFragment());
                        break;

                    case R.id.nav_cabbookings:
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);

                        navItemIndex = 18;
                        CURRENT_TAG = TAG_BOOKINGS_cab;
                        Log.e("SHIVAMDHAVAL", "AAYU");
                        fragmentTransaction.replace(R.id.frame, new CabBookingsFragment());
                        break;


                    case R.id.nav_tvideo:

                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 20;
                        CURRENT_TAG = TAG_TVIDEO;
                        fragmentTransaction.replace(R.id.frame, new TVideoFragment());
                        break;

                    case R.id.nav_offline_images:

                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 20;
                        CURRENT_TAG = TAG_TVIDEO;
                        fragmentTransaction.replace(R.id.frame, new StoredImagesFragment());
                        break;
                    default:
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_BOOKINGS_cab;
                        fragmentTransaction.replace(R.id.frame, new CabBookingsFragment());
                        break;

                }
                fragmentTransaction.commitAllowingStateLoss();
                drawer.closeDrawers();
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);


                return true;
            }
        });

    }

    public void confirmLogout() {
        try {
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.logout_msg))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            com.kamaii.rider.Glob.BUBBLE_VALUE = "1";


                            dialog.dismiss();
                            prefrence.clearAllPreferences();
                            logoutflag = true;

                            isOnline("0");
                            Intent intent = new Intent(BaseActivity.this, CheckSignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {

        if (!Settings.canDrawOverlays(this)) { // WHAT IF THIS EVALUATES TO FALSE.

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));

            startActivityForResult(intent, 125);
        } else {

        }
    }

    @Override
    public void onBackPressed() {

        Log.e("CAMERA", "on backpress");

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {

                navItemIndex = 0;
                CURRENT_TAG = TAG_MAIN;
                loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                return;
            }
        }
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.close_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        if (check_flag == 0) {

                            if (artistDetailsDTO.getCab_booking_flag().equalsIgnoreCase("1")) {

                                Intent intent = new Intent(BaseActivity.this, BasicInfoActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {

//                                if (prefrence.getValue(Consts.IS_ONLINE).equalsIgnoreCase("1")) {
//
//                                    isOnline("0");
//                                } else {
//                                    finish();
//                                }

                                finish();
                            }
                        } else {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_MAIN);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }

                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void clickProfile() {
        check_flag = 1;
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.incomplete_profile))
                .setMessage(getResources().getString(R.string.incomplete_profile_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetworkManager.isConnectToInternet(mContext)) {
                            Glob.BUBBLE_VALUE = "1";
//                            Intent intent = new Intent(mContext, EditPersnoalInfo.class);
                            Intent intent = new Intent(mContext, DocumentUploadTwoActivity.class);
                            intent.putExtra(Consts.CATEGORY_list, categoryDTOS);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_up, R.anim.stay);
                        } else {
                            // ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                            //  ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                        }

                        dialog.dismiss();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.skip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void getMyLocation() {

        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(1000);
                    locationRequest.setFastestInterval(1000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback() {

                        @Override
                        public void onResult(@NonNull Result result) {
                            Log.e("SHIVAMONLINE","2");
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(BaseActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);


                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        status.startResolutionForResult(BaseActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }


                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        String str = Build.BRAND;

                        if (str.equalsIgnoreCase("xiaomi")) {

                            if (!prefrence.getBooleanValue(Consts.EXTRA_PERMISSION_GRANT)) {

                                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                                intent.setClassName("com.miui.securitycenter",
                                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                                intent.putExtra("extra_pkgname", getPackageName());
                                startActivity(intent);
                                prefrence.setBooleanValue(Consts.EXTRA_PERMISSION_GRANT, true);
                            }
                        }
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }

        if (requestCode == 23 || requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    //Permission is not available. Display error text.


                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = (earthRadius * c);

        return dist; // output distance, in MILES
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(BaseActivity.this)
                .enableAutoManage(BaseActivity.this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public void updateLocation() {
        //  Toast.makeText(mContext, "update called", Toast.LENGTH_LONG).show();

        Log.e("PARAM", "" + parms.toString());
        //ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_LOCATION_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("PARAMS2", "" + response.toString());

                } else {
                    //   ProjectUtils.showToast(mContext, msg);
//                    Log.e("PARAMS3", "" + response.toString());

                }
            }
        });
    }

    public void language(String language) {
        String languageToLoad = language; // your language

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        BaseActivity.this.getResources().updateConfiguration(config,
                BaseActivity.this.getResources().getDisplayMetrics());

        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);


    }

    public void getCategory() {
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        clickProfile();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }
            }
        });
    }

    public void getApproveStatus() {
        new HttpsRequest(Consts.GET_APPROVAL_STATUS_API, parmsApprove, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        int approval_status = response.getInt("approval_status");
                        userDTO.setApproval_status(approval_status);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        if (approval_status == 0) {
                            approveDailog();
                        }
                        if (approval_status == 2) {
                            Intent browserIntent = new Intent(BaseActivity.this, DocumentUploadTwoActivity.class);
                            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(browserIntent);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }

    public void approveDailog() {

        dialogfinish.show();

        dno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfinish.dismiss();
            }
        });

        dyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfinish.dismiss();
            }
        });

        laychat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.link/c59fzm"));
                startActivity(browserIntent);
            }
        });

        layudoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ivSearch.setVisibility(View.GONE);
                navItemIndex = 0;
                CURRENT_TAG = TAG_DOCUMENT;

                dialogfinish.dismiss();
                // prefrence.setIntValue("tab",4);
               /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://multiscopesolution.com/app/Document"));
                startActivity(browserIntent);*/
//                loadHomeFragment(new DocumentUploadFragment(), TAG_DOCUMENT);
                Intent browserIntent = new Intent(BaseActivity.this, DocumentUploadTwoActivity.class);
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(browserIntent);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();


        if (CURRENT_TAG.equalsIgnoreCase(TAG_TVIDEO)) {

        } else {
            if (Glob.BUBBLE_VALUE.equalsIgnoreCase("0")) {

                if (prefrence.getValue(Consts.IS_ONLINE).equalsIgnoreCase("1")) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
                        startService(new Intent(BaseActivity.this, FloatingWidgetService.class).putExtra("activity_background", true));

                    }
                }

            } else {

            }
        }


    }

    public void isOnline(String onoff) {

        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }
        Log.e("SHIVAMONLINE","a");
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
                        offline_msg = object.getString("msg");
                        int sstatus = object.getInt("status");
                        if (sstatus == 1) {

                            if (!logoutflag) {

                                getArtist();
                                Intent i = new Intent();
                                i.setAction(Intent.ACTION_MAIN);
                                i.addCategory(Intent.CATEGORY_HOME);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();

                            } else {

                            }

                        } else {

                        }


                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
             /*   Toast.makeText(mContext, "Try again. Server is not responding",
                        LENGTH_LONG).show();*/

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        String str = Build.BRAND;
        Log.e("STR", " 3 " + str);

        if (str.equalsIgnoreCase("xiaomi")) {

            if (!prefrence.getBooleanValue(Consts.EXTRA_PERMISSION_GRANT)) {

                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", getPackageName());
                startActivity(intent);
                prefrence.setBooleanValue(Consts.EXTRA_PERMISSION_GRANT, true);
            }
        }
        if (CabBookingsFragment.online_flag) {
            swOnOff.setChecked(false);
        }
        Log.e("CAMERA", "on resume");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            stopService(new Intent(BaseActivity.this, FloatingWidgetService.class).putExtra("activity_background", true));

        } else {

        }


    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
        this.mWakeLock.release();

    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }
}
