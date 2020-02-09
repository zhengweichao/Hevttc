package top.vchao.hevttc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import top.vchao.hevttc.R;
import top.vchao.hevttc.activity.LoseDetailActivity;
import top.vchao.hevttc.adapter.NNNAdapter;
import top.vchao.hevttc.bean.LoseItem;
import top.vchao.hevttc.cootab.RecyclerAdapter;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/9/14 on 15:29.
 * @ 描述：失物招领 丢
 * @ 作者: vchao
 */
public class LoseFragment1 extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private SwipeRefreshLayout splMainNews;
    private List<String> mDatas;
    private static final String ARG_TITLE = "title";
    private String mTitle;
    private NNNAdapter LoseAdapter;
    private ArrayList<LoseItem> LoseBeen;

    public static LoseFragment1 getInstance(String title) {
        LoseFragment1 fra = new LoseFragment1();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mTitle = bundle.getString(ARG_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        splMainNews = (SwipeRefreshLayout) v.findViewById(R.id.spl_main_news);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        initData();
        return v;
    }

    protected void initData() {
        LoseAdapter = new NNNAdapter();
        LoseBeen = new ArrayList<>();

        BmobQuery<LoseItem> query = new BmobQuery<LoseItem>();
        query.order("-time");
        //返回6条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(15);
        //执行查询方法
        query.findObjects(new FindListener<LoseItem>() {
            @Override
            public void done(List<LoseItem> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    for (LoseItem loseItem : object) {
                        LoseBeen.add(loseItem);
                        LogUtils.e(loseItem.getAuthor());
                    }
                    LoseAdapter.setData(LoseBeen);
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        mRecyclerView.setAdapter(LoseAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        LoseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int position) {
                Intent intent = new Intent(getActivity(), LoseDetailActivity.class);
                intent.putExtra("losebean", LoseBeen.get(position));
                startActivity(intent);
            }
        });

        splMainNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1500);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //停止刷新操作
                                splMainNews.setRefreshing(false);
                                ToastUtil.show(getActivity(), "没有更多数据了！", Toast.LENGTH_SHORT);
                            }
                        });

                    }
                }).start();

            }
        });


    }

}