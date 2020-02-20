package top.vchao.hevttc.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.GeneralAdapter;
import top.vchao.hevttc.bean.Teacher;
import top.vchao.hevttc.utils.CommonUtil;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.view.CustomItemDecoration;
import top.vchao.hevttc.view.SideBar;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/9/28 on 00:12.
 * @ 描述：校园通讯录页面
 * @ 作者: vchao
 */
public class ContactsActivity extends BaseActivity {

    @BindView(R.id.rv_contact)
    RecyclerView rvContact;
    @BindView(R.id.side_bar)
    SideBar sideBar;

    private GeneralAdapter mAdapter;
    private CustomItemDecoration decoration;

    List<Teacher> contactList = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView() {
        initTitleBar(getString(R.string.contacts));

        mAdapter = new GeneralAdapter();

        layoutManager = new LinearLayoutManager(this);
        rvContact.setLayoutManager(layoutManager);
        rvContact.addItemDecoration(decoration = new CustomItemDecoration(this));
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
                    contactList.addAll(object);
                    //对数据源进行排序
                    CommonUtil.sortData(contactList);
                    //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
                    String tagsStr = CommonUtil.getTags(contactList);
                    sideBar.setIndexStr(tagsStr);
                    decoration.setDatas(contactList, tagsStr);
                    mAdapter.setData(contactList);

                    rvContact.setAdapter(mAdapter);
                    sideBar.setIndexChangeListener(new SideBar.indexChangeListener() {
                        @Override
                        public void indexChanged(String tag) {
                            if (TextUtils.isEmpty(tag) || contactList.size() <= 0) return;
                            for (int i = 0; i < contactList.size(); i++) {
                                if (tag.equals(contactList.get(i).getIndexTag())) {
                                    layoutManager.scrollToPositionWithOffset(i, 0);
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
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int position) {
                //跳到拨号页面
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactList.get(position).getTel()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

}
