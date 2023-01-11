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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.adapter.BirthdayCardSliderAdapter;
import com.kamaii.rider.ui.models.BusinessCardSliderModel;
import com.kamaii.rider.utils.ImageCompression;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class BirthdayCardActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = com.kamaii.rider.ui.activity.BirthdayCardActivity.class.getSimpleName();
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
    TextInputEditText customEditText;
    RelativeLayout birthday_cardmain;
    LinearLayout applay_btn;
    String pathOfImage = "";
    ImageCompression imageCompression;
    File file;
    CircleImageView customer_image;
    String dirpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_card);

        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        progressDialog = new ProgressDialog(this);
        promotionpager = findViewById(R.id.birthdaypager);
        applay_btn = findViewById(R.id.applay_btn);
        customEditText = findViewById(R.id.custoer_name_etx);
        birthday_cardmain = findViewById(R.id.birthday_cardmain);
        customer_image = findViewById(R.id.birthday_preview_image);
        dotsIndicator = findViewById(R.id.dots22);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        findViewById(R.id.applay_btn).setOnClickListener(this);
        findViewById(R.id.birthday_preview_image).setOnClickListener(this);
        findViewById(R.id.birthdaycardllBack).setOnClickListener(this);
        findViewById(R.id.share_birthdaycard).setOnClickListener(this);

        getCategory();

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
    }

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
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Business-Card");
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
        new HttpsRequest(Consts.GET_BIRTHDAY_CARD_IMAGES, parms, com.kamaii.rider.ui.activity.BirthdayCardActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                progressDialog.dismiss();
                if (flag) {

                    try {
                        Log.e("SliderRES", "" + response.toString());
                        tvideoModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<BusinessCardSliderModel>>() {
                        }.getType();

                        card_flag = true;
                        tvideoModelArrayList = (ArrayList<BusinessCardSliderModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        loadAdapter("","");
                        /*ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
                            @Override
                            public void transformPage(@NonNull View page, float position) {
                                float r = 1 - Math.abs(position);
                                page.setScaleY(0.95f + r + 0.05f);
                            }
                        };*/
                        //   promotionpager.setPageTransformer(true, pageTransformer);
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

    public void loadAdapter(String s, String pathOfImage) {

        promotionpager.setAdapter(new BirthdayCardSliderAdapter(com.kamaii.rider.ui.activity.BirthdayCardActivity.this, tvideoModelArrayList, userDTO, categoryDTOS, s,pathOfImage));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.birthday_preview_image:
                ImagePicker.Companion.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(768)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;

            case R.id.birthdaycardllBack:
                onBackPressed();
                break;

            case R.id.share_birthdaycard:
                File file = saveBitMap(com.kamaii.rider.ui.activity.BirthdayCardActivity.this, birthday_cardmain);
                break;

            case R.id.applay_btn:
                String str = customEditText.getText().toString();
                loadAdapter(str,pathOfImage);
                findViewById(R.id.share_birthdaycard).setEnabled(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 23) {
                Uri picUri = data.getData();

                pathOfImage = picUri.getPath();
                imageCompression = new ImageCompression(com.kamaii.rider.ui.activity.BirthdayCardActivity.this);
                imageCompression.execute(pathOfImage);
                imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                    @Override
                    public void processFinish(String imagePath) {
                        try {
                            file = new File(imagePath);


                            Glide.with(com.kamaii.rider.ui.activity.BirthdayCardActivity.this).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(customer_image);

                            if (NetworkManager.isConnectToInternet(com.kamaii.rider.ui.activity.BirthdayCardActivity.this)) {
                                //updateProfileSelf();
                            } else {
                                ProjectUtils.showToast(com.kamaii.rider.ui.activity.BirthdayCardActivity.this, getResources().getString(R.string.internet_concation));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            try {
                com.kamaii.rider.ui.activity.BirthdayCardActivity.this.runOnUiThread(new Runnable() {
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