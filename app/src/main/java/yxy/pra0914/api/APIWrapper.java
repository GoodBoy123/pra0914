package yxy.pra0914.api;



import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;
import yxy.pra0914.bean.HttpResponse;
import yxy.pra0914.dto.User;
import yxy.pra0914.utils.RetrofitUtil;


public class APIWrapper extends RetrofitUtil {

    private static APIWrapper mAPIWrapper;

    public APIWrapper() {
    }

    public static APIWrapper getInstance() {
        if (mAPIWrapper == null) {
            mAPIWrapper = new APIWrapper();
        }
        return mAPIWrapper;
    }

    public Observable<HttpResponse<List<String>>> alterHeadImg(int userId, Map<String,RequestBody> params) {
        return getAPIService().alterHeadImg(userId, params);
    }

    public Observable<HttpResponse<User>> getUserInfo(String userId) {
        return getAPIService().getUserInfo(userId);
    }

    public Observable<Map> updateuser( User user) {
        return getAPIService().updateuser(user);
    }

    public Observable<Map> addDep(int userId,int usertype , String content , Map<String,RequestBody> params) {
        return getAPIService().addDep(userId ,usertype , content , params);
    }
}
