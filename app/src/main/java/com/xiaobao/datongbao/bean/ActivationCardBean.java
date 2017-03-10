package com.xiaobao.datongbao.bean;

import java.io.Serializable;

/**
 * Created by song on 2017/1/17.
 * 作者:沉默
 * QQ:823925783
 */
public class ActivationCardBean implements Serializable {
    /// 服务卡卡密
    private String CardNumber;
    //流水号
    private String SerialsNumber;
    /// 商品Id
    private String GoodsId;
    //imei
    private String imei;
    /// 被保人姓名
    private String RecognizeeName;
    /// 电话
    private String Telephone;
    private String uGoodsId;
    /// 身份证
    private String IDCard;

    private int Source;

    public ActivationCardBean() {
    }

    public ActivationCardBean(int source, String cardNumber, String serialsNumber, String goodsId, String imei, String recognizeeName, String telephone, String uGoodsId, String IDCard) {
        Source = source;
        CardNumber = cardNumber;
        SerialsNumber = serialsNumber;
        GoodsId = goodsId;
        this.imei = imei;
        RecognizeeName = recognizeeName;
        Telephone = telephone;
        this.uGoodsId = uGoodsId;
        this.IDCard = IDCard;
    }

    public void setSource(int source) {
        Source = source;
    }

    public int getSource() {
        return Source;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getSerialsNumber() {
        return SerialsNumber;
    }

    public void setSerialsNumber(String serialsNumber) {
        SerialsNumber = serialsNumber;
    }

    public String getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(String goodsId) {
        GoodsId = goodsId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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

    public String getuGoodsId() {
        return uGoodsId;
    }

    public void setuGoodsId(String uGoodsId) {
        this.uGoodsId = uGoodsId;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }
}
