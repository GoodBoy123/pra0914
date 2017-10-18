package yxy.pra0914;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import yxy.pra0914.MyView.OptionsPickerView;
import yxy.pra0914.base.BaseActivity;
import yxy.pra0914.bean.CardBean;
import yxy.pra0914.listener.CustomListener;

public class GoodsInfo extends BaseActivity implements View.OnClickListener{


    private OptionsPickerView OptionsWeight,Optionsvalue;
    private ArrayList<CardBean> weights = new ArrayList<>();
    private ArrayList<CardBean> values = new ArrayList<>();
    private LinearLayout layout_weight,layout_value;
    private TextView weight,value,goods_type,serv_tip,goods_back,serv_type;
    private Button comfirm;
    private EditText goods_name;
    private ArrayList<RadioButton> btn_types = new ArrayList<RadioButton>();
    private RadioButton serv_send,serv_helpBuy;
    private int[] ids = new int[]{R.id.type1,R.id.type2,R.id.type3,R.id.type4,R.id.type5,R.id.type6,
            R.id.type7,R.id.type8,R.id.type9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_info);
        //等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃。
        getOptionData();
        initCustomOptionPicker();
        initView();
    }

    private void initView()
    {
        weight = (TextView) findViewById(R.id.weight);
        value = (TextView) findViewById(R.id.value);
        layout_weight = (LinearLayout) findViewById(R.id.layout_weight);
        layout_value = (LinearLayout) findViewById(R.id.layout_value);
        comfirm = (Button) findViewById(R.id.comfirm);
        goods_back = (TextView)findViewById(R.id.goods_back);
        goods_type = (TextView)findViewById(R.id.goods_type);
        serv_send = (RadioButton)findViewById(R.id.serv_send);
        serv_helpBuy = (RadioButton)findViewById(R.id.serv_helpBuy);
        serv_tip = (TextView)findViewById(R.id.serv_tip);
        serv_type = (TextView)findViewById(R.id.serv_type);
        goods_name = (EditText)findViewById(R.id.goods_name);
        int i = 0;
        for(int k = 0 ; k < 9 ; k++)
        {
            RadioButton btn = new RadioButton(this);
            btn = (RadioButton)findViewById(ids[i]);
            btn.setOnClickListener(this);
            btn_types.add(btn);
            i++;
        }
        serv_send.setOnClickListener(this);
        serv_helpBuy.setOnClickListener(this);
        comfirm.setOnClickListener(this);
        goods_back.setOnClickListener(this);
        layout_weight.setOnClickListener(this);
        layout_value.setOnClickListener(this);
    }
    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        OptionsWeight = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = weights.get(options1).getPickerViewText();
                weight.setText(tx);
                weight.setTextColor(getResources().getColor(R.color.textBlack));
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OptionsWeight.returnData();
                                OptionsWeight.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OptionsWeight.dismiss();
                            }
                        });
                    }
                })
                .isDialog(true)
                .build();

        OptionsWeight.setPicker(weights);//添加数据


        Optionsvalue = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = values.get(options1).getPickerViewText();
                value.setText(tx);
                value.setTextColor(getResources().getColor(R.color.textBlack));
            }
        })

                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Optionsvalue.returnData();
                                Optionsvalue.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Optionsvalue.dismiss();
                            }
                        });


                    }
                })
                .isDialog(true)
                .build();

        Optionsvalue.setPicker(values);//添加数据


    }
    private void getOptionData() {

        /**
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        getCardData();
    }
    private void getCardData() {
        for(int j = 1 ; j <= 50 ; j++)
        {
            weights.add(new CardBean(j, j + "斤"));
        }
        int startValue = 0;
        int skipOne = 10;
        int skipTwo = 30;
        for(int i = 1 ; i <= 20 ; i++)
        {
            if(i <= 10)
                startValue = startValue + skipOne;
            if(i > 10)
                startValue = startValue + skipTwo;
            values.add(new CardBean(i,String.valueOf(startValue) + "元"));
        }
    }
    public void onClick(View v) {
        int index = 0;
        for(int temp : ids)
        {
            if(v.getId() == temp)
            {
             break;
            }
            index++;
        }

        if(index < 9)
        {
            int count = 0;
            for(RadioButton button : btn_types)
            {
                if(count == index)
                {
                    button.setTextColor(getResources().getColor(R.color.buttonChecked));
                    button.setBackground(getResources().getDrawable(R.drawable.radio_focus));
                    goods_type.setText(button.getText().toString());
                }else{
                    button.setTextColor(getResources().getColor(R.color.textTipColor));
                    button.setBackground(getResources().getDrawable(R.drawable.radio_unfocus));
                }
                count++;
            }
        }
        switch (v.getId()) {
            case R.id.goods_back:
                finish();
                break;
            case R.id.layout_weight:
                OptionsWeight.show();
                break;
            case R.id.layout_value:
                Optionsvalue.show();
                break;
            case R.id.comfirm:
                Intent i=new Intent();
                //服务类型/物品类型/物品价值/物品重量
                String str = serv_type.getText() + "/" + goods_type.getText() + "/" + goods_name.getText() + "/"
                        + value.getText() + "/" +weight.getText()  ;
                Toast.makeText(GoodsInfo.this, str, Toast.LENGTH_SHORT).show();
                i.putExtra("goodsInfo", str);
                setResult(RESULT_OK,i);
                finish();
                break;
            case R.id.serv_send:
                serv_send.setTextColor(getResources().getColor(R.color.buttonChecked));
                serv_send.setBackground(getResources().getDrawable(R.drawable.radio_focus));
                serv_helpBuy.setTextColor(getResources().getColor(R.color.textTipColor));
                serv_helpBuy.setBackground(getResources().getDrawable(R.drawable.radio_unfocus));
                serv_tip.setVisibility(View.INVISIBLE);
                serv_type.setText(serv_send.getText().toString());
                break;
            case R.id.serv_helpBuy:
                serv_send.setTextColor(getResources().getColor(R.color.textTipColor));
                serv_send.setBackground(getResources().getDrawable(R.drawable.radio_unfocus));
                serv_helpBuy.setTextColor(getResources().getColor(R.color.buttonChecked));
                serv_helpBuy.setBackground(getResources().getDrawable(R.drawable.radio_focus));
                serv_tip.setVisibility(View.VISIBLE);
                serv_type.setText(serv_helpBuy.getText().toString());
                break;
        }
    }

}
