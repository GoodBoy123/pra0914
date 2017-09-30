package yxy.pra0914.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import yxy.pra0914.R;
import yxy.pra0914.base.BaseApplication;


/**
 * Created by Teprinciple on 2016/8/23.
 * 地图上自定义的infowindow的适配器
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private Context mContext = BaseApplication.getIntance().getBaseContext();
    private LatLng latLng;
    private TextView comfirm;
    private TextView nameTV;
    private String agentName;
    private TextView addrTV;
    private String snippet;
    private Marker marker;

    @Override
    public View getInfoWindow(Marker marker) {
        this.marker = marker;
        initData(marker);
        View view = initView();
        return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        snippet = marker.getSnippet();
        agentName = marker.getTitle();
    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_infowindow, null);
        comfirm = (TextView) view.findViewById(R.id.comfirm);
        nameTV = (TextView) view.findViewById(R.id.agent_name);
        addrTV = (TextView) view.findViewById(R.id.agent_addr);

        nameTV.setText(agentName);
        addrTV.setText(snippet);

        comfirm.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.comfirm:  //点击确定
                marker.hideInfoWindow();
                break;

        }
    }

}
