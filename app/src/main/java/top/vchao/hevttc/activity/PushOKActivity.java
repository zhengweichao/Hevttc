package top.vchao.hevttc.activity;

import butterknife.OnClick;
import top.vchao.hevttc.R;

public class PushOKActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_ok;
    }


    @OnClick(R.id.btn_ok_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
