package top.vchao.hevttc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;
import top.vchao.hevttc.R;
import top.vchao.hevttc.cootab.MyPagerAdapter;
import top.vchao.hevttc.fragment.BuyFragment1;
import top.vchao.hevttc.fragment.BuyFragment2;

public class BuyActivity extends AppCompatActivity {

    private CoordinatorTabLayout mCoordinatorTabLayout;
    private int[] mImageArray, mColorArray;
    private ViewPager mViewPager;
    private final String[] mTitles = {"淘点宝贝", "换点银子"};
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);
        ButterKnife.bind(this);

        initFragments();
        initViewPager();

        mImageArray = new int[]{
                R.mipmap.img_bg_buy,
                R.mipmap.img_bg_sale};
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light};

        mCoordinatorTabLayout = (CoordinatorTabLayout) findViewById(R.id.coordinatortablayout);
        mCoordinatorTabLayout.setTransulcentStatusBar(this)
                .setTitle("二手交易")
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(BuyFragment1.getInstance(mTitles[0]));
        mFragments.add(BuyFragment2.getInstance(mTitles[1]));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.iv_add_lose)
    public void onViewClicked() {
        Intent intent = new Intent(BuyActivity.this, BuyAddActivity.class);
        startActivity(intent);
    }
}
