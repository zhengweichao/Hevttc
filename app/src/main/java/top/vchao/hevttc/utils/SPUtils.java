package top.vchao.hevttc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import top.vchao.hevttc.constant.SPkey;

/**
 * @ 创建时间: 2017/7/18 on 12:58.
 * @ 描述：SharedPreferences封装类
 * @ 作者: vchao
 */
public class SPUtils {
    //  保存值
    public static void save(Context context, String key, String value) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SPkey.SpName,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //删除
    public static void delete(Context context, String key) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SPkey.SpName,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, SPkey.DEFAULT);
        editor.commit();

    }

    //查看
    public static String look(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPkey.SpName,
                Activity.MODE_PRIVATE);
        String data = sharedPreferences.getString(key, SPkey.DEFAULT);
        return data;
    }

    //查看
    public static String look(Context context, String key, String default_key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPkey.SpName,
                Activity.MODE_PRIVATE);
        String data = sharedPreferences.getString(key, default_key);
        return data;
    }

    //删除全部
    public static void deleteAll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SPkey.SpName, Activity.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    /**
     * 判断本地是否存储该值。
     * 有值 ====   true
     * 无值  ====  false
     *
     * @param value
     * @return
     */
    public static boolean isHave(Context context, String value) {
        if (!TextUtils.isEmpty(value)) {
            String look = SPUtils.look(context, value);
            if (!TextUtils.isEmpty(look) && (!SPkey.DEFAULT.equals(look))) {

                return true;
            }
        }
        return false;
    }

    /**
     * 根据值获取存储的键名
     */
    public static String getKeyByValue(Context context, String value, String default_value) {
        SharedPreferences spf = context.getSharedPreferences(SPkey.SpName, Activity.MODE_PRIVATE);
        Map<String, ?> key_Value = (Map<String, ?>) spf.getAll(); //获取所有保存在对应标识下的数据，并以Map形式返回

        /* 只需遍历即可得到存储的key和value值*/
        for (Map.Entry<String, ?> entry : key_Value.entrySet()) {
            try {
                if (TextUtils.equals(look(context, entry.getKey()), value)) {
                    return entry.getKey();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return default_value;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(SPkey.SpName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, null);
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(SPkey.SpName,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SPkey.SpName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SPkey.SpName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SPkey.SpName,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SPkey.SpName,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    public static final String PREF_NAME = "config";

    public static void setXndInfo(Context ctx, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString("courseXnds", value).commit();
    }

//    public static void setCourseInfo(Context ctx, String value) {
//        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
//                Context.MODE_PRIVATE);
//        sp.edit().putString("courseTable", value).commit();
//    }

    public static void setCourseInfo(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getXndInfo(Context ctx, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getString("courseXnds", defaultValue);
    }

//    public static String  getCourseInfo(Context ctx, String defaultValue) {
//        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
//                Context.MODE_PRIVATE);
//        return  sp.getString("courseTable",defaultValue);
//    }

    public static String getCourseInfo(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static final void setBeginTime(Context ctx, long value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putLong("begintime", value).commit();
    }

    public static final long getBeginTime(Context ctx, long defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getLong("begintime", defaultValue);
    }


    public static final void setJwUrl(Context ctx, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString("jwurl", value).commit();
    }

    public static final String getJwUrl(Context ctx, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString("jwurl", defaultValue);
    }

    public static final void setCurrXnd(Context ctx, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString("currXnd", value).commit();
    }

    public static final String getCurrXnd(Context ctx, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString("currXnd", defaultValue);
    }

    public static final void setCurrXqd(Context ctx, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString("currXqd", value).commit();
    }

    public static final String getCurrXqd(Context ctx, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString("currXqd", defaultValue);
    }
}
