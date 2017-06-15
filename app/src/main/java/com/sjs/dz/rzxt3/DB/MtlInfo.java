package com.sjs.dz.rzxt3.DB;

/**
 * Created by win on 2017/6/7.
 */

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 */
@Table(name = "mtl_info",onCreated = "")
public class MtlInfo {
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    //文件编号
//    @Column(name = "mtl_id")
//    private int mtl_id=0;

    @Column(name = "mtl_no",isId = true,autoGen = false,property = "NOT NULL")
    private int mtl_no=0;
    //项目id
    @Column(name = "item_no")
    private String item_no;
    //文件类型
    @Column(name = "mtl_type")
    private String mtl_type;
    //文件名称
    @Column(name = "mtl_name")
    private String mtl_name;
    //文件大小
    @Column(name = "mtl_size")
    private String mtl_size;


    //文件创建时间
    @Column(name = "mtl_time")
    private String mtl_time;
    //文件格式
    @Column(name = "mtl_format")
    private String mtl_format;
    //文件下载地址
    @Column(name = "mtl_down_path")
    private String mtl_down_path;


    public MtlInfo(int mtl_no,String item_no,String mtl_type,String mtl_name,String mtl_size, String mtl_time,
                    String mtl_format,String mtl_down_path) {
        this.mtl_no = mtl_no;
        this.item_no = item_no;
        this.mtl_type = mtl_type;
        this.mtl_name = mtl_name;
        this.mtl_size = mtl_size;

        this.mtl_time = mtl_time;
        this.mtl_format = mtl_format;
        this.mtl_down_path = mtl_down_path;
    }
    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public MtlInfo() {
    }

//    public int getMtl_id() {
//        return mtl_id;
//    }
//
//    public void setMtl_id(int mtl_id) {
//        this.mtl_id = mtl_id;
//    }

    public int getMtl_no() {
        return mtl_no;
    }

    public void setMtl_no(int mtl_no) {
        this.mtl_no = mtl_no;
    }
    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getMtl_type() {
        return mtl_type;
    }

    public void setMtl_type(String mtl_type) {
        this.mtl_type = mtl_type;
    }

    public String getMtl_name() {
        return mtl_name;
    }

    public void setMtl_name(String mtl_name) {
        this.mtl_name = mtl_name;
    }

    public String getMtl_size() {
        return mtl_size;
    }

    public void setMtl_size(String mtl_size) {
        this.mtl_size = mtl_size;
    }

    public String getMtl_time() {
        return mtl_time;
    }

    public void setMtl_time(String mtl_time) {
        this.mtl_time = mtl_time;
    }

    public String getMtl_format() {
        return mtl_format;
    }

    public void setMtl_format(String mtl_format) {
        this.mtl_format = mtl_format;
    }

    public String getMtl_down_path() {
        return mtl_down_path;
    }

    public void setMtl_down_path(String mtl_down_path) {
        this.mtl_down_path = mtl_down_path;
    }

    @Override
    public String toString() {
        return "MtlInfo{" +
//                "mtl_id=" + mtl_id +
                " mtl_no='" + mtl_no + '\'' +
                ", item_no='" + item_no + '\'' +
                ", mtl_type='" + mtl_type + '\'' +
                ", mtl_name='" + mtl_name + '\'' +
                ", mtl_size='" + mtl_size + '\'' +
                ", mtl_time='" + mtl_time + '\'' +
                ", mtl_format='" + mtl_format + '\'' +
                ", mtl_down_path='" + mtl_down_path + '\'' +
                '}';
    }

}
