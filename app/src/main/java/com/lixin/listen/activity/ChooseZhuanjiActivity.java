package com.lixin.listen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.bean.FirstTypeVO;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.bean.SecondTypeVo;
import com.lixin.listen.bean.ThirdTypeVO;
import com.lixin.listen.common.Constant;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.widget.CommonGridView;
import com.lixin.listen.widget.recyclerview.CommonAdapter;
import com.lixin.listen.widget.recyclerview.ViewHolder;
import com.lixin.listen.wxapi.WXEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class ChooseZhuanjiActivity extends AppCompatActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.rv2)
    RecyclerView rv2;
    @Bind(R.id.gv)
    CommonGridView gv;

    private CommonAdapter<FirstTypeVO.FirstTypeListBean> firstAdapter;
    private List<FirstTypeVO.FirstTypeListBean> firList = new ArrayList<>();

    private CommonAdapter<SecondTypeVo.SecondTypeListBean> secondAdapter;
    private List<SecondTypeVo.SecondTypeListBean> secondList = new ArrayList<>();

    private com.lixin.listen.widget.CommonAdapter<ThirdTypeVO.ThirdTypeListBean> thirdAdapter;
    private List<ThirdTypeVO.ThirdTypeListBean> thirdList = new ArrayList<>();

    private String firstId = "";
    private String secondId= "";
    private String thirdId = "";

    private boolean isFromLocalFile = false;

    private String firstRoot = "";
    private String sencondRoot = "";
    private String thirdRoot = "";
    private String[] firstRootList = new String[]{};
    private String[] sencondRootList = new String[]{};
    private String[] thirdRootList = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_zhuanji);
        ButterKnife.bind(this);

        getParams();
        initViews();
        initDatas();
    }

    private void getParams() {
        isFromLocalFile = getIntent().getBooleanExtra("isFromLocalFile", false);

        firstRoot = PrefsUtil.getString(ChooseZhuanjiActivity.this, Constant.FIRST_ROOT, "");
        sencondRoot = PrefsUtil.getString(ChooseZhuanjiActivity.this, Constant.SECOND_ROOT, "");
        thirdRoot = PrefsUtil.getString(ChooseZhuanjiActivity.this, Constant.THIRD_ROOT, "");

        firstRootList = firstRoot.split(",");
        sencondRootList = sencondRoot.split(",");
        thirdRootList = thirdRoot.split(",");
    }

    private void initDatas() {
        doLoadFirstType();
    }

    private void doLoadFirstType() {
        String url = Constant.URL + "json=" + new Gson().toJson(new RequestVO("getFirstType"));
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(ChooseZhuanjiActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        FirstTypeVO vo = new Gson().fromJson(response, FirstTypeVO.class);
                        updataViews(vo);

                    }

                });
    }

    private void doLoadSecondType(String firstId) {

        RequestVO vo = new RequestVO();
        vo.setFirstTypeId(firstId);
        vo.setCmd("getSecondType");
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ChooseZhuanjiActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SecondTypeVo vo = new Gson().fromJson(response, SecondTypeVo.class);
                        updataViews2(vo);
                    }

                });
    }

    // 设置二级分类
    private void updataViews2(SecondTypeVo vo) {
        secondList.clear();
        secondList.addAll(vo.getSecondTypeList());
        if (TextUtils.isEmpty(secondId)){
            secondId = vo.getSecondTypeList().get(0).getSecondTypeId();
        }
        secondAdapter.notifyDataSetChanged();


        if (isFromLocalFile){
            List<SecondTypeVo.SecondTypeListBean> tempList = new ArrayList<>();
            for (int i = 0; i < secondList.size(); i++) {
                SecondTypeVo.SecondTypeListBean bean = secondList.get(i);
                for (int i1 = 0; i1 < sencondRootList.length; i1++) {
                    if (bean.getSecondTypeId().equals(sencondRootList[i1])){
                        tempList.add(bean);
                        break;
                    }
                }
            }
            secondList.clear();
            secondList.addAll(tempList);
        }

        // 加载三级分类
        doLoadThirdType(secondList.get(0).getSecondTypeId());
    }

    // 请求三级分类
    private void doLoadThirdType(String secondTypeId) {
        RequestVO vo = new RequestVO();
        vo.setSecondTypeId(secondTypeId);
        vo.setCmd("getThirdType");
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ChooseZhuanjiActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ThirdTypeVO vo = new Gson().fromJson(response, ThirdTypeVO.class);
                        updataViews3(vo);
                    }

                });
    }

    // 设置三级分类
    private void updataViews3(ThirdTypeVO vo) {
        thirdList.clear();
        thirdList.addAll(vo.getThirdTypeList());
        if (TextUtils.isEmpty(thirdId)){
            thirdId = vo.getThirdTypeList().get(0).getThirdTypeId();
        }

        if (isFromLocalFile){
            List<ThirdTypeVO.ThirdTypeListBean> tempList = new ArrayList<>();
            for (int i = 0; i < thirdList.size(); i++) {
                ThirdTypeVO.ThirdTypeListBean bean = thirdList.get(i);
                for (int i1 = 0; i1 < thirdRootList.length; i1++) {
                    if (bean.getThirdTypeId().equals(thirdRootList[i1])){
                        tempList.add(bean);
                        break;
                    }
                }
            }
            thirdList.clear();
            thirdList.addAll(tempList);
        }

        thirdAdapter.notifyDataSetChanged();

    }

    // 设置一级分类
    private void updataViews(FirstTypeVO vo) {
        firList.clear();
        firList.addAll(vo.getFirstTypeList());
        firList.get(0).setChecked(true);
        if (TextUtils.isEmpty(firstId)){
            firstId = firList.get(0).getFirstTypeId();
        }

        if (isFromLocalFile){
            List<FirstTypeVO.FirstTypeListBean> tempList = new ArrayList<>();
            for (int i = 0; i < firList.size(); i++) {
                FirstTypeVO.FirstTypeListBean bean = firList.get(i);
                for (int i1 = 0; i1 < firstRootList.length; i1++) {
                    if (bean.getFirstTypeId().equals(firstRootList[i1])){
                        tempList.add(bean);
                        break;
                    }
                }
            }
            firList.clear();
            firList.addAll(tempList);
        }

        firstAdapter.notifyDataSetChanged();
        // 加载第二级
        if (firList.size() == 0) {
            Toast.makeText(ChooseZhuanjiActivity.this, "暂时没有归类的文件", Toast.LENGTH_SHORT).show();
            return;
        }
        doLoadSecondType(firList.get(0).getFirstTypeId());
    }


    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("选择专辑名称");

        // 一级分类
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        firstAdapter = new CommonAdapter<FirstTypeVO.FirstTypeListBean>(ChooseZhuanjiActivity.this, firList, R.layout.item_first_type) {
            @Override
            protected void convert(ViewHolder holder, final FirstTypeVO.FirstTypeListBean bean, final int position) {

                if (isFromLocalFile) {
                    for (String s : firstRootList) {
                        if (s.equals(bean.getFirstTypeId())){
                            holder.setVisible(R.id.ll_item, true);
                            break;
                        }
                        else {
                            holder.setVisible(R.id.ll_item, false);
                        }
                    }
                }

                holder.setText(R.id.tv_name, bean.getFirstTypeName());
                if (bean.isChecked()) {
                    holder.setVisible(R.id.view, true);
                } else {
                    holder.setVisible(R.id.view, false);
                }
                holder.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firstId = bean.getFirstTypeId();
                        for (FirstTypeVO.FirstTypeListBean listBean : firList) {
                            listBean.setChecked(false);
                        }
                        firList.get(position).setChecked(true);
                        notifyDataSetChanged();
                        doLoadSecondType(bean.getFirstTypeId());

                    }
                });
            }
        };
        rv.setAdapter(firstAdapter);

        // 二级分类
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv2.setLayoutManager(linearLayoutManager2);
        secondAdapter = new CommonAdapter<SecondTypeVo.SecondTypeListBean>(ChooseZhuanjiActivity.this,
                secondList, R.layout.item_second_type) {
            @Override
            protected void convert(ViewHolder holder, final SecondTypeVo.SecondTypeListBean bean, int position) {

                if (isFromLocalFile) {
                    for (String s : sencondRootList) {
                        if (s.equals(bean.getSecondTypeId())){
                            holder.setVisible(R.id.rl_item, true);
                            break;
                        }
                        else {
                            holder.setVisible(R.id.rl_item, false);
                        }
                    }
                }

                Glide.with(ChooseZhuanjiActivity.this).load(bean.getSecondTypeImg()).placeholder(R.drawable.img_zhuanji_bg).error(R.drawable.img_zhuanji_bg).into((ImageView) holder.getView(R.id.iv));
                holder.setText(R.id.tv_name, bean.getSecondTypeName());
                holder.setOnClickListener(R.id.iv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondId = bean.getSecondTypeId();
                        doLoadThirdType(bean.getSecondTypeId());
                    }
                });
            }
        };
        rv2.setAdapter(secondAdapter);

        // 三级分类
        thirdAdapter = new com.lixin.listen.widget.CommonAdapter<ThirdTypeVO.ThirdTypeListBean>(ChooseZhuanjiActivity.this,
                thirdList, R.layout.item_third_type) {
            @Override
            public void convert(com.lixin.listen.widget.ViewHolder holder, final ThirdTypeVO.ThirdTypeListBean bean) {

                if (isFromLocalFile) {
                    for (String s : thirdRootList) {
                        if (s.equals(bean.getThirdTypeId())){
                            holder.setVisible(R.id.rl_item3, true);
                            break;
                        }
                        else {
                            holder.setVisible(R.id.rl_item3, false);
                        }
                    }
                }

                thirdId = bean.getThirdTypeId();
                holder.setText(R.id.tv_name, bean.getThirdTypeName());
                Glide.with(ChooseZhuanjiActivity.this).load(bean.getThirdTypeImg()).placeholder(R.mipmap.img_fenlei2).error(R.mipmap.img_fenlei2).into((ImageView) holder.getView(R.id.iv));
                holder.setOnClickListener(R.id.iv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("bean", bean);
                        intent.putExtra("thirdTypeId", thirdId);
                        intent.putExtra("secondTypeId", secondId);
                        intent.putExtra("firstTypeId", firstId);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        };
        gv.setAdapter(thirdAdapter);
    }
}
