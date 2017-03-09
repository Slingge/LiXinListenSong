package com.lixin.listen.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;

import com.lixin.listen.bean.IPlayCompleted;
import com.lixin.listen.bean.ISeekbarProgress;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LiPeng on 16/5/8.
 */
public class MediaPlayerUtil implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private SeekBar skbProgress;

    public IPlayCompleted playCompleted;

    public ISeekbarProgress seekBarChangeListener;

    private Timer mTimer = new Timer();

    private MediaPlayerUtil() {

    }

    private static final MediaPlayerUtil mediaPlayerUtil = new MediaPlayerUtil();

    public static MediaPlayerUtil getInStance() {
        return mediaPlayerUtil;
    }

    private MediaPlayer mediaPlayer = null;

    public void init() {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnBufferingUpdateListener(this);
                mediaPlayer.setOnPreparedListener(this);
                mTimer.schedule(mTimerTask, 0, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void play(String filePath, SeekBar skbProgress, IPlayCompleted playCompleted, ISeekbarProgress seekBarChangeListener) {
        try {
            this.skbProgress = skbProgress;
            this.playCompleted = playCompleted;
            this.seekBarChangeListener = seekBarChangeListener;

            mediaPlayer.reset();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepareAsync();//缓冲
//            mediaPlayer.start();//开始或恢复播放
        } catch (Exception e) {
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

    /*****************************************************
     * 结束
     ******************************************************/

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        // 缓冲进度，不处理
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (playCompleted != null) {
            playCompleted.playCompleted();
        }
        if (skbProgress != null) {
            skbProgress.setProgress(0);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
