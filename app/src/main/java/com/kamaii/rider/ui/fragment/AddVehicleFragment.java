package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.OnSpinerItemClick;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AddVehicleAdapter;
import com.kamaii.rider.ui.models.VehicleModel;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ExpandableHeightGridView;
import com.kamaii.rider.utils.SpinnerDialog;

import org.json.JSONArray;
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

public class AddVehicleFragment extends Fragment {

    private String TAG = AddVehicleFragment.class.getSimpleName();
    LinearLayout add_vehicle_btn;
    public Dialog dialogEditProduct;
    private BaseActivity baseActivity;
    private SpinnerDialog spinnerDialogCate;
    ExpandableHeightGridView add_vehicle_expandablegrid;
    Uri picUri;
    String encodedBase64 = "";
    ProgressDialog progressDialog;
    String vehicleImagePath = "";
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> parmsCategory2 = new HashMap<>();
    private HashMap<String, String> parmsCategory3 = new HashMap<>();
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    CustomTextViewBold vehicle_name_spinner, addvehicle_submit_btn;
    ImageView dialog_vehicle_img,addvehicle_cancel_btn;
    Button dialog_photo_btn;
    CustomEditText dialog_vechial_number;
    AddVehicleAdapter addVehicleAdapter;
    List<VehicleModel> vehicleList = new ArrayList<>();
    String vehicle_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_vehicle));
        init(view);

        add_vehicle_btn = view.findViewById(R.id.add_vehicle_btn);

        add_vehicle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addServices();
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

        getvehicleDisplayList();

        // loadAdapter();
        return view;
    }

    private void loadAdapter() {

        Log.e("rider_charges",""+vehicleList.get(0).getRider_charges());

        addVehicleAdapter = new AddVehicleAdapter(getActivity(), vehicleList, userDTO.getUser_id());
        add_vehicle_expandablegrid.setAdapter(addVehicleAdapter);
        add_vehicle_expandablegrid.setExpanded(true);
    }

    private void init(View view) {

        add_vehicle_expandablegrid = view.findViewById(R.id.add_vehicle_expandablegrid);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please Wait");
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parmsCategory2.put(Consts.USER_ID, userDTO.getUser_id());

        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_vehicle, null, false);
        dialogEditProduct.setContentView(view1);

        dialogEditProduct.setCancelable(false);

       /* binding1 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_add_vehicle, null, false);
        dialogEditProduct.setContentView(binding1.getRoot());

        dialogEditProduct.setCancelable(false);*/
        vehicleList = new ArrayList<>();
        vehicle_name_spinner = view1.findViewById(R.id.vehicle_name_spinner);
        dialog_vehicle_img = view1.findViewById(R.id.dialog_photo_btn);
        addvehicle_submit_btn = view1.findViewById(R.id.addvehicle_submit_btn);
        addvehicle_cancel_btn = view1.findViewById(R.id.addvehicle_cancel_btn);
        dialog_vechial_number = view1.findViewById(R.id.dialog_vechial_number);
    }


    public void getvehicleDisplayList() {

//        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_VEHICLE_DISPLAY_LIST, parmsCategory2, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {

                        String s = response.toString();
                        Log.e("GETVEHICLELIST", "" + s);

                        JSONObject jsonObject = response;
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        vehicleList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Log.e("message", jsonObject1.getString("vehicle_id"));
                            VehicleModel vehicleModel = new VehicleModel();
                            vehicleModel.setImage(jsonObject1.getString("image"));
                            vehicleModel.setVehicle_id(jsonObject1.getString("vehicle_id"));
                            vehicleModel.setVehicle_name(jsonObject1.getString("vehicle_name"));
                            vehicleModel.setVehicle_no(jsonObject1.getString("vehicle_no"));
                            vehicleModel.setApprove_status(jsonObject1.getString("approve_status"));
                            vehicleModel.setRider_charges(jsonObject1.getString("rider_charges"));
                            vehicleModel.setVstatus(jsonObject1.getString("vstatus"));

                            vehicleList.add(vehicleModel);
                        }
                        Log.e("VEHICLELIST", "" + vehicleList.toString());
                        // progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loadAdapter();

                } else {
                    progressDialog.dismiss();
                    //          Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getVehicleData() {


        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_VEHICLE_LIST_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
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

                        spinnerDialogCate = new SpinnerDialog(getActivity(), categoryDTOS, getResources().getString(R.string.all_vehicle));// With 	Animation
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
                    //       Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addServices() {

        vehicle_name_spinner.setText(getResources().getString(R.string.all_vehicle));
     //   dialog_vehicle_img.setImageResource(R.drawable.vehicle_img_placeholder);
        dialog_vechial_number.setText("");
        dialogEditProduct.show();
        // getCategory();
        dialogProduct();

    }

    public void getImage() {


        ImagePicker.Companion.with(getActivity())
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(768)
                //Final image size will be less than 1 MB(Optional)
                .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(23);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 23) {

            Uri imageUri = data.getData();

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
                Glide.with(getActivity()).load(vehicleImagePath).into(dialog_vehicle_img);
                ConcatedEncoded = encodedBase64;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Log.e("PATH",""+builder.toString());
    }


    public void dialogProduct() {

        vehicle_name_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogCate.showSpinerDialog();
            }
        });

        dialog_vehicle_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        getVehicleData();

        addvehicle_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogEditProduct.dismiss();
            }
        });
        addvehicle_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String name = vehicle_name_spinner.getText().toString().trim();
                String num = dialog_vechial_number.getText().toString().trim();

                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (name.equalsIgnoreCase(getResources().getString(R.string.all_categories))) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.val_vahicle), Toast.LENGTH_SHORT).show();
                    } else {
                        if (name.equalsIgnoreCase(getResources().getString(R.string.all_vehicle))) {
                            Toast.makeText(baseActivity, "Select your vehicle", Toast.LENGTH_SHORT).show();
                        } else if (encodedBase64.equalsIgnoreCase("")) {
                            Toast.makeText(baseActivity, "Upload vehicle image", Toast.LENGTH_SHORT).show();
                        } else if (num.equalsIgnoreCase("")) {
                            Toast.makeText(baseActivity, "Enter Vehicle number", Toast.LENGTH_SHORT).show();
                        } else {

                            //        Toast.makeText(baseActivity, ""+encodedBase64, Toast.LENGTH_SHORT).show();
                            parmsCategory3.put("user_id", userDTO.getUser_id());
                            parmsCategory3.put("vehicle_id", vehicle_id);
                            parmsCategory3.put("vehicle_no", num);
                            parmsCategory3.put("vehicle_image", encodedBase64);
                            addProduct();
                        }

                    }


                } else {
                    //   Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void addProduct() {

        progressDialog.show();
        new HttpsRequest(Consts.ADD_VEHICLE_API, parmsCategory3, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {

                        Log.e("DIALOG_CAT", "" + response.toString());
                        JSONObject jsonObject = response;
                        String message = jsonObject.getString("message");
                        progressDialog.dismiss();

                        dialogEditProduct.dismiss();
                        getvehicleDisplayList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    progressDialog.dismiss();
                    // Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;

    }
}