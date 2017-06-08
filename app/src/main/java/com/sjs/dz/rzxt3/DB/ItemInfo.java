package com.sjs.dz.rzxt3.DB;

/**
 * Created by win on 2017/5/23.
 */

import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**2
 * onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 */
@Table(name = "item_info",onCreated = "")
public class ItemInfo {
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "item_no",isId = true,autoGen = false,property = "NOT NULL")
    private String item_no;
    //项目状态
    @Column(name = "item_status")
    private String item_status;
    //合同编号（外键）
    @Column(name = "pact_no")
    private String pact_no;
    //产品编号(外键)
//    @Column(name = "pro_no")
//    private String pro_no;
//    //生产类型
    @Column(name = "pro_type")
    @SerializedName("prdt_type")
    private String pro_type;

    //认证范围
    @Column(name = "rz_scope")
    private String rz_scope;
    //认证类型
    @Column(name = "rz_type")
    private String rz_type;
    //检查类型
    @Column(name = "check_type")
    private String check_type;
    //申请单编号
    @Column(name = "apply_id")
    private String apply_id;

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public ItemInfo(String item_no, String item_status, String pact_no, String pro_type, String rz_scope,
                    String rz_type, String check_type, String apply_id) {
        this.item_no = item_no;
        this.item_status = item_status;
        this.pact_no = pact_no;
//        this.pro_no = pro_no;
        this.pro_type = pro_type;

        this.rz_scope = rz_scope;
        this.rz_type = rz_type;
        this.check_type = check_type;
        this.apply_id=apply_id;


    }
    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public ItemInfo() {
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getPact_no() {
        return pact_no;
    }

    public void setPact_no(String pact_no) {
        this.pact_no = pact_no;
    }

//    public String getPro_no() {
//        return pro_no;
//    }
//
//    public void setPro_no(String pro_no) {
//        this.pro_no = pro_no;
//    }

    public String getPro_type() {
        return pro_type;
    }

    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    public String getRz_scope() {
        return rz_scope;
    }

    public void setRz_scope(String rz_scope) {
        this.rz_scope = rz_scope;
    }

    public String getRz_type() {
        return rz_type;
    }

    public void setRz_type(String rz_type) {
        this.rz_type = rz_type;
    }

    public String getCheck_type() {
        return check_type;
    }

    public void setCheck_type(String check_type) {
        this.check_type = check_type;
    }

    @Override
    public String toString() {
        return "ItemInfo{" +
                "item_no='" + item_no + '\'' +
                ", item_status='" + item_status + '\'' +
                ", pact_no='" + pact_no + '\'' +
                ", pro_type='" + pro_type + '\'' +
                ", rz_scope='" + rz_scope + '\'' +
                ", rz_type='" + rz_type + '\'' +
                ", check_type='" + check_type + '\'' +
                ", apply_id='" + apply_id + '\'' +
                '}';
    }
}