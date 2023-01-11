package com.kamaii.rider.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.AdapterAllBookingsBinding;
import com.kamaii.rider.databinding.AdapterAllOldBookingBinding;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.ui.fragment.NewBookings;
import com.kamaii.rider.ui.models.ArtistBookingOld;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.MultiTouchListener;
import com.kamaii.rider.utils.OnTouch;
import com.kamaii.rider.utils.ProjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AdapterAllBookingsOld extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = AdapterAllBookingsOld.class.getSimpleName();
    private NewBookings newBookings;
    private ArrayList<ArtistBookingOld> artistBookingsList;
    private ArrayList<ArtistBookingOld> searchBookingsList;
    private UserDTO userDTO;
    private LayoutInflater myInflater;
    private Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;
    private GridLayoutManager gridLayoutManager;
    AdapterCart adapterCart;
    private ArrayList<ProductDTO> productDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private Dialog dialogpriview;
    String locationstatus = "";
    ImageView img_priview;
    Boolean isCheck = true;

    public static ImageView img_upload;

    public AdapterAllBookingsOld(NewBookings newBookings, ArrayList<ArtistBookingOld> artistBookingsList, UserDTO userDTO, LayoutInflater myInflater) {
        this.newBookings = newBookings;
        this.artistBookingsList = artistBookingsList;
        this.searchBookingsList = new ArrayList<ArtistBookingOld>();
        this.searchBookingsList.addAll(artistBookingsList);
        this.userDTO = userDTO;
        this.myInflater = myInflater;
        context = newBookings.getActivity();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        RecyclerView.ViewHolder vh;
        if (myInflater == null) {
            myInflater = LayoutInflater.from(viewGroup.getContext());
        }
        if (viewType == VIEW_ITEM) {
            AdapterAllOldBookingBinding binding =
                    DataBindingUtil.inflate(myInflater, R.layout.adapter_all_old_booking, viewGroup, false);
            vh = new MyViewHolder(binding);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section, viewGroup, false);
            vh = new MyViewHolderSection(v);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderMain, int pos) {

        dialogpriview = new Dialog(context, R.style.Theme_Dialog);
        dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpriview.setContentView(R.layout.dailog_priview);
        dialogpriview.setCancelable(true);


        final int position = pos;


        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        if (holderMain instanceof MyViewHolder) {
            final MyViewHolder holder = (MyViewHolder) holderMain;

            gridLayoutManager = new GridLayoutManager(context, 1);
            productDTOArrayList = new ArrayList<>();


            productDTOArrayList = artistBookingsList.get(position).getProduct();
            locationstatus = artistBookingsList.get(position).getLocation_status();
            adapterCart = new AdapterCart(newBookings, productDTOArrayList, locationstatus);
            holder.binding.rvCart.setLayoutManager(gridLayoutManager);
            holder.binding.rvCart.setAdapter(adapterCart);
            //holder.binding.llLocationdesti.setVisibility(View.VISIBLE);

            SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");

            try {
                Date dt = sdf.parse(artistBookingsList.get(position).getWorking_min());
                sdf = new SimpleDateFormat("HH:mm:ss");

                holder.binding.preparationTime.setText(context.getResources().getString(R.string.duration) + " " + sdf.format(dt));

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!artistBookingsList.get(position).getDestinationaddress().equalsIgnoreCase("")) {

                holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getDestinationaddress());
            } else {
                holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getAddress());

            }


            holder.binding.tvName.setText(artistBookingsList.get(position).getUserName());
            holder.binding.tvtotal.setText(artistBookingsList.get(position).getPrice());
            holder.binding.tvLocation.setText(artistBookingsList.get(position).getAddress());

            holder.binding.tvreason.setText(artistBookingsList.get(position).getDecline_reason());
            holder.binding.tvDate.setText(ProjectUtils.changeDateFormate1(artistBookingsList.get(position).getBooking_date()) + " " + artistBookingsList.get(position).getBooking_time());

            if (!artistBookingsList.get(position).getApproxdatetime().equalsIgnoreCase("")) {
                holder.binding.img.setVisibility(View.VISIBLE);
                holder.binding.tvapproxtime.setText("Approx Time" + " : " + artistBookingsList.get(position).getApproxdatetime());
            } else {
                holder.binding.img.setVisibility(View.GONE);
            }


            if (!artistBookingsList.get(position).getImage().equalsIgnoreCase("")) {
                Glide.with(context).
                        load(Consts.IMAGE_URL + artistBookingsList.get(position).getImage())
                        .placeholder(R.drawable.dummyuser_image)
                        .into(holder.binding.iviamge);
            } else {

            }


            holder.binding.txtmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCheck) {

                        // holder.binding.tmpView3.setVisibility(View.VISIBLE);
                        // holder.binding.tmpView2.setVisibility(View.VISIBLE);
                        holder.binding.laymoreee.setVisibility(View.VISIBLE);
                        isCheck = false;
                    } else {
                        // holder.binding.tmpView3.setVisibility(View.GONE);
                        //   holder.binding.tmpView2.setVisibility(View.GONE);
                        holder.binding.laymoreee.setVisibility(View.GONE);
                        isCheck = true;
                    }
                }
            });


            holder.binding.iviamge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_priview = dialogpriview.findViewById(R.id.img_priview);

                    if (!artistBookingsList.get(position).getImage().equalsIgnoreCase("")) {
                        Glide.with(context).
                                load(Consts.IMAGE_URL + artistBookingsList.get(position).getImage())
                                .placeholder(R.drawable.dummyuser_image)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(img_priview);
                    }

                    img_priview.setOnTouchListener(new MultiTouchListener(onTouch));
                    dialogpriview.show();
                }
            });
            if (!artistBookingsList.get(position).getUserMobile().equalsIgnoreCase("")) {
                holder.binding.imgphone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileno = artistBookingsList.get(position).getUserMobile();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileno));
                        context.startActivity(intent);
                    }
                });
            }

            Glide.with(context).
                    load(artistBookingsList.get(position).getUserImage())
                    .placeholder(R.drawable.dummyuser_image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivArtist);


            //holder.binding.tvDescription.setText(artistBookingsList.get(position).getDescription());
            holder.binding.tvcategory.setText(artistBookingsList.get(position).getCategory_name());


            if (artistBookingsList.get(position).getLocation_status().equalsIgnoreCase("1")) {
                holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.lightgreen));
                //holder.binding.imgLocationCustomer.setVisibility(View.VISIBLE);
            }


            if (artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("0") || artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("3")) {

                if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("2")) {
                    holder.binding.llSt.setVisibility(View.GONE);
                    holder.binding.llACDE.setVisibility(View.GONE);
                    holder.binding.tvCompleted.setVisibility(View.GONE);
                    holder.binding.iviamge.setVisibility(View.GONE);
                    //     holder.binding.tvRejected.setVisibility(View.VISIBLE);
                    holder.binding.llreason.setVisibility(View.VISIBLE);
                    //                    holder.binding.imgLocationCustomer.setVisibility(View.GONE);


                    holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.chat_in));


                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("4")) {
                    holder.binding.llSt.setVisibility(View.GONE);
                    holder.binding.llACDE.setVisibility(View.GONE);
                    //  holder.binding.tvCompleted.setVisibility(View.VISIBLE);


                    holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.white));


                    if (!artistBookingsList.get(position).getImage().equalsIgnoreCase("")) {
                        holder.binding.iviamge.setVisibility(View.VISIBLE);
                    }


                    holder.binding.tvRejected.setVisibility(View.GONE);
                    holder.binding.llreason.setVisibility(View.GONE);
                    //                    holder.binding.imgLocationCustomer.setVisibility(View.GONE);
                }


            } else if (artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("2")) {

                if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("2")) {
                    holder.binding.llSt.setVisibility(View.GONE);
                    holder.binding.llACDE.setVisibility(View.GONE);
                    holder.binding.tvCompleted.setVisibility(View.GONE);
                    holder.binding.iviamge.setVisibility(View.GONE);
                    //         holder.binding.tvRejected.setVisibility(View.VISIBLE);
                    holder.binding.llreason.setVisibility(View.VISIBLE);
                    //                    holder.binding.imgLocationCustomer.setVisibility(View.GONE);

                    holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.lightred));

                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("4")) {
                    holder.binding.llSt.setVisibility(View.GONE);
                    holder.binding.llACDE.setVisibility(View.GONE);
                    //      holder.binding.tvCompleted.setVisibility(View.VISIBLE);
                    if (!artistBookingsList.get(position).getImage().equalsIgnoreCase("")) {
                        holder.binding.iviamge.setVisibility(View.VISIBLE);
                    }


                    holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.white));

                    holder.binding.tvRejected.setVisibility(View.GONE);
                    holder.binding.llreason.setVisibility(View.GONE);
                    //      holder.binding.imgLocationCustomer.setVisibility(View.GONE);
                }


            }

        } else {
            MyViewHolderSection view = (MyViewHolderSection) holderMain;
            view.tvSection.setText(artistBookingsList.get(position).getSection_name());
        }
    }


    OnTouch onTouch = new OnTouch() {
        @Override
        public void removeBorder() {

        }
    };


    @Override
    public int getItemViewType(int position) {
        return artistBookingsList.get(position).isSection() ? VIEW_SECTION : VIEW_ITEM;
    }

    @Override
    public int getItemCount() {
        return artistBookingsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterAllOldBookingBinding binding;

        public MyViewHolder(AdapterAllOldBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }


    public static class MyViewHolderSection extends RecyclerView.ViewHolder {
        public CustomTextView tvSection;

        public MyViewHolderSection(View view) {
            super(view);
            tvSection = view.findViewById(R.id.tvSection);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        artistBookingsList.clear();
        if (charText.length() == 0) {
            artistBookingsList.addAll(searchBookingsList);
        } else {
            for (ArtistBookingOld historyDTO : searchBookingsList) {
                if (historyDTO.getUserName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    artistBookingsList.add(historyDTO);
                }
            }
        }
        notifyDataSetChanged();
    }
}
