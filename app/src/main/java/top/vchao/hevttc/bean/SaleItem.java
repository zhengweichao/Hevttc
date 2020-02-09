package top.vchao.hevttc.bean;

import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import top.vchao.hevttc.R;
import xyz.zpayh.adapter.BaseViewHolder;
import xyz.zpayh.adapter.IMultiItem;

/**
 * @ 创建时间: 2017/10/3 on 22:31.
 * @ 描述：二手交易
 * @ 作者: vchao
 */
public class SaleItem extends BmobObject implements Serializable, IMultiItem {
    private String title;
    private String tel;
    private String author;
    private String price;
    private String isOver;
    private String content;
    private BmobFile pic;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_sale;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.setText(R.id.tv_sale_title, title);
        holder.setText(R.id.tv_sale_author, author);
        holder.setText(R.id.tv_sale_price, price);
        holder.setText(R.id.tv_sale_time, getCreatedAt().substring(0, 10));

        if ("1".equals(isOver)) {
            ImageView iv_img = holder.find(R.id.iv_lose_isok);
            iv_img.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getSpanSize() {
        return 0;
    }
}
