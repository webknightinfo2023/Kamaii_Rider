package com.kamaii.rider.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.isseiaoki.simplecropview.CropImageView;
import com.kamaii.rider.R;


public class CropActivity extends AppCompatActivity implements View.OnClickListener {

    CropImageView mCropView;
    private ImageButton buttonRotateLeft,buttonRotateRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        mCropView =  findViewById(R.id.cropImageView);

        findViewById(R.id.buttonDone).setOnClickListener((View.OnClickListener) this);
        findViewById(R.id.buttonCancel).setOnClickListener((View.OnClickListener) this);


        buttonRotateLeft =  findViewById(R.id.buttonRotateLeft);
        buttonRotateRight = findViewById(R.id.buttonRotateRight);

        buttonRotateLeft.setOnClickListener(this);
        buttonRotateRight.setOnClickListener(this);

        if (mCropView.getImageBitmap() == null) {
            mCropView.setImageBitmap(com.kamaii.rider.Glob.cameragalleryselectedimage1);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonDone:
                com.kamaii.rider.Glob.cameragalleryselectedimage1 = mCropView.getCroppedBitmap();


                finish();



                break;
            case R.id.buttonCancel:

                onBackPressed();

                break;


            case R.id.buttonRotateLeft:
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                break;
            case R.id.buttonRotateRight:
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                break;


        }

    }



    public void onBackPressed() {

        com.kamaii.rider.Glob.cameragalleryselectedimage1=null;
       finish();

    }


}
