package top.vchao.hevttc.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import top.vchao.hevttc.R;

/**
 * @ 创建时间: 2017/8/23 on 22:09.
 * @ 描述：闪屏页面
 * @ 作者: vchao
 */
public class SplashActivity extends BaseActivity {
    private Intent intent;
    private Button btn_splash_jump;
    private CountDownTimer countDownTimer = new CountDownTimer(1100, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("zwc", "onTick: " + millisUntilFinished);
            btn_splash_jump.setText("跳过(" + millisUntilFinished / 1000 + "s)");
        }

        @Override
        public void onFinish() {
            btn_splash_jump.setText("跳过(" + 0 + "s)");
            goLoginActivity();
        }
    };

    private void goLoginActivity() {
        //直接跳转主页面
        BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        if (user == null) {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
        }
        //直接跳转主页面
        startActivity(intent);
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    void initView() {
        btn_splash_jump = (Button) findViewById(R.id.sp_jump_btn);
    }

    @Override
    void initData() {
        btn_splash_jump.setVisibility(View.VISIBLE);
        countDownTimer.start();

        //Bmob初始化
        Bmob.initialize(this, "7de3a2c669418b3957557a6f519afc3e");
    }

    @Override
    void initListener() {
        btn_splash_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行跳转逻辑
                goLoginActivity();
            }
        });
    }

}
