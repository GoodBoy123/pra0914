package yxy.pra0914;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yxy.pra0914.api.APIWrapper;
import yxy.pra0914.bean.HttpResponse;
import yxy.pra0914.dto.OrderDetailDto;
import yxy.pra0914.utils.TLog;

public class OrderDetailDone extends AppCompatActivity {

    private TextView express_name,cost,actual_pay,consignee,goodsName,goods_detail,address,con_tel,order_no,releasetime,
            startTime,endTime,receive_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_done);
        initView();
        Bundle bundle = getIntent().getExtras();
        String oid = String.valueOf(bundle.get("oid"));
        APIWrapper.getInstance().getOrderDetail(oid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResponse<OrderDetailDto>>() {
                    @Override
                    public void onCompleted() {
                        TLog.log("成功了？", "是的");

                    }

                    @Override
                    public void onError(Throwable e) {
                        TLog.error("onError " + e.toString());
                    }

                    @Override
                    public void onNext(HttpResponse<OrderDetailDto> response) {
                        TLog.log("onNext " + response.obj);
                        OrderDetailDto orderDetailDto = response.obj;
                        express_name.setText(orderDetailDto.getExpresssName().substring(0,1) + "师傅");
                        cost.setText(orderDetailDto.getCost() + "￥");
                        actual_pay.setText(orderDetailDto.getCost() + "￥");
                        consignee.setText(orderDetailDto.getConsignee());
                        goodsName.setText(orderDetailDto.getGoodsName());
                        goods_detail.setText(orderDetailDto.getGoodsDetail());
                        address.setText(orderDetailDto.getAddress());
                        con_tel.setText(orderDetailDto.getConTel());
                        order_no.setText(orderDetailDto.getNo());
                        releasetime.setText(orderDetailDto.getReleaseTime());
                        startTime.setText(orderDetailDto.getStartTime());
                        endTime.setText(orderDetailDto.getEndTime());
                        receive_code.setText(orderDetailDto.getCode());

                    }
                });
    }

    public void initView()
    {
        express_name = (TextView)findViewById(R.id.express_name);
        cost = (TextView)findViewById(R.id.cost);
        actual_pay = (TextView)findViewById(R.id.actual_pay);
        consignee = (TextView)findViewById(R.id.consignee);
        goodsName = (TextView)findViewById(R.id.goodsName);
        goods_detail = (TextView)findViewById(R.id.goods_detail);
        address = (TextView)findViewById(R.id.address);
        con_tel = (TextView)findViewById(R.id.con_tel);
        order_no = (TextView)findViewById(R.id.order_no);
        releasetime = (TextView)findViewById(R.id.releasetime);
        endTime = (TextView)findViewById(R.id.endTime);
        startTime = (TextView)findViewById(R.id.startTime);
        receive_code = (TextView)findViewById(R.id.receive_code);
    }
}
