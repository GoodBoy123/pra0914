package yxy.pra0914.order;

import java.util.List;

/**
 * Created by suxq on 2017/4/14.
 *
 * Gank.io 接口返回的json数据对象，results会根据不同接口有不同的形态
 * 所以用 {@link GankController} 对Retrofit再做一层封装，对results做不同的处理
 */

public class GankResEntity {

    private String msg;
    private boolean success;
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return success;
    }

    public void setError(boolean error) {
        this.success = error;
    }

    public Object getResults() {
        return data;
    }

    public void setResults(Object results) {
        this.data = results;
    }

    @Override
    public String toString() {
        return "GankResEntity{" +
                "msg=" + msg +
                ", error=" + success +
                ", results='" + data + '\'' +
                '}';
    }
}
