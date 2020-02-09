package top.vchao.hevttc.utils;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ 创建时间: 2017/4/29 on 16:35.
 * @ 描述：数据库加载工具类
 * @ 作者: vchao
 */
public class InitDBUtils {
    public static void initDB(String DBname, Activity mactivity) {
        Log.e("zwc", "initDB:开始加载数据库");
        InputStream is = null;
        try {
            is = mactivity.getAssets().open(DBname);
            File file = new File(mactivity.getFilesDir(), DBname);
            if (file.exists()) {
                return;
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            LogUtils.e("加载完毕");
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("加载异常");
        }
    }

}
