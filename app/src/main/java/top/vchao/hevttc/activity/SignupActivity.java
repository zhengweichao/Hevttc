package top.vchao.hevttc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class SignupActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUserName;
    @BindView(R.id.et_mobile)
    EditText etMoblie;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_re_password)
    EditText etRePassword;
    @BindView(R.id.btn_signup)
    Button btnSignUp;
    @BindView(R.id.et_auth_code)
    EditText etAuthCode;
    @BindView(R.id.btn_getcode)
    Button btnGetcode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    /**
     * 注册方法
     */
    public void signup() {
        String code = etAuthCode.getText().toString();
        if (!validate()) {
            onSignupFailed(2);
            return;
        }
        if (code.isEmpty()) {
            etAuthCode.setError("请输入验证码");
            return;
        } else {
            etAuthCode.setError(null);
        }
        btnSignUp.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("创建账号...");
        progressDialog.show();

        String mobile = etMoblie.getText().toString();
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
                    onSignupSuccess();
                    //对话框消失
                    progressDialog.dismiss();
                } else {
                    LogUtils.e("注册失败" + e.getMessage());
                    onSignupFailed(2);
                    progressDialog.dismiss();
                }

            }
        });

    }

    /**
     * 注册失败，按钮置为可用
     * 依据传参不同，进行不同吐司
     */
    public void onSignupFailed(int i) {
        if (i == 1) {
            ToastUtil.show(getBaseContext(), "该用户名已经被注册", Toast.LENGTH_LONG);
        } else {
            ToastUtil.show(getBaseContext(), "注册失败", Toast.LENGTH_LONG);
        }
        btnSignUp.setEnabled(true);
    }

    public void onSignupSuccess() {
        btnSignUp.setEnabled(true);
        setResult(RESULT_OK, null);

        ToastUtil.show(SignupActivity.this, "注册成功，已自动登录", Toast.LENGTH_SHORT);
        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
        startActivity(intent);

        finish();
    }

    public boolean validate() {
        String mobile = etMoblie.getText().toString();
        String password = etPassword.getText().toString();
        String reEnterPassword = etRePassword.getText().toString();

        if (mobile.isEmpty()) {
            etMoblie.setError("请输入有效的手机号");
            return false;
        } else {
            etMoblie.setError(null);
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
            btnGetcode.setText(millisUntilFinished / 1000 + "秒后可以重新获取");
        }

        @Override
        public void onFinish() {
            btnGetcode.setText("获取验证码");
            btnGetcode.setEnabled(true);
        }
    };

    @OnClick({R.id.btn_getcode, R.id.btn_signup, R.id.tv_link_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_getcode:
                btnGetcode.setEnabled(false);
                if (!validate()) {
                    ToastUtil.show(this, "信息不完善！", Toast.LENGTH_SHORT);
                    btnGetcode.setEnabled(true);
                    return;
                }
                countDownTimer.start();
                String mobile = etMoblie.getText().toString();
                IsSignUp(mobile);

                break;
            case R.id.btn_signup:
                signup();
                break;
            case R.id.tv_link_login:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
                        ToastUtil.show(SignupActivity.this, "这个手机号已经注册了", Toast.LENGTH_SHORT);
                    } else {
                        LogUtils.e(tel + "没有注册");
                        BmobSMS.requestSMSCode(tel, "keshi", new QueryListener<Integer>() {
                            @Override
                            public void done(Integer smsId, BmobException ex) {
                                if (ex == null) {//验证码发送成功
                                    LogUtils.e("验证码发送成功,短信id：" + smsId);//用于查询本次短信发送详情
                                    ToastUtil.show(SignupActivity.this, "验证码已发送，请注意查收", Toast.LENGTH_SHORT);
                                } else {
                                    ToastUtil.show(SignupActivity.this, "验证码获取失败，请稍后重试", Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    }
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                    ToastUtil.show(SignupActivity.this, "验证码获取失败，请稍后重试", Toast.LENGTH_SHORT);
                }
            }
        });
    }

}