package com.bigdata.entity;


/**
 * 蔬菜价格相关实体
 */
public class Vegetables {

    /**
     * 用于在hbase索引定义由 区域_蔬菜拼音_蔬菜更新日期
     */
    private String rowKey;
    /**
     * 蔬菜名称
     */
    private String vName;
    /**
     * 蔬菜拼音
     */
    private String vNameEn;
    /**
     * 蔬菜平均价格
     */
    private String vPrice;
    /**
     * 蔬菜最高价格
     */
    private String hPrice;
    /**
     * 蔬菜最低价格
     */
    private String lPrice;
    /**
     * 蔬菜批发市场名称
     */
    private String vMarket;
    /**
     * 所属区域
     */
    private String area;
    /**
     * 数据来源网站
     */
    private String source;
    /**
     * 网站数据更新时间
     */
    private String updateTime;
    /**
     * 数据入库时间
     */
    private String insertTime;

    public String getvNameEn() {
        return vNameEn;
    }

    public void setvNameEn(String vNameEn) {
        this.vNameEn = vNameEn;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvPrice() {
        return vPrice;
    }

    public void setvPrice(String vPrice) {
        this.vPrice = vPrice;
    }

    public String gethPrice() {
        return hPrice;
    }

    public void sethPrice(String hPrice) {
        this.hPrice = hPrice;
    }

    public String getlPrice() {
        return lPrice;
    }

    public void setlPrice(String lPrice) {
        this.lPrice = lPrice;
    }

    public String getvMarket() {
        return vMarket;
    }

    public void setvMarket(String vMarket) {
        this.vMarket = vMarket;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }
}
