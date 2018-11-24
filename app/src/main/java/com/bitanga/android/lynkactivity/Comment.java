package com.bitanga.android.lynkactivity;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class Comment {

    private String mUserId;
    private String mUserName;
    private String mContent;
    private Date mTimestamp;

    public Comment() {
    }

    public Comment(FirebaseUser user, String content) {
        this.mUserId = user.getUid();
        this.mUserName = user.getDisplayName();
        if (TextUtils.isEmpty(this.mUserName)) {
            this.mUserName = user.getEmail();
        }

        this.mContent = content;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.mTimestamp = timestamp;
    }
}
