package top.vchao.hevttc.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.Teacher;
import top.vchao.hevttc.utils.ColorGenerator;
import top.vchao.hevttc.utils.TextDrawable;

/**
 * @ 创建时间: 2017/9/24 on 22:02.
 * @ 描述：校园通讯录适配器
 * @ 作者: vchao
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyRecycleHolder> implements View.OnClickListener {

    private List<Teacher> contactBeanList;
    private Context mContext;
    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();

    public ContactAdapter(Context context) {
        this.mContext = context;
        contactBeanList = new ArrayList<>();
    }

    public void addAll(List<Teacher> beans) {
        if (contactBeanList.size() > 0) {
            contactBeanList.clear();
        }
        contactBeanList.addAll(beans);
        notifyDataSetChanged();
    }

    public void add(Teacher bean, int position) {
        contactBeanList.add(position, bean);
        notifyItemInserted(position);
    }

    public void add(Teacher bean) {
        contactBeanList.add(bean);
        notifyItemChanged(contactBeanList.size() - 1);
    }

    @Override
    public MyRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_layout, parent, false);
        MyRecycleHolder vh = new MyRecycleHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyRecycleHolder holder, int position) {
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        if (contactBeanList == null || contactBeanList.size() == 0 || contactBeanList.size() <= position)
            return;
        Teacher bean = contactBeanList.get(position);
        if (bean != null) {
            holder.tv_name.setText(bean.getName());
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(bean.getName().charAt(0)), mColorGenerator.getColor(bean.getName()));
            holder.iv_img.setImageDrawable(drawable);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return contactBeanList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public static class MyRecycleHolder extends RecyclerView.ViewHolder {
        public final TextView tv_name;
        public final ImageView iv_img;

        public MyRecycleHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


}
