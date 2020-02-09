package top.vchao.hevttc.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.PhotoBean;


public class PicDetailActivity extends BaseActivity {

    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    private PhotoBean bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pic_detail;
    }

    @Override
    void initView() {
        bean = (PhotoBean) getIntent().getSerializableExtra("bean");
    }

    @Override
    void initData() {
//        ivDetail.setImageResource(getIntent().getExtras().getInt("id"));
//        String desc = getIntent().getStringExtra("desc");
//        tvDescription.setText("  "+desc);
        tvDescription.setText(bean.getDescription());
        if (bean.getPic() != null) {
            Glide.with(PicDetailActivity.this)                             //配置上下文
                    .load(bean.getPic())      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.mipmap.default_image)           //设置错误图片
                    .placeholder(R.mipmap.default_image)     //设置占位图片
                    .fitCenter()
                    .into(ivDetail);
        }

    }

}
