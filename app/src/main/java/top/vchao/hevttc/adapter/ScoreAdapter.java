package top.vchao.hevttc.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import top.vchao.hevttc.R;
import top.vchao.hevttc.activity.ScoreAtivity;
import top.vchao.hevttc.jw.bean.CourseScore;

/**
 * 成绩适配器
 * <p>
 * Created by EsauL on 2016-10-07.
 */

public class ScoreAdapter extends ArrayAdapter<CourseScore> {

    private ScoreAtivity context;
    private int resource;

    public ScoreAdapter(Context context, int resource, List<CourseScore> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = (ScoreAtivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout view = null;
        if (convertView == null) {
            view = new LinearLayout(getContext());
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, view, true);
        } else {
            view = (LinearLayout) convertView;
        }

        TextView name = (TextView) view.findViewById(R.id.tv_course_name);
        TextView score = (TextView) view.findViewById(R.id.tv_course_score);
        TextView xnd = (TextView) view.findViewById(R.id.tv_course_xn);

        CourseScore cs = getItem(position);

        name.setText(cs.getName());
        name.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
        score.setText(cs.getScore());
        xnd.setText(cs.getXnd() + "学年第" + cs.getXqd() + "学期");

        return view;

    }
}




































