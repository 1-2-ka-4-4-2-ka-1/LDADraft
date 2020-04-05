package com.bankbros.ldaadmin;

import java.io.Serializable;

public class RegisteredUsersModel implements Serializable {

    private String UserName;
    private String UserEmail;
    private String UserId;
    private String DateRegistered;
    private String NoticeLimit;
    private String Donatios;
    private int notificationcount;
    private boolean UserEnabled;

    public RegisteredUsersModel() {
    }

    public RegisteredUsersModel(String userName, String userEmail, String userId, String dateRegistered, String noticeLimit, String donatios, int notificationcount, boolean userEnabled) {
        UserName = userName;
        UserEmail = userEmail;
        UserId = userId;
        DateRegistered = dateRegistered;
        NoticeLimit = noticeLimit;
        Donatios = donatios;
        this.notificationcount = notificationcount;
        UserEnabled = userEnabled;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDateRegistered() {
        return DateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        DateRegistered = dateRegistered;
    }

    public String getNoticeLimit() {
        return NoticeLimit;
    }

    public void setNoticeLimit(String noticeLimit) {
        NoticeLimit = noticeLimit;
    }

    public String getDonatios() {
        return Donatios;
    }

    public void setDonatios(String donatios) {
        Donatios = donatios;
    }

    public int getNotificationcount() {
        return notificationcount;
    }

    public void setNotificationcount(int notificationcount) {
        this.notificationcount = notificationcount;
    }

    public boolean isUserEnabled() {
        return UserEnabled;
    }

    public void setUserEnabled(boolean userEnabled) {
        UserEnabled = userEnabled;
    }
}

