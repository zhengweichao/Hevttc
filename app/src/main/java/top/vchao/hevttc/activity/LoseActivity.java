package top.vchao.hevttc.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;
import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.MyPagerAdapter;
import top.vchao.hevttc.constant.Config;
import top.vchao.hevttc.fragment.FindLoserFragment;
import top.vchao.hevttc.fragment.FindThingFragment;

/**
 * @ 创建时间: 2017/10/3 on 10:03.
 * @ 描述：失物招领 + 寻物启事 页面
 * @ 作者: vchao
 */
public class LoseActivity extends BaseAppCompatActivity {
    @BindView(R.id.coordinatortablayout)
    CoordinatorTabLayout mCoordinatorTabLayout;
    @BindView(R.id.vp)
    ViewPager mViewPager;

    private final String[] mTitles = {Config.MODULE_NAME_LOSE, Config.MODULE_NAME_FIND};
    private ArrayList<Fragment> mFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lose;
    }

    @Override
    protected void initView() {
        initFragments();
        initViewPager();

        int[] mImageArray = new int[]{
                R.mipmap.img_bg_lose,
                R.mipmap.img_bg_find};
        int[] mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light};

        mCoordinatorTabLayout.setTransulcentStatusBar(this)
                .setTitle("失物招领")
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new FindLoserFragment());
        mFragments.add(new FindThingFragment());
    }

    @OnClick(R.id.iv_add_lose)
    public void onViewClicked() {
        Intent intent = new Intent(LoseActivity.this, LoseAddActivity.class);
        startActivity(intent);
    }
}