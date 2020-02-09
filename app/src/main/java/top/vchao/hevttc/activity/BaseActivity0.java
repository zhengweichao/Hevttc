package top.vchao.hevttc.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.LogUtils;

/**
 * @ 创建时间: 2017/5/17 on 11:16.
 * @ 描述：Activity基类
 * @ 作者: vchao
 */
public abstract class BaseActivity0 extends AppCompatActivity {

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        LogUtils.e(getClass().getName() + "   -----------onCreate");

        getPreIntent();

        initView();
        initActionBar();
        initBack();
        initData();
        initListener();

        initFragment();
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

    public void initFragment() {

    }

    protected void initActionBar() {

    }

    /**
     * 获取上一个页面传递来的intent数据
     */
    public void getPreIntent() {
    }


    /**
     * 初始化标题栏
     *
     * @param color
     * @param text
     */
    void initTitleBar(int color, int text) {
        View btn_back = findViewById(R.id.btn_back);
        if (btn_back != null) {
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        LinearLayout layout_title = (LinearLayout) findViewById(R.id.layout_title);
        layout_title.setBackgroundResource(color);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(this.getResources().getString(text));
    }

    void initTitleBar(String text) {
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
    void initView() {
    }

    /**
     * 初始化界面数据
     */
    void initData() {
    }

    /**
     * 绑定监听器与适配器
     */
    void initListener() {
    }

}
