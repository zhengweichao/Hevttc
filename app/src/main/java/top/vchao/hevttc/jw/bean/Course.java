package top.vchao.hevttc.jw.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * 课程类
 *
 * @author EsauLu
 */
public class Course implements Serializable, Parcelable {

    /**
     * 全部周数
     */
    public transient static final int ALL_WEEK = 0;

    /**
     * 单周
     */
    public transient static final int SINGLE_WEEK = 1;

    /**
     * 双周
     */
    public transient static final int DOUBLE_WEEK = 2;

    /**
     * 姓名
     */
    public String name;

    /**
     * 教室
     */
    public String classRoom;

    /**
     * 老师
     */
    public String teacher;

    /**
     * 上课时间
     */
    public String classTime;

    /**
     * 周数
     */
    public String weekNum;

    /**
     * 课程开始周
     */
    public int startWeek;


    /**
     * 课程结束周
     */
    public int endWeek;

    /**
     * 记录该课程是双周还是单周上课
     */
    public int weekState;

    /**
     * 课程在课表中的位置
     */
    public SchoolTime schoolTime;

    /**
     * 记录课程在课表中位置的类
     *
     * @author EsauLu
     */
    public class SchoolTime implements Serializable {
        /**
         * 第几节课
         */
        public int number;
        /**
         * 星期几
         */
        public int day;

        public SchoolTime() {
            // TODO Auto-generated constructor stub
        }

        public SchoolTime(int x, int y) {
            // TODO Auto-generated constructor stub
            this.number = x;
            this.day = y;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int x) {
            this.number = x;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int y) {
            this.day = y;
        }

    }

    /**
     * 构造函数
     */
    public Course() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 获取姓名
     *
     * @return 返回姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取教室
     *
     * @return 返回教室
     */
    public String getClassRoom() {
        return classRoom;
    }

    /**
     * 设置教室
     *
     * @param classRoom 返回教室
     */
    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    /**
     * 获取教师
     *
     * @return 返回教师
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * 设置教师
     *
     * @param teacher 教师
     */
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    /**
     * 获取上课时间
     *
     * @return 返回上课时间
     */
    public String getClassTime() {
        return classTime;
    }

    /**
     * 设置上课时间
     *
     * @param time 上课时间
     */
    public void setClassTime(String time) {
        this.classTime = time;

    }

    /**
     * 返回上课周数
     *
     * @return 上课周数
     */
    public String getWeekNum() {
        return weekNum;
    }

    /**
     * 设置上课周数
     *
     * @param weekNum 上课周数
     */
    public void setWeekNum(String weekNum) {
        this.weekNum = weekNum;
    }

    /**
     * 获取课程在课表的位置
     *
     * @return 课表的位置
     */
    public SchoolTime getSchoolTime() {
        return schoolTime;
    }

    /**
     * 设置课表位置
     *
     * @param schoolTime 课表位置
     */
    public void setSchoolTime(SchoolTime schoolTime) {
        this.schoolTime = schoolTime;
    }

    /**
     * 设置课表位置
     *
     * @param x 节数
     * @param y 星期几
     */
    public void setSchoolTime(int x, int y) {
        this.schoolTime = new SchoolTime(x, y);
    }

    /**
     * 获取第几节课上课
     *
     * @return 返回值表示第几节课上课
     */
    public int getNumber() {
        return schoolTime.getNumber();
    }

    /**
     * 获取星期几上课
     *
     * @return 返回星期几
     */
    public int getDay() {
        return schoolTime.getDay();
    }

    /**
     * 获取课程开始周
     *
     * @return 课程开始周
     */
    public int getStartWeek() {
        return startWeek;
    }

    /**
     * 设置课程开始周
     *
     * @param startWeek 课程开始周
     */
    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    /**
     * 获取课程结束周
     *
     * @return 课程结束周
     */
    public int getEndWeek() {
        return endWeek;
    }

    /**
     * 设置课程结束周
     *
     * @param endWeek 课程结束周
     */
    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    /**
     * 获取课程上课是全部周、双周还是单周
     *
     * @return 返回值为ALL_WEEK、SINGLE_WEEK或DOUBLE_WEEK
     */
    public int getWeekState() {
        return weekState;
    }

    /**
     * 设置课程的上课周
     *
     * @param weekState 可取值为ALL_WEEK、SINGLE_WEEK或DOUBLE_WEEK
     */
    public void setWeekState(int weekState) {
        this.weekState = weekState;
    }


    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", teacher='" + teacher + '\'' +
                ", classTime='" + classTime + '\'' +
                ", weekNum='" + weekNum + '\'' +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", weekState=" + weekState +
                ", schoolTime=" + schoolTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(classRoom);
        dest.writeString(teacher);
        dest.writeString(classTime);
        dest.writeString(weekNum);
        dest.writeInt(startWeek);
        dest.writeInt(endWeek);
        dest.writeInt(weekState);
        dest.writeValue(schoolTime);

    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {

        @Override
        public Course createFromParcel(Parcel source) {
            Log.i(">>>>>>>>>", "=========================读取==========================");
            Log.i("+++++", "=========================读取==========================");

            Course course = new Course();

            course.setName(source.readString());
            course.setClassRoom(source.readString());
            course.setTeacher(source.readString());
            course.setClassTime(source.readString());
            course.setWeekNum(source.readString());

            course.setStartWeek(source.readInt());
            course.setEndWeek(source.readInt());
            course.setWeekState(source.readInt());

            course.setSchoolTime((SchoolTime) source.readValue(Course.class.getClassLoader()));

            return course;

        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }

    };

}




















