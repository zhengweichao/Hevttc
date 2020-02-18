package top.vchao.hevttc.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.TeamBean;

/**
 * @ 创建时间: 2017/10/4 on 20:09.
 * @ 描述：组织社团详情页面
 * @ 作者: vchao
 */
public class TeamDetailActivity extends BaseActivity {

    @BindView(R.id.iv_team_detail_logo)
    ImageView ivTeamDetailLogo;
    @BindView(R.id.tv_team_detail_title)
    TextView tvTeamDetailTitle;
    @BindView(R.id.tv_team_detail_time)
    TextView tvTeamDetailTime;
    @BindView(R.id.tv_team_detail_content)
    TextView tvTeamDetailContent;
    private TeamBean teambean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_detail;
    }

    @Override
    protected void initView() {
        teambean = (TeamBean) getIntent().getSerializableExtra("teambean");
    }

    @Override
    protected void initData() {
        tvTeamDetailTitle.setText(teambean.getTitle());
        tvTeamDetailTime.setText(teambean.getTime());
        tvTeamDetailContent.setText(teambean.getContent());
        if (teambean.getPic1() != null) {
            Glide.with(TeamDetailActivity.this)                             //配置上下文
                    .load(teambean.getPic1())      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.mipmap.default_image)           //设置错误图片
                    .placeholder(R.mipmap.default_image)     //设置占位图片
                    .fitCenter()
                    .into(ivTeamDetailLogo);
        }

    }
}
