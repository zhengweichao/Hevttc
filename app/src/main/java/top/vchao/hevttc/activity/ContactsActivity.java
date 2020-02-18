package top.vchao.hevttc.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import it.gmariotti.recyclerview.itemanimator.SlideInOutLeftItemAnimator;
import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.ContactAdapter;
import top.vchao.hevttc.bean.Teacher;
import top.vchao.hevttc.utils.CommonUtil;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.view.CustomItemDecoration;
import top.vchao.hevttc.view.SideBar;

/**
 * @ 创建时间: 2017/9/28 on 00:12.
 * @ 描述：校园通讯录页面
 * @ 作者: vchao
 */
public class ContactsActivity extends BaseActivity {

    @BindView(R.id.rl_recycle_view)
    RecyclerView rlRecycleView;
    @BindView(R.id.side_bar)
    SideBar sideBar;

    private ContactAdapter mAdapter;
    private CustomItemDecoration decoration;

    List<Teacher> nameList = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView() {
        initTitleBar(getString(R.string.contacts));

        mAdapter = new ContactAdapter(this);

        layoutManager = new LinearLayoutManager(this);
        rlRecycleView.setLayoutManager(layoutManager);
        rlRecycleView.addItemDecoration(decoration = new CustomItemDecoration(this));
        rlRecycleView.setItemAnimator(new SlideInOutLeftItemAnimator(rlRecycleView));
    }

    /**
     * 添加联系人方法
     */
    public void TestAdd() {
        Teacher bean = new Teacher();
        bean.setName("安其拉666");
        nameList.add(bean);
        //对数据源进行排序
        CommonUtil.sortData(nameList);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = CommonUtil.getTags(nameList);
        sideBar.setIndexStr(tagsStr);
        decoration.setDatas(nameList, tagsStr);
        //这里写死位置1 只是为了看动画效果
        mAdapter.add(bean, 1);
    }

    @Override
    protected void initData() {
        BmobQuery<Teacher> query = new BmobQuery<Teacher>();
        //返回100条数据
        query.setLimit(100);
        //执行查询方法
        query.findObjects(new FindListener<Teacher>() {
            @Override
            public void done(List<Teacher> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    nameList.addAll(object);
                    //对数据源进行排序
                    CommonUtil.sortData(nameList);
                    //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
                    String tagsStr = CommonUtil.getTags(nameList);
                    sideBar.setIndexStr(tagsStr);
                    decoration.setDatas(nameList, tagsStr);
                    mAdapter.addAll(nameList);

                    rlRecycleView.setAdapter(mAdapter);
                    sideBar.setIndexChangeListener(new SideBar.indexChangeListener() {
                        @Override
                        public void indexChanged(String tag) {
                            if (TextUtils.isEmpty(tag) || nameList.size() <= 0) return;
                            for (int i = 0; i < nameList.size(); i++) {
                                if (tag.equals(nameList.get(i).getIndexTag())) {
                                    layoutManager.scrollToPositionWithOffset(i, 0);
//                        layoutManager.scrollToPosition(i);
                                    return;
                                }
                            }
                        }
                    });

                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    @Override
    protected void initListener() {

//        点击事件
        mAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳到拨号页面
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + nameList.get(position).getTel()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

}
