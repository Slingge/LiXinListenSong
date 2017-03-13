package com.lixin.listen.activity;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.czp.library.ArcProgress;
import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.bean.RecordSuccessVO;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.bean.StartRecordVo;
import com.lixin.listen.common.Constant;
import com.lixin.listen.event.RefreshEvent;
import com.lixin.listen.util.AppHelper;
import com.lixin.listen.util.BusProvider;
import com.lixin.listen.util.DateUtil;
import com.lixin.listen.util.DirTraversal;
import com.lixin.listen.util.MediaRecorderUtil;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.util.ProgressDialog;
import com.lixin.listen.util.RealPathMediaRecordUtil;
import com.lixin.listen.util.ToastUtil;
import com.lixin.listen.view.ClickImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class RecordActivity extends AppCompatActivity implements ClickImageView.ClickImageViewCallBack {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.activity_record)
    RelativeLayout activityRecord;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.myProgress01)
    ArcProgress myProgress01;
    @Bind(R.id.rl_record)
    RelativeLayout rlRecord;

    @Bind(R.id.tv_fenbei)
    TextView tvFenbei;
    @Bind(R.id.tv_shijian)
    TextView tvShijian;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.iv_low)
    ImageView ivLow;
    @Bind(R.id.iv_mid)
    ImageView ivMid;
    @Bind(R.id.iv_high)
    ImageView ivHigh;

    ClickImageView btnStop;

    private Timer mTimer;
    private Timer mTimer1;

    private StartRecordVo startRecordVo;

    private File RecordFile;//录音文件

    private boolean isSaveFile = true;

    //    File extDir = Environment.getExternalStorageDirectory();
    //    File extDir = AppHelper.getInnerFilePath(this);
    private long originalTime = -1l;
    // 临时文件地址
    String filePath = "/sdcard/lixin/";
    // 真实文件地址
    String realFilePath = "";
    // 目标分贝，低于此分贝停止录音并生成文件
    private double targetFenbei = 45.0;
    // 当声音低于目标分贝且超过3秒的时候，停止录音
    private int noVocieTime = 0;
    // 当天录制文件的总时间长度
    private int AllFileTiem = 0;

    private int l = 0;//录音时间
    private int startTime;//开始录音的时间
    private int startFileTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        getParams();
        initViews();
        btnStop = (ClickImageView) findViewById(R.id.btn_stop);
        btnStop.setClickImageViewCallBack(this);
        // 开始录音-记录临时文件
        tempMediarecorder();

    }

    public void tempMediarecorder() {
        File file = new File(AppHelper.getInnerFilePath(this), RecordActivity.this.getPackageName() + File.separator + "temp");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                file.mkdirs();
            }
        }

        filePath = new File(file, "weiguilei" + "_" + DateUtil.toDayFormat(System.currentTimeMillis()) + "_" + System.currentTimeMillis()).getAbsolutePath() + ".amr";

        startTimer();
        startTimer1();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 记录当天录制总时间
                        tvTime.setText(getTime());
                        // 设置当前进度条
                        Double cur1 = Double.valueOf(sencondTem);
                        Double curTotal = Double.valueOf(String.valueOf(currentTotalTime(startRecordVo)));
                        int percent = Integer.valueOf(String.valueOf(cur1 / curTotal * 100).substring(0, String.valueOf(cur1 / curTotal * 100).indexOf(".")));
                        myProgress01.setProgress(percent);
                    }
                });
            }
        }, 0, 1);

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        l++;
                        try {
                            double ratio = MediaRecorderUtil.recorder.getMaxAmplitude();
                            if (ratio > 1)
                                ratio = 20 * Math.log10(ratio);
                            tvFenbei.setText("当前音量：" + ratio);
                            doSetAnim(ratio);
                            // 大于目标分贝开始录音
                            if (ratio > targetFenbei) {

                                if (RealPathMediaRecordUtil.realRecord == null) {

                                    MediaRecorderUtil.stopRecordering();
                                    doRealMediaRecord();
                                    Log.e("RecordAcitivy", "开始录制真实文件");

                                }
                                noVocieTime = 0;
                                startTime++;
                                AllFileTiem++;

                            } else {
                                noVocieTime++;
                                Log.e("RecordAcitivy", "noVocieTime" + noVocieTime);

                            }
                            // 三秒内没有大于目标分贝则停止
                            if (noVocieTime > 3) {
                                if (RealPathMediaRecordUtil.realRecord != null) {
                                    RealPathMediaRecordUtil.stopRecordering();
                                    noVocieTime = 0;
//                                    AllFileTiem += 3;
//                                    MediaRecorderUtil.startRecordering(filePath);
                                    Log.e("RecordAcitivy", "开始录制临时文件");

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }, 0, 1000);

        mTimer1 = new Timer();
        mTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            double ratio = RealPathMediaRecordUtil.realRecord.getMaxAmplitude();
                            if (ratio > 1)
                                ratio = 20 * Math.log10(ratio);
//                            tvFenbei.setText("当前音量：" + ratio);
//                            doSetAnim(ratio);
                            // 大于目标分贝开始录音
                            if (ratio > targetFenbei) {

                                if (RealPathMediaRecordUtil.realRecord == null) {
                                    doRealMediaRecord();
                                    MediaRecorderUtil.stopRecordering();
//                                    Log.e("RecordAcitivy", "录制真实文件");
                                }
                                noVocieTime = 0;
                                startTime++;
                                AllFileTiem++;

                            } else {
                                noVocieTime++;
                                Log.e("RecordAcitivy", "noVocieTime" + noVocieTime);

                            }
                            // 三秒内没有大于目标分贝则停止
                            if (noVocieTime > 3) {
                                if (RealPathMediaRecordUtil.realRecord != null) {
                                    RealPathMediaRecordUtil.stopRecordering();
                                    noVocieTime = 0;
//                                    AllFileTiem += 3;
                                    MediaRecorderUtil.startRecordering(filePath);
                                    Log.e("RecordAcitivy", "开始录制临时文件");
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }, 0, 1000);

        MediaRecorderUtil.startRecordering(filePath);

    }

    private void doSetAnim(double ratio) {
        if (ratio < 50) {
            ivLow.setVisibility(View.VISIBLE);
            ivMid.setVisibility(View.GONE);
            ivHigh.setVisibility(View.GONE);
        } else if (ratio < 70 && ratio >= 50) {
            ivLow.setVisibility(View.GONE);
            ivMid.setVisibility(View.VISIBLE);
            ivHigh.setVisibility(View.GONE);
        } else if (ratio > 70) {
            ivLow.setVisibility(View.GONE);
            ivMid.setVisibility(View.GONE);
            ivHigh.setVisibility(View.VISIBLE);
        }
    }

    public void doRealMediaRecord() {
        File file1 = new File(AppHelper.getInnerFilePath(this), RecordActivity.this.getPackageName());
        if (!file1.exists()) {
            if (!file1.mkdirs()) {
                file1.mkdirs();
            }
        }
        realFilePath = new File(file1, "weiguilei" + "_" + DateUtil.toDayFormat(System.currentTimeMillis()) + "_" + System.currentTimeMillis()).getAbsolutePath() + ".amr";
        RealPathMediaRecordUtil.startRecordering(realFilePath);
    }

    // 旋转的进度条
    private String getTime1() {
        long time = System.currentTimeMillis() - originalTime;
        long second = time / 1000l;
        return second + "";
    }

    // 当日总时间:秒
    private long currentTotalTime(StartRecordVo vo) {
        long result = 0;
        result = Long.valueOf(vo.getDayCount()) * 60;
        return result;
    }


    long sencondTem = 0;

    // 时间：分秒
    private String getTime() {
//        long time = System.currentTimeMillis() -  currentSecond(vo);
        sencondTem = currentSecond(startRecordVo);
        sencondTem += Long.valueOf(getTime1());
//        long second = time / 1000l;
        return ShowTime((int) sencondTem);
    }

    private long currentSecond(StartRecordVo vo) {
        long result = 0;
        String timeLenth = vo.getTimeLength();
        if (timeLenth.contains("分")) {
            result = Long.valueOf(timeLenth.substring(0, timeLenth.indexOf("分"))) * 60 +
                    Long.valueOf(timeLenth.substring(timeLenth.indexOf("分") + 1, timeLenth.indexOf("秒")));
        } else {
            result = 0;
        }
        return result;
    }

    public String ShowTime(int time) {
//        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    private void startTimer() {
        originalTime = System.currentTimeMillis();
        if (!new File(filePath).exists()) {
            try {
                new File(filePath).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void startTimer1() {
        originalTime = System.currentTimeMillis();
        if (!new File(realFilePath).exists()) {
            try {
                new File(realFilePath).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void getParams() {
        startRecordVo = (StartRecordVo) getIntent().getSerializableExtra("vo");
        if (startRecordVo.getFileTimeLength().contains("分")) {
            String fileOLength = startRecordVo.getFileTimeLength();
            String fileOLengthFen = fileOLength.substring(0, fileOLength.indexOf("分"));
            String fileOLengthMiao = fileOLength.substring(fileOLength.indexOf("分") + 1, fileOLength.indexOf("秒"));
            AllFileTiem = Integer.valueOf(fileOLengthFen) * 60 + Integer.valueOf(fileOLengthMiao);
        } else {
            AllFileTiem = 0;
        }
        startFileTime = AllFileTiem;
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                String message = "确定要退出录音吗？\n";
                builder.setMessage(message);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐藏当前对话框
                        dialog.dismiss();
                        finish();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐藏当前对话框
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        tvTitle.setText("录制中");

        AnimationDrawable ad = (AnimationDrawable) getResources().getDrawable(
                R.drawable.img_fenbei_low);
        ivLow.setBackgroundDrawable(ad);
        ad.start();

        AnimationDrawable ad1 = (AnimationDrawable) getResources().getDrawable(
                R.drawable.img_fenbei_mid);
        ivMid.setBackgroundDrawable(ad1);
        ad1.start();

        AnimationDrawable ad2 = (AnimationDrawable) getResources().getDrawable(
                R.drawable.img_fenbei_high);
        ivHigh.setBackgroundDrawable(ad2);
        ad2.start();

        Typeface tf = Typeface.createFromAsset(getAssets(), "MyriadPro-BoldCond.otf");
        tvTime.setTypeface(tf);
    }

    private void doFinishRecord() {
        ProgressDialog.showProgressDialog(this, "正在保存文件...");

        mTimer.cancel();
        if (l < 6) {//录制时间小于6s，不保存
            ToastUtil.showToast("录音时间不足6秒，已被舍弃");
            ProgressDialog.dismissDialog();
            finish();
            return;
        }
        RequestVO vo = new RequestVO();
        vo.setCmd("endRecording");
        vo.setUid(PrefsUtil.getString(RecordActivity.this, "userid", ""));

        int time;
        if (l > startTime) {
            time = Integer.valueOf(l + startFileTime);
        } else {
            time = Integer.valueOf(startTime + startFileTime + 2);
        }

        String time1 = time / 60 + "分" + time % 60 + "秒";
        // 录制时长
        // 文件时长-
        vo.setRecTimeLength(time1);
        String fileTime = AllFileTiem / 60 + "分" + AllFileTiem % 60 + "秒";

        vo.setFileTimeLength(getTodayFileTime());

        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ToastUtil.showToast("网络错误，保存失败");
                        ProgressDialog.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        RecordSuccessVO vo = new Gson().fromJson(response, RecordSuccessVO.class);
                        if (vo.getResult().equals("0")) {
                            ProgressDialog.dismissDialog();
                            BusProvider.getInstance().post(new RefreshEvent());
                            finish();
                        } else {
                            Toast.makeText(RecordActivity.this, vo.getResultNote(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 获取文件录制总时间
     *
     * @return
     */
    private String getTodayFileTime() {
        String result = "";

        SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        String todayTime = sf.format(c.getTime());

        Log.e("RecordAcitivy", "当日时间:" + todayTime);

        List<File> tempFileList = new ArrayList<>();

        List<File> files = DirTraversal.listFiles(AppHelper.getInnerFilePath(RecordActivity.this) + File.separator
                + getPackageName());

        Log.e("RecordAcitivy", "文件数量:" + files.size());
        if (files == null)
            return "";
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            if (file.getName().contains(todayTime)) {
                tempFileList.add(file);
            }
        }

        int fileTimeAll = 0;
        for (int i = 0; i < tempFileList.size(); i++) {
            File file = tempFileList.get(i);
            fileTimeAll += getFileTime(file.getAbsolutePath());
        }

        Log.e("RecordAcitivy", "文件总时间:" + fileTimeAll);
        Log.e("RecordAcitivy", "文件总时间格式化:" + getTimeFormat(fileTimeAll / 1000));


        return getTimeFormat(fileTimeAll / 1000);
    }

    /**
     * 获取文件录制时间
     */
    public int getFileTime(String filePath) {
        int fileTime = 0;

        try {
            final MediaPlayer mediaPlayer1 = new MediaPlayer();
            mediaPlayer1.setDataSource(filePath);
            mediaPlayer1.prepare();
            fileTime = mediaPlayer1.getDuration();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileTime;
    }

    /**
     * 分秒时间，格式：10分5秒
     */
    public String getTimeFormat(int second) {
        String result = "";
        result = (String.valueOf(second / 60).length() == 1 ? second / 60 : second / 60) + "分" +
                (String.valueOf(second % 60).length() == 1 ? second % 60 : second % 60) + "秒";
        return result;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
            String message = "确定要退出录音吗？\n";
            builder.setMessage(message);

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 隐藏当前对话框
                    dialog.dismiss();
                    finish();
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 隐藏当前对话框
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void clickImage() {
        if (RealPathMediaRecordUtil.realRecord != null) {
            RealPathMediaRecordUtil.stopRecordering();
        }
        MediaRecorderUtil.stopRecordering();
        mTimer.cancel();

        // 获取录制目录下的所有文件
        final List<File> files = DirTraversal.listFiles(AppHelper.getInnerFilePath(RecordActivity.this).getAbsolutePath() + File.separator
                + getPackageName());
        for (int i = 0; i < files.size(); i++) {

            final File file = files.get(i);
            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setDataSource(files.get(i).getAbsolutePath());
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int size = mp.getDuration();
                    int timelong = size / 1000;
                    if (timelong < 6) {
                        file.delete();
                    }
                }
            });
        }
        doFinishRecord();
    }
}
