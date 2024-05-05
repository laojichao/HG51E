package com.aotem.hg51e;

/****
*** author：lao
*** package：com.aotem.hg51e
*** project：HG51E
*** name：User
*** date：2024/5/5  1:58
*** filename：User
*** desc：
***/
    
public class User {

    /**
     * retcode : 0
     * data : {"web_user_name":"user","web_tele_account_name":"CMCCAdmin","userNameModify_enable":"0","aucDefaultAccountPassword":"useradmin","teleAccount_password":"6CghB7m#","telecomNameModify":"0","userNameModify":"0"}
     */

    private String retcode;
    private DataBean data;

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * web_user_name : user
         * web_tele_account_name : CMCCAdmin
         * userNameModify_enable : 0
         * aucDefaultAccountPassword : useradmin
         * teleAccount_password : 6CghB7m#
         * telecomNameModify : 0
         * userNameModify : 0
         */

        private String web_user_name;
        private String web_tele_account_name;
        private String userNameModify_enable;
        private String aucDefaultAccountPassword;
        private String teleAccount_password;
        private String telecomNameModify;
        private String userNameModify;

        public String getWeb_user_name() {
            return web_user_name;
        }

        public void setWeb_user_name(String web_user_name) {
            this.web_user_name = web_user_name;
        }

        public String getWeb_tele_account_name() {
            return web_tele_account_name;
        }

        public void setWeb_tele_account_name(String web_tele_account_name) {
            this.web_tele_account_name = web_tele_account_name;
        }

        public String getUserNameModify_enable() {
            return userNameModify_enable;
        }

        public void setUserNameModify_enable(String userNameModify_enable) {
            this.userNameModify_enable = userNameModify_enable;
        }

        public String getAucDefaultAccountPassword() {
            return aucDefaultAccountPassword;
        }

        public void setAucDefaultAccountPassword(String aucDefaultAccountPassword) {
            this.aucDefaultAccountPassword = aucDefaultAccountPassword;
        }

        public String getTeleAccount_password() {
            return teleAccount_password;
        }

        public void setTeleAccount_password(String teleAccount_password) {
            this.teleAccount_password = teleAccount_password;
        }

        public String getTelecomNameModify() {
            return telecomNameModify;
        }

        public void setTelecomNameModify(String telecomNameModify) {
            this.telecomNameModify = telecomNameModify;
        }

        public String getUserNameModify() {
            return userNameModify;
        }

        public void setUserNameModify(String userNameModify) {
            this.userNameModify = userNameModify;
        }
    }
}
