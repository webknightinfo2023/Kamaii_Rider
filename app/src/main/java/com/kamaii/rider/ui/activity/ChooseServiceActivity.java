package com.kamaii.rider.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.DailogArProductBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.OnSpinerItemClick;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.model.CommonServiceModel;
import com.kamaii.rider.model.ThirdCateModel;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.fragment.Services;
import com.kamaii.rider.ui.models.ContactModel;
import com.kamaii.rider.ui.models.SubCateModel;
import com.kamaii.rider.utils.ExpandableHeightGridView;
import com.kamaii.rider.utils.ImageCompression;
import com.kamaii.rider.utils.MainFragment;
import com.kamaii.rider.utils.SpinnerDialog;
import com.kamaii.rider.utils.SpinnerDialogFour;
import com.kamaii.rider.utils.SpinnerDialogSubCate;
import com.kamaii.rider.utils.SpinnerDialogThird;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChooseServiceActivity extends Activity {

    private String TAG = ChooseServiceActivity.class.getSimpleName();
    DailogArProductBinding binding1;
    RadioButton rb;
    RadioGroup radioGroup;
    RadioButton radio_favourate, radio_contact;
    List<ContactModel> contactList;
    ExpandableHeightGridView recyclerView;
    ProgressDialog progressDialog;
    int radiopos = 1;
    public int rb_pos = 1;
    SQLiteDatabase db;
    EditText svSearch;
    Services services;
    Services fragment;
    public Dialog dialogEditProduct;
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> parmsSubcatImage = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    private SpinnerDialogSubCate spinnerDialogsubcate;
    private SpinnerDialogThird SpinnerDialogThird;
    private SpinnerDialogFour spinnerDialogFour;
    // private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> paramsucategory = new HashMap<>();
    private HashMap<String, String> paramsrate = new HashMap<>();
    private HashMap<String, String> paramsthird = new HashMap<>();
    private HashMap<String, String> paramsFour = new HashMap<>();
    private HashMap<String, String> paramsUpdate;
    ArrayList<SubCateModel> subCateModelArrayList = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListserivce = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListnew = new ArrayList<>();
    ArrayList<ThirdCateModel> thirdCateModelArrayList = new ArrayList<>();
    ArrayList<CommonServiceModel> fourModelArrayList = new ArrayList<>();
    ArrayList<ProductDTO> productDTOArrayListnew = new ArrayList<>();
    private HashMap<String, File> paramsFile;
    BottomSheet.Builder builder;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    ImageCompression imageCompression;
    Bitmap bm;
    String pathOfImage;
    File file = null;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String category_id = "", sub_category_id = "", third_id = "", vechilenumber = "", servicename = "", serviceid = "";
    String send_img_str = null;
    String send_price_str = null;
    String encodedBase64 = "";
    boolean image_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);

        init();


        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseServiceActivity.this, BaseActivity.class));
            }
        });

        findViewById(R.id.radio_favourate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_pos = 0;
                image_flag = true;
//                        fragment.addServices();
                addServices();
                if (binding1.manualServiceRelative.getVisibility() == View.VISIBLE) {

                    binding1.manualServiceRelative.setVisibility(View.GONE);
                }
                radio_favourate.setBackground(getResources().getDrawable(R.drawable.radio_btn_selectedleft));
                radio_contact.setBackground(getResources().getDrawable(R.drawable.radio_btn_unselected_right));
                radio_contact.setTextColor(getResources().getColor(R.color.black));
                radio_favourate.setTextColor(getResources().getColor(R.color.white));

            }

        });

        findViewById(R.id.radio_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_flag = false;
                if (binding1.manualServiceRelative.getVisibility() == View.VISIBLE) {

                    binding1.manualServiceRelative.setVisibility(View.GONE);
                }
                encodedBase64 = "null";
                rb_pos = 1;
                addServices();
                radio_contact.setBackground(getResources().getDrawable(R.drawable.radio_btn_selected));
                radio_favourate.setBackground(getResources().getDrawable(R.drawable.radio_btn_unselected));
                radio_favourate.setTextColor(getResources().getColor(R.color.black));
                radio_contact.setTextColor(getResources().getColor(R.color.white));
            }

        });
    }

    public void getRadio() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb) {

                }
            }
        });
    }

    public void init() {

        //  fragment = (Services) getApplicationContext().getSupportFragmentManager().findFragmentById(R.id.service_fragment);
        prefrence = SharedPrefrence.getInstance(ChooseServiceActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        radioGroup = findViewById(R.id.contactlist_radiogroup);
        svSearch = findViewById(R.id.contact_list_svSearch);
        radio_favourate = findViewById(R.id.radio_favourate);
        radio_contact = findViewById(R.id.radio_contact);
        recyclerView = findViewById(R.id.rv_contactlist);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        progressDialog = new ProgressDialog(ChooseServiceActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please Wait");
        dialogEditProduct = new Dialog(ChooseServiceActivity.this/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getCategory();
        binding1 = DataBindingUtil.inflate(LayoutInflater.from(ChooseServiceActivity.this), R.layout.dailog_ar_product, null, false);
        dialogEditProduct.setContentView(binding1.getRoot());

        dialogEditProduct.setCancelable(false);
    }

    public void getCategory() {


        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, ChooseServiceActivity.this).stringPost(TAG, new Helper() {
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

                        spinnerDialogCate = new SpinnerDialog(ChooseServiceActivity.this, categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvcat.setText(item);
                                paramsucategory.put(Consts.CATEGORY_ID, id);
                                paramsthird.put(Consts.CATEGORY_ID, id);
                                paramsUpdate.put(Consts.CATEGORY_ID, id);
                                paramsFour.put("cat_id", id);
                                category_id = id;

                                binding1.tvFiltersub.setText(getResources().getString(R.string.all_sub_categories));
                                binding1.tvhird.setText(getResources().getString(R.string.all_sub__level_categories));
                                binding1.tvservice.setText(getResources().getString(R.string.all_service_name));
                                binding1.etvechialNumber.setText("");
                                if (checkarss(category_id)) {
                                    binding1.etvechialNumber.setVisibility(View.VISIBLE);
                                } else {
                                    binding1.etvechialNumber.setVisibility(View.GONE);
                                }
                                getSubCategory();

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ChooseServiceActivity.this, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void getSubCategory() {

        progressDialog.show();
        new HttpsRequest(Consts.GET_SERVICE_SUB_CATEGORY_API, paramsucategory, ChooseServiceActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {

                    try {
                        binding1.tvFiltersub.setVisibility(View.VISIBLE);
                        subCateModelArrayListserivce = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<SubCateModel>>() {
                        }.getType();
                        subCateModelArrayListserivce = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        Collections.sort(subCateModelArrayListserivce, new Comparator<SubCateModel>() {
                            @Override
                            public int compare(SubCateModel lhs, SubCateModel rhs) {
                                return lhs.getCat_name().compareTo(rhs.getCat_name());
                            }
                        });
                        spinnerDialogsubcate = new SpinnerDialogSubCate(ChooseServiceActivity.this, subCateModelArrayListserivce, getResources().getString(R.string.select_subcategory));
                        spinnerDialogsubcate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvFiltersub.setText(item);
                                paramsUpdate.put(Consts.sub_category_id, id);
                                paramsthird.put(Consts.sub_category_id, id);
                                paramsFour.put("sub_id", id);
                                sub_category_id = id;
                                getThirdCategory();

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(ChooseServiceActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getSubCatImage() {

        Log.e("parmsSubcatImage", "" + parmsSubcatImage.toString());
        new HttpsRequest(Consts.GET_SUBCAT_IMAGE_API, parmsSubcatImage, ChooseServiceActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    //ProjectUtils.showToast(Booking.this, msg);
                    try {
                        Log.e("RESPONSE_SERVICE", "" + response.toString());
                        JSONObject jsonObject = response;
                        String status = jsonObject.getString("status");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String image_uri = jsonObject1.getString("image");


                        // Log.e("success_msg", "" + success_msg);
                        Log.d("image_uri", "" + image_uri);
                        send_img_str = image_uri;
                        Glide.with(ChooseServiceActivity.this).load(image_uri).into(binding1.manualServiceImg);

                        if (rb_pos == 1) {
                            if (binding1.manualServiceRelative.getVisibility() == View.GONE) {

                                binding1.manualServiceRelative.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

        public void getImage() {
    }

    public void getThirdCategory() {
        progressDialog.show();

        new HttpsRequest(Consts.get_sublevel_cat, paramsthird, ChooseServiceActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {

                        binding1.tvhird.setVisibility(View.VISIBLE);

                        thirdCateModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ThirdCateModel>>() {
                        }.getType();
                        thirdCateModelArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        Collections.sort(thirdCateModelArrayList, new Comparator<ThirdCateModel>() {
                            @Override
                            public int compare(ThirdCateModel lhs, ThirdCateModel rhs) {
                                return lhs.getCat_name().compareTo(rhs.getCat_name());
                            }
                        });
                        SpinnerDialogThird = new SpinnerDialogThird(ChooseServiceActivity.this, thirdCateModelArrayList, getResources().getString(R.string.select_sublevelcategory));
                        SpinnerDialogThird.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvhird.setText(item);
                                paramsFour.put("third_id", id);
                                paramsUpdate.put("third_id", id);
                                third_id = id;


                                getServiceName();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    binding1.tvhird.setVisibility(View.GONE);

                    thirdCateModelArrayList.clear();
                    paramsFour.put("third_id", "0");
                    getServiceName();


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

    public void addServices() {
        dialogEditProduct.show();
        // getCategory();
        dialogProduct();

    }

    public void getServiceName() {
        progressDialog.show();
        new HttpsRequest(Consts.getAllservice, paramsFour, ChooseServiceActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {
                        binding1.tvservice.setVisibility(View.VISIBLE);
                        fourModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CommonServiceModel>>() {
                        }.getType();
                        fourModelArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        spinnerDialogFour = new SpinnerDialogFour(ChooseServiceActivity.this, fourModelArrayList, getResources().getString(R.string.select_sname));
                        spinnerDialogFour.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvservice.setText(item);
                                parmsSubcatImage.put("service_name", item);
                                getSubCatImage();
                                binding1.manualPriceEtx.setText(fourModelArrayList.get(position).getPrice());
                                servicename = item;
                                serviceid = id;

                            }
                        });

                        binding1.manualPhotoBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(ChooseServiceActivity.this, "jkjlkjjlk", Toast.LENGTH_SHORT).show();
                                getImage();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    fourModelArrayList.clear();
                    binding1.tvservice.setVisibility(View.GONE);
                    Toast.makeText(ChooseServiceActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void dialogProduct() {

        paramsUpdate = new HashMap<>();
        paramsFile = new HashMap<>();
        paramsucategory = new HashMap<>();
        paramsthird = new HashMap<>();
        paramsFour = new HashMap<>();


        binding1.tvcat.setText(getResources().getString(R.string.all_categories));
        binding1.tvFiltersub.setText(getResources().getString(R.string.all_sub_categories));
        binding1.tvhird.setText(getResources().getString(R.string.all_sub__level_categories));
        binding1.tvservice.setText(getResources().getString(R.string.all_service_name));
        binding1.etvechialNumber.setText("");
        binding1.etImageD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        binding1.tvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                } else {
                    Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvFiltersub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCateModelArrayListserivce.size() > 0) {
                    spinnerDialogsubcate.showSpinerDialog();
                } else {
                    Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.no_sub_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        binding1.tvhird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCateModelArrayList.size() > 0) {
                    SpinnerDialogThird.showSpinerDialog();
                } else {
                    Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.no_sub_cate_level_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fourModelArrayList.size() > 0) {
                    spinnerDialogFour.showSpinerDialog();
                } else {
                    Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.no_service_name_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvNoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDTOS.clear();
                binding1.tvFiltersub.setVisibility(View.GONE);
                binding1.tvhird.setVisibility(View.GONE);
                binding1.tvservice.setVisibility(View.GONE);
                binding1.etvechialNumber.setVisibility(View.GONE);
                vechilenumber = "";
                subCateModelArrayListserivce.clear();
                thirdCateModelArrayList.clear();
                fourModelArrayList.clear();
                dialogEditProduct.dismiss();
                getCategory();
            }
        });

        binding1.tvYesPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String cat = binding1.tvcat.getText().toString().trim();
                String subcat = binding1.tvFiltersub.getText().toString().trim();
                String third = binding1.tvhird.getText().toString().trim();
                String four = binding1.tvservice.getText().toString().trim();
                vechilenumber = binding1.etvechialNumber.getText().toString().trim();


                if (NetworkManager.isConnectToInternet(ChooseServiceActivity.this)) {
                    if (cat.equalsIgnoreCase(getResources().getString(R.string.all_categories))) {
                        Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.val_category), Toast.LENGTH_SHORT).show();
                    } else if (subcat.equalsIgnoreCase(getResources().getString(R.string.all_sub_categories))) {
                        Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.val_subcatogry), Toast.LENGTH_SHORT).show();
                    } else if (four.equalsIgnoreCase(getResources().getString(R.string.all_service_name))) {
                        Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.val_sname), Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkarss(category_id)) {
                            if (third.equalsIgnoreCase(getResources().getString(R.string.all_sub__level_categories))) {
                                Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.val_sublevelcatogry), Toast.LENGTH_SHORT).show();
                            } else if (vechilenumber.equalsIgnoreCase("")) {
                                Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.val_pnumber), Toast.LENGTH_SHORT).show();

                            } else {
                                addProduct();
                            }
                        } else {
                            addProduct();
                        }


                    }


                } else {
                    Toast.makeText(ChooseServiceActivity.this, getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void addProduct() {

        if (image_flag) {

            encodedBase64 = "null";
            send_price_str = "null";
        }
        //Toast.makeText(this, "Image"+encodedBase64, Toast.LENGTH_SHORT).show();
        send_price_str = binding1.manualPriceEtx.getText().toString();
        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.addProduct(category_id, sub_category_id, third_id, userDTO.getUser_id(), servicename, serviceid, vechilenumber, encodedBase64, send_price_str);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("ADD_PRODUCT_RES", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            Toast.makeText(ChooseServiceActivity.this, message, Toast.LENGTH_SHORT).show();


                            binding1.tvFiltersub.setVisibility(View.GONE);
                            binding1.tvhird.setVisibility(View.GONE);
                            binding1.tvservice.setVisibility(View.GONE);
                            binding1.etvechialNumber.setVisibility(View.GONE);
                            vechilenumber = "";
                            categoryDTOS.clear();
                            subCateModelArrayListserivce.clear();
                            thirdCateModelArrayList.clear();
                            fourModelArrayList.clear();
                            dialogEditProduct.dismiss();
                            //  getArtist();
                            getCategory();


                        } else {

                            Toast.makeText(ChooseServiceActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        Toast.makeText(ChooseServiceActivity.this, "Try Again Later ", Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(ChooseServiceActivity.this, "Server Did Not Responding and Try again ", Toast.LENGTH_SHORT).show();


            }
        });

    }

}