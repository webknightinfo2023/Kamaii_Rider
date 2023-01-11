package com.kamaii.rider.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.adapter.BusinessCardSliderAdapter;
import com.kamaii.rider.ui.models.BusinessCardSliderModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class BusinessCardActivity extends AppCompatActivity {

    private String TAG = com.kamaii.rider.ui.activity.BusinessCardActivity.class.getSimpleName();
    ViewPager promotionpager;
    DotsIndicator dotsIndicator;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private ArrayList<BusinessCardSliderModel> tvideoModelArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();
    public static boolean card_flag = false;
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    RelativeLayout business_cardmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        progressDialog = new ProgressDialog(this);
        promotionpager = findViewById(R.id.promotionpager);
        business_cardmain = findViewById(R.id.business_cardmain);
        dotsIndicator = findViewById(R.id.dots2);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        findViewById(R.id.businesscardllBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getCategory();

        findViewById(R.id.share_businesscard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = saveBitMap(com.kamaii.rider.ui.activity.BusinessCardActivity.this, business_cardmain);    //which view you want to pass that view as parameter
                if (file != null) {
                    Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
                } else {
                    Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
                }
            }
        });
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }//create bitmap from view and returns it

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM,uri);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Business-Card");
                    // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCategory() {


        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {
                    try {
                        Log.e("getAllCaegory123", "" + response.toString());
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        gettvideo();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    public void gettvideo() {
        Log.e("Vider", "1");
        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_BUSINESS_CARD_IMAGES, parms, com.kamaii.rider.ui.activity.BusinessCardActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                progressDialog.dismiss();
                if (flag) {

                    try {
                        Log.e("SliderRES", "" + response.toString());
                        tvideoModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<BusinessCardSliderModel>>() {
                        }.getType();

                        tvideoModelArrayList = (ArrayList<BusinessCardSliderModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        promotionpager.setAdapter(new BusinessCardSliderAdapter(com.kamaii.rider.ui.activity.BusinessCardActivity.this, tvideoModelArrayList, userDTO, categoryDTOS));

                        dotsIndicator.attachViewPager(promotionpager);

                        /*Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("Vider", "4");

                }
            }
        });


    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            try {
                com.kamaii.rider.ui.activity.BusinessCardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (promotionpager.getCurrentItem() < tvideoModelArrayList.size() - 1) {
                            promotionpager.setCurrentItem(promotionpager.getCurrentItem() + 1);
                        } else {
                            promotionpager.setCurrentItem(0);
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }
}