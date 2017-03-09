package com.lixin.listen.fragment;


import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.activity.ChooseZhuanjiActivity;
import com.lixin.listen.bean.CommonVO;
import com.lixin.listen.bean.IPlayCompleted;
import com.lixin.listen.bean.ISeekbarProgress;
import com.lixin.listen.bean.MusicBean;
import com.lixin.listen.bean.MyZhuanjiVo;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.bean.ZhuanjiQuziVO;
import com.lixin.listen.common.Constant;
import com.lixin.listen.util.MediaPlayerUtil;
import com.lixin.listen.util.PlayAudioManager;
import com.lixin.listen.util.Player;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.widget.recyclerview.CommonAdapter;
import com.lixin.listen.widget.recyclerview.MultiItemTypeAdapter;
import com.lixin.listen.widget.recyclerview.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFileFragment extends Fragment {

    @Bind(R.id.rv_zhuanji)
    RecyclerView rvZhuanji;
    @Bind(R.id.rv_zhuanji_quzi)
    RecyclerView rvZhuanjiQuzi;

    private CommonAdapter<MyZhuanjiVo.AlbumListBean> zhuanjiAdapter;
    private List<MyZhuanjiVo.AlbumListBean> zhuanjiList = new ArrayList<>();

    private CommonAdapter<ZhuanjiQuziVO.AlbumListBean> quziAdapter;
    private List<ZhuanjiQuziVO.AlbumListBean> quziList = new ArrayList<>();

    private int zhuanji_index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_server_file, container, false);
        ButterKnife.bind(this, view);
        initViews();
        loadData();
        return view;
    }

    private void initViews() {
        // 专辑
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvZhuanji.setLayoutManager(linearLayoutManager2);
        zhuanjiAdapter = new CommonAdapter<MyZhuanjiVo.AlbumListBean>(getActivity(), zhuanjiList, R.layout.item_zhuanji) {
            @Override
            protected void convert(ViewHolder holder, MyZhuanjiVo.AlbumListBean bean, int position) {
                holder.setText(R.id.tv_zhuanji_name, bean.getAlbumName());
                if (bean.isSelect()) {
                    holder.setVisible(R.id.iv_selected, true);
                } else {
                    holder.setVisible(R.id.iv_selected, false);
                }
                Glide.with(getActivity()).load(bean.getAlbumImg()).dontAnimate().placeholder(R.mipmap.img_zhuanji).
                        into(((ImageView) holder.getView(R.id.iv)));

            }
        };
        rvZhuanji.setAdapter(zhuanjiAdapter);
        zhuanjiAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MyZhuanjiVo.AlbumListBean bean = zhuanjiList.get(position);
                doSelectQuzi(bean.getAlbumId());
                for (int i = 0; i < zhuanjiList.size(); i++) {
                    MyZhuanjiVo.AlbumListBean bean1 = zhuanjiList.get(i);
                    if (position == i) {
                        bean1.setSelect(true);
                    } else {
                        bean1.setSelect(false);
                    }
                }
                zhuanjiAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        // 曲子
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvZhuanjiQuzi.setLayoutManager(linearLayoutManager);
        quziAdapter = new CommonAdapter<ZhuanjiQuziVO.AlbumListBean>(getActivity(), quziList, R.layout.item_music) {
            @Override
            protected void convert(final ViewHolder holder, final ZhuanjiQuziVO.AlbumListBean bean, int position) {

                holder.setVisible(R.id.iv_share, true);
                holder.setVisible(R.id.iv_weilei, false);
                holder.setVisible(R.id.iv_upload, false);
                holder.setText(R.id.tv_name, bean.getAlbumName());
                holder.setText(R.id.tv_all_time, bean.getAlbumLength());
                holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteDialog(bean, holder);

                    }
                });

                holder.setOnClickListener(R.id.rl_play, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        holder.setVisible(R.id.iv_pause, true);
                        holder.setVisible(R.id.iv_play, false);
                        MediaPlayerUtil.getInStance().play(bean.getAlbumVoice(), ((SeekBar) holder.getView(R.id.sb)), new IPlayCompleted() {
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
            }
        };
        rvZhuanjiQuzi.setAdapter(quziAdapter);

    }

    public String ShowTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * 删除对话框
     */
    public void showDeleteDialog(final ZhuanjiQuziVO.AlbumListBean musicBean, final ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = "确定要删除文件吗？\n";
        builder.setMessage(message);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
                doDeleteQuzi(musicBean.getAlbumId());
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

    /**
     * 赞
     *
     * @param albumId
     */
    private void doZan(String albumId) {
        RequestVO vo = new RequestVO();
        vo.setCmd("praiseMusic");
        vo.setUid(PrefsUtil.getString(getActivity(), "userid", ""));
        vo.setAlbumId(albumId);
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommonVO vo = new Gson().fromJson(response, CommonVO.class);
                        Toast.makeText(getActivity(), vo.getResultNote(), Toast.LENGTH_SHORT).show();
                        loadData();
                    }

                });
    }

    /**
     * 删除曲子
     *
     * @param albumId
     */
    private void doDeleteQuzi(String albumId) {
        RequestVO vo = new RequestVO();
        vo.setCmd("deleteMusic");
        vo.setUid(PrefsUtil.getString(getActivity(), "userid", ""));
        vo.setAlbumId(albumId);
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommonVO vo = new Gson().fromJson(response, CommonVO.class);
                        Toast.makeText(getActivity(), vo.getResultNote(), Toast.LENGTH_SHORT).show();
                        PrefsUtil.putString(getActivity(), "@" + vo.getLocalId().substring(vo.getLocalId().indexOf("_"))
                                , "upload");
                        loadData();
                    }

                });
    }

    /**
     * 获取专辑下的曲子
     */
    private void doSelectQuzi(String quziId) {
        RequestVO vo = new RequestVO();
        vo.setCmd("getAlbumList");
        vo.setThirdTypeId(quziId);
        vo.setUid(PrefsUtil.getString(getActivity(), "userid", ""));
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ZhuanjiQuziVO vo = new Gson().fromJson(response, ZhuanjiQuziVO.class);
                        updateQuzi(vo);
                    }

                });
    }

    private void updateQuzi(ZhuanjiQuziVO vo) {
        quziList.clear();
        quziList.addAll(vo.getAlbumList());
        quziAdapter.notifyDataSetChanged();
    }

    /**
     * 获取我的专辑
     */
    private void loadData() {
        RequestVO vo = new RequestVO();
        vo.setCmd("getAlbum");
        vo.setUid(PrefsUtil.getString(getActivity(), "userid", ""));
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyZhuanjiVo vo = new Gson().fromJson(response, MyZhuanjiVo.class);
                        updateMyzhuanji(vo);
                    }

                });
    }

    // 设置我的专辑
    private void updateMyzhuanji(MyZhuanjiVo vo) {
        zhuanjiList.clear();
        zhuanjiList.addAll(vo.getAlbumList());

        if (vo.getAlbumList().size() > 0) {
            vo.getAlbumList().get(0).setSelect(true);
            doSelectQuzi(vo.getAlbumList().get(0).getAlbumId());
        }
        zhuanjiAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
