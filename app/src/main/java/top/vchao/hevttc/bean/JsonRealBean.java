package top.vchao.hevttc.bean;

import java.util.List;

/**
 * @ 创建时间: 2017/10/2 on 15:34.
 * @ 描述：实名认证json Bean
 * @ 作者: vchao
 */
public class JsonRealBean {


    /**
     * code : 200
     * list : [{"stu_no":"0923160102","name":"崔鹏鹏","sex":"男","birth":"19980925","idcard":"131081199809251015","kaohao":"16131081150974","shenfen":"共青团员","minzu":"汉族","xiaoqu":"秦皇岛","yuanxi":"数学与信息科技学院","zhuanye":"计算机应用技术","banhao":"09231601","xuezhi":"3","cengci":"专科","address":"河北省廊坊市霸州市老堤乡牛岗村","tezheng":"河北"}]
     */

    private String code;
    private List<ListBean> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * stu_no : 0923160102
         * name : 崔鹏鹏
         * sex : 男
         * birth : 19980925
         * idcard : 131081199809251015
         * kaohao : 16131081150974
         * shenfen : 共青团员
         * minzu : 汉族
         * xiaoqu : 秦皇岛
         * yuanxi : 数学与信息科技学院
         * zhuanye : 计算机应用技术
         * banhao : 09231601
         * xuezhi : 3
         * cengci : 专科
         * address : 河北省廊坊市霸州市老堤乡牛岗村
         * tezheng : 河北
         */

        private String stu_no;
        private String name;
        private String sex;
        private String birth;
        private String idcard;
        private String kaohao;
        private String shenfen;
        private String minzu;
        private String xiaoqu;
        private String yuanxi;
        private String zhuanye;
        private String banhao;
        private String xuezhi;
        private String cengci;
        private String address;
        private String tezheng;

        public String getStu_no() {
            return stu_no;
        }

        public void setStu_no(String stu_no) {
            this.stu_no = stu_no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getKaohao() {
            return kaohao;
        }

        public void setKaohao(String kaohao) {
            this.kaohao = kaohao;
        }

        public String getShenfen() {
            return shenfen;
        }

        public void setShenfen(String shenfen) {
            this.shenfen = shenfen;
        }

        public String getMinzu() {
            return minzu;
        }

        public void setMinzu(String minzu) {
            this.minzu = minzu;
        }

        public String getXiaoqu() {
            return xiaoqu;
        }

        public void setXiaoqu(String xiaoqu) {
            this.xiaoqu = xiaoqu;
        }

        public String getYuanxi() {
            return yuanxi;
        }

        public void setYuanxi(String yuanxi) {
            this.yuanxi = yuanxi;
        }

        public String getZhuanye() {
            return zhuanye;
        }

        public void setZhuanye(String zhuanye) {
            this.zhuanye = zhuanye;
        }

        public String getBanhao() {
            return banhao;
        }

        public void setBanhao(String banhao) {
            this.banhao = banhao;
        }

        public String getXuezhi() {
            return xuezhi;
        }

        public void setXuezhi(String xuezhi) {
            this.xuezhi = xuezhi;
        }

        public String getCengci() {
            return cengci;
        }

        public void setCengci(String cengci) {
            this.cengci = cengci;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTezheng() {
            return tezheng;
        }

        public void setTezheng(String tezheng) {
            this.tezheng = tezheng;
        }
    }
}
