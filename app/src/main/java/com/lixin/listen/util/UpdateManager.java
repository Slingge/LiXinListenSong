package com.lixin.listen.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.bean.UpdateVO;
import com.lixin.listen.common.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;

/**
 * Created by admin on 2017/2/8.
 */

public class UpdateManager {

    private ProgressBar mProgressBar;//进度条
    private TextView tv1;
    private TextView tv2;
    private Dialog mDownloadDialog;//对话框

    private String mSavePath;//apk保存地址
    private int mProgress;//进度值

    private boolean mIsCancel = false;//是否取消下载标示符

    private static final int DOWNLOADING = 1;//apk下载中
    private static final int DOWNLOAD_FINISH = 2;//apk下载完毕
    private float length;

    private static final String PATH = "http://www.jcpeixun.com/app_client_api/app_version.aspx";//更新地址

    private String mVersion_code;//软件升级标示号
    private String mVersion_name = "xiaozhiqu.apk";//apk名
    private String mVersion_desc;//更新详情
    private String mVersion_path;//apk下载地址

    private Context mContext;//上下文

    public UpdateManager(Context context) {
        mContext = context;
    }

    private Handler mGetVersionHandler = new Handler(){
        public void handleMessage(Message msg) {
            UpdateVO jsonObject = (UpdateVO) msg.obj;
            System.out.println(jsonObject.toString());
            Log.e("----------",jsonObject.toString());
            try {
                mVersion_code = jsonObject.getVersionNumber();
                //mVersion_name = jsonObject.getString("version_name");
                //mVersion_desc = jsonObject.getString("version_desc");
                mVersion_path = jsonObject.getUpdataAddress();

                Log.e("version ",mVersion_code);
                Log.e("downurl ",mVersion_path);

                if (isUpdate()){
                    //Toast.makeText(mContext, "需要更新", Toast.LENGTH_SHORT).show();
                    // 显示提示更新对话框
                    showNoticeDialog();
                } else{
                    Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e){
                e.printStackTrace();
            }
        };
    };

    private Handler mUpdateProgressHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DOWNLOADING:
                    // 设置进度条
                    mProgressBar.setProgress(mProgress);
                    tv1.setText( mProgress + "/100");
                    float len = length/1024/1024;//将length转换为M单位
                    float b = (float)(Math.round(len*100))/100;//保留两位小数点
                    tv2.setText( b+"M");
                    break;
                case DOWNLOAD_FINISH:
                    // 隐藏当前下载对话框
                    mDownloadDialog.dismiss();
                    // 安装 APK 文件
                    installAPK();
                    break;
                default:
                    break;
            }
        };
    };

    /*
     * 检测软件是否需要更新
     */
    public void checkUpdate() {


        String url = Constant.URL + "json=" + new Gson().toJson(new RequestVO("getVersion"));
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        UpdateVO vo = new Gson().fromJson(response, UpdateVO.class);
                        Toast.makeText(mContext, vo.getResultNote(), Toast.LENGTH_SHORT).show();

                        try {
                            if (Integer.valueOf(vo.getVersionNumber()) > AppHelper.getVersionCode(mContext)) {
                                Message msg = Message.obtain();
                                msg.obj = vo;
                                mGetVersionHandler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    /*
     * 与本地版本比较判断是否需要更新
     */
    protected boolean isUpdate() {
        //int serverVersion = Integer.parseInt(mVersion_code);
        double serverVersion = Double.parseDouble(mVersion_code);
        double localVersion = 1;

        try {
            //获得当前apk版本号versionCode
            localVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (serverVersion > localVersion)
            return true;
        else
            return false;
    }

    /*
     * 有更新时显示提示对话框
     */
    protected void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        String message = "软件有更新，要下载安装吗？\n";
        builder.setMessage(message);

        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });

        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /*
     * 显示正在下载对话框
     */
    protected void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("下载中");
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null);
        mProgressBar = (ProgressBar) view.findViewById(R.id.id_progress);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        builder.setView(view);

        // 设置按钮(左)
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
                // 设置下载状态为取消
                mIsCancel = true;
            }
        });
        // 设置按钮(右)
        builder.setNeutralButton("隐藏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mDownloadDialog = builder.create();
        mDownloadDialog.show();

        // 下载文件
        downloadAPK();
    }

    /*
     * 开启新线程下载文件
     */
    private void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{//检查sd是否挂载
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        String sdPath = AppHelper.getInnerFilePath(mContext) + "/";
                        mSavePath = sdPath + "MyDownload";

                        File dir = new File(mSavePath);
                        if (!dir.exists())
                            dir.mkdir();
                        // 下载文件
                        HttpURLConnection conn = (HttpURLConnection) new URL(mVersion_path).openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        length = conn.getContentLength();
                        File apkFile = new File(mSavePath, mVersion_name);
                        FileOutputStream fos = new FileOutputStream(apkFile);

                        int count = 0;
                        byte[] buffer = new byte[1024];
                        while (!mIsCancel){
                            int numread = is.read(buffer);
                            count += numread;
                            // 计算进度条的当前位置
                            mProgress = (int) ((count/length) * 100);
                            // 更新进度条
                            mUpdateProgressHandler.sendEmptyMessage(DOWNLOADING);

                            // 下载完成
                            if (numread < 0){
                                mUpdateProgressHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                                break;
                            }
                            fos.write(buffer, 0, numread);
                        }
                        fos.close();
                        is.close();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
     * 下载到本地后执行安装
     */
    protected void installAPK() {
        File apkFile = new File(mSavePath, mVersion_name);
        if (!apkFile.exists())
            return;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + apkFile.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(intent);

    }

}
