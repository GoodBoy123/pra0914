package yxy.pra0914.order;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import yxy.pra0914.dto.PageHelper;

/**
 * Created by dasu on 2016/8/25.
 *
 * gank.io 的 api
 * 干货包括：github开源项目、技术文章、视频、图片等
 */
interface GankApi {


    /**
     * 普通用户id
     * 数据类型： 进行中 | 已取消 | 已完成 |
     * pageHelper,存储第几页及显示条数
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("userOrder/getOrders/{userId}/{type}.html")
    Call<GankResEntity> getSpecifyGanHuo(@Path("userId") int userId, @Path("type") String type, @Body PageHelper pageHelper);


}
