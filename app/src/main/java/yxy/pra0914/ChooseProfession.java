package yxy.pra0914;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import yxy.pra0914.adapter.MyBaseAdapter;
import yxy.pra0914.base.BaseActivity;

public class ChooseProfession extends BaseActivity {

    private ListView listView;
    String[] strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_profession);
        listView = (ListView) findViewById(R.id.listView);
        strings = new String[]{"互联网-软件","通信-硬件","房地产-建筑","文化传媒","金融类","消费品","汽车-机械","教育-翻译",
                "贸易-物流","生物-医疗","能源-化工","政府机构","服务业","其他行业"};
        //接收从InfoEdite传递的数据
        Intent intent = getIntent();
        String profession = "其他行业";
        if (intent != null) {
            profession = intent.getStringExtra("profession");
        }
        MyBaseAdapter mBaseAdapter = new MyBaseAdapter(getApplicationContext(),strings,profession);
        listView.setAdapter(mBaseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent();
                i.putExtra("profession", strings[position].toString());
                setResult(5,i);
                finish();
            }
        });

    }
}
