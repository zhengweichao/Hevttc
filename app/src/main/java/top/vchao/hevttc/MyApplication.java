package top.vchao.hevttc;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cookie.store.PersistentCookieStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.v3.Bmob;
import top.vchao.hevttc.bean.DaoMaster;
import top.vchao.hevttc.bean.DaoSession;
import top.vchao.hevttc.constant.Constant;
import top.vchao.hevttc.utils.LogUtils;

/**
 * @ 创建时间: 2017/6/10 on 17:36.
 * @ 描述：MyApplication
 * @ 作者: vchao
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainTreadId;
    private static Looper mMainLooper;
    private static Handler mHandler;
    private static DaoSession daoSession;
    private static MyApplication INSTANCE;

    public static Handler getHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainTreadId() {
        return mMainTreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static MyApplication INSTANCE() {
        return INSTANCE;
    }

    private static void setBmobIMApplication(MyApplication a) {
        MyApplication.INSTANCE = a;
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    private void setInstance(MyApplication app) {
        setBmobIMApplication(app);
    }

    @Override
    public void onCreate() {
        // 初始化化一些.常用属性.然后放到盒子里面来
        mContext = getApplicationContext();

        // 主线程
        mMainThread = Thread.currentThread();

        // 主线程Id
        mMainTreadId = android.os.Process.myTid();

        // tid thread
        // uid user
        // pid process
        // 主线程Looper对象
        mMainLooper = getMainLooper();

        // 定义一个handler

        mHandler = new Handler();

        super.onCreate();

        initOKGO();

        //Bmob初始化
        Bmob.initialize(this, Constant.BMOB_APP_KEY);

        //配置数据库
//        setupDatabase();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
        LogUtils.i("onCreate: MyApplication配置完成。");
    }

    private void initOKGO() {
        //必须调用初始化
        OkGo.init(this);

        try {
            OkGo.getInstance()
                    .debug("OkGo")
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
//                    .setCacheMode(CacheMode.NO_CACHE)
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                    .setCookieStore(new PersistentCookieStore());          //cookie持久化存储，如果cookie不过期，则一直有效
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shop.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        // 获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        // 获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

}
