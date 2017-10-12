package yxy.pra0914.order;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import yxy.pra0914.utils.LogUtils;

/**
 * Created by dasu on 2017/4/20.
 *
 * CategoryActivity对应的Controller
 */
public class CategoryAController {
    private static final String TAG = CategoryAController.class.getSimpleName();

    private Context mContext;
    private UserOrderActivity mUserOrderActivity;
    //tab 标签页
    private List<String> mCategoryList;

    public CategoryAController(Context context) {
        if (!(context instanceof UserOrderActivity)) {
            LogUtils.e(TAG, TAG + "绑定错误的Activity");
            throw new UnsupportedOperationException(TAG + "绑定错误的Activity");
        }
        mContext = context;
        mUserOrderActivity = (UserOrderActivity) context;
        initVariable();
    }

    private void initVariable() {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(GankController.TYPE_UNFINISHED);
        mCategoryList.add(GankController.TYPE_CANCELED);
        mCategoryList.add(GankController.TYPE_FINISHED);

    }

    public List<String> getCategoryList() {
        if (mCategoryList == null) {
            mCategoryList = new ArrayList<>();
        }
        return mCategoryList;
    }

}
