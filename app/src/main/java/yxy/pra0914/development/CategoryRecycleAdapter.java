package yxy.pra0914.development;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yxy.pra0914.MyView.CircleImageView;
import yxy.pra0914.MyView.ScaleImageView;
import yxy.pra0914.MyView.recyclerview.LoadMoreRecycleAdapter;
import yxy.pra0914.R;
import yxy.pra0914.dto.DevelopmentDto;

import yxy.pra0914.utils.TimeUtils;

/**
 * Created by dasu on 2017/4/20.
 *
 * Fragment中的RecyclerView的适配器，用于显示各干货数据
 */

class CategoryRecycleAdapter extends LoadMoreRecycleAdapter<CategoryRecycleAdapter.ViewHolder> {

    private List<DevelopmentDto> mDataList;
    private Context mContext;
    private OnItemClickListener<DevelopmentDto> mClickListener;

    CategoryRecycleAdapter(List<DevelopmentDto> data) {
        mDataList = data;
    }

    @Override
    public int getDataSize() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dep, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        final DevelopmentDto data = mDataList.get(position);
        holder.data = data;
        if(data.getNickname() != null)
            holder.dep_nickname.setText(data.getNickname());
        holder.dep_time.setText(data.getReleaseTime());
        if(data.getCity() != null)
            holder.dep_city.setText(data.getCity());
        holder.dep_content.setText(data.getContent());
        if(data.getImgs() != null)
        {
            setDemoImage(holder.iv_category_demo, data.getImgs());
            holder.tv_dep_img_count.setText("" + data.getImgs().size() + "张");
        }
        else{
            holder.imgs.setVisibility(View.GONE);
        }
        if(data.getHeadimg() != null)
        {
            Glide.with(mContext)
                    .load(data.getHeadimg())
                    .asBitmap()
                    .into(holder.headimg);
        }

    }

    private void setDemoImage(final ScaleImageView imageView, List<String> images) {
            Glide.with(mContext)
                    .load(images.get(0))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.img_loading_image)
                    .error(R.drawable.img_no_image)
                    .into(imageView);

    }

    private void setDate(Date date, TextView tv) {
        String time = TimeUtils.howLongAgo(TimeUtils.adjustDate(date));
        tv.setText(time);
    }

    public void setOnItemClickListener(OnItemClickListener<DevelopmentDto> listener) {
        mClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView headimg;
        TextView dep_nickname;
        TextView dep_time;
        TextView dep_city;
        TextView dep_content;
        LinearLayout imgs;
        ScaleImageView iv_category_demo;
        TextView tv_dep_img_count;
        DevelopmentDto data;

        ViewHolder(View itemView) {
            super(itemView);
            headimg = (CircleImageView)itemView.findViewById(R.id.imageView);
            dep_nickname = (TextView) itemView.findViewById(R.id.dep_nickname);
            dep_time = (TextView) itemView.findViewById(R.id.dep_time);
            dep_city = (TextView) itemView.findViewById(R.id.dep_city);
            dep_content = (TextView) itemView.findViewById(R.id.dep_content);
            imgs = (LinearLayout)itemView.findViewById(R.id.imgs);
            iv_category_demo = (ScaleImageView) itemView.findViewById(R.id.iv_category_demo);
            tv_dep_img_count = (TextView)itemView.findViewById(R.id.tv_dep_img_count);
            iv_category_demo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                if (v == iv_category_demo) {
                    List<String> imgs = null;
                    if (data.getImgs() != null && data.getImgs().size() > 0) {
                        imgs = data.getImgs();
                    } else {
                        imgs = new ArrayList<>();
                        imgs.add("http://192.168.1.101:8080/PictureManage/uploadImages/%E7%8E%8B%E8%80%85/2017-10-07/pexels-photo-295821.png");
                    }
                    mClickListener.onImageClick(imgs);
                } else {
                    mClickListener.onItemClick(data);

                }
            }
        }
    }

}
