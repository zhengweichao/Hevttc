package top.vchao.hevttc.bean;

import android.widget.ImageView;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.GlideImageLoader;
import xyz.zpayh.adapter.BaseViewHolder;
import xyz.zpayh.adapter.IMultiItem;

/**
 * @ 创建时间: 2017/10/4 on 22:46.
 * @ 描述：图说校园
 * @ 作者: vchao
 */
public class PhotoBean extends BmobObject implements Serializable, IMultiItem {

    private String name;
    private String pic;
    private String description;

    public PhotoBean() {
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_photo_school;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        if (!(pic == null)) {
            final ImageView sdv1 = holder.find(R.id.iv_icon);
            GlideImageLoader.setImage(sdv1, pic);
        }
    }

    @Override
    public int getSpanSize() {
        return 0;
    }

}
