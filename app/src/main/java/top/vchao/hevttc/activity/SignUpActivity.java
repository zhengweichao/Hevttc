package top.vchao.hevttc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.MyUser;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;

/**
 * @ 创建时间: 2017/8/23 on 22:09.
 * @ 描述：注册页面
 * @ 作者: vchao
 */
public class SignUpActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUserName;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_re_password)
    EditText etRePassword;
    @BindView(R.id.btn_signup)
    Button btnSignUp;
    @BindView(R.id.et_auth_code)
    EditText etAuthCode;
    @BindView(R.id.btn_getcode)
    Button btnGetCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    /**
     * 注册方法
     */
    public void signUp() {
        String code = etAuthCode.getText().toString();
        if (!validate()) {
            onSignUpFailed(2);
            return;
        }
        if (code.isEmpty()) {
            etAuthCode.setError("请输入验证码");
            return;
        } else {
            etAuthCode.setError(null);
        }
        btnSignUp.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("创建账号...");
        progressDialog.show();

        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();

        MyUser user = new MyUser();
        user.setUsername("用户" + mobile);
        user.setPassword(password);
        user.setMobilePhoneNumber(mobile);
        user.signOrLogin(code, new SaveListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    LogUtils.e("注册成功:" + user.getMobilePhoneNumber() + "-" + user.getObjectId());
                    onSignUpSuccess();
                    //对话框消失
                    progressDialog.dismiss();
                } else {
                    LogUtils.e("注册失败" + e.getMessage());
                    onSignUpFailed(2);
                    progressDialog.dismiss();
                }

            }
        });

    }

    /**
     * 注册失败，按钮置为可用
     * 依据传参不同，进行不同吐司
     */
    public void onSignUpFailed(int i) {
        if (i == 1) {
            ToastUtil.showShort("该用户名已经被注册");
        } else {
            ToastUtil.showShort("注册失败");
        }
        btnSignUp.setEnabled(true);
    }

    public void onSignUpSuccess() {
        btnSignUp.setEnabled(true);
        setResult(RESULT_OK, null);

        ToastUtil.showShort("注册成功，已自动登录");
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);

        finish();
    }

    public boolean validate() {
        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();
        String reEnterPassword = etRePassword.getText().toString();

        if (mobile.isEmpty()) {
            etMobile.setError("请输入有效的手机号");
            return false;
        } else {
            etMobile.setError(null);
        }

        if (password.isEmpty()) {
            etPassword.setError("请输入有效的密码");
            return false;
        } else {
            etPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(password))) {
            etRePassword.setError("两次密码输入不一致");
            return false;
        } else {
            etRePassword.setError(null);
        }
        return true;
    }

    private CountDownTimer countDownTimer = new CountDownTimer(60800, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnGetCode.setText(millisUntilFinished / 1000 + "秒后可以重新获取");
        }

        @Override
        public void onFinish() {
            btnGetCode.setText("获取验证码");
            btnGetCode.setEnabled(true);
        }
    };

    @OnClick({R.id.btn_getcode, R.id.btn_signup, R.id.tv_link_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_getcode:
                btnGetCode.setEnabled(false);
                if (!validate()) {
                    ToastUtil.showShort("信息不完善！");
                    btnGetCode.setEnabled(true);
                    return;
                }
                countDownTimer.start();
                String mobile = etMobile.getText().toString();
                IsSignUp(mobile);

                break;
            case R.id.btn_signup:
                signUp();
                break;
            case R.id.tv_link_login:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;

        }
    }

    /**
     * 是否已经注册
     */
    private void IsSignUp(final String tel) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("mobilePhoneNumber", tel);
        //执行查询方法
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
                if (e == null) {
                    if (object.size() != 0) {
                        LogUtils.e("非空" + tel + " 已经注册了");
                        ToastUtil.showShort("这个手机号已经注册了");
                    } else {
                        LogUtils.e(tel + "没有注册");
                        BmobSMS.requestSMSCode(tel, "keshi", new QueryListener<Integer>() {
                            @Override
                            public void done(Integer smsId, BmobException ex) {
                                if (ex == null) {//验证码发送成功
                                    LogUtils.e("验证码发送成功,短信id：" + smsId);//用于查询本次短信发送详情
                                    ToastUtil.showShort("验证码已发送，请注意查收");
                                } else {
                                    ToastUtil.showShort("验证码获取失败，请稍后重试");
                                }
                            }
                        });
                    }
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                    ToastUtil.showShort("验证码获取失败，请稍后重试");
                }
            }
        });
    }

}