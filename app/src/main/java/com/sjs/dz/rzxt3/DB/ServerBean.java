package com.sjs.dz.rzxt3.DB;

import java.util.List;

/**
 * Created by win on 2017/5/31.
 */
public class ServerBean {
    //0:成功，1：失败
    private String err;
    private String msg;
    private UserInfo user;
    private List<PactInfo> htList;
    private List<ProInfo> cpList;
    private List<ItemInfo> xmList;

    public List<MtlInfo> getMtList() {
        return mtList;
    }

    public void setMtList(List<MtlInfo> mtList) {
        this.mtList = mtList;
    }

    private List<MtlInfo> mtList;

    public String getErr() {
        return err;
    }

    public void setErr(String error) {
        this.err = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public List<PactInfo> getHtList() {
        return htList;
    }

    public void setHtList(List<PactInfo> htList) {
        this.htList = htList;
    }

    public List<ProInfo> getCpList() {
        return cpList;
    }

    public void setCpList(List<ProInfo> cpList) {
        this.cpList = cpList;
    }

    public List<ItemInfo> getXmList() {
        return xmList;
    }

    public void setXmList(List<ItemInfo> xmList) {
        this.xmList = xmList;
    }

    @Override
    public String toString() {
        return "ServerBean{" +
                "err=" + err +
                ", msg='" + msg + '\'' +
                ", user=" + user +
                ", htList=" + htList +
                ", cpList=" + cpList +
                ", xmList=" + xmList +
                ", mtList=" + mtList +
                '}';
    }

    public ServerBean(String err, String msg,UserInfo user,List<PactInfo> htList,List<ProInfo> cpList,List<ItemInfo> xmList,List<MtlInfo> mtList) {
        this.err = err;
        this.msg = msg;
        this.user = user;
        this.htList = htList;
        this.cpList = cpList;
        this.xmList = xmList;
        this.mtList = mtList;

    }
    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public ServerBean() {
    }
}
