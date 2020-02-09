package top.vchao.hevttc.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.FindItem;
import top.vchao.hevttc.bean.LoseItem;
import top.vchao.hevttc.utils.LogUtils;

/**
 * @ 创建时间: 2017/10/3 on 16:03.
 * @ 描述：失物招领详情页面
 * @ 作者: vchao
 */
public class LoseDetailActivity extends BaseActivity {
    @BindView(R.id.tv_detail_lose_title)
    TextView tvDetailLoseTitle;
    @BindView(R.id.tv_detail_lose_author)
    TextView tvDetailLoseAuthor;
    @BindView(R.id.tv_detail_lose_time)
    TextView tvDetailLoseTime;
    @BindView(R.id.tv_detail_foods_desc)
    TextView tvDetailLoseDesc;
    @BindView(R.id.tv_detail_foods_tel)
    TextView tvDetailFoodsTel;
    @BindView(R.id.iv_lose_detail)
    ImageView ivLoseDetail;
    @BindView(R.id.bt_detail_lose_tel)
    Button btDetailLoseTel;
    private LoseItem losebean;
    private FindItem findbean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lose_detail;
    }

    @Override
    void initView() {
        losebean = (LoseItem) getIntent().getSerializableExtra("losebean");
        findbean = (FindItem) getIntent().getSerializableExtra("findbean");
        if (losebean != null) {
            tvDetailLoseTitle.setText(losebean.getTitle());
            tvDetailLoseAuthor.setText(losebean.getAuthor());
            tvDetailLoseTime.setText(losebean.getTime());
            tvDetailLoseDesc.setText(losebean.getContent());
            tvDetailFoodsTel.setText(losebean.getTel());

            if (losebean.getPic() != null) {
                ivLoseDetail.setVisibility(View.VISIBLE);
                Glide.with(LoseDetailActivity.this)                             //配置上下文
                        .load(losebean.getPic().getFileUrl())      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                        .error(R.mipmap.default_image)           //设置错误图片
                        .placeholder(R.mipmap.default_image)     //设置占位图片
                        .centerCrop()
                        .into(ivLoseDetail);
            }
        } else {
            tvDetailLoseTitle.setText(findbean.getTitle());
            tvDetailLoseAuthor.setText(findbean.getAuthor());
            tvDetailLoseTime.setText(findbean.getTime());
            tvDetailLoseDesc.setText(findbean.getContent());
            tvDetailFoodsTel.setText(findbean.getTel());

            if (findbean.getPic() != null) {
                ivLoseDetail.setVisibility(View.VISIBLE);
                Glide.with(LoseDetailActivity.this)                             //配置上下文
                        .load(findbean.getPic().getFileUrl())      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                        .error(R.mipmap.default_image)           //设置错误图片
                        .placeholder(R.mipmap.default_image)     //设置占位图片
                        .fitCenter()                            //完全显示
                        .into(ivLoseDetail);
            }
        }
    }

    @OnClick(R.id.bt_detail_lose_tel)
    public void onViewClicked() {
        if (!(losebean == null)) {
            LogUtils.e("TEL:" + losebean.getTel());
            //跳到拨号页面
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + losebean.getTel()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (!(findbean == null)) {
            LogUtils.e("TEL:" + findbean.getTel());
            //跳到拨号页面
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + findbean.getTel()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "该用户没有留下电话信息，请私信尝试", Toast.LENGTH_SHORT).show();
        }
    }


}
