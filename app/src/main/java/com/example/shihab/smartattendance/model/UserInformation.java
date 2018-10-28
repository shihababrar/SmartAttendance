package com.example.shihab.smartattendance.model;

public class UserInformation {
    public String userId,userPassword,userEmailAddress;
    public UserInformation(String userId, String userPassword) {
        this.userId=userId;
        this.userPassword=userPassword;
    }
    public void changePassword(String userId, String userEmailAddress){

    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }
}
