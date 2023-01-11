package com.kamaii.rider.ui.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.HistoryDTO;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.Glob;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.ActivityViewInvoiceBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.adapter.AdapterInvoiceService;
import com.kamaii.rider.ui.fragment.PaidFrag;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewInvoice extends AppCompatActivity {

    private ActivityViewInvoiceBinding binding;
    private HistoryDTO historyDTO;
    private Context mContext;
    // private LinearLayout llPdf;
    private Bitmap bitmap;
    double totalamount = 0, artistamount = 0, commisionamount = 0;
    private GridLayoutManager gridLayoutManager;
    AdapterInvoiceService adapterInvoiceService;
    private ArrayList<ProductDTO> productDTOArrayList;
    private String booking_id = "";
    private String TAG = PaidFrag.class.getSimpleName();
    private ArrayList<HistoryDTO> historyDTOList;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    CustomTextViewBold servicecharge;
    String s_charge = "";
    RelativeLayout rlClick;
    CardView cardData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_invoice);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_invoice);
        mContext = ViewInvoice.this;
        // llPdf=findViewById(R.id.llPdf);
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        servicecharge = findViewById(R.id.service_digit_txt1);
        cardData = findViewById(R.id.cardData);
        rlClick = findViewById(R.id.rlClick);
        if (getIntent().hasExtra(Consts.BOOKING_ID)) {
            booking_id = getIntent().getStringExtra(Consts.BOOKING_ID);
        }
        getHistroy();
    }

    public void setUiAction() {

        binding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(mContext).
                load(historyDTO.getUserImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProfile);
        gridLayoutManager = new GridLayoutManager(ViewInvoice.this, 1);
        productDTOArrayList = historyDTO.getProduct();
        adapterInvoiceService = new AdapterInvoiceService(mContext, productDTOArrayList, "0");
        binding.rvCart.setLayoutManager(gridLayoutManager);
        binding.rvCart.setAdapter(adapterInvoiceService);

        binding.tvDate.setText(ProjectUtils.changeDateFormate1(historyDTO.getBooking_date()) + " " + historyDTO.getBooking_time());
        binding.tvInvoiceId.setText(historyDTO.getInvoice_id());
        binding.tvName.setText(ProjectUtils.getFirstLetterCapital(historyDTO.getUserName()));
        binding.tvServiceType.setText(historyDTO.getCategoryName());
        // binding.tvWork.setText(historyDTO.getProduct());
        binding.tvPrice.setText(Html.fromHtml("&#x20B9;" + historyDTO.getArtist_amount()));
        binding.tvSubtotal.setText(Html.fromHtml("&#x20B9;" + historyDTO.getItemtotal()));
        //binding.tvservicetotal.setText(historyDTO.getTotal_amount() +" "+"Rs");

        if (!historyDTO.getAddress().equalsIgnoreCase("")) {
            binding.etAddressSelectsource.setVisibility(View.VISIBLE);
            binding.etAddressSelectsource.setText(historyDTO.getAddress());
        }

        if (!historyDTO.getDestinationaddress().equalsIgnoreCase("")) {
            binding.etAddressSelectdesti.setVisibility(View.VISIBLE);
            binding.etAddressSelectdesti.setText(historyDTO.getDestinationaddress());
        } else {
            binding.etAddressSelectdesti.setVisibility(View.VISIBLE);
            binding.etAddressSelectdesti.setText(historyDTO.getAddress());
        }

        if (historyDTO.getPayment_type().equalsIgnoreCase("0")) {
            binding.tvptype.setText("Online Payment");
        } else if (historyDTO.getPayment_type().equalsIgnoreCase("1")) {
            binding.tvptype.setText("Cash Payment");
        } else if (historyDTO.getPayment_type().equalsIgnoreCase("2")) {
            binding.tvptype.setText("Wallet Payment");
        }
        totalamount = Double.parseDouble(historyDTO.getTotal_amount());
        artistamount = Double.parseDouble(historyDTO.getArtist_amount());

        try {
            commisionamount = totalamount - artistamount;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }

        DecimalFormat newFormat = new DecimalFormat("##.##");
        String mainprice = String.valueOf(newFormat.format(commisionamount));
        // binding.tvDiscount.setText(mainprice+" "+"Rs");

        binding.tvTotal.setText(Html.fromHtml("&#x20B9;" + historyDTO.getServicecharge()));
        s_charge = historyDTO.getServicecharge();


        if (productDTOArrayList.get(0).getSub_category_id().equals("242") || productDTOArrayList.get(0).getSub_category_id().equals("434")) {
            binding.serviceTxt.setText("Driver Allowance");
            binding.serviceDigitTxt1.setText(Html.fromHtml("&#x20B9;" + historyDTO.getServicecharge()));
        } else if (productDTOArrayList.get(0).getSub_category_id().equals("453")) {
            binding.serviceTxt.setVisibility(View.GONE);
            binding.serviceDigitTxt1.setVisibility(View.GONE);
        } else {
            if (s_charge.equals("0")) {
                binding.serviceDigitTxt1.setText("Free");
            } else {
                binding.serviceDigitTxt1.setText(Html.fromHtml("&#x20B9;" + historyDTO.getServicecharge()));
            }
        }

        binding.btnPdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ViewInvoice.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ViewInvoice.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {

                    File file = saveBitMap(ViewInvoice.this, cardData);    //which view you want to pass that view as parameter
                    if (file != null) {
                        Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
                    } else {
                        Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
                    }
                }

            }
        });
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpeg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
            Toast.makeText(ViewInvoice.this, "Invoice saved at Pictures/Handcare in your file manager", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        return pictureFile;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            File file = saveBitMap(ViewInvoice.this, cardData);    //which view you want to pass that view as parameter
            if (file != null) {
                Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
            } else {
                Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
            }

        } else {
            Toast.makeText(mContext, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;


        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
        String targetPdf = binding.tvInvoiceId.getText().toString() + ".pdf";
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // File myDir = new File(Environment.getExternalStorageDirectory().toString(), "/OkyBookPartner");
            File myDir = new File(android.os.Environment.getExternalStorageDirectory().toString(), "/OkyBookPartner");


            if (!myDir.exists()) {
                boolean md = myDir.mkdirs();
                if (!md) {

                }
            }

            final File file = new File(myDir, targetPdf);
            if (file.exists()) {
                file.delete();
            }
            try {
                document.writeTo(new FileOutputStream(file));

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("PDF_ERROR", "" + e.toString());
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            }
            Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();
        }
        document.close();


          openGeneratedPDF();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void openGeneratedPDF() {
        File file = new File("/sdcard/pdffromlayout.pdf");
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(ViewInvoice.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            } catch (FileUriExposedException e) {

            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        com.kamaii.rider.Glob.BUBBLE_VALUE = "0";
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.BOOKING_ID, booking_id);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ROLE, "3");
        Log.e("response_history", "" + parms.toString());
        return parms;
    }

    public boolean checkarss(String catid) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;
            } else {
                value = false;
            }

        }
        return value;
    }

    public void getHistroy() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_INVOICE_API2, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                Log.e("response_history", "" + response.toString());
                if (flag) {

                    try {
                        historyDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<HistoryDTO>>() {
                        }.getType();
                        historyDTOList = (ArrayList<HistoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        historyDTO = historyDTOList.get(0);
                        setUiAction();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
