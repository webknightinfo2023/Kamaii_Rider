package com.kamaii.rider.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class MyService extends Service {

    private static final String TAG = "MyService";
    boolean not_decline = false;
    boolean isPLAYING = false;
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Toast.makeText(this, "MyService Started.", Toast.LENGTH_SHORT).show();
        not_decline = intent.getBooleanExtra("not_decline", false);

        if (not_decline) {

        } else {
            final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor descriptor = null;

            if (!isPLAYING) {
                isPLAYING = true;
                if (!mediaPlayer.isPlaying()) {

                    try {

                        //  descriptor = this.getAssets().openFd("accept.mpeg");
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        descriptor = this.getAssets().openFd("new_order.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();
                        mediaPlayer.prepare();
                      //  mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();


                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                try {
                                    mediaPlayer.start();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }

                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    mediaPlayer.stop();
                }
            } else {
                isPLAYING = false;
                mediaPlayer.stop();
            }
            //If service is killed while starting, it restarts.
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        try {
            mediaPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }

}