package com.kamaii.rider.ui.adapter;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.rider.DatabaseHandler;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.fragment.CabBookingsFragment;
import com.kamaii.rider.ui.fragment.StoredImagesFragment;
import com.kamaii.rider.ui.models.OfflineDataArrayListModal;
import com.kamaii.rider.ui.models.OfflineImageDataModal;
import com.kamaii.rider.utils.CustomTextViewBold;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class OffLineImageAdapter extends RecyclerView.Adapter<OffLineImageAdapter.OfflineViewHolder> {

    Context context;
    List<OfflineImageDataModal> arraylist;
    private StoredImagesFragment storedImagesFragment;
    DatabaseHandler databaseHandler;
    SharedPrefrence sharedPrefrence;

    private Dialog dialogcamera;
    private CustomTextViewBold finddevice_btn, printbtn, tvdCancelreason, tvdAddreason, tvcameraskip, ctvbTitle, tvcamera, tvupload, tvtotalfinish, dialog_paytype, tvfinishsubmit, tvfinishsubmitrating;
    ImageView tvfinishcancel, pay_done_img,imageClose;
    public static ImageView img_upload;
    public static Uri img_uri = null;
    public static String img_url = "";
    public OffLineImageAdapter(StoredImagesFragment storedImagesFragment,Context context, List<OfflineImageDataModal> arraylist) {
        this.storedImagesFragment = storedImagesFragment;
        this.context = context;
        this.arraylist = arraylist;
        databaseHandler = new DatabaseHandler(context);
        sharedPrefrence = SharedPrefrence.getInstance(context);

    }

    @NonNull
    @Override
    public OfflineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stored_images_rv_layout,parent,false);

        return new OfflineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (arraylist.get(position).getImage_path().equalsIgnoreCase("")){
            Log.e("TAG", "onBindViewHolder: ahi ayu 66666  first"+arraylist.get(position).getImage_path());
            holder.imgsubmit.setVisibility(View.GONE);
            holder.imgUpload.setVisibility(View.VISIBLE);
        }
        dialogcamera = new Dialog(context, R.style.Theme_Dialog);
        dialogcamera.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcamera.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogcamera.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogcamera.setContentView(R.layout.dailog_camera);
        dialogcamera.setCancelable(false);
        holder.id.setText(arraylist.get(position).getBooking_id());

        tvcameraskip = dialogcamera.findViewById(R.id.tvcameraskip);
        tvcameraskip.setVisibility(View.GONE);
        tvcamera = dialogcamera.findViewById(R.id.tvcamera);
        ctvbTitle = dialogcamera.findViewById(R.id.ctvbTitle);
        ctvbTitle.setText("Order Photo");
        tvupload = dialogcamera.findViewById(R.id.tvupload);
        imageClose = dialogcamera.findViewById(R.id.imageClose);
        img_upload = dialogcamera.findViewById(R.id.img_upload);
        Log.e("TAG", "onBindViewHolder: uri "+img_uri );
        if (img_uri != null){
            img_upload.setVisibility(View.VISIBLE);
            img_upload.setImageURI(img_uri);
        }
        Log.e("TAG", "onBindViewHolder: ahi ayu 66666 position :-- "+position+arraylist.get(position).getImage_path());

        if (arraylist.get(position).getTracker()!=null){
            holder.tracker.setText(arraylist.get(position).getTracker());
        }

        if (arraylist.get(position).getImage_path() != null || !arraylist.get(position).getImage_path().equalsIgnoreCase("")){
            Log.e("TAG", "onBindViewHolder: ahi ayu 66666 position :-- "+position+arraylist.get(position).getImage_path());

            byte[] decodedString = Base64.decode(arraylist.get(position).getImage_path(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //  image.setImageBitmap(decodedByte);
            Glide.with(context).load(decodedByte).into(holder.offlineimage);
            holder.imgsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickupbookingupload(arraylist.get(position).getTracker(),position);
                }
            });
        }else {
            Log.e("TAG", "onBindViewHolder: ahi ayu nathi ");
            holder.imgsubmit.setVisibility(View.GONE);
            holder.imgUpload.setVisibility(View.VISIBLE);
        }

        holder.imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogcamera.show();
                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogcamera.dismiss();
                        //notifyDataSetChanged();
                        //newBookings.reloadFragment();
                    }
                });
                tvupload.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                            requestPermissions(storedImagesFragment.getActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},23);
                        }else {

                            storedImagesFragment.opencamrea();
                        }

                        //  lldauploadImageLayout.setVisibility(View.GONE);
                    }
                });

                tvcamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (img_upload.getVisibility() == View.GONE) {

                            Toast.makeText(context, "Please upload the image!!", Toast.LENGTH_SHORT).show();
//                                        tvcamera.setEnabled(false);
//                                        tvcamera.setClickable(false);
                            Log.e("TAG", "onSlideComplete: btnclick_flag ");

                            return;

                        } else {
                            Log.e("TAG", "onSlideComplete: btnclick_flag 2 ");

                            tvcamera.setEnabled(true);
                            tvcamera.setClickable(true);
                        }
                        Log.e("TAG", "onClick: img_urlll "+img_url );
                        if (!img_url.equalsIgnoreCase("") || img_url != null){

//                            commonbookingupload(arraylist.get(position).getTracker(),position,img_url);
                            Log.e("TAG", "onBindViewHolder: ahi ayu 66666 tracker 11" );
                            OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                            offlineImageDataModal.setBooking_id(arraylist.get(position).getBooking_id());
                            Log.e("TAG", "onBindViewHolder: ahi ayu 66666 before add in model "+img_url );
//                            offlineImageDataModal.setImage_path(String.valueOf(img_uri));
                            offlineImageDataModal.setImage_path(sharedPrefrence.getValue("pic_img"));

                            offlineImageDataModal.setTracker(arraylist.get(position).getTracker());
                            offlineImageDataModal.setApproxdatetime(arraylist.get(position).getApproxdatetime());
                            offlineImageDataModal.setRequest(arraylist.get(position).getRequest());
                            offlineImageDataModal.setUser_id(arraylist.get(position).getUser_id());

                            databaseHandler.updateContact(offlineImageDataModal);

                            List<OfflineImageDataModal> a = databaseHandler.getAllContacts();
//                            Log.e("TAG", "onBindViewHolder: ahi ayu 66666 aaaaaaaaa "+a.get(0).getImage_path() );
//                            Log.e("TAG", "onBindViewHolder: ahi ayu 66666 bbbbbbbb "+a.get(1).getImage_path() );
                            dialogcamera.dismiss();
                            notifyItemRangeChanged(position, arraylist.size());
                            notifyDataSetChanged();
                            storedImagesFragment.reloadFragment();
                        }else {
                            Log.e("TAG", "onBindViewHolder: ahi ayu 66666 tracker 22" );
                        }
                    }
                });
            }
        });

    }

    public void commonbookingupload(String req, int pos,String img) {

        HashMap<String,String> paramsBookingOp = new HashMap<>();
        paramsBookingOp.put("booking_id", arraylist.get(pos).getBooking_id());
        paramsBookingOp.put("tracker", arraylist.get(pos).getTracker());
        paramsBookingOp.put("user_id",arraylist.get(pos).getUser_id());
        paramsBookingOp.put("amount",arraylist.get(pos).getAmount());
        paramsBookingOp.put("approxdatetime",arraylist.get(pos).getApproxdatetime());
        paramsBookingOp.put("request",arraylist.get(pos).getRequest());
        paramsBookingOp.put("image",img);

        Log.e("TAG", "commonbookingupload: params "+paramsBookingOp.toString() );
        new HttpsRequest(Consts.OFFLINE_IMG_API_NEW, paramsBookingOp, context).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {
                    OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                    offlineImageDataModal.setBooking_id(arraylist.get(pos).getBooking_id());
                    offlineImageDataModal.setImage_path(arraylist.get(pos).getImage_path());
                    offlineImageDataModal.setTracker(arraylist.get(pos).getTracker());
                    databaseHandler.deleteContact(offlineImageDataModal);
                    arraylist.remove(pos);
//                    context.reremoveViewAt(position);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, arraylist.size());
                    notifyDataSetChanged();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    dialogcamera.dismiss();
                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void pickupbookingupload(String req, int pos) {

        HashMap<String,String> paramsBookingOp = new HashMap<>();
        paramsBookingOp.put("booking_id", arraylist.get(pos).getBooking_id());
        paramsBookingOp.put("tracker", arraylist.get(pos).getTracker());
        paramsBookingOp.put("user_id",arraylist.get(pos).getUser_id());
        paramsBookingOp.put("amount",arraylist.get(pos).getAmount());
        paramsBookingOp.put("approxdatetime",arraylist.get(pos).getApproxdatetime());
        paramsBookingOp.put("request",arraylist.get(pos).getRequest());
        paramsBookingOp.put("image",arraylist.get(pos).getImage_path());


        new HttpsRequest(Consts.OFFLINE_IMG_API_NEW, paramsBookingOp, context).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {
                    OfflineImageDataModal offlineImageDataModal = new OfflineImageDataModal();
                    offlineImageDataModal.setBooking_id(arraylist.get(pos).getBooking_id());
                    offlineImageDataModal.setImage_path(arraylist.get(pos).getImage_path());
                    offlineImageDataModal.setTracker(arraylist.get(pos).getTracker());
                    databaseHandler.deleteContact(offlineImageDataModal);
                    arraylist.remove(pos);
//                    context.reremoveViewAt(position);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, arraylist.size());
                    notifyDataSetChanged();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class OfflineViewHolder extends RecyclerView.ViewHolder {
        TextView id,tracker;
        ImageView offlineimage;
        Button imgsubmit,imgUpload;

        public OfflineViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.booking_id);
            tracker = itemView.findViewById(R.id.tracker);
            offlineimage = itemView.findViewById(R.id.offlineimage);
            imgsubmit = itemView.findViewById(R.id.imgsubmit);
            imgUpload = itemView.findViewById(R.id.imgupload);
        }
    }
}
