package com.czl.lib_base.data.entity;

public class SplashTokenBean {

    private String app_name;
    private String app_build;
    private String os_ver;
    private String sys_uid;
    private String app_ver;
    private String sys_ver;
    private String imei;
    private String model;
    private String brand;
    private int cur_uid;
    private String client_ip;
    private String meelinked_token;
    private String meelinked_pass;
    private int create_time;

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_build() {
        return app_build;
    }

    public void setApp_build(String app_build) {
        this.app_build = app_build;
    }

    public String getOs_ver() {
        return os_ver;
    }

    public void setOs_ver(String os_ver) {
        this.os_ver = os_ver;
    }

    public String getSys_uid() {
        return sys_uid;
    }

    public void setSys_uid(String sys_uid) {
        this.sys_uid = sys_uid;
    }

    public String getApp_ver() {
        return app_ver;
    }

    public void setApp_ver(String app_ver) {
        this.app_ver = app_ver;
    }

    public String getSys_ver() {
        return sys_ver;
    }

    public void setSys_ver(String sys_ver) {
        this.sys_ver = sys_ver;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCur_uid() {
        return cur_uid;
    }

    public void setCur_uid(int cur_uid) {
        this.cur_uid = cur_uid;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getMeelinked_token() {
        return meelinked_token;
    }

    public void setMeelinked_token(String meelinked_token) {
        this.meelinked_token = meelinked_token;
    }

    public String getMeelinked_pass() {
        return meelinked_pass;
    }

    public void setMeelinked_pass(String meelinked_pass) {
        this.meelinked_pass = meelinked_pass;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "SplashTokenBean{" +
                "app_name='" + app_name + '\'' +
                ", app_build='" + app_build + '\'' +
                ", os_ver='" + os_ver + '\'' +
                ", sys_uid='" + sys_uid + '\'' +
                ", app_ver='" + app_ver + '\'' +
                ", sys_ver='" + sys_ver + '\'' +
                ", imei='" + imei + '\'' +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", cur_uid=" + cur_uid +
                ", client_ip='" + client_ip + '\'' +
                ", meelinked_token='" + meelinked_token + '\'' +
                ", meelinked_pass='" + meelinked_pass + '\'' +
                ", create_time=" + create_time +
                '}';
    }
}
