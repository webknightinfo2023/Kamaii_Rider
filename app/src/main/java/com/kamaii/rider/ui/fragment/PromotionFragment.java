package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.activity.BirthdayCardActivity;
import com.kamaii.rider.ui.activity.BusinessCardActivity;
import com.kamaii.rider.ui.activity.ContactListActivity;
import com.kamaii.rider.ui.activity.MySqliteHelper;
import com.kamaii.rider.ui.models.ContactModel;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import ezvcard.property.Telephone;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


public class PromotionFragment extends Fragment {

    private String TAG = PromotionFragment.class.getSimpleName();
    private BaseActivity baseActivity;
    MySqliteHelper mySqliteHelper = new MySqliteHelper();
    SQLiteDatabase db;
  //  private HashMap<String, String> paramsGetMsg = new HashMap<>();
    ArrayList<ContactModel> contactModelArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> params = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.promotion));
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        //paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());

        view.findViewById(R.id.share_profile_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bookArtist2();
            }
        });

        view.findViewById(R.id.upload_contacts_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(getActivity(), "", "Please Wait...", true);

//                db = getActivity().openOrCreateDatabase("DATABASE", android.content.Context.MODE_PRIVATE, null);

                db = getActivity().openOrCreateDatabase("kamaii.db", android.content.Context.MODE_PRIVATE, null);
                try {
                    final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS con_data ("
                            + "ID INTEGER primary key AUTOINCREMENT,"
                            + "Name TEXT,"
                            + "Phone TEXT,"
                            + "Fav INTEGER);";

                    db.execSQL(CREATE_TABLE_CONTAIN);
                } catch (Exception ignored) {

                }


                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, "Contacts.vcf");
                List<VCard> vcard;
                try {
                    vcard = Ezvcard.parse(file).all();
                    System.out.println("vafdfd " + vcard);
                    for (VCard vCard : vcard) {
                        List<Telephone> telePhoneNumbers = vCard.getTelephoneNumbers();
                        List<FormattedName> nameListing = vCard.getFormattedNames();

                        for (int i = 0; i < telePhoneNumbers.size(); i++) {
                            System.out.println(nameListing.get(0).getValue() + " -> " + telePhoneNumbers.get(i).getText());
                            String sql = "INSERT INTO con_data(Name, Phone, Fav) VALUES('" + nameListing.get(0).getValue().replaceAll("[^a-zA-Z0-9]", " ") + "','" + telePhoneNumbers.get(i).getText() + "',0)";
                            db.execSQL(sql);

                            if (i == (telePhoneNumbers.size() - 1)) {
                                //this is the last iteration of for loop
                                progressDialog.dismiss();
                                getActivity().startActivity(new Intent(getActivity(), ContactListActivity.class));
                            }
                        }
                    }

                    Cursor cc = db.rawQuery("SELECT * FROM con_data", null);

                    if (cc != null) {
                        cc.moveToFirst();
                        getActivity().startManagingCursor(cc);

                        for (int i = 0; i < cc.getCount(); i++) {
                            contactModelArrayList.add(new ContactModel(cc.getInt(0),cc.getString(1), cc.getString(2), cc.getInt(3), cc.getInt(4)));
                            cc.moveToNext();
                        }
                    }
                    //  Log.e("SHIVOBNM", "" + cc.getCount());
                    /*ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>(this, R.layout.list_item, R.id.name, name);
                    listView.setAdapter(itemsAdapter);*/

                } catch (IOException e1) {
                    progressDialog.dismiss();
                    getActivity().startActivity(new Intent(getActivity(), ContactListActivity.class));
                    e1.printStackTrace();
                    System.out.println("asd " + e1);
                }
            }
        });

        view.findViewById(R.id.birth_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(baseActivity, "Coming Soon...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), BirthdayCardActivity.class));

            }
        });
        view.findViewById(R.id.share_services_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(baseActivity, "Coming soon...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), BusinessCardActivity.class));
               /* Fragment fragment = new SelectServicesFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/            }
        });
        return view;

    }

    public void bookArtist2 () {


        //Log.e("UID_QTY", "5->" + quentity);
        //Log.e("HASHMAP", "5->" + paramsBookingOp2.toString());

        new HttpsRequest(Consts.GET_SHARED_PROFILE_MSG_API,params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.e("SHARE_PROFILE",""+response.toString());
                if (flag) {
                    //ProjectUtils.showToast(Booking.this, msg);
                    try {
                        //  userBookingList = new ArrayList<>();
                        // Type getpetDTO = new TypeToken<List<CartModel>>() {
                        // }.getType();
                        // JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject = response;
                        String status = jsonObject.getString("status");
                        String success_msg = jsonObject.getString("message");
                        String data_msg = jsonObject.getString("data");




                        Log.e("success_msg", "" + success_msg);
                        Log.d("data_msg", "" + data_msg);


                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = data_msg;
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}