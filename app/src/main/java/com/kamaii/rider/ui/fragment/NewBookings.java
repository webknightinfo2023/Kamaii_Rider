package com.kamaii.rider.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentNewBookingsBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.PrintInvoiceOrderDetails;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.printer.AsyncBluetoothEscPosPrint;
import com.kamaii.rider.printer.AsyncEscPosPrinter;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AdapterAllBookings;
import com.kamaii.rider.ui.adapter.AdapterAllBookingsOld;
import com.kamaii.rider.ui.models.ArtistBookingOld;
import com.kamaii.rider.utils.FileUtility;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class NewBookings extends Fragment implements View.OnClickListener, PrintInvoiceOrderDetails {

    private BaseActivity baseActivity;
    private FragmentNewBookingsBinding binding;
    private ArrayList<ArtistBooking> artistBookingsList;
    private ArrayList<ArtistBooking> artistBookingsListnew = new ArrayList<>();
    private ArrayList<ArtistBookingOld> artistBookingsListold;
    private ArrayList<ArtistBookingOld> artistBookingsListoldnew = new ArrayList<>();
    private ArrayList<ArtistBooking> artistBookingsListSection;
    private ArrayList<ArtistBooking> artistBookingsListSection1;
    private ArrayList<ProductDTO> productDTOArrayList = new ArrayList<>();
    private HashMap<String, String> parmsversion = new HashMap<>();
    private LinearLayoutManager mLayoutManager;
    private String TAG = NewBookings.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> parmsartist = new HashMap<>();
    private LayoutInflater myInflater;
    private LayoutInflater myInflaterOld;
    private AdapterAllBookings adapterAllBookings;
    private AdapterAllBookingsOld adapterAllBookingsOld;
    private ArtistDetailsDTO artistDetailsDTO;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean cameraAccepted, storageAccepted, accessNetState, fineLoc, corasLoc;
    private SharedPrefrence prefference;
    Context mContext;
    public static HashMap<String, File> paramsFile;
    public static Uri uri = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HashMap<String, String> paramsUpdate;
    int current_page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_bookings, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        myInflater = LayoutInflater.from(getActivity());
        myInflaterOld = LayoutInflater.from(getActivity());
        parmsartist.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.USER_ID, userDTO.getUser_id());

        mContext = getActivity();
        prefference = SharedPrefrence.getInstance(mContext);

        paramsFile = new HashMap<>();

        setUiAction();

        return binding.getRoot();
    }


    private void setUiAction() {
        binding.tvStatus.setText(getResources().getString(R.string.pending));

        baseActivity.headerNameTV.setText(getResources().getString(R.string.bookings));
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.rvBooking.setLayoutManager(mLayoutManager);
        binding.rvBookingOld.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.newbookingScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    current_page++;
                    //   Log.e("CURRENT_PAGE", "" + current_page + " count "+ totalItemCount);
                    Log.e("CURRENT_PAGE", "" + current_page);
                    // getBookings2(current_page);
                    binding.progressbarRv.setVisibility(View.VISIBLE);

                    getBookingsCopy(current_page);
                }
            }
        });

        binding.tvPendings.setOnClickListener(this);
        binding.tvAccepted.setOnClickListener(this);
        binding.tvRejected.setOnClickListener(this);
        binding.tvCompleted.setOnClickListener(this);
        binding.tvcurrent.setOnClickListener(this);

        binding.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    adapterAllBookings.filter(newText.toString());
                    adapterAllBookingsOld.filter(newText.toString());

                } else {
                    parms.put(Consts.BOOKING_FLAG, "4");
                    getBookings(current_page);

                }
                return false;
            }
        });


        //binding.tvCompleted.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line_two));
        binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
        binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
        binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));
        binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));

        binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
        binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
        binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
        binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));
        binding.tvCompleted.setTextColor(getResources().getColor(R.color.white));


        binding.tvStatus.setText(getResources().getString(R.string.com));

        if (NetworkManager.isConnectToInternet(getActivity())) {
            // getArtist();
            parms.put(Consts.BOOKING_FLAG, "4");
            getBookings(current_page);


        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }


    }

    public void getBookings(int range) {

        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put("range", String.valueOf(range));

        binding.progressbarRv.setVisibility(View.GONE);
        ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_BOOKING_ARTIST_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                if (flag) {
                    Log.e("api_res", "" + response.toString());
                    binding.tvNo.setVisibility(View.GONE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.rvBookingOld.setVisibility(View.VISIBLE);
                    binding.rlSearch.setVisibility(View.VISIBLE);
                    try {
                        artistBookingsList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                        }.getType();
                        artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        for (int i = 0;i<artistBookingsList.size();i++){
                            artistBookingsListnew.add(artistBookingsList.get(i));
                        }

                        if (artistBookingsListnew.size() > 6){
                            binding.progressbarRv.setVisibility(View.VISIBLE);

                        }

                        showData();
                      //  getBookingsold(current_page);
                       // showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    binding.progressbarRv.setVisibility(View.GONE);

                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.GONE);
                    binding.rvBookingOld.setVisibility(View.GONE);

                    binding.rlSearch.setVisibility(View.GONE);

                }
            }
        });


    }


    public void getBookingsCopy(int range) {

        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put("range", String.valueOf(range));

        new HttpsRequest(Consts.GET_ALL_BOOKING_ARTIST_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                if (flag) {
                    Log.e("api_res", "" + response.toString());
                    binding.tvNo.setVisibility(View.GONE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.rvBookingOld.setVisibility(View.VISIBLE);
                    binding.rlSearch.setVisibility(View.VISIBLE);
                    try {
                        artistBookingsList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                        }.getType();
                        artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        for (int i = 0;i<artistBookingsList.size();i++){
                            artistBookingsListnew.add(artistBookingsList.get(i));
                        }

                       /* if (artistBookingsListnew.size() > 6){
                            binding.progressbarRv.setVisibility(View.VISIBLE);

                        }*/

                        showData();
                      //  getBookingsold(current_page);
                       // showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    if(artistBookingsListnew.size()!=0){
                        binding.progressbarRv.setVisibility(View.GONE);

                        showData();
                    }else {



                    binding.progressbarRv.setVisibility(View.GONE);

                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.GONE);
                    binding.rvBookingOld.setVisibility(View.GONE);

                    binding.rlSearch.setVisibility(View.GONE);
                    }

                }
            }
        });


    }

    public void getBookingsold(int range) {

        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put("range", String.valueOf(range));

        ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_BOOKING_ARTIST_OLD_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                if (flag) {
                    Log.e("api_res", "" + response.toString());
                    binding.tvNo.setVisibility(View.GONE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.rvBookingOld.setVisibility(View.VISIBLE);
                    binding.rlSearch.setVisibility(View.VISIBLE);
                    try {
                        artistBookingsListold = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ArtistBookingOld>>() {
                        }.getType();
                        artistBookingsListold = (ArrayList<ArtistBookingOld>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        for (int i = 0;i < artistBookingsListold.size();i++){
                            artistBookingsListoldnew.add(artistBookingsListold.get(i));
                        }
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.GONE);
                    binding.rvBookingOld.setVisibility(View.GONE);

                    binding.rlSearch.setVisibility(View.GONE);

                }
            }
        });


    }

    public void showData() {
//        Collections.reverse(artistBookingsList);
        adapterAllBookings = new AdapterAllBookings(NewBookings.this, artistBookingsListnew, userDTO, myInflater,this);
        binding.rvBooking.setAdapter(adapterAllBookings);

       /* Collections.reverse(artistBookingsListold);
        adapterAllBookingsOld = new AdapterAllBookingsOld(NewBookings.this, artistBookingsListoldnew, userDTO, myInflaterOld);
        binding.rvBookingOld.setAdapter(adapterAllBookingsOld);*/
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvPendings:


                binding.tvPendings.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));

                binding.tvPendings.setTextColor(getResources().getColor(R.color.white));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));


                binding.tvStatus.setText(getResources().getString(R.string.pending));
                parms.put(Consts.BOOKING_FLAG, "0");
                getBookings(current_page);
                break;
            case R.id.tvcurrent:


                binding.tvcurrent.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));


                binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.white));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.black));

                binding.tvStatus.setText(getResources().getString(R.string.curent));
                parms.put(Consts.BOOKING_FLAG, "3");
                getBookings(current_page);
                break;
            case R.id.tvAccepted:


                binding.tvAccepted.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));


                binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.white));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));

                binding.tvStatus.setText(getResources().getString(R.string.acc));
                parms.put(Consts.BOOKING_FLAG, "1");
                getBookings(current_page);
                break;
            case R.id.tvRejected:
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line_two));

                binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));


                binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.white));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));

                artistBookingsListnew = new ArrayList<>();
                current_page = 0;
                binding.tvStatus.setText(getResources().getString(R.string.rej));
                parms.put(Consts.BOOKING_FLAG, "2");
                getBookings(current_page);
                break;
            case R.id.tvCompleted:
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line_two));
                binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));

                binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.white));

                artistBookingsListnew = new ArrayList<>();
                current_page = 0;
                binding.tvStatus.setText(getResources().getString(R.string.com));
                parms.put(Consts.BOOKING_FLAG, "4");
                getBookings(current_page);
                break;

        }

    }


    public void opencamrea() {


        if (!hasPermissions(getActivity(), permissions)) {
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            getPhotoFromCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                try {

                    cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, cameraAccepted);

                    storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, storageAccepted);

                    accessNetState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, accessNetState);

                    fineLoc = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.FINE_LOC, fineLoc);

                    corasLoc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CORAS_LOC, corasLoc);
                    getPhotoFromCamera();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void getPhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


        File file = new File(Environment.getExternalStorageDirectory(), "OkyBookPartner" + String.valueOf(System.currentTimeMillis()) + ".png");
        disableException();
        uri = Uri.fromFile(file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);

    }

    public void disableException() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
               /* if (data != null) {
                    Uri imageUri = data.getData();
                    try {


                        Glob.cameragalleryselectedimage1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);



                        Intent intent = new Intent(NextActivity.this, CropActivity.class);
                        startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
            } else if (requestCode == 3) {
                // Uri imageUri = data.getData();


                String filepath = FileUtility.getPath(getActivity(), uri);
                FileUtility fileUtility = new FileUtility();
                filepath = fileUtility.compressImage(getActivity(), filepath);
                File file = new File(filepath);
                paramsFile.put(Consts.IMAGE, file);
                AdapterAllBookings.img_upload.setVisibility(View.VISIBLE);
                AdapterAllBookings.img_upload.setImageURI(uri);


            }


        } else {

        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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
            new AsyncBluetoothEscPosPrint(getActivity()).execute(getAsyncEscPosPrinter(selectedDevice, pos));

           /* Log.e("Bluetooth_request", "3");

            requestPermissions(newBookings.getActivity(), new String[]{Manifest.permission.BLUETOOTH}, 109);*/
        } else {
            new AsyncBluetoothEscPosPrint(getActivity()).execute(getAsyncEscPosPrinter(selectedDevice, pos));

        }
    }

    @Override
    public void getInvoice(int position) {
        new AsyncBluetoothEscPosPrint(getActivity()).execute(getAsyncEscPosPrinter(selectedDevice, position));

    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, int pos) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        String paymnt_mode = "";

        if (artistBookingsList.get(pos).getPay_type().equalsIgnoreCase("0")) {
            paymnt_mode = "Onlile Payment";

        } else if (artistBookingsList.get(pos).getPay_type().equalsIgnoreCase("1")) {
            paymnt_mode = "Cash Payment";

        } else if (artistBookingsList.get(pos).getPay_type().equalsIgnoreCase("2")) {
            paymnt_mode = "Wallet Payment";
        }


        String ty = "";
        double price = Double.parseDouble(artistBookingsList.get(pos).getActuallcollectamount()) + Double.parseDouble(artistBookingsList.get(pos).getMy_discount());
        String price_with_dis = String.valueOf(price);

        /*
          ty += "[L]<b>" + productDTOArrayList.get(i).getProduct_name() + "[R]₹" + productDTOArrayList.get(i).getProduct_price() + "</b>\n" +
                    "[L]QTY: " + productDTOArrayList.get(i).getQty() + "[R]" + productDTOArrayList.get(i).getDiscount() + "% OFF</b>\n\n";*/

        for (int i = 0; i < productDTOArrayList.size(); i++) {

            Log.e("price", "" + productDTOArrayList.get(i).getPrice());
            ty += "[L]<b>" + productDTOArrayList.get(i).getProduct_name() +"</b>\n" +
                    "[L]<font size='normal'>"+productDTOArrayList.get(i).getQty_description()+"</font>\n" +
                    "[L]QTY: " + productDTOArrayList.get(i).getQty() + "[R]<b>₹" + productDTOArrayList.get(i).getProduct_price() + "</b>\n\n"+
                   /* "[L]<b>Batch No : [R]070122/01</b>\n"+
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
                   */
                    "[C]--------------------------------\n";
        }
        Log.e("SHIVAKASHI", ty);
        //   booking("3", pos);
        //   dialogcamera.dismiss();
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.setTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, getApplicationContext().getResources().getDrawableForDensity(R.drawable.kamaii_final_logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n"+
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
                        "[C]--------------------------------\n"+
                      /*  "[L]<b>Your order is completed</b>\n" +
                        "[L]Deliver Time: [R]" + artistBookingsList.get(pos).getEnd_time() + "\n\n" +
                       */

                        ty +

                     /*   "[L]<b>Masala Khakhra[R]₹80</b>\n" +
                        "[L]QTY: 1(500 Grams)[R]15% OFF</b>\n\n" +
*/
                        /* "[L]<b>Ingradients: RICE FLOUR, CHICKPEA FLOUR, BUTTER, VAGETABLE OIL, GARAM MASALA, RED CHILLI</b>\n\n" +
                         *//*
                        "[L]QTY: 1(500 Grams)[R]15% OFF</b>\n\n" +
*/
                        // "[C]<u>You will save ₹" + artistBookingsList.get(pos).getMy_discount() + " on this order</u></b>\n\n" +
                        "[L]<b>Order Detail</b>\n" +
                        "[L]Total Price (" + productDTOArrayList.size() + " items)[R]<b>"+price_with_dis+"</b>\n"+
                        // "[L]Discount[R]<b>₹-" + artistBookingsList.get(pos).getMy_discount() + "</b>\n" +
                        "[L]Shipping Charge[R]<b>" + String.valueOf(Double.parseDouble(artistBookingsList.get(pos).getShipping_charges()))+"</b>\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[L]\n" +
                        "[L]Order Amount[R]<b>₹" + artistBookingsList.get(pos).getActuallcollectamount() + "</b>\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[L]\n" +
                        "[C]Thank you for Order!\n"
                        +"[C] Keep Ordering :)");
    }

}
