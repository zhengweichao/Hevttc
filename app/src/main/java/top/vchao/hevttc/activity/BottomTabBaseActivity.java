package top.vchao.hevttc.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

import butterknife.BindView;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.LightStatusBarUtils;
import top.vchao.hevttc.view.BottomTabView;

/**
 * @ 创建时间: 2017/5/21 on 12:38.
 * @ 描述：底部标签页面基类
 * @ 作者: vchao
 */
public abstract class BottomTabBaseActivity extends BaseAppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bottomTabView)
    BottomTabView bottomTabView;

    FragmentPagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return R.layout.activity_base_bottom_tab;
    }

    @Override
    void initView() {
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return getFragments().get(position);
            }

            @Override
            public int getCount() {
                return getFragments().size();
            }
        };
        viewPager.setAdapter(adapter);
        if (getCenterView() == null) {
            bottomTabView.setTabItemViews(getTabViews());
        } else {
            bottomTabView.setTabItemViews(getTabViews(), getCenterView());
        }

        bottomTabView.setUpWithViewPager(viewPager);
    }

    @Override
    public void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    LightStatusBarUtils.setLightStatusBar(BottomTabBaseActivity.this, true);
                } else {
                    LightStatusBarUtils.setLightStatusBar(BottomTabBaseActivity.this, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected abstract List<BottomTabView.TabItemView> getTabViews();

    protected abstract List<Fragment> getFragments();

    protected View getCenterView() {
        return null;
    }


}
