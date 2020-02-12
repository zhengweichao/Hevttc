package top.vchao.hevttc.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.LogUtils;

/**
 * @ 创建时间: 2017/8/23 on 22:09.
 * @ 描述：闪屏页面
 * @ 作者: vchao
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.sp_jump_btn)
    Button btn_splash_jump;

    private CountDownTimer countDownTimer = new CountDownTimer(1100, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            LogUtils.e("onTick: " + millisUntilFinished);
            btn_splash_jump.setText("跳过(" + millisUntilFinished / 1000 + "s)");
        }

        @Override
        public void onFinish() {
            btn_splash_jump.setText("跳过(" + 0 + "s)");
            jumpActivity();
        }
    };

    private void jumpActivity() {
        // 判断本地是否有登录信息。
        // 有 --> 主页面；  无 --> 登录页面
        BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        Intent intent;
        if (user == null) {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    void initData() {
        btn_splash_jump.setVisibility(View.VISIBLE);
        countDownTimer.start();
    }

    @OnClick(R.id.sp_jump_btn)
    public void onViewClicked() {
        //执行跳转逻辑
        jumpActivity();
    }
}
