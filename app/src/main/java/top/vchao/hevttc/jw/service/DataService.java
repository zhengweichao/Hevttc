package top.vchao.hevttc.jw.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import top.vchao.hevttc.jw.bean.Course;
import top.vchao.hevttc.jw.bean.CourseTable;
import top.vchao.hevttc.jw.bean.ScoreTable;
import top.vchao.hevttc.jw.bean.StuSimpleInfo;
import top.vchao.hevttc.jw.bean.User;
import top.vchao.hevttc.constant.Constant;
import top.vchao.hevttc.jw.dao.OkHttpDAO;
import top.vchao.hevttc.jw.tool.HtmlTools;

public class DataService extends Service implements OkHttpDAO {

    /**
     * 记录正方教务系统页面表单的__VIEWSTATE的值
     */
    private String mViewState;

    /**
     * 已登录用户的信息
     */
    private User mUser;

    /**
     * 登录错误信息
     */
    private String mErrorMessege;

    /**
     * 查询课程表信息的URL
     */
    private String mCourseURL;

    /**
     * 查询个人信息的URL
     */
    private String mPersonalInfoURL;

    /**
     * 查询成绩表的URL
     */
    private String mScorceURL;

    /**
     * 教务网地址
     */
    private String mBaseURL;

    /**
     * Http客户端
     */
    private OkHttpClient mOkHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            String u = url.host();
            String urlStr = url.url().toString();
            if (urlStr.equals(mBaseURL)) {
                cookieStore.put(u, cookies);
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }).build();

    /**
     * Binder实例
     */
    private DataBinder myBinder = new DataBinder();

    /**
     * 自定义binder类
     */
    public class DataBinder extends Binder {

        public void login(User user) {
            DataService.this.login(user);
        }

        public void getCheckImg() {
            DataService.this.getCheckImg();
        }

        public void qureyCourseTable(String xnd, String xqd) {
            DataService.this.getCourseTable(xnd, xqd);
        }

        public void qureyXnds() {
            DataService.this.getXndAndXqd();
        }

        public void qureyScore() {
            DataService.this.getScore();
        }

        public void qureyStuInfo() {
            DataService.this.getPersonalInfo();
        }

        public void setURL(String url) {
            setJwURL(url);
        }


    }

    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        this.mViewState = "";
        this.mBaseURL = intent.getStringExtra(Constant.JW_URL);//读取教务网地址
        if (mBaseURL == null) {
            mBaseURL = Constant.BASE_URL;
        }
        init();
        return myBinder;
    }

    @Override
    public void init() {
        //初始化
        Request request = new Request.Builder().url(mBaseURL).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                connectionError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mViewState = HtmlTools.findViewState(response.body().string());//获取隐藏域
                getCheckImg();
            }
        });
    }

    @Override
    public void sendGetRequest(String url, String ref) {

    }

    @Override
    public void sendPostRequest(String url, String ref) {

    }

    @Override
    public void getCheckImg() {
        Request request = new Request.Builder().url(mBaseURL + Constant.CHECK_IMAGE_URL).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                connectionError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                byte[] data = body.bytes();
                //取得验证码数据并发送广播，将在登录界面接收广播
                Intent it = new Intent(BroadcastAction.CHECK_IMG);
                it.putExtra(Constant.CHECK, data);
                sendBroadcast(it);
            }
        });
    }

    @Override
    public void login(User user) {

        //表单信息
        FormBody body = new FormBody.Builder()
                .add("__VIEWSTATE", mViewState)
                .add("txtUserName", user.getName())
                .add("TextBox2", user.getPasswd())
                .add("txtSecretCode", user.getCheck())
                .add("RadioButtonList1", Constant.RADIO_BUTTON_LIST)
                .add("Button1", "")
                .add("lbLanguage", "")
                .add("hidPdrs", "")
                .add("hidsc", "")
                .build();

        Request request = new Request.Builder()
                .url(mBaseURL + Constant.LOGIN_URL)
                .post(body)
                .build();
        mUser = user;//记录登录的用户
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                connectionError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String html = body.string();
                String errorMessege = "";
                //检测是否有登录错误的信息，有则记录信息，否则登录成功
                String p = "<script language='javascript' defer>alert\\('([\\s\\S]+?)'\\);document\\.getElementById\\('([\\s\\S]+?)'\\)\\.focus\\(\\);</script>";
                Pattern pattern = Pattern.compile(p);
                Matcher m = pattern.matcher(html);
                Intent it = new Intent();
                if (m.find()) {
                    //登录失败
                    errorMessege = m.group(1);
                    it.setAction(BroadcastAction.LOGIN_FAIL);
                    it.putExtra(Constant.LOGIN_FAIL, errorMessege);
                } else {
                    saveQueryURL(html);//保存查询链接
                    it.setAction(BroadcastAction.LOGIN_SUCCESS);
                    it.putExtra(Constant.LOGIN_SUCCESS, "登录成功");
                }
                sendBroadcast(it);

            }
        });

    }

    @Override
    public void getXndAndXqd() {

        String urlStr = mBaseURL + mCourseURL;//查询课表的URL
        String referer = mBaseURL + Constant.STUDENT_URL + mUser.getName();//引用地址
        Request.Builder builder = new Request.Builder().url(urlStr).addHeader("Referer", referer);
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                connectionError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String html = response.body().string();
                mViewState = HtmlTools.findViewState(html);//记录__VIEWSTATE
                StuSimpleInfo stuInfo = HtmlTools.getCourseStuInfo(html);
                String[] xnds = HtmlTools.getXnd(html);    //在响应内容中获取学年度选项列表
                String xnd = HtmlTools.getCurrXnd(html);
                String xqd = HtmlTools.getCurrXqd(html);
//                Log.i("获取当前学年","学年："+xnd+" , 学期："+xqd);
//                Log.i("获取当前学年",html.substring(html.length()/2));

                //通过广播将课表发送到主界面
                Intent it = new Intent(BroadcastAction.COURSE_XND);
                Bundle bl = new Bundle();
                bl.putSerializable(Constant.COURSE_XND, xnds);
                bl.putString("currXnd", xnd);
                bl.putString("currXqd", xqd);
                it.putExtras(bl);
                sendBroadcast(it);
            }
        });

    }

    @Override
    public void getCourseTable(final String xnd, final String xqd) {
        String urlStr = mBaseURL + mCourseURL;//查询课表的URL
        String referer = mBaseURL + Constant.STUDENT_URL + mUser.getName();//引用地址
        Request.Builder builder = new Request.Builder().url(urlStr).addHeader("Referer", referer);
        //根据传入参数获取对应学年学期的课表，参数为空则获取默认课表
        if (xnd != null && xqd != null) {
            FormBody body = new FormBody.Builder()
                    .add("__EVENTTARGET", "xqd")
                    .add("__EVENTARGUMENT", "")
                    .add("__VIEWSTATE", mViewState)
                    .add("xnd", xnd)
                    .add("xqd", xqd)
                    .build();
            builder.post(body);
        }
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                connectionError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String html = response.body().string();
                CourseTable courseTable = new CourseTable();
                mViewState = HtmlTools.findViewState(html);//记录__VIEWSTATE
                StuSimpleInfo stuInfo = HtmlTools.getCourseStuInfo(html);
                ArrayList<Course> courses = HtmlTools.getCourseList(html);    //在响应内容中获取课表

                String _xnd = xnd;
                String _xqd = xqd;
//
//                //如果传进来的学年度和学期参数为空，则使用默认选项
//                Log.i(courses.size()+"课表："+_xnd+" , "+_xqd,"\n"+html.substring(html.length()/2));
                if (_xnd == null || _xnd.equals("")) {
                    _xnd = HtmlTools.getCurrXnd(html);
                }
                if (_xqd == null || _xqd.equals("")) {
                    _xqd = HtmlTools.getCurrXqd(html);
                }

                //保存获取到的所有信息
                courseTable.setCurrXnd(_xnd);
                courseTable.setCurrXqd(_xqd);
                courseTable.setCourses(courses);
                courseTable.setSimpleInfo(stuInfo);
//                Log.i("保存课表：",""+_xnd+" , "+_xqd);

                //通过广播将课表发送到主界面
                Intent it = new Intent(BroadcastAction.COURSE_TABLE);
                Bundle bl = new Bundle();
                bl.putSerializable(Constant.COURSE_TABLE, courseTable);
                it.putExtras(bl);
                sendBroadcast(it);
            }
        });

    }

    @Override
    public void getPersonalInfo() {
    }

    @Override
    public void getScore() {

        String urlStr = mBaseURL + mScorceURL;//查询课表的URL
        String referer = mBaseURL + Constant.STUDENT_URL + mUser.getName();//引用地址
        Request request = new Request.Builder().url(urlStr).addHeader("Referer", referer).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                connectionError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //该次返回的页面是查询成绩的页面，但是默认没有成绩数据，需要重新递交请求
                String html = response.body().string();
                //保存隐藏的页面状态
                mViewState = HtmlTools.findViewState(html);
                score();//请求成绩数据
            }
        });

    }

    @Override
    public void setJwURL(String url) {
        this.mBaseURL = url;
        //设置新的教务网地址后要初始化
        init();
    }

    /**
     * 请求成绩数据
     */
    private void score() {

        String urlStr = mBaseURL + mScorceURL;//查询课表的URL
        String ref = urlStr.substring(0, urlStr.indexOf("&xm=") + 4);
        try {
            int index = urlStr.indexOf("&gnmkdm=");
            ref = ref + URLEncoder.encode(urlStr.substring(ref.length(), index), "ISO-8859-1") + urlStr.substring(index);
        } catch (Exception e) {
            return;
        }
        //表单信息
        FormBody body = new FormBody.Builder()
                .add("__VIEWSTATE", mViewState)
                .add("ddlXN", "")
                .add("ddlXQ", "")
                .add("Button1", "按学期查询")
                .build();
        Request request = new Request.Builder()
                .url(urlStr)
                .addHeader("Referer", ref)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                connectionError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                mViewState = HtmlTools.findViewState(html);

                //获取成绩数据
                ScoreTable scoreTable = HtmlTools.getScoreTable(html);

                Intent it = new Intent(BroadcastAction.SCORE);
                Bundle bl = new Bundle();
                bl.putSerializable(Constant.SCORE_TABLE, scoreTable);
                it.putExtras(bl);
                sendBroadcast(it);
            }
        });

    }

    /**
     * 在学生首页中查找并保存查询各种信息的URL
     *
     * @param html HTML文档
     */
    private void saveQueryURL(String html) {
        // TODO Auto-generated method stub

        String pattern = "<a href=\"(\\w+)\\.aspx\\?xh=(\\d+)&xm=(.+?)&gnmkdm=N(\\d+)\" target='zhuti' onclick=\"GetMc\\('(.+?)'\\);\">(.+?)</a>";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(html);

        while (m.find()) {
            String res = m.group();
            String url = res.substring(res.indexOf("href=\"") + 6);
            url = url.substring(0, url.indexOf("\""));

            if (res.contains("学生个人课表")) {
                mCourseURL = url;
                continue;
            }
            if (res.contains("成绩查询")) {
                mScorceURL = url;
                continue;
            }
            if (res.contains("个人信息")) {
                mPersonalInfoURL = url;
            }
        }

    }

    /**
     * 请求失败或错误时，发送错误信息广播
     */
    private void connectionError() {
        Log.i("连接失败！", "连接失败！");
        Intent it = new Intent();
        String errorMessege = "连接失败！";
        it.setAction(BroadcastAction.LOGIN_FAIL);
        it.putExtra(Constant.LOGIN_FAIL, errorMessege);
        sendBroadcast(it);
    }

}





































