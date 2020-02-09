package top.vchao.hevttc.bean;

import android.graphics.Point;
import android.text.TextUtils;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.bmob.v3.BmobObject;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.FrescoUtil;
import top.vchao.hevttc.utils.LogUtils;
import xyz.zpayh.adapter.BaseViewHolder;
import xyz.zpayh.adapter.IMultiItem;

/**
 * @ 创建时间: 2017/9/20 on 16:32.
 * @ 描述：新闻bean类
 * @ 作者: vchao
 */

public class NewsBean extends BmobObject implements IMultiItem {

    private String title;
    private String author;
    private String time;
    private String tag;
    private String pic1;
    private String pic2;
    private String pic3;
    private String content;
    private Point mImageViewSize;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public NewsBean(String tag) {
        this.tag = tag;
    }

    public NewsBean(String title, String author, String time, String tag, String pic1, String pic2, String pic3) {
        this.title = title;
        this.author = author;
        this.time = time;
        this.tag = tag;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    @Override
    public int getLayoutRes() {
        if ("3".equals(tag)) {
            return R.layout.item_news_type3;
        } else if ("2".equals(tag)) {
            return R.layout.item_news_type2;
        } else {
            return R.layout.item_news_type1;
        }
    }

    @Override
    public void convert(BaseViewHolder holder) {

        if ("3".equals(tag)) {
            holder.setText(R.id.tv_type3_title, title);
            holder.setText(R.id.tv_type3_author, author);
            holder.setText(R.id.tv_type3_time, time);
            try {
                if (!TextUtils.isEmpty(pic1)) {
                    SimpleDraweeView sdv1 = holder.find(R.id.iv_news_type3_1);
                    FrescoUtil.setWrapImage(sdv1, pic1);
                }
                if (!TextUtils.isEmpty(pic2)) {
                    SimpleDraweeView sdv2 = holder.find(R.id.iv_news_type3_2);
                    FrescoUtil.setWrapImage(sdv2, pic2);
                }
                if (!TextUtils.isEmpty(pic3)) {
                    SimpleDraweeView sdv3 = holder.find(R.id.iv_news_type3_3);
                    FrescoUtil.setWrapImage(sdv3, pic3);
                }
            } catch (Exception e) {
                LogUtils.e("出现异常，图片没有加载完成");
            }
//            LogUtils.e("新闻列表图片加载完毕");
        } else if ("2".equals(tag)) {
            holder.setText(R.id.tv_type2_title, title);
            holder.setText(R.id.tv_type2_author, author);
            holder.setText(R.id.tv_type2_time, time);
            try {
                if (!TextUtils.isEmpty(pic1)) {
                    SimpleDraweeView sdv1 = holder.find(R.id.iv_news_type2);
                    FrescoUtil.setWrapImage(sdv1, pic1);
                }
            } catch (Exception e) {
                LogUtils.e("出现异常，图片没有加载完成");
            }
//            LogUtils.e("新闻列表图片加载完毕");

        } else {
            holder.setText(R.id.tv_type1_title, title);
            holder.setText(R.id.tv_type1_author, author);
            holder.setText(R.id.tv_type1_time, time);
        }

    }

    @Override
    public int getSpanSize() {
        return 0;
    }
}
