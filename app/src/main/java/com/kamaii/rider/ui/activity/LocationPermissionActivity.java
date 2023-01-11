package com.kamaii.rider.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.kamaii.rider.R;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.preferences.SharedPrefrence;

public class LocationPermissionActivity extends AppCompatActivity {

    private SharedPrefrence prefference;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private static final int REQUEST_CAURSE_LOCATION_PERMISSION = 1004;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 1005;
    private static final int REQUEST_BACKGROUND_LOCATION_PERMISSION = 1006;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK};
    private boolean cameraAccepted, storageAccepted, accessNetState, fineLoc, corasLoc, readstorage, wakelock, background, internal_window;
    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 3000;
    Context mContext;
    MediaPlayer mediaPlayer;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);

        findViewById(R.id.btnlocationapprove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check()) {

                    startActivity(new Intent(LocationPermissionActivity.this, DocumentUploadTwoActivity.class));
                }
            }
        });

    }


    public boolean check() {
        if (checkPermission()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission() {
         builder = new AlertDialog.Builder(this);

        //Check, API Version is Above 23 than open Permission dialog
        if (Build.VERSION.SDK_INT >= 23) {
            boolean isFinelocation = false, iscrose = false, isbackground = false;
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

            try {
                int hasBackgroundlocationPermission = checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                if (hasBackgroundlocationPermission != PackageManager.PERMISSION_GRANTED) {

                    isbackground = false;
                } else {
                    isbackground = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isbackground = true;
            }

            if (!isFinelocation) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_FINE_LOCATION)) {


                    ActivityCompat.requestPermissions(
                            LocationPermissionActivity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                    //                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                    //                       Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            },
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                    );


                    // If we should give explanation of requested permissions

                    // Show an alert dialog here with request explanation
                    /*builder.setMessage("Background Location" +
                            " is required to do the task.");
                    builder.setTitle("Please grant those permissions");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    LocationPermissionActivity.this,
                                    new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION
                    //                        Manifest.permission.ACCESS_COARSE_LOCATION,
                     //                       Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                    },
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    *//*AlertDialog dialog = builder.create();
                    dialog.show();*/


                } else {
                    this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION_PERMISSION);
                    //this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

                }
            }
            else if(!iscrose){

                ActivityCompat.requestPermissions(
                        LocationPermissionActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION
                                //                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                //                       Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        },
                        REQUEST_CAURSE_LOCATION_PERMISSION
                );
               /* if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_FINE_LOCATION)
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    // If we should give explanation of requested permissions

                    // Show an alert dialog here with request explanation
                    builder.setMessage("Background Location" +
                            " is required to do the task.");
                    builder.setTitle("Please grant those permissions");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    LocationPermissionActivity.this,
                                    new String[]{
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                            //                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                            //                       Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                    },
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    *//*AlertDialog dialog = builder.create();
                    dialog.show();*//*


                } else {
                    this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    //this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);}
*/

            }else if(!isbackground){
                ActivityCompat.requestPermissions(
                        LocationPermissionActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                //                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                //                       Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        },
                        REQUEST_BACKGROUND_LOCATION_PERMISSION
                );
            }
            //Must be all permission true otherwise again check permission call
            if (isFinelocation && iscrose && isbackground) {

                startActivity(new Intent(LocationPermissionActivity.this, CheckSignInActivity.class));

                return true;
            } else {
                return false;
            }
        }


        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                try {

                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[2] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[3] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[4] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[5] == PackageManager.PERMISSION_GRANTED) {

                        cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, cameraAccepted);

                        storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, storageAccepted);

                        accessNetState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, accessNetState);

                        readstorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        prefference.setBooleanValue(Consts.READ_STORAGE, readstorage);


                        wakelock = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                        prefference.setBooleanValue(Consts.WACK_LOCK, wakelock);


                        internal_window = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                        prefference.setBooleanValue(Consts.INTERNAL_WINDOW, internal_window);
                    }else {
                       // checkPermission();
                //        Toast.makeText(this, "Permissions are required", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case REQUEST_CAURSE_LOCATION_PERMISSION:
                try{
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        corasLoc = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        prefference.setBooleanValue(Consts.CORAS_LOC, corasLoc);
                    }
                    else {
                        //checkPermission();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case REQUEST_BACKGROUND_LOCATION_PERMISSION:
                try{
                   if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                       background = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                       prefference.setBooleanValue(Consts.BACKGROUND, background);
                   }else {
                      // Toast.makeText(this, "Permissions are required", Toast.LENGTH_LONG).show();
                       Intent intent = new Intent();
                       intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                       Uri uri = Uri.fromParts("package", getPackageName(), null);
                       intent.setData(uri);
                       startActivity(intent);                   }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case REQUEST_FINE_LOCATION_PERMISSION:
                try {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        fineLoc = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        prefference.setBooleanValue(Consts.FINE_LOC, fineLoc);
                    }else {
                       // checkPermission();
                        //Toast.makeText(this, "Permissions are required", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        if (Build.VERSION.SDK_INT >= 29){

            check();
        }
        else {
            startActivity(new Intent(LocationPermissionActivity.this, CheckSignInActivity.class));
        }
    }

}