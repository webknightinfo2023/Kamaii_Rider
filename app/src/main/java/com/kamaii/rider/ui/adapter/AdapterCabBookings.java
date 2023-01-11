package com.kamaii.rider.ui.adapter;

import static android.widget.Toast.LENGTH_LONG;
import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DatabaseHandler;
import com.kamaii.rider.databinding.AdapterCabBookingsBinding;
import com.kamaii.rider.databinding.PaymentHandoverDialogBinding;
import com.kamaii.rider.interfacess.FindPlaceInterface;
import com.kamaii.rider.interfacess.OnReasonSelectedListener;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.printer.AsyncBluetoothEscPosPrint;
import com.kamaii.rider.printer.AsyncEscPosPrinter;
import com.kamaii.rider.ui.fragment.MyEarning;
import com.kamaii.rider.ui.models.CancelReasonModel;
import com.kamaii.rider.ui.models.ContactModel;
import com.kamaii.rider.ui.models.OfflineData;
import com.kamaii.rider.ui.models.OfflineDataArrayListModal;
import com.kamaii.rider.ui.models.OfflineImageDataModal;
import com.kamaii.rider.ui.models.ServiceNameModal;
import com.kamaii.rider.ui.models.ServiceQty;
import com.kamaii.rider.ui.models.neworder.AllBooking;
import com.kamaii.rider.ui.models.neworder.NewOrderModel;
import com.kamaii.rider.ui.models.neworder.Product;
import com.kamaii.rider.utils.ExpandableHeightGridView;
import com.ncorti.slidetoact.SlideToActView;
import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.fragment.CabBookingsFragment;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.OnTouch;
import com.kamaii.rider.utils.ProjectUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class AdapterCabBookings extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = AdapterCabBookings.class.getSimpleName();
    private CabBookingsFragment newBookings;
    List<AllBooking> artistBookingsList;
    ArrayList<String> arr = new ArrayList<>();
    private ArrayList<AllBooking> searchBookingsList;
    private UserDTO userDTO;
    private LayoutInflater myInflater;
    private Context context;
    private HashMap<String, String> paramsBookingOp;
    private HashMap<String, String> paramsDecline;
    HashMap<String, String> requestParams;
    SharedPrefrence sharedPrefrence;
    boolean click_submit_btn_flag = false;
    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;
    private GridLayoutManager gridLayoutManager;
    private List<Product> productDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private Dialog dialog, dialogcustomservice, dialogdecline, dialogapproxtime, dialogcamera, dialogpriview, dialogfinish, dailograting;
    private CustomTextViewBold finddevice_btn, printbtn, tvdCancelreason, tvdAddreason, tvcameraskip, ctvbTitle, tvupload, tvtotalfinish, dialog_paytype, tvfinishsubmit, tvfinishsubmitrating;
    public static CustomTextViewBold tvcamera;
    String locationstatus = "";
    ImageView tvfinishcancel, pay_done_img, imageClose;
    CustomTextViewBold tvskip, tvupdate;
    CustomEditText etd_reason;
    CheckBox checkone, checktwo, checkthree, checkfour, checkfive, checksix;
    LinearLayout llDate;
    String reason;
    private SimpleDateFormat sdf1;
    CustomTextView tvapproxDate;
    private Date date;
    ImageView imgclose;
    FindPlaceInterface findPlaceInterface;
    public static ImageView img_upload;
    private int mHour, mMinute;
    AdapterCartCab adapterCart;
    Boolean isCheck = true;
    public static Boolean btnclick_flag = false;
    String cancelreason = "";
    public ProgressDialog progressDialog;
    LinearLayout layfinishbackground;
    private RatingBar rbReview;
    private float myrating;
    public static String pay_type = null;
    ExpandableHeightGridView cancell_reason_list;
    DatabaseHandler databaseHandler;
    private HashMap<String, String> cancelParms = new HashMap<>();
    private ArrayList<HashMap<String, String>> offlineDataParms = new ArrayList<>();
    List<CancelReasonModel> cancelList;
    public BottomSheetDialog addressBottomSheet;
    public CustomTextView no_rider_found_txt, change_address_btn;
    public CustomTextViewBold address_submit, changeCollection;
    public static CustomEditText location_etx, house_no_etx, society_name_etx;
    public String house_no_str, society_name_str;
    public static String street_address_str = "";
    LinearLayout customAddress;
    LinearLayout address_lay_dest_location;
    RelativeLayout address_relative;
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    String address_radio_txt = "Home";
    double live_latitude = 0.0;
    double live_longitude = 0.0;
    boolean address_flag = false;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    String found_user = "";
    String address_id = "";
    String myamount = "";
    String partner_image = "";
    String partner_name = "";
    String partner_id = "";
    String handover_text = "";
    Dialog paymentDialog;
    PaymentHandoverDialogBinding paymentBinding;
    EditText dialogPaymentEtx;
    boolean pickup_flag = false;
    boolean drop_flag = false;
    public boolean refresh_booking_flag = false;
    boolean isPLAYING = false;
    MediaPlayer mediaPlayer;
    public static String strSeparator = "__,__";

    private ArrayList<NewOrderModel> artistBookingsListnew = new ArrayList<>();
    private List<AllBooking> artistBookingsListnewcopy;
    TextView beforeText;
    String amount = "0";
    BaseActivity baseActivity;

    public AdapterCabBookings(CabBookingsFragment newBookings, List<AllBooking> artistBookingsList, UserDTO userDTO, LayoutInflater myInflater, String addrs, double lat, double longi, FindPlaceInterface findPlaceInterface, boolean refresh_booking_flag, BaseActivity baseActivity) {


        this.newBookings = newBookings;
        this.artistBookingsList = artistBookingsList;
        Log.e("rider_123", " sub adapter " + artistBookingsList.size());
        Log.e("SHIVAMCHECKING", "13");
        this.searchBookingsList = new ArrayList<AllBooking>();
        this.searchBookingsList.addAll(artistBookingsList);
        this.userDTO = userDTO;
        this.baseActivity = baseActivity;
        this.myInflater = myInflater;
        this.refresh_booking_flag = refresh_booking_flag;
        cancelList = new ArrayList<>();
        context = newBookings.getActivity();
        street_address_str = addrs;
        Log.e("street_address_str1234", "" + street_address_str);
        live_latitude = lat;
        live_longitude = longi;
        sharedPrefrence = SharedPrefrence.getInstance(context);
        this.findPlaceInterface = findPlaceInterface;
        databaseHandler = new DatabaseHandler(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        RecyclerView.ViewHolder vh;
       /* if (myInflater == null) {
            myInflater = LayoutInflater.from(viewGroup.getContext());
        }
        if (viewType == VIEW_ITEM) {

        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section, viewGroup, false);
            vh = new MyViewHolderSection(v);
        }*/
        AdapterCabBookingsBinding binding =
                DataBindingUtil.inflate(myInflater, R.layout.adapter_cab_bookings, viewGroup, false);
        vh = new MyViewHolder(binding);
        return vh;
    }

    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
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

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderMain, @SuppressLint("RecyclerView") int pos) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        Log.e("addressid partner", "" + artistBookingsList.get(pos).getPartnerAddressId());
        Log.e("addressid customer", "" + artistBookingsList.get(pos).getCustAddressId());

        sdf1 = new SimpleDateFormat(Consts.DATE_FORMATE_SERVER, Locale.ENGLISH);
        date = new Date();
        paramsBookingOp = new HashMap<>();
        dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dailog_add_services);
        dialog.setCancelable(false);

//        dialogPaymentEtx = paymentDialog.findViewById(R.id.dialog_payment_amount_etx);

        dialogcustomservice = new Dialog(context, R.style.Theme_Dialog);
        dialogcustomservice.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcustomservice.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialogcustomservice.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogcustomservice.setContentView(R.layout.dailog_add_custom_services);
        dialogcustomservice.setCancelable(false);

        dialogapproxtime = new Dialog(context, R.style.Theme_Dialog);
        dialogapproxtime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogapproxtime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialogapproxtime.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogapproxtime.setContentView(R.layout.dailog_approxtime);
        dialogapproxtime.setCancelable(false);


        dialogdecline = new Dialog(context, R.style.Theme_Dialog);
        dialogdecline.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogdecline.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogdecline.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogdecline.setContentView(R.layout.dailog_decline);
        dialogdecline.setCancelable(false);

        cancell_reason_list = dialogdecline.findViewById(R.id.cancell_reason_list);
        cancelParms.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());

//        getCancellReasons();

        dialogcamera = new Dialog(context, R.style.Theme_Dialog);
        dialogcamera.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcamera.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogcamera.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogcamera.setContentView(R.layout.dailog_camera);
        dialogcamera.setCancelable(false);


        dialogfinish = new Dialog(context, R.style.Theme_Dialog);
        dialogfinish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogfinish.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogfinish.setContentView(R.layout.dailog_finish);
        dialogfinish.setCancelable(false);


        dailograting = new Dialog(context, R.style.Theme_Dialog);
        dailograting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailograting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailograting.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dailograting.setContentView(R.layout.dailog_rating);
        dailograting.setCancelable(false);

        rbReview = dailograting.findViewById(R.id.rbReview);

        tvfinishsubmitrating = dailograting.findViewById(R.id.tvfinishsubmitrating);

        dialogpriview = new Dialog(context, R.style.Theme_Dialog);
        dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpriview.setContentView(R.layout.dailog_priview);
        dialogpriview.setCancelable(true);


        final int position = pos;


        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());


        if (holderMain instanceof MyViewHolder) {

            final MyViewHolder holder = (MyViewHolder) holderMain;

            gridLayoutManager = new GridLayoutManager(context, 1);
            productDTOArrayList = new ArrayList<>();


            productDTOArrayList = artistBookingsList.get(position).getProduct();
            //locationstatus = artistBookingsList.get(position).getL();

            // holder.binding.txtcat.setText(artistBookingsList.get(position).getCategory_name());
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm");
            String currentDateandTime = sdf12.format(new Date());

            //long diff = getDateDiff(new SimpleDateFormat("hh:mm"),  currentDateandTime,artistBookingsList.get(position).getCustomer_time_slot());
            //   long diff = getDateDiff(new SimpleDateFormat("hh:mm"),  "02/26/2014 09:00:00","02/26/2014 19:30:00");


            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            long second = 1000l;
            long minute = 60l * second;
            long hour = 60l * minute;

            Date date1 = null;
            Date date2 = null;
            try {
                date1 = dateFormat.parse("2022/02/05 06:30:00 PM");
                // date2 = dateFormat.format(c);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            //   long diff = date2.getTime() - date1.getTime();

            long diff = c.getTime() - date1.getTime();

            //String f = sdf12.f
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;


//            System.out.print("oneeee hr " + String.format("%02d", diff / hour));
//            System.out.print(":");
//            System.out.print(" : oneeee min " + String.format("%02d", (diff % hour) / minute));
//            System.out.print(":");
//            System.out.print(" : oneeee sec " + String.format("%02d", (diff % minute) / second));
//
//            // Calucalte time difference
//            // in milliseconds
//
//          //  int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"), "29/05/2017", "31/05/2017");
//
//            long different
//                    = d2.getTime() - d1.getTime();
//

            Log.e("TAG", "setMainAdapter: onBindViewHolder: " + artistBookingsList.get(position).getId2());


            getOfflineStore(position);


            holder.binding.customerslottime.setText("Delivery Before " + artistBookingsList.get(position).getCustomerTimeslot().substring(19));
//            holder.binding.customerslottime.setText(artistBookingsList.get(position).getCustomer_time_slot().substring(19)+" - "+currentDateandTime+" = "+hours+":"+minutes);

            // if (artistBookingsList.get(0).getMap().equalsIgnoreCase("1")){
            sharedPrefrence.setValue(Consts.MAP_TRACKER, artistBookingsList.get(0).getMap());
            Log.e("pickup_tracker", " adapter preference stored value " + sharedPrefrence.getValue(Consts.MAP_TRACKER));

            //  }
            Log.e("Customer_slot", " -- customer_slot-- " + artistBookingsList.get(position).getCustomerTimeslot());
            Log.e("Customer_slot", " -- partner_slot-- " + artistBookingsList.get(position).getPartnerTimeslot());
            holder.binding.customerTimeSlot.setText(artistBookingsList.get(position).getCustomerTimeslot());
            holder.binding.partnerTimeSlot.setText(artistBookingsList.get(position).getPartnerTimeslot());
            adapterCart = new AdapterCartCab(newBookings, productDTOArrayList, locationstatus);
            holder.binding.rvCart.setLayoutManager(gridLayoutManager);
            holder.binding.rvCart.setAdapter(adapterCart);

            holder.binding.tvName.setText(artistBookingsList.get(position).getIdwithhas());

            // holder.binding.tvDate.setText(ProjectUtils.changeDateFormate1(artistBookingsList.get(position).get()) + " " + artistBookingsList.get(position).getBooking_time());
            pay_type = artistBookingsList.get(pos).getPayType();


            if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("0")) {
                holder.binding.txtptype.setVisibility(View.GONE);
                holder.binding.txtptype.setText("Online Payment");

            } else if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("1")) {
                holder.binding.txtptype.setVisibility(View.GONE);
                holder.binding.txtptype.setText("Cash");

            } else if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("2")) {
                holder.binding.txtptype.setVisibility(View.GONE);
                holder.binding.txtptype.setText("Wallet Payment");

            } else {
                holder.binding.txtptype.setVisibility(View.GONE);
            }

         /*   Glide.with(context).
                    load(Consts.IMAGE_URL + artistBookingsList.get(position).())
                    .placeholder(R.drawable.dummyuser_image)
                    .into(holder.binding.ivArtist);*/


            holder.binding.imgphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //   if (!artistBookingsList.get(position).get().equalsIgnoreCase("")) {

                    String mobileno = artistBookingsList.get(position).getCustomerMobileNo();

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobileno));
                    context.startActivity(intent);
                    //   }

                }
            });


            holder.binding.imgphone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  if (!artistBookingsList.get(position).getUserMobile().equalsIgnoreCase("")) {

                    String mobileno = artistBookingsList.get(position).getUserMobile();

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobileno));
                    context.startActivity(intent);
                    // }

                }
            });

            holder.binding.llLocationdesti.setVisibility(View.VISIBLE);
            if (!artistBookingsList.get(position).getCustomerHouseNo().equalsIgnoreCase("")) {
                holder.binding.deliveryBlockNo.setText("House / Flat No : " + artistBookingsList.get(position).getCustomerHouseNo());
            } else {
                holder.binding.deliveryBlockNo.setVisibility(View.GONE);
            }
            if (!artistBookingsList.get(position).getCustomerSocietyName().equalsIgnoreCase("")) {
                holder.binding.deliveryLandmark.setText("Landmark : " + artistBookingsList.get(position).getCustomerSocietyName());
            } else {
                holder.binding.deliveryLandmark.setVisibility(View.GONE);
            }
            //    if (!artistBookingsList.get(position).getFulladdress().equalsIgnoreCase("")) {

            //  holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getFulladdress());
            holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getCustomerStreetAddress());
            //      }

            if (!artistBookingsList.get(position).getPartnerHouseNo().equalsIgnoreCase("")) {
                holder.binding.blockNo.setText("House / Flat No : " + artistBookingsList.get(position).getPartnerHouseNo());
            } else {
                holder.binding.blockNo.setVisibility(View.GONE);
            }
            if (!artistBookingsList.get(position).getPartnerSocietyName().equalsIgnoreCase("")) {
                holder.binding.landmarkName.setText("Landmark : " + artistBookingsList.get(position).getPartnerSocietyName());
            } else {
                holder.binding.landmarkName.setVisibility(View.GONE);
            }

            holder.binding.custNameTxt1.setText(artistBookingsList.get(position).getCustomerName());
            holder.binding.tvLocation.setText(artistBookingsList.get(position).getPartnerStreetAddress());

            holder.binding.tvtotal.setText(artistBookingsList.get(position).getOrderTotal());
            holder.binding.locationLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddressDialog(position);
                }
            });

            if (artistBookingsList.get(position).getPartner_late_button_show().equalsIgnoreCase("1")) {
                holder.binding.delaybtn.setVisibility(View.VISIBLE);
            } else {
                holder.binding.delaybtn.setVisibility(View.GONE);
            }

            holder.binding.delaybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HashMap<String, String> delayParams = new HashMap<>();
                    delayParams.put(Consts.BOOKING_ID, artistBookingsList.get(position).getId2());
                    new HttpsRequest(Consts.FOOD_NOT_READY, delayParams, context).stringPostthree("TAG", new Helper() {
                        @Override
                        public void backResponse(boolean flag, String msg, JSONObject response) {
                            if (flag) {

                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                Log.e("REQUEST_AMOUNT", "" + response.toString());
                            }
                        }
                    });
                }
            });
            holder.binding.layoutMoreLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCheck) {
                        holder.binding.laymoreee.setVisibility(View.VISIBLE);
                        isCheck = false;
                    } else {
                        holder.binding.laymoreee.setVisibility(View.GONE);
                        isCheck = true;
                    }
                }
            });


            holder.binding.pickupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (artistBookingsList.get(position).getPartnerLat().equalsIgnoreCase("0")) {
                        Toast.makeText(context, "Please contact to administrator", Toast.LENGTH_SHORT).show();
                    } else {

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(context, Locale.getDefault());

                        try {
                            Log.e("lat_lang", "" + artistBookingsList.get(position).getPartnerLat() + " " + artistBookingsList.get(position).getPartnerLong());
                            addresses = geocoder.getFromLocation(Double.parseDouble(artistBookingsList.get(position).getPartnerLat()), Double.parseDouble(artistBookingsList.get(position).getPartnerLong()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();

                            //Toast.makeText(context, "Address :" + address, Toast.LENGTH_SHORT).show();

                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getPartnerLat() + "," + artistBookingsList.get(position).getPartnerLong());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            context.startActivity(mapIntent);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });

            holder.binding.dropBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (artistBookingsList.get(position).getUserLat().equalsIgnoreCase("0")) {
                        Toast.makeText(context, "Please contact to administrator", Toast.LENGTH_SHORT).show();
                    } else {

                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getUserLat() + "," + artistBookingsList.get(position).getUserLong());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);

                        /* Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(0).getUserLat() + "," + artistBookingsList.get(0).getUserLong());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);*/
                    }
                }
            });

            holder.binding.startnew.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {


                    List<OfflineImageDataModal> imageData = databaseHandler.getAllContacts();
                    String[] servname = new String[artistBookingsList.get(position).getProduct().size()];

                    for (int l = 0;l<artistBookingsList.get(position).getProduct().size();l++){
                        servname[l]= artistBookingsList.get(position).getProduct().get(l).getProductName();
                    }
                    String ser_name = convertArrayToString(servname);

                    //   imageData.clear();
                    if (imageData.size() > 0) {

                        for (OfflineImageDataModal offlineImageDataModal : imageData){

                            Log.e(TAG, "onSlideComplete: imageData tracker 1 " + imageData.get(imageData.size() - 1).getBooking_id().equalsIgnoreCase(artistBookingsList.get(position).getId2()));
                            Log.e(TAG, "onSlideComplete: imageData tracker 1 " + imageData.get(imageData.size() - 1).getBooking_id());
                            Log.e(TAG, "onSlideComplete: imageData tracker 1 " + artistBookingsList.get(position).getOrderId());
                            if (offlineImageDataModal.getTracker().equalsIgnoreCase("1") && !offlineImageDataModal.getImage_path().equalsIgnoreCase("")
                                    &&
                                    offlineImageDataModal.getBooking_id().equalsIgnoreCase(artistBookingsList.get(position).getId2())) {
                                Log.e(TAG, "onSlideComplete: imageData tracker 2" + imageData.get(imageData.size() - 1).getBooking_id().equalsIgnoreCase(artistBookingsList.get(position).getOrderId()));

                                bookingstart("2", pos, String.valueOf(sdf1.format(date)));
                                progressDialog.show();

                            } else {
                                Log.e(TAG, "onSlideComplete: imageData tracker 3");

                                mediaPlayer = apiClient.getMediaPlayer(context);
                                AssetFileDescriptor descriptor = null;
                                if (!isPLAYING) {

                                    Log.e("isPLAYING", " 1v " + isPLAYING);
                                    isPLAYING = true;
                                    if (!mediaPlayer.isPlaying()) {

                                        try {
                                            //  descriptor = this.getAssets().openFd("accept.mpeg");
                                            mediaPlayer.reset();
                                            mediaPlayer.setDataSource(Consts.RIDER_PICKUP_RING);
                                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                            mediaPlayer.prepare();
                                            //mediaPlayer.setVolume(1f, 1f);
                                            mediaPlayer.setLooping(false);
                                            mediaPlayer.start();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Log.e("isPLAYING", " 2v " + isPLAYING);
                                        isPLAYING = false;
                                        apiClient.releaseMediaPlayer();
                                        //mediaPlayer.stop();
                                    }
                                } else {
                                    Log.e("isPLAYING", " 3v " + isPLAYING);

                                    // isPLAYING = false;
                                    apiClient.releaseMediaPlayer();
                                    //mediaPlayer.stop();
                                }

                                tvcameraskip = dialogcamera.findViewById(R.id.tvcameraskip);
                                tvcameraskip.setVisibility(View.GONE);
                                tvcamera = dialogcamera.findViewById(R.id.tvcamera);
                                ctvbTitle = dialogcamera.findViewById(R.id.ctvbTitle);
                                ctvbTitle.setText("Order Photo");
                                tvupload = dialogcamera.findViewById(R.id.tvupload);
                                imageClose = dialogcamera.findViewById(R.id.imageClose);
                                img_upload = dialogcamera.findViewById(R.id.img_upload);

                                tvcamera.setEnabled(true);
                                tvcamera.setClickable(true);
                                //   lldauploadImageLayout = dialogcamera.findViewById(R.id.uploadImageLayout);
                                dialogcamera.show();

                                Log.e("pickup_tracker", " pickup " + " orderid " + artistBookingsList.get(0).getOrderId() + " km " + artistBookingsList.get(0).getKm());

//                            if (btnclick_flag) {
//                                if (img_upload.getVisibility() == View.VISIBLE) {
//
//                                    tvcamera.setEnabled(true);
//                                    tvcamera.setClickable(true);
//                                }
//                            }

                                imageClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogcamera.dismiss();
                                        //notifyDataSetChanged();
                                        newBookings.reloadFragment();
                                    }
                                });
                                tvupload.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {

                                        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                            requestPermissions(newBookings.getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 23);
                                        } else {

                                            CabBookingsFragment.online_flag = false;
                                            newBookings.opencamrea();
                                        }

                                        //  lldauploadImageLayout.setVisibility(View.GONE);
                                    }
                                });

                                tvcamera.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (img_upload.getVisibility() == View.GONE) {

                                            Toast.makeText(context, "Please upload the image!!", Toast.LENGTH_SHORT).show();
//                                        tvcamera.setEnabled(false);
//                                        tvcamera.setClickable(false);
                                            Log.e(TAG, "onSlideComplete: btnclick_flag " + btnclick_flag);

                                            return;

                                        } else {
                                            Log.e(TAG, "onSlideComplete: btnclick_flag 222 " + btnclick_flag);
                                            boolean flag = sharedPrefrence.getBooleanValue("click_flag");
                                            if (!click_submit_btn_flag) {
                                                click_submit_btn_flag = true;
                                                tvcamera.setEnabled(true);
                                                tvcamera.setClickable(true);
                                                Log.e(TAG, "onClick: click 111-1 ");

                                                // sharedPrefrence.setBooleanValue("click_flag",true);
                                            } else {
                                                Log.e(TAG, "onClick: click 222-1 ");

                                                tvcamera.setEnabled(false);
                                                tvcamera.setClickable(false);
                                            }
                                        }
                                        tvcamera.setEnabled(false);
                                        tvcamera.setClickable(false);
                                        sharedPrefrence.setBooleanValue("pickup_flag", true);


                                        OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                                        offlineImageDataModal.setBooking_id(artistBookingsList.get(position).getId2());
                                        offlineImageDataModal.setImage_path(sharedPrefrence.getValue("pickup_img"));
                                        offlineImageDataModal.setTracker(artistBookingsList.get(position).getAppFlag());
                                        offlineImageDataModal.setApproxdatetime(String.valueOf(sdf1.format(date)));
                                        offlineImageDataModal.setRequest("2");
                                        offlineImageDataModal.setService_name(ser_name);
                                        offlineImageDataModal.setUser_id(userDTO.getUser_id());

                                        databaseHandler.updateContact(offlineImageDataModal);

                                        Toast.makeText(context, "Image saved in your device!!", Toast.LENGTH_SHORT).show();

                                        if (img_upload.getVisibility() == View.VISIBLE) {

                                            if (NetworkManager.isConnectToInternet(context)) {

                                                pickupbookingupload("2", position);

                                                Log.e("pickup_tracker", " refresh_booking_flag " + refresh_booking_flag);


                                            } else {
                                                //        ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                            }
                                        } else {
                                            Toast.makeText(context, "Please Upload Photo", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }
                        }

                    } else {

                        Log.e(TAG, "onSlideComplete: imageData tracker 4");

                        mediaPlayer = apiClient.getMediaPlayer(context);
                        AssetFileDescriptor descriptor = null;
                        if (!isPLAYING) {

                            Log.e("isPLAYING", " 1v " + isPLAYING);
                            isPLAYING = true;
                            if (!mediaPlayer.isPlaying()) {

                                try {
                                    //  descriptor = this.getAssets().openFd("accept.mpeg");
                                    mediaPlayer.reset();
                                    mediaPlayer.setDataSource(Consts.RIDER_PICKUP_RING);
                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    mediaPlayer.prepare();
                                    //mediaPlayer.setVolume(1f, 1f);
                                    mediaPlayer.setLooping(false);
                                    mediaPlayer.start();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.e("isPLAYING", " 2v " + isPLAYING);
                                isPLAYING = false;
                                apiClient.releaseMediaPlayer();
                                //mediaPlayer.stop();
                            }
                        } else {
                            Log.e("isPLAYING", " 3v " + isPLAYING);

                            // isPLAYING = false;
                            apiClient.releaseMediaPlayer();
                            //mediaPlayer.stop();
                        }
                        tvcameraskip = dialogcamera.findViewById(R.id.tvcameraskip);
                        tvcameraskip.setVisibility(View.GONE);
                        tvcamera = dialogcamera.findViewById(R.id.tvcamera);
                        ctvbTitle = dialogcamera.findViewById(R.id.ctvbTitle);
                        ctvbTitle.setText("Order Photo");
                        tvupload = dialogcamera.findViewById(R.id.tvupload);
                        imageClose = dialogcamera.findViewById(R.id.imageClose);
                        img_upload = dialogcamera.findViewById(R.id.img_upload);


                        tvcamera.setEnabled(true);
                        tvcamera.setClickable(true);

                        //   lldauploadImageLayout = dialogcamera.findViewById(R.id.uploadImageLayout);
                        dialogcamera.show();

//                        if (btnclick_flag) {
//                            if (img_upload.getVisibility() == View.VISIBLE) {
//
//                                tvcamera.setEnabled(true);
//                                tvcamera.setClickable(true);
//                            }
//                        }

                        Log.e("pickup_tracker", " pickup " + " orderid " + artistBookingsList.get(0).getOrderId() + " km " + artistBookingsList.get(0).getKm());

                        imageClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogcamera.dismiss();
                                //notifyDataSetChanged();
                                newBookings.reloadFragment();
                            }
                        });
                        tvupload.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {

                                if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                        context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    requestPermissions(newBookings.getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 23);
                                } else {

                                    CabBookingsFragment.online_flag = false;
                                    newBookings.opencamrea();
                                }

                                //  lldauploadImageLayout.setVisibility(View.GONE);
                            }
                        });

                        tvcamera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (img_upload.getVisibility() == View.GONE) {
                                    Log.e(TAG, "onSlideComplete: btnclick_flag 11 " + btnclick_flag);

                                    Toast.makeText(context, "Please upload the image!!", Toast.LENGTH_SHORT).show();
//                                    tvcamera.setEnabled(false);
//                                    tvcamera.setClickable(false);
                                    return;

                                } else {
                                    Log.e(TAG, "onSlideComplete: btnclick_flag 222 " + btnclick_flag);

                                    if (!click_submit_btn_flag) {
                                        click_submit_btn_flag = true;
                                        tvcamera.setEnabled(true);
                                        tvcamera.setClickable(true);
                                        Log.e(TAG, "onClick: click 111-2 ");
                                        // sharedPrefrence.setBooleanValue("click_flag",true);
                                    } else {
                                        tvcamera.setEnabled(false);
                                        tvcamera.setClickable(false);
                                        Log.e(TAG, "onClick: click 222-2 ");
                                    }
                                }
                                sharedPrefrence.setBooleanValue("pickup_flag", true);
                                tvcamera.setEnabled(false);
                                tvcamera.setClickable(false);

                                OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                                offlineImageDataModal.setBooking_id(artistBookingsList.get(position).getId2());
                                offlineImageDataModal.setImage_path(sharedPrefrence.getValue("pickup_img"));
                                offlineImageDataModal.setTracker(artistBookingsList.get(position).getAppFlag());
                                offlineImageDataModal.setApproxdatetime(String.valueOf(sdf1.format(date)));
                                offlineImageDataModal.setRequest("2");
                                offlineImageDataModal.setUser_id(userDTO.getUser_id());
                                offlineImageDataModal.setService_name(ser_name);
                                databaseHandler.updateContact(offlineImageDataModal);

                                Toast.makeText(context, "Image saved in your device!!", Toast.LENGTH_SHORT).show();

                                if (img_upload.getVisibility() == View.VISIBLE) {

                                    if (NetworkManager.isConnectToInternet(context)) {

                                        pickupbookingupload("2", position);

                                        Log.e("pickup_tracker", " refresh_booking_flag " + refresh_booking_flag);


                                    } else {
                                        //        ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                    }
                                } else {
                                    Toast.makeText(context, "Please Upload Photo", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }


                }
            });

            holder.binding.orderCount.setText(String.valueOf(position + 1));
            holder.binding.partnerName.setText(artistBookingsList.get(position).getPartnerName());
            holder.binding.callLinearPartner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileno = artistBookingsList.get(position).getPartnerMobile();

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobileno));
                    context.startActivity(intent);
                }
            });
            if (artistBookingsList.get(position).getOrderEnable().equalsIgnoreCase("1")) {

                Log.e(TAG, "onBindViewHolder: " + " if 1");
              /*  holder.binding.cardRelativeMain.setBackground(context.getResources().getDrawable(R.drawable.rider_request_bg));
                holder.binding.cardRelativeMain.setAlpha(1f);*/

            /*    holder.binding.locationLinear.setEnabled(true);
                holder.binding.locationLinear.setClickable(true);
                holder.binding.layoutMoreLinear.setEnabled(true);
                holder.binding.layoutMoreLinear.setClickable(true);
                holder.binding.dropBtn.setEnabled(true);
                holder.binding.dropBtn.setEnabled(true);
                holder.binding.pickupBtn.setEnabled(true);
                holder.binding.pickupBtn.setClickable(true);*/
                holder.binding.startnew.setEnabled(true);
                holder.binding.finishnew.setEnabled(true);
                holder.binding.startnew.setClickable(true);
                holder.binding.finishnew.setClickable(true);
                holder.binding.tmpViewFirst.setBackgroundColor(context.getResources().getColor(R.color.purple));
                holder.binding.customerslottime.setTextColor(context.getResources().getColor(R.color.purple));
                holder.binding.toprelativelayout.setBackgroundColor(context.getResources().getColor(R.color.light_purple));
                holder.binding.tmpView1.setBackgroundColor(context.getResources().getColor(R.color.purple));
                holder.binding.tmpView21.setBackgroundColor(context.getResources().getColor(R.color.black));
                //    holder.binding.tmpView1111.setBackgroundColor(context.getResources().getColor(R.color.green_slide_color));
                holder.binding.tmpView11.setBackgroundColor(context.getResources().getColor(R.color.purple));
                holder.binding.tmpView2.setBackgroundColor(context.getResources().getColor(R.color.purple));
                holder.binding.tmpView3.setBackgroundColor(context.getResources().getColor(R.color.purple));
                holder.binding.tmpView123.setBackgroundColor(context.getResources().getColor(R.color.purple));

            } else if (artistBookingsList.get(position).getOrderEnable().equalsIgnoreCase("0")) {

                Log.e(TAG, "onBindViewHolder: " + " if 2");
//                holder.binding.cardRelativeMain.setBackground(context.getResources().getDrawable(R.drawable.rider_request_bg2));
                //   holder.binding.cardRelativeMain.setAlpha(0.40f);
                holder.binding.cardRelativeMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  mediaPlayer = new MediaPlayer();
                        mediaPlayer = apiClient.getMediaPlayer(context);
                        AssetFileDescriptor descriptor = null;
                        if (!isPLAYING) {

                            Log.e("isPLAYING", " 1v " + isPLAYING);
                            isPLAYING = true;
                            if (!mediaPlayer.isPlaying()) {

                                try {
                                    //  descriptor = this.getAssets().openFd("accept.mpeg");
                                    mediaPlayer.reset();
                                    descriptor = context.getAssets().openFd("order_sequence.mp3");
                                    mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                                    descriptor.close();
                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                                    mediaPlayer.prepare();
                                    mediaPlayer.setVolume(1f, 1f);
                                    mediaPlayer.setLooping(false);
                                    mediaPlayer.start();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.e("isPLAYING", " 2v " + isPLAYING);
                                isPLAYING = false;
                                apiClient.releaseMediaPlayer();
                                //mediaPlayer.stop();
                            }
                        } else {
                            Log.e("isPLAYING", " 3v " + isPLAYING);

                            // isPLAYING = false;
                            apiClient.releaseMediaPlayer();
                            //mediaPlayer.stop();
                        }
                    }
                });

               /* holder.binding.locationLinear.setEnabled(false);
                holder.binding.locationLinear.setClickable(false);
                holder.binding.layoutMoreLinear.setEnabled(false);
                holder.binding.layoutMoreLinear.setClickable(false);

                holder.binding.dropBtn.setEnabled(false);
                holder.binding.dropBtn.setEnabled(false);
                holder.binding.pickupBtn.setEnabled(false);
                holder.binding.pickupBtn.setClickable(false);*/
                holder.binding.startnew.setEnabled(false);
                holder.binding.finishnew.setEnabled(false);
                holder.binding.startnew.setClickable(false);
                holder.binding.finishnew.setClickable(false);
                holder.binding.toprelativelayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.tmpViewFirst.setBackgroundColor(context.getResources().getColor(R.color.black));
                holder.binding.customerslottime.setTextColor(context.getResources().getColor(R.color.black));
                holder.binding.tmpView1.setBackgroundColor(context.getResources().getColor(R.color.black));
                holder.binding.tmpView21.setBackgroundColor(context.getResources().getColor(R.color.black));
                //      holder.binding.tmpView1111.setBackgroundColor(context.getResources().getColor(R.color.black));
                holder.binding.tmpView11.setBackgroundColor(context.getResources().getColor(R.color.black));
                holder.binding.tmpView2.setBackgroundColor(context.getResources().getColor(R.color.black));
                holder.binding.tmpView3.setBackgroundColor(context.getResources().getColor(R.color.black));
                holder.binding.tmpView123.setBackgroundColor(context.getResources().getColor(R.color.black));

            }


            if (artistBookingsList.get(pos).getAppFlag().equalsIgnoreCase("2")) {

                found_user = "customer";
                Log.e(TAG, "onBindViewHolder: " + " if 3");
                if (artistBookingsList.get(position).getCustomer_visible().equalsIgnoreCase("1")) {
                    holder.binding.callLinear.setVisibility(View.GONE);
                    holder.binding.finishnew.setVisibility(View.VISIBLE);
                    if (artistBookingsList.get(position).getDropBtnTracker().equalsIgnoreCase("1")) {
                        holder.binding.dropBtn.setVisibility(View.VISIBLE);
                        holder.binding.dropBtnDupli.setVisibility(View.GONE);
                    } else {
                        holder.binding.dropBtn.setVisibility(View.GONE);
                        holder.binding.dropBtnDupli.setVisibility(View.VISIBLE);
                    }

                } else {
                    if (artistBookingsList.get(position).getDropBtnTracker().equalsIgnoreCase("1")) {
                        holder.binding.dropBtn.setVisibility(View.VISIBLE);
                        holder.binding.dropBtnDupli.setVisibility(View.GONE);
                    } else {
                        holder.binding.dropBtn.setVisibility(View.GONE);
                        holder.binding.dropBtnDupli.setVisibility(View.VISIBLE);
                    }

                    holder.binding.gotocust.setVisibility(View.VISIBLE);
                    holder.binding.callLinear.setVisibility(View.GONE);
//                    holder.binding.callLinear.setAlpha(1f);

                    holder.binding.callLinear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendReachNotification(artistBookingsList.get(position).getId(), position, "1");
                        }
                    });
                }
                holder.binding.finishnew.setText("Delivered");
                holder.binding.pickupRel.setVisibility(View.GONE);
                holder.binding.morePickupAddressTitle.setVisibility(View.GONE);
                holder.binding.llLocation.setVisibility(View.GONE);
                holder.binding.tmpView21.setVisibility(View.GONE);

                holder.binding.partnerPrepTitle.setVisibility(View.GONE);
                holder.binding.partnerTimeSlot.setVisibility(View.GONE);

                holder.binding.llCancel.setVisibility(View.GONE);
                //     holder.binding.llCancelduplicate.setVisibility(View.VISIBLE);
                holder.binding.pickupBtn.setVisibility(View.GONE);
                holder.binding.pickupBtnDupli.setVisibility(View.GONE);
//                holder.binding.dropBtn.setVisibility(View.VISIBLE);
                holder.binding.callLinearDisable.setVisibility(View.GONE);
                holder.binding.callLinear.setVisibility(View.GONE);

                //     holder.binding.riderRelative1.setVisibility(View.GONE);
                //        holder.binding.tmpView1111.setVisibility(View.GONE);


                holder.binding.tvdestiLocation.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.tvdestiLocation.setTextColor(context.getResources().getColor(R.color.green));

                holder.binding.deliveryBlockNo.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.deliveryBlockNo.setTextColor(context.getResources().getColor(R.color.green));

                holder.binding.deliveryLandmark.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.deliveryLandmark.setTextColor(context.getResources().getColor(R.color.green));
                holder.binding.deliveryrunning.setVisibility(View.GONE);
                holder.binding.pickuprunning.setVisibility(View.GONE);
                holder.binding.moreDeliveryAddressTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_16));
                //    holder.binding.moreDeliveryAddressTitle.setTextColor(context.getResources().getColor(R.color.green));

                holder.binding.deliveryLocationIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));


                holder.binding.tvLocation.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_12));
                holder.binding.tvLocation.setTextColor(context.getResources().getColor(R.color.black));

                holder.binding.blockNo.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_12));
                holder.binding.blockNo.setTextColor(context.getResources().getColor(R.color.black));

                holder.binding.landmarkName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_12));
                holder.binding.landmarkName.setTextColor(context.getResources().getColor(R.color.black));

                holder.binding.morePickupAddressTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_16));
                holder.binding.morePickupAddressTitle.setTextColor(context.getResources().getColor(R.color.black));

                holder.binding.pickupLocationIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.icon_color)));

                // holder.binding.moreAddressTitle.setText("Delivery Address");

            } else if (artistBookingsList.get(pos).getAppFlag().equalsIgnoreCase("1")) {

                found_user = "partner";
                Log.e(TAG, "onBindViewHolder: " + " if 4");
                if (artistBookingsList.get(position).getBeforeText().equals("")) {
                    holder.binding.riderWaitingTxt.setVisibility(View.GONE);
                    holder.binding.riderWaitingLayout.setVisibility(View.GONE);
                } else {
                    holder.binding.riderWaitingLayout.setVisibility(View.VISIBLE);
                    holder.binding.riderWaitingTxt.setVisibility(View.VISIBLE);
                    holder.binding.riderWaitingTxt.setText(artistBookingsList.get(position).getBeforeText());
                }
                holder.binding.pickupRel.setVisibility(View.VISIBLE);
                holder.binding.morePickupAddressTitle.setVisibility(View.VISIBLE);
                holder.binding.llLocation.setVisibility(View.VISIBLE);
                holder.binding.tmpView21.setVisibility(View.VISIBLE);

                holder.binding.partnerPrepTitle.setVisibility(View.VISIBLE);
                holder.binding.partnerTimeSlot.setVisibility(View.VISIBLE);

                holder.binding.startnew.setText("Pickup Order");

                holder.binding.callLinear.setVisibility(View.VISIBLE);
                if (artistBookingsList.get(position).getPartner_visible().equalsIgnoreCase("1")) {
                    Log.e(TAG, "onBindViewHolder: " + " if 5");
                    holder.binding.callLinear.setVisibility(View.GONE);
                    holder.binding.startnew.setVisibility(View.VISIBLE);
                    if (artistBookingsList.get(position).getPickupBtnTracker().equalsIgnoreCase("1")) {
                        Log.e(TAG, "onBindViewHolder: " + " if 6");
                        holder.binding.pickupBtn.setVisibility(View.VISIBLE);
                        holder.binding.pickupBtnDupli.setVisibility(View.GONE);
                    } else {
                        Log.e(TAG, "onBindViewHolder: " + " if 7");
                        holder.binding.pickupBtn.setVisibility(View.GONE);
                        holder.binding.pickupBtnDupli.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (artistBookingsList.get(position).getPickupBtnTracker().equalsIgnoreCase("1")) {
                        Log.e(TAG, "onBindViewHolder: " + " if 8");
                        holder.binding.pickupBtn.setVisibility(View.VISIBLE);
                        holder.binding.pickupBtnDupli.setVisibility(View.GONE);
                    } else {
                        Log.e(TAG, "onBindViewHolder: " + " if 9");
                        holder.binding.pickupBtn.setVisibility(View.GONE);
                        holder.binding.pickupBtnDupli.setVisibility(View.VISIBLE);
                    }
//                    holder.binding.pickupBtn.setVisibility(View.GONE);
//                    holder.binding.pickupBtnDupli.setVisibility(View.VISIBLE);
                    holder.binding.gotopart.setVisibility(View.VISIBLE);
                    holder.binding.callLinear.setVisibility(View.GONE);
//                    holder.binding.callLinear.setAlpha(1f);

                    holder.binding.callLinear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendReachNotification(artistBookingsList.get(position).getId(), position, "1");
                        }
                    });
                }

                holder.binding.tvdestiLocation.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.tvdestiLocation.setTextColor(context.getResources().getColor(R.color.black));

                holder.binding.deliveryBlockNo.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.deliveryBlockNo.setTextColor(context.getResources().getColor(R.color.black));

                holder.binding.deliveryLandmark.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.deliveryLandmark.setTextColor(context.getResources().getColor(R.color.black));

                holder.binding.moreDeliveryAddressTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_16));
                holder.binding.moreDeliveryAddressTitle.setTextColor(context.getResources().getColor(R.color.black));

                holder.binding.deliveryLocationIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.icon_color)));

                holder.binding.deliveryrunning.setVisibility(View.GONE);
                holder.binding.tvLocation.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.tvLocation.setTextColor(context.getResources().getColor(R.color.green));
                holder.binding.pickuprunning.setVisibility(View.GONE);
                holder.binding.blockNo.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.blockNo.setTextColor(context.getResources().getColor(R.color.green));

                holder.binding.landmarkName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_15));
                holder.binding.landmarkName.setTextColor(context.getResources().getColor(R.color.green));

                holder.binding.morePickupAddressTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimension(R.dimen.text_size_16));
                //   holder.binding.morePickupAddressTitle.setTextColor(context.getResources().getColor(R.color.green));

                holder.binding.pickupLocationIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));

                if (artistBookingsList.get(position).getCashDeposite().equalsIgnoreCase("1")) {

                    Log.e(TAG, "onBindViewHolder: " + " if 10");
                    paymentDialog = new Dialog(context, R.style.Theme_Dialog);
                    paymentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    paymentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    paymentDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT);
                    paymentBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.payment_handover_dialog, null, false);
                    paymentDialog.setContentView(paymentBinding.getRoot());
                    paymentDialog.setCancelable(false);

                    Glide.with(context).load(artistBookingsList.get(position).getPartnerImage()).placeholder(R.drawable.dafault_product).into(paymentBinding.dialogPartnerImage);
                    paymentBinding.dialogPaymentPartnerName.setText(artistBookingsList.get(position).getPartnerName());
                    paymentBinding.dialogPaymentAmountEtx.setText(artistBookingsList.get(position).getMyamount());
                    paymentBinding.dialogPaymentTxt.setText(artistBookingsList.get(position).getHandoverText());
                    paymentBinding.cancelPartnerPayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            paymentDialog.dismiss();
                        }
                    });
                    paymentBinding.dialogPaymentSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("payment1234", " paymentBinding " + paymentBinding.dialogPaymentAmountEtx.getText().toString());

                            paymentBinding.dialogPaymentSubmit.setEnabled(false);
                            paymentBinding.dialogPaymentSubmit.setClickable(false);
                            if (!paymentBinding.dialogPaymentAmountEtx.getText().toString().isEmpty()) {
                                myamount = artistBookingsList.get(position).getMyamount();
                                Log.e("payment1234", " myamount " + myamount);

                                int amt = Integer.parseInt(paymentBinding.dialogPaymentAmountEtx.getText().toString());
                                if (amt <= Integer.parseInt(myamount)) {

                                    requestParams = new HashMap<>();
                                    requestParams.put(Consts.ARTIST_ID, artistBookingsList.get(position).getPartnerId());
                                    requestParams.put(Consts.RIDER_ID, userDTO.getUser_id());
                                    requestParams.put(Consts.AMOUNT, paymentBinding.dialogPaymentAmountEtx.getText().toString());

                                    new HttpsRequest(Consts.REQUEST_AMOUNT_TO_PARTNER, requestParams, context).stringPost("TAG", new Helper() {
                                        @Override
                                        public void backResponse(boolean flag, String msg, JSONObject response) {
                                            if (flag) {

                                                paymentBinding.dialogPaymentAmountEtx.setText("");
                                                paymentDialog.dismiss();
                                                Log.e("REQUEST_AMOUNT", "" + response.toString());
                                            }
                                        }
                                    });

                                } else {

                                    Log.e("payment1234", "1");
                                    Toast.makeText(context, "Enter valid amount", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Log.e("payment1234", "2");
                                Toast.makeText(context, "Enter valid amount", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    paymentDialog.show();
                }

            }

            holder.binding.llCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   holder.binding.llarrival.setVisibility(View.GONE);
                    //  etd_reason = dialogdecline.findViewById(R.id.etd_reason);
                    tvdCancelreason = dialogdecline.findViewById(R.id.tvdCancelreason);
                    tvdAddreason = dialogdecline.findViewById(R.id.tvdAddreason);
                    dialogdecline.show();

                    checkone = dialogdecline.findViewById(R.id.checkone);
                    checktwo = dialogdecline.findViewById(R.id.checktwo);
                    checkthree = dialogdecline.findViewById(R.id.checkthree);
                    checkfour = dialogdecline.findViewById(R.id.checkfour);
                    tvdCancelreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            cancelreason = "";
                            dialogdecline.dismiss();
                        }
                    });
                    tvdAddreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (cancelreason.equalsIgnoreCase("")) {
                                ProjectUtils.showToast(context, "Please Enter Reason");
                            } else {

                                decline(position, cancelreason);
                                dialogdecline.dismiss();
                            }


                        }
                    });

                }
            });


            holder.binding.finishnew.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                    List<OfflineImageDataModal> imageData = databaseHandler.getAllContacts();
                    String[] servname = new String[artistBookingsList.get(position).getProduct().size()];

                    for (int l = 0;l<artistBookingsList.get(position).getProduct().size();l++){
                        servname[l]= artistBookingsList.get(position).getProduct().get(l).getProductName();
                    }
                    String ser_name = convertArrayToString(servname);

                    if (imageData.size() > 0) {

                        for (OfflineImageDataModal offlineImageDataModal : imageData){

                            Log.e(TAG, "onSlideComplete: imageData tracker 1 " + imageData.get(imageData.size() - 1).getBooking_id().equalsIgnoreCase(artistBookingsList.get(position).getOrderId()));
                            Log.e(TAG, "onSlideComplete: imageData tracker 1 " + imageData.get(imageData.size() - 1).getBooking_id());
                            Log.e(TAG, "onSlideComplete: imageData tracker 1 " + artistBookingsList.get(position).getOrderId());
                            if (offlineImageDataModal.getTracker().equalsIgnoreCase("2") && !offlineImageDataModal.getImage_path().equalsIgnoreCase("")
                                        &&
                                    offlineImageDataModal.getBooking_id().equalsIgnoreCase(artistBookingsList.get(position).getId2())) {
                                Log.e(TAG, "onSlideComplete: imageData tracker 2" + imageData.get(imageData.size() - 1).getBooking_id().equalsIgnoreCase(artistBookingsList.get(position).getOrderId()));

                                if (NetworkManager.isConnectToInternet(context)) {

                                    //printBluetooth(position);

                                    dialogcamera.dismiss();
                                    tvtotalfinish = dialogfinish.findViewById(R.id.tvtotalfinish);
                                    pay_done_img = dialogfinish.findViewById(R.id.pay_done_img);
                                    dialog_paytype = dialogfinish.findViewById(R.id.dialog_paytype);
                                    tvfinishsubmit = dialogfinish.findViewById(R.id.tvfinishsubmit);
                                    tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                                    layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);

                                    tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                                    layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);
                                    changeCollection = dialogfinish.findViewById(R.id.changeCollection);


                                   /* if (artistBookingsList.get(position).getShow_print_button().equalsIgnoreCase("1")) {
                                        print_linear.setVisibility(View.VISIBLE);
                                        printbtn.setVisibility(View.VISIBLE);
                                    } else {
                                        print_linear.setVisibility(View.GONE);
                                        printbtn.setVisibility(View.GONE);
                                    }*/


                                    if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("0")) {
                                        if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                            changeCollection.setVisibility(View.VISIBLE);
                                        } else {
                                            changeCollection.setVisibility(View.GONE);
                                        }
                                        dialog_paytype.setText("Onlile Payment");
                                        //   tvtotalfinish.setVisibility(View.GONE);
                                        pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                        //     layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.chat_out));
                                        layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                        tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.dark_blue_color));
                                        tvfinishsubmit.setText("Okay");


                                    } else if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("1")) {
                                        if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                            changeCollection.setVisibility(View.VISIBLE);
                                        } else {
                                            changeCollection.setVisibility(View.GONE);
                                        }

                                        dialog_paytype.setText("Cash Payment");
                                        layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.green));
                                        tvfinishsubmit.setText("Collect");

                                        changeCollection.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Dialog dialog = new Dialog(context);

                                                dialog.setContentView(R.layout.dailog_cashout_change);
                                                dialog.setCancelable(false);
//                                                dialog.setTitle("Custom Alert Dialog");

                                                final CustomEditText amountCustomer = (CustomEditText) dialog.findViewById(R.id.etamount);
                                                CustomTextViewBold btnSave = dialog.findViewById(R.id.tvAdd);
                                                ImageView btnCancel = dialog.findViewById(R.id.tvCancel);
                                                CustomTextView totalAmounting = dialog.findViewById(R.id.totalAmounting);
                                                CustomTextView change_amounting = dialog.findViewById(R.id.change_amounting);

                                                totalAmounting.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getOrderTotal()));
                                                amountCustomer.requestFocus();
                                                dialog.show();

                                                amountCustomer.addTextChangedListener(new TextWatcher() {

                                                    @Override
                                                    public void afterTextChanged(Editable s) {
                                                    }

                                                    @Override
                                                    public void beforeTextChanged(CharSequence s, int start,
                                                                                  int count, int after) {
                                                    }

                                                    @Override
                                                    public void onTextChanged(CharSequence s, int start,
                                                                              int before, int count) {
                                                        if (s.length() != 0)
                                                            change_amounting.setText(Html.fromHtml("&#x20B9;" + (Integer.parseInt(amountCustomer.getText().toString()) - Integer.parseInt(artistBookingsList.get(position).getOrderTotal()))));
//                                                            change_amounting.setText("");
                                                    }
                                                });

                                                btnSave.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Integer.parseInt(amountCustomer.getText().toString()) <= Integer.parseInt(artistBookingsList.get(position).getOrderTotal())) {
                                                            Toast.makeText(context, "Kindly collect more amount then order amount", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            amount = amountCustomer.getText().toString();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });
                                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                            }
                                        });
                                    } else if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("2")) {
                                        if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                            changeCollection.setVisibility(View.VISIBLE);
                                        } else {
                                            changeCollection.setVisibility(View.GONE);
                                        }
                                        dialog_paytype.setText("Wallet Payment");
                                        // tvtotalfinish.setVisibility(View.GONE);
                                        pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                        layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                        tvfinishsubmit.setText("Okay");
                                        tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.dark_blue_color));

                                    }
                                    dialogfinish.show();


                                    tvtotalfinish.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getOrderTotal()));


                                    tvfinishcancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialogfinish.dismiss();
                                        }
                                    });


                                    tvfinishsubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //dialogfinish.dismiss();
                                            // dialogcamera.dismiss();
//                                            Toast.makeText(context, amount, LENGTH_LONG).show();
                                            tvfinishsubmit.setEnabled(false);
                                            tvfinishsubmit.setClickable(false);

                                            deliverybookingupload("3", position, amount);

//                                            dialogfinish.dismiss();
                                        }
                                    });


                                } else {
                                    //        ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                }

                            } else {
                                Log.e(TAG, "onSlideComplete: imageData tracker 3");

                                printbtn = dialogcamera.findViewById(R.id.printbtn);
                                LinearLayout print_linear = dialogcamera.findViewById(R.id.print_linear);
                                tvcameraskip = dialogcamera.findViewById(R.id.tvcameraskip);
                                tvcamera = dialogcamera.findViewById(R.id.tvcamera);
                                tvupload = dialogcamera.findViewById(R.id.tvupload);
                                img_upload = dialogcamera.findViewById(R.id.img_upload);
                                finddevice_btn = dialogcamera.findViewById(R.id.finddevice_btn);
                                imageClose = dialogcamera.findViewById(R.id.imageClose);

                                //   lldauploadImageLayout = dialogcamera.findViewById(R.id.uploadImageLayout);
                                dialogcamera.show();
                                imageClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogcamera.dismiss();
                                        //  context.loadHomeFragment(new MyEarning(), "8");
                                        newBookings.reloadFragment();
                                    }
                                });
                                finddevice_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //  browseBluetoothDevice();
                                        // printBluetooth(position);
                                    }
                                });

                                printbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                        if (!bluetoothAdapter.isEnabled()) {

                                            bluetoothAdapter.enable();

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    printBluetooth(position);
                                                }
                                            }, 3000);
                                        }
                                    }
                                });

                                tvcameraskip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (NetworkManager.isConnectToInternet(context)) {

                                            //printBluetooth(position);
                                            booking("3", pos);
                                            dialogcamera.dismiss();

                                        } else {
                                            //       ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                        }
                                        dialogcamera.dismiss();
                                    }
                                });

                                tvupload.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {

                                        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                                ||
                                                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                            requestPermissions(newBookings.getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 23);
                                        } else {


                                            tvupload.setEnabled(false);
                                            tvupload.setClickable(false);
                                            newBookings.opencamrea();
                                        }


                                        //  lldauploadImageLayout.setVisibility(View.GONE);
                                    }
                                });

                                tvcamera.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (img_upload.getVisibility() == View.GONE) {
                                            Log.e(TAG, "onSlideComplete: btnclick_flag 11 " + btnclick_flag);

                                            Toast.makeText(context, "Please upload the image!!", Toast.LENGTH_SHORT).show();
//                                    tvcamera.setEnabled(false);
//                                    tvcamera.setClickable(false);
                                            return;

                                        }else {
                                            Log.e(TAG, "onSlideComplete: btnclick_flag 222 " + btnclick_flag);
                                            boolean flag = sharedPrefrence.getBooleanValue("click_flag");
                                            if (!click_submit_btn_flag) {
                                                click_submit_btn_flag = true;
                                                tvcamera.setEnabled(true);
                                                tvcamera.setClickable(true);
                                                Log.e(TAG, "onClick: click 111-3 ");

                                                // sharedPrefrence.setBooleanValue("click_flag",true);
                                            } else {
                                                Log.e(TAG, "onClick: click 222-3 ");

                                                tvcamera.setEnabled(false);
                                                tvcamera.setClickable(false);
                                            }
                                        }
                                        tvcamera.setEnabled(false);
                                        tvcamera.setClickable(false);

                                        OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                                        offlineImageDataModal.setBooking_id(artistBookingsList.get(position).getId2());
                                        offlineImageDataModal.setImage_path(sharedPrefrence.getValue("pickup_img"));
                                        offlineImageDataModal.setTracker(artistBookingsList.get(position).getAppFlag());
                                        offlineImageDataModal.setRequest("3");
                                        offlineImageDataModal.setUser_id(userDTO.getUser_id());
                                        offlineImageDataModal.setService_name(ser_name);
                                        offlineImageDataModal.setAmount(artistBookingsList.get(position).getOrderTotal());
                                        databaseHandler.updateContact(offlineImageDataModal);

                                        Toast.makeText(context, "Image saved in your device!!", Toast.LENGTH_SHORT).show();

                        /*    tvcamera.setEnabled(false);
                            tvcamera.setClickable(false);*/
                                        if (img_upload.getVisibility() == View.VISIBLE) {

                                            if (NetworkManager.isConnectToInternet(context)) {

                                                //printBluetooth(position);

                                                dialogcamera.dismiss();
                                                tvtotalfinish = dialogfinish.findViewById(R.id.tvtotalfinish);
                                                pay_done_img = dialogfinish.findViewById(R.id.pay_done_img);
                                                dialog_paytype = dialogfinish.findViewById(R.id.dialog_paytype);
                                                tvfinishsubmit = dialogfinish.findViewById(R.id.tvfinishsubmit);
                                                tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                                                layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);

                                                tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                                                layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);
                                                changeCollection = dialogfinish.findViewById(R.id.changeCollection);


                                   /* if (artistBookingsList.get(position).getShow_print_button().equalsIgnoreCase("1")) {
                                        print_linear.setVisibility(View.VISIBLE);
                                        printbtn.setVisibility(View.VISIBLE);
                                    } else {
                                        print_linear.setVisibility(View.GONE);
                                        printbtn.setVisibility(View.GONE);
                                    }*/


                                                if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("0")) {
                                                    if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                                        changeCollection.setVisibility(View.VISIBLE);
                                                    } else {
                                                        changeCollection.setVisibility(View.GONE);
                                                    }
                                                    dialog_paytype.setText("Onlile Payment");
                                                    //   tvtotalfinish.setVisibility(View.GONE);
                                                    pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                                    //     layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.chat_out));
                                                    layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                                    tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.dark_blue_color));
                                                    tvfinishsubmit.setText("Okay");


                                                } else if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("1")) {
                                                    if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                                        changeCollection.setVisibility(View.VISIBLE);
                                                    } else {
                                                        changeCollection.setVisibility(View.GONE);
                                                    }

                                                    dialog_paytype.setText("Cash Payment");
                                                    layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.green));
                                                    tvfinishsubmit.setText("Collect");

                                                    changeCollection.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            final Dialog dialog = new Dialog(context);

                                                            dialog.setContentView(R.layout.dailog_cashout_change);
                                                            dialog.setCancelable(false);
//                                                dialog.setTitle("Custom Alert Dialog");

                                                            final CustomEditText amountCustomer = (CustomEditText) dialog.findViewById(R.id.etamount);
                                                            CustomTextViewBold btnSave = dialog.findViewById(R.id.tvAdd);
                                                            ImageView btnCancel = dialog.findViewById(R.id.tvCancel);
                                                            CustomTextView totalAmounting = dialog.findViewById(R.id.totalAmounting);
                                                            CustomTextView change_amounting = dialog.findViewById(R.id.change_amounting);

                                                            totalAmounting.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getOrderTotal()));
                                                            amountCustomer.requestFocus();
                                                            dialog.show();

                                                            amountCustomer.addTextChangedListener(new TextWatcher() {

                                                                @Override
                                                                public void afterTextChanged(Editable s) {
                                                                }

                                                                @Override
                                                                public void beforeTextChanged(CharSequence s, int start,
                                                                                              int count, int after) {
                                                                }

                                                                @Override
                                                                public void onTextChanged(CharSequence s, int start,
                                                                                          int before, int count) {
                                                                    if (s.length() != 0)
                                                                        change_amounting.setText(Html.fromHtml("&#x20B9;" + (Integer.parseInt(amountCustomer.getText().toString()) - Integer.parseInt(artistBookingsList.get(position).getOrderTotal()))));
//                                                            change_amounting.setText("");
                                                                }
                                                            });

                                                            btnSave.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    if (Integer.parseInt(amountCustomer.getText().toString()) <= Integer.parseInt(artistBookingsList.get(position).getOrderTotal())) {
                                                                        Toast.makeText(context, "Kindly collect more amount then order amount", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        amount = amountCustomer.getText().toString();
                                                                        dialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog.dismiss();
                                                                }
                                                            });

                                                        }
                                                    });
                                                } else if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("2")) {
                                                    if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                                        changeCollection.setVisibility(View.VISIBLE);
                                                    } else {
                                                        changeCollection.setVisibility(View.GONE);
                                                    }
                                                    dialog_paytype.setText("Wallet Payment");
                                                    // tvtotalfinish.setVisibility(View.GONE);
                                                    pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                                    layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                                    tvfinishsubmit.setText("Okay");
                                                    tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.dark_blue_color));

                                                }
                                                dialogfinish.show();


                                                tvtotalfinish.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getOrderTotal()));


                                                tvfinishcancel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        dialogfinish.dismiss();
                                                    }
                                                });


                                                tvfinishsubmit.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //dialogfinish.dismiss();
                                                        // dialogcamera.dismiss();
//                                            Toast.makeText(context, amount, LENGTH_LONG).show();
                                                        tvfinishsubmit.setEnabled(false);
                                                        tvfinishsubmit.setClickable(false);

                                                        deliverybookingupload("3", position, amount);

//                                            dialogfinish.dismiss();
                                                    }
                                                });


                                            } else {
                                                //        ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                            }
                                        } else {
                                            Toast.makeText(context, "Please Upload Photo", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        printbtn = dialogcamera.findViewById(R.id.printbtn);
                        LinearLayout print_linear = dialogcamera.findViewById(R.id.print_linear);
                        tvcameraskip = dialogcamera.findViewById(R.id.tvcameraskip);
                        tvcamera = dialogcamera.findViewById(R.id.tvcamera);
                        tvupload = dialogcamera.findViewById(R.id.tvupload);
                        img_upload = dialogcamera.findViewById(R.id.img_upload);
                        finddevice_btn = dialogcamera.findViewById(R.id.finddevice_btn);
                        imageClose = dialogcamera.findViewById(R.id.imageClose);

                        //   lldauploadImageLayout = dialogcamera.findViewById(R.id.uploadImageLayout);
                        dialogcamera.show();
                        imageClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogcamera.dismiss();
                                //  context.loadHomeFragment(new MyEarning(), "8");
                                newBookings.reloadFragment();
                            }
                        });
                        finddevice_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //  browseBluetoothDevice();
                                // printBluetooth(position);
                            }
                        });

                        printbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                if (!bluetoothAdapter.isEnabled()) {

                                    bluetoothAdapter.enable();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            printBluetooth(position);
                                        }
                                    }, 3000);
                                }
                            }
                        });

                        tvcameraskip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (NetworkManager.isConnectToInternet(context)) {

                                    //printBluetooth(position);
                                    booking("3", pos);
                                    dialogcamera.dismiss();

                                } else {
                                    //       ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                }
                                dialogcamera.dismiss();
                            }
                        });

                        tvupload.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(View v) {

                                if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                        ||
                                        context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    requestPermissions(newBookings.getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 23);
                                } else {


                                    tvupload.setEnabled(false);
                                    tvupload.setClickable(false);
                                    newBookings.opencamrea();
                                }


                                //  lldauploadImageLayout.setVisibility(View.GONE);
                            }
                        });

                        tvcamera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // click_submit_btn_flag = sharedPrefrence.getBooleanValue("click_flag");
                                if (img_upload.getVisibility() == View.GONE) {
                                    Log.e(TAG, "onSlideComplete: btnclick_flag 11 " + btnclick_flag);

                                    Toast.makeText(context, "Please upload the image!!", Toast.LENGTH_SHORT).show();
//                                    tvcamera.setEnabled(false);
//                                    tvcamera.setClickable(false);
                                    return;

                                } else {
                                    Log.e(TAG, "onSlideComplete: btnclick_flag 222 " + btnclick_flag);
                                    boolean flag = sharedPrefrence.getBooleanValue("click_flag");
                                    if (!click_submit_btn_flag) {
                                        click_submit_btn_flag = true;
                                        tvcamera.setEnabled(true);
                                        tvcamera.setClickable(true);
                                        Log.e(TAG, "onClick: click 111-4 ");

                                        // sharedPrefrence.setBooleanValue("click_flag",true);
                                    } else {
                                        Log.e(TAG, "onClick: click 222-4 ");

                                        tvcamera.setEnabled(false);
                                        tvcamera.setClickable(false);
                                    }
                                }
                                tvcamera.setEnabled(false);
                                tvcamera.setClickable(false);
                                OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                                offlineImageDataModal.setBooking_id(artistBookingsList.get(position).getId2());
                                offlineImageDataModal.setImage_path(sharedPrefrence.getValue("pickup_img"));
                                offlineImageDataModal.setTracker(artistBookingsList.get(position).getAppFlag());
                                offlineImageDataModal.setRequest("3");
                                offlineImageDataModal.setUser_id(userDTO.getUser_id());
                                offlineImageDataModal.setService_name(ser_name);
                                offlineImageDataModal.setAmount(artistBookingsList.get(position).getOrderTotal());
                                databaseHandler.updateContact(offlineImageDataModal);

                                Toast.makeText(context, "Image saved in your device!!", Toast.LENGTH_SHORT).show();

                        /*    tvcamera.setEnabled(false);
                            tvcamera.setClickable(false);*/
                                if (img_upload.getVisibility() == View.VISIBLE) {

                                    if (NetworkManager.isConnectToInternet(context)) {

                                        //printBluetooth(position);

                                        dialogcamera.dismiss();
                                        tvtotalfinish = dialogfinish.findViewById(R.id.tvtotalfinish);
                                        pay_done_img = dialogfinish.findViewById(R.id.pay_done_img);
                                        dialog_paytype = dialogfinish.findViewById(R.id.dialog_paytype);
                                        tvfinishsubmit = dialogfinish.findViewById(R.id.tvfinishsubmit);
                                        tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                                        layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);

                                        tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                                        layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);
                                        changeCollection = dialogfinish.findViewById(R.id.changeCollection);


                                   /* if (artistBookingsList.get(position).getShow_print_button().equalsIgnoreCase("1")) {
                                        print_linear.setVisibility(View.VISIBLE);
                                        printbtn.setVisibility(View.VISIBLE);
                                    } else {
                                        print_linear.setVisibility(View.GONE);
                                        printbtn.setVisibility(View.GONE);
                                    }*/


                                        if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("0")) {
                                            if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                                changeCollection.setVisibility(View.VISIBLE);
                                            } else {
                                                changeCollection.setVisibility(View.GONE);
                                            }
                                            dialog_paytype.setText("Onlile Payment");
                                            //   tvtotalfinish.setVisibility(View.GONE);
                                            pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                            //     layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.chat_out));
                                            layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                            tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.dark_blue_color));
                                            tvfinishsubmit.setText("Okay");


                                        } else if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("1")) {
                                            if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                                changeCollection.setVisibility(View.VISIBLE);
                                            } else {
                                                changeCollection.setVisibility(View.GONE);
                                            }

                                            dialog_paytype.setText("Cash Payment");
                                            layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.green));
                                            tvfinishsubmit.setText("Collect");

                                            changeCollection.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final Dialog dialog = new Dialog(context);

                                                    dialog.setContentView(R.layout.dailog_cashout_change);
                                                    dialog.setCancelable(false);
//                                                dialog.setTitle("Custom Alert Dialog");

                                                    final CustomEditText amountCustomer = (CustomEditText) dialog.findViewById(R.id.etamount);
                                                    CustomTextViewBold btnSave = dialog.findViewById(R.id.tvAdd);
                                                    ImageView btnCancel = dialog.findViewById(R.id.tvCancel);
                                                    CustomTextView totalAmounting = dialog.findViewById(R.id.totalAmounting);
                                                    CustomTextView change_amounting = dialog.findViewById(R.id.change_amounting);

                                                    totalAmounting.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getOrderTotal()));
                                                    amountCustomer.requestFocus();
                                                    dialog.show();

                                                    amountCustomer.addTextChangedListener(new TextWatcher() {

                                                        @Override
                                                        public void afterTextChanged(Editable s) {
                                                        }

                                                        @Override
                                                        public void beforeTextChanged(CharSequence s, int start,
                                                                                      int count, int after) {
                                                        }

                                                        @Override
                                                        public void onTextChanged(CharSequence s, int start,
                                                                                  int before, int count) {
                                                            if (s.length() != 0)
                                                                change_amounting.setText(Html.fromHtml("&#x20B9;" + (Integer.parseInt(amountCustomer.getText().toString()) - Integer.parseInt(artistBookingsList.get(position).getOrderTotal()))));
//                                                            change_amounting.setText("");
                                                        }
                                                    });

                                                    btnSave.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (Integer.parseInt(amountCustomer.getText().toString()) <= Integer.parseInt(artistBookingsList.get(position).getOrderTotal())) {
                                                                Toast.makeText(context, "Kindly collect more amount then order amount", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                amount = amountCustomer.getText().toString();
                                                                dialog.dismiss();
                                                            }
                                                        }
                                                    });
                                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                }
                                            });
                                        } else if (artistBookingsList.get(position).getPayType().equalsIgnoreCase("2")) {
                                            if (artistBookingsList.get(position).getChangeTracker().equalsIgnoreCase("1")) {
                                                changeCollection.setVisibility(View.VISIBLE);
                                            } else {
                                                changeCollection.setVisibility(View.GONE);
                                            }
                                            dialog_paytype.setText("Wallet Payment");
                                            // tvtotalfinish.setVisibility(View.GONE);
                                            pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                            layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                            tvfinishsubmit.setText("Okay");
                                            tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.dark_blue_color));

                                        }
                                        dialogfinish.show();


                                        tvtotalfinish.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getOrderTotal()));


                                        tvfinishcancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                dialogfinish.dismiss();
                                            }
                                        });


                                        tvfinishsubmit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //dialogfinish.dismiss();
                                                // dialogcamera.dismiss();
//                                            Toast.makeText(context, amount, LENGTH_LONG).show();
                                                tvfinishsubmit.setEnabled(false);
                                                tvfinishsubmit.setClickable(false);

                                                deliverybookingupload("3", position, amount);

//                                            dialogfinish.dismiss();
                                            }
                                        });


                                    } else {
                                        //        ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                    }
                                } else {
                                    Toast.makeText(context, "Please Upload Photo", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }

                }

            });
            holder.binding.gotopart.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {

//                    Toast.makeText(context, "asasas", Toast.LENGTH_SHORT).show();
                    sendReachNotification(artistBookingsList.get(position).getId(), position, "1");

                }

            });
            holder.binding.gotocust.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                    Log.e(TAG, "sendReachNotification: " + 1);
//                    Toast.makeText(context, "asasas", Toast.LENGTH_SHORT).show();
                    sendReachNotification(artistBookingsList.get(position).getId(), position, "2");

                }

            });

        } else {
            MyViewHolderSection view = (MyViewHolderSection) holderMain;
            //view.tvSection.setText(artistBookingsList.get(position).getSection_name());
        }
    }

    private void getOfflineStore(int position) {

        List<OfflineImageDataModal> imageData = databaseHandler.getAllContacts();

        String[] servname = new String[artistBookingsList.get(position).getProduct().size()];

        for (int l = 0;l<artistBookingsList.get(position).getProduct().size();l++){
            servname[l]= artistBookingsList.get(position).getProduct().get(l).getProductName();
        }
        String ser_name = convertArrayToString(servname);
        Log.e(TAG, "getOfflineStore: comma saperated:-- "+ser_name);
        if (arr.size() > 0) {

            for (int j = 0; j < imageData.size(); j++) {

                if (!arr.contains(artistBookingsList.get(position).getId2())) {

                    arr.add(imageData.get(j).getBooking_id());
                }
            }

        } else {
            for (int j = 0; j < imageData.size(); j++) {
                arr.add(imageData.get(j).getBooking_id());
            }
        }
        if (imageData.size() > 0) {
            Log.e(TAG, "onBindViewHolder: tracker 1 ");
            for (OfflineImageDataModal s : imageData) {

                if (!s.getBooking_id().equalsIgnoreCase(artistBookingsList.get(position).getId2())) {
                    if (arr.contains(artistBookingsList.get(position).getId2())) {
                        Log.e(TAG, "onBindViewHolder: tracker 2 ");

                        return;
                    } else {
                        Log.e(TAG, "onBindViewHolder: tracker 5 ");
                        OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                        offlineImageDataModal.setBooking_id(artistBookingsList.get(position).getId2());
                        offlineImageDataModal.setTracker(artistBookingsList.get(position).getAppFlag());
                        offlineImageDataModal.setService_name(ser_name);
//                        offlineImageDataModal.setService_name(artistBookingsList.get(position).get);
                        databaseHandler.addContact(offlineImageDataModal);
                        Log.e(TAG, "onBindViewHolder: first ");

                    }
                } else if (s.getBooking_id().equalsIgnoreCase(artistBookingsList.get(position).getId2())) {

                    if (s.getTracker().equalsIgnoreCase(artistBookingsList.get(position).getAppFlag())) {
                        Log.e(TAG, "onBindViewHolder: tracker 3 ");
                        return;
                    } else {
                        Log.e(TAG, "onBindViewHolder: tracker 4 ");
                        OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                        offlineImageDataModal.setBooking_id(artistBookingsList.get(position).getId2());
                        offlineImageDataModal.setTracker(artistBookingsList.get(position).getAppFlag());
                        offlineImageDataModal.setService_name(ser_name);
                        databaseHandler.addContact(offlineImageDataModal);
                        Log.e(TAG, "onBindViewHolder: first ");

                    }

                }
            }

        } else {
            Log.e(TAG, "onBindViewHolder: second size zero 6 lya");
            Log.e(TAG, "onBindViewHolder: tracker 6 ");
            OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
            offlineImageDataModal.setBooking_id(artistBookingsList.get(position).getId2());
            offlineImageDataModal.setTracker(artistBookingsList.get(position).getAppFlag());
            offlineImageDataModal.setService_name(ser_name);

            databaseHandler.addContact(offlineImageDataModal);
        }
    }

    private void showAddressDialog(int position) {
        addressBottomSheet = new BottomSheetDialog(context);
        addressBottomSheet.setContentView(R.layout.activity_address_bottom_dialog);
        change_address_btn = addressBottomSheet.findViewById(R.id.change_address_btn);
        //RelativeLayout address_relative = addressBottomSheet.findViewById(R.id.address_relative);
        addressBottomSheet.setCancelable(false);
        RadioGroup add_addressradioGroup;
        ImageView back = addressBottomSheet.findViewById(R.id.address_dialog_close_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBottomSheet.dismiss();

            }
        });

        Typeface font2 = Typeface.createFromAsset(context.getAssets(), "Poppins-Regular.otf");


        CustomTextViewBold ctvbTitle = addressBottomSheet.findViewById(R.id.ctvbTitle);
        CustomTextViewBold select_add_txt = addressBottomSheet.findViewById(R.id.select_add_txt);
        select_add_txt.setVisibility(View.GONE);
        // ctvbTitle.setText("Business Address Verification");
        View add_view1 = addressBottomSheet.findViewById(R.id.add_view1);
        location_etx = addressBottomSheet.findViewById(R.id.location_etx);
        location_etx.setText(street_address_str);


        house_no_etx = addressBottomSheet.findViewById(R.id.house_no_etx);
        society_name_etx = addressBottomSheet.findViewById(R.id.society_name_etx);
        customAddress = addressBottomSheet.findViewById(R.id.customAddress);
        add_addressradioGroup = addressBottomSheet.findViewById(R.id.add_addressradioGroup);
        address_submit = addressBottomSheet.findViewById(R.id.address_submit);


        location_etx.setTypeface(font2);
        house_no_etx.setTypeface(font2);
        society_name_etx.setTypeface(font2);


        if (found_user.equalsIgnoreCase("customer")) {

            addAddressHashmap.put(Consts.USER_ID, artistBookingsList.get(position).getCustomerId());
            house_no_etx.setText(artistBookingsList.get(position).getCustomerHouseNo());
            society_name_etx.setText(artistBookingsList.get(position).getCustomerSocietyName());
            address_id = artistBookingsList.get(position).getCustAddressId();

        } else if (found_user.equalsIgnoreCase("partner")) {

            addAddressHashmap.put(Consts.USER_ID, artistBookingsList.get(position).getPartnerId());
            house_no_etx.setText(artistBookingsList.get(position).getPartnerHouseNo());
            society_name_etx.setText(artistBookingsList.get(position).getPartnerSocietyName());
            address_id = artistBookingsList.get(position).getPartnerAddressId();

        }
        //   addAddressHashmap.put(Consts.USER_ID, artistBookingsList.get(position).getid);
        address_relative = addressBottomSheet.findViewById(R.id.address_relative);
        address_lay_dest_location = addressBottomSheet.findViewById(R.id.address_lay_dest_location);
        // LinearLayout lll = bottomSheetDialog.findViewById(R.id.lll);
        RecyclerView recyclerView = addressBottomSheet.findViewById(R.id.address_recycler);
        recyclerView.setVisibility(View.GONE);
        address_lay_dest_location.setVisibility(View.GONE);
        add_view1.setVisibility(View.GONE);
        customAddress.setVisibility(View.VISIBLE);


        add_addressradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.add_home_radio:
                        address_radio_txt = "Home";
                        // Toast.makeText(mContext, "Home  : --"+address_radio_txt, Toast.LENGTH_SHORT).show();
                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                    case R.id.add_work_radio:
                        address_radio_txt = "Work";
                        // Toast.makeText(mContext, "Work  : --"+address_radio_txt, Toast.LENGTH_SHORT).show();

                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                    case R.id.add_other_radio:
                        address_radio_txt = "Others";
                        // Toast.makeText(mContext, "Others  : --"+address_radio_txt, Toast.LENGTH_SHORT).show();

                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                }
            }
        });

        change_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findPlaceInterface.getLocation();
                address_flag = true;

            }
        });

        address_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                house_no_str = house_no_etx.getText().toString();
                society_name_str = society_name_etx.getText().toString();
                street_address_str = location_etx.getText().toString();
                Log.e("ADDRESS_VALIDATION", " house_no_str " + house_no_str);
                Log.e("ADDRESS_VALIDATION", " society_name_str " + society_name_str);
                Log.e("ADDRESS_VALIDATION", " street_address_str " + street_address_str);

                if (house_no_str.isEmpty() || house_no_str.equals("&nbsp;")) {
                    Toast.makeText(context, "Please enter House no. / Flat no. / Floor / Building", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (society_name_str.equalsIgnoreCase("&nbsp;")) {
                    Toast.makeText(context, "Please enter valid Landmark", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (street_address_str.isEmpty()) {
                    Toast.makeText(context, "Please enter valid Location", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    addAddressHashmap.put("house_no", house_no_str);
                    addAddressHashmap.put("society_name", society_name_str);

                    addAddress();
                    //binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue_color));

                    //etLocationD.setText(house_no_str + ", " + society_name_str + ", " + street_address_str);

                    customAddress.setVisibility(View.GONE);
                    address_relative.setVisibility(View.VISIBLE);
                    address_lay_dest_location.setVisibility(View.VISIBLE);


                    addressBottomSheet.dismiss();

                }

            }
        });

        addressBottomSheet.show();

    }

    public void addAddress() {

        addAddressHashmap.put("address_id", address_id);
        addAddressHashmap.put("addresstype", address_radio_txt);

        if (address_flag) {

            //    Log.e("FIND_ADDRESS_CALLED", " adapter " + CabBookingsFragment.street_address + CabBookingsFragment.user_latitude + CabBookingsFragment.user_longitude);
            addAddressHashmap.put("street_address", CabBookingsFragment.street_address);
            addAddressHashmap.put(Consts.LATITUDE, String.valueOf(CabBookingsFragment.user_latitude));
            addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(CabBookingsFragment.user_longitude));

        } else {

            addAddressHashmap.put("street_address", street_address_str);
            addAddressHashmap.put(Consts.LATITUDE, String.valueOf(live_latitude));
            addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(live_longitude));

        }

        //   Log.e("Live_ADDRESS", "" + address_radio_txt);

        Log.e("addAddressHashmap", "" + addAddressHashmap.toString());

        addAddressHashmap.put("rider_id", userDTO.getUser_id());

        new HttpsRequest(Consts.UPDATE_LOCATION_BY_RIDER, addAddressHashmap, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    //    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    Log.e("update_location", "" + response.toString());
                    addressBottomSheet.dismiss();
                    newBookings.getBookings("8");
                    //    Log.e("ADD_ADDRESS_API", "" + response.toString());
                } else {
                    //   Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendReachNotification(String orderid2, int position, String tracker) {

        HashMap<String, String> temp = new HashMap<>();
        temp.put(Consts.API_NAME, Consts.SEND_REACH_NOTIFICATION);
        temp.put(Consts.ARTIST_ID, userDTO.getUser_id());
        temp.put(Consts.BOOKING_ID, orderid2);
        temp.put("tracker_collection", tracker);
        OfflineDataArrayListModal offlineDataArrayListModal = new OfflineDataArrayListModal();
        Log.e(TAG, "sendReachNotification: " + 2);
        cancelParms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        cancelParms.put(Consts.BOOKING_ID, orderid2);
        cancelParms.put("tracker_collection", tracker);

        offlineDataParms.add(temp);
        offlineDataArrayListModal.setArrayList(offlineDataParms);
        sharedPrefrence.setOfflineData(offlineDataArrayListModal, "offline");

        OfflineDataArrayListModal mmmmm = new OfflineDataArrayListModal();

        mmmmm = sharedPrefrence.getOfflineData("offline");
        Log.e(TAG, "sharedPrefrence: sharedPrefrence " + offlineDataParms.toString());
        System.out.println("sendReachNotification-param" + cancelParms.toString());
//        Toast.makeText(context, "sending==>" + tracker + cancelParms.toString(), LENGTH_LONG).show();
        new HttpsRequest(Consts.SEND_REACH_NOTIFICATION, cancelParms, context).stringPostthree(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e(TAG, "sendReachNotification: " + 22);

//                System.out.println("sendReachNotification-response" + response.toString());
                if (flag) {
                    newBookings.getBookings("9");
                    if (tracker.equals("1")) {
                        Log.e(TAG, "sendReachNotification: " + 3);
//                        Toast.makeText(context, "sending==>" + tracker + response.toString(), LENGTH_LONG).show();
                        /*System.out.println("sendReachNotification-tracker" + "1");*/
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getPartnerLat() + "," + artistBookingsList.get(position).getPartnerLong());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);

//                        Log.e("cancelreason", "" + response.toString());
                    } else {
                        Log.e(TAG, "sendReachNotification: " + 4);
//                        Toast.makeText(context, "sending==>" + tracker + response.toString(), LENGTH_LONG).show();
                        /*System.out.println("sendReachNotification-tracker" + "2");*/
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getUserLat() + "," + artistBookingsList.get(position).getUserLong());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);
                    }

                }
            }
        });

    }

    public void getCancellReasons() {

        cancelParms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        cancelParms.put("role", "1");
        Log.e("SHIVAMCHECKING", "14");
        new HttpsRequest(Consts.GET_CANCEL_REASON_API, cancelParms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("SHIVAMCHECKING", "15");
                    Log.e("cancelreason", "" + response.toString());
                    Type getpetDTO = new TypeToken<List<CancelReasonModel>>() {
                    }.getType();
                    try {
                        cancelList = (ArrayList<CancelReasonModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        ReasonAdapter reasonAdapter = new ReasonAdapter(context, cancelList, new OnReasonSelectedListener() {
                            @Override
                            public void onReasonSelected(int position, String reason) {

                                cancelreason = reason;
                                Log.e("cancelreason", "" + cancelreason);
                            }
                        });
                        cancell_reason_list.setAdapter(reasonAdapter);
                        cancell_reason_list.setExpanded(true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    OnTouch onTouch = new OnTouch() {
        @Override
        public void removeBorder() {

        }
    };


    public void booking(String req, int pos) {

        paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getOrderId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());
        progressDialog.show();
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, context).stringPostthree(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    // ProjectUtils.showToast(context, msg);

                    newBookings.getBookingscopy("3");


                } else {
                    // ProjectUtils.showToast(context, msg);
                }


            }
        });
    }

    public void bookingupload(String req, int pos, String amountCust) {

        paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getOrderId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());
        paramsBookingOp.put(Consts.AMOUNT, amountCust);
        progressDialog.show();
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, context).stringPostthree(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();

                Log.e(Consts.BOOKING_OPERATION_API, response.toString());
                if (flag) {
                    dialogfinish.dismiss();
//                    OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
//                    offlineImageDataModal.setBooking_id(artistBookingsList.get(pos).getOrderId());
//                    offlineImageDataModal.setImage_path(sharedPrefrence.getValue("pickup_img"));
//                    offlineImageDataModal.setTracker(artistBookingsList.get(pos).getAppFlag());
//                    databaseHandler.deleteContact(offlineImageDataModal);
//
//                    List<OfflineImageDataModal> imageData = databaseHandler.getAllContacts();

//                    Log.e("imageData offline", " imageData position:-- "+ pos + offlineImageDataModal.getImage_path());
                    //        Log.e("imageData offline", " imageData " + imageData.size());
                    //  Log.e("imageData offline", " imageData " + imageData.get(imageData.size()-1).getBooking_id());

                    Log.e("pickup_tracker", " pickupbookingupload called " + response.toString());

                    newBookings.reloadFragment();
                    //newBookings.getBookingscopy("3");
                } else {
                }
            }
        });
    }


    public void deliverybookingupload(String req, int pos, String amountCust) {

//        HashMap<String,String> temp = new HashMap<>();
        HashMap<String, String> paramsBookingOp2 = new HashMap<>();
//        temp.put(Consts.API_NAME,Consts.PICKUP_BOOKING_OPERATION_API);
//        temp.put(Consts.ARTIST_ID, userDTO.getUser_id());
//        temp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId2());
//        temp.put(Consts.REQUEST, req);
//        temp.put(Consts.USER_ID, userDTO.getUser_id());
//        temp.put(Consts.AMOUNT, amountCust);
//        OfflineDataArrayListModal offlineDataArrayListModal = new OfflineDataArrayListModal();
        paramsBookingOp2.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId2());
        paramsBookingOp2.put(Consts.REQUEST, req);
        paramsBookingOp2.put(Consts.USER_ID, userDTO.getUser_id());

//        offlineDataParms.add(temp);
//        offlineDataArrayListModal.setArrayList(offlineDataParms);

//        sharedPrefrence.setOfflineData(offlineDataArrayListModal,"offline");

        OfflineDataArrayListModal mmmmm = new OfflineDataArrayListModal();
        mmmmm = sharedPrefrence.getOfflineData("offline");

        bookingupload("3", pos, amountCust);

        Log.e(TAG, "sharedPrefrence: sharedPrefrence " + mmmmm.getArrayList().toString());

        Log.e("paramsBookingOP", " pickup " + paramsBookingOp.toString() + " newBookings.paramsFile " + newBookings.paramsFile);
        progressDialog.show();
        new HttpsRequest(Consts.PICKUP_BOOKING_OPERATION_API, paramsBookingOp2, newBookings.paramsFile, context).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();

                if (flag) {
                    dialogcamera.dismiss();

                    OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                    offlineImageDataModal.setBooking_id(artistBookingsList.get(pos).getId2());
                    offlineImageDataModal.setImage_path(sharedPrefrence.getValue("pickup_img"));
                    offlineImageDataModal.setTracker(artistBookingsList.get(pos).getAppFlag());
                    databaseHandler.deleteContact(offlineImageDataModal);

                    List<OfflineImageDataModal> imageData = databaseHandler.getAllContacts();

//                    Log.e("imageData offline", " imageData position:-- "+ pos + offlineImageDataModal.getImage_path());
                    Log.e("imageData offline", " imageData " + imageData.size());
                    //  Log.e("imageData offline", " imageData " + imageData.get(imageData.size()-1).getBooking_id());

                    Log.e("pickup_tracker", " pickupbookingupload called " + response.toString());
//                    bookingstart("2", pos, String.valueOf(sdf1.format(date)));

                    // ProjectUtils.showToast(context, msg);
                    // newBookings.getBookingscopy("3");

                } else {
                    // ProjectUtils.showToast(context, msg);
                }


            }
        });
    }

    public void pickupbookingupload(String req, int pos) {

        HashMap<String, String> temp = new HashMap<>();
        temp.put(Consts.API_NAME, Consts.PICKUP_BOOKING_OPERATION_API);
        temp.put(Consts.ARTIST_ID, userDTO.getUser_id());
        temp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        temp.put(Consts.REQUEST, req);
        OfflineDataArrayListModal offlineDataArrayListModal = new OfflineDataArrayListModal();
        paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());


        offlineDataParms.add(temp);
        offlineDataArrayListModal.setArrayList(offlineDataParms);

        sharedPrefrence.setOfflineData(offlineDataArrayListModal, "offline");

        OfflineDataArrayListModal mmmmm = new OfflineDataArrayListModal();
        mmmmm = sharedPrefrence.getOfflineData("offline");

        bookingstart("2", pos, String.valueOf(sdf1.format(date)));

        Log.e(TAG, "sharedPrefrence: sharedPrefrence " + mmmmm.getArrayList().toString());

        Log.e("paramsBookingOP", " pickup " + paramsBookingOp.toString() + " newBookings.paramsFile " + newBookings.paramsFile);
//        if (con)

        Log.e(TAG, "pickupbookingupload: 111 params:-- " + paramsBookingOp.toString());
        progressDialog.show();
        new HttpsRequest(Consts.PICKUP_BOOKING_OPERATION_API, paramsBookingOp, newBookings.paramsFile, context).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();

                if (flag) {
                    dialogcamera.dismiss();

                    OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                    offlineImageDataModal.setBooking_id(artistBookingsList.get(pos).getId2());
                    offlineImageDataModal.setImage_path(sharedPrefrence.getValue("pickup_img"));
                    offlineImageDataModal.setTracker(artistBookingsList.get(pos).getAppFlag());
                    databaseHandler.deleteContact(offlineImageDataModal);

                    List<OfflineImageDataModal> imageData = databaseHandler.getAllContacts();

//                    Log.e("imageData offline", " imageData position:-- "+ pos + offlineImageDataModal.getImage_path());
                    Log.e("imageData offline", " imageData " + imageData.size());
                    //  Log.e("imageData offline", " imageData " + imageData.get(imageData.size()-1).getBooking_id());

                    Log.e("pickup_tracker", " pickupbookingupload called " + response.toString());
//                    bookingstart("2", pos, String.valueOf(sdf1.format(date)));

                    // ProjectUtils.showToast(context, msg);
                    // newBookings.getBookingscopy("3");

                } else {
                    // ProjectUtils.showToast(context, msg);
                }


            }
        });
    }

    private BluetoothConnection selectedDevice;

    public void browseBluetoothDevice() {
        Log.e("Bluetooth_request", "3");
        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

        if (bluetoothDevicesList != null) {

            Log.e("Bluetooth_request", "4");
            final String[] items = new String[bluetoothDevicesList.length + 1];
            items[0] = "Default printer";
            int i = 0;
            for (BluetoothConnection device : bluetoothDevicesList) {
                items[++i] = device.getDevice().getName();
                Log.e("Bluetooth_request", "4 " + bluetoothDevicesList.length + " " + bluetoothDevicesList.toString());

            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Bluetooth printer selection");
            alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int index = i - 1;
                    if (index == -1) {
                        selectedDevice = null;
                    } else {
                        selectedDevice = bluetoothDevicesList[index];
                    }
                    //Button button = (Button) context.findViewById(R.id.button_bluetooth_browse);
                    //button.setText(items[i]);
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();

        } else {
            Log.e("Bluetooth_request", "5");
        }
    }

    public void printBluetooth(int pos) {
        Log.e("Bluetooth_request", "1");

        // BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.e("Bluetooth_request", "2");

        if (selectedDevice == null) {

            //bluetoothAdapter.enable();
            //  browseBluetoothDevice();
            //    new AsyncBluetoothEscPosPrint(context).execute(getAsyncEscPosPrinter(selectedDevice, pos));

           /* Log.e("Bluetooth_request", "3");

            requestPermissions(newBookings.getActivity(), new String[]{Manifest.permission.BLUETOOTH}, 109);*/
        } else {
            //    new AsyncBluetoothEscPosPrint(context).execute(getAsyncEscPosPrinter(selectedDevice, pos));

        }
    }

   /* @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, int pos) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        String paymnt_mode = "";

        if (artistBookingsList.get(pos).getPayType().equalsIgnoreCase("0")) {
            paymnt_mode = "Online Payment";

        } else if (artistBookingsList.get(pos).getPayType().equalsIgnoreCase("1")) {
            paymnt_mode = "Cash Payment";

        } else if (artistBookingsList.get(pos).getPayType().equalsIgnoreCase("2")) {
            paymnt_mode = "Wallet Payment";
        }


        String ty = "";
        double price = Double.parseDouble(artistBookingsList.get(pos). ())
        +Double.parseDouble(artistBookingsList.get(pos).getMy_discount());
        String price_with_dis = String.valueOf(price);
        productDTOArrayList = new ArrayList<>();
        productDTOArrayList = artistBookingsList.get(pos).getProduct();

        *//*
          ty += "[L]<b>" + productDTOArrayList.get(i).getProduct_name() + "[R]₹" + productDTOArrayList.get(i).getProduct_price() + "</b>\n" +
                    "[L]QTY: " + productDTOArrayList.get(i).getQty() + "[R]" + productDTOArrayList.get(i).getDiscount() + "% OFF</b>\n\n";*//*

        for (int i = 0; i < productDTOArrayList.size(); i++) {

            Log.e("price", "" + productDTOArrayList.get(i).getPrice());
            ty += "[L]<b>" + productDTOArrayList.get(i).getProduct_name() + "</b>\n" +
                    "[L]<font size='normal'>" + productDTOArrayList.get(i).getQty_description() + "</font>\n" +
                    "[L]QTY: " + productDTOArrayList.get(i).getQty() + "[R]<b>RS " + productDTOArrayList.get(i).getProduct_price() + "</b>\n\n" +
                   *//* "[L]<b>Batch No : [R]070122/01</b>\n"+
                    "[L]<b>Pkg Date : [R]07 Jan 2022</b>\n"+
                    "[L]<b>Exp. Date : [R]21 Feb 2022</b>\n\n"+
                    "[L]<b>Ingredients : </b>\n"+
                    "[L]<b>RICE FLOUR, CHICKPEA FLOUR, BUTTER, VAGETABLE OIL, GARAM MASALA, RED CHILLI.</b>\n\n" +
                    "[L]<b>Nutrition Value Per 100 Gram : </b>\n\n"+
                    "[L]<b>Energy : [R]100 Kacal</b>\n"+
                    "[L]<b>Carbohydrate : [R]100 Gram</b>\n"+
                    "[L]<b>Protein : [R]80 Gram</b>\n"+
                    "[L]<b>Sugar : [R]60 Gram</b>\n"+
                    "[L]<b>Total Fats : [R]20 Gram</b>\n"+
                   *//*
                    "[C]--------------------------------\n";
        }
        Log.e("SHIVAKASHI", ty);
        //   booking("3", pos);
        //   dialogcamera.dismiss();
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.setTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, getApplicationContext().getResources().getDrawableForDensity(R.drawable.kamaii_final_logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        //"[C]<u><font size='big'>Kamaii</font></u>\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[L]\n" +
                        "[L]Order Id:     " + "#" + artistBookingsList.get(pos).getOrder_no() + "\n" +
                        "[L]Time:      " + artistBookingsList.get(pos).getOrder_date_time() + "\n" +
                        "[L]Payment Mode: [R]" + paymnt_mode + "\n" +
                        "[C]--------------------------------\n" +
                        "[L]<b>Delivery Address</b>\n\n" +
                        "[L]<b>" + artistBookingsList.get(pos).getUserName() + ",</b>\n" +
                        "[L]<b>" + artistBookingsList.get(pos).getCust_house_no() + "," + artistBookingsList.get(pos).getCust_street_address() + "</b>\n" +
                        //   "[L]LandMark: " + artistBookingsList.get(pos).getCust_society_no() + "\n\n" +
                        "[L]<b>Mobile Number: " + artistBookingsList.get(pos).getCustomerMobile() + "</b>\n" +
                        "[C]--------------------------------\n" +
                      *//*  "[L]<b>Your order is completed</b>\n" +
                        "[L]Deliver Time: [R]" + artistBookingsList.get(pos).getEnd_time() + "\n\n" +
                       *//*

                        ty +

                     *//*   "[L]<b>Masala Khakhra[R]₹80</b>\n" +
                        "[L]QTY: 1(500 Grams)[R]15% OFF</b>\n\n" +
*//*
     *//* "[L]<b>Ingradients: RICE FLOUR, CHICKPEA FLOUR, BUTTER, VAGETABLE OIL, GARAM MASALA, RED CHILLI</b>\n\n" +
     *//**//*
                        "[L]QTY: 1(500 Grams)[R]15% OFF</b>\n\n" +
*//*
                        // "[C]<u>You will save ₹" + artistBookingsList.get(pos).getMy_discount() + " on this order</u></b>\n\n" +
                        "[L]<b>Order Detail</b>\n" +
                        "[L]Total Price (" + productDTOArrayList.size() + " items)[R]RS <b>" + price_with_dis + "</b>\n" +
                        // "[L]Discount[R]<b>₹-" + artistBookingsList.get(pos).getMy_discount() + "</b>\n" +
                        "[L]Shipping Charge[R]<b>RS " + String.valueOf(Double.parseDouble(artistBookingsList.get(pos).getShipping_charges())) + "</b>\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[L]\n" +
                        "[L]Order Amount[R]<b>RS " + artistBookingsList.get(pos).getActuallcollectamount() + "</b>\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[L]\n" +
                        "[C]Thank you for Order!\n"
                        + "[C] Keep Ordering :)");
    }
*/
   /* @Override
    public int getItemViewType(int position) {
       // return artistBookingsList.get(position).isSection() ? VIEW_SECTION : VIEW_ITEM;
    }*/

    @Override
    public int getItemCount() {
        return artistBookingsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCabBookingsBinding binding;

        public MyViewHolder(AdapterCabBookingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    public void bookingstart(String req, int pos, String datetime) {
        getBookingscopy("3", req, pos, datetime);
        /*paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.APPROXTIME, datetime);
        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());
        //  progressDialog.show();

        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, context).stringPostthree(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //   progressDialog.dismiss();
                if (flag) {
                    //    ProjectUtils.showToast(context, msg);
                    Log.e("pickup_tracker", " bookingstart called " + response.toString());
                    getBookingscopy("3", req, pos, datetime);
                } else {
                    //   ProjectUtils.showToast(context, msg);
                }


            }
        });*/
    }

    public void getBookingscopy(final String position, String req, int pos, String datetime) {
        //      progressDialog.show();

        HashMap<String, String> temp = new HashMap<>();
        temp.put(Consts.API_NAME, "booking_operation");
        temp.put(Consts.ARTIST_ID, userDTO.getUser_id());
        temp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        temp.put(Consts.REQUEST, req);
        OfflineDataArrayListModal offlineDataArrayListModal = new OfflineDataArrayListModal();
        offlineDataParms.add(temp);
        offlineDataArrayListModal.setArrayList(offlineDataParms);

        sharedPrefrence.setOfflineData(offlineDataArrayListModal, "offline");

        OfflineDataArrayListModal mmmmm = new OfflineDataArrayListModal();
        mmmmm = sharedPrefrence.getOfflineData("offline");
        Log.e(TAG, "sharedPrefrence: sharedPrefrence " + mmmmm.getArrayList().toString());
        Log.e("pickup_tracker", " getBookingscopy ");

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.booking_operationCopy(artistBookingsList.get(pos).getId(), req, datetime, userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //            progressDialog.dismiss();
                //       binding.tvNo.setVisibility(View.GONE);
//                binding.rvBooking.setVisibility(View.VISIBLE);
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("tracker123456", "6" + s);

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
                                newBookings.reloadFragment();
//                                newBookings.getArtistcpoy();
//                                newBookings.setMainAdapter();
//                                newBookings.showDataOrders();
                                //   showData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            //artistBookingsList = new ArrayList<>();
                            newBookings.getArtistcpoy();
                            Log.e("pickup_tracker", " getBookingscopy called else");
                            //     showData();
                        }


                    } else {

                        /*newBookings.binding.rvBooking.setVisibility(View.GONE);
                        newBookings.binding.rlSearch.setVisibility(View.GONE);
                        newBookings.binding.lay.setVisibility(View.VISIBLE);*/

                        Log.e("tracker123456", "6 else");

                        /*Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();*/


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
//                        LENGTH_LONG).show();


            }
        });


    }

    public void decline(int pos, String reason) {
        progressDialog.show();
        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        paramsDecline.put(Consts.DECLINE_BY, "3");
        paramsDecline.put(Consts.DECLINE_REASON, reason);
        paramsDecline.put("cat_id", "0");
        paramsDecline.put("sub_id", "0");
        paramsDecline.put("third_id", "0");
        paramsDecline.put("lat", artistBookingsList.get(pos).getUserLat());
        paramsDecline.put("lang", artistBookingsList.get(pos).getUserLong());
        paramsDecline.put("passvalue", "0");


        new HttpsRequest(Consts.DECLINE_ULTIMATE_BOOKING_API, paramsDecline, context).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    // ProjectUtils.showToast(context, msg);

                    try {
                        MediaPlayer mediaPlayer = apiClient.getMediaPlayer(context);
                        mediaPlayer.reset();
                        AssetFileDescriptor descriptor = context.getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    newBookings.getBookingscopy("3");

                } else {
                    //  ProjectUtils.showToast(context, msg);
                }


            }
        });
    }

    public static class MyViewHolderSection extends RecyclerView.ViewHolder {
        public CustomTextView tvSection;

        public MyViewHolderSection(View view) {
            super(view);
            tvSection = view.findViewById(R.id.tvSection);
        }

    }
}
