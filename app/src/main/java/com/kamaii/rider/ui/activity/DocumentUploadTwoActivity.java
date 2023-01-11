package com.kamaii.rider.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.DocumentDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.OnSpinerItemClick;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;
import com.kamaii.rider.utils.SpinnerDialog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DocumentUploadTwoActivity extends AppCompatActivity {

    private String TAG = DocumentUploadTwoActivity.class.getSimpleName();
    CustomTextViewBold select_category;
    CustomTextViewBold document_up_two_txt;
    private HashMap<String, String> parms = new HashMap<>();
    CustomTextViewBold personal_doc;
    CustomTextViewBold buiness_doc;
    private UserDTO userDTO;
    CustomTextViewBold training;
    private ArtistDetailsDTO artistDetailsDTO;
    String category_id = "-1";
    boolean flaging = true;
    ProgressDialog progressDialog;
    CustomTextViewBold bank_info, artist_name;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private SpinnerDialog spinnerDialogCate;
    private SharedPrefrence prefrence;
    private HashMap<String, String> paramsUpdate = new HashMap<>();
    private HashMap<String, String> parmsApprove = new HashMap<>();
    LinearLayout laychat, help_chat_linear;
    private CustomTextViewBold dno, dyes;
    private HashMap<String, String> chat = new HashMap<>();

    private HashMap<String, String> paramsUpdateTracker = new HashMap<>();
    RelativeLayout helppage;
    Button btnSubmit;
    CustomTextViewBold customTextViewBold;
    ScrollView scrollView;
    private ArrayList<DocumentDTO> documentDTOArrayList;
    CircleImageView add_profile_pic;
    private Dialog dialogfinish;
    LinearLayout cardcall, cardchat, cardemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload_two);

        findViewById(R.id.logout_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmLogout();

            }
        });
    }

    public void confirmLogout() {
        try {
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.logout_msg))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            com.kamaii.rider.Glob.BUBBLE_VALUE = "1";


                            dialog.dismiss();
                            prefrence.clearAllPreferences();
                           // logoutflag = true;

                            Intent intent = new Intent(DocumentUploadTwoActivity.this, CheckSignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        laychat = findViewById(R.id.laychat123);
        help_chat_linear = findViewById(R.id.help_chat_linear);
        document_up_two_txt = findViewById(R.id.document_up_two_txt);
        personal_doc = findViewById(R.id.personal_doc);
        artist_name = findViewById(R.id.artist_name);
        buiness_doc = findViewById(R.id.buiness_doc);
        training = findViewById(R.id.training);
        helppage = findViewById(R.id.helppage);
        bank_info = findViewById(R.id.bank_info);
        btnSubmit = findViewById(R.id.btnSubmitn);
        scrollView = findViewById(R.id.form_field);
        add_profile_pic = findViewById(R.id.add_profile_pic);
        customTextViewBold = findViewById(R.id.laymsg);
        select_category = findViewById(R.id.select_category);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmsApprove.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        artist_name.setText("Hello, " + userDTO.getName());
        cardcall = findViewById(R.id.cardcall);
        cardchat = findViewById(R.id.cardchat);
        cardemail = findViewById(R.id.cardemail);
        chat.put(Consts.USER_ID, userDTO.getUser_id());
        chat.put("status", "1");
      /*  dialogfinish = new Dialog(DocumentUploadTwoActivity.this, R.style.Theme_Dialog);
        dialogfinish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogfinish.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogfinish.setContentView(R.layout.dailog_approve);
        dialogfinish.setCancelable(false);
        laychat = dialogfinish.findViewById(R.id.laychat);
        layudoc = dialogfinish.findViewById(R.id.layudoc);
        dno = dialogfinish.findViewById(R.id.dno);
        dyes = dialogfinish.findViewById(R.id.dyes);
        */
        document_up_two_txt.setAlpha(0.4f);
        document_up_two_txt.setClickable(false);
        personal_doc.setAlpha(0.4f);
        personal_doc.setClickable(false);
        buiness_doc.setAlpha(0.4f);
        buiness_doc.setClickable(false);
        training.setAlpha(0.4f);
        training.setClickable(false);
        bank_info.setAlpha(0.4f);
        bank_info.setClickable(false);
        btnSubmit.setAlpha(0.4f);
        btnSubmit.setEnabled(false);

        cardemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        cardchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    whatsappChatmsg();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cardcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9909998404"));
                startActivity(intent);

            }
        });

        helppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocumentUploadTwoActivity.this, Help.class));
            }
        });

        document_up_two_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocumentUploadTwoActivity.this, BasicInfoActivity.class));
            }
        });
        personal_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocumentUploadTwoActivity.this, PersonalDocumentation.class));
            }
        });
        buiness_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocumentUploadTwoActivity.this, BusinessDocumentActivity.class));
            }
        });
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocumentUploadTwoActivity.this, TrainingActivity.class));
            }
        });
        bank_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocumentUploadTwoActivity.this, BankDocument.class));
            }
        });


        select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                    //getCategory();


                } else {
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_status();
            }
        });

        laychat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.link/c59fzm"));
                startActivity(browserIntent);
            }
        });
    }
    protected void sendEmail() {

        String[] TO = {"support@kamaii.in"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));


        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DocumentUploadTwoActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public void whatsappChatmsg() {
        progressDialog.show();
        Log.e(TAG, "whatsappChatmsg: "+chat.toString() );
        new HttpsRequest(Consts.GET_WHATS_APP_CHAT_API, chat, this).stringPost("Kamaii", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Log.e("CAT_RES", "" + response.toString());

                        JSONObject jsonObject = response;
                        String status = jsonObject.getString("status");
                        String success_msg = jsonObject.getString("message");
                        String data_msg = jsonObject.getString("data");

                        String phoneNumberWithCountryCode = "+919714443264";

                        Log.e("JOINING_DATE", "" + userDTO.getCreated_at());
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                                phoneNumberWithCountryCode,data_msg ))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();


                } else {
                    progressDialog.dismiss();

                }
            }
        });
    }

    public void getCategory() {
        progressDialog.show();
        Log.e(TAG, "getCategory: "+parmsCategory.toString() );
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API2, parmsCategory, this).stringPost("Kamaii", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e(TAG, "backResponse: "+response.toString() );
                if (flag) {
                    try {
                        Log.e("CAT_RES", "" + response.toString());
                        String seventh = response.getJSONArray("data").getJSONObject(0).getString("final_approval").toString();

                        if (seventh.equals("1")) {
                            scrollView.setVisibility(View.GONE);
                            customTextViewBold.setVisibility(View.GONE);
                            help_chat_linear.setVisibility(View.GONE);

                        } else if (seventh.equals("2")) {
                            scrollView.setVisibility(View.GONE);
                            customTextViewBold.setVisibility(View.VISIBLE);
                            help_chat_linear.setVisibility(View.VISIBLE);


                        } else {
                            scrollView.setVisibility(View.VISIBLE);
                            customTextViewBold.setVisibility(View.GONE);
                            help_chat_linear.setVisibility(View.GONE);


                            categoryDTOS = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                            }.getType();
                            categoryDTOS = new Gson().fromJson(response.getJSONArray("data").getJSONObject(0).getJSONArray("getallcat").toString(), getpetDTO);

                            String first = response.getJSONArray("data").getJSONObject(0).getString("step_1").toString();
                            String second = response.getJSONArray("data").getJSONObject(0).getString("step_2").toString();
                            String third = response.getJSONArray("data").getJSONObject(0).getString("step_3").toString();
                            String forth = response.getJSONArray("data").getJSONObject(0).getString("step_4").toString();
                            String fifth = response.getJSONArray("data").getJSONObject(0).getString("step_5").toString();
                            String sixtth = response.getJSONArray("data").getJSONObject(0).getString("step_6").toString();
                            String Img_path = response.getJSONArray("data").getJSONObject(0).getString("profile_pic").toString();
                            String seventhbtn = response.getJSONArray("data").getJSONObject(0).getString("step_7").toString();
                            String is_map = response.getJSONArray("data").getJSONObject(0).getString("is_map").toString();

                            Log.e("is_map", "" + is_map);
                            Collections.sort(categoryDTOS, new Comparator<CategoryDTO>() {
                                @Override
                                public int compare(CategoryDTO lhs, CategoryDTO rhs) {
                                    return lhs.getCat_name().compareTo(rhs.getCat_name());
                                }
                            });

                            if (is_map.equalsIgnoreCase("0")) {

                                buiness_doc.setVisibility(View.GONE);

                                if (!fifth.equalsIgnoreCase("1")) {

                                    training.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_four, 0, R.drawable.ic_arrow, 0);
                                }

                                if (!sixtth.equalsIgnoreCase("1")) {

                                    bank_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fifth, 0, R.drawable.ic_arrow, 0);
                                }
                            }
                            if (is_map.equalsIgnoreCase("1")) {

                                buiness_doc.setVisibility(View.VISIBLE);

                                if (!fifth.equalsIgnoreCase("1")) {

                                    training.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fifth, 0, R.drawable.ic_arrow, 0);
                                }

                                if (!sixtth.equalsIgnoreCase("1")) {

                                    bank_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic__six, 0, R.drawable.ic_arrow, 0);
                                }
                            }
                            if (first.equals("1")) {
//                                select_category.setClickable(false);
                                select_category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_right, 0, R.drawable.ic_arrow, 0);

                                select_category.setText(response.getJSONArray("data").getJSONObject(0).getString("cat_name"));

//                                document_up_two_txt.setAlpha(1f);
                                document_up_two_txt.setClickable(true);
                                btnSubmit.setAlpha(0.4f);
                                btnSubmit.setEnabled(false);

                            }
                            if (second.equals("1")) {
                                document_up_two_txt.setAlpha(1f);
                                document_up_two_txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_right, 0, R.drawable.ic_arrow, 0);
                                document_up_two_txt.setClickable(false);
//                                personal_doc.setAlpha(1f);
                                personal_doc.setClickable(true);

                                if (!Img_path.equals("0")) {
                                    Glide.with(DocumentUploadTwoActivity.this).
                                            load(Img_path)
                                            .placeholder(R.drawable.ic_user_placeholder)
                                            .dontAnimate()
                                            .error(R.drawable.ic_user_placeholder)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(add_profile_pic);
                                }
                                btnSubmit.setAlpha(0.4f);
                                btnSubmit.setEnabled(false);
                            }
                            if (third.equals("1")) {
                                personal_doc.setAlpha(1f);
                                personal_doc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_right, 0, R.drawable.ic_arrow, 0);
                                personal_doc.setClickable(false);
//                                buiness_doc.setAlpha(1f);
                                buiness_doc.setClickable(true);

                                btnSubmit.setAlpha(0.4f);
                                btnSubmit.setEnabled(false);
                            }

                            if (forth.equals("1")) {
                                buiness_doc.setAlpha(1f);
                                buiness_doc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_right, 0, R.drawable.ic_arrow, 0);
                                buiness_doc.setClickable(false);
                                /*training.setAlpha(1f);*/
                                training.setClickable(true);

                                btnSubmit.setAlpha(0.4f);
                                btnSubmit.setEnabled(false);
                            }

                            if (fifth.equals("1")) {
                                training.setAlpha(1f);
                                training.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_right, 0, R.drawable.ic_arrow, 0);
                                training.setClickable(false);
//                                bank_info.setAlpha(1f);
                                bank_info.setClickable(true);

                                btnSubmit.setAlpha(0.4f);
                                btnSubmit.setEnabled(false);
                            }

                            if (sixtth.equals("1")) {
                                bank_info.setAlpha(1f);
                                bank_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_right, 0, R.drawable.ic_arrow, 0);
                                bank_info.setClickable(false);

                                btnSubmit.setAlpha(0.4f);
                                btnSubmit.setEnabled(false);
                            }

                            if (seventhbtn.equals("1")) {
                                btnSubmit.setBackgroundResource(R.drawable.basicinfo_layout_submit_btn);
                                btnSubmit.setTextColor(getResources().getColor(R.color.white));
                                btnSubmit.setAlpha(1f);
                                btnSubmit.setEnabled(true);
                            }

                            spinnerDialogCate = new SpinnerDialog(DocumentUploadTwoActivity.this, categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                            spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                                @Override
                                public void onClick(String item, String id, int position) {
                                    flaging = false;
                                    select_category.setText(item);
                                    select_category.setClickable(true);
                                    select_category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_right, 0, R.drawable.ic_arrow, 0);
                                    category_id = id;
                                    submitPersonalProfile(category_id);
                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();


                } else {
                    progressDialog.dismiss();


                }
            }
        });
    }

    public void submitPersonalProfile(String category_idNew) {
        if (category_idNew.equals("-1")) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(DocumentUploadTwoActivity.this)) {

                paramsUpdate.put("artist_id", userDTO.getUser_id());
                paramsUpdate.put("cat_id", category_idNew);
                updateProfile();
            } else {
                ProjectUtils.showToast(DocumentUploadTwoActivity.this, getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            ProjectUtils.showLong(DocumentUploadTwoActivity.this, msg);
            return false;
        } else {
            return true;
        }
    }

    public void updateProfile() {
        ProjectUtils.showProgressDialog(DocumentUploadTwoActivity.this, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_CATEGORY_ID, paramsUpdate, DocumentUploadTwoActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {
                        ProjectUtils.showToast(DocumentUploadTwoActivity.this, msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        overridePendingTransition(R.anim.stay, R.anim.slide_down);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(DocumentUploadTwoActivity.this, msg);
                }


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        init();
        getCategory();
    }

    public void update_status() {

        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_APPROVAL, parms, DocumentUploadTwoActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    try {
                        scrollView.setVisibility(View.INVISIBLE);
                        customTextViewBold.setVisibility(View.VISIBLE);
                        laychat.setVisibility(View.GONE);
                        help_chat_linear.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(DocumentUploadTwoActivity.this, msg);
                }
                progressDialog.dismiss();

            }
        });


    }

    public void getApproveStatus() {
        new HttpsRequest(Consts.GET_APPROVAL_STATUS_API, parmsApprove, DocumentUploadTwoActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        int approval_status = response.getInt("approval_status");
                        userDTO.setApproval_status(approval_status);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        if (approval_status == 0) {
                        }
                        if (approval_status == 2) {
                            Intent browserIntent = new Intent(DocumentUploadTwoActivity.this, DocumentUploadTwoActivity.class);
                            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(browserIntent);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }

}