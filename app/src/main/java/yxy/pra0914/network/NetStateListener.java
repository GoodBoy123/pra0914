package yxy.pra0914.network;

/**
 * Created by dasu on 2017/4/7.
 * https://github.com/woshidasusu/Meizi
 *
 * 手机网络状态改变时回调
 */

public interface NetStateListener {

    void onNetChanged(boolean isConnected);

}
