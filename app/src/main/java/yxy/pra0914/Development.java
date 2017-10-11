package yxy.pra0914;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import yxy.pra0914.base.BaseActivity;

public class Development extends BaseActivity implements View.OnClickListener{
    private TextView add,dep_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development);
        dep_back = (TextView)findViewById(R.id.dep_back);
        add = (TextView)findViewById(R.id.add);
        add.setOnClickListener(this);
        dep_back.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId()) {
            case R.id.add:
                intent = new Intent(Development.this,Add_development.class);
                startActivity(intent);
                break;
            case R.id.dep_back:
                finish();
                break;
        }
    }
}
