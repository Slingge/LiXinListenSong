package com.lixin.listen.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lixin.listen.R;
import com.lixin.listen.bean.IEditMaxLenth;
import com.lixin.listen.bean.IPlayCompleted;
import com.lixin.listen.bean.ISeekbarProgress;
import com.lixin.listen.bean.MusicBean;
import com.lixin.listen.bean.ThirdTypeVO;
import com.lixin.listen.common.Constant;
import com.lixin.listen.util.MaxLengthWatcher;
import com.lixin.listen.util.MediaPlayerUtil;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.view.ClickButton;
import com.lixin.listen.view.ClickImageView;
import com.lixin.listen.view.ClickImageView.ClickImageViewCallBack;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lixin.listen.R.id.iv_tuijianzhuanji;

/**
 * 归类本地文件
 */
public class GuileiLocalFileActivity extends AppCompatActivity implements ClickImageViewCallBack,ClickButton.ClickButtonCallBack{

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.activity_guilei_local_file)
    LinearLayout activityGuileiLocalFile;

    MusicBean musicBean;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.iv_pause)
    ImageView ivPause;
    @Bind(R.id.rl_play)
    RelativeLayout rlPlay;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.sb)
    SeekBar sb;
    @Bind(R.id.iv_delete)
    ImageView ivDelete;
    @Bind(R.id.iv_upload)
    ImageView ivUpload;
    @Bind(R.id.iv_weilei)
    ImageView ivWeilei;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_zhuanji)
    TextView tvZhuanji;

    @Bind(R.id.iv_shadow)
    ImageView ivShadow;
    @Bind(R.id.tv_all_time)
    TextView tvAllTime;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.rl_shang)
    RelativeLayout rlShang;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.iv_chongxingeilei)
    ImageView ivChongxingeilei;
    @Bind(R.id.tv_zhuanji_icon)
    TextView tvZhuanjiIcon;

    private String firstTypeId = "";
    private String secondTypeId = "";
    private String thirdTypeId = "";

    ClickImageView ivTuijianzhuanji;
    ClickButton btnSave;

    // 专辑名称
    String zhuanjiName;
    // 曲子名称
    String quziName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guilei_local_file);
        ButterKnife.bind(this);
        getParams();
        initViews();
        ivTuijianzhuanji= (ClickImageView) findViewById(R.id.iv_tuijianzhuanji);
        ivTuijianzhuanji.setClickImageViewCallBack(this);
        btnSave= (ClickButton) findViewById(R.id.btn_save);
        btnSave.setClickButtonCallBack(this);

    }

    private void getParams() {
        musicBean = (MusicBean) getIntent().getSerializableExtra("vo");

    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("归类本地文件");
        ivDelete.setVisibility(View.GONE);
        ivUpload.setVisibility(View.GONE);
        ivWeilei.setVisibility(View.GONE);
        if (musicBean.getMusicName().contains("weiguilei")) {
            tvName.setText("未归类");

        } else {
            zhuanjiName = musicBean.getMusicName().substring(1, musicBean.getMusicName().indexOf("#"));
            quziName = musicBean.getMusicName().substring(musicBean.getMusicName().indexOf("#") + 1,
                    musicBean.getMusicName().indexOf("%"));
            etName.setText(quziName);
            tvZhuanji.setText(zhuanjiName);
            tvZhuanjiIcon.setText(zhuanjiName);
            tvZhuanjiIcon.setVisibility(View.VISIBLE);
            tvName.setText(quziName);
        }
        tvAllTime.setText(ShowTime(getFileTime(musicBean.getFilePath())));
        etName.addTextChangedListener(new MaxLengthWatcher(12, etName, new IEditMaxLenth() {
            @Override
            public void maxLenth() {
                Toast.makeText(GuileiLocalFileActivity.this, "最多只能输入12个字符", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @OnClick({iv_tuijianzhuanji, R.id.tv_zhuanji})
    public void toTuijian() {
        Intent intent = new Intent(GuileiLocalFileActivity.this, ChooseZhuanjiActivity.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            ThirdTypeVO.ThirdTypeListBean bean = (ThirdTypeVO.ThirdTypeListBean) data.getSerializableExtra("bean");
            tvZhuanji.setText(bean.getThirdTypeName());
            firstTypeId = data.getStringExtra("firstTypeId");
            secondTypeId = data.getStringExtra("secondTypeId");
            thirdTypeId = data.getStringExtra("thirdTypeId");
        }
    }


    /**
     * 播放语音
     */
    @OnClick(R.id.iv_play)
    public void playVoice() {
        ivPause.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.GONE);
        MediaPlayerUtil.getInStance().play(musicBean.getFilePath(), sb, new IPlayCompleted() {
            @Override
            public void playCompleted() {
                ivPause.setVisibility(View.GONE);
                ivPlay.setVisibility(View.VISIBLE);
            }
        }, new ISeekbarProgress() {
            @Override
            public void seekbarProgress(int pos) {
                tvTime.setText(ShowTime(pos));
            }
        });
    }

    @OnClick(R.id.iv_pause)
    public void pause() {
        ivPause.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
        MediaPlayerUtil.getInStance().pause();
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

    @Override
    public void clickImage() {
        Intent intent = new Intent(GuileiLocalFileActivity.this, ChooseZhuanjiActivity.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void clickButton() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            Toast.makeText(this, "请先填写曲子的名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tvZhuanji.getText().toString())) {
            Toast.makeText(this, "请先选择专辑", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(musicBean.getFilePath());
        String filePath = musicBean.getFilePath();
        // 未归类文件
        if (filePath.contains("weiguilei")) {
            filePath = filePath.replace("weiguilei_", "$" + tvZhuanji.getText() + "#" + etName.getText() + "%");
        }
        // 已归类文件
        else {
            filePath = filePath.replace(quziName, etName.getText().toString().trim());
            filePath = filePath.replace(zhuanjiName, tvZhuanji.getText().toString());
        }

        file.renameTo(new File(filePath));

        final String finalFilePath = filePath;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("firstTypeId", firstTypeId);
                intent.putExtra("secondTypeId", secondTypeId);
                intent.putExtra("thirdTypeId", thirdTypeId);
                intent.putExtra("albumName", etName.getText().toString());
                intent.putExtra("fileTime", finalFilePath.substring(finalFilePath.indexOf("_") + 1, finalFilePath.indexOf("_") + 14));
                PrefsUtil.putString(GuileiLocalFileActivity.this, finalFilePath.substring(finalFilePath.indexOf("_") + 1, finalFilePath.indexOf("_") + 14), firstTypeId +
                        "," + secondTypeId + "," + thirdTypeId + "," + etName.getText().toString());
                // 保存一级目录
                if (TextUtils.isEmpty(PrefsUtil.getString(GuileiLocalFileActivity.this, Constant.FIRST_ROOT, ""))) {
                    PrefsUtil.putString(GuileiLocalFileActivity.this, Constant.FIRST_ROOT, firstTypeId);
                } else {
                    String firstRoot = PrefsUtil.getString(GuileiLocalFileActivity.this, Constant.FIRST_ROOT, "");
                    PrefsUtil.putString(GuileiLocalFileActivity.this, Constant.FIRST_ROOT, firstTypeId + "," +
                            firstRoot
                    );
                }
                // 保存二级目录
                if (TextUtils.isEmpty(PrefsUtil.getString(GuileiLocalFileActivity.this, Constant.SECOND_ROOT, ""))) {
                    PrefsUtil.putString(GuileiLocalFileActivity.this, Constant.SECOND_ROOT, secondTypeId);
                } else {
                    String sencondRoot = PrefsUtil.getString(GuileiLocalFileActivity.this, Constant.SECOND_ROOT, "");
                    PrefsUtil.putString(GuileiLocalFileActivity.this, Constant.SECOND_ROOT, secondTypeId + "," + sencondRoot);
                }
                // 保存三级目录
                if (TextUtils.isEmpty(PrefsUtil.getString(GuileiLocalFileActivity.this, Constant.THIRD_ROOT, ""))) {
                    PrefsUtil.putString(GuileiLocalFileActivity.this, Constant.THIRD_ROOT, thirdTypeId);
                } else {
                    String thirdRoot = PrefsUtil.getString(GuileiLocalFileActivity.this, Constant.THIRD_ROOT, "");
                    PrefsUtil.putString(GuileiLocalFileActivity.this, Constant.THIRD_ROOT, thirdTypeId + "," + thirdRoot);
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        }, 1000);
    }

}

