package top.vchao.hevttc.bean;

import android.text.TextUtils;
import android.view.View;

import cn.bmob.v3.BmobObject;
import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.ColorGenerator;
import top.vchao.hevttc.utils.TextDrawable;
import xyz.zpayh.adapter.BaseViewHolder;
import xyz.zpayh.adapter.IMultiItem;

/**
 * @ 创建时间: 2017/9/18 on 22:04.
 * @ 描述：校园通讯录bean类型
 * @ 作者: vchao
 */

public class Teacher extends BmobObject implements IMultiItem {
    private String indexTag;
    private String name;
    private String department;
    private String tel;

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_contact;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.setText(R.id.tv_contact_name, name);
        if (!TextUtils.isEmpty(department)) {
            holder.setVisibility(R.id.tv_contact_department, View.VISIBLE);
            holder.setText(R.id.tv_contact_department, department);
        } else {
            holder.setVisibility(R.id.tv_contact_department, View.GONE);
        }

        TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        TextDrawable drawable = mDrawableBuilder.build(String.valueOf(name.charAt(0)),
                mColorGenerator.getColor(name));
        holder.setImage(R.id.iv_img, drawable);
    }

    @Override
    public int getSpanSize() {
        return 0;
    }
}
