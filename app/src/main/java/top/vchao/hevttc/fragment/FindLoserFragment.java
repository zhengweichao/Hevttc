package top.vchao.hevttc.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
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
import top.vchao.hevttc.adapter.GeneralAdapter;
import top.vchao.hevttc.bean.LoseItem;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/9/14 on 15:29.
 * @ 描述：【失物招领】
 * 捡东西了 ==> 寻找失主 ==> 看谁丢了 ===>  展示 丢东西的人 列表
 * @ 作者: vchao
 */
public class FindLoserFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.spl_main_news)
    SwipeRefreshLayout splMainNews;

    private GeneralAdapter LoseAdapter;
    private ArrayList<LoseItem> LoseBeen;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData() {
        LoseAdapter = new GeneralAdapter();
        LoseBeen = new ArrayList<>();

        BmobQuery<LoseItem> query = new BmobQuery<LoseItem>();
        query.order("-time");
        //返回10条数据
        query.setLimit(10);
        //执行查询方法
        query.findObjects(new FindListener<LoseItem>() {
            @Override
            public void done(List<LoseItem> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    LoseBeen.addAll(object);
                    LoseAdapter.setData(LoseBeen);
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        mRecyclerView.setAdapter(LoseAdapter);
    }

    @Override
    protected void initListener() {
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
//                TODO 网络请求数据（现在是假刷新）

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1500);

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