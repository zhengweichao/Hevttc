package top.vchao.hevttc.bean;

import android.support.annotation.DrawableRes;

import top.vchao.hevttc.R;
import xyz.zpayh.adapter.BaseViewHolder;
import xyz.zpayh.adapter.IMultiItem;

/**
 * @ 创建时间: 2017/5/14 on 9:10.
 * @ 描述：模块条目
 * @ 作者: vchao
 */

public class ModuleItem implements IMultiItem {
    @DrawableRes
    private int iconResId;
    private String moduleName;
    private String webUrl;
    private Class clazz;

    public ModuleItem(@DrawableRes int imageResId, String imageTitle, Class clazz) {
        this.iconResId = imageResId;
        this.moduleName = imageTitle;
        this.clazz = clazz;
    }

    public ModuleItem(@DrawableRes int mImageResId, String mImageTitle, String webUrl, Class clazz) {
        this.iconResId = mImageResId;
        this.moduleName = mImageTitle;
        this.webUrl = webUrl;
        this.clazz = clazz;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_module;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.setText(R.id.tv_item_name, moduleName);
        holder.setImage(R.id.iv_item_logo, iconResId);
    }

    @Override
    public int getSpanSize() {
        return 0;
    }
}
