package yxy.pra0914;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;

import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;


import java.util.ArrayList;
import java.util.List;

import yxy.pra0914.adapter.InfoWinAdapter;
import yxy.pra0914.poisearch.PoiOverlay;
import yxy.pra0914.utils.AMapUtil;
import yxy.pra0914.utils.ToastUtil;


/**
 * AMapV1地图中简单介绍poisearch搜索
 */
public class ReceiverInfo extends FragmentActivity implements
        OnMarkerClickListener, TextWatcher,
        OnPoiSearchListener, OnClickListener, InputtipsListener {
    private AMap aMap;
    private String city = "北京";
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private TextView key_search,back,receiver_done;
    private InfoWinAdapter adapter;
    private EditText receiver_name,receiver_phone,address_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_info);
        Intent intent = getIntent();
        if (intent != null) {
            city = intent.getStringExtra("city");
            keyWord = intent.getStringExtra("now_place");
        }
        init();
        doSearchQuery();

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            setUpMap();
        }
        receiver_name = (EditText) findViewById(R.id.receiver_name);
        receiver_phone = (EditText)findViewById(R.id.receiver_phone);
        address_detail = (EditText)findViewById(R.id.address_detail);
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {
        key_search = (TextView) findViewById(R.id.key_search);
        key_search.setOnClickListener(this);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.keyWord);
        autoCompleteTextView.addTextChangedListener(this);// 添加文本输入框监听事件
        back = (TextView)findViewById(R.id.receiver_back);
        back.setOnClickListener(this);
        receiver_done = (TextView)findViewById(R.id.receiver_done);
        receiver_done.setOnClickListener(this);
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        //点击marker出现地点提示框
        adapter = new InfoWinAdapter();
        aMap.setInfoWindowAdapter(adapter);
    }

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        keyWord = AMapUtil.checkEditText(autoCompleteTextView);
        if ("".equals(keyWord)) {
            ToastUtil.show(ReceiverInfo.this, "请输入搜索关键字");
            return;
        } else {
            doSearchQuery();
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(1);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }


    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());

        }

    }


    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(ReceiverInfo.this, infomation);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, city);
            Inputtips inputTips = new Inputtips(ReceiverInfo.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }


    /**

     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(ReceiverInfo.this,
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(ReceiverInfo.this,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 点击搜索按钮
             */
            case R.id.key_search:
                searchButton();
                break;
            case R.id.receiver_back:
                finish();
                break;
            case R.id.receiver_done:
                if(receiver_name.getText().toString().equals("") || receiver_phone.getText().toString().equals("") ||
                        keyWord.toString().equals("") || address_detail.getText().toString().equals(""))
                {
                    ToastUtil.show(ReceiverInfo.this, "收货人信息不能为空");
                }else
                {
                    Intent i=new Intent();
                    //服务类型/物品类型/物品重量/物品价值
                    String str = receiver_name.getText().toString() + "/" + receiver_phone.getText().toString() + "/" +AMapUtil.checkEditText(autoCompleteTextView) + "/"
                            + address_detail.getText().toString();
                    Toast.makeText(ReceiverInfo.this, str, Toast.LENGTH_SHORT).show();
                    i.putExtra("receiver_name", receiver_name.getText().toString());
                    i.putExtra("receiver_phone", receiver_phone.getText().toString());
                    i.putExtra("keyWord", AMapUtil.checkEditText(autoCompleteTextView));
                    i.putExtra("address_detail", address_detail.getText().toString());
                    i.putExtra("receiverInfo", str);
                    setResult(RESULT_OK,i);
                    finish();
                }
                break;
//            /**
//             * 点击下一页按钮
//             */
//            case R.id.nextButton:
//                nextButton();
//                break;
            default:
                break;
        }
    }



    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }
}
