package top.vchao.hevttc.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.Feedback;
import top.vchao.hevttc.bean.MyUser;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;
import top.vchao.hevttc.view.LoadDialog;

/**
 * @ 创建时间: 2017/10/2 on 17:12.
 * @ 描述：投诉页面
 * @ 作者: vchao
 */
public class ComplaintsDetailActivity extends BaseActivity {

    @BindView(R.id.sp_comp_kind)
    Spinner spCompKind;
    @BindView(R.id.et_comp_content)
    EditText etCompContent;
    String kind = "食堂投诉";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_complaints_detail;
    }

    @Override
    void initView() {
        initTitleBar("权益保障");
    }

    @Override
    void initListener() {
        spCompKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] units = getResources().getStringArray(R.array.feedkinds);
                kind = units[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.bt_comp_submit)
    public void onViewClicked() {
        String content = etCompContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showShort(ComplaintsDetailActivity.this, "请填写反馈信息！");
        } else {
            LoadDialog.show(ComplaintsDetailActivity.this, "反馈中……");
            Feedback bean = new Feedback();
            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            bean.setTheme(kind);
            bean.setContent(content);
            bean.setUser(user);
            bean.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        ToastUtil.show(ComplaintsDetailActivity.this, "我们已经收到您的反馈！", Toast.LENGTH_SHORT);
                        LogUtils.e("已经收到您的反馈：" + objectId);
                        Intent intent = new Intent(ComplaintsDetailActivity.this, PushOKActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.show(ComplaintsDetailActivity.this, "反馈失败，请检查网络后重试！", Toast.LENGTH_SHORT);
                        LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                    }

                    LoadDialog.dismiss(ComplaintsDetailActivity.this);
                }
            });

        }
    }
}
