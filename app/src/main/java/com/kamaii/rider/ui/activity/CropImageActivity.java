package com.kamaii.rider.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.system.ErrnoException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.rider.R;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CropImageActivity extends AppCompatActivity {
    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    String filepath,ActivityName;
    Intent SendIntent;
    static File MyFilePath;
    CustomTextViewBold crop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        crop=findViewById(R.id.crop);
        mCropImageView = (CropImageView)  findViewById(R.id.CropImageView);
        Intent intent = getIntent();
        filepath= intent.getStringExtra("imagePath");
        ActivityName= intent.getStringExtra("activity");
        Uri fileUri = Uri.parse(filepath);
        mCropImageView.setImageUri(fileUri);

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CroppedImage();
            }
        });

    }
    public void onCropImageClick(View view) {
        Bitmap cropped =  mCropImageView.getCroppedImage(500, 500);
        if (cropped != null)
            mCropImageView.setImageBitmap(cropped);
    }

    @Override
    protected void onActivityResult(int  requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri =  getPickImageResultUri(data);

            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                mCropImageView.setImageUriAsync(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    public Intent getPickImageChooserIntent() {

        Uri outputFileUri =  getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager =  getPackageManager();

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam =  packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

   private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null  && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ?  getCaptureImageOutputUri() : data.getData();
    }

    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.crop_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.crop:
                CroppedImage();
               // onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public  void CroppedImage()
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        //MyFilePath = new File(directory, "file" + String.valueOf(System.currentTimeMillis()) + ".png");
         File file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + ".png");
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
            Bitmap bm=mCropImageView.getCroppedImage(500, 500);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception e)
        {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }

        Intent returnIntent =new Intent();
        returnIntent.putExtra("path",file.toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
