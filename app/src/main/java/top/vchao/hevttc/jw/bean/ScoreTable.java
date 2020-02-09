package top.vchao.hevttc.jw.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 成绩表
 * Created by EsauLu on 2016-10-03.
 */

public class ScoreTable implements Serializable {

    /**
     * 学生信息
     */
    private StuSimpleInfo stuInfo;

    /**
     * 成绩表
     */
    private ArrayList<CourseScore> scoreList;

    public ScoreTable() {

    }

    public StuSimpleInfo getStuInfo() {
        return stuInfo;
    }

    public void setStuInfo(StuSimpleInfo stuInfo) {
        this.stuInfo = stuInfo;
    }

    public ArrayList<CourseScore> getScoreList() {
        return scoreList;
    }

    public void setScoreList(ArrayList<CourseScore> scoreList) {
        this.scoreList = scoreList;
    }

}
