package yxy.pra0914.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yxy.pra0914.MyView.ScaleImageView;
import yxy.pra0914.MyView.recyclerview.LoadMoreRecycleAdapter;
import yxy.pra0914.R;
import yxy.pra0914.utils.TimeUtils;

/**
 * Created by dasu on 2017/4/20.
 *
 * Fragment中的RecyclerView的适配器，用于显示各干货数据
 */

class CategoryRecycleAdapter extends LoadMoreRecycleAdapter<CategoryRecycleAdapter.ViewHolder> {

    private List<GanHuoEntity> mDataList;
    private Context mContext;
    private OnItemClickListener<GanHuoEntity> mClickListener;

    CategoryRecycleAdapter(List<GanHuoEntity> data) {
        mDataList = data;
    }

    @Override
    public int getDataSize() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        final GanHuoEntity data = mDataList.get(position);
        holder.data = data;
        setDemoImage(holder.mDemoIv, data.getImages());
        holder.mTitleTv.setText(data.getDesc());
        String source = GanHuoHelper.getUrlSource(data.getUrl());
        holder.mSourceTv.setText((source.equals("其它来源") ? "个人博客" : source));
        holder.mAuthorTv.setText("© " + data.getWho());
        setDate(data.getPublishedAt(), holder.mDateTv);
        holder.mImgCountsTv.setText((data.getImages() == null ? "0" : data.getImages().size()) + "张");

    }

    private void setDemoImage(final ScaleImageView imageView, List<String> images) {
        Glide.with(mContext)
                .load(images != null ? images.get(0) : "http://192.168.1.101:8080/PictureManage/uploadImages/tom/2017-10-07/girl2.png")
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

    public void setOnItemClickListener(OnItemClickListener<GanHuoEntity> listener) {
        mClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ScaleImageView mDemoIv;
        TextView mTitleTv;
        TextView mAuthorTv;
        TextView mSourceTv;
        TextView mDateTv;
        ViewGroup mInfoLayout;
        TextView mImgCountsTv;
        GanHuoEntity data;

        ViewHolder(View itemView) {
            super(itemView);
            mDemoIv = (ScaleImageView) itemView.findViewById(R.id.iv_category_demo);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_category_title);
            mSourceTv = (TextView) itemView.findViewById(R.id.tv_category_source);
            mAuthorTv = (TextView) itemView.findViewById(R.id.tv_category_author);
            mDateTv = (TextView) itemView.findViewById(R.id.tv_category_date);
            mInfoLayout = (ViewGroup) itemView.findViewById(R.id.layout_category_info);
            mImgCountsTv = (TextView) itemView.findViewById(R.id.tv_category_img_count);
            mDemoIv.setOnClickListener(this);
            mInfoLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                if (v == mDemoIv) {
                    List<String> imgs = null;
                    if (data.getImages() != null && data.getImages().size() > 0) {
                        imgs = data.getImages();
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
