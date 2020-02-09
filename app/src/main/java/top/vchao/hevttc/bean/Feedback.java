package top.vchao.hevttc.bean;

import cn.bmob.v3.BmobObject;

/**
 * @ 创建时间: 2017/10/2 on 8:40.
 * @ 描述：意见反馈
 * @ 作者: vchao
 */

public class Feedback extends BmobObject {
    private String theme;
    private MyUser user;
    private String content;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
