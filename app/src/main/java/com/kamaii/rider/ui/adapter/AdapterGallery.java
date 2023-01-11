package com.kamaii.rider.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.rider.DTO.GalleryDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.ui.fragment.ImageGallery;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterGallery extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ImageGallery imageGallery;
    LayoutInflater mLayoutInflater;
    private ArrayList<GalleryDTO> gallery;
    private Context context;
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;
    private HashMap<String, String> parms = new HashMap<>();
    private String TAG = AdapterGallery.class.getSimpleName();
    private DialogInterface dialog_book;

    public AdapterGallery(ImageGallery imageGallery, ArrayList<GalleryDTO> gallery) {
        this.imageGallery = imageGallery;
        context = imageGallery.getActivity();
        this.gallery = gallery;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gallery, parent, false);
            return new ViewHolder1(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gallery, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_CAMERA:
                final ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                viewHolder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageGallery.addGalleryClick();
                    }
                });
                break;

            case TYPE_NORMAL:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                final int pos = position - 1;
                if (gallery.get(pos).getImage().equalsIgnoreCase("")){
                    myViewHolder.itemView.setVisibility(View.GONE);
                }
                Glide
                        .with(context)
                        .load(gallery.get(pos).getImage())
                        .placeholder(R.drawable.bg)
                        .into(myViewHolder.iv_bottom_foster);

                myViewHolder.iv_bottom_foster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageGallery.showImg(gallery.get(pos).getImage());
                    }
                });

                myViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parms.put(Consts.ID, gallery.get(pos).getId());
                        parms.put(Consts.USER_ID, gallery.get(pos).getUser_id());
                        deleteDialog();
                    }
                });

                break;

        }

    }

    @Override
    public int getItemCount() {
        return gallery.size() + 1;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_bottom_foster, ivDelete;

        public MyViewHolder(View view) {
            super(view);

            iv_bottom_foster = (ImageView) itemView.findViewById(R.id.iv_bottom_foster);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);


        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        public ViewHolder1(View v) {
            super(v);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_NORMAL;
        }
    }

    public void deleteDialog() {
        try {
            new AlertDialog.Builder(context)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(context.getResources().getString(R.string.delete_gallery))
                    .setMessage(context.getResources().getString(R.string.delete_gallery_msg))
                    .setCancelable(false)
                    .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            deleteGallery();

                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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

    public void deleteGallery() {
        new HttpsRequest(Consts.DELETE_GALLERY_API, parms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    imageGallery.getParentData();
                } else {
                    ProjectUtils.showLong(context, msg);
                }
            }
        });
    }

}