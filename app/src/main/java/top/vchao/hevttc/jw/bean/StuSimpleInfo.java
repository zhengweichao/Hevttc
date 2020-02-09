package top.vchao.hevttc.jw.bean;

import java.io.Serializable;

/**
 * 学生概要信息
 *
 * @author EsauLu
 */
public class StuSimpleInfo implements Serializable {

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学号
     */
    private String id;

    /**
     * 系别
     */
    private String department;

    /**
     * 专业
     */
    private String major;

    /**
     * 班级
     */
    private String classNum;

    /**
     * 构造函数
     */
    public StuSimpleInfo() {
        // TODO Auto-generated constructor stub
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

}
