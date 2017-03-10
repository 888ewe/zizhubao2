package com.xiaobao.datongbao.bean;

import java.io.Serializable;

/**
 * Created by Pan on 2016/11/16.
 */
public class ServiceInfoBean implements Serializable{
    //包含流水号，设备码，图片标识，手机品牌，手机型号加密后的字符串
    private String encryptedData;
    //本次验机结果的流水号
    private String serialsNumber;
    //验机设备的设备号
    private String imei;
    //平安系统对应的图片标识号
    private String pictureCode;


    //被投保人的姓名
    private String recognizeeName;
    //被投保人的手机号
    private String telephone;
    //被投保人的身份证号
    private String idCard;
    //手机品牌
    private String phoneBrand;
    //手机型号
    private String phoneModel;


    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getSerialsNumber() {
        return serialsNumber;
    }

    public void setSerialsNumber(String serialsNumber) {
        this.serialsNumber = serialsNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPictureCode() {
        return pictureCode;
    }

    public void setPictureCode(String pictureCode) {
        this.pictureCode = pictureCode;
    }

    public String getRecognizeeName() {
        return recognizeeName;
    }

    public void setRecognizeeName(String recognizeeName) {
        this.recognizeeName = recognizeeName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }
}
