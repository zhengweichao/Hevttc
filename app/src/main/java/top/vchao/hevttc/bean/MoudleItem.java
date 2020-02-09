package top.vchao.hevttc.bean;

import android.support.annotation.DrawableRes;

/**
 * @ 创建时间: 2017/5/14 on 9:10.
 * @ 描述：模块条目
 * @ 作者: vchao
 */

public class MoudleItem {
    @DrawableRes
    private int mImageResId;
    private String mImageTitle;
    private Class clazz;


    public MoudleItem(int imageResId, String imageTitle, Class clazz) {
        this.mImageResId = imageResId;
        this.mImageTitle = imageTitle;
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public int getmImageResId() {
        return mImageResId;
    }

    public void setmImageResId(int mImageResId) {
        this.mImageResId = mImageResId;
    }

    public String getmImageTitle() {
        return mImageTitle;
    }

    public void setmImageTitle(String mImageTitle) {
        this.mImageTitle = mImageTitle;
    }
}
