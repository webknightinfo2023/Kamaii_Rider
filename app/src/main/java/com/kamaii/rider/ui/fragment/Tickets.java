package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.HistoryDTO;
import com.kamaii.rider.DTO.TicketDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.OnSpinerItemClick;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.TicketAdapter;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;
import com.kamaii.rider.utils.SpinnerDialogInvoice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class Tickets extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = Tickets.class.getSimpleName();
    private RecyclerView RVhistorylist;
    private TicketAdapter ticketAdapter;
    private ArrayList<TicketDTO> ticketDTOSList;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private View view;
    private BaseActivity baseActivity;
    private ImageView ivPost;
    private Dialog dialog;
    private CustomEditText etReason, etDescription;
    private CustomTextView tvCancel, tvAdd;
    private HashMap<String, String> parmsadd = new HashMap<>();
    private ArrayList<HistoryDTO> historyDTOList=new ArrayList<>();
    SpinnerDialogInvoice spinnerDialogInvoice;
    CustomTextViewBold tvcat;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ticket, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.support));
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getHistroy();

        } else {
        //    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        if (NetworkManager.isConnectToInternet(getActivity())) {
            getTicket();

        } else {
        //    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
        setUiAction(view);
        return view;
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getHistroy();
    }
    public void getHistroy() {
        progressDialog.show();


        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getMyInvoice(userDTO.getUser_id(),"1");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus==1)
                        {

                            try {
                                historyDTOList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<HistoryDTO>>() {
                                }.getType();
                                historyDTOList = (ArrayList<HistoryDTO>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                                spinnerDialogInvoice = new SpinnerDialogInvoice(getActivity(), historyDTOList, getResources().getString(R.string.select_invoice_id));// With 	Animation
                                spinnerDialogInvoice.bindOnSpinerListener(new OnSpinerItemClick() {
                                    @Override
                                    public void onClick(String item, String id, int position) {
                                        tvcat.setText(item);

                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), message,
                                    LENGTH_LONG).show();
                        }

                    }
                    else {
                     /*   Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
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


            }
        });

    }

    public HashMap<String, String> getparminvoice() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ROLE, "1");
        return parms;
    }

    public void setUiAction(View v) {
        tvNo = v.findViewById(R.id.tvNo);
        RVhistorylist = v.findViewById(R.id.RVhistorylist);
        ivPost = v.findViewById(R.id.ivPost);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVhistorylist.setLayoutManager(mLayoutManager);

        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogshow();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void getTicket() {
        progressDialog.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getMyTicket(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {

                        tvNo.setVisibility(View.GONE);
                        RVhistorylist.setVisibility(View.VISIBLE);
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus==1)
                        {

                            try {
                                ticketDTOSList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<TicketDTO>>() {
                                }.getType();
                                ticketDTOSList = (ArrayList<TicketDTO>) new Gson().fromJson(object.getJSONArray("my_ticket").toString(), getpetDTO);
                                showData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            tvNo.setVisibility(View.VISIBLE);
                            RVhistorylist.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), message,
                                    LENGTH_LONG).show();
                        }

                    }
                    else {
                       /* Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                                LENGTH_LONG).show();
*/

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public void showData() {
        ticketAdapter = new TicketAdapter(Tickets.this, ticketDTOSList, userDTO);
        RVhistorylist.setAdapter(ticketAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }


    public void dialogshow() {
        dialog = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_add_ticket);

        etDescription = (CustomEditText) dialog.findViewById(R.id.etDescription);
        etReason = (CustomEditText) dialog.findViewById(R.id.etReason);
        tvCancel = (CustomTextView) dialog.findViewById(R.id.tvCancel);
        tvAdd = (CustomTextView) dialog.findViewById(R.id.tvAdd);

        tvcat=dialog.findViewById(R.id.tvcat);

        tvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyDTOList.size() != 0) {
                    spinnerDialogInvoice.showSpinerDialog();
                } else {
                    ProjectUtils.showLong(getActivity(), getResources().getString(R.string.no_cate_found));
                }
            }
        });
        dialog.show();
        dialog.setCancelable(false);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }

    public void submitForm() {

        String cat=tvcat.getText().toString().trim();

        if (cat.equalsIgnoreCase(getResources().getString(R.string.select_invoice_id))) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_iid), Toast.LENGTH_SHORT).show();
        }
        else if (!validateReason()) {
            return;
        } else if (!validateDescription()) {
            return;
        } else {
            addTicket();

        }

    }

    public boolean validateReason() {
        if (etReason.getText().toString().trim().equalsIgnoreCase("")) {
            etReason.setError(getResources().getString(R.string.val_title));
            etReason.requestFocus();
            return false;
        } else {
            etReason.setError(null);
            etReason.clearFocus();
            return true;
        }
    }

    public boolean validateDescription() {
        if (etDescription.getText().toString().trim().equalsIgnoreCase("")) {
            etDescription.setError(getResources().getString(R.string.val_description));
            etDescription.requestFocus();
            return false;
        } else {
            etDescription.setError(null);
            etDescription.clearFocus();
            return true;
        }
    }


    public void addTicket() {
        parmsadd.put(Consts.REASON, ProjectUtils.getEditTextValue(etDescription));
        parmsadd.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(etReason));
        parmsadd.put(Consts.USER_ID, userDTO.getUser_id());
        parmsadd.put(Consts.INVOICEID, tvcat.getText().toString());

        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GENERATE_TICKET_API, parmsadd, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    dialog.dismiss();
               //     ProjectUtils.showToast(getActivity(), msg);
                    getTicket();
                } else {
                 //   ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }
}
