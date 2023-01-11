package com.kamaii.rider.interfacess;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface apiRest {
    @FormUrlEncoded
    @POST("SignUp")
    Call<ResponseBody> SignUp(@Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("password") String password, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("token") String token);

    @FormUrlEncoded
    @POST("getMapCategory")
    Call<ResponseBody> getMapCategory(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("checksignin")
    Call<ResponseBody> checksignin(@Field("mobile") String mobile, @Field("role") String role);

    @FormUrlEncoded
    @POST("getAllBookingArtistCab")
    Call<ResponseBody>  getAllBookingArtistCab(@Field("artist_id") String artist_id);

    @FormUrlEncoded
    @POST("getAllBookingArtistCabtest")
    Call<ResponseBody> getAllBookingArtistCabtest(@Field("artist_id") String artist_id);

   /* @FormUrlEncoded
    @POST("getAllBookingArtistCabtestrider")
    Call<ResponseBody> getAllBookingArtistCabtestrider(@Field("artist_id") String artist_id);
*/
    @FormUrlEncoded
    @POST("booking_operation")
    Call<ResponseBody> booking_operationCopy(@Field("booking_id") String booking_id,@Field("request") String request,@Field ("approxdatetime") String approxdatetime,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getAllBookingArtistCabtestrider_new_flow")
    Call<ResponseBody> getAllBookingArtistCabtestrider(@Field("artist_id") String artist_id);

    @FormUrlEncoded
    @POST("getAllBookingArtistCabtestnewcheck")
    Call<ResponseBody> getAllBookingArtistCabtestnewcheck(@Field("artist_id") String artist_id);

    @FormUrlEncoded
    @POST("onlineOffline")
    Call<ResponseBody> onlineOffline(@Field("user_id") String user_id, @Field("is_online") String is_online, @Field("device_token") String device_token, @Field("offline_reason") String offline_reason, @Field("source_lat") String source_lat, @Field("source_long") String source_long);

    @FormUrlEncoded
    @POST("check_rider_leave")
    Call<ResponseBody> checkRiderLeave(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("addProduct")
    Call<ResponseBody> addProduct(@Field("category_id") String category_id, @Field("sub_category_id") String sub_category_id, @Field("third_id") String third_id, @Field("user_id") String user_id, @Field("product_name") String product_name, @Field("service_id") String service_id, @Field("partner_number") String partner_number, @Field("service_image") String image_str, @Field("service_price") String price);

    @FormUrlEncoded
    @POST("deleteNotification")
    Call<ResponseBody> deleteNotification(@Field("id") String id);

    @FormUrlEncoded
    @POST("getMyInvoice")
    Call<ResponseBody> getMyInvoice(@Field("user_id") String user_id, @Field("role") String role);


    @FormUrlEncoded
    @POST("getMyTicket")
    Call<ResponseBody> getMyTicket(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("booking_operation")
    Call<ResponseBody> booking_operation(@Field("booking_id") String booking_id, @Field("request") String request, @Field("user_id") String user_id, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("address_str") String address);

    @FormUrlEncoded
    @POST("reach_destination2")
    Call<ResponseBody> setReachedLocation(@Field("user_id") String user_id, @Field("latitude") String latitude, @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("getAllBookingArtist")
    Call<ResponseBody> getAllBookingArtist(@Field("artist_id") String user_id,@Field("range") String id);

    @FormUrlEncoded
    @POST("getcashdepositdetail")
    Call<ResponseBody> getCashDepositeDetail(@Field("artist_id") String artist_id);

    @FormUrlEncoded
    @POST("send_otp")
    Call<ResponseBody> send_otp(@Field("mobile") String mobile,@Field("role") String role);

    @FormUrlEncoded
    @POST("resend_otp")
    Call<ResponseBody> resend_otp(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("getArtistByid")
    Call<ResponseBody> getArtistByid(@Field("artist_id") String artist_id, @Field("user_id") String user_id, @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("arrival")
    Call<ResponseBody> arival(@Field("artist_id") String artist_id, @Field("booking_id") String booking_id);

    @FormUrlEncoded
    @POST("rider_earning_data")
    Call<ResponseBody> getEarningData(@Field("rider_id") String artist_id, @Field("day") String booking_id,@Field("range") String range);

    @FormUrlEncoded
    @POST("updateLocation")
    Call<ResponseBody> updateLocation(@Field("user_id") String user_id, @Field("role") String role, @Field("latitude") String latitude, @Field("longitude") String longitude);

    @Multipart
    @POST("addPartnerDocuments")
    Call<ResponseBody> Update_Step_One(@Part("model_name") RequestBody model_name, @Part("city") RequestBody city, @Part("car_booking") RequestBody car_booking,
                                       @Part("car_category") RequestBody car_category,
                                       @Part("ref_number") RequestBody ref_number,
                                       @Part("car_number") RequestBody car_number,
                                       @Part("bank_name") RequestBody bank_name,
                                       @Part("benefiacary_name") RequestBody benefiacary_name,
                                       @Part("account_number") RequestBody account_number,
                                       @Part("ifsc_code") RequestBody ifsc_code,
                                       @Part("branch") RequestBody branch,
                                       @Part MultipartBody.Part fileone, @Part MultipartBody.Part filetwo, @Part MultipartBody.Part filethree, @Part MultipartBody.Part filefour,
                                       @Part MultipartBody.Part filefive, @Part MultipartBody.Part filesix, @Part MultipartBody.Part fileseven, @Part MultipartBody.Part fileeight, @Part MultipartBody.Part filenine, @Part MultipartBody.Part fileten, @Part("artist_id") RequestBody artist_id, @Part MultipartBody.Part fileeleven, @Part MultipartBody.Part filetwelve, @Part MultipartBody.Part filethirtenn, @Part MultipartBody.Part filethirdfourtenn, @Part("cat_id") RequestBody cat_id);

    @Multipart
    @POST("basicinfostep4")
    Call<ResponseBody> upload_business_data(
                                            @Part MultipartBody.Part filethree, @Part MultipartBody.Part filefour,
                                            @Part("artist_id") RequestBody artist_id,
                                            @Part MultipartBody.Part filetwelve,
                                            @Part MultipartBody.Part filethirtenn,
                                            @Part ("vehicle_image") RequestBody vehicle_image,
                                            @Part ("vehicle_id") RequestBody vehicle_id,
                                            @Part ("vehicle_no") RequestBody vehicle_no);

    @Multipart
    @POST("basicinfostep6")
    Call<ResponseBody> Add_bank_details(
            @Part("bank_name") RequestBody bank_name,
            @Part("benefiacary_name") RequestBody benefiacary_name,
            @Part("account_number") RequestBody account_number,
            @Part("ifsc_code") RequestBody ifsc_code,
            @Part("branch") RequestBody branch,
            @Part MultipartBody.Part fileten, @Part("artist_id") RequestBody artist_id);

    @Multipart
    @POST("basicinfostep3")
    Call<ResponseBody> Add_PERSONAL_DOC(
            @Part MultipartBody.Part fileone,
            @Part MultipartBody.Part filetwo,
            @Part MultipartBody.Part filethree,
            @Part("artist_id") RequestBody artist_id);


}
