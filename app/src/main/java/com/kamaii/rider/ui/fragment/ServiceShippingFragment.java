package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.DTO.ShippingDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.OnSelectedItemListener;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AdapterCategoryShippingServices;
import com.kamaii.rider.ui.adapter.SubCateShippingAdapter;
import com.kamaii.rider.ui.models.CategoryModel;
import com.kamaii.rider.ui.models.SubCateModel;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ItemDecorationAlbumColumns;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ServiceShippingFragment extends Fragment {

    private String TAG = ServiceShippingFragment.class.getSimpleName();
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private RecyclerView rv_services_cat, rv_services_sub_car;
    private CustomTextViewBold tvNotFound;
    private Dialog dialogEditProduct;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private ArrayList<CategoryModel> categoryarraylist = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayList = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListnew = new ArrayList<>();
    ArrayList<ProductDTO> productDTOArrayListnew = new ArrayList<>();
    private HashMap<String, String> parmsaddproduct = new HashMap<>();
    ViewFlipper viewflipper;
    AdapterCategoryShippingServices adapterCategoryServices;
    SubCateShippingAdapter subCateAdapter;
    private HashMap<String, String> parms = new HashMap<>();
    CustomTextViewBold tvtitle, tvNo, tvYesPro;
    String catidmain = "";
    private BaseActivity baseActivity;
    CheckBox cbshipping, cbmylocation;
    String shipping = "", mylocation = "", subcatid = "", maxprice = "1";
    private HashMap<String, String> parmsshipping = new HashMap<>();
    ShippingDTO shippingDTO;
    private ArrayList<ShippingDTO> shippingDTOArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    CustomEditText edt_s_price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services_shippping, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.serviceee));

        // artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        showUiAction(view);
        parmsaddproduct.put(Consts.USER_ID, userDTO.getUser_id());
        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditProduct.setContentView(R.layout.dailog_service_shipping);
        tvtitle = dialogEditProduct.findViewById(R.id.tvtitle);
        tvNo = dialogEditProduct.findViewById(R.id.tvNoedit);
        tvYesPro = dialogEditProduct.findViewById(R.id.tvYesPro);
        cbshipping = dialogEditProduct.findViewById(R.id.cbshipping);
        cbmylocation = dialogEditProduct.findViewById(R.id.cbmylocation);
        edt_s_price = dialogEditProduct.findViewById(R.id.edt_s_price);
        dialogEditProduct.setCancelable(true);
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
        // ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        // ProjectUtils.showToast(getActivity(), msg);
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


    public void getshippingproduct(final String subid, final String name) {

        parmsshipping.put(Consts.USER_ID, userDTO.getUser_id());
        parmsshipping.put(Consts.SUB_CAT_ID, subid);
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_SERVICE_SHIPPING, parmsshipping, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        // shippingDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ShippingDTO.class);


                        JSONArray jsonarray = response.getJSONArray("data");

                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject c = jsonarray.getJSONObject(i);


                            String id = c.getString("id");
                            String sub_cat_id = c.getString("sub_cat_id");
                            String servershipping = c.getString("shipping");
                            String my_location = c.getString("my_location");
                            String smaxPrice = c.getString("maxprice");

                            shippingDTO = new ShippingDTO();

                            shippingDTO.setId(id);
                            shippingDTO.setSub_cat_id(sub_cat_id);
                            shippingDTO.setShipping(servershipping);
                            shippingDTO.setMy_location(my_location);
                            shippingDTO.setMaxprice(smaxPrice);

                            shippingDTOArrayList.add(shippingDTO);


                            for (int j = 0; j < shippingDTOArrayList.size(); j++) {
                                if (shippingDTOArrayList.get(j).getSub_cat_id().equalsIgnoreCase(subid)) {

                                    if (shippingDTOArrayList.get(j).getShipping().equalsIgnoreCase("0")) {
                                        shipping = "0";

                                    } else {
                                        shipping = "1";
                                    }

                                    if (shippingDTOArrayList.get(j).getMy_location().equalsIgnoreCase("0")) {
                                        mylocation = "0";

                                    } else {
                                        mylocation = "1";
                                    }

                                    maxprice = smaxPrice;
                                    dialogProduct(shipping, mylocation, name, subid, maxprice);
                                }
                            }
                        }


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    // ProjectUtils.showToast( Booking.this, msg);

                } else {
                    dialogProduct("1", "1", name, subid, maxprice);
                }


            }
        });
    }

    public void showUiAction(View v) {

        rv_services_cat = v.findViewById(R.id.rv_services_cat);
        rv_services_sub_car = v.findViewById(R.id.rv_services_sub_car);


        viewflipper = v.findViewById(R.id.viewflipper);
        tvNotFound = v.findViewById(R.id.tvNotFound);


    }


    public void showData() {

        //getshippingproduct();
        categoryarraylist = new ArrayList<>();
        categoryarraylist = artistDetailsDTO.getCategory();

        if (categoryarraylist.size() <= 0) {
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
        adapterCategoryServices = new AdapterCategoryShippingServices(getActivity(), categoryarraylist, onItemListener);
        rv_services_cat.setAdapter(adapterCategoryServices);

        if (categoryarraylist.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
            adapterCategoryServices.notifyDataSetChanged();
        }


        subCateModelArrayList = new ArrayList<>();
        subCateModelArrayList = artistDetailsDTO.getSubcategory();


        if (subCateModelArrayList.size() <= 0) {
            subCateModelArrayListnew.addAll(subCateModelArrayList);
        }
        productDTOArrayListnew = new ArrayList<>();
        if (subCateModelArrayListnew.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
            rv_services_sub_car.setLayoutManager(new GridLayoutManager(getActivity(), 1));


            Collections.sort(subCateModelArrayListnew, new Comparator<SubCateModel>() {
                @Override
                public int compare(SubCateModel lhs, SubCateModel rhs) {
                    return lhs.getCat_name().compareTo(rhs.getCat_name());
                }
            });
            subCateAdapter = new SubCateShippingAdapter(getActivity(), subCateModelArrayListnew, onsubItemListener, userDTO.getUser_id());
            rv_services_sub_car.setAdapter(subCateAdapter);
        } else {
            if (categoryarraylist.size() >= 0) {
                tvNotFound.setVisibility(View.GONE);

            } else {
                tvNotFound.setVisibility(View.VISIBLE);

            }
            rv_services_sub_car.setVisibility(View.GONE);

        }

    }


    private ArrayList<SubCateModel> getIndexByProperty(String yourString) {
        ArrayList<SubCateModel> temo = new ArrayList<>();

        for (int i = 0; i < subCateModelArrayList.size(); i++) {
            if (subCateModelArrayList != null && subCateModelArrayList.get(i).getCat_id().equalsIgnoreCase(yourString)) {
                temo.add(subCateModelArrayList.get(i));
            }
        }
        return temo;// not there is list
    }

    private OnSelectedItemListener onItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position, String name, String shipping, String mylocation) {

            catidmain = categoryarraylist.get(position).getId();
            subCateModelArrayListnew = getIndexByProperty(catidmain);

            if (subCateModelArrayList.size() <= 0) {
                subCateModelArrayListnew.addAll(subCateModelArrayList);
            }

            if (subCateModelArrayListnew.size() > 0) {
                tvNotFound.setVisibility(View.GONE);
                rv_services_sub_car.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                  subCateAdapter = new SubCateShippingAdapter(getActivity(), subCateModelArrayListnew, onsubItemListener, userDTO.getUser_id());
                rv_services_sub_car.setAdapter(subCateAdapter);
            } else {
                tvNotFound.setVisibility(View.VISIBLE);
                rv_services_sub_car.setVisibility(View.GONE);
            }
            subCateAdapter.notifyDataChanged(subCateModelArrayListnew);
            viewflipper.showNext();

        }
    };
    private OnSelectedItemListener onsubItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position, String name, String shipping, String mylocation) {


            getshippingproduct(selectionString, name);
            // productDTOArrayListnew= getIndexByPropertyproduct(catidmain,subid);
            //   adapterServices.notifyDataSetChanged();
            // adapterServices = new AdapterServices(ServiceShippingFragment.this, productDTOArrayListnew);
            // rv_services.setAdapter(adapterServices);
            //  viewflipper.showNext();
            // dialogProduct(selectionString,position,name,shipping,mylocation);
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getArtist();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

    }


    public void dialogProduct(String sshipping, String smylocation, String name, String subid, String maxpricee) {


        dialogEditProduct.show();

        tvtitle.setText(name);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditProduct.dismiss();

            }
        });

        subcatid = subid;
        shipping = sshipping;
        mylocation = smylocation;

        edt_s_price.setText(maxpricee);
        if (sshipping.equalsIgnoreCase("1")) {
            cbshipping.setChecked(true);
        } else if (sshipping.equalsIgnoreCase("0")) {
            cbshipping.setChecked(false);
        }


        if (smylocation.equalsIgnoreCase("1")) {
            cbmylocation.setChecked(true);
        } else if (smylocation.equalsIgnoreCase("0")) {
            cbmylocation.setChecked(false);
        }


        cbshipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    shipping = "1";
                    // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                } else {
                    shipping = "0";
                    //  Toast.makeText(getActivity(),"0",Toast.LENGTH_LONG).show();
                }
            }
        });

        cbmylocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mylocation = "1";
                    // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                } else {
                    mylocation = "0";
                    //Toast.makeText(getActivity(),"0",Toast.LENGTH_LONG).show();
                }
            }
        });
        tvYesPro.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (NetworkManager.isConnectToInternet(getActivity())) {

                            if (cbshipping.isChecked() || cbmylocation.isChecked()) {
                                //  Toast.makeText(getActivity(),shipping+""+mylocation,Toast.LENGTH_LONG).show();
                                // shipping="1";

                                if (!edt_s_price.getText().toString().equalsIgnoreCase("")) {

                                    maxprice = edt_s_price.getText().toString();
                                    addProduct();
                                } else {
                                    Toast.makeText(getActivity(), "Please Enter Price", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), "Please Select Atleast One Value", Toast.LENGTH_LONG).show();

                            }

                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }

                    }
                });

    }

    public void addProduct() {

        parmsaddproduct.put(Consts.SUB_CAT_ID, subcatid);
        parmsaddproduct.put(Consts.SHIPPING, shipping);
        parmsaddproduct.put(Consts.MY_LOCATION, mylocation);
        parmsaddproduct.put(Consts.MAX_PRICE, maxprice);
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADD_SERVICE_SHIPPING, parmsaddproduct, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(getActivity(), msg);
                    dialogEditProduct.dismiss();
                    subCateAdapter.notifyDataChanged(subCateModelArrayListnew);
                    //subCateAdapter=new SubCateShippingAdapter(getActivity(),subCateModelArrayListnew,onsubItemListener,userDTO.getUser_id());
                    //  rv_services_sub_car.setAdapter(subCateAdapter);
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                    dialogEditProduct.dismiss();
                }


            }
        });
    }


}
