package top.vchao.hevttc.activity;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import top.vchao.hevttc.R;
import top.vchao.hevttc.fragment.TabHomeFragment;
import top.vchao.hevttc.fragment.TabMyFragment;
import top.vchao.hevttc.fragment.TabNewsFragment;
import top.vchao.hevttc.view.BottomTabView;

/**
 * @ 创建时间: 2017/5/17 on 11:31.
 * @ 描述：主界面 Activity
 * @ 作者: vchao
 */

public class HomeActivity extends BottomTabBaseActivity {
    private long mExitTime;

    @Override
    protected List<BottomTabView.TabItemView> getTabViews() {
        List<BottomTabView.TabItemView> tabItemViews = new ArrayList<>();
        tabItemViews.add(new BottomTabView.TabItemView(this, "首页", R.color.colorPrimary,
                R.color.colorAccent, R.mipmap.main_home_nor, R.mipmap.main_home_pre));
        tabItemViews.add(new BottomTabView.TabItemView(this, "新闻", R.color.colorPrimary,
                R.color.colorAccent, R.mipmap.main_buy_nor, R.mipmap.main_buy_pre));
        tabItemViews.add(new BottomTabView.TabItemView(this, "我的", R.color.colorPrimary,
                R.color.colorAccent, R.mipmap.main_user_nor, R.mipmap.main_user_pre));
        return tabItemViews;
    }

    @Override
    protected List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TabHomeFragment());
        fragments.add(new TabNewsFragment());
        fragments.add(new TabMyFragment());
        return fragments;
    }


    /**
     * 重写返回键返回方法，防止误触退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(HomeActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

   /*
   // 中间按钮
   @Override
    protected View getCenterView() {
        ImageView centerView = new ImageView(this);
        centerView.setImageResource(R.mipmap.ic_launcher_round);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        layoutParams.leftMargin = 60;
        layoutParams.rightMargin = 60;
        layoutParams.bottomMargin = 0;
        centerView.setLayoutParams(layoutParams);
        centerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBottomTabBaseActivity.this, "centerView 点击了", Toast.LENGTH_SHORT).show();
            }
        });
        return centerView;
    }*/
}
