package top.vchao.hevttc.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.Date;

import top.vchao.hevttc.R;
import top.vchao.hevttc.utils.DateUtils;
import top.vchao.hevttc.utils.SPUtils;

/**
 * 窗口小部件
 * <p>
 * Created by EsauLu on 2016-10-09.
 */

public class CourseWidgetProvider extends AppWidgetProvider {

    public static final int PRE_BUTTON = 1;
    public static final int NEXT_BUTTON = 2;
    public static final String UPDATE_UI = "update_all_widget";
    public static int WEEK_DAY = -1;
    public static int WEEK_NUM = -1;


    public CourseWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Uri data = intent.getData();
        if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            //点击按钮发送过来的Intent
            int buttonId = Integer.parseInt(data.getSchemeSpecificPart());
            WEEK_DAY = intent.getIntExtra("day", WEEK_DAY);
            updateAll(context, AppWidgetManager.getInstance(context), buttonId);
        } else if (UPDATE_UI.equals(intent.getAction())) {
            updateAll(context, AppWidgetManager.getInstance(context), 0);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        updateAll(context, appWidgetManager, 0);
    }

    /**
     * 更新界面
     *
     * @param context          上下文
     * @param appWidgetManager AppWidgetManager对象
     * @param buttonId         按钮id
     */
    private void updateAll(Context context, AppWidgetManager appWidgetManager, int buttonId) {

        updateWeekDay(context);
        switch (buttonId) {
            case PRE_BUTTON:
                WEEK_DAY = (WEEK_DAY - 1 + 7) % 7;
                break;
            case NEXT_BUTTON:
                WEEK_DAY = (WEEK_DAY + 1) % 7;
                break;
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_layout);
        remoteViews.setTextViewText(R.id.tv_widget_week_num, "第" + WEEK_NUM + "周");
        remoteViews.setTextViewText(R.id.tv_widget_week_day, getWeekDayStr(WEEK_DAY));

        remoteViews.setOnClickPendingIntent(R.id.ib_pre_day, getBtnPendingIntent(context, PRE_BUTTON));
        remoteViews.setOnClickPendingIntent(R.id.ib_next_day, getBtnPendingIntent(context, NEXT_BUTTON));
        Intent intent = new Intent(context, CourseRemoteViewService.class);
        remoteViews.setRemoteAdapter(R.id.lv_widget_course_list, intent);

        // 相当于获得所有本程序创建的appwidget
        ComponentName componentName = new ComponentName(context, CourseWidgetProvider.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
        if (buttonId != 0) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.lv_widget_course_list);
        }

    }

    /**
     * 更新星期
     */
    private void updateWeekDay(Context context) {
        long currTime = new Date().getTime();
        long beginTime = SPUtils.getBeginTime(context, currTime);
        WEEK_NUM = DateUtils.countCurrWeek(beginTime, currTime);//周数
        if (WEEK_DAY == -1) {
            WEEK_DAY = DateUtils.countCurrWeekDay(beginTime, currTime);//星期几，0代表星期日
        }
    }

    /**
     * 获取PendingIntent对象
     *
     * @param context  上下文
     * @param buttonId 按钮id
     * @return 返回一个PendingIntent对象
     */
    private PendingIntent getBtnPendingIntent(Context context, int buttonId) {
        Intent intent = new Intent(context, CourseWidgetProvider.class);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setData(Uri.parse("course:" + buttonId));
        intent.putExtra("day", WEEK_DAY);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    //获取星期几的字符串
    private String getWeekDayStr(int day) {
        String str = "";
        switch (day) {
            case 0:
                str = "星期日";
                break;
            case 1:
                str = "星期一";
                break;
            case 2:
                str = "星期二";
                break;
            case 3:
                str = "星期三";
                break;
            case 4:
                str = "星期四";
                break;
            case 5:
                str = "星期五";
                break;
            case 6:
                str = "星期六";
                break;
        }
        return str;
    }
}



























