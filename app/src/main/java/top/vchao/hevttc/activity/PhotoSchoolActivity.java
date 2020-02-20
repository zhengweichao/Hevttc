package top.vchao.hevttc.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
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
import top.vchao.hevttc.adapter.GeneralAdapter;
import top.vchao.hevttc.bean.PhotoBean;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.RomUtils;
import top.vchao.hevttc.utils.ToastUtil;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/10/4 on 22:09.
 * @ 描述：图说校园
 * @ 作者: vchao
 */
public class PhotoSchoolActivity extends BaseActivity {

    @BindView(R.id.rv_photo_school)
    RecyclerView rvPhotoSchool;
    @BindView(R.id.spl_main)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<PhotoBean> photoBeanList;
    private GeneralAdapter photoAdapter;
    private int refreshCount = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_school;
    }

    protected void initData() {
        photoAdapter = new GeneralAdapter();
        photoBeanList = new ArrayList<PhotoBean>();
        BmobQuery<PhotoBean> query = new BmobQuery<PhotoBean>();
        query.order("id");
        //返回10条数据，默认返回10条数据
        query.setLimit(10);
        //执行查询方法
        query.findObjects(new FindListener<PhotoBean>() {
            @Override
            public void done(List<PhotoBean> object, BmobException e) {
                if (e == null) {
                    photoBeanList.addAll(object);
                    photoAdapter.setData(photoBeanList);
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        rvPhotoSchool.setAdapter(photoAdapter);

    }

    @Override
    protected void initListener() {
        photoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int position) {
                Intent intent = new Intent(PhotoSchoolActivity.this, PicDetailActivity.class);
                intent.putExtra("bean", photoBeanList.get(position));
                if (RomUtils.isAndroid5()) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(PhotoSchoolActivity.this,
                            view.findViewById(R.id.iv_icon),
                            "shareView").toBundle();
                    startActivity(intent, bundle);
                } else {
                    startActivity(intent);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1500);
                        BmobQuery<PhotoBean> query = new BmobQuery<PhotoBean>();
                        query.order("id");
                        query.setLimit(10);
                        query.setSkip(10 * refreshCount);
                        //执行查询方法
                        query.findObjects(new FindListener<PhotoBean>() {
                            @Override
                            public void done(List<PhotoBean> object, BmobException e) {
                                if (e == null) {
                                    refreshCount++;
                                    if (object.size() == 0) {
                                        ToastUtil.showShort("暂无更多数据");
                                    } else {
                                        for (PhotoBean bean : object) {
                                            photoBeanList.add(0, bean);
                                        }
                                        photoAdapter.setData(photoBeanList);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                    //停止刷新操作
                                    mSwipeRefreshLayout.setRefreshing(false);
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

}