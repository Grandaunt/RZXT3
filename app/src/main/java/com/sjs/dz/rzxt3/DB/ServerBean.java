package com.sjs.dz.rzxt3.DB;

import java.util.List;

/**
 * Created by win on 2017/5/31.
 */
public class ServerBean {
    //0:成功，1：失败
    private int error;
    private String msg;
    private UserInfo user;
    private List<PactInfo> htList;
    private List<ProInfo> cpList;
    private List<ItemInfo> xmList;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
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
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", user=" + user +
                ", htList=" + htList +
                ", cpList=" + cpList +
                ", xmList=" + xmList +
                '}';
    }
}
