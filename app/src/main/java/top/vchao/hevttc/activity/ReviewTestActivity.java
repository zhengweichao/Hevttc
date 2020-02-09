package top.vchao.hevttc.activity;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import top.vchao.hevttc.R;

/**
 * 答题页面（正在开发）
 */
public class ReviewTestActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_an_home;
    }

    @OnClick({R.id.btn_sxlx, R.id.btn_zjlx, R.id.btn_mnks, R.id.btn_sjlx, R.id.btn_ctjlb, R.id.btn_point, R.id.btn_law, R.id.btn_cheats, R.id.btn_collection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //            点击开始答题，则带参传递，跳转页面，默认选择题
            case R.id.btn_mnks:
                // TODO: 2017/6/13 开始答题
                Intent intent1 = new Intent(ReviewTestActivity.this, AnswerActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;

            case R.id.btn_point:
                // TODO: 2017/6/13 选择题
                Intent intent3 = new Intent(ReviewTestActivity.this, AnswerActivity.class);
                intent3.putExtra("type", 1);
                startActivity(intent3);
                break;

            case R.id.btn_law:
                // TODO: 2017/6/13 判断题
                Intent intent4 = new Intent(ReviewTestActivity.this, AnswerActivity.class);
                intent4.putExtra("type", 2);
                startActivity(intent4);
                break;

            case R.id.btn_cheats:
                // TODO: 2017/6/13 简答题
                Intent intent5 = new Intent(ReviewTestActivity.this, AnswerActivity.class);
                intent5.putExtra("type", 3);
                startActivity(intent5);
                break;

            case R.id.btn_collection:
                // TODO: 2017/6/13 关于
                Intent intent6 = new Intent(ReviewTestActivity.this, AboutActivity.class);
                startActivity(intent6);
                break;
        }
    }
}
