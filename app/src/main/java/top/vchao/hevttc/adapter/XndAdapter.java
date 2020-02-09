package top.vchao.hevttc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import top.vchao.hevttc.R;
import top.vchao.hevttc.activity.ChooseXndActivity;

/**
 * Created by EsauLu on 2017/2/27.
 */

public class XndAdapter extends ArrayAdapter<String> {


    private ChooseXndActivity context;
    private int resource;
    private String currXnd;
    private String currXqd;

    public XndAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = (ChooseXndActivity) context;
        this.resource = resource;
    }

    public XndAdapter(Context context, int resource, List<String> objects, String currXnd, String currXqd) {
        super(context, resource, objects);
        this.context = (ChooseXndActivity) context;
        this.resource = resource;
        this.currXnd = currXnd;
        this.currXqd = currXqd;
    }

    @NonNull
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

        TextView tv_grade = (TextView) view.findViewById(R.id.tv_grade);
        TextView tv_xnd = (TextView) view.findViewById(R.id.tv_course_xnd);
        TextView tv_xqd = (TextView) view.findViewById(R.id.tv_course_xqd);
        TextView tv1 = (TextView) view.findViewById(R.id.tv_xnd);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_di);
        TextView tv3 = (TextView) view.findViewById(R.id.tv_xqd);

        String[] xs = getItem(position).split("@");
        String grade = xs[0];
        String xnd = xs[1];
        String xqd = xs[2];

        tv_grade.setText(grade);
        tv_xnd.setText(xnd);
        tv_xqd.setText(xqd);

        if (xnd.equals(currXnd) && xqd.equals(currXqd)) {
            tv1.setTextColor(0xffff7f27);
            tv2.setTextColor(0xffff7f27);
            tv3.setTextColor(0xffff7f27);
            tv_grade.setTextColor(0xffff7f27);
            tv_xnd.setTextColor(0xffff7f27);
            tv_xqd.setTextColor(0xffff7f27);
        } else {
            tv1.setTextColor(0xff777777);
            tv2.setTextColor(0xff777777);
            tv3.setTextColor(0xff777777);
            tv_grade.setTextColor(0xff777777);
            tv_xnd.setTextColor(0xff777777);
            tv_xqd.setTextColor(0xff777777);
        }

        return view;

    }

    public void setCurrXnd(String currXnd) {
        this.currXnd = currXnd;
    }

    public void setCurrXqd(String currXqd) {
        this.currXqd = currXqd;
    }

    public String getCurrXnd() {
        return currXnd;
    }

    public String getCurrXqd() {
        return currXqd;
    }
}































