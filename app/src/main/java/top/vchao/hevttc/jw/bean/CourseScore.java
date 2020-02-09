package top.vchao.hevttc.jw.bean;

import java.io.Serializable;

/**
 * 课程成绩
 * Created by EsauLu on 2016-10-03.
 */

public class CourseScore implements Serializable {

    /**
     * 学年度
     */
    private String xnd;

    /**
     * 学期
     */
    private String xqd;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程性质（必修或选修）
     */
    private String property;

    /**
     * 成绩
     */
    private String score;

    /**
     * 学分
     */
    private String credit;

    /**
     * 构造函数
     */
    public CourseScore() {

    }

    public String getXnd() {
        return xnd;
    }

    public void setXnd(String xnd) {
        this.xnd = xnd;
    }

    public String getXqd() {
        return xqd;
    }

    public void setXqd(String xqd) {
        this.xqd = xqd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
