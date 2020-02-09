package top.vchao.hevttc.jw.factor;


import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.vchao.hevttc.jw.bean.Course;
import top.vchao.hevttc.jw.bean.CourseScore;
import top.vchao.hevttc.jw.bean.StuSimpleInfo;
import top.vchao.hevttc.jw.bean.User;

/**
 * Bean工厂
 *
 * @author EsauLu
 */
public class BeanFactor {

    /**
     * 匹配课程开始和结束周的正则表达式
     */
    private static String pattern = "(.*?)(\\d+)-(\\d+)(.*)";

    /**
     * 匹配课程开始和结束周的Pattern实例
     */
    private static Pattern p = Pattern.compile(pattern);

    /**
     * 创建课程对象
     *
     * @param info 课程信息
     * @param x    第x节课
     * @param y    星期y
     * @return 返回课程对象
     */
    public static Course createCourse(String[] info, int x, int y) {

        Course c = new Course();
        c.setName(info[0]);

        String[] t = (" " + info[1]).split("\\{");
        c.setClassTime(t[0].trim());//上课时间

        String weekNum = t[1].substring(0, t[1].length() - 1);
        Matcher m = p.matcher(weekNum);
        String res = "";
        if (m.find()) {

            try {
                res = m.group(2);
                int begin = Integer.parseInt(res);
                c.setStartWeek(begin);
                res = m.group(3);
                int end = Integer.parseInt(res);
                c.setEndWeek(end);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            res = m.group(4);

            if (res.contains("单周")) {
                c.setWeekState(Course.SINGLE_WEEK);
            } else if (res.contains("双周")) {
                c.setWeekState(Course.DOUBLE_WEEK);
            } else {
                c.setWeekState(Course.ALL_WEEK);
            }
        }

        c.setWeekNum(weekNum);//周数
        c.setTeacher(info[2]);
        c.setClassRoom(info[3]);
        c.setSchoolTime(x, y);

        return c;
    }

    /**
     * 创建用户对象
     *
     * @param name   用户名（学号）
     * @param passwd 密码
     * @param check  验证码
     * @return 用户对象
     */
    public static User createUser(String name, String passwd, String check) {
        User user = new User();
        user.setName(name);
        user.setPasswd(passwd);
        user.setCheck(check);
        return user;
    }

    /**
     * 创建学生信息
     *
     * @param info 信息数组
     * @return 学生信息对象
     */
    public static StuSimpleInfo createStuSimpleInfo(Map<String, String> info) {
        StuSimpleInfo stu = new StuSimpleInfo();

        stu.setId(info.get("id"));
        stu.setName(info.get("name"));
        stu.setDepartment(info.get("department"));
        stu.setMajor(info.get("major"));
        stu.setClassNum("classNum");

        return stu;
    }

    /**
     * 获取课程成绩
     *
     * @param info 课程成绩信息
     * @return 课程成绩实例
     */
    public static CourseScore createCourseScore(Map<String, String> info) {
        CourseScore cs = new CourseScore();

        cs.setXnd(info.get("xnd"));
        cs.setXqd(info.get("xqd"));
        cs.setName(info.get("name"));
        cs.setCredit(info.get("credit"));
        cs.setScore(info.get("score"));
        cs.setProperty(info.get("property"));

        return cs;
    }

}


















































