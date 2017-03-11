package com.lixin.listen.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.activity.ChooseZhuanjiActivity;
import com.lixin.listen.activity.GuileiActivity;
import com.lixin.listen.activity.GuileiLocalFileActivity;
import com.lixin.listen.bean.CommonVO;
import com.lixin.listen.bean.CurrentDayTimeVo;
import com.lixin.listen.bean.IPlayCompleted;
import com.lixin.listen.bean.ISeekbarProgress;
import com.lixin.listen.bean.MusicBean;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.bean.ThirdTypeVO;
import com.lixin.listen.common.Constant;
import com.lixin.listen.event.StopPlayingEvent;
import com.lixin.listen.util.AppHelper;
import com.lixin.listen.util.BusProvider;
import com.lixin.listen.util.CustomDialog;
import com.lixin.listen.util.DateUtil;
import com.lixin.listen.util.DirTraversal;
import com.lixin.listen.util.MediaPlayerUtil;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.util.ProgressDialog;
import com.lixin.listen.util.ToastUtil;
import com.lixin.listen.util.abLog;
import com.lixin.listen.widget.CommonAdapter;
import com.lixin.listen.widget.CommonListView;
import com.lixin.listen.widget.ViewHolder;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.R.string.ok;
import static android.app.Activity.RESULT_OK;

/**
 * 本地文件
 * A simple {@link Fragment} subclass.
 */
public class LocalFileFragment extends Fragment {


    @Bind(R.id.iv_riqi)
    ImageView ivRiqi;
    @Bind(R.id.tv_riqi)
    TextView tvRiqi;
    @Bind(R.id.ll_riqi)
    LinearLayout llRiqi;
    @Bind(R.id.iv_zhuanji)
    ImageView ivZhuanji;
    @Bind(R.id.tv_zhuanji)
    TextView tvZhuanji;
    @Bind(R.id.ll_zhuanji)
    LinearLayout llZhuanji;
    @Bind(R.id.iv_weiguilei)
    ImageView ivWeiguilei;
    @Bind(R.id.tv_weiguilei)
    TextView tvWeiguilei;
    @Bind(R.id.ll_weiguilei)
    LinearLayout llWeiguilei;
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.lv)
    CommonListView lv;
    @Bind(R.id.ll_date)
    LinearLayout llDate;

    long tempData = 0;
    int addNum = 0;
    int reduceNum = 0;
    @Bind(R.id.tv_choose_zhuanji)
    TextView tvChooseZhuanji;
    @Bind(R.id.tv_dangrishijian)
    TextView tvDangrishijian;
    @Bind(R.id.tv_dangriwenjian)
    TextView tvDangriwenjian;
    @Bind(R.id.ll_times)
    LinearLayout llTimes;
    @Bind(R.id.rl_kuang)
    RelativeLayout rlKuang;
    @Bind(R.id.rl_time)
    RelativeLayout rlTime;

    private CommonAdapter<MusicBean> commonAdapter;
    private List<MusicBean> musicBeanList = new ArrayList<>();
    private List<MusicBean> tempMusicBeanList = new ArrayList<>();

    public MediaPlayer mediaPlayer = new MediaPlayer();

    private List<MusicBean> temList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private int flag = -1;//0按日期，1按专辑，2未归类
    MusicBean updatemusicBean;

    CustomDialog dialog;

    ViewHolder lastHolder;
    int lastPos = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_file, container, false);
        ButterKnife.bind(this, view);
        BusProvider.getInstance().register(this);
        getParams();
        initViews();
        initRefresh(view);
        return view;
    }


    private void initRefresh(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.base_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (flag == 0) {
                            riqi();
                        } else if (flag == 1) {
                            zhuanji();
                        } else if (flag == 2) {
                            weiguilei();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    public void setClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                weiguilei();
            }
        }, 500);
    }

    private void getParams() {
        musicBeanList.clear();
        // 获取文件夹下的文件
        List<File> files = DirTraversal.listFiles(AppHelper.getInnerFilePath(getActivity()) + File.separator
                + getActivity().getPackageName());
        if (files == null)
            return;
        for (int i = 0; i < files.size(); i++) {
            MusicBean bean = new MusicBean();
            bean.setMusicName(files.get(i).getName());
            bean.setFile(files.get(i));
            bean.setFilePath(files.get(i).getAbsolutePath());
            musicBeanList.add(bean);
        }
        Collections.reverse(musicBeanList);
        tempMusicBeanList.addAll(musicBeanList);


    }

    private void initViews() {

        dialog = new CustomDialog(getActivity(), R.style.CustomDialog);

        final Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "MyriadPro-BoldCond.otf");
        tvTime.setTypeface(tf);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getActivity(), "播放完成", Toast.LENGTH_SHORT).show();
            }
        });

        commonAdapter = new CommonAdapter<MusicBean>(getActivity(), musicBeanList, R.layout.item_music) {

            @Override
            public void convert(final ViewHolder holder, final MusicBean musicBean) {

                holder.setText(R.id.tv_all_time, ShowTime(getFileTime(musicBean.getFilePath())));

                if (musicBean.isUpload()) {
                    ((ImageView) holder.getView(R.id.iv_upload)).setVisibility(View.INVISIBLE);
                    holder.setVisible(R.id.iv_uploaded, true);
                } else {
                    holder.setVisible(R.id.iv_upload, true);
                    holder.setVisible(R.id.iv_uploaded, false);
                }

                if (PrefsUtil.getString(getActivity(), "@" + musicBean.getFilePath().substring(musicBean.getFilePath().indexOf("_")), "").equals("uploaded")) {
                    ((ImageView) holder.getView(R.id.iv_upload)).setVisibility(View.INVISIBLE);
                    holder.setVisible(R.id.iv_uploaded, true);
                } else {
                    holder.setVisible(R.id.iv_upload, true);
                    holder.setVisible(R.id.iv_uploaded, false);
                }

                if (musicBean.getFilePath().contains("weiguilei")) {
                    holder.setVisible(R.id.iv_weilei, true);
                    holder.setVisible(R.id.iv_chongxingeilei, false);
                    holder.setVisible(R.id.tv_zhuanji_icon, false);
                } else {
                    holder.setVisible(R.id.iv_weilei, false);
                    holder.setVisible(R.id.iv_chongxingeilei, true);
                    holder.setVisible(R.id.tv_zhuanji_icon, true);
                    holder.setText(R.id.tv_zhuanji_icon, musicBean.getMusicName().substring(1, musicBean.getMusicName().indexOf("#")));
                }

                // 归类到专辑
                holder.setOnClickListener(R.id.iv_weilei, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), GuileiLocalFileActivity.class);
                        intent.putExtra("vo", musicBean);
                        startActivityForResult(intent, 1000);
                    }
                });

                // 重新归类
                holder.setOnClickListener(R.id.iv_chongxingeilei, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), GuileiLocalFileActivity.class);
                        intent.putExtra("vo", musicBean);
                        intent.putExtra("musicId", PrefsUtil.getString(getActivity(), musicBean.getMusicName(), ""));
                        startActivityForResult(intent, 3000);
                        updatemusicBean = musicBean;
                    }
                });

                // 删除
                holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showDeleteDialog(musicBean, holder);

                    }
                });

                // 上传
                holder.setOnClickListener(R.id.iv_upload, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (musicBean.getFilePath().contains("weiguilei")) {
                            Intent intent = new Intent(getActivity(), GuileiActivity.class);
                            intent.putExtra("vo", musicBean);
                            startActivityForResult(intent, 2001);
                        } else {
                            dialog.show();
                            doUploadFile(musicBean);
                        }
                    }
                });

                if (musicBean.isPlaying()) {
                    holder.setVisible(R.id.iv_pause, true);
                    holder.setVisible(R.id.iv_play, false);
                } else {
                    holder.setVisible(R.id.iv_pause, false);
                    holder.setVisible(R.id.iv_play, true);
                }

                // 播放
                holder.setOnClickListener(R.id.iv_play, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 把上次播放的状态清空
                        for (int i = 0; i < commonAdapter.getDataList().size(); i++) {
                            if (holder.getPosition() == i) {
                                commonAdapter.getDataList().get(i).setPlaying(true);
                            } else {
                                commonAdapter.getDataList().get(i).setPlaying(false);
                            }

                        }
                        commonAdapter.notifyDataSetChanged();
                        holder.setVisible(R.id.iv_pause, true);
                        holder.setVisible(R.id.iv_play, false);
                        MediaPlayerUtil.getInStance().play(musicBean.getFilePath(), ((SeekBar) holder.getView(R.id.sb)), new IPlayCompleted() {
                            @Override
                            public void playCompleted() {
                                holder.setVisible(R.id.iv_pause, false);
                                holder.setVisible(R.id.iv_play, true);
                                ((TextView) holder.getView(R.id.tv_time)).setText("00:00");
                            }
                        }, new ISeekbarProgress() {
                            @Override
                            public void seekbarProgress(int pos) {
                                ((TextView) holder.getView(R.id.tv_time)).setText(ShowTime(pos));
                            }
                        });
                    }
                });

                // 暂停
                holder.setOnClickListener(R.id.iv_pause, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.setVisible(R.id.iv_pause, false);
                        holder.setVisible(R.id.iv_play, true);
                        MediaPlayerUtil.getInStance().pause();
                    }
                });

                // 未归类
                if (musicBean.getFilePath().contains("weiguilei")) {
                    holder.setText(R.id.tv_name, "未归类");
                }
                // 已归类
                else {
                    try {
                        String filePath = musicBean.getFilePath();
                        String zhuanjiName = filePath.substring(filePath.indexOf("$") + 1, filePath.lastIndexOf("#"));
                        String geming = filePath.substring(filePath.indexOf("#") + 1, filePath.lastIndexOf("%"));
                        holder.setText(R.id.tv_name, geming);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        lv.setAdapter(commonAdapter);

        llRiqi.performClick();
        tempData = System.currentTimeMillis();
        tvTime.setText(DateUtil.toDayFormat(tempData));
        doRequestCurrentTime(DateUtil.toDateStrSimple(tempData));
        temList.clear();
        for (MusicBean bean : musicBeanList) {
            if (bean.getMusicName().contains(DateUtil.toDayFormat(tempData))) {
                temList.add(bean);
            }
        }
        musicBeanList.clear();
        musicBeanList.addAll(temList);
        commonAdapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llRiqi.performClick();
            }
        }, 500);
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
        result = (String.valueOf(second / 60).length() == 1 ? "0" + second / 60 : second / 60) + ":" +
                (String.valueOf(second % 60).length() == 1 ? "0" + second % 60 : second % 60);
        return result;
    }


    @Override
    public void onPause() {
        super.onPause();
        resetStatus();
        MediaPlayerUtil.getInStance().pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetStatus();
    }

    @Subscribe
    public void onReceiveStopPlaying(StopPlayingEvent event) {
        resetStatus();
    }

    private void resetStatus() {
        for (MusicBean bean : commonAdapter.getDataList()) {
            bean.setPlaying(false);
        }
        commonAdapter.notifyDataSetChanged();
    }

    /**
     * 上传文件
     *
     * @param musicBean
     */
    private void doUploadFile(final MusicBean musicBean) {

        RequestVO vo = new RequestVO();
        vo.setCmd("uploadMusic");
        vo.setUid(PrefsUtil.getString(getActivity(), "userid", ""));
        String id = musicBean.getFilePath().substring(musicBean.getFilePath().indexOf("_") + 1, musicBean.getFilePath().indexOf("_") + 14);
        vo.setFirstTypeId(PrefsUtil.getString(getActivity(), id, "").split(",")[0]);
        vo.setSecondTypeId(PrefsUtil.getString(getActivity(), id, "").split(",")[1]);
        vo.setThirdTypeId(PrefsUtil.getString(getActivity(), id, "").split(",")[2]);
        vo.setAlbumLength(getTimeFormat(getFileTime(musicBean.getFilePath()) / 1000));
//        vo.setFile();
        vo.setAlbumName(PrefsUtil.getString(getActivity(), id, "").split(",")[3]);
        vo.setLocalId(musicBean.getFilePath());

        String url = Constant.URL + "json=" + new Gson().toJson(vo);

        Map<String, String> params = new HashMap<>();
        params.put("cmd", "uploadMusic");
        params.put("uid", PrefsUtil.getString(getActivity(), "userid", ""));
        params.put("firstTypeId", PrefsUtil.getString(getActivity(), id, "").split(",")[0]);
        params.put("secondTypeId", PrefsUtil.getString(getActivity(), id, "").split(",")[1]);
        params.put("thirdTypeId", PrefsUtil.getString(getActivity(), id, "").split(",")[2]);
        params.put("albumLength", getTimeFormat(getFileTime(musicBean.getFilePath()) / 1000));
        params.put("albumName", PrefsUtil.getString(getActivity(), id, "").split(",")[3]);
        params.put("localId", musicBean.getFilePath());

        OkHttpUtils.post()//
                .addFile("file", createRandom(false, 10) + ".amr", new File(musicBean.getFilePath()))//
                .url(url)
                .params(params)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommonVO vo = new Gson().fromJson(response, CommonVO.class);
                        Toast.makeText(getActivity(), vo.getResultNote(), Toast.LENGTH_SHORT).show();
                        PrefsUtil.putString(getActivity(), "@" + musicBean.getFilePath().substring(musicBean.getFilePath().indexOf("_"))
                                , "uploaded");
                        musicBean.setUpload(true);
                        PrefsUtil.putString(getActivity(), musicBean.getMusicName(), vo.getMid() + "");
                        commonAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
    }

    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
//            Logdada("文件不存在！"+"\n");
        }
    }

    public String ShowTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    @OnClick(R.id.ll_riqi)
    public void riqi() {
        flag = 0;
        rlTime.setVisibility(View.VISIBLE);
        ivRiqi.setBackgroundResource(R.mipmap.img_light_bright);
        tvRiqi.setTextColor(getResources().getColor(R.color.base_color));
        ivWeiguilei.setBackgroundResource(R.mipmap.img_light_grey);
        tvWeiguilei.setTextColor(getResources().getColor(R.color.col3));
        ivZhuanji.setBackgroundResource(R.mipmap.img_light_grey);
        tvZhuanji.setTextColor(getResources().getColor(R.color.col3));
        getParams();
        temList.clear();
        for (MusicBean bean : musicBeanList) {
            if (bean.getMusicName().contains(tvTime.getText().toString())) {
                temList.add(bean);
            }
        }
        musicBeanList.clear();
        musicBeanList.addAll(temList);
        commonAdapter.notifyDataSetChanged();
        llDate.setVisibility(View.VISIBLE);
        tvChooseZhuanji.setVisibility(View.GONE);
        rlKuang.setVisibility(View.GONE);
        llTimes.setVisibility(View.VISIBLE);
        doRequestCurrentTime("2017-" + tvTime.getText().toString());

        // 切换时停止播放
        MediaPlayerUtil.getInStance().pause();
    }

    @OnClick(R.id.ll_weiguilei)
    public void weiguilei() {
        flag = 2;
        rlTime.setVisibility(View.GONE);
        ivWeiguilei.setBackgroundResource(R.mipmap.img_light_bright);
        tvWeiguilei.setTextColor(getResources().getColor(R.color.base_color));
        ivRiqi.setBackgroundResource(R.mipmap.img_light_grey);
        tvRiqi.setTextColor(getResources().getColor(R.color.col3));
        ivZhuanji.setBackgroundResource(R.mipmap.img_light_grey);
        tvZhuanji.setTextColor(getResources().getColor(R.color.col3));
        getParams();
        temList.clear();
        for (MusicBean bean : musicBeanList) {
            if (bean.getMusicName().contains("weiguilei")) {
                temList.add(bean);
            }
        }
        musicBeanList.clear();
        musicBeanList.addAll(temList);
        commonAdapter.notifyDataSetChanged();
        llDate.setVisibility(View.GONE);
        tvChooseZhuanji.setVisibility(View.GONE);
        rlKuang.setVisibility(View.GONE);
        llTimes.setVisibility(View.GONE);

        // 切换时停止播放
        MediaPlayerUtil.getInStance().pause();
    }

    @OnClick(R.id.ll_zhuanji)
    public void zhuanji() {
        flag = 1;
        rlTime.setVisibility(View.GONE);
        ivZhuanji.setBackgroundResource(R.mipmap.img_light_bright);
        tvZhuanji.setTextColor(getResources().getColor(R.color.base_color));
        ivRiqi.setBackgroundResource(R.mipmap.img_light_grey);
        tvRiqi.setTextColor(getResources().getColor(R.color.col3));
        ivWeiguilei.setBackgroundResource(R.mipmap.img_light_grey);
        tvWeiguilei.setTextColor(getResources().getColor(R.color.col3));
        llDate.setVisibility(View.GONE);
        tvChooseZhuanji.setVisibility(View.VISIBLE);
        rlKuang.setVisibility(View.VISIBLE);
        doRefreshZhuanji();
        llTimes.setVisibility(View.GONE);

        // 切换时停止播放
        MediaPlayerUtil.getInStance().pause();
    }

    @OnClick(R.id.iv_left)
    public void setIvLeft() {

        SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, --reduceNum);
        tvTime.setText(sf.format(c.getTime()));
        getParams();
        temList.clear();
        for (MusicBean bean : musicBeanList) {
            if (bean.getMusicName().contains(sf.format(c.getTime()))) {
                temList.add(bean);
            }
        }
        musicBeanList.clear();
        musicBeanList.addAll(temList);
        commonAdapter.notifyDataSetChanged();
        // 获取当前录制时间
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        doRequestCurrentTime(sf1.format(c.getTime()));

        // 切换时停止播放
        MediaPlayerUtil.getInStance().pause();
    }

    private void doRequestCurrentTime(String format) {
        RequestVO vo = new RequestVO();
        vo.setCmd("getLocalAlbumList");
        vo.setUid(PrefsUtil.getString(getActivity(), "userid", ""));
        vo.setDate(format);
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();

                        Toast.makeText(getActivity(), "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String str = response;
                        try {
                            CurrentDayTimeVo vo = new Gson().fromJson(response, CurrentDayTimeVo.class);
                            tvDangrishijian.setText("当日总录制时间：" + vo.getRecTimeLength());
                            tvDangriwenjian.setText("当日文件总时长：" + vo.getFileTimeLength());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    @OnClick(R.id.iv_right)
    public void setIvRight() {
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, ++reduceNum);
        tvTime.setText(sf.format(c.getTime()));
        getParams();
        temList.clear();
        for (MusicBean bean : musicBeanList) {
            if (bean.getMusicName().contains(sf.format(c.getTime()))) {
                temList.add(bean);
            }
        }
        musicBeanList.clear();
        musicBeanList.addAll(temList);
        commonAdapter.notifyDataSetChanged();
        // 获取当前录制时间
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        doRequestCurrentTime(sf1.format(c.getTime()));

        // 切换时停止播放
        MediaPlayerUtil.getInStance().pause();
    }

    @OnClick(R.id.tv_choose_zhuanji)
    public void doChooseZhuanji() {
        Intent intent = new Intent(getActivity(), ChooseZhuanjiActivity.class);
        intent.putExtra("isFromLocalFile", true);
        startActivityForResult(intent, 1001);

        // 切换时停止播放
        MediaPlayerUtil.getInStance().pause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 归类文件返回
        if (requestCode == 1000 && resultCode == RESULT_OK) {

            String fileId = data.getStringExtra("fileTime");
            musicBeanList.clear();
            getParams();
            for (MusicBean bean : musicBeanList) {
                if (bean.getFilePath().contains(fileId)) {
                    bean.setFirstTypeId(data.getStringExtra("firstTypeId"));
                    bean.setSecondTypeId(data.getStringExtra("secondTypeId"));
                    bean.setThirdTypeId(data.getStringExtra("thirdTypeId"));
                    bean.setQuziName(data.getStringExtra("albumName"));
                }
            }
            llWeiguilei.performClick();
        }

        // 按专辑搜索
        else if (requestCode == 1001 && resultCode == RESULT_OK) {
            ThirdTypeVO.ThirdTypeListBean bean = (ThirdTypeVO.ThirdTypeListBean) data.getSerializableExtra("bean");
            tvChooseZhuanji.setText(bean.getThirdTypeName());
            doRefreshZhuanji();
        } else if (requestCode == 2001 && resultCode == RESULT_OK) {
            MusicBean musicBean = (MusicBean) data.getSerializableExtra("vo");
            Intent intent = new Intent(getActivity(), GuileiLocalFileActivity.class);
            intent.putExtra("vo", musicBean);
            startActivityForResult(intent, 1000);
        } else if (requestCode == 3000 && resultCode == RESULT_OK) {//重新归类
            musicBeanList.clear();
            getParams();
            String firstTypeId, secondTypeId, thirdTypeId;
            String id = updatemusicBean.getFilePath().substring(updatemusicBean.getFilePath().indexOf("_") + 1, updatemusicBean.getFilePath().indexOf("_") + 14);
            firstTypeId = PrefsUtil.getString(getActivity(), id, "").split(",")[0];
            secondTypeId = PrefsUtil.getString(getActivity(), id, "").split(",")[1];
            thirdTypeId = PrefsUtil.getString(getActivity(), id, "").split(",")[2];

            upDateService(data.getStringExtra("musicId"), firstTypeId, secondTypeId, thirdTypeId, data.getStringExtra("albumName"));
        }

    }


    /**
     * 重新归类更新服务器
     * albumid 曲目Id
     * firstTypeId 新所属一级分类
     * secondTypeId 新所属二级分类
     * thirdTypeId 新所属三级分类(专辑ID)
     * albumName 新曲目名称
     */
    private void upDateService(final String albumid, String firstTypeId, String secondTypeId, String thirdTypeId, final String albumName) {
        ProgressDialog.showProgressDialog(getActivity(), "更新服务器数据...");
        String json = "{\"cmd\":\"updateType\",\"uid\":\"" + PrefsUtil.getString(getActivity(), "userid", "") + "\",\"albumid\":\"" + albumid +
                "\",\"firstTypeId\":\"" + firstTypeId + "\",\"secondTypeId\":\"" + secondTypeId + "\",\"thirdTypeId\":\"" + thirdTypeId + "\",\"albumName\":\"" + albumName + "\"}";
//        abLog.e("重新归类参数...........", json);
        OkHttpUtils.post().url(Constant.URL).addParams("json", json).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.showToast("网络错误，更新服务器数据失败");
            }

            @Override
            public void onResponse(String response, int id) {
//                abLog.e("重新归类结果..............", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("result").equals("0")) {
                        PrefsUtil.putString(getActivity(), albumName, albumid);
                    } else {
                        ToastUtil.showToast(obj.getString("resultNote"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ProgressDialog.dismissDialog();
    }


    // 刷新专辑列表
    private void doRefreshZhuanji() {
        musicBeanList.clear();
        temList.clear();
        getParams();
        for (MusicBean bean : musicBeanList) {
            if (bean.getFilePath().contains(tvChooseZhuanji.getText())) {
                temList.add(bean);
            }
        }
        musicBeanList.clear();
        musicBeanList.addAll(temList);
        commonAdapter.notifyDataSetChanged();
    }

    /**
     * 删除对话框
     */
    public void showDeleteDialog(final MusicBean musicBean, final ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = "确定要删除文件吗？\n";
        builder.setMessage(message);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
                File file = new File(musicBean.getFilePath());
                deleteFile(file);
                musicBeanList.remove(viewHolder.getPosition());
                commonAdapter.notifyDataSetChanged();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        BusProvider.getInstance().unregister(this);
    }

    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }


}