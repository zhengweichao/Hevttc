package top.vchao.hevttc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

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
    private ArrayList<LoveBean> LoveBeen;

    private EditText et_love_add_content;
    private EditText et_love_add_touser;
    private String addContent;
    private Switch swi_noname;
    private String addTosuer;
    private boolean addNoName = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_love;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new GeneralAdapter();
        LoveBeen = new ArrayList<>();
        rvLove.setLayoutManager(new LinearLayoutManager(LoveActivity.this));
        BmobQuery<LoveBean> query = new BmobQuery<LoveBean>();
        query.order("-createdAt");
        query.setLimit(20);
        query.findObjects(new FindListener<LoveBean>() {
            @Override
            public void done(List<LoveBean> object, BmobException e) {
                if (e == null) {
                    LogUtils.e("查询成功：共" + object.size() + "条数据。");
                    LoveBeen.addAll(object);
                    mAdapter.setData(LoveBeen);
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
        et_love_add_content = (EditText) view.findViewById(R.id.et_love_add_content);
        et_love_add_touser = (EditText) view.findViewById(R.id.et_love_add_touser);
        swi_noname = (Switch) view.findViewById(R.id.swi_noname);
        addNoName = true;
        swi_noname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addNoName = true;
                } else {
                    addNoName = false;
                }
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(LoveActivity.this)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addTosuer = et_love_add_touser.getText().toString().trim();
                        addContent = et_love_add_content.getText().toString().trim();
                        if (TextUtils.isEmpty(addTosuer) || TextUtils.isEmpty(addContent)) {
                            ToastUtil.show(LoveActivity.this, "请填写完整信息哦！", Toast.LENGTH_SHORT);
                        } else {
                            UpdateLove();
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
     */
    private void UpdateLove() {
        final LoveBean bean = new LoveBean();
        bean.setTouser(addTosuer);
        bean.setContent(addContent);
        if (!addNoName) {
            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            bean.setAuthor(user.getUsername());
        } else {
            bean.setAuthor("匿名");
        }
        bean.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoveActivity.this, "发布表白成功", Toast.LENGTH_SHORT).show();
                    LoveBeen.add(0, bean);
                    mAdapter.setData(LoveBeen);
                    mAdapter.notifyDataSetChanged();

                } else {
                    Log.e("zwc", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }
}
