package top.vchao.hevttc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ 创建时间: 2017/5/14 on 14:20.
 * @ 描述：BaseFragment 基类
 * @ 作者: vchao
 */
public abstract class BaseFragment extends Fragment {

    public Activity mActivity;
    protected boolean isVisible; // Fragment当前状态是否可见
    protected View mView;

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getPreIntent();
        if (mView == null) {
            mView = View.inflate(mActivity, getLayoutId(), null);
        }
        unbinder = ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    /**
     * @return 布局文件id
     */
    public abstract int getLayoutId();

    /**
     * 获取 上一个页面 传的数据
     */
    public void getPreIntent() {
    }

    //view的初始化
    protected void initView() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    /**
     * 初始化监听器
     */
    public void initListener() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    /**
     * 延迟加载
     */
    protected void lazyLoad() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            unbinder.unbind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
