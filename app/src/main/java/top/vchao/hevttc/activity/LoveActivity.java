package top.vchao.hevttc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.GeneralAdapter;
import top.vchao.hevttc.bean.LoveBean;
import top.vchao.hevttc.bean.MyUser;
import top.vchao.hevttc.utils.CommonUtil;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;

/**
 * @ 创建时间: 2017/6/13 on 16:03.
 * @ 描述：表白墙页面
 * @ 作者: vchao
 */
public class LoveActivity extends BaseActivity {

    @BindView(R.id.rv_love)
    RecyclerView rvLove;

    private GeneralAdapter mAdapter;
    private ArrayList<LoveBean> mLoveBeanList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_love;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new GeneralAdapter();
        mLoveBeanList = new ArrayList<>();
        rvLove.setLayoutManager(new LinearLayoutManager(LoveActivity.this));
        BmobQuery<LoveBean> query = new BmobQuery<LoveBean>();
        query.order("-createdAt");
        query.setLimit(20);
        query.findObjects(new FindListener<LoveBean>() {
            @Override
            public void done(List<LoveBean> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    mLoveBeanList.addAll(object);
                    mAdapter.setData(mLoveBeanList);
                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        rvLove.setAdapter(mAdapter);
    }

    @OnClick(R.id.iv_add_love)
    public void onViewClicked() {
//        弹出对话框
        View view = LayoutInflater.from(LoveActivity.this).inflate(R.layout.dialog_love_add, null);
        final EditText et_love_add_content = (EditText) view.findViewById(R.id.et_love_add_content);
        final EditText et_love_add_touser = (EditText) view.findViewById(R.id.et_love_add_touser);
        final Switch switchAnonymous = (Switch) view.findViewById(R.id.swi_noname);

        AlertDialog dialog = new AlertDialog.Builder(LoveActivity.this)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CommonUtil.isAllNotNull(et_love_add_touser, et_love_add_content)) {
                            String addTosuer = CommonUtil.getText(et_love_add_touser);
                            String addContent = CommonUtil.getText(et_love_add_content);
                            addLoveInfo(addTosuer, addContent, switchAnonymous.isChecked());
                        } else {
                            ToastUtil.showShort("请填写完整信息哦！");
                        }
                    }
                })
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 发布表白墙信息
     *
     * @param addTosuer  要表白的人
     * @param addContent 要发布的表白内容
     * @param isRealName 是否实名
     */
    private void addLoveInfo(String addTosuer, String addContent, boolean isRealName) {
        final LoveBean bean = new LoveBean();
        bean.setTouser(addTosuer);
        bean.setContent(addContent);
        if (isRealName) {
            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            bean.setAuthor(user.getUsername());
        } else {
            bean.setAuthor("匿名");
        }
        bean.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    ToastUtil.showShort("发布表白成功");
                    mLoveBeanList.add(0, bean);
                    mAdapter.setData(mLoveBeanList);
                    mAdapter.notifyDataSetChanged();

                } else {
                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }
}
