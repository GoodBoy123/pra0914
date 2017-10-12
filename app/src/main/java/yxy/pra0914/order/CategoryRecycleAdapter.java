package yxy.pra0914.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.text.DateFormat;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import yxy.pra0914.MyView.recyclerview.LoadMoreRecycleAdapter;
import yxy.pra0914.R;
import yxy.pra0914.dto.UserOrderDto;
import yxy.pra0914.utils.TimeUtils;

/**
 * Created by dasu on 2017/4/20.
 *
 * Fragment中的RecyclerView的适配器，用于显示各干货数据
 */

class CategoryRecycleAdapter extends LoadMoreRecycleAdapter<CategoryRecycleAdapter.ViewHolder> {

    private List<UserOrderDto> mDataList;
    private Context mContext;
    private OnItemClickListener<UserOrderDto> mClickListener;

    CategoryRecycleAdapter(List<UserOrderDto> data) {
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
        final UserOrderDto data = mDataList.get(position);
        holder.data = data;
        holder.mReleaseTime.setText(data.getReleaseTime());
        holder.mSendAddress.setText(data.getSendaddress());
        holder.mAddress.setText(data.getAddress());


    }

//    private void setDemoImage(final ScaleImageView imageView, List<String> images) {
//        Glide.with(mContext)
//                .load(images != null ? images.get(0) : "http://192.168.1.101:8080/PictureManage/uploadImages/tom/2017-10-07/girl2.png")
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.img_loading_image)
//                .error(R.drawable.img_no_image)
//                .into(imageView);
//    }

    private void setDate(Date date, TextView tv) {
        String time = TimeUtils.howLongAgo(TimeUtils.adjustDate(date));
        tv.setText(time);
    }

    public void setOnItemClickListener(OnItemClickListener<UserOrderDto> listener) {
        mClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout linearLayout;
        TextView mReleaseTime;
        TextView mSendAddress;
        TextView mAddress;
        UserOrderDto data;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.order);
            mReleaseTime = (TextView) itemView.findViewById(R.id.releasetime);
            mSendAddress = (TextView) itemView.findViewById(R.id.sendaddress);
            mAddress = (TextView) itemView.findViewById(R.id.address);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
//                if (v == mDemoIv) {
//                    List<String> imgs = null;
//                    if (data.getImages() != null && data.getImages().size() > 0) {
//                        imgs = data.getImages();
//                    } else {
//                        imgs = new ArrayList<>();
//                        imgs.add("http://192.168.1.101:8080/PictureManage/uploadImages/%E7%8E%8B%E8%80%85/2017-10-07/pexels-photo-295821.png");
//                    }
//                    mClickListener.onImageClick(imgs);
//                } else {
                mClickListener.onItemClick(data);

            }
        }
    }

}
