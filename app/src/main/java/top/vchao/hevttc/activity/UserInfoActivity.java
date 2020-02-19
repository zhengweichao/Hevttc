package top.vchao.hevttc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.helper.ErrorCode;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.Response;
import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.JsonRealBean;
import top.vchao.hevttc.bean.MyUser;
import top.vchao.hevttc.constant.MyUrl;
import top.vchao.hevttc.constant.SPkey;
import top.vchao.hevttc.utils.CommonUtil;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.SPUtils;
import top.vchao.hevttc.utils.ToastUtil;
import top.vchao.hevttc.view.LoadDialog;

/**
 * @ 创建时间: 2017/5/23 on 22:09.
 * @ 描述：用户信息页面
 * @ 作者: vchao
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.tv_user_info_name)
    TextView tvUserInfoName;
    @BindView(R.id.tv_user_info_sex)
    TextView tvUserInfoSex;
    @BindView(R.id.tv_user_info_stuno)
    TextView tvUserInfoStuno;
    @BindView(R.id.tv_user_info_tel)
    TextView tvUserInfoTel;
    @BindView(R.id.tv_user_info_yuan)
    TextView tvUserInfoYuan;
    @BindView(R.id.tv_user_info_class)
    TextView tvUserInfoClass;
    @BindView(R.id.tv_user_info_created)
    TextView tvUserInfoCreated;
    @BindView(R.id.ll_user_info_sex)
    LinearLayout llUserInfoSex;
    @BindView(R.id.ll_user_info_stuno)
    LinearLayout llUserInfoStuno;
    @BindView(R.id.ll_user_info_yuan)
    LinearLayout llUserInfoYuan;
    @BindView(R.id.ll_user_info_class)
    LinearLayout llUserInfoClass;
    @BindView(R.id.bt_real_name)
    Button btRealName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initData() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        tvUserInfoTel.setText(user.getMobilePhoneNumber());
        tvUserInfoCreated.setText(user.getCreatedAt());

        if (!TextUtils.isEmpty(user.getStuno())) {
            tvUserInfoName.setText(user.getUsername());
            tvUserInfoSex.setText(user.getSex());
            tvUserInfoStuno.setText(user.getStuno());
            tvUserInfoYuan.setText(user.getYuan());
            tvUserInfoClass.setText(user.getClazz());
            btRealName.setVisibility(View.GONE);
        } else {
            tvUserInfoName.setText("未进行实名认证");
            tvUserInfoSex.setText("未进行实名认证");
            tvUserInfoStuno.setText("未进行实名认证");
            tvUserInfoYuan.setText("未进行实名认证");
            tvUserInfoClass.setText("未进行实名认证");
        }
    }

    /**
     * 实名认证
     */
    private void RealName() {
        View view = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.dialog_reallyname, null);
        final EditText et_real_stuno = (EditText) view.findViewById(R.id.et_real_stuno);
        final EditText et_real_name = (EditText) view.findViewById(R.id.et_real_name);

        AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = CommonUtil.getText(et_real_name);
                        String stuno = CommonUtil.getText(et_real_stuno);
                        if (CommonUtil.isAllNotNull(et_real_name, et_real_stuno)) {
                            UpdateReal(name, stuno);
                        } else {
                            ToastUtil.showShort("请填写完整信息");
                        }
                    }
                })
                .setTitle("实名认证")
                .setIcon(R.mipmap.ic_app_logo)
                .setView(view)
                .create();
        dialog.show();
    }

    private void UpdateReal(String name, final String stuno) {
        LoadDialog.show(UserInfoActivity.this, "实名认证中");
        OkGo.get(MyUrl.URL_QUERY)
                .params("stuname", name)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.e("onSuccess: " + s);
                        JsonRealBean jsonRealBean = new Gson().fromJson(s, JsonRealBean.class);

                        for (int i = 0; i < jsonRealBean.getList().size(); i++) {
                            if (TextUtils.equals(stuno, jsonRealBean.getList().get(i).getStu_no() + "")) {
                                tvUserInfoName.setText(jsonRealBean.getList().get(i).getName());
                                tvUserInfoSex.setText(jsonRealBean.getList().get(i).getSex());
                                tvUserInfoStuno.setText(jsonRealBean.getList().get(i).getStu_no());
                                tvUserInfoYuan.setText(jsonRealBean.getList().get(i).getYuanxi());
                                tvUserInfoClass.setText(jsonRealBean.getList().get(i).getZhuanye());
                                SPUtils.put(UserInfoActivity.this, "real", "true");
                                LogUtils.e("onSuccess: 实名认证成功！");
                                MyUser bean = new MyUser();
                                bean.setUsername(jsonRealBean.getList().get(i).getName());
                                bean.setStuno(jsonRealBean.getList().get(i).getStu_no());
                                bean.setSex(jsonRealBean.getList().get(i).getSex());
                                bean.setClazz(jsonRealBean.getList().get(i).getZhuanye());
                                bean.setYuan(jsonRealBean.getList().get(i).getYuanxi());

                                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                                bean.update(bmobUser.getObjectId(), new UpdateListener() {

                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            LogUtils.e("个人信息更新成功！");
                                            btRealName.setVisibility(View.GONE);
                                            ToastUtil.show(UserInfoActivity.this, "实名认证成功", Toast.LENGTH_SHORT);
                                            fetchUserInfo();
                                        } else {
                                            LogUtils.e("错误：" + e.getMessage());
                                            ToastUtil.show(UserInfoActivity.this, "实名认证失败", Toast.LENGTH_SHORT);
                                        }
                                    }
                                });

                                LoadDialog.dismiss(UserInfoActivity.this);
                                return;
                            }
                        }
                        LoadDialog.dismiss(UserInfoActivity.this);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LoadDialog.dismiss(UserInfoActivity.this);
                    }
                });
    }


    /**
     * 更新本地用户信息
     * 注意：需要先登录，否则会报9024错误
     *
     * @see ErrorCode#E9024S
     */
    private void fetchUserInfo() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LogUtils.e("Newest UserInfo is " + s);
                } else {
                    LogUtils.e(e.toString());
                }
            }
        });
    }


    @OnClick({R.id.bt_real_name, R.id.layout_title, R.id.ll_user_info_name, R.id.ll_user_info_sex, R.id.ll_user_info_stuno,
            R.id.ll_user_info_tel, R.id.ll_user_info_yuan, R.id.ll_user_info_class, R.id.ll_user_info_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_real_name:
            case R.id.ll_user_info_class:
            case R.id.ll_user_info_sex:
            case R.id.ll_user_info_stuno:
            case R.id.ll_user_info_yuan:
                String isReal = SPUtils.look(UserInfoActivity.this, SPkey.isRealName, "false");
                if (!TextUtils.equals(isReal, "true")) {
                    RealName();
                }
        }
    }
}