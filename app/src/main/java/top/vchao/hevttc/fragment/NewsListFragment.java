package top.vchao.hevttc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import top.vchao.hevttc.R;
import top.vchao.hevttc.activity.NewsWebActivity;
import top.vchao.hevttc.adapter.GeneralAdapter;
import top.vchao.hevttc.bean.NewsBean;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/10/3 on 12:49.
 * @ 描述：新闻列表页面
 * @ 作者: vchao
 */
public class NewsListFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.spl_main_news)
    SwipeRefreshLayout splMainNews;

    private GeneralAdapter mAdapter;
    private ArrayList<NewsBean> newsBeanList;
    private int refreshCount = 1;

    private static final String BUNDLE_TITLE_TRYPE = "titleType";
    private String mTitleType;

    public static NewsListFragment getInstance(String title) {
        NewsListFragment fra = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE_TRYPE, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void getPreIntent() {
        mTitleType = getArguments().getString(BUNDLE_TITLE_TRYPE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int position) {
                LogUtils.e(newsBeanList.get(position).getContent());
                Intent intent = new Intent(getActivity(), NewsWebActivity.class);
                intent.putExtra("url", newsBeanList.get(position).getContent());
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
                        query.addWhereEqualTo("kind", mTitleType);
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
                                            newsBeanList.add(0, newsBean);
                                            LogUtils.e(newsBean.getAuthor());
                                        }
                                        mAdapter.setData(newsBeanList);
                                        //得到adapter.然后刷新
                                        mRecyclerView.getAdapter().notifyDataSetChanged();
                                        ToastUtil.showShort("刷新成功");
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

    @Override
    public void initData() {
        mAdapter = new GeneralAdapter();
        newsBeanList = new ArrayList<>();
        BmobQuery<NewsBean> query = new BmobQuery<NewsBean>();
        query.order("-time");
        query.addWhereEqualTo("kind", mTitleType);
        query.setLimit(10);
        query.findObjects(new FindListener<NewsBean>() {
            @Override
            public void done(List<NewsBean> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    newsBeanList.addAll(object);
                    mAdapter.setData(newsBeanList);
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

}