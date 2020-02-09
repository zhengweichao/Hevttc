package top.vchao.hevttc.cootab;

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
import top.vchao.hevttc.activity.NewsWebActivity;
import top.vchao.hevttc.adapter.NNNAdapter;
import top.vchao.hevttc.bean.NewsBean;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/10/3 on 13:19.
 * @ 描述：新闻页面3
 * @ 作者: vchao
 */
public class MainFragment3 extends Fragment {
    private RecyclerView mRecyclerView;

    private static final String ARG_TITLE = "title";
    private String mTitle;
    private NNNAdapter nnnAdapter;
    private ArrayList<NewsBean> newsBeen;
    private SwipeRefreshLayout splMainNews;
    private int refreshCount = 1;

    public static MainFragment3 getInstance(String title) {
        MainFragment3 fra = new MainFragment3();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        nnnAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int position) {
                LogUtils.e(newsBeen.get(position).getContent());
                Intent intent = new Intent(getActivity(), NewsWebActivity.class);
                intent.putExtra("url", newsBeen.get(position).getContent());
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
                        BmobQuery<NewsBean> query = new BmobQuery<NewsBean>();
                        query.order("-time");
                        query.setLimit(10);
                        query.addWhereEqualTo("kind", "c");
                        query.setSkip(10 * refreshCount);
                        //执行查询方法
                        query.findObjects(new FindListener<NewsBean>() {
                            @Override
                            public void done(List<NewsBean> object, BmobException e) {
                                if (e == null) {
                                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                                    refreshCount++;
                                    if (object.size() == 0) {
                                        ToastUtil.show(getActivity(), "暂无更多数据", Toast.LENGTH_SHORT);
                                    } else {
                                        for (NewsBean newsBean : object) {
                                            newsBeen.add(0, newsBean);
                                            LogUtils.e(newsBean.getAuthor());
                                        }
                                        nnnAdapter.setData(newsBeen);
                                        //得到adapter.然后刷新
                                        mRecyclerView.getAdapter().notifyDataSetChanged();
                                        ToastUtil.show(getActivity(), "刷新成功", Toast.LENGTH_SHORT);
                                    }
                                    //停止刷新操作
                                    splMainNews.setRefreshing(false);
                                } else {
                                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }
                }).start();

            }
        });
    }

    protected void initData() {
        nnnAdapter = new NNNAdapter();
        newsBeen = new ArrayList<>();
        BmobQuery<NewsBean> query = new BmobQuery<NewsBean>();
        query.order("-time");
        query.addWhereEqualTo("kind", "c");
        query.setLimit(10);
        query.findObjects(new FindListener<NewsBean>() {
            @Override
            public void done(List<NewsBean> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    for (NewsBean newsBean : object) {
                        newsBeen.add(newsBean);
//                        LogUtils.e(newsBean.getAuthor());
                    }
                    nnnAdapter.setData(newsBeen);
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        mRecyclerView.setAdapter(nnnAdapter);
    }
}