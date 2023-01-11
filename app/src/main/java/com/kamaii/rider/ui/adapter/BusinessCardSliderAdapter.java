package com.kamaii.rider.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.ui.models.BusinessCardSliderModel;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BusinessCardSliderAdapter extends PagerAdapter {

    private ArrayList<BusinessCardSliderModel> images;
    private LayoutInflater inflater;
    private Context context;
    ArrayList<CategoryDTO> categoryDTOS;
    UserDTO userDTO;

    ImageView myImage;
    ListView imagelistview;
    CustomTextViewBold b_name;
    CustomTextViewBold rider_name;
    CustomTextViewBold business_services_name;

    public BusinessCardSliderAdapter(Context context, ArrayList<BusinessCardSliderModel> images, UserDTO userDTO, ArrayList<CategoryDTO> categoryDTOS) {
        this.context = context;
        this.images = images;
        this.userDTO = userDTO;
        this.categoryDTOS = categoryDTOS;
        inflater = LayoutInflater.from(context);

        Log.e("Sliderimagesize1234", "" + images.size());
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
    public @NotNull Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide2, view, false);
        ImageView myImage = myImageLayout.findViewById(R.id.image2);
        ListView imagelistview = myImageLayout.findViewById(R.id.slide2_listview);
        CustomTextViewBold b_name = myImageLayout.findViewById(R.id.b_name);
        CustomTextViewBold rider_name = myImageLayout.findViewById(R.id.business_card_rider_name);
        CustomTextViewBold business_services_name = myImageLayout.findViewById(R.id.business_services_name);


        b_name.setText(userDTO.getName());
        rider_name.setText(images.get(position).getAtist_name());

        imagelistview.setAdapter(new listAdapter(categoryDTOS));
        Log.e("LOSULALIT", "LOSULALIT1");
        Log.e("Sliderimagesize1234", "" + images.get(position).getImg_path());


        Glide.with(context)
                .load(images.get(position).getImg_path())
                .placeholder(R.drawable.dafault_product)
                .into(myImage);
        // myImage.setImageResource(Integer.parseInt(images.get(position).getImage()));


        view.addView(myImageLayout, 0);

        return myImageLayout;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    class listAdapter extends BaseAdapter {

        List<CategoryDTO> categoryDTOS;

        public listAdapter(List<CategoryDTO> categoryDTOS) {
            this.categoryDTOS = categoryDTOS;
        }

        @Override
        public int getCount() {
            return categoryDTOS.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.businesscard_listview_recycle_layout, null);

            CustomTextView customTextViewBold = view.findViewById(R.id.business_layout_textview);


            if (position <= 5) {

                customTextViewBold.setText(categoryDTOS.get(position).getCat_name());
            } else {
                customTextViewBold.setVisibility(View.GONE);
            }
            return view;
        }
    }
}


