package top.vchao.hevttc.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
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
import top.vchao.hevttc.activity.LoseDetailActivity;
import top.vchao.hevttc.adapter.NNNAdapter;
import top.vchao.hevttc.bean.FindItem;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/9/14 on 15:29.
 * @ 描述：【寻物启事】
 * 丢东西了 ==> 要找东西  ==> 看谁捡了 ===>  展示捡到的东西的列表
 * @ 作者: vchao
 */
public class FindThingFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.spl_main_news)
    SwipeRefreshLayout splMainNews;

    private NNNAdapter findAdapter;
    private ArrayList<FindItem> findBeen;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    public void initData() {
        findAdapter = new NNNAdapter();
        findBeen = new ArrayList<>();
        BmobQuery<FindItem> query = new BmobQuery<FindItem>();
        query.order("-time");
        query.setLimit(10);
        //执行查询方法
        query.findObjects(new FindListener<FindItem>() {
            @Override
            public void done(List<FindItem> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    for (FindItem findItem : object) {
                        findBeen.add(findItem);
                        LogUtils.e(findItem.getAuthor());
                    }
                    findAdapter.setData(findBeen);
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        mRecyclerView.setAdapter(findAdapter);
    }

    @Override
    public void initListener() {
        findAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int position) {
                Intent intent = new Intent(getActivity(), LoseDetailActivity.class);
                intent.putExtra("findbean", findBeen.get(position));
                startActivity(intent);
            }
        });
        splMainNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                TODO 网络请求数据（现在是假刷新）

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1500);
                        //停止刷新操作，
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //停止刷新操作
                                splMainNews.setRefreshing(false);
                                ToastUtil.showShort("没有更多数据了！");
                            }
                        });
                    }
                }).start();

            }
        });

    }

}