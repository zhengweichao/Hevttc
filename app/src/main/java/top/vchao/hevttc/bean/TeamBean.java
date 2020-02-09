package top.vchao.hevttc.bean;

import android.text.TextUtils;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.FrescoUtil;
import top.vchao.hevttc.utils.LogUtils;
import xyz.zpayh.adapter.BaseViewHolder;
import xyz.zpayh.adapter.IMultiItem;

/**
 * @ 创建时间: 2017/9/20 on 16:32.
 * @ 描述：社团组织
 * @ 作者: vchao
 */

public class TeamBean extends BmobObject implements IMultiItem, Serializable {

    private String title;
    private String title2;
    private String time;
    private String pic1;
    private String content;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_team;
    }

    @Override
    public void convert(BaseViewHolder holder) {

        holder.setText(R.id.tv_team_title, title);
        holder.setText(R.id.tv_team_title2, title2);
        holder.setText(R.id.tv_team_time, time);

        try {
            if (!TextUtils.isEmpty(pic1)) {
                SimpleDraweeView sdv1 = holder.find(R.id.iv_team_logo);
                FrescoUtil.setWrapImage(sdv1, pic1);
            }
        } catch (Exception e) {
            LogUtils.e("出现异常，图片没有加载完成");
        }
        LogUtils.e("新闻列表图片加载完毕");
    }

    @Override
    public int getSpanSize() {
        return 0;
    }
}
