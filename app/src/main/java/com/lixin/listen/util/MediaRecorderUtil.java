package com.lixin.listen.util;

import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by chenjiahuan on 16/5/8.
 */
public class MediaRecorderUtil {

    public static MediaRecorder recorder;

    public static void startRecordering(String filePath) {
        try {
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            recorder = new MediaRecorder();
            // 设置麦克风为音频源
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频文件的编码
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            // 设置输出文件的格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//可以设置成 MediaRecorder.AudioEncoder.AMR_NB

            recorder.setOutputFile(filePath);

            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RecordAcitivy", "开始录制文件报错" + e.getMessage());
        }
    }

    public static void stopRecordering() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
