package yxy.pra0914.api;



import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;
import yxy.pra0914.bean.HttpResponse;
import yxy.pra0914.dto.MainOrderDto;
import yxy.pra0914.dto.OrderDetailDto;
import yxy.pra0914.dto.TestUser;
import yxy.pra0914.dto.User;


/**
 * 接口定义
 * @author lizhixian
 * @time 16/8/18 21:46
 */
public interface APIService {
    //服务器端获取userId,可以这样request.getParameter("userId")
    @Multipart
    @POST("picture/alterHeadImg.html")
    Observable<HttpResponse<List<String>>> alterHeadImg(@Part("userId") int userId, @PartMap Map<String, RequestBody> params);

    //获取用户个人信息
    @GET("user/getUserInfo/{userId}.html")
    Observable<HttpResponse<User>> getUserInfo(@Path("userId") String userId);

    //普通用户修改个人信息
    @POST("usermanage/updateuser.html")
    Observable<Map> updateuser(@Part("user") User user);

    //普通用户发布动态，文字加多张图片
    @Multipart
    @POST("depManage/addDep.html")
    Observable<Map> addDep(@Part("id") int id ,@Part("usertype") int usertype , @Part("content") String content , @PartMap Map<String, RequestBody> params);

    //获取订单信息
    @GET("userOrder/getOrderDetail/{oid}.html")
    Observable<HttpResponse<OrderDetailDto>> getOrderDetail(@Path("oid") String oid);

    //下单
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("userOrder/doOrder.html")
    Observable<HttpResponse<String>> doOrder(@Body MainOrderDto mainOrderDto);
}
