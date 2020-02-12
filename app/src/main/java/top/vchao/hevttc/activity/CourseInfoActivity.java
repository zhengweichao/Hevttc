package top.vchao.hevttc.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import top.vchao.hevttc.R;
import top.vchao.hevttc.jw.bean.Course;


/**
 * 显示课程信息
 * Created by FatCat on 2016/10/3.
 */
public class CourseInfoActivity extends BaseAppCompatActivity {

    private Toolbar mToolbar;
    private Course course;

    private TextView courseName;
    private TextView courseRoom;
    private TextView courseTeacher;
    private TextView courseNum;
    private TextView courseWeek;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        course = (Course) intent.getSerializableExtra("course");

        courseName = (TextView) findViewById(R.id.course_name);
        courseRoom = (TextView) findViewById(R.id.course_room);
        courseTeacher = (TextView) findViewById(R.id.course_teacher);
        courseNum = (TextView) findViewById(R.id.course_num);
        courseWeek = (TextView) findViewById(R.id.course_week);

    }

    @Override
    protected void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle(course.getName());// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorBlue));
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_courseinfo;
    }

    @Override
    protected void initData() {


        courseName.setText(course.getName());
        courseRoom.setText(course.getClassRoom());
        courseTeacher.setText(course.getTeacher());
        courseNum.setText(course.getClassTime());
        courseWeek.setText(course.getWeekNum());

    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
