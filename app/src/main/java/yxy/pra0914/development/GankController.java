package yxy.pra0914.development;


import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yxy.pra0914.BuildConfig;
import yxy.pra0914.dto.DevelopmentDto;
import yxy.pra0914.dto.PageHelper;
import yxy.pra0914.order.GankResEntity;
import yxy.pra0914.utils.LogUtils;


/**
 * Created by dasu on 2017/4/8.
 */
public class GankController {
    private static final String TAG = GankController.class.getSimpleName();
    //订单类型
    public static final String TYPE_WE = "我们";
    public static final String TYPE_EXPRESS = "快递人";
    //默认每次只加载10个数据
    private static final int DEFAULT_LOAD_COUNTS = 10;

    private static Gson sGson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:SS")
            .serializeNulls()
            .create();

    private static GankApi getGankApi() {
        return GankApiSingleton.mInstance;
    }

    /**
     * 获取指定类型的干货，默认一次加载10条信息
     *
     * @param type
     *
     * @param page     分页加载
     * @param callback
     */
    public static void getSpecifyGanHuo(int id , final String type, int page, final RetrofitListener<List<DevelopmentDto>> callback) {
        LogUtils.d(TAG, "正在下载动态，类型： " + type + " ，下载数量： " + DEFAULT_LOAD_COUNTS + " ，下载第 " + page + " 页数据");
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(page);
        pageHelper.setRows(DEFAULT_LOAD_COUNTS);
        getGankApi().getSpecifyDep(id ,type, pageHelper).enqueue(new Callback<GankResEntity>() {
            @Override
            public void onResponse(Call<GankResEntity> call, Response<GankResEntity> response) {
                if (response.isSuccessful()) {
                    LogUtils.d(TAG, type + "动态下载成功： " + response.body().toString());
                    Object results = response.body().getResults();
                    Type t = new TypeToken<List<DevelopmentDto>>() {
                    }.getType();
                    List<DevelopmentDto> data = sGson.fromJson(sGson.toJson(results), t);
                    callback.onSuccess(data);

                } else {
                    //获取数据失败，可能是 404 之类的原因
                    LogUtils.e(TAG, type + "动态下载失败，code： " + response.code());
                    callback.onError("请求失败，code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GankResEntity> call, Throwable t) {
                //可能是网络问题，请求发送失败
                LogUtils.e(TAG,type + "动态下载失败", t);
                callback.onError(t.getMessage());
            }
        });
    }


    private static class GankApiSingleton {
        private static GankApi mInstance = RetrofitHelper.newRetrofit(BuildConfig.GANK_SERVICE).create(GankApi.class);
    }

}
