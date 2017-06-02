package com.sjs.dz.rzxt3.DB;

/**
 * Created by win on 2017/5/31.
 */
public class UserInfo {

        //请求结果
        private String AUTH_RESULT;
        //用户口令
        private  String AUTH_TOKEN;
        //用户登陆账号
        private String USER_ACCOUNT;
        //用户姓名
        private String USER_NAME;
        //用户身份
        private String USER_IDE;


        //用户电话
        private String USER_TEL;
        //用户所属部门
        private String USER_DEPT_NAME;
        //用户所属部门机构码
        private String USER_DEPT_ORG_CODE;

    public UserInfo(String AUTH_RESULT, String AUTH_TOKEN, String USER_ACCOUNT, String USER_NAME, String USER_IDE,
                   String pro_num, String pro_ralue, String base_name,String base_address) {
        this.AUTH_RESULT = AUTH_RESULT;
        this.AUTH_TOKEN = AUTH_TOKEN;
        this.USER_ACCOUNT = USER_ACCOUNT;
        this.USER_NAME = USER_NAME;
        this.USER_IDE = USER_IDE;

        this.USER_TEL = USER_TEL;
        this.USER_DEPT_NAME = USER_DEPT_NAME;
        this.USER_DEPT_ORG_CODE = USER_DEPT_ORG_CODE;


    }
    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public UserInfo() {
    }
        public String getAUTH_RESULT() {
            return AUTH_RESULT;
        }

        public void setAUTH_RESULT(String AUTH_RESULT) {
            this.AUTH_RESULT = AUTH_RESULT;
        }

        public String getAUTH_TOKEN() {
            return AUTH_TOKEN;
        }

        public void setAUTH_TOKEN(String AUTH_TOKEN) {
            this.AUTH_TOKEN = AUTH_TOKEN;
        }

        public String getUSER_ACCOUNT() {
            return USER_ACCOUNT;
        }

        public void setUSER_ACCOUNT(String USER_ACCOUNT) {
            this.USER_ACCOUNT = USER_ACCOUNT;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public String getUSER_IDE() {
            return USER_IDE;
        }

        public void setUSER_IDE(String USER_IDE) {
            this.USER_IDE = USER_IDE;
        }

        public String getUSER_TEL() {
            return USER_TEL;
        }

        public void setUSER_TEL(String USER_TEL) {
            this.USER_TEL = USER_TEL;
        }

        public String getUSER_DEPT_NAME() {
            return USER_DEPT_NAME;
        }

        public void setUSER_DEPT_NAME(String USER_DEPT_NAME) {
            this.USER_DEPT_NAME = USER_DEPT_NAME;
        }

        public String getUSER_DEPT_ORG_CODE() {
            return USER_DEPT_ORG_CODE;
        }

        public void setUSER_DEPT_ORG_CODE(String USER_DEPT_ORG_CODE) {
            this.USER_DEPT_ORG_CODE = USER_DEPT_ORG_CODE;
        }

        @Override
        public String toString() {
            return "User{" +
                    "USER_ACCOUNT='" + USER_ACCOUNT + '\'' +
                    ", USER_NAME='" + USER_NAME + '\'' +
                    ", USER_IDE='" + USER_IDE + '\'' +
                    ", USER_TEL='" + USER_TEL + '\'' +
                    '}';
        }

}
