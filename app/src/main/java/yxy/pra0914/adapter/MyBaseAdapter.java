package yxy.pra0914.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import yxy.pra0914.R;

/**
 * Created by YXY on 2017/9/19.
 */

public class MyBaseAdapter extends BaseAdapter {

    private String[] data;
    private Context mContext;
    private String checkedStr;
    public MyBaseAdapter(Context mContext, String[] data,String checkedStr) {
        super();
        this.mContext = mContext;
        this.data = data;
        this.checkedStr = checkedStr;
    }

    @Override
    public int getCount() {
        return data.length;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choose_profession_item, null);
            holder = new ViewHolder();
            holder.txv_profession = (TextView) convertView.findViewById(R.id.txv_profession);
            holder.txv_checked = (TextView) convertView.findViewById(R.id.txv_checked);

            convertView.setTag(holder);
        }    else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txv_profession.setText(data[position]);
        if(checkedStr != null && checkedStr.equals( holder.txv_profession.getText().toString()))
        {
            holder.txv_checked.setVisibility(View.VISIBLE);
        }else{
            holder.txv_checked.setVisibility(View.GONE);
        }
        return convertView;

    }
    static class ViewHolder{
        TextView txv_profession;
        TextView txv_checked;
    }
}

