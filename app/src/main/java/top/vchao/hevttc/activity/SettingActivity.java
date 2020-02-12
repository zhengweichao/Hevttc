package top.vchao.hevttc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import top.vchao.hevttc.R;
import top.vchao.hevttc.jw.service.BroadcastAction;
import top.vchao.hevttc.utils.DateUtils;
import top.vchao.hevttc.utils.SPUtils;

/**
 * 设置界面
 */
public class SettingActivity extends BaseAppCompatActivity {

    private Toolbar mToolbar;
    private TextView mCurrWeekView;
    private TextView mCurrXnd;
    private int mCurrWeekNum;


    @Override
    public void initView() {
        mCurrWeekView = (TextView) findViewById(R.id.setting_curr_week_des);
        mCurrXnd = (TextView) findViewById(R.id.tv_curr_xnd);
        updateCurrWeek();
    }

    @Override
    protected void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        mToolbar.setTitle("更多选项");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar); // toolbar.setSubtitle("副标题");
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorBlue));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String xnd = SPUtils.getCurrXnd(this, null);
        String xqd = SPUtils.getCurrXqd(this, null);
        if (xnd != null && xqd != null) {
            mCurrXnd.setText(xnd + "学年第" + xqd + "学期");
        }
    }

    /**
     * 更新当前周
     */
    public void updateCurrWeek() {
        long currTime = new Date().getTime();
        long beginTime = SPUtils.getBeginTime(this, currTime);
        mCurrWeekNum = DateUtils.countCurrWeek(beginTime, currTime);
        String s = "";
        if (mCurrWeekNum == 0) {
            s = "你还没添加过任何课表";
        } else if (mCurrWeekNum > 25) {
            s = "本学期已结束";
        } else {
            s = "现在是第" + mCurrWeekNum + "周";
        }
        mCurrWeekView.setText(s);
    }

    public void settingOnClick(View view) {

        switch (view.getId()) {
            case R.id.setting_mycourse: {
                clickMyCourse();
                break;
            }
            case R.id.setting_curr_week: {
                clickCurrWeekNum();
                break;
            }
            case R.id.setting_qurey_score: {
                qureyScore();
                break;
            }

        }

    }


    private void clickMyCourse() {
        Intent intent = new Intent(SettingActivity.this, ChooseXndActivity.class);
        startActivity(intent);
    }

    public void qureyScore() {
        Intent intent = new Intent(SettingActivity.this, CourseLoginActivity.class);
        intent.putExtra("qurey", "获取成绩...");
        startActivity(intent);
    }

    private void clickCurrWeekNum() {
        if (mCurrWeekNum == 0) {
            return;
        }
        String[] num = new String[25];
        for (int i = 0; i < 25; i++) {
            num[i] = "第" + String.valueOf(i + 1) + "周";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("选择当前周");
        builder.setItems(num, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which + 1 == mCurrWeekNum) {
                    return;
                }
                SPUtils.setBeginTime(SettingActivity.this, DateUtils.countBeginTime(Calendar.getInstance(), which + 1));
                updateCurrWeek();
                Intent intent = new Intent();
                intent.setAction(BroadcastAction.UPDATE_CURR_WEEK_NUM);
                SettingActivity.this.sendBroadcast(intent);

            }
        });
        builder.create().show();
    }

}




































