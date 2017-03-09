package com.lixin.listen.util;

import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by admin on 2017/1/23.
 */

public class RealPathMediaRecordUtil {

    public static MediaRecorder realRecord;

    public static void startRecordering(String filePath) {
        try {
            if (realRecord != null) {
                realRecord.release();
                realRecord = null;
            }
            realRecord = new MediaRecorder();
            // 设置麦克风为音频源
            realRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频文件的编码
            realRecord.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            // 设置输出文件的格式
            realRecord.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//可以设置成 MediaRecorder.AudioEncoder.AMR_NB

            realRecord.setOutputFile(filePath);

            realRecord.prepare();
            realRecord.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RecordAcitivy", "真实开始录制文件报错" + e.getMessage());
        }
    }

    public static void stopRecordering() {
        if (realRecord != null) {
            try {
                realRecord.setOnErrorListener(null);
                realRecord.setOnInfoListener(null);
                realRecord.setPreviewDisplay(null);
                realRecord.stop();
                realRecord.release();
                realRecord = null;
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("RecordAcitivy", "结束录制文件报错" + e.getMessage());
            }

        }
    }
}
