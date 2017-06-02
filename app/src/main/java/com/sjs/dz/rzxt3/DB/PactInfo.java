package com.sjs.dz.rzxt3.DB;

/**
 * Created by win on 2017/5/23.
 */

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**1
 * onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 */
@Table(name = "pact_info",onCreated = "")
public class PactInfo {
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "pact_no",isId = true,autoGen = false,property = "NOT NULL")
    private String pact_no;
    //工作流id
    @Column(name = "task_id")
    private String task_id;
    //申请单编号
    @Column(name = "apply_id")
    private String apply_id;
    //合同状态
    @Column(name = "pact_status")
    private String pact_status;
    //初期资料是否齐全
    @Column(name = "pact_is_early_file")
    private String pact_is_early_file;

    //检查机构代码
    @Column(name = "pact_check_org_no")
    private String pact_check_org_no;
    //检查机构名称
    @Column(name = "pact_check_org_name")
    private String pact_check_org_name;
    //检查类型
    @Column(name = "pact_check_type")
    private String pact_check_type;
    //检查选项
    @Column(name = "pact_check_option")
    private String pact_check_option;
    //合同检查员账号
    @Column(name = "pact_iner_acc")
    private String pact_iner_acc;

    //合同开始日期
    @Column(name = "pact_start_date")
    private String pact_start_date;
    //合同截止日期
    @Column(name = "pact_end_date")
    private String pact_end_date;
    //合同完成日期
    @Column(name = "pact_finish_date")
    private String pact_finish_date;
    //合同审核人账号
    @Column(name = "pact_audit_date")
    private String pact_audit_date;
    //公司编号(组织机构代码)
    @Column(name = "pact_com_no")
    private String pact_com_no;

    //公司名称
    @Column(name = "pact_com_name")
    private String pact_com_name;
    //公司地址
    @Column(name = "pact_com_addr")
    private String pact_com_addr;
    //公司电话
    @Column(name = "pact_com_tel")
    private String pact_com_tel;
    //公司邮编
    @Column(name = "pact_com_post_code")
    private String pact_com_post_code;
    //公司邮箱
    @Column(name = "pact_com_email")
    private String pact_com_email;

    //公司传真
    @Column(name = "pact_com_fax")
    private String pact_com_fax;
    //公司网址
    @Column(name = "pact_com_web_url")
    private String pact_com_web_url;
    //公司联系人姓名
    @Column(name = "pact_com_con_name")
    private String pact_com_con_name;
    //公司联系人电话
    @Column(name = "pact_com_con_tel")
    private String pact_com_con_tel;
    //公司联系人职位
    @Column(name = "pact_com_con_ide")
    private String pact_com_con_ide;


    public PactInfo(String pact_no,String task_id,String pact_is_early_file,String apply_id,String pact_status,
                     String pact_check_org_no,String pact_check_org_name,String pact_check_type,String pact_check_option,String pact_iner_acc,
                     String pact_start_date,String pact_end_date,String pact_finish_date,String pact_audit_date,String pact_com_no,
                     String pact_com_name,String pact_com_addr,String pact_com_tel,String pact_com_post_code,String pact_com_email,
                     String pact_com_fax,String pact_com_web_url,String pact_com_con_name,String pact_com_con_tel,String pact_com_con_ide) {
        this.pact_no = pact_no;
        this.task_id = task_id;
        this.pact_is_early_file = pact_is_early_file;
        this.apply_id = apply_id;
        this.pact_status = pact_status;

        this.pact_check_org_no = pact_check_org_no;
        this.pact_check_org_name = pact_check_org_name;
        this.pact_check_type = pact_check_type;
        this.pact_check_option = pact_check_option;
        this.pact_iner_acc = pact_iner_acc;

        this.pact_start_date = pact_start_date;
        this.pact_end_date = pact_end_date;
        this.pact_finish_date = pact_finish_date;
        this.pact_audit_date = pact_audit_date;
        this.pact_com_no = pact_com_no;

        this.pact_com_name = pact_com_name;
        this.pact_com_addr = pact_com_addr;
        this.pact_com_tel = pact_com_tel;
        this.pact_com_post_code = pact_com_post_code;
        this.pact_com_email = pact_com_email;

        this.pact_com_fax = pact_com_fax;
        this.pact_com_web_url = pact_com_web_url;
        this.pact_com_con_name = pact_com_con_name;
        this.pact_com_con_tel = pact_com_con_tel;
        this.pact_com_con_ide = pact_com_con_ide;


    }
    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public PactInfo() {
    }

    public String getPact_no() {
        return pact_no;
    }

    public void setPact_no(String pact_no) {
        this.pact_no = pact_no;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getPact_status() {
        return pact_status;
    }

    public void setPact_status(String pact_status) {
        this.pact_status = pact_status;
    }

    public String getPact_is_early_file() {
        return pact_is_early_file;
    }

    public void setPact_is_early_file(String pact_is_early_file) {
        this.pact_is_early_file = pact_is_early_file;
    }

    public String getPact_check_org_no() {
        return pact_check_org_no;
    }

    public void setPact_check_org_no(String pact_check_org_no) {
        this.pact_check_org_no = pact_check_org_no;
    }

    public String getPact_check_org_name() {
        return pact_check_org_name;
    }

    public void setPact_check_org_name(String pact_check_org_name) {
        this.pact_check_org_name = pact_check_org_name;
    }

    public String getPact_check_type() {
        return pact_check_type;
    }

    public void setPact_check_type(String pact_check_type) {
        this.pact_check_type = pact_check_type;
    }

    public String getPact_check_option() {
        return pact_check_option;
    }

    public void setPact_check_option(String pact_check_option) {
        this.pact_check_option = pact_check_option;
    }

    public String getPact_iner_acc() {
        return pact_iner_acc;
    }

    public void setPact_iner_acc(String pact_iner_acc) {
        this.pact_iner_acc = pact_iner_acc;
    }

    public String getPact_start_date() {
        return pact_start_date;
    }

    public void setPact_start_date(String pact_start_date) {
        this.pact_start_date = pact_start_date;
    }

    public String getPact_end_date() {
        return pact_end_date;
    }

    public void setPact_end_date(String pact_end_date) {
        this.pact_end_date = pact_end_date;
    }

    public String getPact_finish_acc() {
        return pact_finish_date;
    }

    public void setPact_finish_acc(String pact_finish_acc) {
        this.pact_finish_date = pact_finish_acc;
    }

    public String getPact_audit_acc() {
        return pact_audit_date;
    }

    public void setPact_audit_acc(String pact_audit_acc) {
        this.pact_audit_date = pact_audit_acc;
    }

    public String getPact_com_no() {
        return pact_com_no;
    }

    public void setPact_com_no(String pact_com_no) {
        this.pact_com_no = pact_com_no;
    }

    public String getPact_com_name() {
        return pact_com_name;
    }

    public void setPact_com_name(String pact_com_name) {
        this.pact_com_name = pact_com_name;
    }

    public String getPact_com_addr() {
        return pact_com_addr;
    }

    public void setPact_com_addr(String pact_com_addr) {
        this.pact_com_addr = pact_com_addr;
    }

    public String getPact_com_tel() {
        return pact_com_tel;
    }

    public void setPact_com_tel(String pact_com_tel) {
        this.pact_com_tel = pact_com_tel;
    }

    public String getPact_com_post_code() {
        return pact_com_post_code;
    }

    public void setPact_com_post_code(String pact_com_post_code) {
        this.pact_com_post_code = pact_com_post_code;
    }

    public String getPact_com_email() {
        return pact_com_email;
    }

    public void setPact_com_email(String pact_com_email) {
        this.pact_com_email = pact_com_email;
    }

    public String getPact_com_fax() {
        return pact_com_fax;
    }

    public void setPact_com_fax(String pact_com_fax) {
        this.pact_com_fax = pact_com_fax;
    }

    public String getPact_com_web_url() {
        return pact_com_web_url;
    }

    public void setPact_com_web_url(String pact_com_web_url) {
        this.pact_com_web_url = pact_com_web_url;
    }

    public String getPact_com_con_name() {
        return pact_com_con_name;
    }

    public void setPact_com_con_name(String pact_com_con_name) {
        this.pact_com_con_name = pact_com_con_name;
    }

    public String getPact_com_con_tel() {
        return pact_com_con_tel;
    }

    public void setPact_com_con_tel(String pact_com_con_tel) {
        this.pact_com_con_tel = pact_com_con_tel;
    }

    public String getPact_com_con_ide() {
        return pact_com_con_ide;
    }

    public void setPact_com_con_ide(String pact_com_con_ide) {
        this.pact_com_con_ide = pact_com_con_ide;
    }

    @Override
    public String toString() {
        return "PactInfo{" +
                "pact_no='" + pact_no + '\'' +
                ", task_id='" + task_id + '\'' +
                ", apply_id='" + apply_id + '\'' +
                ", pact_status='" + pact_status + '\'' +
                ", pact_is_early_file='" + pact_is_early_file + '\'' +
                ", pact_check_org_no='" + pact_check_org_no + '\'' +
                ", pact_check_org_name='" + pact_check_org_name + '\'' +
                ", pact_check_type='" + pact_check_type + '\'' +
                ", pact_check_option='" + pact_check_option + '\'' +
                ", pact_iner_acc='" + pact_iner_acc + '\'' +
                ", pact_start_date='" + pact_start_date + '\'' +
                ", pact_end_date='" + pact_end_date + '\'' +
                ", pact_finish_acc='" + pact_finish_date + '\'' +
                ", pact_audit_acc='" + pact_audit_date + '\'' +
                ", pact_com_no='" + pact_com_no + '\'' +
                ", pact_com_name='" + pact_com_name + '\'' +
                ", pact_com_addr='" + pact_com_addr + '\'' +
                ", pact_com_tel='" + pact_com_tel + '\'' +
                ", pact_com_post_code='" + pact_com_post_code + '\'' +
                ", pact_com_email='" + pact_com_email + '\'' +
                ", pact_com_fax='" + pact_com_fax + '\'' +
                ", pact_com_web_url='" + pact_com_web_url + '\'' +
                ", pact_com_con_name='" + pact_com_con_name + '\'' +
                ", pact_com_con_tel='" + pact_com_con_tel + '\'' +
                ", pact_com_con_ide='" + pact_com_con_ide + '\'' +
                '}';
    }
}