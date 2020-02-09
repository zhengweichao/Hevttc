package top.vchao.hevttc.jw.dao;


import top.vchao.hevttc.jw.bean.User;

/**
 * 网络访问接口
 * Created by EsauLu on 2016-10-01.
 */

public interface OkHttpDAO {

    /**
     * 初始化，主要用于收集cookie和viewState
     */
    public void init();

    /**
     * 根据指定url发送给请求
     *
     * @param url 请求url
     * @param ref 引用
     * @return 响应页面的HTML文档
     */
    public void sendGetRequest(String url, String ref);

    /**
     * 根据指定url和参数值发送post请求
     *
     * @param url 请求url
     * @param ref 引用
     * @return 响应页面的HTML文档
     */
    public void sendPostRequest(String url, String ref);

    /**
     * 获取验证码
     *
     * @return 验证码图片
     */
    public void getCheckImg();

    /**
     * 登录
     *
     * @param user 用户信息
     */
    public void login(User user);

    public void getXndAndXqd();

    /**
     * 根据学年度和学期获取课表
     *
     * @param xnd 学年度
     * @param xqd 学期
     * @return 课表
     */
    public void getCourseTable(String xnd, String xqd);

    /**
     * 获取个人信息
     */
    public void getScore();

    /**
     * 获取个人信息
     */
    public void getPersonalInfo();

    /**
     * 设置教务网地址
     */
    public void setJwURL(String url);


}
