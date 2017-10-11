package yxy.pra0914;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yxy.pra0914.MyView.CircleImageView;
import yxy.pra0914.MyView.OptionsPickerView;
import yxy.pra0914.api.APIWrapper;
import yxy.pra0914.base.BaseActivity;
import yxy.pra0914.base.BaseApplication;
import yxy.pra0914.bean.CardBean;
import yxy.pra0914.bean.HttpResponse;
import yxy.pra0914.dto.User;
import yxy.pra0914.listener.CustomListener;
import yxy.pra0914.utils.FileUtils;
import yxy.pra0914.utils.TLog;


public class InfoEdite extends BaseActivity implements View.OnClickListener{

    private CircleImageView ivHead;
    private RelativeLayout layout_choose;
    private RelativeLayout layout_photo;
    private RelativeLayout layout_close;

    private LinearLayout layout_all;
    protected int mScreenWidth;

    /**
     * 定义三种状态
     */
    private static final int REQUESTCODE_PIC = 1;//相册
    private static final int REQUESTCODE_CAM = 2;//相机
    private static final int REQUESTCODE_CUT = 3;//图片裁剪
    private static final int REQUESTCODE_PROFESSION = 5;//选择行业

    private Bitmap mBitmap;
    private File mFile;

    private OptionsPickerView OptionsGender,OptionsAges;
    private ArrayList<CardBean> genders = new ArrayList<>();
    private ArrayList<CardBean> ages = new ArrayList<>();
    private TextView gender,age,profession;
    private EditText nickname,company,job ,person_des;
    private RelativeLayout rlt_profession;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_edite);
        ivHead = (CircleImageView) findViewById(R.id.iv_head);
        layout_all = (LinearLayout) findViewById(R.id.layout_all);
        ivHead.setOnClickListener(this);
        TextView text_cancel = (TextView) findViewById(R.id.cancel);
        TextView text_done = (TextView) findViewById(R.id.done);
        profession = (TextView) findViewById(R.id.profession);
        text_cancel.setOnClickListener(this);
        text_done.setOnClickListener(this);

        //等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃。
        getOptionData();
        initCustomOptionPicker();
        nickname = (EditText) findViewById(R.id.nickname);
        gender = (TextView) findViewById(R.id.gender);
        age = (TextView) findViewById(R.id.age);
        company = (EditText) findViewById(R.id.company);
        job = (EditText) findViewById(R.id.job);
        person_des = (EditText) findViewById(R.id.person_des);
        rlt_profession = (RelativeLayout) findViewById(R.id.rlt_profession);
        rlt_profession.setOnClickListener(this);
        age.setOnClickListener(this);
        gender.setOnClickListener(this);

        Intent intent = getIntent();
        User user = null;
        if (intent != null) {
            user = (User) intent.getSerializableExtra("userInfo");
        }
        if(user != null)
        {
            if (user.getHeadimg() != null)
            {
                Glide.with(InfoEdite.this)
                        .load(user.getHeadimg())
                        .asBitmap()
                        .into(ivHead);
            }
            nickname.setText(user.getNickname());
            gender.setText(user.getGender());
            age.setText(user.getAgeDes());
            if(user.getProfession() != null)
                profession.setText(user.getProfession());
            company.setText(user.getCompany());
            job.setText(user.getJob());
            person_des.setText(user.getPersonDes());
        }

    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        OptionsGender = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = genders.get(options1).getPickerViewText();
                gender.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OptionsGender.returnData();
                                OptionsGender.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OptionsGender.dismiss();
                            }
                        });
                    }
                })
                .isDialog(true)
                .build();

        OptionsGender.setPicker(genders);//添加数据


        OptionsAges = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = ages.get(options1).getPickerViewText();
                age.setText(tx);
            }
        })

                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OptionsAges.returnData();
                                OptionsAges.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OptionsAges.dismiss();
                            }
                        });


                    }
                })
                .isDialog(true)
                .build();

        OptionsAges.setPicker(ages);//添加数据


    }
    private void getOptionData() {

        /**
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        getCardData();
    }
    private void getCardData() {
        genders.add(new CardBean(0, "男"));
        genders.add(new CardBean(1, "女"));

        ages.add(new CardBean(0,"60后"));
        ages.add(new CardBean(1,"70后"));
        ages.add(new CardBean(2,"80后"));
        ages.add(new CardBean(3,"90后"));
        ages.add(new CardBean(4,"00后"));
        ages.add(new CardBean(5,"10后"));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_head:
                showMyDialog();
                break;
            case R.id.cancel:
                this.finish();
                break;
            case R.id.done:
                User user = new User();
                user.setId(BaseApplication.getUserId());
                user.setNickname(nickname.getText().toString());
                user.setGender(gender.getText().toString());
                user.setAgeDes(age.getText().toString());
                user.setProfession(profession.getText().toString());
                user.setCompany(company.getText().toString());
                user.setJob(job.getText().toString());
                user.setPersonDes(person_des.getText().toString());

                APIWrapper.getInstance().updateuser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Map>() {
                            @Override
                            public void onCompleted() {
                                TLog.log("成功了？", "是的");
                            }

                            @Override
                            public void onError(Throwable e) {
                                TLog.error("onError " + e.toString());
                            }

                            @Override
                            public void onNext(Map response) {
                                TLog.error("onNext " + response.get("msg"));
                            }
                        });
                if(mBitmap != null)
                {
                    final File head_img = FileUtils.compressImage(mBitmap);
                    String path = head_img.getPath();
                    ArrayList<String> pathList = new ArrayList<>();
                    pathList.add(path);
                    Map<String, RequestBody> bodyMap = new HashMap<>();
                    if(pathList.size() > 0) {
                        for (int i = 0; i < pathList.size(); i++) {
                            File file = new File(pathList.get(i));
                            bodyMap.put("file"+i+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("image/png"),file));
                        }
                    }

                    APIWrapper.getInstance().alterHeadImg(BaseApplication.getUserId(),bodyMap)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<HttpResponse<List<String>>>() {
                                @Override
                                public void onCompleted() {
                                    TLog.log("成功了？", "是的");
                                    head_img.delete();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    TLog.error("onError " + e.toString());
                                }

                                @Override
                                public void onNext(HttpResponse<List<String>> response) {
                                    TLog.error("onNext " + response.msg);

                                }
                            });

                }

                Toast.makeText(InfoEdite.this, "done", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gender:
                OptionsGender.show();
                break;
            case R.id.age:
                OptionsAges.show();
                break;
            case R.id.rlt_profession:
                String str = profession.getText().toString();
                Intent intent = new Intent(InfoEdite.this,ChooseProfession.class);
                intent.putExtra("profession", str);
                startActivityForResult(intent,REQUESTCODE_PROFESSION);
        }
    }

    PopupWindow avatorPop;


    private void showMyDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_show_dialog,
                null);
        layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
        layout_close = (RelativeLayout) view.findViewById(R.id.layout_close);

        layout_choose.setBackgroundColor(getResources().getColor(
                R.color.base_color_text_white));
        layout_photo.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.pop_bg_press));
        layout_close.setBackgroundColor(getResources().getColor(
                R.color.base_color_text_white));

        layout_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                layout_choose.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                layout_photo.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                layout_close.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                openCamera();
            }
        });

        layout_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                layout_photo.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                layout_choose.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                layout_close.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                openPic();

            }
        });

        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_photo.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                layout_close.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                layout_choose.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                avatorPop.dismiss();
            }
        });



        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        avatorPop = new PopupWindow(view, mScreenWidth, 200);
        avatorPop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    avatorPop.dismiss();
                    return true;
                }
                return false;
            }
        });

        avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        avatorPop.setTouchable(true);
        avatorPop.setFocusable(true);
        avatorPop.setOutsideTouchable(true);
        avatorPop.setBackgroundDrawable(new BitmapDrawable());
        // 动画效果 从底部弹起
        avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
        avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 打开相册
     */
    private void openPic() {
        Intent picIntent = new Intent(Intent.ACTION_PICK,null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(picIntent,REQUESTCODE_PIC);
    }

    /**
     * 调用相机
     */
    private void openCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()){
                file.mkdirs();
            }
            mFile = new File(file, System.currentTimeMillis() + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
            startActivityForResult(intent,REQUESTCODE_CAM);
        } else {
            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_CAM:
                    startPhotoZoom(Uri.fromFile(mFile));
                    break;
                case REQUESTCODE_PIC:

                    if (data == null || data.getData() == null){
                        return;
                    }
                    startPhotoZoom(data.getData());

                    break;
                case REQUESTCODE_CUT:

                    if (data!= null){
                        setPicToView(data);
                    }
                    break;
                case REQUESTCODE_PROFESSION:
                    if(data != null)
                    {
                        profession.setText(data.getExtras().get("profession").toString());
                    }
                    break;
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Intent data) {
        Bundle bundle =  data.getExtras();
        if (bundle != null){
            //这里也可以做文件上传
            mBitmap = bundle.getParcelable("data");
            ivHead.setImageBitmap(mBitmap);

        }
    }


    /**
     * 打开系统图片裁剪功能
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop",true);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",300);
        intent.putExtra("outputY",300);
        intent.putExtra("scale",true); //黑边
        intent.putExtra("scaleUpIfNeeded",true); //黑边
        intent.putExtra("return-data",true);
        intent.putExtra("noFaceDetection",true);
        startActivityForResult(intent,REQUESTCODE_CUT);

    }

}
