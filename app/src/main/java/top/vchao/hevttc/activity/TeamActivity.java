package top.vchao.hevttc.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.NNNAdapter;
import top.vchao.hevttc.bean.TeamBean;
import top.vchao.hevttc.utils.LogUtils;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/10/4 on 22:09.
 * @ 描述：组织社团页面
 * @ 作者: vchao
 */
public class TeamActivity extends BaseActivity {

    @BindView(R.id.rv_team)
    RecyclerView rvTeam;
    private NNNAdapter mAdapter;
    private ArrayList<TeamBean> teamBeen;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team;
    }

    @Override
    void initData() {
        mAdapter = new NNNAdapter();
        teamBeen = new ArrayList<>();
        rvTeam.setLayoutManager(new LinearLayoutManager(TeamActivity.this));

        BmobQuery<TeamBean> query = new BmobQuery<TeamBean>();
        query.order("-time");
        query.setLimit(15);
        query.findObjects(new FindListener<TeamBean>() {
            @Override
            public void done(List<TeamBean> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    for (TeamBean teamBean : object) {
                        teamBeen.add(teamBean);
                    }
                    mAdapter.setData(teamBeen);
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        rvTeam.setAdapter(mAdapter);
    }

    @Override
    void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int position) {
                Intent intent = new Intent(TeamActivity.this, TeamDetailActivity.class);
                intent.putExtra("teambean", teamBeen.get(position));
                startActivity(intent);
            }
        });

    }
}
