package com.kamaii.rider.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kamaii.rider.R;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.fragment.AddReferFragment;
import com.kamaii.rider.ui.fragment.ContactUs;
import com.kamaii.rider.ui.fragment.MainserviceFragment;
import com.kamaii.rider.ui.fragment.MyEarning;
import com.kamaii.rider.ui.fragment.NewBookings;
import com.kamaii.rider.ui.fragment.PaidFrag;
import com.kamaii.rider.ui.fragment.PromotionFragment;
import com.kamaii.rider.ui.fragment.TVideoFragment;
import com.kamaii.rider.ui.fragment.Wallet;
import com.kamaii.rider.ui.models.SiderModel;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    private ArrayList<SiderModel> images;
    private LayoutInflater inflater;
    private Context context;

    public SliderAdapter(Context context, ArrayList<SiderModel> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);

        Log.e("Sliderimagesize", "" + images.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = myImageLayout.findViewById(R.id.image);

        String thumbnailMq = "http://img.youtube.com/vi/" + images.get(position).getImage() + "/mqdefault.jpg";

        Glide.with(context)
                .load(images.get(position).getImage()).placeholder(R.drawable.dafault_product)
                .into(myImage);
        // myImage.setImageResource(Integer.parseInt(images.get(position).getImage()));


        view.addView(myImageLayout, 0);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (images.get(position).getUrl().equalsIgnoreCase("0")) {

                    ((BaseActivity) context).navItemIndex = 39;
                    ((BaseActivity) context).loadHomeFragment(new MainserviceFragment(), "8");
                } else {
                    getFragment(images.get(position).getUrl());
                }
                //Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(images.get(position).getUrl()));


                //Toast.makeText(context,"click",Toast.LENGTH_SHORT).show();
            }
        });
        return myImageLayout;
    }

    public void getFragment(String value) {
        switch (value) {

            case "1":
                ((BaseActivity) context).navItemIndex = 59;
                ((BaseActivity) context).loadHomeFragment(new MainserviceFragment(), "profile");
                break;
            case "2":
                ((BaseActivity) context).navItemIndex = 59;

                ((BaseActivity) context).loadHomeFragment(new TVideoFragment(), "tvideo");
                break;
            case "3":
                ((BaseActivity) context).navItemIndex = 59;

                ((BaseActivity) context).loadHomeFragment(new AddReferFragment(), "notification");
                break;
            case "4":
                ((BaseActivity) context).navItemIndex = 59;

                ((BaseActivity) context).loadHomeFragment(new MyEarning(), "earn");
                break;
            case "5":
                ((BaseActivity) context).navItemIndex = 59;

                ((BaseActivity) context).loadHomeFragment(new PromotionFragment(), "promotion");
                break;
            case "6":
                ((BaseActivity) context).navItemIndex = 59;

                ((BaseActivity) context).loadHomeFragment(new ContactUs(), "history");
                break;
            case "7":
                ((BaseActivity) context).navItemIndex = 59;

                ((BaseActivity) context).loadHomeFragment(new NewBookings(), "jobs");
                break;
            case "8":
                ((BaseActivity) context).navItemIndex = 59;

                ((BaseActivity) context).loadHomeFragment(new Wallet(), "wallet");
                break;
            case "9":
                ((BaseActivity) context).navItemIndex = 59;

                ((BaseActivity) context).loadHomeFragment(new PaidFrag(), "history");
                break;

        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
