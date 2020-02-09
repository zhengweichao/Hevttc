package top.vchao.hevttc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ 创建时间: 2017/5/25 on 17:14.
 * @ 描述：时间工具类
 * @ 作者: vchao
 */

public class TimeUtil {
    public static final SimpleDateFormat DATE_FORMAT_MIN = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtil() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String getYear() {
        return getTime(getCurrentTimeInLong(), new SimpleDateFormat("yyyy"));
    }

    public static String getMonth() {
        return getTime(getCurrentTimeInLong(), new SimpleDateFormat("MM"));
    }

    public static String getDate() {
        return getTime(getCurrentTimeInLong(), new SimpleDateFormat("dd"));
    }


}

