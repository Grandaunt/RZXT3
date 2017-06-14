package com.sjs.dz.rzxt3.DB;

import java.util.List;

/**
 * Created by win on 2017/5/31.
 */
public class ResultBean {
    //0:成功，1：失败
    private String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;


    public ResultBean(String error, String msg) {
        this.err = error;
        this.msg = msg;


    }
    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public ResultBean() {
    }
}
