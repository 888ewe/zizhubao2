package com.xiaobao.datongbao.bean;

/**
 * Created by Administrator on 2016/8/1.
 */
public class UserInfoBean {

    /**
     * status : 0
     * msg : 获取成功
     * data : {"info":{"ID":1157856,"NickName":"bao215644","HeadPhoto":"","TelNum":"18511215644","Sex":false}}
     */

    private int status;
    private String msg;
    /**
     * info : {"ID":1157856,"NickName":"bao215644","HeadPhoto":"","TelNum":"18511215644","Sex":false}
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
         * ID : 1157856
         * NickName : bao215644
         * HeadPhoto :
         * TelNum : 18511215644
         * Sex : false
         */

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            private int ID;
            private String NickName;
            private String HeadPhoto;
            private String TelNum;
            private boolean Sex;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getNickName() {
                return NickName;
            }

            public void setNickName(String NickName) {
                this.NickName = NickName;
            }

            public String getHeadPhoto() {
                return HeadPhoto;
            }

            public void setHeadPhoto(String HeadPhoto) {
                this.HeadPhoto = HeadPhoto;
            }

            public String getTelNum() {
                return TelNum;
            }

            public void setTelNum(String TelNum) {
                this.TelNum = TelNum;
            }

            public boolean isSex() {
                return Sex;
            }

            public void setSex(boolean Sex) {
                this.Sex = Sex;
            }
        }
    }
}
