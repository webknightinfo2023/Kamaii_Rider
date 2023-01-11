package com.kamaii.rider.ui.adapter;

import static android.util.Log.e;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.AdapterNewCabBookingLayoutBinding;
import com.kamaii.rider.interfacess.FindPlaceInterface;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.fragment.CabBookingsFragment;
import com.kamaii.rider.ui.models.ArtistBookingNew;
import com.kamaii.rider.ui.models.CancelReasonModel;
import com.kamaii.rider.ui.models.neworder.NewOrderModel;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ExpandableHeightGridView;
import com.tsuryo.androidcountdown.Counter;
import com.tsuryo.androidcountdown.TimeUnits;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterNewCabBooking extends RecyclerView.Adapter<AdapterNewCabBooking.CabBookingViewholder> implements FindPlaceInterface {

    private CabBookingsFragment newBookings;
    private ArrayList<NewOrderModel> artistBookingsList;
    private ArrayList<NewOrderModel> searchBookingsList;
    private UserDTO userDTO;
    private LayoutInflater myInflater;
    private Context context;
    private HashMap<String, String> paramsBookingOp;
    private HashMap<String, String> paramsDecline;
    HashMap<String, String> requestParams;

    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<ProductDTO> productDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private Dialog dialog, dialogcustomservice, dialogdecline, dialogapproxtime, dialogcamera, dialogpriview, dialogfinish, dailograting;
    private CustomTextViewBold finddevice_btn, printbtn, tvdCancelreason, tvdAddreason, tvcameraskip, ctvbTitle, tvcamera, tvupload, tvtotalfinish, dialog_paytype, tvfinishsubmit, tvfinishsubmitrating;
    String locationstatus = "";
    ImageView tvfinishcancel, pay_done_img;
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
    String cancelreason = "";
    public ProgressDialog progressDialog;
    LinearLayout layfinishbackground;
    private RatingBar rbReview;
    private float myrating;
    public static String pay_type = null;
    ExpandableHeightGridView cancell_reason_list;
    private HashMap<String, String> cancelParms = new HashMap<>();
    List<CancelReasonModel> cancelList;
    public BottomSheetDialog addressBottomSheet;
    public CustomTextView no_rider_found_txt, change_address_btn;
    public CustomTextViewBold address_submit;
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
    EditText dialogPaymentEtx;
    public boolean refresh_booking_flag = false;
    BaseActivity baseActivity;
    public AdapterNewCabBooking(CabBookingsFragment newBookings, ArrayList<NewOrderModel> artistBookingsList, UserDTO userDTO, LayoutInflater myInflater, String addrs, double lat, double longi, FindPlaceInterface findPlaceInterface, boolean refresh_booking_flag, BaseActivity baseActivity) {

        this.newBookings = newBookings;
        this.artistBookingsList = artistBookingsList;
        Log.e("rider_123", " adapter " + artistBookingsList.size());
        Log.e("SHIVAMCHECKING", "12");
        this.searchBookingsList = new ArrayList<NewOrderModel>();
        this.searchBookingsList.addAll(artistBookingsList);
        this.myInflater = myInflater;
        this.userDTO = userDTO;
        this.baseActivity = baseActivity;
        this.refresh_booking_flag = refresh_booking_flag;
        cancelList = new ArrayList<>();
        context = newBookings.getActivity();
        street_address_str = addrs;
        Log.e("street_address_str1234", "" + street_address_str);
        live_latitude = lat;
        live_longitude = longi;
        this.findPlaceInterface = findPlaceInterface;
    }

    @NonNull
    @Override
    public CabBookingViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterNewCabBookingLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_new_cab_booking_layout, parent, false);
        return new CabBookingViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CabBookingViewholder holder, int position) {
        holder.binding.newOrderRv.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.customerslottime.setText(artistBookingsList.get(position).getRider_end_slot());

        AdapterCabBookings adapterAllBookings = new AdapterCabBookings(newBookings, artistBookingsList.get(position).getAllBooking(), userDTO, myInflater, street_address_str, live_latitude, live_longitude, this, refresh_booking_flag,baseActivity);
        holder.binding.newOrderRv.setAdapter(adapterAllBookings);

        if (artistBookingsList.get(position).getSlot_active().equalsIgnoreCase("0")) {
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.disabled_border));
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.disable_btn_color));
        } else {
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.green_border_bg));
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_green_bg));
        }
        if (artistBookingsList.get(position).getEndTimeTracker() == 0) {
            holder.binding.counter.setVisibility(View.VISIBLE);

            Log.e("endtime", "" + artistBookingsList.get(position).getSlotTime());
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            e("tracker", "1");
            Date date = null;
            try {
                e("tracker", "2");

                //   date = format.parse("2022-02-12T18:00:00");
                date = format.parse(artistBookingsList.get(position).getSlotTime());
                holder.binding.counter.setDate(date);//countdown starts
                e("tracker", "3");


            } catch (ParseException e) {
                e.printStackTrace();
            }
            //holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

            holder.binding.counter.setIsShowingTextDesc(true);
            holder.binding.counter.setTextSize(35);
            holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
            holder.binding.counter.setTextColor(context.getResources().getColor(R.color.green));
            e("tracker", "4");

            holder.binding.counter.setListener(new Counter.Listener() {
                @Override
                public void onTick(long millisUntilFinished) {
//                    e("TAG", "onTick: Counter - " + millisUntilFinished);

                    if (millisUntilFinished == 0) {
                        holder.binding.counter.setVisibility(View.GONE);
                        holder.binding.preparationTimeFirst.setVisibility(View.VISIBLE);
                    }
                    e("tracker", "5");

                }

                @Override
                public void onTick(long days, long hours, long minutes, long seconds) {
                    //     Log.e("tracker", "6");

               /*     Log.e("TAG", "onTick" + days + "D " +
                            hours + "H " +
                            minutes + "M " +
                            seconds + "S ");*/

                    String newtime = "";

                    if (days == 00 && hours == 00) {
                        newtime =
                                minutes + "M " +
                                        seconds + "S ";
                    } else if (days == 00 && hours != 00) {

                        newtime =
                                hours + "H " + minutes + "M " + seconds + "S ";
                    } else {
                    }
                    //      Log.e("tracker", "7" + newtime);

                    holder.binding.preparationTimeFirst.setText(newtime);

                }
            });

        } else {
            holder.binding.counter.setVisibility(View.GONE);
            holder.binding.customerslottime.setVisibility(View.GONE);
            holder.binding.orderDelayTimer.setVisibility(View.VISIBLE);
            // holder.binding.orderDelayTimer.setText(orderList.get(position).getEnd_time_diff());

            int stoppedMilliseconds = 0;

            String chronoText = artistBookingsList.get(position).getEndTimeDiff();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                        + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                        + Integer.parseInt(array[1]) * 60 * 1000
                        + Integer.parseInt(array[2]) * 1000;
            }

            holder.binding.orderDelayTimer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
            holder.binding.orderDelayTimer.start();
            holder.binding.orderStatusFirst.setText("Preparing order delayed by");
            holder.binding.preparationTimeFirst.setVisibility(View.VISIBLE);
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.red_border_bg));
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_red_bg));
            holder.binding.infoFirst.setImageTintList(context.getResources().getColorStateList(R.color.red));

        }

    }

    @Override
    public int getItemCount() {
        return artistBookingsList.size();
    }

    @Override
    public void getLocation() {

        newBookings.findPlace();
    }


    class CabBookingViewholder extends RecyclerView.ViewHolder {
        AdapterNewCabBookingLayoutBinding binding;

        public CabBookingViewholder(@NonNull AdapterNewCabBookingLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
