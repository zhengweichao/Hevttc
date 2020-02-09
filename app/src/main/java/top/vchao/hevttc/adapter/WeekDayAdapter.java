package top.vchao.hevttc.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import top.vchao.hevttc.R;
import top.vchao.hevttc.activity.CourseActivity;

/**
 * 周数下拉框适配器
 * Created by EsauLu on 2016-10-04.
 */

public class WeekDayAdapter extends ArrayAdapter<String> {

    private CourseActivity context;
    private int resource;

    public WeekDayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = (CourseActivity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view;

        if (convertView == null) {
            view = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(resource, view, true);
        } else {
            view = (LinearLayout) convertView;
        }

        String item = getItem(position);
        long id = getItemId(position);
        TextView weekNum = (TextView) view.findViewById(R.id.tv_week_num);
        TextView xnd = (TextView) view.findViewById(R.id.tv_xnd);

        int currWeek = context.getCurrWeek();
        String s = context.getCurrXnd() + "学年 第" + context.getCurrXqd() + "学期";
        if (id + 1 == currWeek) {
            item += "(本周)";
        } else {
            item = item + "(非本周)";
        }
        weekNum.setText(item);
        xnd.setText(s);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LinearLayout view;
        if (convertView == null) {
            view = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(R.layout.week_day_drop_item_layout, view, true);
        } else {
            view = (LinearLayout) convertView;
        }

        String item = getItem(position);
        long id = getItemId(position);
        TextView weekNum = (TextView) view.findViewById(R.id.tv_week_item);
        int currWeek = context.getCurrWeek();
        if (id + 1 == currWeek) {
            item += "(本周)";
            weekNum.setBackgroundResource(R.drawable.bg1);
            weekNum.setTextColor(Color.WHITE);
        } else {
            weekNum.setBackgroundColor(Color.WHITE);
            weekNum.setTextColor(0xff2e94da);
        }
        weekNum.setText(item);

        return view;
    }
}

























