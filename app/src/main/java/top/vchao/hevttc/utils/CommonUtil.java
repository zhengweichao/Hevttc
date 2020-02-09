package top.vchao.hevttc.utils;

import android.view.View;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import top.vchao.hevttc.bean.Teacher;

/**
 * @ 创建时间: 2017/9/18 on 22:13.
 * @ 描述：
 * @ 作者: vchao
 */

public class CommonUtil {

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 对数据进行排序
     *
     * @param list 要进行排序的数据源
     */
    public static void sortData(List<Teacher> list) {
        if (list == null || list.size() == 0) return;
        for (int i = 0; i < list.size(); i++) {
            Teacher bean = list.get(i);
            String tag = Pinyin.toPinyin(bean.getName().substring(0, 1).charAt(0)).substring(0, 1);
            if (tag.matches("[A-Z]")) {
                bean.setIndexTag(tag);
            } else {
                bean.setIndexTag("#");
            }
        }
        Collections.sort(list, new Comparator<Teacher>() {
            @Override
            public int compare(Teacher o1, Teacher o2) {
                if ("#".equals(o1.getIndexTag())) {
                    return 1;
                } else if ("#".equals(o2.getIndexTag())) {
                    return -1;
                } else {
                    return o1.getIndexTag().compareTo(o2.getIndexTag());
                }
            }
        });
    }

    /**
     * @param beans 数据源
     * @return tags 返回一个包含所有Tag字母在内的字符串
     */
    public static String getTags(List<Teacher> beans) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < beans.size(); i++) {
            if (!builder.toString().contains(beans.get(i).getIndexTag())) {
                builder.append(beans.get(i).getIndexTag());
            }
        }
        return builder.toString();
    }

    private static String[] nameArray = new String[]{
            "Windows", "Mac", "Linux"
    };
    private static String[] contentArray = new String[]{
            "在动作类影片中，只要发生混乱，那么绝对就有木仓战。现在的技术越来越发达，电影或电视中的特效也做的越来越逼真，演员们被木仓打中的效果也很形象，我们经常能看到被木仓打中的伤口血淋林的暴露在大屏幕中，从演员的表演中我们能看到木仓击是很痛的，那么你们有想过被木仓打中到底会有多痛？什么感觉吗？网站有网友为我们分享被子弹打中的感觉\n" +
                    "1、“老实说，比我想象中的感觉要轻很多。本来我以为很痛，可是被打中后就像是被棒球击中的感觉一样，刚开始的几秒钟没什么知觉，过会才感到痛\n" +
                    "2、“被子弹打到的感觉就像是一直有人拿针扎你一样，刺痛刺痛的。”\n" +
                    "3、“我当初大腿被木仓击中，子弹直接从我的大腿中传过去，连带着我的肌腱也被击中，那种感觉我觉得用疼痛两个字已经不足以形容了\n" +
                    "4、“在我十七岁的时候，脚被木仓击中，当时我以为是被蜜蜂蛰了，因为仿佛听到了蜜蜂的声音，没过几秒钟，脚上就传来灼热感，这才知道原来是被木仓击中了。\n" +
                    "5、“我只是听到的木仓声，却没有意识到自己中木仓了。直到血流出来才意识到。所以，对我来讲，被子弹击中没什么感觉。"
            ,
            "GNOME or KDE desktop\n" +
                    " processor with support for AMD Virtualization™ (AMD-V™)"


    };

    /**
     * 获取文本内容根据下标
     *
     * @param position
     * @return
     */

    public static String getContent(int position) {
        return contentArray[position % contentArray.length];
    }

    /**
     * 获取名称根据下标
     *
     * @param position
     * @return
     */
    public static String getName(int position) {
        return nameArray[position % contentArray.length];
    }

}
