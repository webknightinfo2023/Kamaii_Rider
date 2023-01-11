package com.kamaii.rider.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.DailogArProductBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.OnSelectedItemListener;
import com.kamaii.rider.interfacess.OnSpinerItemClick;
import com.kamaii.rider.model.CommonServiceModel;
import com.kamaii.rider.model.ThirdCateModel;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AdapterCategoryServices;
import com.kamaii.rider.ui.adapter.AdapterServices;
import com.kamaii.rider.ui.adapter.SubCateAdapter;
import com.kamaii.rider.ui.models.CategoryModel;
import com.kamaii.rider.ui.models.RateModel;
import com.kamaii.rider.ui.models.SubCateModel;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ImageCompression;
import com.kamaii.rider.utils.ItemDecorationAlbumColumns;
import com.kamaii.rider.utils.MainFragment;
import com.kamaii.rider.utils.ProjectUtils;
import com.kamaii.rider.utils.SpinnerDialog;
import com.kamaii.rider.utils.SpinnerDialogFour;
import com.kamaii.rider.utils.SpinnerDialogRate;
import com.kamaii.rider.utils.SpinnerDialogSubCate;
import com.kamaii.rider.utils.SpinnerDialogThird;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Services extends Fragment {

    private String TAG = Services.class.getSimpleName();
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private RecyclerView rv_services,rv_services_cat,rv_services_sub_car;
    private ArrayList<ProductDTO> productDTOList;
    private AdapterServices adapterServices;
    private Bundle bundle;
    private GridLayoutManager gridLayoutManager;
    private RelativeLayout rlView;
    private CustomTextViewBold tvNotFound;
    private HashMap<String, String> paramsUpdate;
    public Dialog dialogEditProduct;
    private HashMap<String, File> paramsFile;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    File file=null;
    Bitmap bitmap = null;
    DailogArProductBinding binding1;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private ArrayList<CategoryModel> categoryarraylist = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayList=new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListserivce=new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListnew=new ArrayList<>();
    ArrayList<ThirdCateModel> thirdCateModelArrayList=new ArrayList<>();
    ArrayList<CommonServiceModel> fourModelArrayList=new ArrayList<>();
    ArrayList<ProductDTO> productDTOArrayListnew=new ArrayList<>();
    ArrayList<RateModel> rateArrayList=new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    private SpinnerDialogSubCate spinnerDialogsubcate;
    private SpinnerDialogThird SpinnerDialogThird;
    private SpinnerDialogFour spinnerDialogFour;
    private SpinnerDialogRate spinnerDialogRate;
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> paramsucategory = new HashMap<>();
    private HashMap<String, String> paramsrate = new HashMap<>();
    private HashMap<String, String> paramsthird = new HashMap<>();
    private HashMap<String, String> paramsFour = new HashMap<>();
    ViewFlipper viewflipper;
    AdapterCategoryServices adapterCategoryServices;
    SubCateAdapter subCateAdapter;
    private HashMap<String, String> parms = new HashMap<>();
    String catidmain="";
    private BaseActivity baseActivity;
    ProgressDialog progressDialog;
    String category_id="",sub_category_id="",third_id="",vechilenumber="",servicename="",serviceid="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.serviceee));
        bundle = this.getArguments();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding1 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dailog_ar_product, null, false);
        dialogEditProduct.setContentView(binding1.getRoot());


        dialogEditProduct.setCancelable(false);
       // artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        showUiAction(view);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        getCategory();
       // getrate();
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        if (NetworkManager.isConnectToInternet(getActivity())) {

            getArtist();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void getArtist() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {

                        artistDetailsDTO=new ArtistDetailsDTO();
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void showUiAction(View v) {
        rv_services = v.findViewById(R.id.rv_services);
        rv_services_cat = v.findViewById(R.id.rv_services_cat);
        rv_services_sub_car = v.findViewById(R.id.rv_services_sub_car);


        viewflipper =  v.findViewById(R.id.viewflipper);
        tvNotFound = v.findViewById(R.id.tvNotFound);
        rlView = v.findViewById(R.id.rlView);

        builder = new BottomSheet.Builder(getActivity()).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                        //Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);
                                        picUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", file);
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
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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

      //  showData();

    }

    public void getCategory() {


        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);


                        Collections.sort(categoryDTOS, new Comparator<CategoryDTO>() {
                            @Override
                            public int compare(CategoryDTO lhs, CategoryDTO rhs) {
                                return lhs.getCat_name().compareTo(rhs.getCat_name());
                            }
                        });
                        progressDialog.dismiss();

                        spinnerDialogCate = new SpinnerDialog(getActivity(), categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvcat.setText(item);
                                paramsucategory.put(Consts.CATEGORY_ID, id);
                                paramsthird.put(Consts.CATEGORY_ID, id);
                                paramsUpdate.put(Consts.CATEGORY_ID, id);
                                paramsFour.put("cat_id", id);
                                category_id=id;

                                binding1.tvFiltersub.setText(getResources().getString(R.string.all_sub_categories));
                                binding1.tvhird.setText(getResources().getString(R.string.all_sub__level_categories));
                                binding1.tvservice.setText(getResources().getString(R.string.all_service_name));
                                binding1.etvechialNumber.setText("");
                                if(checkarss(category_id)){
                                    binding1.etvechialNumber.setVisibility(View.VISIBLE);
                                }else {
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
                    Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void getSubCategory() {

      progressDialog.show();
        new HttpsRequest(Consts.GET_SERVICE_SUB_CATEGORY_API, paramsucategory, getActivity()).stringPost(TAG, new Helper() {
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
                        spinnerDialogsubcate = new SpinnerDialogSubCate(getActivity(), subCateModelArrayListserivce, getResources().getString(R.string.select_subcategory));
                        spinnerDialogsubcate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvFiltersub.setText(item);
                                paramsUpdate.put(Consts.sub_category_id, id);
                                paramsthird.put(Consts.sub_category_id, id);
                                paramsFour.put("sub_id", id);

                                sub_category_id=id;
                                getThirdCategory();

                            }
                        });



                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void getThirdCategory() {
        progressDialog.show();

        new HttpsRequest(Consts.get_sublevel_cat, paramsthird, getActivity()).stringPost(TAG, new Helper() {
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
                        SpinnerDialogThird = new SpinnerDialogThird(getActivity(), thirdCateModelArrayList, getResources().getString(R.string.select_sublevelcategory));
                        SpinnerDialogThird.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvhird.setText(item);
                                paramsFour.put("third_id", id);
                                paramsUpdate.put("third_id", id);
                                third_id=id;


                                getServiceName();
                            }
                        });



                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    binding1.tvhird.setVisibility(View.GONE);

                    thirdCateModelArrayList.clear();
                    paramsFour.put("third_id","0");
                   // Toast.makeText(getActivity(),"Not Found",Toast.LENGTH_SHORT).show();
                    getServiceName();


                }
            }
        });
    }

    public   boolean checkarss(String catid){

        boolean value=false;
        for(int i=0;i<BaseActivity.addressModelArrayList.size();i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value= true;
                break;

            } else {
                value= false;

            }

        }
        return value;
    }

    public void getServiceName() {
        progressDialog.show();
        new HttpsRequest(Consts.getAllservice, paramsFour, getActivity()).stringPost(TAG, new Helper() {
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

                        spinnerDialogFour = new SpinnerDialogFour(getActivity(), fourModelArrayList,getResources().getString(R.string.select_sname));
                        spinnerDialogFour.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvservice.setText(item);
                               servicename=item;
                              serviceid=id;

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    fourModelArrayList.clear();
                    binding1.tvservice.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getrate() {
        new HttpsRequest(Consts.GET_RATE, paramsrate, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        rateArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<RateModel>>() {
                        }.getType();
                        rateArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        spinnerDialogRate = new SpinnerDialogRate(getActivity(), rateArrayList, getResources().getString(R.string.select_rate));// With 	Animation
                        spinnerDialogRate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvrate.setText(item);


                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void showData() {
        categoryarraylist=new ArrayList<>();
        categoryarraylist = artistDetailsDTO.getCategory();

        if (categoryarraylist.size()<=0){
           // categoryarraylist.add(new CategoryModel());
        }
        rv_services_cat.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rv_services_cat.addItemDecoration(new ItemDecorationAlbumColumns(
                2,
                4));

        Collections.sort(categoryarraylist, new Comparator<CategoryModel>() {
            @Override
            public int compare(CategoryModel lhs, CategoryModel rhs) {
                return lhs.getCat_name().compareTo(rhs.getCat_name());
            }
        });
        adapterCategoryServices=new AdapterCategoryServices(Services.this,categoryarraylist,onItemListener);
        rv_services_cat.setAdapter(adapterCategoryServices);

      if (categoryarraylist.size() > 0)
        {
            tvNotFound.setVisibility(View.GONE);
            adapterCategoryServices.notifyDataSetChanged();
        }


        subCateModelArrayList=new ArrayList<>();
        subCateModelArrayList = artistDetailsDTO.getSubcategory();


        if (subCateModelArrayList.size()<=0){
            subCateModelArrayListnew.addAll(subCateModelArrayList);
        }
        productDTOArrayListnew = new ArrayList<>();
        if (subCateModelArrayListnew.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
            rv_services_sub_car.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            rv_services_sub_car.addItemDecoration(new ItemDecorationAlbumColumns(
                    2,
                    4));

            Collections.sort(subCateModelArrayListnew, new Comparator<SubCateModel>() {
                @Override
                public int compare(SubCateModel lhs, SubCateModel rhs) {
                    return lhs.getCat_name().compareTo(rhs.getCat_name());
                }
            });
            subCateAdapter=new SubCateAdapter(getActivity(),subCateModelArrayListnew,onsubItemListener);
            rv_services_sub_car.setAdapter(subCateAdapter);
        } else {
            if(categoryarraylist.size()>=0){
                tvNotFound.setVisibility(View.GONE);

            }else {
                tvNotFound.setVisibility(View.VISIBLE);

            }
            rv_services_sub_car.setVisibility(View.GONE);

        }




        if(categoryarraylist.size()>0){
            productDTOList = new ArrayList<>();

            productDTOList = artistDetailsDTO.getProducts();

            if (productDTOArrayListnew.size()<=0){
                productDTOArrayListnew.addAll(productDTOList);
            }
            if (productDTOArrayListnew.size() > 0)
            {
                gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                adapterServices = new AdapterServices(Services.this, productDTOArrayListnew);
                rv_services.setLayoutManager(gridLayoutManager);
                rv_services.addItemDecoration(new ItemDecorationAlbumColumns(
                        2,
                        4));
                rv_services.setAdapter(adapterServices);

            } else {


            }
        }


    }
    private ArrayList<SubCateModel> getIndexByProperty(String yourString)
    {
        ArrayList<SubCateModel> temo=new ArrayList<>();

        for (int i = 0; i < subCateModelArrayList.size(); i++) {
            if (subCateModelArrayList !=null && subCateModelArrayList.get(i).getCat_id().equalsIgnoreCase(yourString)) {
               temo.add(subCateModelArrayList.get(i));
            }
        }
        return temo;// not there is list
    }
    private ArrayList<ProductDTO> getIndexByPropertyproduct(String catid, String subid)
    {
        ArrayList<ProductDTO> temo=new ArrayList<>();

        for (int i = 0; i < productDTOList.size(); i++) {
            if (productDTOList !=null && productDTOList.get(i).getCategory_id().equalsIgnoreCase(catid)  && productDTOList.get(i).getSub_category_id().equalsIgnoreCase(subid)) {
                temo.add(productDTOList.get(i));
            }
        }
        return temo;// not there is list
    }
    private OnSelectedItemListener onItemListener=new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position, String name, String shipping, String mylocation) {

            catidmain = categoryarraylist.get(position).getId();
            subCateModelArrayListnew= getIndexByProperty(catidmain);

            if (subCateModelArrayList.size()<=0){
                subCateModelArrayListnew.addAll(subCateModelArrayList);
            }

            if (subCateModelArrayListnew.size() > 0) {
                tvNotFound.setVisibility(View.GONE);
                rv_services_sub_car.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                rv_services_sub_car.addItemDecoration(new ItemDecorationAlbumColumns(
                        2,
                        4));
                // ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_padding);
                //   rec_category.addItemDecoration(itemDecoration);
                subCateAdapter=new SubCateAdapter(getActivity(),subCateModelArrayListnew,onsubItemListener);
                rv_services_sub_car.setAdapter(subCateAdapter);
            } else {
                tvNotFound.setVisibility(View.VISIBLE);
                rv_services_sub_car.setVisibility(View.GONE);
            }
            subCateAdapter.notifyDataChanged(subCateModelArrayListnew);
            viewflipper.showNext();

        }
    };
    private OnSelectedItemListener onsubItemListener=new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position, String name, String shipping, String mylocation) {

            String subid = selectionString;
            productDTOArrayListnew= getIndexByPropertyproduct(catidmain,subid);
         //   adapterServices.notifyDataSetChanged();
            adapterServices = new AdapterServices(Services.this, productDTOArrayListnew);
            rv_services.setAdapter(adapterServices);
            viewflipper.showNext();

        }
    };
    public void getParentData() {

        if (NetworkManager.isConnectToInternet(getActivity())) {
            getArtist();


        } else {
            Toast.makeText(getActivity(),getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

        }
    }
    public void getrefresh() {

        adapterServices.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getArtist();

        } else {
            Toast.makeText(getActivity(),getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

        }

    }

    public void addServices() {
        dialogEditProduct.show();
        dialogProduct();

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
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvFiltersub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCateModelArrayListserivce.size() > 0) {
                    spinnerDialogsubcate.showSpinerDialog();
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_sub_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        binding1.tvhird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCateModelArrayList.size() > 0) {
                    SpinnerDialogThird.showSpinerDialog();
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_sub_cate_level_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fourModelArrayList.size() > 0) {
                    spinnerDialogFour.showSpinerDialog();
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_service_name_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        binding1.tvrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rateArrayList.size() > 0) {
                    spinnerDialogRate.showSpinerDialog();
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_rate_found), Toast.LENGTH_SHORT).show();

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
                vechilenumber="";
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

                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        String cat=binding1.tvcat.getText().toString().trim();
                        String subcat=binding1.tvFiltersub.getText().toString().trim();
                        String third=binding1.tvhird.getText().toString().trim();
                        String four=binding1.tvservice.getText().toString().trim();
                        vechilenumber=binding1.etvechialNumber.getText().toString().trim();


                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            if (cat.equalsIgnoreCase(getResources().getString(R.string.all_categories))) {
                                Toast.makeText(getActivity(),getResources().getString(R.string.val_category), Toast.LENGTH_SHORT).show();
                            }
                            else if (subcat.equalsIgnoreCase(getResources().getString(R.string.all_sub_categories))) {
                                Toast.makeText(getActivity(),getResources().getString(R.string.val_subcatogry), Toast.LENGTH_SHORT).show();
                            }

                            else if (four.equalsIgnoreCase(getResources().getString(R.string.all_service_name))) {
                                Toast.makeText(getActivity(),getResources().getString(R.string.val_sname), Toast.LENGTH_SHORT).show();
                            }


                            else {
                                if(checkarss(category_id)){
                                    if (third.equalsIgnoreCase(getResources().getString(R.string.all_sub__level_categories))) {
                                        Toast.makeText(getActivity(),getResources().getString(R.string.val_sublevelcatogry), Toast.LENGTH_SHORT).show();
                                    }else if(vechilenumber.equalsIgnoreCase("")){
                                        Toast.makeText(getActivity(),getResources().getString(R.string.val_pnumber), Toast.LENGTH_SHORT).show();

                                    }else {
                                       // addProduct();
                                    }
                                }else {
                                    //addProduct();
                                }


                            }



                        } else {
                            Toast.makeText(getActivity(),getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);
                                binding1.etImageD.setText(imagePath);
                                Log.e("image", imagePath);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);
                                binding1.etImageD.setText(imagePath);
                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence
                        .getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }

}
