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
 * Created by admin on 2017/1/21.
 */

public class Player implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    public MediaPlayer mediaPlayer;
    private SeekBar skbProgress;
    private Timer mTimer = new Timer();
    private Context context;

    public IPlayCompleted playCompleted;

    public ISeekbarProgress seekBarChangeListener;

    public Player(SeekBar skbProgress, Context context, IPlayCompleted playCompleted, ISeekbarProgress seekBarChangeListener) {
        this.skbProgress = skbProgress;
        this.context = context;
        this.playCompleted = playCompleted;
        this.seekBarChangeListener = seekBarChangeListener;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }

        mTimer.schedule(mTimerTask, 0, 1000);
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
    //*****************************************************

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

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
        playCompleted.playCompleted();
        skbProgress.setProgress(0);
//        stop();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
//        skbProgress.setSecondaryProgress(bufferingProgress);
//        int currentProgress=skbProgress.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
//        Log.e(currentProgress+"% play", bufferingProgress + "% buffer");
    }
}
