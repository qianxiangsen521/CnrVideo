package com.cnr.basemodule.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnr.basemodule.R;
import com.cnr.basemodule.activity.BaseActivity;
import com.cnr.basemodule.http.HttpDataListener;
import com.cnr.basemodule.response.BaseResponse;
import com.cnr.basemodule.utils.MSConfig;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by qianxiangsen on 18-5-21.
 */

public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,HttpDataListener<BaseResponse>, View.OnTouchListener{

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initEventAndData(Bundle savedInstanceState);

    protected abstract void requestData();
    protected Context mContext;

    protected View mView;
    protected BaseActivity mActivity;
    protected Unbinder mUnBinder;

    protected final static int START_PAGE_INDEX = MSConfig.COMMON_PAGE_START;
    protected final static int PAGE_NUM_COUNT = MSConfig.COMMON_PAGE_NUM;
    /**
     * 请求参数，请求数据的索引
     */
    protected int commonParam_fetchDataIndex = START_PAGE_INDEX;
    /**
     * 请求参数，每页取的数据量
     */
    protected int commonParam_pageNumCount = PAGE_NUM_COUNT;
    @Override
    public void onAttach(Context context) {
        mActivity = (BaseActivity) context;
        mContext = context;
        super.onAttach(context);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //防止点击穿透
        mView.setOnTouchListener(this);
        if (getSwipeRefreshLayout() != null){
            onRefresh();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        initEventAndData(savedInstanceState);
        requestData();
        if (getSwipeRefreshLayout() != null) {
            getSwipeRefreshLayout().setOnRefreshListener(this);
        }

    }

    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return null;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(BaseResponse baseResponse) {
    }

    @Override
    public void onError(Throwable t) {
        if (getSwipeRefreshLayout() != null) {
            getSwipeRefreshLayout().setRefreshing(false);
        }
    }

    @Override
    public void onCompleted() {
        if (getSwipeRefreshLayout() != null) {
            getSwipeRefreshLayout().setRefreshing(false);
        }

    }
    protected View getHeaderView(int layout) {
        View view = getLayoutInflater().inflate(layout, null);
        return view;
    }

    public void addFragment(int fragment_full, BaseFragment fragments) {

        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.a_to_b_of_in,
                R.anim.a_back_b_of_out, R.anim.a_to_b_of_in,
                R.anim.a_back_b_of_out);
        ft.replace(fragment_full, fragments);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null)
                .commit();
    }
    public void removeFragment(BaseFragment welcomeFragment){
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(welcomeFragment);
        transaction.commit();
    }
    protected void removeFragment() {
        if (mActivity.getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            mActivity.getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * 跳转到指定的Activity
     *
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(@NonNull Class<?> targetActivity) {

        Intent intent = new Intent(mActivity, targetActivity);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param flags          intent flags
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(int flags, @NonNull Class<?> targetActivity) {
        final Intent intent = new Intent(mActivity, targetActivity);
        intent.setFlags(flags);
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param data           Activity之间传递数据，Intent的Extra key为Constant.EXTRA_NAME.DATA
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(@NonNull Bundle data, @NonNull Class<?> targetActivity) {
        final Intent intent = new Intent();
        if (data != null) {
            intent.putExtras(data);
            intent.setClass(mActivity, targetActivity);
            startActivity(intent);
        }
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的String类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull String extraValue, @NonNull Class<?> targetActivity) {
        if (!TextUtils.isEmpty(extraName)) {
            final Intent intent = new Intent(mActivity, targetActivity);
            intent.putExtra(extraName, extraValue);
            startActivity(intent);
        } else {
            throw new NullPointerException("传递的值的键名称为null或空");
        }
    }

    public final void startActivity(@NonNull String extraName,int extraValue, @NonNull Class<?> targetActivity) {
        if (!TextUtils.isEmpty(extraName)) {
            final Intent intent = new Intent(mActivity, targetActivity);
            intent.putExtra(extraName, extraValue);
            startActivity(intent);
        } else {
            throw new NullPointerException("传递的值的键名称为null或空");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
