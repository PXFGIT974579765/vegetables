package com.bigdata.entity;

/**
 * 天气实体对象
 */
public class Weather {

    /**
     * 用于在hbase索引定义由 区域_蔬菜拼音_蔬菜更新日期
     */
    private String rowKey;

    /**
     * 日期
     */
    private String ymd;

    /**
     * 风力
     */
    private String fengli;
    /**
     * 风向
     */
    private String fengxiang;

    /**
     * 空气质量等级
     */
    private String aqiLevel;

    /**
     * 空气指数
     */
    private String aqi;

    /**
     * 最低温度
     */
    private String bWendu;
    /**
     * 最高温度
     */
    private String yWendu;
    /**
     * 天气状况
     */
    private String tianqi;
    /**
     * 空气质量评价 优，良，轻度污染等
     */
    private String aqiInfo;
    /**
     * 城市
     */
    private String area;

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getAqiLevel() {
        return aqiLevel;
    }

    public void setAqiLevel(String aqiLevel) {
        this.aqiLevel = aqiLevel;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getbWendu() {
        return bWendu;
    }

    public void setbWendu(String bWendu) {
        this.bWendu = bWendu;
    }

    public String getyWendu() {
        return yWendu;
    }

    public void setyWendu(String yWendu) {
        this.yWendu = yWendu;
    }

    public String getTianqi() {
        return tianqi;
    }

    public void setTianqi(String tianqi) {
        this.tianqi = tianqi;
    }

    public String getAqiInfo() {
        return aqiInfo;
    }

    public void setAqiInfo(String aqiInfo) {
        this.aqiInfo = aqiInfo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
