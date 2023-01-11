package com.kamaii.rider.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.gson.Gson;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.OnSpinerItemClick;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.utils.CustomButton;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ImageCompression;
import com.kamaii.rider.utils.MainFragment;
import com.kamaii.rider.utils.ProjectUtils;
import com.kamaii.rider.utils.SpinnerDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EditPersnoalInfo extends AppCompatActivity implements View.OnClickListener {

    private String TAG = EditPersnoalInfo.class.getSimpleName();
    private Context mContext;
    private CustomEditText etCategoryD, etNameD, etBioD, etAboutD, etCityD, etCountryD, etLocationD, etRateD,etBankName,etBenificiaryName,etacc_no,etifsc_code;
    private CustomTextViewBold tvText;
    private CustomButton btnSubmit;
    private LinearLayout llBack;
    private CustomTextView bioLength, aboutLength;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    private ArtistDetailsDTO artistDetailsDTO;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1000;
    private double lats = 0;
    private double longs = 0;
    private HashMap<String, String> paramsUpdate = new HashMap<>();
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private ImageView ivBanner;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    File file;
    Bitmap bitmap = null;
    private HashMap<String, File> paramsFile = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_persnoal_info);
        mContext = EditPersnoalInfo.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        if (getIntent().hasExtra(Consts.CATEGORY_list)) {
            categoryDTOS = (ArrayList<CategoryDTO>) getIntent().getSerializableExtra(Consts.CATEGORY_list);
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
        }
        setUiAction();
    }

    public void setUiAction() {
        etCategoryD = (CustomEditText) findViewById(R.id.etCategoryD);
        etNameD = (CustomEditText) findViewById(R.id.etNameD);
        etBioD = (CustomEditText) findViewById(R.id.etBioD);
        etAboutD = (CustomEditText) findViewById(R.id.etAboutD);
        etCityD = (CustomEditText) findViewById(R.id.etCityD);
        etCountryD = (CustomEditText) findViewById(R.id.etCountryD);
        etLocationD = (CustomEditText) findViewById(R.id.etLocationD);
        etRateD = (CustomEditText) findViewById(R.id.etRateD);
        etBankName = (CustomEditText) findViewById(R.id.etBankName);
        etBenificiaryName = (CustomEditText) findViewById(R.id.etBenificiaryName);
        etacc_no = (CustomEditText) findViewById(R.id.etacc_no);
        etifsc_code = (CustomEditText) findViewById(R.id.etifsc_code);




        tvText = (CustomTextViewBold) findViewById(R.id.tvText);
        btnSubmit = (CustomButton) findViewById(R.id.btnSubmit);
        llBack = (LinearLayout) findViewById(R.id.llBack);
        ivBanner = (ImageView) findViewById(R.id.ivBanner);
        bioLength = (CustomTextView) findViewById(R.id.bioLength);
        aboutLength = (CustomTextView) findViewById(R.id.aboutLength);

        etCategoryD.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        llBack.setOnClickListener(this);
        ivBanner.setOnClickListener(this);

        etLocationD.setEnabled(false);
        Geocoder geocoder = new Geocoder(EditPersnoalInfo.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)), 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            etLocationD.setText(obj.getAddressLine(0));

        } catch (IOException e) {
            e.printStackTrace();
        }




        etBioD.addTextChangedListener(new TextWatcher() {

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
                bioLength.setText(String.valueOf(s.length()) + "/40");

            }
        });
        etAboutD.addTextChangedListener(new TextWatcher() {

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
                aboutLength.setText(String.valueOf(s.length()) + "/200");

            }
        });

        builder = new BottomSheet.Builder(EditPersnoalInfo.this).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(EditPersnoalInfo.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(EditPersnoalInfo.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        picUri = FileProvider.getUriForFile(mContext.getApplicationContext(), mContext.getApplicationContext().getPackageName() + ".fileprovider", file);
                                    } else {
                                        picUri = Uri.fromFile(file); // create
                                    }

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
                        if (ProjectUtils.hasPermissionInManifest(EditPersnoalInfo.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(EditPersnoalInfo.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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
        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                etCategoryD.setText(item);

                tvText.setText(getResources().getString(R.string.commis_msg) + categoryDTOS.get(position).getCurrency_type() + categoryDTOS.get(position).getPrice());


            }
        });

        if (artistDetailsDTO != null) {
            showData();
        }

        etRateD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });



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
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.APP_NAME + timeStamp + ".png");

            imageName = Consts.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }

    public void showData() {
        for (int j = 0; j < categoryDTOS.size(); j++) {
            if (categoryDTOS.get(j).getId().equalsIgnoreCase(artistDetailsDTO.getCategory_id())) {
                categoryDTOS.get(j).setSelected(true);
                etCategoryD.setText(categoryDTOS.get(j).getCat_name());
                tvText.setText(getResources().getString(R.string.commis_msg) + categoryDTOS.get(j).getCurrency_type() + categoryDTOS.get(j).getPrice());


            }
        }

        spinnerDialogCate = new SpinnerDialog((Activity) mContext, categoryDTOS, getResources().getString(R.string.select_cate));// With 	Animation
        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                etCategoryD.setText(item);
                tvText.setText(getResources().getString(R.string.commis_msg) + categoryDTOS.get(position).getCurrency_type() + categoryDTOS.get(position).getPrice());


            }
        });
        etCategoryD.setText(artistDetailsDTO.getCategory_name());
        etNameD.setText(artistDetailsDTO.getName());
        etBioD.setText(artistDetailsDTO.getBio());
        etAboutD.setText(artistDetailsDTO.getAbout_us());
        etCityD.setText(artistDetailsDTO.getCity());
        etCountryD.setText(artistDetailsDTO.getCountry());
        etLocationD.setText(artistDetailsDTO.getLocation());
        etRateD.setText(artistDetailsDTO.getPrice());
        etBankName.setText(artistDetailsDTO.getBank_name());
        etBenificiaryName.setText(artistDetailsDTO.getBenifeciry_name());
        etacc_no.setText(artistDetailsDTO.getAccount_no());
        etifsc_code.setText(artistDetailsDTO.getIfsc_code());


        Glide.with(mContext).
                load(artistDetailsDTO.getBanner_image())
                .placeholder(R.drawable.banner_img)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivBanner);


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
            case R.id.ivBanner:
                builder.show();
                break;
            case R.id.llBack:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        com.kamaii.rider.Glob.BUBBLE_VALUE ="0";
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            lats = ((Point) selectedCarmenFeature.geometry()).latitude();
            longs = ((Point) selectedCarmenFeature.geometry()).longitude();

            etLocationD.setText(selectedCarmenFeature.placeName());

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
                .accessToken( getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .country("IN")
                        .build(PlaceOptions.MODE_CARDS))
                .build(EditPersnoalInfo.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }



    public void submitPersonalProfile() {
        if (!validation(etNameD, getResources().getString(R.string.val_name))) {
            return;
        }
        else if (!validation(etCityD, getResources().getString(R.string.val_city))) {
            return;
        }  else if (!validation(etLocationD, getResources().getString(R.string.val_location))) {
            return;
        }
       else {
            if (NetworkManager.isConnectToInternet(mContext)) {

                paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                paramsUpdate.put(Consts.NAME, ProjectUtils.getEditTextValue(etNameD));
                paramsUpdate.put(Consts.BIO, "");
                paramsUpdate.put(Consts.ABOUT_US, ProjectUtils.getEditTextValue(etAboutD));
                paramsUpdate.put(Consts.CITY, ProjectUtils.getEditTextValue(etCityD));
                paramsUpdate.put(Consts.COUNTRY, "");
                paramsUpdate.put(Consts.LOCATION, ProjectUtils.getEditTextValue(etLocationD));
                paramsUpdate.put(Consts.BANK_NAME, "");
                paramsUpdate.put(Consts.ACCOUNT_HOLDER_NAME, "");
                paramsUpdate.put(Consts.ACCOUNT_NO, "");
                paramsUpdate.put(Consts.IFSC_CODE, "");
                paramsFile.put(Consts.BANNER_IMAGE, file);
                if (lats != 0)
                    paramsUpdate.put(Consts.LATITUDE, String.valueOf(lats));

                if (longs != 0)
                    paramsUpdate.put(Consts.LONGITUDE, String.valueOf(longs));

                Log.e("PARAM",paramsUpdate.toString());
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
        new HttpsRequest(Consts.UPDATE_PROFILE_ARTIST_API, paramsUpdate,  mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.e("ABC",response.toString());
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {
                        ProjectUtils.showToast(mContext, msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        userDTO.setIs_profile(1);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        finish();
                        overridePendingTransition(R.anim.stay, R.anim.slide_down);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }


}
