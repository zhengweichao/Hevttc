package top.vchao.hevttc.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;
import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.MyPagerAdapter;
import top.vchao.hevttc.constant.Config;

/**
 * @ 创建时间: 2017/9/21 on 19:39.
 * @ 描述：新闻页面fragment
 * @ 作者: vchao
 */
public class TabNewsFragment extends BaseFragment {

    @BindView(R.id.vp_news)
    ViewPager mViewPager;
    @BindView(R.id.cootablayout_news)
    CoordinatorTabLayout mCoordinatorTabLayout;

    private final String[] mTitles = {Config.MODULE_NEWS_ONE, Config.MODULE_NEWS_TWO,
            Config.MODULE_NEWS_THREE, Config.MODULE_NEWS_FOUR};
    private ArrayList<Fragment> mFragments;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public void initData() {

        initFragments();
        initViewPager();

        int[] mImageArray = new int[]{
                R.mipmap.img_keshi1,
                R.mipmap.img_keshi4,
                R.mipmap.img_bg_news3,
                R.mipmap.img_bg_news4};
        int[] mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};

        mCoordinatorTabLayout.setTransulcentStatusBar(mActivity)
                .setTitle("")
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments, mTitles));
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(NewsListFragment.getInstance("a"));
        mFragments.add(NewsListFragment.getInstance("b"));
        mFragments.add(NewsListFragment.getInstance("c"));
        mFragments.add(NewsListFragment.getInstance("d"));
    }

}
