package yxy.pra0914.dto;

import java.io.Serializable;

/**
 * Created by YXY on 2017/10/18.
 */

public class MainOrderDto implements Serializable{

    private int uid;
    private String goods;
    private String sendaddress;
    private String recieveInfo;
    private double cost;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getSendaddress() {
        return sendaddress;
    }

    public void setSendaddress(String sendaddress) {
        this.sendaddress = sendaddress;
    }

    public String getRecieveInfo() {
        return recieveInfo;
    }

    public void setRecieveInfo(String recieveInfo) {
        this.recieveInfo = recieveInfo;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
