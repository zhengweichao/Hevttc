package top.vchao.hevttc.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.ScoreAdapter;
import top.vchao.hevttc.constant.Constant;
import top.vchao.hevttc.jw.bean.CourseScore;
import top.vchao.hevttc.jw.bean.ScoreTable;

/**
 * 成绩表界面
 * Created by EsauLu on 2016-10-07.
 */

public class ScoreAtivity extends BaseAppCompatActivity {

    private Toolbar mToolbar;
    private ListView mScoreList;
    private ScoreAdapter mScoreAdapter;
    private ArrayList<CourseScore> mCourseScores;
    private ScoreTable mScoreTable;

    @Override
    protected int getLayoutId() {
        return R.layout.score_activity_layout;
    }

    @Override
    protected void initView() {
        mScoreList = (ListView) findViewById(R.id.lv_score_list);
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        mScoreTable = (ScoreTable) intent.getSerializableExtra(Constant.SCORE_TABLE);
        mCourseScores = mScoreTable.getScoreList();
        if (mCourseScores == null) {
            mCourseScores = new ArrayList<>();
        }

        mScoreAdapter = new ScoreAdapter(this, R.layout.score_item_layout, mCourseScores);
        mScoreList.setAdapter(mScoreAdapter);
    }


    @Override
    protected void initActionBar() {
        super.initActionBar();
        mToolbar = (Toolbar) findViewById(R.id.score_activity_toolbar);
        mToolbar.setTitle("查看成绩");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar); // toolbar.setSubtitle("副标题");
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorBlue));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}






































