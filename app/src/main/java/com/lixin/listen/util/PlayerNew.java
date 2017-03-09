package com.lixin.listen.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

import com.lixin.listen.bean.IPlayCompleted;
import com.lixin.listen.bean.ISeekbarProgress;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2017/2/23.
 */

public class PlayerNew {

    private IPlayCompleted playCompleted;

    private ISeekbarProgress seekBarChangeListener;

    private Context context;

    private SeekBar skbProgress;

    private MediaPlayer mediaPlayer;

    private Timer mTimer ;

    private PlayerNew() {

    }

    private static final PlayerNew player = new PlayerNew();

    public static PlayerNew getInstance() {

        try {
            if (player.mediaPlayer == null) {
                player.mediaPlayer = new MediaPlayer();
                player.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Log.e("mediaPlayer", "onCompletion");
                        player.playCompleted.playCompleted();
                        player.skbProgress.setProgress(0);
                    }
                });
//            player.mediaPlayer.setOnBufferingUpdateListener(this);
                player.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        Log.e("mediaPlayer", "onPrepared");
                    }
                });
            }

        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }

        if (player.mTimer == null){
            player.mTimer = new Timer();
            player.mTimer.schedule(player.mTimerTask, 0, 1000);
        }


        return player;
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (duration > 0) {
                long pos = skbProgress.getMax() * position / duration;
                skbProgress.setProgress((int) pos);
                seekBarChangeListener.seekbarProgress((int) position);
            }
        }

        ;
    };

    public void play() {
        mediaPlayer.start();
    }

    public void playUrl(String videoUrl) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
//            mediaPlayer.prepare(context, Uri.parse(videoUrl));//prepare之后自动播放
            mediaPlayer.prepareAsync();
//            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setPlayCompleted(IPlayCompleted playCompleted) {
        this.playCompleted = playCompleted;
    }


    public void setSeekBarChangeListener(ISeekbarProgress seekBarChangeListener) {
        this.seekBarChangeListener = seekBarChangeListener;
    }

    public void setSkbProgress(SeekBar skbProgress) {
        this.skbProgress = skbProgress;
    }

}
