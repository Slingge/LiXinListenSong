package com.lixin.listen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.bean.CommonVO;
import com.lixin.listen.bean.DaySignalVO;
import com.lixin.listen.bean.QuestionListvo;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.common.Constant;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.util.ProgressDialog;
import com.lixin.listen.widget.recyclerview.CommonAdapter;
import com.lixin.listen.widget.recyclerview.MultiItemTypeAdapter;
import com.lixin.listen.widget.recyclerview.ViewHolder;
import com.lixin.listen.wxapi.WXEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 帮助与反馈
 */
public class FankuiActivity extends AppCompatActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_fankui)
    TextView tvFankui;
    @Bind(R.id.activity_fankui)
    LinearLayout activityFankui;
    @Bind(R.id.rv_question)
    RecyclerView rvQuestion;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.btn_commit)
    Button btnCommit;

    private CommonAdapter<QuestionListvo.QuestionListBean> questionAdapter;
    private List<QuestionListvo.QuestionListBean> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);
        ButterKnife.bind(this);
        initViews();
        loadData();
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FankuiActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvQuestion.setLayoutManager(linearLayoutManager);
        questionAdapter = new CommonAdapter<QuestionListvo.QuestionListBean>(FankuiActivity.this, questionList, R.layout.item_fankui_list) {
            @Override
            protected void convert(ViewHolder holder, QuestionListvo.QuestionListBean questionListBean, int position) {
                holder.setText(R.id.tv, questionListBean.getQuestionName());
            }
        };
        rvQuestion.setAdapter(questionAdapter);
        questionAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(FankuiActivity.this, FankuiXiangQingActivity.class);
                intent.putExtra("questionId", questionList.get(position).getQuestionId());
                intent.putExtra("title",questionList.get(position).getQuestionName());
                startActivityForResult(intent, 1000);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void loadData() {
        ProgressDialog.showProgressDialog(this, null);
        RequestVO vo = new RequestVO();
        vo.setCmd("getQuestionList");
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(FankuiActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        QuestionListvo vo = new Gson().fromJson(response, QuestionListvo.class);
                        upadataViews(vo);
                    }

                });
        ProgressDialog.dismissDialog();
    }

    private void upadataViews(QuestionListvo vo) {
        questionList.clear();
        if (vo.getQuestionList().size() > 0) {
            questionList.addAll(vo.getQuestionList());
        }
        questionAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_commit)
    public void doCommit() {
        ProgressDialog.showProgressDialog(this, null);
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            ProgressDialog.dismissDialog();
            return;
        }
//        if (TextUtils.isEmpty(etPhone.getText().toString())){
//            Toast.makeText(this, "电话不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
        RequestVO vo = new RequestVO();
        vo.setCmd("upComplain");
        vo.setUid(PrefsUtil.getString(FankuiActivity.this, "userid", ""));
        vo.setComplainContent(etContent.getText().toString());
        vo.setTellphone(etPhone.getText().toString());
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(FankuiActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommonVO vo = new Gson().fromJson(response, CommonVO.class);
                        if (vo.getResult().endsWith("0")) {

                            finish();
                        }
                        Toast.makeText(FankuiActivity.this, vo.getResultNote(), Toast.LENGTH_SHORT).show();
                    }

                });
        ProgressDialog.dismissDialog();
    }

    @OnClick(R.id.iv_back)
    public void doBack() {
        finish();
    }
}
