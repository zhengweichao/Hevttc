package top.vchao.hevttc.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import java.util.ArrayList;

import top.vchao.hevttc.R;
import top.vchao.hevttc.jw.bean.Course;
import top.vchao.hevttc.jw.bean.CourseTable;
import top.vchao.hevttc.utils.SPUtils;

/**
 * 窗口小部件的ListView集合窗口更新服务
 * Created by EsauLu on 2016-10-10.
 */
public class CourseRemoteViewService extends RemoteViewsService {

    public CourseRemoteViewService() {
        super();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CourseWidgetFactory(this, intent);
    }

    public class CourseWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private ArrayList<Course> mCourseList;

        public CourseWidgetFactory(Context context, Intent intent) {
            mContext = context;
            mCourseList = new ArrayList<>();
        }

        private void initData() {

            mCourseList.clear();
            CourseTable ct = null;
            String xnd = SPUtils.getCurrXnd(mContext, "");
            String xqd = SPUtils.getCurrXqd(mContext, "");
            String courseString = SPUtils.getCourseInfo(mContext, xnd + xqd, "");
            Log.i("++//获取课表：", "+++" + xnd + xqd + "+++");
            Log.i("++//获取课表：", "+++" + courseString + "+++");
            if (courseString != null && !courseString.equals("")) {
                //获取课表
                Gson gson = new Gson();
                ct = gson.fromJson(courseString, CourseTable.class);//获取课表对象
            } else {
                return;
            }
            Log.i("++**获取成功：", "+++" + courseString + "+++");
            int weekDay = CourseWidgetProvider.WEEK_DAY;//获取星期几
            int weekNum = CourseWidgetProvider.WEEK_NUM;//获取周数
            int weekState = weekNum % 2 == 0 ? Course.DOUBLE_WEEK : Course.SINGLE_WEEK;

            //查找WEEK_DAY的课程
            mCourseList.clear();
            for (Course c : ct.getCourses()) {
                if (c.getDay() == weekDay) {
                    int cState = c.getWeekState();
                    if (c.getStartWeek() <= weekNum && c.getEndWeek() >= weekNum) {
                        if (cState == Course.ALL_WEEK || cState == weekState) {
                            mCourseList.add(c);
                        }
                    }
                }
            }

        }

        @Override
        public void onCreate() {

            initData();

        }

        @Override
        public void onDataSetChanged() {
            initData();
        }

        @Override
        public void onDestroy() {
            mCourseList.clear();
        }

        @Override
        public int getCount() {
            return mCourseList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Course c = mCourseList.get(position);

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_course_list_item_layout);

            remoteViews.setTextViewText(R.id.tv_number_of_course, String.valueOf(c.getNumber()) + "-" + String.valueOf(c.getNumber() + 1));
            remoteViews.setTextViewText(R.id.tv_name_of_course, c.getName());
            remoteViews.setTextViewText(R.id.tv_classroom_of_course, c.getClassRoom());

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    public class WidgetUIBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}
