package top.vchao.hevttc.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
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
 * @ 描述：意见反馈界面
 * @ 作者: vchao
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.et_feedback_theme)
    EditText etFeedbackTheme;
    @BindView(R.id.et_feedback_content)
    EditText etFeedbackContent;
    @BindView(R.id.bt_feedback)
    Button btFeedback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    void initView() {
        initTitleBar("意见反馈");
    }

    @OnClick(R.id.bt_feedback)
    public void onViewClicked() {
        String theme = etFeedbackTheme.getText().toString().trim();
        String content = etFeedbackContent.getText().toString().trim();

        if (TextUtils.isEmpty(theme) || TextUtils.isEmpty(content)) {
            ToastUtil.showShort(FeedbackActivity.this, "请将信息填写完整！");
        } else {
            LoadDialog.show(FeedbackActivity.this, "反馈中……");
            Feedback bean = new Feedback();
            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            bean.setTheme(theme);
            bean.setContent(content);
            bean.setUser(user);
            bean.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        ToastUtil.show(FeedbackActivity.this, "我们已经收到您的反馈！", Toast.LENGTH_SHORT);
                        LogUtils.e("已经收到您的反馈：" + objectId);
                        Intent intent = new Intent(FeedbackActivity.this, PushOKActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.show(FeedbackActivity.this, "反馈失败，请检查网络后重试！", Toast.LENGTH_SHORT);
                        LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                    }

                    LoadDialog.dismiss(FeedbackActivity.this);
                }
            });

        }
    }
}
