package top.vchao.hevttc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.PhotoBean;

/**
 * @ 创建时间: 2017/6/8 on 13:23.
 * @ 描述：
 * @ 作者: vchao
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private Context context;
    private List<PhotoBean> listData;

    public RecyclerAdapter(Context context, List<PhotoBean> listData) {
        this.context = context;
        this.listData = listData;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * 创建一个ViewHolder对象
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建一个根布局
        View itemView = View.inflate(context, R.layout.item_recycler, null);
        return new RecyclerHolder(itemView);
    }

    /**
     * 绑定一个ViewHolder
     * * @param holder
     *
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerHolder holder, int position) {
        holder.setDataAndRefreshUI(listData.get(position));

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }

    }

    /**
     * 列表的Item的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (listData != null) {
            return listData.size();
        }
        return 0;
    }

    /**
     * @Description:ReclyclerView的Holder
     */
    public class RecyclerHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;
        private TextView itemText;

        /**
         * 初始化ViewHolder
         *
         * @param itemView
         */
        public RecyclerHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.iv_icon);
            itemText = (TextView) itemView.findViewById(R.id.tv_name);
        }

        /**
         * 设置数据刷新UI
         *
         * @param listBean
         */
        public void setDataAndRefreshUI(PhotoBean listBean) {
            itemText.setText(listBean.getName());
//            itemImage.setImageResource(listBean.getIconId());
        }
    }

}
