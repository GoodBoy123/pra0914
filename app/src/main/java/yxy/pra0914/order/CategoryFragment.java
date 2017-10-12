package yxy.pra0914.order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import yxy.pra0914.MyView.recyclerview.LoadMoreRecyclerView;
import yxy.pra0914.MyView.recyclerview.OnPullUpRefreshListener;
import yxy.pra0914.R;
import yxy.pra0914.dto.UserOrderDto;
import yxy.pra0914.utils.ToastUtil;
import yxy.pra0914.utils.ToastUtils;

/**
 * Created by dasu on 2017/4/20.
 *
 * 各类型干货的数据展示界面，
 */

public class CategoryFragment extends BaseFragment implements ICategoryController {
    private static final String TAG = CategoryFragment.class.getSimpleName();

    @Override
    public String getCategoryType() {
        Bundle bundle = getArguments();
        return bundle.getString("_type", "");
    }

    public CategoryFragment() {
    }

    //刷新的状态
    private static final int STATE_REFRESHING = 1;
    private static final int STATE_REFRESH_FINISH = 2;
    private int mRefreshState = STATE_REFRESH_FINISH;

    private CategoryFController mCategoryController;
    private Context mContext;
    private OnSwipeRefreshListener mRefreshListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnSwipeRefreshListener) {
            mRefreshListener = (OnSwipeRefreshListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initView(view);
        return view;
    }

    private List<UserOrderDto> mUserOrderList = new ArrayList<>();

    private LoadMoreRecyclerView mCategoryRv;
    private CategoryRecycleAdapter mRecycleAdapter;

    private void initView(View view) {
        mCategoryRv = (LoadMoreRecyclerView) view.findViewById(R.id.rv_category_content);
        mCategoryRv.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycleAdapter = new CategoryRecycleAdapter(mUserOrderList);
        mRecycleAdapter.setOnItemClickListener(onItemClick());
        mCategoryRv.setAdapter(mRecycleAdapter);
        mCategoryRv.setOnPullUpRefreshListener(onPullUpRefresh());
    }

    private OnPullUpRefreshListener onPullUpRefresh() {
        return new OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                //正在刷新的话，就不加载下拉刷新了
                if (mRefreshState == STATE_REFRESHING) {
                    return;
                }
                mRefreshState = STATE_REFRESHING;
                mRefreshListener.onRefreshing();
                mCategoryController.startPullUpRefresh();
            }
        };
    }

    private OnItemClickListener<UserOrderDto> onItemClick() {
        return new OnItemClickListener<UserOrderDto>() {
            @Override
            public void onImageClick(List<String> imgUrls) {
                ImageActivity.startActivity(mContext, 0, (ArrayList<String>) imgUrls);
            }

            @Override
            public void onItemClick(UserOrderDto data) {
                Toast.makeText(getContext(), "点击了一个item",Toast.LENGTH_SHORT).show();
//                WebViewActivity.startActivity(mContext, data.getUrl(), data.getDesc());
            }
        };
    }

    private void notifyDataSetChanged() {
        if (mRecycleAdapter != null) {
            mRecycleAdapter.notifyDataSetChanged();
        }
    }

    public CategoryFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }

    @Override
    public void updateGanHuo(List<UserOrderDto> data) {
        mRefreshState = STATE_REFRESH_FINISH;
        mRefreshListener.onRefreshFinish();
        if (data == null || data.size() == 0) {
            return;
        }
        if (mUserOrderList == null) {
            mUserOrderList = new ArrayList<>();
        }
        mUserOrderList.clear();
        mUserOrderList.addAll(data);
        if (isFragmentVisible()) {
            notifyDataSetChanged();
        }
    }

    public void refreshData(List<UserOrderDto> data) {
        mRefreshState = STATE_REFRESH_FINISH;
        mRefreshListener.onRefreshFinish();
        if (mUserOrderList == null) {
            mUserOrderList = new ArrayList<>();
        }
        int oldSize = mUserOrderList.size();
        mUserOrderList.addAll(data);
        mRecycleAdapter.notifyItemRangeInserted(oldSize, data.size());
        ToastUtils.show(mContext, "加载成功，新增" + data.size() + "项订单数据！");
    }

    @Override
    public void onLoadFailed() {
        mRefreshState = STATE_REFRESH_FINISH;
        mRefreshListener.onRefreshFinish();
        ToastUtils.show(mContext, "数据加载失败，请重试");
    }

    /**
     * 重新加载数据
     */
    public void retryLoadData() {
        mRefreshState = STATE_REFRESHING;
        mCategoryController.loadBaseData();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            notifyDataSetChanged();
            if (mRefreshState == STATE_REFRESHING) {
                mRefreshListener.onRefreshing();
            }
        } else {
            mRefreshListener.onRefreshFinish();
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        mCategoryController = new CategoryFController(this);
        mRefreshState = STATE_REFRESHING;
        mCategoryController.loadBaseData();
    }
}
