package com.kamaii.rider.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.kamaii.rider.DatabaseHandler;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AdapterCabBookings;
import com.kamaii.rider.ui.adapter.OffLineImageAdapter;
import com.kamaii.rider.ui.models.OfflineImageDataModal;
import com.kamaii.rider.utils.FileUtility;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;


public class StoredImagesFragment extends Fragment {

    public RecyclerView recyclerView;
    TextView nodatafound;
    OffLineImageAdapter offLineImageAdapter;
    DatabaseHandler databaseHandler;
    private BaseActivity baseActivity;
    public static Uri uri = null;
    private SharedPrefrence prefrence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_stored_images, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());

        baseActivity.headerNameTV.setText(getResources().getString(R.string.imagesoffline));

        recyclerView = view.findViewById(R.id.storedimages_rv);
        nodatafound = view.findViewById(R.id.nodatafound);
        databaseHandler = new DatabaseHandler(getActivity());
        List<OfflineImageDataModal> imageData = databaseHandler.getAllContacts();
        if (imageData.size() == 0){
            nodatafound.setVisibility(View.VISIBLE);
        }
        Log.e("TAG", "onCreateView: 1 "+imageData.get(0).getService_name() );
        Log.e("TAG", "onCreateView: 2 "+imageData.get(1).getService_name() );
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        offLineImageAdapter = new OffLineImageAdapter(StoredImagesFragment.this,getActivity(),imageData);
        recyclerView.setAdapter(offLineImageAdapter);
        return view;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.e("onRequestPer", "onRequestPermissionsResult: result called");
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // adapterAllBookings.printBluetooth();
            }
        } else if (requestCode == 23) {

            Toast.makeText(baseActivity, "Allow Camera and access files and media permission", Toast.LENGTH_SHORT).show();
            if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

    }

    public void opencamrea() {

        Log.e("onRequestPe", "onRequestPermissionsResult: result called 10");

        ImagePicker.Companion.with(getActivity())
//                .crop()
                .crop()
                .cameraOnly()
                //.compress(768)
                .maxResultSize(620, 620)
                .start(23);

    }

    public void reloadFragment() {
        baseActivity.loadHomeFragment(new StoredImagesFragment(), "cab");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            if (requestCode == 23) {
                Log.e("Image_response", " 1111111 ");
                Bitmap profilePicture = (Bitmap) data.getExtras().get("data");

               // uri = getImageUri(getActivity(), profilePicture);
                OffLineImageAdapter.img_upload.setVisibility(View.VISIBLE);
                Uri imageUri = data.getData();
                    OffLineImageAdapter.img_upload.setImageURI(imageUri);

                    final InputStream imageStream;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = encodeImage(selectedImage);
                        prefrence.setValue("pic_img", encodedImage);

                        OffLineImageAdapter.img_url = encodedImage;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String path = FileUtility.getPath(getActivity(), imageUri);
                    File file = new File(path);

            }

        } else {

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}