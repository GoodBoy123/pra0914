package yxy.pra0914.base;

import android.app.Application;
import android.content.IntentFilter;

import yxy.pra0914.network.NetBroadcastReceiver;

/**
 * Created by Teprinciple on 2016/11/11.
 */
public class BaseApplication extends Application {

    private static BaseApplication mApplication;
    private static int userId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        registerNetStateListener();

    }

    public static int getUserId(){return userId;}

    public static BaseApplication getIntance() {
        return mApplication;
    }
//////////////////////////////
    private static final String TAG = BaseApplication.class.getSimpleName();

    private NetBroadcastReceiver mNetBroadcastReceiver;



    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterNetStateListener();
    }


    private void registerNetStateListener() {
        IntentFilter filter = new IntentFilter(NetBroadcastReceiver.NET_CHANGED_ACTION);
        mNetBroadcastReceiver = new NetBroadcastReceiver();
        registerReceiver(mNetBroadcastReceiver, filter);
    }

    private void unregisterNetStateListener() {
        if (mNetBroadcastReceiver != null) {
            unregisterReceiver(mNetBroadcastReceiver);
            mNetBroadcastReceiver = null;
        }
    }

}
