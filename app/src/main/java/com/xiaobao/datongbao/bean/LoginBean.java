package com.xiaobao.datongbao.bean;

/**
 * Created by Administrator on 2016/8/10.
 */
public class LoginBean {
    /**
     * status : 0
     * msg : 登陆成功
     * data : {"token":{"UserID":1157856,"Key":"0bda7ce8beeb4e29b80c79ca138bb79a","Signature":"2ca6187d50da7ef1c0f09f54d6ee3a3"},"inviteTel":"15011189266"}
     */

    private int status;
    private String msg;
    /**
     * token : {"UserID":1157856,"Key":"0bda7ce8beeb4e29b80c79ca138bb79a","Signature":"2ca6187d50da7ef1c0f09f54d6ee3a3"}
     * inviteTel : 15011189266
     */

    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * UserID : 1157856
         * Key : 0bda7ce8beeb4e29b80c79ca138bb79a
         * Signature : 2ca6187d50da7ef1c0f09f54d6ee3a3
         */

        private TokenBean token;
        private String inviteTel;

        public TokenBean getToken() {
            return token;
        }

        public void setToken(TokenBean token) {
            this.token = token;
        }

        public String getInviteTel() {
            return inviteTel;
        }

        public void setInviteTel(String inviteTel) {
            this.inviteTel = inviteTel;
        }

        public static class TokenBean {
            private int UserID;
            private String Key;
            private String Signature;

            public int getUserID() {
                return UserID;
            }

            public void setUserID(int UserID) {
                this.UserID = UserID;
            }

            public String getKey() {
                return Key;
            }

            public void setKey(String Key) {
                this.Key = Key;
            }

            public String getSignature() {
                return Signature;
            }

            public void setSignature(String Signature) {
                this.Signature = Signature;
            }
        }
    }


}
