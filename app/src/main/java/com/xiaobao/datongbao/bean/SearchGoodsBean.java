package com.xiaobao.datongbao.bean;

import java.util.List;

/**
 * Created by Pan on 2016/11/2.
 */
public class SearchGoodsBean {


    /**
     * status : 0
     * data : {"list":[{"GoodsID":631202,"Name":"苹果iPhone 7 256G","Photo":"http://192.168.1.94:8089/Images/System/Goods/1/D53E2CE5-C054-46D2-9B39-2E532C108123.jpg","CategoryID":1,"Price":6988,"BrandId":23,"InnerId":804},{"GoodsID":631200,"Name":"苹果iPhone 7 128G","Photo":"http://192.168.1.94:8089/Images/System/Goods/1/0574ABF4-5C29-4BFC-A060-753C40ECBB9F.jpg","CategoryID":1,"Price":6188,"BrandId":23,"InnerId":804},{"GoodsID":631160,"Name":"苹果iPhone 7 plus128G","Photo":"http://192.168.1.94:8089/Images/System/Goods/1/BA3BD465-FF5D-4A5B-900E-3010C0A65719.jpg","CategoryID":1,"Price":7188,"BrandId":23,"InnerId":835},{"GoodsID":616067,"Name":"苹果iPhone7 Plus 32G","Photo":"http://192.168.1.94:8089/Images/System/Goods/1/eea823b0-0b3c-422b-8f28-d525390c14b6.jpg","CategoryID":1,"Price":6200,"BrandId":23,"InnerId":835},{"GoodsID":575351,"Name":"苹果iPhone7 32G","Photo":"http://192.168.1.94:8089/Images/System/Goods/1/0b67b125-ac73-419f-9db4-4bee4b81227d.jpg","CategoryID":1,"Price":5388,"BrandId":23,"InnerId":804}]}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * GoodsID : 631202
         * Name : 苹果iPhone 7 256G
         * Photo : http://192.168.1.94:8089/Images/System/Goods/1/D53E2CE5-C054-46D2-9B39-2E532C108123.jpg
         * CategoryID : 1
         * Price : 6988
         * BrandId : 23
         * InnerId : 804
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int GoodsID;
            private String Name;
            private String Photo;
            private int CategoryID;
            private double Price;
            private int BrandId;
            private int InnerId;

            public int getGoodsID() {
                return GoodsID;
            }

            public void setGoodsID(int GoodsID) {
                this.GoodsID = GoodsID;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getPhoto() {
                return Photo;
            }

            public void setPhoto(String Photo) {
                this.Photo = Photo;
            }

            public int getCategoryID() {
                return CategoryID;
            }

            public void setCategoryID(int CategoryID) {
                this.CategoryID = CategoryID;
            }

            public double getPrice() {
                return Price;
            }

            public void setPrice(int Price) {
                this.Price = Price;
            }

            public int getBrandId() {
                return BrandId;
            }

            public void setBrandId(int BrandId) {
                this.BrandId = BrandId;
            }

            public int getInnerId() {
                return InnerId;
            }

            public void setInnerId(int InnerId) {
                this.InnerId = InnerId;
            }
        }
    }
}
