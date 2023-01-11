package com.kamaii.rider.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.DocumentDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.LocationService;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.ImagePickSetOnclickListner;
import com.kamaii.rider.interfacess.OnSpinerItemClick;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.adapter.AddVehicleAdapter;
import com.kamaii.rider.ui.models.VehicleModel;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class BusinessDocumentActivity extends AppCompatActivity {
    Button btn_submit;
    private String TAG = BusinessDocumentActivity.class.getSimpleName();
    private View view;
    ImageView layrcfront, layrcback, layinsuarnce, laycarpermit, laypoliceverification, laycarphoto, layownerphoto, layselfie, laydrivingfront, laydrivingback;
    private Dialog dialog;
    LinearLayout laycamera, laygalley;
    CustomTextViewBold tvCancel;
    String vehicleImagePath = "", pathone = "", pathtwo = "", paththree = "", pathfour = "", pathfive = "", pathsix = "", pathseven = "", patheight = "", pathnine = "", pathten = "", pathelven = "", pathtwelve = "", paththirteen = "", carmodelnamestring = "", carcategorystring = "", citystring = "", pathfourteen = "",
            bookingcarstring = "", carno = "", rno = "";
    String flag = "";
    String encodedBase64 = "";
    String vehicle_no = "";

    String[] city = {"Ahmedabad", "Amreli", "Gandhinagar", "Vadodara", "Rajkot", "Surat", "Bhavanagar", "Lunawada", "Godhra", "Anand", "Banas", "Kantha", "Bharuch", "Dohad", "Jamnagar"
            , "Junagadh", "Kachchh", "Kheda", "Mahesana", "Narmada", "Navsari", "Panch", "Mahals", "Patan", "Porbandar", "Sabar", "Surendranagar", "Tapi", "The Dangs", "Valsad", "Modasa"
            , "Mumbai", "Delhi", "Pune", "Other City"};
    String[] carcategory = {"Select car category", "Hatchback Car", "Sedan Car", "SUV Car", "Luxury Car"};
    String[] carmodelname = {"Select car model", "Hyundai Xcent", "Hyundai Accent", "Hyundai i10", "Maruti Swift Dzire", "Maruti Alto", "Tata Indica", "Tata Zest",
            "Toyota Innova Crysta", "Nissan Sunny",
            "Chevrolet Spark", "Honda WRV", "Honda City", "Mahindra TUV300", "Mahindra Bolero", "Hyundai EON", "Maruti Ertiga", "Maruti WagonR", "Maruti Eeco", "Maruti Celerio",
            "Tata Itos", "Chevrolet Tavera", "Toyota Etios", "Nissan Datsun Go", "Honda Amaze", "Honda BRV", "Mahindra Xylo", "Mahindra Marazzo", " Ford Figo Aspire", "Rickshaw Bajaj",
            "Bike", "Others"};
    Spinner spicity, spicarmodelname, spicarmcategory;
    private HashMap<String, String> params;
    CheckBox checkone, checktwo, checkthree, checkfour;
    private HashMap<String, File> paramsFile;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    ProgressDialog progressDialog;
    File fileone = null, filetwo = null, filethree = null, filefour = null, filefive = null, filesix = null, fileseven = null, fileeight = null, filenine = null, fileten = null, fileeleven = null, filetwelve = null, filethirteen = null, filefourteen = null;
    private Bundle bundle;
    private ArtistDetailsDTO artistDetailsDTO;
    private ArrayList<DocumentDTO> documentDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private BaseActivity baseActivity;
    Uri uri;
    public static LinearLayout layall;
    CustomTextViewBold laymsg;
    String edit = "0";
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    CustomTextViewBold tvcat;
    String category_id = "";
    LinearLayout layallrcbook, layalldlicence, laycarnformation, layselectcategory;
    CustomTextViewBold vehicle_name_spinner, addvehicle_submit_btn, addvehicle_cancel_btn;
    CustomEditText dialog_vechial_number;
    ImageView dialog_vehicle_img;
    LinearLayout llBack;
    private HashMap<String, String> parmsCategory2 = new HashMap<>();
    String vehicle_id = "";
    List<VehicleModel> vehicleList;
    AddVehicleAdapter addVehicleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_document);

        prefrence = SharedPrefrence.getInstance(BusinessDocumentActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        documentDTOArrayList = new ArrayList<>();
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        progressDialog = new ProgressDialog(BusinessDocumentActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        parmsCategory2.put(Consts.USER_ID, userDTO.getUser_id());

        params = new HashMap<>();
        paramsFile = new HashMap<>();

        vehicle_name_spinner = findViewById(R.id.vehicle_name_spinner);
        dialog_vehicle_img = findViewById(R.id.vehicle_img);
        dialog_vechial_number = findViewById(R.id.dialog_vechial_number);

        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        layrcfront = findViewById(R.id.layrcfront);
        layrcback = findViewById(R.id.layrcback);
        layinsuarnce = findViewById(R.id.layinsuarnce);
        laycarpermit = findViewById(R.id.laycarpermit);
        laypoliceverification = findViewById(R.id.laypoliceverification);
        layselfie = findViewById(R.id.layselfie);
        laycarphoto = findViewById(R.id.laycarphoto);
        layownerphoto = findViewById(R.id.layownerphoto);
        btn_submit = findViewById(R.id.btn_submit);
        checkone = findViewById(R.id.checkone);
        checktwo = findViewById(R.id.checktwo);
        checkthree = findViewById(R.id.checkthree);
        checkfour = findViewById(R.id.checkfour);
        laymsg = findViewById(R.id.laymsg);
        layall = findViewById(R.id.layall);
        laydrivingfront = findViewById(R.id.laydrivingfront);
        laydrivingback = findViewById(R.id.laydrivingback);
        tvcat = findViewById(R.id.tvcat);
        layallrcbook = findViewById(R.id.layallrcbook);
        layalldlicence = findViewById(R.id.layalldlicence);
        laycarnformation = findViewById(R.id.laycarnformation);
        layselectcategory = findViewById(R.id.layselectcategory);


        spicarmodelname = findViewById(R.id.spicarmodelname);
        spicarmcategory = findViewById(R.id.spicarmcategory);

        llBack = findViewById(R.id.llBack);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BusinessDocumentActivity.this, DocumentUploadTwoActivity.class);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });

        dialog_vechial_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if ((dialog_vechial_number.getText().length() + 1 == 3 || dialog_vechial_number.getText().length() + 1 == 6 || dialog_vechial_number.getText().length() + 1 == 9)) {
                    if (before - count < 0) {
                        dialog_vechial_number.setText(dialog_vechial_number.getText() + " ");
                        dialog_vechial_number.setSelection(dialog_vechial_number.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

        });
        getVehicleData();

        dialog = new Dialog(BusinessDocumentActivity.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dailog_camera_gallery);
        dialog.setCancelable(false);
        laycamera = dialog.findViewById(R.id.laycamera);
        laygalley = dialog.findViewById(R.id.laygalley);
        tvCancel = dialog.findViewById(R.id.tvCancel);

        vehicle_name_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogCate.showSpinerDialog();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        laycamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        laygalley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        laydrivingfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "12";

                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(768)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "12";

                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(768)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });

        laydrivingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "13";

                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(768)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "13";

                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(768)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });

        layrcfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "3";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(768)
                                .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "3";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(768)
                            .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });


        layrcback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "4";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(768)
                                .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "4";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()
                            .compress(768)
                            .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });

        dialog_vehicle_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = "44";
                ImagePicker.Companion.with(BusinessDocumentActivity.this)
                        .crop()
                        .compress(768)
                        .maxResultSize(768, 768)
                        .start(23);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {
                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {

                        if (edit.equalsIgnoreCase("1")) {

                            btn_submit.setText("Submit");
                            edit = "0";
                        } else {
                            Click();

                        }


                    } else {
                   //     Toast.makeText(BusinessDocumentActivity.this, "Already approved document. Good Luck!", LENGTH_LONG).show();
                    }
                } else {
                    Click();
                }

            }
        });
    }

    public void getVehicleData() {


        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_VEHICLE_LIST_API, parmsCategory, BusinessDocumentActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        Log.e("DIALOG_CAT", "" + categoryDTOS.toString());
                        Collections.sort(categoryDTOS, new Comparator<CategoryDTO>() {
                            @Override
                            public int compare(CategoryDTO lhs, CategoryDTO rhs) {
                                return lhs.getCat_name().compareTo(rhs.getCat_name());
                            }
                        });
                        progressDialog.dismiss();

                        spinnerDialogCate = new SpinnerDialog(BusinessDocumentActivity.this, categoryDTOS, getResources().getString(R.string.all_vehicle));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                vehicle_name_spinner.setText(item);
                                vehicle_id = categoryDTOS.get(position).getId();
                                Log.e("ID", "" + vehicle_id);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    progressDialog.dismiss();
               //     Toast.makeText(BusinessDocumentActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int loop(String val, String[] array) {
        return new ArrayList<String>(Arrays.asList(array)).indexOf(val);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 23) {


                Uri imageUri = data.getData();

                if (flag.equalsIgnoreCase("3")) {
                    layrcfront.setImageURI(imageUri);
                    paththree = imageUri.getPath();
                } else if (flag.equalsIgnoreCase("4")) {
                    layrcback.setImageURI(imageUri);
                    pathfour = imageUri.getPath();
                } else if (flag.equalsIgnoreCase("12")) {
                    laydrivingfront.setImageURI(imageUri);
                    pathtwelve = imageUri.getPath();
                } else if (flag.equalsIgnoreCase("13")) {
                    laydrivingback.setImageURI(imageUri);
                    paththirteen = imageUri.getPath();
                }else if (flag.equalsIgnoreCase("44")) {
                    dialog_vehicle_img.setImageURI(imageUri);
                    vehicleImagePath = imageUri.getPath();


                    File originalFile = new File(vehicleImagePath);
                    String ConcatedEncoded = "";
                    try {
                        int flags = android.util.Base64.NO_WRAP;
                        FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                        byte[] bytes = new byte[(int) originalFile.length()];
                        fileInputStreamReader.read(bytes);
                        encodedBase64 = new String(Base64.encodeToString(bytes, flags));
                        Log.e("PATH", "" + encodedBase64);
                        Glide.with(BusinessDocumentActivity.this).load(vehicleImagePath).into(dialog_vehicle_img);
                        ConcatedEncoded = encodedBase64;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else {

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void updateprofile() {
        MultipartBody.Part fileToSendfive = null;
        MultipartBody.Part fileToSendsix = null;
        MultipartBody.Part fileToSendseven = null;
        MultipartBody.Part fileToSendele = null;
        MultipartBody.Part fileToSendfourteen = null;
        MultipartBody.Part fileToSendthree = null;
        MultipartBody.Part fileToSendfour = null;
        MultipartBody.Part fileToSendtwelve = null;
        MultipartBody.Part fileToSendthirteen = null;
        MultipartBody.Part fileEncodedBase64 = null;
        MultipartBody.Part fileToSendnine = null;

        if (!patheight.trim().isEmpty()) {
            RequestBody requestBodyeight = RequestBody.create(MediaType.parse("multipart/form-data"), fileeight);
            fileEncodedBase64 = MultipartBody.Part.createFormData("selfi_with_car_photo", fileeight.getName(), requestBodyeight);

        }
        if (!pathnine.trim().isEmpty()) {
            RequestBody requestBodynine = RequestBody.create(MediaType.parse("multipart/form-data"), filenine);
            fileToSendnine = MultipartBody.Part.createFormData("owner_photo", filenine.getName(), requestBodynine);
        }

        if (!paththree.trim().isEmpty()) {
            RequestBody requestBodyth = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
            fileToSendthree = MultipartBody.Part.createFormData("rc_book", filethree.getName(), requestBodyth);

        }

        if (!pathfour.trim().isEmpty()) {
            RequestBody requestBodyfour = RequestBody.create(MediaType.parse("multipart/form-data"), filefour);
            fileToSendfour = MultipartBody.Part.createFormData("rc_book_back", filefour.getName(), requestBodyfour);

        }

        if (!pathfive.trim().isEmpty()) {
            RequestBody requestBodyfive = RequestBody.create(MediaType.parse("multipart/form-data"), filefive);
            fileToSendfive = MultipartBody.Part.createFormData("car_insurance", filefive.getName(), requestBodyfive);
        }


        if (!pathsix.trim().isEmpty()) {
            RequestBody requestBodysix = RequestBody.create(MediaType.parse("multipart/form-data"), filesix);
            fileToSendsix = MultipartBody.Part.createFormData("car_permit", filesix.getName(), requestBodysix);
        }


        if (!pathseven.trim().isEmpty()) {
            RequestBody requestBodyse = RequestBody.create(MediaType.parse("multipart/form-data"), fileseven);
            fileToSendseven = MultipartBody.Part.createFormData("police_verification", fileseven.getName(), requestBodyse);

        }

        if (!pathelven.trim().isEmpty()) {
            RequestBody requestBodyele = RequestBody.create(MediaType.parse("multipart/form-data"), fileeleven);
            fileToSendele = MultipartBody.Part.createFormData("selfie_photo", fileeleven.getName(), requestBodyele);

        }

        if (!pathtwelve.trim().isEmpty()) {
            RequestBody requestBodytwelve = RequestBody.create(MediaType.parse("multipart/form-data"), filetwelve);
            fileToSendtwelve = MultipartBody.Part.createFormData("driving_licence_front", filetwelve.getName(), requestBodytwelve);

        }

        if (!paththirteen.trim().isEmpty()) {
            RequestBody requestBodythirteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethirteen);
            fileToSendthirteen = MultipartBody.Part.createFormData("driving_licence_back", filethirteen.getName(), requestBodythirteen);
        }

        RequestBody carmodel = RequestBody.create(MediaType.parse("text/plain"), carmodelnamestring);
        //RequestBody city = RequestBody.create(MediaType.parse("text/plain"), citystring);
        RequestBody bokingcar = RequestBody.create(MediaType.parse("text/plain"), bookingcarstring);
        RequestBody carcategory = RequestBody.create(MediaType.parse("text/plain"), carcategorystring);
        RequestBody carnooo = RequestBody.create(MediaType.parse("text/plain"), carno);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());
        RequestBody vehicle_image = RequestBody.create(MediaType.parse("text/plain"), encodedBase64);
        RequestBody vehicleno = RequestBody.create(MediaType.parse("text/plain"), vehicle_no);
        RequestBody vehicleid = RequestBody.create(MediaType.parse("text/plain"), vehicle_id);
        // RequestBody cat_idreq = RequestBody.create(MediaType.parse("text/plain"), category_id);

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.upload_business_data(fileToSendthree, fileToSendfour, userid, fileToSendtwelve, fileToSendthirteen, vehicle_image, vehicleid, vehicleno);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody1 = response.body();

                        String s = responseBody1.string();
                        Log.e("BUSINESS_RES",""+s);
                        JSONObject object = new JSONObject(s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        if (sstatus == 1) {
                            Log.e("BusinessTrack","1");
                      //      Toast.makeText(BusinessDocumentActivity.this, message, LENGTH_LONG).show();
                            //getArtist();
                            Intent intent1 = new Intent(BusinessDocumentActivity.this, DocumentUploadTwoActivity.class);
                            startActivity(intent1);
                            finish();
                            overridePendingTransition(R.anim.stay, R.anim.slide_down);
                        } else if (sstatus == 3) {
                            Log.e("BusinessTrack","2");
                      //      Toast.makeText(BusinessDocumentActivity.this, message, LENGTH_LONG).show();
                        } else {
                            Log.e("BusinessTrack","3");
                       //     Toast.makeText(BusinessDocumentActivity.this, message, LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("BusinessTrack","4");
                        /*Toast.makeText(BusinessDocumentActivity.this, "Server Did Not Responding and Try again ",
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
                progressDialog.dismiss();
                Toast.makeText(BusinessDocumentActivity.this, "Server Did Not Responding and Try again ",
                        LENGTH_LONG).show();
            }
        });

    }


    public void Click() {
        bookingcarstring = "";
        String cat = vehicle_name_spinner.getText().toString().trim();
        vehicle_no = dialog_vechial_number.getText().toString();
        if (checkone.isChecked()) {
            bookingcarstring = bookingcarstring + checkone.getText().toString();

        }

        if (paththree.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Rc Book Front Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (pathfour.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Rc Book Back Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (pathtwelve.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Driving Licence Front Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (paththirteen.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Driving Licence Back Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (cat.equalsIgnoreCase(getResources().getString(R.string.all_vehicle))) {
            Toast.makeText(BusinessDocumentActivity.this, getResources().getString(R.string.val_vahicle), Toast.LENGTH_SHORT).show();
            return;
        }else if (vehicle_no.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Driving Licence Back Photo", Toast.LENGTH_LONG).show();
            return;
        }else if (encodedBase64.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Vehicle Image", Toast.LENGTH_LONG).show();
            return;
        }  else {


            filethree = new File(paththree);
            filefour = new File(pathfour);
            filetwelve = new File(pathtwelve);
            filethirteen = new File(paththirteen);

            if (NetworkManager.isConnectToInternet(BusinessDocumentActivity.this)) {
                updateprofile();
            } else {
                ProjectUtils.showToast(BusinessDocumentActivity.this, getResources().getString(R.string.internet_concation));
            }


        }
    }

}
