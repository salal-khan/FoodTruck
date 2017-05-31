package com.codiansoft.foodtruck.Models;

/**
 * Created by Ahmad on 11/8/2016.
 */

public class DeviceDetail {
    String deviceId;
    String deviceSerial;
    String softwareVersion;
    String basebandVersion;
    String kernalVersion;
    String buildNumber;
    String IMEI;
    String Image;

    public DeviceDetail() {
    }

    public DeviceDetail(String deviceId, String deviceSerial, String softwareVersion, String basebandVersion, String kernalVersion, String buildNumber, String image, String IMEI) {
        this.deviceId = deviceId;
        this.deviceSerial = deviceSerial;
        this.softwareVersion = softwareVersion;
        this.basebandVersion = basebandVersion;
        this.kernalVersion = kernalVersion;
        this.buildNumber = buildNumber;
        Image = image;
        this.IMEI = IMEI;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getKernalVersion() {
        return kernalVersion;
    }

    public void setKernalVersion(String kernalVersion) {
        this.kernalVersion = kernalVersion;
    }

    public String getBasebandVersion() {
        return basebandVersion;
    }

    public void setBasebandVersion(String basebandVersion) {
        this.basebandVersion = basebandVersion;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }
}
