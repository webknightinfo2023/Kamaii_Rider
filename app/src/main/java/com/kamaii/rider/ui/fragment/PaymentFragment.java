package com.kamaii.rider.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentPaymentBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.apiRest;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class PaymentFragment extends Fragment implements View.OnClickListener {

    BaseActivity baseActivity;
    FragmentPaymentBinding binding;
    HashMap<String, String> params = new HashMap<>();
    HashMap<String, String> requestParams = new HashMap<>();
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    String msg = "";
    String myamount = "";
    String partner_image = "";
    String partner_name = "";
    String partner_id = "";
    String handover_text = "";
    boolean pay_flag = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.headerNameTV.setText("Payable Amount");
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        getData();
        binding.paymentSubmit.setOnClickListener(this);

        return binding.getRoot();
    }

    private void getData() {

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getCashDepositeDetail(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //       progressDialog.dismiss();
                Log.e("CASHDIPOSITE_RESPONSE", " response " + response.toString());

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("Live_ADDRESS", " response " + s);

                        Log.e("tracker123456", "1" + s);
                        JSONObject object = new JSONObject(s);

                        //Log.e("ERROR_RES",""+s);
                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            myamount = object.getString("myamount");
                            partner_image = object.getString("partner_image");
                            partner_name = object.getString("partner_name");
                            partner_id = object.getString("partner_id");
                            handover_text = object.getString("handover_text");

                            Glide.with(getActivity()).load(Uri.parse(partner_image)).into(binding.partnerImage);
                            binding.paymentPartnerName.setText(partner_name);
                            binding.paymentTxt.setText(handover_text);
                            binding.paymentAmountEtx.setMaxEms(Integer.parseInt(myamount));
                            binding.paymentAmountEtx.setText(myamount);

                        } else if (sstatus == 2) {


                            myamount = object.getString("myamount");
                            partner_image = object.getString("partner_image");
                            partner_name = object.getString("partner_name");
                            partner_id = object.getString("partner_id");
                            handover_text = object.getString("handover_text");

                            Glide.with(getActivity()).load(Uri.parse(partner_image)).into(binding.partnerImage);
                            binding.paymentPartnerName.setText(partner_name);
                            binding.paymentTxt.setText(handover_text);
                            binding.paymentAmountEtx.setMaxEms(Integer.parseInt(myamount));
                            binding.paymentAmountEtx.setText(myamount);

                            binding.paymentSubmit.setVisibility(View.GONE);
                            binding.tvNoedit.setVisibility(View.VISIBLE);
                            binding.paymentAmountEtx.setEnabled(false);
                            binding.paymentAmountEtx.setClickable(false);
                            binding.paymentAmountEtx.setFocusable(false);
                        } else {

                            binding.paymentCard.setVisibility(View.GONE);
                            binding.paymentNoDataFound.setVisibility(View.VISIBLE);

                        }
                    } else {
                        Log.e("tracker123456", "1 else");

                        Toast.makeText(getActivity(), "Try Again Later ",
                                LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Server Did Not Responding and Try again ",
                        LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = ((BaseActivity) context);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.payment_submit) {

            binding.paymentSubmit.setClickable(false);
            binding.paymentSubmit.setEnabled(false);
            binding.paymentSubmit.setVisibility(View.GONE);
            binding.tvNoedit.setVisibility(View.VISIBLE);
            pay_flag = true;
            if (!binding.paymentAmountEtx.getText().toString().isEmpty()) {

                int amt = Integer.parseInt(binding.paymentAmountEtx.getText().toString());
                if (amt <= Integer.parseInt(myamount)) {

                    if (pay_flag){
                        sendRequest();
                        pay_flag = false;
                    }

                } else {

                    Toast.makeText(baseActivity, "Enter valid amount", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(baseActivity, "You are not supposed to pay any amount", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendRequest() {

        requestParams.put(Consts.ARTIST_ID, partner_id);
        requestParams.put(Consts.RIDER_ID, userDTO.getUser_id());
        requestParams.put(Consts.AMOUNT, binding.paymentAmountEtx.getText().toString());

        new HttpsRequest(Consts.REQUEST_AMOUNT_TO_PARTNER, requestParams, getActivity()).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    binding.paymentSubmit.setVisibility(View.GONE);
                    binding.tvNoedit.setVisibility(View.VISIBLE);
                    binding.paymentAmountEtx.setEnabled(false);
                    binding.paymentAmountEtx.setClickable(false);
                    binding.paymentAmountEtx.setFocusable(false);
                    Log.e("REQUEST_AMOUNT", "" + response.toString());
                }
            }
        });
    }
}