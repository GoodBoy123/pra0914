package yxy.pra0914;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yxy.pra0914.MyView.CircleImageView;
import yxy.pra0914.MyView.HomeToolbar;
import yxy.pra0914.adapter.InfoWinAdapter;

import yxy.pra0914.api.APIWrapper;
import yxy.pra0914.base.BaseApplication;
import yxy.pra0914.bean.HttpResponse;
import yxy.pra0914.development.DevelopmentActivity;
import yxy.pra0914.dto.MainOrderDto;
import yxy.pra0914.dto.TestUser;
import yxy.pra0914.dto.User;
import yxy.pra0914.order.UserOrderActivity;
import yxy.pra0914.utils.TLog;
import yxy.pra0914.utils.ToastUtil;

public class MainActivity extends CheckPermissionsActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener, AMap.OnMarkerClickListener,AMap.OnMapClickListener{

    private TextView txt_city,start_place,goods_info,receiverInfo,cost_line , cost , end_infomation;
    private RelativeLayout choose_GoodsInf;
    private LinearLayout lay_cost,find,talk;
    private CircleImageView circleImageView;
    private View myMakerView;
    //城市
    private static final int REQUESTCODE_FINDCITY = 6;
    //物品信息
    private static final int REQUESTCODE_GOODSINFO = 7;
    //收件人
    private static final int REQUESTCODE_RECEIVERINFO = 8;

    private MapView mapView;
    private AMap aMap;
    private InfoWinAdapter adapter;
    private Marker oldMarker;
    private Marker newMarker;
    private GeocodeSearch geocodeSearch;


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private String newMarker_title = "暂无数据";
    private String newMarker_snippet = "暂无数据";
    private User userToEditInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeToolbar toolbar = (HomeToolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //点击头像跳往个人信息的编辑
        circleImageView = (CircleImageView)navigationView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.imageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent forEdite = new Intent(getApplicationContext(),InfoEdite.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userInfo" , userToEditInfo);
                    forEdite.putExtras(bundle);
                    startActivity(forEdite);
                }
            });

        initView();
        //点击主页上方的城市，跳往选择城市
        txt_city.setOnClickListener(this);
        //选择收件人信息
        receiverInfo.setOnClickListener(this);
        //选择物品信息
        choose_GoodsInf.setOnClickListener(this);
        //查看动态
        find.setOnClickListener(this);

        if(aMap == null)
        {
            aMap = mapView.getMap();
            CameraUpdate cu = CameraUpdateFactory.zoomTo(16);
            aMap.moveCamera(cu);
        }
        mapView.onCreate(savedInstanceState);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);

        // 定义 Marker拖拽的监听
        AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {

            // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDragStart(Marker arg0) {
                LatLng latLng = arg0.getPosition();
                CameraUpdate cu = CameraUpdateFactory.changeLatLng(latLng);
                aMap.moveCamera(cu);

            }

            // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(BaseApplication.getIntance().getBaseContext(), "Marker被拖动了", Toast.LENGTH_SHORT).show();

            }

            // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub

            }
        };
        // 绑定marker拖拽事件
        aMap.setOnMarkerDragListener(markerDragListener);

        //点击marker出现地点提示框
        adapter = new InfoWinAdapter();
        aMap.setInfoWindowAdapter(adapter);
        //初始化定位
        initLocation();

        //创建GeocodeSearch对象
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress addr = regeocodeResult.getRegeocodeAddress();
                newMarker_title = "";
                newMarker_snippet = addr.getFormatAddress();
                newMarker.setTitle(newMarker_title);
                newMarker.setSnippet(newMarker_snippet);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        startLocation();
        APIWrapper.getInstance().getUserInfo(String.valueOf(BaseApplication.getUserId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResponse<User>>() {
                    @Override
                    public void onCompleted() {
                        TLog.log("成功了？", "是的");
                    }

                    @Override
                    public void onError(Throwable e) {
                        TLog.error("onError " + e.toString());
                    }

                    @Override
                    public void onNext(HttpResponse<User> response) {
                        TLog.log("succeed",response.obj.getHeadimg());
                        userToEditInfo = response.obj;
                        if(response.obj.getHeadimg() != null)
                        {
                            Glide.with(MainActivity.this)
                                    .load(response.obj.getHeadimg())
                                    .asBitmap()
                                    .into(circleImageView);
                        }

                    }
                });
    }

    //初始化控件
    private void initView(){
        mapView = (MapView)findViewById(R.id.map);
        txt_city = (TextView)findViewById(R.id.txt_city);
        goods_info = (TextView)findViewById(R.id.goods_info);
        start_place = (TextView)findViewById(R.id.start_place);
        receiverInfo = (TextView)findViewById(R.id.end_infomation);
        choose_GoodsInf = (RelativeLayout)findViewById(R.id.choose_GoodsInf);
        lay_cost = (LinearLayout)findViewById(R.id.lay_cost);
        cost_line = (TextView)findViewById(R.id.cost_line);
        cost = (TextView)findViewById(R.id.cost);
        end_infomation = (TextView)findViewById(R.id.end_infomation) ;
        find = (LinearLayout)findViewById(R.id.find);
        talk = (LinearLayout)findViewById(R.id.talk);

    }

     /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(60000);//可选，设置定位间隔。默认为10秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(final AMapLocation location) {
            if (null != location) {

                StringBuffer city = new StringBuffer();
                String place = new String();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    city.append(location.getCity());
                    place = location.getAddress();
                    LatLng targetPos = new LatLng(location.getLatitude(),location.getLongitude());
                    CameraUpdate cu = CameraUpdateFactory.changeLatLng(targetPos);
                    aMap.moveCamera(cu);
                    aMap.clear();


                    addMarkerToMap(targetPos,location.getPoiName(),location.getAddress());
                } else {
                    //定位失败
                    city.append("定位失败");
                    Toast.makeText(BaseApplication.getIntance().getBaseContext(), "定位失败,请开启GPS,"+"错误码:" + location.getErrorCode(),Toast.LENGTH_SHORT).show();

                }
                //解析定位结果，
                String result = city.toString();
                txt_city.setText(result);
                start_place.setText(place);
            } else {
                txt_city.setText("定位失败，点击手动点位");
            }
        }
    };



    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(){
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //地图的点击事件

    public void onMapClick(LatLng latLng) {
        //点击地图上已经存在用户点击的marker ，则去除

        if(newMarker != null)
        {
            newMarker.remove();
        }

        if (oldMarker != null) {
            oldMarker.hideInfoWindow();
        }

        geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(latLng.latitude,latLng.longitude),20,GeocodeSearch.GPS));

        newMarker = aMap.addMarker(new MarkerOptions().position(latLng).title(newMarker_title).snippet(newMarker_snippet));
        start_place.setText(newMarker_snippet);



    }

    //maker的点击事件

    public boolean onMarkerClick(Marker marker) {
        if (marker != oldMarker) {
           return false;
        }
        oldMarker = marker;
        marker.setDraggable(true);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected));
        return false; //返回 “false”，除定义的操作之外，默认操作也将会被执行
    }

    private void addMarkerToMap(LatLng latLng, String title, String snippet) {
        Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(latLng)
                .draggable(true)
                .title(title)
                .snippet(snippet));

        myMakerView = LayoutInflater.from(this).inflate(
                R.layout.my_marker, null);

        marker.setIcon(BitmapDescriptorFactory.fromView(myMakerView));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_journey) {
            intent = new Intent(MainActivity.this,UserOrderActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_wallet) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.txt_city:
                intent = new Intent(MainActivity.this,ChooseCity.class);
                startActivityForResult(intent,REQUESTCODE_FINDCITY);
                break;
            case R.id.choose_GoodsInf:
                intent = new Intent(MainActivity.this,GoodsInfo.class);
                startActivityForResult(intent,REQUESTCODE_GOODSINFO);
                break;
            case R.id.end_infomation:
                intent = new Intent(MainActivity.this,ReceiverInfo.class);
                intent.putExtra("city",txt_city.getText().toString());
                intent.putExtra("now_place",start_place.getText().toString());
                startActivityForResult(intent,REQUESTCODE_RECEIVERINFO);
                break;
            case R.id.find:
                intent = new Intent(MainActivity.this,DevelopmentActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_FINDCITY:
                    if(data != null)
                    {
                        txt_city.setText(data.getExtras().get("city").toString());
                    }
                    break;
                case REQUESTCODE_GOODSINFO:
                    if(data != null && data.getExtras().get("goodsInfo") != null)
                    {
                        goods_info.setText(data.getExtras().get("goodsInfo").toString());
                    }
                    break;
                case REQUESTCODE_RECEIVERINFO:
                    if(data != null && data.getExtras().get("receiverInfo") != null)
                    {
                        receiverInfo.setText(data.getExtras().get("receiverInfo").toString());
                        lay_cost.setVisibility(View.VISIBLE);
                        final TextView xiadan = (TextView)findViewById(R.id.xiadan);
                        xiadan.setVisibility(View.VISIBLE);
                        cost_line.setVisibility(View.VISIBLE);
                        find.setOnClickListener(null);
                        talk.setOnClickListener(null);

                        xiadan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MainOrderDto mainOrderDto = new MainOrderDto();
                                mainOrderDto.setUid(BaseApplication.getUserId());
                                mainOrderDto.setGoods(goods_info.getText().toString());
                                mainOrderDto.setCost(Double.parseDouble(cost.getText().toString()));
                                mainOrderDto.setSendaddress(start_place.getText().toString());
                                mainOrderDto.setRecieveInfo(end_infomation.getText().toString());
                                APIWrapper.getInstance().doOrder(mainOrderDto)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<HttpResponse<String>>() {
                                            @Override
                                            public void onCompleted() {
                                                TLog.log("成功了？", "是的");
                                            }
                                            @Override
                                            public void onError(Throwable e) {
                                                TLog.error("onError " + e.toString());
                                            }
                                            @Override
                                            public void onNext(HttpResponse<String> response) {
                                                TLog.error("onNext " + response.msg);
                                                receiverInfo.setText("");
                                                lay_cost.setVisibility(View.GONE);
                                                xiadan.setVisibility(View.GONE);
                                                cost_line.setVisibility(View.GONE);
                                                find.setOnClickListener(MainActivity.this);
                                                talk.setOnClickListener(MainActivity.this);
                                                ToastUtil.show(BaseApplication.getIntance(),response.msg);


                                            }
                                        });
                            }
                        });

                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
