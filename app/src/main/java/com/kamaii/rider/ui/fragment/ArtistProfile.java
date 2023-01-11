package com.kamaii.rider.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistDetailsDTO;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentArtistProfileBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.ImagePickSetOnclickListner;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.activity.EditPersnoalInfo;
import com.kamaii.rider.utils.FileUtility;
import com.kamaii.rider.utils.ImageCompression;
import com.kamaii.rider.utils.MainFragment;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ArtistProfile extends Fragment implements View.OnClickListener {

    private String TAG = ArtistProfile.class.getSimpleName();
    private FragmentArtistProfileBinding binding;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private ArtistDetailsDTO artistDetailsDTO;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private PersnoalInfo persnoalInfo = new PersnoalInfo();
    private ImageGallery imageGallery = new ImageGallery();
    private PreviousWork previousWork = new PreviousWork();
    private Services services = new Services();
    private Reviews reviews = new Reviews();
    private DocumentUploadFragment documentUploadFragment = new DocumentUploadFragment();
    private Bundle bundle;
    private ViewPagerAdapter adapter;
    private int mMaxScrollSize;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private HashMap<String, String> paramsUpdate;
    private HashMap<String, File> paramsFile;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    File file;
    private HashMap<String, String> params;
    private BaseActivity baseActivity;
    private HashMap<String, String> paramsDeleteImg = new HashMap<>();
    ProgressDialog progressDialog;
    boolean bg_image_bannew_flag = false;
    public boolean product_image_flag = false;
    ImageCompression imageCompression2;

    String pathOfImage2 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_profile, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_profile));
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        setUiAction();
        return binding.getRoot();
    }

    public void setUiAction() {
        binding.ivEditPersonal.setOnClickListener(this);

        binding.ivArtist.setOnClickListener(this);
        binding.galleryImg.setOnClickListener(this);

        builder = new BottomSheet.Builder(getActivity()).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        //Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);
                                        picUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", file);
                                    } else {
                                        picUri = Uri.fromFile(file); // create
                                    }

                                    prefrence.setValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_pic)), PICK_FROM_GALLERY);

                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getCategory();
            getArtist();

        } else {
      //      ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        //  ProjectUtils.showToast(getActivity(),"work");

    }

    public void getCategory() {
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                }
            }
        });
    }

    public void getArtist() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {
                        progressDialog.dismiss();
                        // ProjectUtils.showToast(getActivity(), msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void showData() {
        bundle = new Bundle();
        bundle.putSerializable(Consts.ARTIST_DTO, artistDetailsDTO);

        binding.tvName.setText(artistDetailsDTO.getName());
        Glide.with(getActivity()).
                load(artistDetailsDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivArtist);

        binding.tvProfileComplete.setText(artistDetailsDTO.getCompletePercentages() + "% " + getResources().getString(R.string.completion));

        if (artistDetailsDTO.getTotalJob().equalsIgnoreCase("1") || artistDetailsDTO.getTotalJob().equalsIgnoreCase("0")) {
            binding.tvJobComplete.setText(artistDetailsDTO.getTotalJob() + " " + getResources().getString(R.string.job_comleted));
        } else {
            binding.tvJobComplete.setText(artistDetailsDTO.getTotalJob() + " " + getResources().getString(R.string.jobs_comleted));
        }



        if (!artistDetailsDTO.getSociety_name().equalsIgnoreCase("")){

            binding.landmarkName.setText(artistDetailsDTO.getSociety_name());
        }
        else {
            binding.landmarkName.setVisibility(View.GONE);
        }

        if (!artistDetailsDTO.getHouse_no().equalsIgnoreCase("")){

            binding.blockNo.setText(artistDetailsDTO.getHouse_no());
        }
        else {
            binding.blockNo.setVisibility(View.GONE);
        }
        binding.tvLocation.setText(artistDetailsDTO.getLocation());


        binding.ratingbar.setRating(Float.parseFloat(artistDetailsDTO.getAva_rating()));
        binding.tvRating.setText("(" + artistDetailsDTO.getAva_rating() + "/5)");
        persnoalInfo.setArguments(bundle);
        imageGallery.setArguments(bundle);
        previousWork.setArguments(bundle);
        reviews.setArguments(bundle);
        services.setArguments(bundle);

        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(persnoalInfo, "Info");

        adapter.addFragment(previousWork, "Works");

        binding.pager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.pager);

        binding.pager.setCurrentItem(prefrence.getIntValue("tab"));
        prefrence.setIntValue("tab", 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEditPersonal:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (categoryDTOS.size() > 0) {
                        Intent intent = new Intent(getActivity(), EditPersnoalInfo.class);
                        intent.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
                        intent.putExtra(Consts.CATEGORY_list, categoryDTOS);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                    } else {
               //         ProjectUtils.showLong(getActivity(), getResources().getString(R.string.try_after));
                    }
                } else {
            //        ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }


                break;

            case R.id.ivArtist:
                if (artistDetailsDTO != null) {
                    //builder.show();
                   /* SelectBottomDialogFragment addPhotoBottomDialogFragment = new SelectBottomDialogFragment(getActivity(), "Image", imageSetOnclickListner);
                    addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(), "add_photo_dialog_fragment");*/

                    ImagePicker.Companion.with(getActivity())
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(768)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.incomplete_profile_msg));
                }
                break;
            case R.id.gallery_img:
                //  Toast.makeText(baseActivity, "qwer", Toast.LENGTH_SHORT).show();
                bg_image_bannew_flag = true;
                ImagePicker.Companion.with(getActivity())
                        .crop()//Crop image(Optional), Check Customization for more option
                        .compress(768)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(768, 768)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
        }
    }


    public void tempimage(String toString, String s) {
        // Toast.makeText(getActivity(),"ccc", Toast.LENGTH_LONG).show();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void isOnline() {
        new HttpsRequest(Consts.ONLINE_OFFLINE_API, paramsUpdate, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    // ProjectUtils.showToast(getActivity(), "Online");
                    getArtist();

                } else {
                    // ProjectUtils.showToast(getActivity(), "Offline");
                }


            }
        });
    }

    private File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root, Consts.APP_NAME);
        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.APP_NAME + timeStamp + ".png");

            imageName = Consts.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }

    private ImagePickSetOnclickListner imageSetOnclickListner = new ImagePickSetOnclickListner() {
        @Override
        public void Camera(Uri uri) {
            if (uri != null) {
                final String path = FileUtility.getPath(getActivity(), uri);
                if (path != null) {
                    if (uri != null) {
                        // uri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));


                        // startCropping(uri, CROP_CAMERA_IMAGE);

                        pathOfImage = picUri.getPath();
                        imageCompression = new ImageCompression(getActivity());
                        imageCompression.execute(pathOfImage);
                        imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                            @Override
                            public void processFinish(String imagePath) {
                                try {
                                    file = new File(imagePath);


                                    Glide.with(getActivity()).load("file://" + imagePath)
                                            .thumbnail(0.5f)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(binding.ivArtist);
                                    paramsFile = new HashMap<>();
                                    paramsFile.put(Consts.IMAGE, file);
                                    Log.e("image", imagePath);
                                    params = new HashMap<>();
                                    params.put(Consts.USER_ID, userDTO.getUser_id());
                                    if (NetworkManager.isConnectToInternet(getActivity())) {
                                        updateProfileSelf();
                                    } else {
                                //        ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                    }


                                    Log.e("image", imagePath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            } else {

                return;
            }

        }

        @Override
        public void Gallary(Uri uri) {
            if (uri != null) {
                final String path = FileUtility.getPath(getActivity(), uri);
                if (path != null) {
                    startCropping(uri, CROP_GALLERY_IMAGE);
                }
            } else {

                return;
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 23) {

            //  Toast.makeText(baseActivity, "6", Toast.LENGTH_SHORT).show();
            Uri picUri = data.getData();

            pathOfImage = picUri.getPath();
            imageCompression = new ImageCompression(getActivity());
            imageCompression.execute(pathOfImage);
            imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                @Override
                public void processFinish(String imagePath) {
                    try {
                        file = new File(imagePath);
                        Glide.with(getActivity()).load("file://" + imagePath)
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.ivArtist);
                        paramsFile = new HashMap<>();
                        paramsFile.put(Consts.IMAGE, file);
                        //   Log.e("image", imagePath);
                        params = new HashMap<>();
                        params.put(Consts.USER_ID, userDTO.getUser_id());
                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            updateProfileSelf();
                        } else {
                       //     ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }


                        // Log.e("image", imagePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }

    public void updateProfileSelf() {
        new HttpsRequest(Consts.UPDATE_PROFILE_API, params, paramsFile, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                     //   ProjectUtils.showToast(getActivity(), msg);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        baseActivity.showImage();

                        Glide.with(getActivity()).
                                load(userDTO.getImage())
                                .placeholder(R.drawable.dummyuser_image)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.ivArtist);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                 //   ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void deleteImage() {
        paramsDeleteImg.put(Consts.USER_ID, userDTO.getUser_id());
        new HttpsRequest(Consts.DELETE_PROFILE_IMAGE_API, paramsDeleteImg, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    userDTO.setImage("");
                    artistDetailsDTO.setImage("");
                    prefrence.setParentUser(userDTO, Consts.USER_DTO);
                    showData();
                } else {
                //    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }


}
