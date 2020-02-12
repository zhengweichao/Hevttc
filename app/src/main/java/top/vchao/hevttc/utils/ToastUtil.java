package top.vchao.hevttc.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import top.vchao.hevttc.MyApplication;
import top.vchao.hevttc.R;

/**
 * @ 创建时间: 2017/10/2 on 8:29.
 * @ 描述：吐司工具类
 * @ 作者: vchao
 */

public class ToastUtil {
    private static Toast toast;

    /**
     * 短时间显示  Toast
     *
     * @param message 吐司内容
     */
    public static void showShort(CharSequence message) {
        show(message, Toast.LENGTH_SHORT);
    }


    /**
     * 长时间显示Toast
     *
     * @param message 吐司内容
     */
    public static void showLong(CharSequence message) {
        show(message, Toast.LENGTH_LONG);
    }


    /**
     * 自定义显示时间
     *
     * @param sequence 吐司内容
     * @param duration 吐司时长
     */
    public static void show(CharSequence sequence, int duration) {
        try {
            LogUtils.e("  吐司：  " + sequence);
            if (toast == null) {
                toast = new Toast(MyApplication.INSTANCE());
            }
            toast.setGravity(Gravity.BOTTOM, 0, 120);
            View v = LayoutInflater.from(MyApplication.INSTANCE()).inflate(R.layout.base_toast_layout, null);
            TextView tv2 = (TextView) v.findViewById(R.id.tv_toast);
            tv2.setText(sequence);
            toast.setView(v);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Looper.prepare();
                Toast.makeText(MyApplication.INSTANCE(), sequence, duration).show();
                Looper.loop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void show(Context context, CharSequence sequence, int duration) {
        show(sequence, duration);
    }

    /**
     * 隐藏toast
     */
    public static void hideToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
