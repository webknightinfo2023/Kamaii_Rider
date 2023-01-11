package com.kamaii.rider.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.github.dhaval2404.imagepicker.ImagePicker;
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
import com.google.gson.Gson;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.LocationService;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.ImagePickSetOnclickListner;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.utils.CustomButton;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.FileUtility;
import com.kamaii.rider.utils.ImageCompression;
import com.kamaii.rider.utils.MainFragment;
import com.kamaii.rider.utils.ProjectUtils;
import com.kamaii.rider.utils.SpinnerDialog;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicInfoActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = BasicInfoActivity.class.getSimpleName();
    private Context mContext;
    private CustomEditText etCategoryD, etNameD, etAboutD, etCityD, etLocationD, etacc_no;
    private CustomTextViewBold artist_name;
    private CustomButton btnSubmit;
    private LinearLayout llBack;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    private ArtistDetailsDTO artistDetailsDTO;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1000;
    private double lats = 0;
    private double longs = 0;
    private BaseActivity baseActivity = new BaseActivity();
    String user_profile_uri_str = "";
    private HashMap<String, String> paramsphoto;

    private HashMap<String, String> paramsUpdate = new HashMap<>();
    private UserDTO userDTO;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private SharedPrefrence prefrence;
    private CircleImageView profile_img;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage = "";
    Bitmap bm;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    File file;
    Bitmap bitmap = null;
    private HashMap<String, File> paramsFile = new HashMap<>();
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    CustomEditText etrefernamenumber;
    HashMap<String, String> parms = new HashMap<>();
    Double latitude = 0.0;
    Double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        mContext = BasicInfoActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        try {
            Intent intent = new Intent(BasicInfoActivity.this, LocationService.class);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        etrefernamenumber = findViewById(R.id.etrefernamenumber);
        if (getIntent().hasExtra(Consts.CATEGORY_list)) {
            categoryDTOS = (ArrayList<CategoryDTO>) getIntent().getSerializableExtra(Consts.CATEGORY_list);
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
        }

        if (artistDetailsDTO != null) {
            showData();
        }

        setUpGClient();
        setUiAction();
    }

    public void setUiAction() {
        etCategoryD = (CustomEditText) findViewById(R.id.etCategoryD);
        etNameD = (CustomEditText) findViewById(R.id.etNameD);
        etAboutD = (CustomEditText) findViewById(R.id.etAboutD);
        etCityD = (CustomEditText) findViewById(R.id.etCityD);
        etLocationD = (CustomEditText) findViewById(R.id.etLocationD);
        artist_name = (CustomTextViewBold) findViewById(R.id.artist_name);

        btnSubmit = (CustomButton) findViewById(R.id.btnSubmit);
        llBack = (LinearLayout) findViewById(R.id.llBack);
        profile_img = findViewById(R.id.add_profile_pic);

        etCategoryD.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        llBack.setOnClickListener(this);
        profile_img.setOnClickListener(this);

      //  etLocationD.setEnabled(false);
        artist_name.setText("Hello, " + userDTO.getName());

        builder = new BottomSheet.Builder(BasicInfoActivity.this).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(BasicInfoActivity.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(BasicInfoActivity.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                              //      Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);

                                    Log.e("file", "" + file);
                                    if (!file.exists()) {
                                        try {
                                      //      Toast.makeText(mContext, "2", Toast.LENGTH_SHORT).show();

                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                         //   Toast.makeText(mContext, "3", Toast.LENGTH_SHORT).show();

                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    //    Toast.makeText(mContext, "4", Toast.LENGTH_SHORT).show();

                                        picUri = FileProvider.getUriForFile(mContext.getApplicationContext(), mContext.getApplicationContext().getPackageName() + ".fileprovider", file);
                                        Log.e("picUri", "" + picUri);
                                     //   Toast.makeText(mContext, "picUri :" + picUri, Toast.LENGTH_SHORT).show();


                                    } else {
                                     //   Toast.makeText(mContext, "5", Toast.LENGTH_SHORT).show();

                                        picUri = Uri.fromFile(file); // create
                                    }
                                //    Toast.makeText(mContext, "6", Toast.LENGTH_SHORT).show();

                                    prefrence.setValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(BasicInfoActivity.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(BasicInfoActivity.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_pic)), PICK_FROM_GALLERY);

                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });

        spinnerDialogCate = new SpinnerDialog((Activity) mContext, categoryDTOS, getResources().getString(R.string.select_cate));// With 	Animation


        if (artistDetailsDTO != null) {
            showData();
        }


    }

    private File getOutputMediaFile(int type) {

        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root, Consts.APP_NAME);
        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            Toast.makeText(mContext, "type 1", Toast.LENGTH_SHORT).show();

            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.APP_NAME + timeStamp + ".png");

            imageName = Consts.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }

    public void showData() {

        etCategoryD.setText(artistDetailsDTO.getCategory_name());
        etNameD.setText(artistDetailsDTO.getName());
        etAboutD.setText(artistDetailsDTO.getAbout_us());
        etCityD.setText(artistDetailsDTO.getCity());
        etLocationD.setText(artistDetailsDTO.getLocation());
        etacc_no.setText(artistDetailsDTO.getAccount_no());


        Glide.with(mContext).
                load(artistDetailsDTO.getBanner_image())
                .placeholder(R.drawable.banner_img)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_img);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etCategoryD:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    if (categoryDTOS.size() > 0)
                        spinnerDialogCate.showSpinerDialog();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

                break;

            case R.id.btnSubmit:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    submitPersonalProfile();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

                break;
            case R.id.add_profile_pic:
                ImagePicker.Companion.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(768)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
            case R.id.llBack:
                Intent intent1 = new Intent(BasicInfoActivity.this, DocumentUploadTwoActivity.class);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;
        }
    }

    private ImagePickSetOnclickListner imageSetOnclickListner = new ImagePickSetOnclickListner() {
        @Override
        public void Camera(Uri uri) {
            if (uri != null) {
                final String path = FileUtility.getPath(BasicInfoActivity.this, uri);
                if (path != null) {
                    if (uri != null) {

                        pathOfImage = picUri.getPath();
                        imageCompression = new ImageCompression(BasicInfoActivity.this);
                        imageCompression.execute(pathOfImage);
                        imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                            @Override
                            public void processFinish(String imagePath) {
                                try {
                                    file = new File(imagePath);


                                    Glide.with(BasicInfoActivity.this).load("file://" + imagePath)
                                            .thumbnail(0.5f)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(profile_img);
                                    paramsFile = new HashMap<>();
                                    paramsFile.put(Consts.IMAGE, file);
                                    Log.e("image", imagePath);
                                    paramsphoto = new HashMap<>();
                                    paramsphoto.put(Consts.USER_ID, userDTO.getUser_id());
                                    if (NetworkManager.isConnectToInternet(BasicInfoActivity.this)) {
                                        updateProfileSelf();
                                    } else {
                                        ProjectUtils.showToast(BasicInfoActivity.this, getResources().getString(R.string.internet_concation));
                                    }


                                    Log.e("image", imagePath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            } else {

                return;
            }

        }

        @Override
        public void Gallary(Uri uri) {
            if (uri != null) {
                final String path = FileUtility.getPath(BasicInfoActivity.this, uri);
                if (path != null) {
                    startCropping(uri, CROP_GALLERY_IMAGE);
                }
            } else {

                return;
            }

        }
    };

    public void updateProfileSelf() {
        new HttpsRequest(Consts.UPDATE_PROFILE_API, paramsphoto, paramsFile, BasicInfoActivity.this).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {

                        Log.e("BasicInfoActivity camera",""+response.toString());
                        ProjectUtils.showToast(BasicInfoActivity.this, msg);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        baseActivity.showImage();

                        Glide.with(BasicInfoActivity.this).
                                load(userDTO.getImage())
                                .placeholder(R.drawable.dummyuser_image)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profile_img);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                   // ProjectUtils.showToast(BasicInfoActivity.this, msg);
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        com.kamaii.rider.Glob.BUBBLE_VALUE = "0";
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 23) {
                Uri picUri = data.getData();
                Log.e("Image_response", " 1 ");

                pathOfImage = picUri.getPath();

                String path = FileUtility.getPath(BasicInfoActivity.this, picUri);
                File file = new File(path);
              //  file = new File(imagePath);


                Glide.with(BasicInfoActivity.this).load(picUri)
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profile_img);
                paramsFile = new HashMap<>();
                paramsFile.put(Consts.IMAGE, file);
             //   Log.e("image", imagePath);
                paramsphoto = new HashMap<>();
                paramsphoto.put(Consts.USER_ID, userDTO.getUser_id());
                if (NetworkManager.isConnectToInternet(BasicInfoActivity.this)) {
                    updateProfileSelf();
                } else {
                    ProjectUtils.showToast(BasicInfoActivity.this, getResources().getString(R.string.internet_concation));
                }


            //    Log.e("image", imagePath);
               /* imageCompression = new ImageCompression(BasicInfoActivity.this);
                imageCompression.execute(pathOfImage);
                imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                    @Override
                    public void processFinish(String imagePath) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });*/
            }
        }

        if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getMyLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(mContext, MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);

    }

    private void findPlace() {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .country("IN")
                        .build(PlaceOptions.MODE_CARDS))
                .build(BasicInfoActivity.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }


    public void submitPersonalProfile() {

      if (!validation(etCityD, getResources().getString(R.string.val_city))) {
            return;
        } else if (!validation(etLocationD, getResources().getString(R.string.val_location))) {
            return;
        } else if (pathOfImage.equals("") || pathOfImage.equals(null)) {
            Toast.makeText(mContext, "Upload your profile image!!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {

                paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                //paramsUpdate.put(Consts.NAME, ProjectUtils.getEditTextValue(etNameD));
                paramsUpdate.put(Consts.NAME, "Kamaii rider");
                paramsUpdate.put(Consts.BIO, "");
              //  paramsUpdate.put(Consts.ABOUT_US, ProjectUtils.getEditTextValue(etAboutD));
                paramsUpdate.put(Consts.ABOUT_US, "Kamaii rider");
                paramsUpdate.put(Consts.CITY, ProjectUtils.getEditTextValue(etCityD));
                paramsUpdate.put(Consts.COUNTRY, "");
                // paramsUpdate.put(Consts.LOCATION, ProjectUtils.getEditTextValue(etLocationD));
                paramsUpdate.put(Consts.BANK_NAME, "");
                paramsUpdate.put(Consts.ACCOUNT_HOLDER_NAME, "");
                paramsUpdate.put(Consts.ACCOUNT_NO, "");
                paramsUpdate.put(Consts.IFSC_CODE, "");
                paramsUpdate.put(Consts.LOCATION, etLocationD.getText().toString());
                paramsUpdate.put("ref_number", etrefernamenumber.getText().toString());
                paramsFile.put(Consts.BANNER_IMAGE, file);
                paramsUpdate.put(Consts.LATITUDE, String.valueOf(latitude));
                paramsUpdate.put(Consts.LONGITUDE, String.valueOf(longitude));

                Log.e("PARAM", paramsUpdate.toString());
                updateProfile();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            ProjectUtils.showLong(mContext, msg);
            return false;
        } else {
            return true;
        }
    }

    public void updateProfile() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PROFILE_ARTIST_API, paramsUpdate, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.e("BASICINFI_ABC", response.toString());
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {
                      //  ProjectUtils.showToast(mContext, msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        userDTO.setIs_profile(1);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        Intent intent1 = new Intent(BasicInfoActivity.this, DocumentUploadTwoActivity.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(R.anim.stay, R.anim.slide_down);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                  //  ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        // Toast.makeText(mContext, "1", Toast.LENGTH_LONG).show();

        if (mylocation != null) {
            //Toast.makeText(mContext, "2", Toast.LENGTH_LONG).show();

            latitude = mylocation.getLatitude();
            Log.e("lat", "" + latitude.toString());
            longitude = mylocation.getLongitude();
            Log.e("lang", "" + longitude.toString());

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {

                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();
                Log.e("IGA123214", "Address" + add);

                etLocationD.setText(obj.getAddressLine(0));

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(BasicInfoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(BasicInfoActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(BasicInfoActivity.this,
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
                            .requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback() {

                        @Override
                        public void onResult(@NonNull Result result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(BasicInfoActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);


                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        status.startResolutionForResult(BasicInfoActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
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
    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(BasicInfoActivity.this)
                .enableAutoManage(BasicInfoActivity.this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

}