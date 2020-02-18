package top.vchao.hevttc.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.XndAdapter;
import top.vchao.hevttc.jw.service.BroadcastAction;
import top.vchao.hevttc.utils.SPUtils;

public class ChooseXndActivity extends BaseAppCompatActivity {

    private Toolbar mToolbar;
    private ListView mXndListView;
    private XndAdapter mXndAdapter;
    private ArrayList<String> mXndList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_xnd;
    }

    @Override
    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.choose_xnd_toolbar);
        mToolbar.setTitle("选择课表");// 标题的文字需在setSupportActionBar之前，不然会无效
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

    @Override
    protected void initData() {

        mXndList = new ArrayList<>();
        mXndListView = (ListView) findViewById(R.id.course_table_list);

        mXndAdapter = new XndAdapter(this, R.layout.xnd_item_layout, mXndList);
        mXndListView.setAdapter(mXndAdapter);
        mXndListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] xs = mXndList.get(position).split("@");
                String xnd = xs[1];
                String xqd = xs[2];
                if (xnd.equals(mXndAdapter.getCurrXnd())
                        && xqd.equals(mXndAdapter.getCurrXqd())) {
                    return;
                }
                SPUtils.setCurrXnd(ChooseXndActivity.this, xnd);
                SPUtils.setCurrXqd(ChooseXndActivity.this, xqd);
                mXndAdapter.setCurrXnd(xnd);
                mXndAdapter.setCurrXqd(xqd);
                Intent intent = new Intent(BroadcastAction.UPDTE_COURSE);
                ChooseXndActivity.this.sendBroadcast(intent);
                ChooseXndActivity.this.finish();
            }
        });

        updateList();
    }

    private void updateList() {

        mXndList.clear();

        Gson gson = new Gson();
        String[] xnds = gson.fromJson(SPUtils.getXndInfo(this, ""), String[].class);
        ArrayList<String> tem = new ArrayList<>();
        if (xnds != null && xnds.length > 0) {
            Calendar cal = Calendar.getInstance();
            int m = cal.MONTH;
            if (1 <= m && m < 7) {
                tem.add(0, xnds[0] + "@2");
            }
            tem.add(0, xnds[0] + "@1");
            for (int i = 1; i < xnds.length; i++) {
                tem.add(0, xnds[i] + "@2");
                tem.add(0, xnds[i] + "@1");
            }
        }

        int size = tem.size();
        for (int i = 0; i < size; i++) {
            String s = tem.get(i);

            switch (i / 2) {
                case 0: {
                    s = "(大一)@" + s;
                    break;
                }
                case 1: {
                    s = "(大二)@" + s;
                    break;
                }
                case 2: {
                    s = "(大三)@" + s;
                    break;
                }
                case 3: {
                    s = "(大四)@" + s;
                    break;
                }
                default: {
                    s = " @" + s;
                    break;
                }
            }

            mXndList.add(0, s);

        }

        String xnd = SPUtils.getCurrXnd(ChooseXndActivity.this, null);
        String xqd = SPUtils.getCurrXqd(ChooseXndActivity.this, null);
        mXndAdapter.setCurrXnd(xnd);
        mXndAdapter.setCurrXqd(xqd);

        mXndAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    public void click(View v) {

        Intent intent = new Intent(this, CourseLoginActivity.class);
        intent.putExtra("qurey", "获取课表...");
        startActivity(intent);

    }

}



































