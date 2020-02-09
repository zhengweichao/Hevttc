package top.vchao.hevttc.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import top.vchao.hevttc.MyApplication;


/**
 * @author Administrator
 * @version $Rev: 8 $
 * @time 2015-7-15 上午10:59:15
 * @des 和ui相关的工具类
 * @updateAuthor $Author: admin $
 * @updateDate $Date: 2015-07-15 17:06:45 +0800 (星期三, 15 七月 2015) $
 * @updateDes TODO
 */
public class UIUtils {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 得到Resouce对象
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串
     */
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到String.xml中的字符串数组
     */
    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**
     * 得到应用程序的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 得到主线程id
     */
    public static long getMainThreadid() {
        return MyApplication.getMainTreadId();
    }

    /**
     * 得到主线程Handler
     */
    public static Handler getMainThreadHandler() {
        return MyApplication.getHandler();
    }

    /**
     * 安全的执行一个任务
     */
    public static void postTaskSafely(Runnable task) {
        int curThreadId = android.os.Process.myTid();

        if (curThreadId == getMainThreadid()) {// 如果当前线程是主线程
            task.run();
        } else {// 如果当前线程不是主线程
            getMainThreadHandler().post(task);
        }

    }

    public static ProgressDialog getProcessDialog(Context context, String tips) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setCancelable(false);
        return dialog;
    }


}
