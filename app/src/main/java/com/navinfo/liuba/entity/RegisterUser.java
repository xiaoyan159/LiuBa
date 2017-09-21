package com.navinfo.liuba.entity;

import java.io.Serializable;

/**
 * Created by zhangdezhi1702 on 2017/9/19.
 */

public class RegisterUser implements Serializable {
    /**
     * userId:23
     * userRealName : 张三
     * userNickName : 小三
     * userPassword : 123456
     * userType : 1
     * userPhone : 110
     * userAddress : 珠穆朗玛峰
     * petNickName : 咪咪
     * petSex : 1
     * petAge : 22
     * petBreed : 藏獒
     * petHabit : 吃饭不擦嘴
     */
    private int userId;
    private String userRealName;
    private String userNickName;
    private String userPassword;
    private int userType;
    private String userPhone;
    private String userAddress;
    private String petNickName;
    private int petSex;
    private int petAge;
    private String petBreed;
    private String petHabit;
    private String location;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getPetNickName() {
        return petNickName;
    }

    public void setPetNickName(String petNickName) {
        this.petNickName = petNickName;
    }

    public int getPetSex() {
        return petSex;
    }

    public void setPetSex(int petSex) {
        this.petSex = petSex;
    }

    public int getPetAge() {
        return petAge;
    }

    public void setPetAge(int petAge) {
        this.petAge = petAge;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getPetHabit() {
        return petHabit;
    }

    public void setPetHabit(String petHabit) {
        this.petHabit = petHabit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
