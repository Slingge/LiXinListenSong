package com.lixin.listen.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by admin on 2017/1/21.
 */

public class PlayAudioManager {

    private static MediaPlayer mediaPlayer;

    public static void playAudio(final Context context, final String url) throws Exception {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                killMediaPlayer();
            }
        });
        mediaPlayer.start();
    }

    private static void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
