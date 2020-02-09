package top.vchao.hevttc.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

import top.vchao.hevttc.R;
import top.vchao.hevttc.bean.Course;

public class CourseActivity0 extends BaseActivity {

    /**
     * 课程表
     */
//    private CourseTable mCourseTable;

    /**
     * 当前周
     */
    private int mCurrWeek;

    /**
     * 选中的周数
     */
    private int mSelectWeek;

    /**
     * 选择周数的下拉框
     */
    private Spinner mWeekDaySpinner;

    /**
     * 下拉框的适配器
     */
//    private  WeekDayAdapter mAdapter;

    /**
     * 周数下拉框的数据
     */
    private ArrayList<String> mWeekArr;

    /**
     * 记录课程的map
     */
//    private HashMap<String, Course> mCourseMap;

    /**
     * 记录课程背景颜色的map
     */
    private HashMap<String, Integer> mBgColorMap;

//    private CourseBroadcast mCourseBroadcast;

    /**
     * 课程页面的button引用，6行7列
     */
    private int[][] lessons = {
            {R.id.lesson17, R.id.lesson11, R.id.lesson12, R.id.lesson13, R.id.lesson14, R.id.lesson15, R.id.lesson16},
            {R.id.lesson27, R.id.lesson21, R.id.lesson22, R.id.lesson23, R.id.lesson24, R.id.lesson25, R.id.lesson26},
            {R.id.lesson37, R.id.lesson31, R.id.lesson32, R.id.lesson33, R.id.lesson34, R.id.lesson35, R.id.lesson36},
            {R.id.lesson47, R.id.lesson41, R.id.lesson42, R.id.lesson43, R.id.lesson44, R.id.lesson45, R.id.lesson46},
            {R.id.lesson57, R.id.lesson51, R.id.lesson52, R.id.lesson53, R.id.lesson54, R.id.lesson55, R.id.lesson56},
            {R.id.lesson67, R.id.lesson61, R.id.lesson62, R.id.lesson63, R.id.lesson64, R.id.lesson65, R.id.lesson66}
    };

    /**
     * 某节课的背景图,用于随机获取
     */
    private int[] bg = {
            R.drawable.kb1, R.drawable.kb2, R.drawable.kb3, R.drawable.kb4, R.drawable.kb5,
            R.drawable.kb6, R.drawable.kb7, R.drawable.kb8, R.drawable.kb9, R.drawable.kb10,
            R.drawable.kb11, R.drawable.kb12, R.drawable.kb13, R.drawable.kb14, R.drawable.kb15,
            R.drawable.kb16, R.drawable.kb17, R.drawable.kb18, R.drawable.kb19, R.drawable.kb20,
            R.drawable.kb21, R.drawable.kb22, R.drawable.kb23, R.drawable.kb24, R.drawable.kb25
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_course0;
    }


    /**
     * 记录课程的map
     */
    private HashMap<String, Course> mCourseMap;


    /**
     * 排列课程到课表界面
     */
    private void rankCourse(int weekNum) {

        for (int i = 0; i < lessons.length; i++) {
            for (int j = 0; j < lessons[i].length; j++) {
                Button btn = (Button) findViewById(lessons[i][j]);
                btn.setText("");
                btn.setBackgroundResource(R.drawable.kb0);
            }
        }

        //记录当前周是单周还是双周
        int currWeekState = (weekNum % 2 == 0) ? Course.DOUBLE_WEEK : Course.SINGLE_WEEK;
        //排列课程信息
        for (final String key : mCourseMap.keySet()) {
            Course course = mCourseMap.get(key);
            int courseState = course.getWeekState();//获取该课程的上课周的单双周
            int x = course.getNumber();
            int y = course.getDay() % 7;
            Button btn = (Button) findViewById(lessons[x / 2][y]);
            String oldText = (String) btn.getText();//取得当前按钮上的文本
            String newText = key.split("\\*")[0];//
            if (oldText.equals(newText)) continue;//若该课程是已经存在的课程，不处理
            if (courseState == Course.ALL_WEEK || courseState == currWeekState || oldText == null || oldText.equals("")) {
                btn.setText(newText);
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundResource(bg[6]);
                //注册点击课程时的时间监听器，用于打开课程详细信息界面
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Course c = mCourseMap.get(key);
                        if (c == null) {
                            return;
                        }
                        Intent intent = new Intent(CourseActivity0.this, CourseActivity0.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("course", c);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

        }
        //填充颜色
        for (int i = 0; i < lessons.length; i++) {
            for (int j = 0; j < lessons[i].length; j++) {
                Button btn = (Button) findViewById(lessons[i][j]);
                String key = btn.getText() + "*" + (2 * i + 1) + j;
                Course c = mCourseMap.get(key);//获得该课程的背景颜色
                if (c == null) continue;
                int state = c.getWeekState();
                if (weekNum >= c.getStartWeek() && weekNum <= c.getEndWeek()) {
                    if (state == Course.ALL_WEEK || state == currWeekState) {
                        //该课程当周要上课，则设置背景颜色
                        btn.setTextColor(Color.WHITE);
                        btn.setBackgroundResource(mBgColorMap.get(c.getName()));
                        continue;
                    }
                }
                //该课程当周不上课，则设置背景颜色为灰色
                btn.setTextColor(0x55555555);
//                btn.setBackgroundResource(R.drawable.unavailable);
            }
        }

    }

}
