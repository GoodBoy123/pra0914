package yxy.pra0914;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import yxy.pra0914.api.APIWrapper;
import yxy.pra0914.base.BaseActivity;
import yxy.pra0914.base.BaseApplication;
import yxy.pra0914.bean.HttpResponse;
import yxy.pra0914.utils.FileUtils;
import yxy.pra0914.utils.TLog;

public class Add_development extends BaseActivity implements View.OnClickListener{
    private LinearLayout add_img,dep_imgs;
    private TextView add_dep_back,save;
    private EditText dep_content;
    private ArrayList<ImageView> list = new ArrayList<>();
    private List<Bitmap> listBitmaps= new ArrayList<>();
    //临时将Bitmap对象转为File存储,有多个Bitmap
    private List<File> tmpFiles = new ArrayList<>();
    private static final int REQUESTCODE_PIC = 1;//相册
    private static final int REQUESTCODE_CUT = 3;//图片裁剪
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_development);
        dep_imgs = (LinearLayout)findViewById(R.id.dep_imgs);
        add_img = (LinearLayout)findViewById(R.id.add_img);
        add_dep_back = (TextView)findViewById(R.id.add_dep_back);
        dep_content = (EditText)findViewById(R.id.dep_content);
        save = (TextView)findViewById(R.id.save);
        save.setOnClickListener(this);
        add_dep_back.setOnClickListener(this);
        add_img.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.add_img:
                if(list != null && list.size() >= 3)
                {
                    Toast.makeText(Add_development.this, "最多添加三张照片哦！", Toast.LENGTH_SHORT).show();
                    break;
                }
                openPic();
                break;
            case R.id.add_dep_back:
                finish();
                break;
            case R.id.save:
                //下面应该是保存动态

                //内容不应为空
                if(dep_content.getText().toString().isEmpty())
                {
                    Toast.makeText(Add_development.this, "请填写内容~", Toast.LENGTH_SHORT).show();
                    break;
                }
                Map<String, RequestBody> bodyMap = new HashMap<>();
                if(listBitmaps.size() != 0) {
                    ArrayList<String> pathList = new ArrayList<>();
                    for (Bitmap mBitmap : listBitmaps) {
                        File dep_img = FileUtils.compressImage(mBitmap);
                        String path = dep_img.getPath();
                        pathList.add(path);
                        tmpFiles.add(dep_img);
                    }

                    if (pathList.size() > 0) {
                        for (int i = 0; i < pathList.size(); i++) {
                            File file = new File(pathList.get(i));
                            bodyMap.put("file" + i + "\"; filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
                        }
                    }
                }
                        APIWrapper.getInstance().addDep(BaseApplication.getUserId(),1,dep_content.getText().toString(),bodyMap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Map>() {
                                    @Override
                                    public void onCompleted() {
                                        TLog.log("成功了？", "是的");
                                        for(File file : tmpFiles)
                                            file.delete();
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


                Toast.makeText(Add_development.this, "发布成功！", Toast.LENGTH_SHORT).show();
//                finish();
                break;
        }
    }

    /**
     * 打开相册
     */
    private void openPic() {
        Intent picIntent = new Intent(Intent.ACTION_PICK,null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(picIntent,REQUESTCODE_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Intent data) {
        Bundle bundle =  data.getExtras();
        if (bundle != null){
            //这里也可以做文件上传
            Bitmap mBitmap = bundle.getParcelable("data");
            listBitmaps.add(mBitmap);
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,200);
            lp.setMargins(0, 0, 10, 0);
            imageView.setLayoutParams(lp);
            imageView.setImageBitmap(mBitmap);
            dep_imgs.addView(imageView,list.size());
            list.add(imageView);

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
