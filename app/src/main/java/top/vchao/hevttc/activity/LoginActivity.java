package top.vchao.hevttc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.stephentuso.welcome.WelcomeHelper;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.MyUser;
import top.vchao.hevttc.constant.Config;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;

/**
 * @ 创建时间: 2017/6/13 on 16:03.
 * @ 描述：登录页面
 * @ 作者: vchao
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;

    private static final int REQUEST_SIGNUP = 0;
    WelcomeHelper welcomeScreen;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void dealBundle(@Nullable Bundle savedInstanceState) {
        welcomeScreen = new WelcomeHelper(this, MyWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);
    }

    @Override
    protected void initData() {
        if (Config.useTestAccount) {
            etUserName.setText(Config.TestAccount);
            etPassword.setText(Config.TestPassword);
        }
    }

    @OnClick({R.id.btn_login, R.id.tv_link_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //如果内容不合法，则直接返回，显示错误。
                if (!validate()) {
                    LogUtils.e("输入内容格式不合法");
                    ToastUtil.showShort("请输入合法的账号密码信息！");
                    return;
                }
                login();
                break;
            case R.id.tv_link_signup:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                //动画效果
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    /**
     * 登录方法
     */
    public void login() {
        //输入合法，将登录按钮置为不可用，显示圆形进度对话框
        btnLogin.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登录中...");
        progressDialog.show();
        //获取输入内容
        String username = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        // 手机号登录
        BmobUser.loginByAccount(username, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (user != null) {
//                  登录成功
                    onLoginSuccess();
                    progressDialog.dismiss();
                } else {
//                  登录失败
                    onLoginFailed(e.getMessage());
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    /**
     * 重写返回键的返回方法
     */
    @Override
    public void onBackPressed() {
        // Disable going back to the ReviewTestActivity
        moveTaskToBack(true);
    }

    /**
     * 登录成功
     */
    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 登录失败
     */
    public void onLoginFailed(String errorTip) {
        ToastUtil.showLong("登录失败 " + errorTip);
        btnLogin.setEnabled(true);
    }

    /**
     * @return 判断是否账号密码是否合法
     */
    public boolean validate() {
        //设置初值，默认为合法
        boolean valid = true;

        //获取输入内容
        String email = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        //判断账号
        if (email.isEmpty()) {
            etUserName.setError("请输入你的账号");
            valid = false;
        } else {
            etUserName.setError(null);
        }
        //判断密码
        if (password.isEmpty()) {
            etPassword.setError("请输入你的密码");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

}