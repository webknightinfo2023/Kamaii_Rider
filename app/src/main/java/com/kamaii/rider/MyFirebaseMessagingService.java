package com.kamaii.rider;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.service.MyService;
import com.kamaii.rider.service.apiClient;
import com.kamaii.rider.ui.activity.BaseActivity;

import java.io.IOException;
import java.util.Map;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPrefrence prefrence;
    int i = 0;
    Context context;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    boolean isPLAYING = false;
    MediaPlayer mediaPlayer;
    public Intent mServiceIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        prefrence = SharedPrefrence.getInstance(this);
        context = this;

      /*  Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restart");
        broadcastIntent.setClass(this, MyDeviceWeakUpReceiver.class);
        this.sendBroadcast(broadcastIntent);
*/
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().containsKey(Consts.TYPE)) {
                if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.CHAT_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.CHAT_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.TICKET_COMMENT_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.TICKET_COMMENT_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.TICKET_STATUS_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.TICKET_STATUS_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.WALLET_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.WALLET_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION)) {
                    // sendNotification(getValue(remoteMessage.getData(), "body"), Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION);
                    sendNotificationrringdecline(getValue(remoteMessage.getData(), "body"), Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.START_BOOKING_ARTIST_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.START_BOOKING_ARTIST_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.RIDER_HAS_PICKUP_ORDER)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.RIDER_HAS_PICKUP_ORDER);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.BRODCAST_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.BRODCAST_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.ADMIN_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.ADMIN_NOTIFICATION);
                } /*else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.BOOK_ARTIST_NOTIFICATION)) {
                    sendNotificationrring(getValue(remoteMessage.getData(), "body"), Consts.BOOK_ARTIST_NOTIFICATION);
                }*/ else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.JOB_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.JOB_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.DELETE_JOB_NOTIFICATION)) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.DELETE_JOB_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.SEND_RIDER_NOTIFICATION)) {
                    sendNotificationrring(getValue(remoteMessage.getData(), "body"), Consts.SEND_RIDER_NOTIFICATION);
                }/* else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.ACCEPT_REQUEST_RIDER_NOTIFICATION)) {
                    sendNotificationrring(getValue(remoteMessage.getData(), "body"), Consts.ACCEPT_REQUEST_RIDER_NOTIFICATION);
                }*/ else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.VENDOR_HAS_BOOKING_END_SUCCESSFULLY)) {
                    sendNotificationstoprring(getValue(remoteMessage.getData(), "body"), Consts.VENDOR_HAS_BOOKING_END_SUCCESSFULLY);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.APKA_ORDER_DELAY_HAI)) {
                    sendURLNotificationrring(getValue(remoteMessage.getData(), "body"), remoteMessage.getData().get(Consts.URL), Consts.APKA_ORDER_DELAY_HAI);
                }else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.RANDOM_RIDER_NOTIFICATION)) {
                    sendURLNotificationrring(getValue(remoteMessage.getData(), "body"), remoteMessage.getData().get(Consts.URL), Consts.RANDOM_RIDER_NOTIFICATION);
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.SEND_RESPONSE_NOTIFICATION)) {
                    // notificationResponse(getValue(remoteMessage.getData(), "body"), Consts.SEND_RESPONSE_NOTIFICATION);
                    //refreshLayout(getValue(remoteMessage.getData(), "body"), Consts.SEND_RESPONSE_NOTIFICATION);
                    sendMobileWakeupNotification(getValue(remoteMessage.getData(), "body"), Consts.SEND_RESPONSE_NOTIFICATION);
                } else {
                    sendNotification(getValue(remoteMessage.getData(), "body"), "");
                }
            }

        }


    }

    public String getValue(Map<String, String> data, String key) {
        try {
            if (data.containsKey(key))
                return data.get(key);
            else
                return getString(R.string.app_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            return getString(R.string.app_name);
        }
    }

    @Override
    public void onNewToken(String token) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Consts.DEVICE_TOKEN, token);
        editor.commit();

        Log.d(TAG, "Refreshed token: " + token);

    }

    private void notifyFragment(String json) {
        Intent intent = new Intent("nameOfTheActionShouldBeUniqueName");
        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendNotification(String messageBody, String tag) {

        Log.e("NOTIFICATION_TRACKER", "notification called 1");

        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra(Consts.SCREEN_TAG, tag);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        long[] v = {500, 1000};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                //  .setSound(defaultSoundUri)
                //  .setVibrate(v)
                /*.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))*/
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);

        //  builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }


    private void sendNotificationstoprring(String messageBody, String tag) {

        Log.e("NOTIFICATION_TRACKER", "notification called 2");
        Intent resultIntent = null;
        /*if(CURRENT_TAG.equalsIgnoreCase(TAG_BOOKINGS_cab)){
            resultIntent = new Intent(getApplicationContext(), BaseActivity.class);
            resultIntent.putExtra("Notification","notification");
            resultIntent.putExtra("message", messageBody);
            notifyFragment(messageBody);

        }else {
             resultIntent = new Intent(getApplicationContext(), BaseActivity.class);
            resultIntent.putExtra("Notification","notification");
            resultIntent.putExtra("message", messageBody);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(resultIntent);
        }*/

        resultIntent = new Intent(this, BaseActivity.class);
        resultIntent.putExtra("Notification", "notification");
        resultIntent.putExtra("message", messageBody);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(resultIntent);

        Intent intent = new Intent(context, MyService.class);
        context.stopService(intent);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))

                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());

    }

    public boolean isMyServiceRunning(Class<?> serviceclass){

        ActivityManager manager =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceclass.getName().equals(serviceInfo.service.getClassName())){
                Log.e(TAG, "isMyServiceRunning: "+true );
                return true;
            }
        }
        Log.e(TAG, "isMyServiceRunning: "+false);
        return false;
    }

    private void sendNotificationrring(String messageBody, String tag) {

        Log.e("NOTIFICATION_TRACKER", "notification called 3");

        try {
           /* mServiceIntent = new Intent(context,MyService.class);

            if (isMyServiceRunning(MyService.class)){
                context.stopService(mServiceIntent);
            }*/

/*
            AudioManager manager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
            manager.adjustStreamVolume(AudioManager.STREAM_MUSIC,manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.ADJUST_SAME);
*/

            Intent intent = new Intent(context, MyService.class);
            context.startService(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent resultIntent = null;

        resultIntent = new Intent(this, BaseActivity.class);
        resultIntent.putExtra("Notification", "notification");
        resultIntent.putExtra("message", messageBody);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(resultIntent);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setPriority(10)
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());

    }


    private void sendNotificationrringdecline(String messageBody, String tag) {

        Log.e("NOTIFICATION_TRACKER", "notification called 4");

        try {

            Intent intent = new Intent(context, MyService.class);
            context.stopService(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent resultIntent = null;


        resultIntent = new Intent(context, BaseActivity.class);
        resultIntent.putExtra("Notification", "notification");
        resultIntent.putExtra("message", messageBody);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(resultIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.cancel);

        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
        r.play();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                // .setSound(soundUri)//<--
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());


    }

    private void sendMobileWakeupNotification(String messageBody, String tag) {


        try {
            //Toast.makeText(context, "service called", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MyService.class);
            intent.putExtra("not_decline", true);
            context.stopService(intent);

        } catch (Exception e) {
            //Toast.makeText(context, "service called2hbjhvhvvhvhhvh", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }


        // Intent resultIntent = null;

        Intent resultIntent = null;
        resultIntent = new Intent(this, BaseActivity.class);
        resultIntent.putExtra("screen_tag", tag);
        resultIntent.putExtra("message", messageBody);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(resultIntent);

        /*resultIntent = new Intent(context, BaseActivity.class);
        resultIntent.putExtra("Notification", "notification");
        resultIntent.putExtra("message", messageBody);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(resultIntent);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.cancel);

        /*Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
        r.play();*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setPriority(2)
                // .setSound(soundUri)//<--
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());

    }

    private void sendURLNotificationrring(String messageBody, String url, String tag) {

        Intent resultIntent = null;

        resultIntent = new Intent(this, BaseActivity.class);
        resultIntent.putExtra("Notification", "notification");
        resultIntent.putExtra("message", messageBody);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(resultIntent);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        //Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.accept);

        final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


        mediaPlayer = apiClient.getMediaPlayer(context);
        //   AssetFileDescriptor descriptor = null;
        if (!mediaPlayer.isPlaying()) {
            if (!isPLAYING) {
                Log.e("URL--1", "" + url);
                isPLAYING = true;
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                  //  mediaPlayer.setVolume(1f, 1f);
                    mediaPlayer.start();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
            } else {
                Log.e("URL---2", "" + url);

                isPLAYING = false;
                stopPlaying();
            }
        } else {
            Log.e("URL---3", "" + url);

            isPLAYING = false;
            stopPlaying();
        }

        /*Uri soundUri = Uri.parse(url);

        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);

        if (!r.isPlaying())
            r.play();
        else
            r.stop();
        */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))

                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
        //  manager.notify(0, builder.build());

    }

    private void stopPlaying() {
        mediaPlayer.reset();
        mediaPlayer = null;
    }
}

