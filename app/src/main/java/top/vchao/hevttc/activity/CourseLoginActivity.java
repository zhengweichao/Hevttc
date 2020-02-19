package top.vchao.hevttc.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;

import top.vchao.hevttc.R;
import top.vchao.hevttc.constant.Constant;
import top.vchao.hevttc.jw.bean.CourseTable;
import top.vchao.hevttc.jw.bean.User;
import top.vchao.hevttc.jw.factor.BeanFactor;
import top.vchao.hevttc.jw.service.BroadcastAction;
import top.vchao.hevttc.jw.service.DataService;
import top.vchao.hevttc.utils.DateUtils;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.SPUtils;
import top.vchao.hevttc.utils.ToastUtil;


/**
 * 登录界面
 * Created by FatCat on 2016/10/2.
 */
public class CourseLoginActivity extends BaseAppCompatActivity {

    private Toolbar mToolbar;

    private EditText etCourseNumber;
    private EditText etCoursePassword;
    private Button btnCourseLogin;
    private EditText etCourseCheck;
    private ImageView ivCheckCode;
    private TextView mJwUrlView;

    private boolean isBinder = false;

    private String doQurey;

    private DataService.DataBinder myBinder;

    private MyBroadcastReceiver mReceiver;

    public int courseTableCount = -1;
    public int xndSum = 0;


    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (DataService.DataBinder) service;
            isBinder = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinder = false;
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtils.e("" + msg.what);
            switch (msg.what) {
                case 0x123: {//登录成功

                    btnCourseLogin.setEnabled(false);
                    btnCourseLogin.setText(doQurey);
                    if (doQurey.equals("获取课表...")) {
                        myBinder.qureyXnds();
                    } else if (doQurey.equals("获取成绩...")) {
                        myBinder.qureyScore();
                    }
                    break;
                }
                case 0x124: {//获取验证码
                    Bundle bl = msg.getData();
                    byte[] data = bl.getByteArray(Constant.CHECK);
                    Bitmap bim = BitmapFactory.decodeByteArray(data, 0, data.length);
                    ivCheckCode.setImageBitmap(bim);
                    break;
                }
                case 0x125: {//登录失败
                    Bundle bl = msg.getData();
                    String error = bl.getString(Constant.LOGIN_FAIL);
                    myBinder.getCheckImg();//重新获取验证码
                    ToastUtil.showShort(error);
                    break;
                }
                case 0x126: {//获取课表
                    CourseTable courseTable = (CourseTable) msg.getData().getSerializable(Constant.COURSE_TABLE);
                    Gson gson = new Gson();

                    if (courseTable.getCurrXqd().equals("3")) {
                        String[] xnds = gson.fromJson(SPUtils.getXndInfo(CourseLoginActivity.this, ""), String[].class);
                        for (int i = xnds.length - 1; i >= 0; i--) {
                            myBinder.qureyCourseTable(xnds[i], "2");
                            myBinder.qureyCourseTable(xnds[i], "1");
                        }
                        break;
                    }

                    //保存课表
                    SPUtils.setCourseInfo(CourseLoginActivity.this, courseTable.getCurrXnd() + courseTable.getCurrXqd(), gson.toJson(courseTable).toString());

                    courseTableCount++;
                    btnCourseLogin.setText(doQurey + courseTableCount + "/" + xndSum);
                    if (courseTableCount == xndSum) {
                        //将当前周作为课表开始时间保存
                        SPUtils.setBeginTime(CourseLoginActivity.this, DateUtils.countBeginTime(Calendar.getInstance(), 1));
                        Intent intent = new Intent(BroadcastAction.UPDTE_COURSE);
                        CourseLoginActivity.this.sendBroadcast(intent);
                        CourseLoginActivity.this.finish();
                    }
                    break;
                }
                case 0x127: {//获取成绩
                    Bundle bl = msg.getData();
                    Intent intent = new Intent(CourseLoginActivity.this, ScoreAtivity.class);
                    if (bl != null) {
                        intent.putExtras(bl);
                    }
                    startActivity(intent);
                    CourseLoginActivity.this.finish();
                    break;
                }
                case 0x128: {//获取学年列表

                    Bundle bl = msg.getData();

                    String[] xnds = (String[]) bl.getSerializable(Constant.COURSE_XND);
                    String xnd = bl.getString("currXnd");
                    String xqd = bl.getString("currXqd");

                    Calendar cal = Calendar.getInstance();
                    int m = cal.get(Calendar.MONTH);
                    if (1 <= m && m < 7) {
                        xqd = "2";
                    } else {
                        xqd = "1";
                    }

                    Gson gson = new Gson();

                    //保存学年列表
                    SPUtils.setXndInfo(CourseLoginActivity.this, gson.toJson(xnds).toString());
                    SPUtils.setCurrXnd(CourseLoginActivity.this, xnd);
                    SPUtils.setCurrXqd(CourseLoginActivity.this, xqd);

                    courseTableCount = 0;
                    xndSum = xnds.length * 2;
                    myBinder.qureyCourseTable(xnds[0], "3");
                    btnCourseLogin.setText(doQurey + 0 + "/" + xndSum);

                    break;
                }

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course_login;
    }

    @Override
    protected void initView() {
        initActionBar();
        etCourseNumber = (EditText) findViewById(R.id.et_course_username);
        etCoursePassword = (EditText) findViewById(R.id.et_course_password);
        btnCourseLogin = (Button) findViewById(R.id.btn_course_login);
        etCourseCheck = (EditText) findViewById(R.id.et_course_check);
        ivCheckCode = (ImageView) findViewById(R.id.iv_checkCode);
        mJwUrlView = (TextView) findViewById(R.id.tv_jw_url);

        Intent intent = getIntent();
        doQurey = intent.getStringExtra("qurey");
        if (doQurey == null) {
            doQurey = "获取课表...";
        }

    }


    protected void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("登录");// 标题的文字需在setSupportActionBar之前，不然会无效
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

        mReceiver = new MyBroadcastReceiver();

        String jwUrl = SPUtils.getJwUrl(this, "");
        if (jwUrl.equals("")) {
            jwUrl = Constant.BASE_URL;
            SPUtils.setJwUrl(this, jwUrl);
        }
        mJwUrlView.setText("教务网地址:" + jwUrl);
        LogUtils.e("" + jwUrl);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.LOGIN_SUCCESS);
        intentFilter.addAction(BroadcastAction.LOGIN_FAIL);
        intentFilter.addAction(BroadcastAction.CHECK_IMG);
        intentFilter.addAction(BroadcastAction.COURSE_TABLE);
        intentFilter.addAction(BroadcastAction.COURSE_XND);
        intentFilter.addAction(BroadcastAction.SCORE);
        registerReceiver(mReceiver, intentFilter);

        Intent it = new Intent(this, DataService.class);
        it.putExtra(Constant.JW_URL, jwUrl);
        bindService(it, sc, Context.BIND_AUTO_CREATE);

    }


    public void loginClick(View view) {
        switch (view.getId()) {
            case R.id.btn_course_login: {
                login();
                break;
            }
            case R.id.iv_checkCode: {
                getCode();
                break;
            }
            case R.id.tv_jw_url: {
                setJwUrl();
                break;
            }
        }
    }

    private void login() {

        String courseName = etCourseNumber.getText().toString().trim();
        String coursePassword = etCoursePassword.getText().toString();
        String checkCode = etCourseCheck.getText().toString().trim();
        User user = BeanFactor.createUser(courseName, coursePassword, checkCode);
        myBinder.login(user);

    }

    private void getCode() {

        myBinder.getCheckImg();
    }

    private void setJwUrl() {
        LinearLayout layout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.set_jw_url_dialog_layout, null);
        final EditText et = (EditText) layout.findViewById(R.id.et_jwurl);
        new AlertDialog.Builder(this).setTitle("修改教务网地址")
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        String url = dealURL(input);
                        SPUtils.setJwUrl(CourseLoginActivity.this, url);//保存到本地
                        mJwUrlView.setText("教务网地址:" + url);//显示修改后的地址
                        myBinder.setURL(url);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 整理输入的url
     *
     * @param url url
     * @return 处理过的urll
     */
    private String dealURL(String url) {
        if (url.matches("http://(.*)/")) {
            return url;
        }
        String u = url;
        if (!u.endsWith("/")) {
            u += "/";
        }
        if (!u.startsWith("http://")) {
            u = "http://" + u;
        }
        return u;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (courseTableCount < 0 || courseTableCount >= 6) {
                    finish();
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (courseTableCount < 0 || courseTableCount >= 6) {
                finish();
            } else {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBinder) {
            unbindService(sc);
        }
        unregisterReceiver(mReceiver);
    }

    /**
     * 广播监听类
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Message msg = new Message();
            Bundle bdl = intent.getExtras();
            msg.setData(bdl);
            switch (action) {
                case BroadcastAction.LOGIN_SUCCESS: {
                    //登录成功
                    msg.what = 0x123;
                    break;
                }
                case BroadcastAction.CHECK_IMG: {
                    //获取验证码
                    msg.what = 0x124;
                    break;
                }
                case BroadcastAction.LOGIN_FAIL: {
                    //登录失败
                    msg.what = 0x125;
                    break;
                }
                case BroadcastAction.COURSE_TABLE: {
                    //接收课程表
                    msg.what = 0x126;
                    break;
                }
                case BroadcastAction.SCORE: {
                    //接收成绩表
                    msg.what = 0x127;
                    break;
                }
                case BroadcastAction.COURSE_XND: {
                    //接收成绩表
                    msg.what = 0x128;
                    break;
                }
            }
            mHandler.sendMessage(msg);
        }
    }

}
