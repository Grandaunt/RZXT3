package com.sjs.dz.rzxt3.DB;

/**
 * Created by win on 2017/5/23.
 */

import com.google.gson.annotations.SerializedName;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**3
 * onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 */
@Table(name = "pro_info",onCreated = "")
public class ProInfo {
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "pro_no",isId = true,autoGen = false,property = "NOT NULL")
    @SerializedName("prdt_type")
    private String pro_no;
    //项目编号
    @Column(name = "item_no")
    private String item_no;
    //产品名称
    @Column(name = "pro_name")
    @SerializedName("prdt_name")
    private String pro_name;
    //产品面积
    @Column(name = "pro_area")
    @SerializedName("prdt_area")
    private String pro_area;
    //产品产量
    @Column(name = "pro_output")
    @SerializedName("prdt_output")
    private String pro_output;

    //产品数量
    @Column(name = "pro_num")
    @SerializedName("prdt_num")
    private String pro_num;
    //产品产值
    @Column(name = "pro_ralue")
    @SerializedName("prdt_ralue")
    private String pro_ralue;
    //基地名称
    @Column(name = "base_name")
    private String base_name;
    //基地地址
    @Column(name = "base_address")
    private String base_address;



    public ProInfo(String pro_no, String item_no, String pro_name, String pro_area, String pro_output,
                   String pro_num, String pro_ralue, String base_name,String base_address) {
        this.pro_no = pro_no;
        this.item_no = item_no;
        this.pro_name = pro_name;
        this.pro_area = pro_area;
        this.pro_output = pro_output;

        this.pro_num = pro_num;
        this.pro_ralue = pro_ralue;
        this.base_name = base_name;
        this.base_address = base_address;


    }
    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public ProInfo() {
    }

    public String getPro_no() {
        return pro_no;
    }

    public void setPro_no(String pro_no) {
        this.pro_no = pro_no;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_area() {
        return pro_area;
    }

    public void setPro_area(String pro_area) {
        this.pro_area = pro_area;
    }

    public String getPro_output() {
        return pro_output;
    }

    public void setPro_output(String pro_output) {
        this.pro_output = pro_output;
    }

    public String getPro_num() {
        return pro_num;
    }

    public void setPro_num(String pro_num) {
        this.pro_num = pro_num;
    }

    public String getPro_ralue() {
        return pro_ralue;
    }

    public void setPro_ralue(String pro_ralue) {
        this.pro_ralue = pro_ralue;
    }

    public String getBase_name() {
        return base_name;
    }

    public void setBase_name(String base_name) {
        this.base_name = base_name;
    }

    public String getBase_address() {
        return base_address;
    }

    public void setBase_address(String base_address) {
        this.base_address = base_address;
    }

    @Override
    public String toString() {
        return "ProInfo{" +
                "pro_no='" + pro_no + '\'' +
                ", item_no='" + item_no + '\'' +
                ", pro_name='" + pro_name + '\'' +
                ", pro_area='" + pro_area + '\'' +
                ", pro_output='" + pro_output + '\'' +
                ", pro_num='" + pro_num + '\'' +
                ", pro_ralue='" + pro_ralue + '\'' +
                ", base_name='" + base_name + '\'' +
                ", base_address='" + base_address + '\'' +
                '}';
    }
}