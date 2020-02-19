package top.vchao.hevttc.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.ButterKnife;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.LogUtils;

/**
 * @ 创建时间: 2017/5/17 on 11:16.
 * @ 描述：Activity基类
 * @ 作者: vchao
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        LogUtils.e(getClass().getName() + "   -----------onCreate");
        dealBundle(savedInstanceState);
        getPreIntent();
        initActionBar();

        initView();
        initBack();
        initData();
        initListener();

    }

    protected void dealBundle(@Nullable Bundle savedInstanceState) {

    }

    private void initBack() {
        View btn_back = findViewById(R.id.btn_back);
        if (btn_back == null) return;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initActionBar() {

    }

    /**
     * 获取上一个页面传递来的intent数据
     */
    protected void getPreIntent() {
    }

    protected void initTitleBar(String text) {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (tvTitle == null) return;
        tvTitle.setText(text);
        View btn_back = findViewById(R.id.btn_back);
        if (btn_back == null) return;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * @return 布局文件id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化View
     */
    protected void initView() {
    }

    /**
     * 初始化界面数据
     */
    protected void initData() {
    }

    /**
     * 绑定监听器与适配器
     */
    protected void initListener() {
    }

}
