package yxy.pra0914.development;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import yxy.pra0914.Add_development;
import yxy.pra0914.R;
import yxy.pra0914.base.BaseActivity;



/**
 * Created by dasu on 2017/4/14.
 *
 * 分类浏览的主界面，该类只负责管理多个Fragment，具体的ui和mode交互交由相应的Fragment和controller负责
 */

public class DevelopmentActivity extends BaseActivity implements OnSwipeRefreshListener,View.OnClickListener {
    private TextView add,dep_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep);
        initVariable();
        initView();
        dep_back = (TextView)findViewById(R.id.dep_back);
        add = (TextView)findViewById(R.id.add);
        add.setOnClickListener(this);
        dep_back.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private yxy.pra0914.development.CategoryAController mCategoryController;
    private CategoryPagerAdapter mPagerAdapter;

    private void initVariable() {
        mCategoryController = new CategoryAController(this);
        mPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), mCategoryController.getCategoryList());
    }

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView mBackBtn;
    private SwipeRefreshLayout mRefreshLayout;

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.layout_category_title);
        mViewPager = (ViewPager) findViewById(R.id.vp_category_content);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_category_content);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.red),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.pink));
        mRefreshLayout.setOnRefreshListener(onPullDownRefresh());
    }

    private SwipeRefreshLayout.OnRefreshListener onPullDownRefresh() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCurRefreshFragment().retryLoadData();
            }
        };
    }

    private CategoryFragment getCurRefreshFragment() {
        return (CategoryFragment) mPagerAdapter.getCurrentFragment();
    }

    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId()) {
            case R.id.add:
                intent = new Intent(DevelopmentActivity.this,Add_development.class);
                startActivity(intent);
                break;
            case R.id.dep_back:
                finish();
                break;
        }
    }



    @Override
    public void onRefreshing() {
        if (mRefreshLayout != null && !mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onRefreshFinish() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }
}
