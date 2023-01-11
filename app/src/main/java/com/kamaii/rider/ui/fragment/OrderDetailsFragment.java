package com.kamaii.rider.ui.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentOrderDetailsBinding;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.OrderDetailsParentAdapter;
import com.kamaii.rider.ui.adapter.neworder.NewOrderDetailsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDetailsFragment extends Fragment {

    FragmentOrderDetailsBinding binding;
    ProgressDialog progressDialog;
    private SharedPrefrence prefrence;
    private SharedPreferences firebase;
    private UserDTO userDTO;
    List<ArtistBooking> orderList;
    private ArrayList<ArtistBooking> artistBookingsListnew = new ArrayList<>();
    private BaseActivity baseActivity;
    int current_page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_order_details, container, false);

        prefrence = SharedPrefrence.getInstance(getActivity());
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText("Order Summery");

        baseActivity.ivLogo.setVisibility(View.GONE);
//        baseActivity.ivEditPersonal.setVisibility(View.GONE);
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);
//        baseActivity.editImage.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        getOrders(current_page);

        binding.newbookingDetailsScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    current_page++;
                    //   Log.e("CURRENT_PAGE", "" + current_page + " count "+ totalItemCount);
                    Log.e("CURRENT_PAGE", "" + current_page);
                    // getBookings2(current_page);
                    binding.idPBLoading.setVisibility(View.VISIBLE);
                    getOrders(current_page);
                }
            }
        });
       return binding.getRoot();
    }

    private void getOrders(int c) {

        Log.e("getOrders lazy",""+c);
        //    progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtist(userDTO.getUser_id(),String.valueOf(c));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //       progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("getOrders", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                binding.tvNo.setVisibility(View.GONE);
                                orderList = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                                orderList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                Log.e("orderArrayList", " 2 " + orderList.size());
                                for (int i=0;i<orderList.size();i++){
                                    artistBookingsListnew.add(orderList.get(i));
                                }

                                if (artistBookingsListnew.size()!=0){
                                    binding.tvNo.setVisibility(View.GONE);

                                }
                                if (artistBookingsListnew.size() >=3) {

                                    binding.idPBLoading.setVisibility(View.VISIBLE);
                                }
                                setOrderAdapter();
                                //  showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            binding.idPBLoading.setVisibility(View.GONE);

                            if (artistBookingsListnew.size()==0){

                                binding.tvNo.setVisibility(View.VISIBLE);
                            }//                            progressDialog.dismiss();
                            //   showData();
                            // binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        if (artistBookingsListnew.size()==0){

                            binding.tvNo.setVisibility(View.VISIBLE);
                        }
                        binding.idPBLoading.setVisibility(View.GONE);

//                        progressDialog.dismiss();

                       /* Toast.makeText(getActivity(), "Please try again later",
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

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });


    }

    private void setOrderAdapter() {

        binding.orderDetailsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderDetailsParentAdapter adapter = new OrderDetailsParentAdapter(getContext(),artistBookingsListnew,baseActivity);
        binding.orderDetailsRv.setAdapter(adapter);

        /*binding.orderDetailsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        NewOrderDetailsAdapter adapter = new NewOrderDetailsAdapter(getContext(), artistBookingsListnew);
        binding.orderDetailsRv.setAdapter(adapter);*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}