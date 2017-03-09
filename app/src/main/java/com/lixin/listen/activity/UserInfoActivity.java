package com.lixin.listen.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.bean.CommonVO;
import com.lixin.listen.bean.DaySignalVO;
import com.lixin.listen.bean.EditUserInfoVO;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.common.Constant;
import com.lixin.listen.util.CameraGallaryUtil;
import com.lixin.listen.util.GlideCircleTransform;
import com.lixin.listen.util.PrefsUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static com.lixin.listen.common.Constant.FILE_PATH;

public class UserInfoActivity extends AppCompatActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.tv_head)
    TextView tvHead;
    @Bind(R.id.iv_header)
    CircleImageView ivHeader;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.tv_sex_text)
    TextView tvSexText;
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.ll_date)
    LinearLayout llDate;
    @Bind(R.id.activity_user_info)
    LinearLayout activityUserInfo;
    @Bind(R.id.btn_commit)
    Button btnCommit;

    Bitmap bitmapImg = null;

    //调用系统相册-选择图片
    private static final int IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(UserInfoActivity.this).load(PrefsUtil.getString(UserInfoActivity.this, "avatar", "")).
                transform(new GlideCircleTransform(UserInfoActivity.this)).into(ivHeader);
        etNickname.setText(PrefsUtil.getString(UserInfoActivity.this, "nickname", ""));
    }

    @OnClick(R.id.iv_left)
    public void doLeft() {
        if (tvSex.getText().equals("男")){
            tvSex.setText("女");
        }
        else {
            tvSex.setText("男");
        }

    }

    @OnClick(R.id.iv_right)
    public void doRight() {
        if (tvSex.getText().equals("男")){
            tvSex.setText("女");
        }
        else {
            tvSex.setText("男");
        }
    }

    @OnClick(R.id.iv_header)
    public void doSelectHeader() {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intent, CameraGallaryUtil.PHOTO_REQUEST_GALLERY);

        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    public void saveFile(Bitmap bm, String fileName) throws IOException {
        String path = FILE_PATH;
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        bos.flush();
        bos.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            bitmapImg = bm;
            try {
                File file = new File(FILE_PATH + "touxiang.jpg");
                if (file.exists()){
                    file.delete();
                }
                saveFile(bitmapImg, "touxiang.jpg");
                ivHeader.setImageBitmap(bitmapImg);

//                Glide.with(UserInfoActivity.this).load(new File(FILE_PATH + "touxiang.jpg")).
//                                transform(new GlideCircleTransform(UserInfoActivity.this)).into(ivHeader);
            } catch (IOException e) {
                e.printStackTrace();
            }
            c.close();
        }


    }

    @OnClick(R.id.btn_commit)
    public void doCommit() {

        if (TextUtils.isEmpty(etNickname.getText().toString())) {
            Toast.makeText(UserInfoActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestVO vo = new RequestVO();
        vo.setCmd("androidUpdateNickname");
        vo.setUid(PrefsUtil.getString(UserInfoActivity.this, "userid", ""));
        vo.setUserName(etNickname.getText().toString());


        String url = Constant.URL + "json=" + new Gson().toJson(vo);

        Map<String, String> params = new HashMap<>();
        params.put("cmd", "androidUpdateNickname");
        params.put("uid", PrefsUtil.getString(UserInfoActivity.this, "userid", ""));
        params.put("userName", etNickname.getText().toString());

        OkHttpUtils.post()//
                .addFile("file", new File(FILE_PATH + "touxiang.jpg").getName(), new File(FILE_PATH + "touxiang.jpg"))//
                .url(url)
                .params(params)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(UserInfoActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        EditUserInfoVO vo = new Gson().fromJson(response, EditUserInfoVO.class);
                        if (vo.getResult().equals("0")) {
                            Toast.makeText(UserInfoActivity.this, vo.getResultNote(), Toast.LENGTH_SHORT).show();
                            PrefsUtil.putString(UserInfoActivity.this, "avatar", vo.getUserIcon());
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(UserInfoActivity.this, "修改失败，请重试", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public String bitmaptoString(Bitmap bitmap) {

        // 将Bitmap转换成字符串

        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);

        byte[] bytes = bStream.toByteArray();

        string = Base64.encodeToString(bytes, Base64.DEFAULT);

        return string;

    }

    public String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);

        FileInputStream in = new FileInputStream(file);

        byte[] buffer = new byte[(int) file.length() + 100];
        int length = in.read(buffer);

        return Base64.encodeToString(buffer, 0, length,Base64.DEFAULT);
    }

}
