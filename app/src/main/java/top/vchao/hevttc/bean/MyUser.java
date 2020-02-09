package top.vchao.hevttc.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * @ 创建时间: 2017/9/18 on 18:47.
 * @ 描述：用户类
 * @ 作者: vchao
 */

public class MyUser extends BmobUser implements Serializable {
    private String stuno;
    private String sex;
    private String yuan;
    private String clazz;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getYuan() {
        return yuan;
    }

    public void setYuan(String yuan) {
        this.yuan = yuan;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getStuno() {
        return stuno;
    }

    public void setStuno(String stuno) {
        this.stuno = stuno;
    }
}
