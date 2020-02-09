package top.vchao.hevttc.bean;

/**
 * @ 创建时间: 2017/5/25 on 11:06.
 * @ 描述：消息bean类，聊天页面
 * @ 作者: vchao
 */

public class Message {
    public static final int SEND = 1;//发送类型
    public static final int RECEIVER = 2;//接收类型

    private String content;//内容
    private int flag;//类型标志
    private String time;//时间
    public static String color = "#ffffff";

    public Message(String content, int flag, String time) {
        setContent(content);
        setFlag(flag);
        setTime(time);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
