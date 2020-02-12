package top.vchao.hevttc.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;
import top.vchao.hevttc.R;
import top.vchao.hevttc.constant.Config;
import top.vchao.hevttc.cootab.MyPagerAdapter;
import top.vchao.hevttc.fragment.SecondBuyFragment;
import top.vchao.hevttc.fragment.SecondSaleFragment;

public class BuyActivity extends BaseAppCompatActivity {

    @BindView(R.id.vp)
    ViewPager mViewPager;
    @BindView(R.id.coordinatortablayout)
    CoordinatorTabLayout mCoordinatorTabLayout;

    public String[] mTitles = {Config.MODULE_NAME_BUY, Config.MODULE_NAME_SALE};
    private ArrayList<Fragment> mFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lose;
    }

    @Override
    void initView() {
        initFragments();
        initViewPager();

        int[] mImageArray = new int[]{
                R.mipmap.img_bg_buy,
                R.mipmap.img_bg_sale};
        int[] mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light};

        mCoordinatorTabLayout.setTransulcentStatusBar(this)
                .setTitle("二手交易")
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new SecondBuyFragment());
        mFragments.add(new SecondSaleFragment());
    }

    @OnClick(R.id.iv_add_lose)
    public void onViewClicked() {
        Intent intent = new Intent(BuyActivity.this, BuyAddActivity.class);
        startActivity(intent);
    }
}