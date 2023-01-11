package com.kamaii.rider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.activity.CheckSignInActivity;
import com.kamaii.rider.ui.activity.DocumentUploadTwoActivity;
import com.kamaii.rider.ui.activity.SelectLanguageActivity;
import com.kamaii.rider.ui.fragment.StoredImagesFragment;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;


public class SplashActivity extends AppCompatActivity {

    private SharedPrefrence prefference;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK};
    private boolean cameraAccepted, storageAccepted, accessNetState, fineLoc, corasLoc, readstorage, wakelock;
    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 3000;
    Context mContext;
    MediaPlayer mediaPlayer;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();
    private String TAG = SplashActivity.class.getSimpleName();
    String version = "";
    CustomTextViewBold version_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(com.kamaii.rider.SplashActivity.this);
        setContentView(R.layout.activity_splash);
        mContext = com.kamaii.rider.SplashActivity.this;
        version_name = findViewById(R.id.version_name);
        prefference = SharedPrefrence.getInstance(com.kamaii.rider.SplashActivity.this);
        //check();

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            version_name.setText("Version "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    scheduleJob(SplashActivity.this,BACKGROUND_SERVICE_JOB_ID);
                } else {
                    startService(new Intent(SplashActivity.this, AppService.class));
                }
*/
                    if (NetworkManager.isConnectToInternet(SplashActivity.this)){

                        check_state();
                    }else {
                        Intent intent = new Intent(SplashActivity.this,BaseActivity.class);
                        intent.putExtra("offline",true);
                        startActivity(intent);
                    }

                /*Intent in = new Intent(com.kamaii.rider.SplashActivity.this, BaseActivity.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);*/
                } else {
                    startActivity(new Intent(com.kamaii.rider.SplashActivity.this, SelectLanguageActivity.class));
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                }
            }
        }, 3000);
    }


/*
    public boolean checkPermission() {

        //Check, API Version is Above 23 than open Permission dialog
        if (Build.VERSION.SDK_INT >= 23)
        {
            boolean  isFinelocation = false,iscrose=false;
            //camera Permission



            //fine location Permission
            try {
                int hasFinelocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasFinelocationPermission != PackageManager.PERMISSION_GRANTED) {
                    isFinelocation = false;
                } else {
                    isFinelocation = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isFinelocation = true;
            }


            try {
                int hasReadStoragePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                if (hasReadStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    iscrose = false;
                } else {
                    iscrose = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                iscrose = true;
            }

            if (!isFinelocation || !iscrose) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 007);
            }
            //Must be all permission true otherwise again check permission call
            if (isFinelocation && iscrose) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
*/

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void check_state() {

        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put("artist_id", userDTO.getUser_id());

        new HttpsRequest(Consts.CHECK_APPROVE, parms, SplashActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    Log.e("splash", "" + response.toString());

                    Intent in = new Intent(com.kamaii.rider.SplashActivity.this, BaseActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                } else {
                    Intent in = new Intent(com.kamaii.rider.SplashActivity.this, DocumentUploadTwoActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                }
            }
        });


    }

}


