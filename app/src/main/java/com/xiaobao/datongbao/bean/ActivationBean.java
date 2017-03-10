package com.xiaobao.datongbao.bean;

import java.util.List;

/**
 * Created by song on 2016/12/7.
 * 作者:沉默
 * QQ:823925783
 */
public class ActivationBean {
    private long CustomerId;
    /// 服务卡卡密
    private String CardNumber;
    //图片url
    private String SerialsNumber;
    private String UGoodsId;
    private List<CoOrderInfo> OrderList;

    public ActivationBean(long customerId, String cardNumber, String serialsNumber, List<CoOrderInfo> orderList,String uGoodsId) {
        CustomerId = customerId;
        CardNumber = cardNumber;
        SerialsNumber = serialsNumber;
        OrderList = orderList;
        UGoodsId=uGoodsId;
    }


    public ActivationBean(long customerId, String cardNumber, String serialsNumber, List<CoOrderInfo> orderList) {
        CustomerId = customerId;
        CardNumber = cardNumber;
        SerialsNumber = serialsNumber;
        OrderList = orderList;
    }

    /**
     * ProductId : 3
     */


    public long getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(long CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String CardNumber) {
        this.CardNumber = CardNumber;
    }

    public String getSerialsNumber() {
        return SerialsNumber;
    }

    public void setSerialsNumber(String SerialsNumber) {
        this.SerialsNumber = SerialsNumber;
    }

    public String getUGoodsId() {
        return UGoodsId;
    }

    public void setUGoodsId(String UGoodsId) {
        this.UGoodsId = UGoodsId;
    }

    public List<CoOrderInfo> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<CoOrderInfo> OrderList) {
        this.OrderList = OrderList;
    }

    public static class CoOrderInfo {
        /// 商品Id
        private int GoodsId;
        /// 商品IMEI号
        private String GoodsSerialNo;
        //小保是13
        private int Source;
        private List<CoOrderRecognizee> recognizeelist;

        public CoOrderInfo(int goodsId, String goodsSerialNo, int source, List<CoOrderRecognizee> recognizeelist) {
            GoodsId = goodsId;
            GoodsSerialNo = goodsSerialNo;
            this.recognizeelist = recognizeelist;
            Source=source;
        }

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int goodsId) {
            GoodsId = goodsId;
        }

        public String getGoodsSerialNo() {
            return GoodsSerialNo;
        }

        public void setGoodsSerialNo(String goodsSerialNo) {
            GoodsSerialNo = goodsSerialNo;
        }

        public int getSource() {
            return Source;
        }
        public void setSource(int source) {
            Source = source;
        }


        public List<CoOrderRecognizee> getRecognizeelist() {
            return recognizeelist;
        }

        public void setRecognizeelist(List<CoOrderRecognizee> recognizeelist) {
            this.recognizeelist = recognizeelist;
        }
        public static class CoOrderRecognizee {
            /// 被保人姓名
            private String RecognizeeName ;
            /// 电话
            private String Telephone;
            /// 身份证
            private String IDCard ;

            public CoOrderRecognizee(String recognizeeName, String telephone, String IDCard) {
                RecognizeeName = recognizeeName;
                Telephone = telephone;
                this.IDCard = IDCard;
            }

            public String getRecognizeeName() {
                return RecognizeeName;
            }

            public void setRecognizeeName(String recognizeeName) {
                RecognizeeName = recognizeeName;
            }

            public String getTelephone() {
                return Telephone;
            }

            public void setTelephone(String telephone) {
                Telephone = telephone;
            }

            public String getIDCard() {
                return IDCard;
            }

            public void setIDCard(String IDCard) {
                this.IDCard = IDCard;
            }
        }

    }
}
