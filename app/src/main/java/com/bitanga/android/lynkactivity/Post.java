package com.bitanga.android.lynkactivity;

import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

public class Post {

    private UUID mId;
    private String mContent;
    private String mUsername;

    private int mComments;
    private int mLikes;
    private int mNumOfTimesFlagged;

    private ImageView mPostPhoto;
//    private User user;
    private Date mTimestamp;

    private boolean mHasPhoto;


    public Post() {
        this(UUID.randomUUID());
        //maybe add this here?
        mTimestamp = new Date();
    }

    public Post(UUID id) {
        mId = id;
        mTimestamp = new Date();
    }

    public UUID getId() {return mId;}

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    /*returns number of comments and likes*/
    public int getNumComments() {
        return mComments;
    }

    public void setComments(int comments) {
        mComments = comments;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int likes) {
        mLikes = likes;
    }

    public int getNumOfTimesFlagged() {
        return mNumOfTimesFlagged;
    }

    public void setNumOfTimesFlagged(int numOfTimesFlagged) {
        mNumOfTimesFlagged = numOfTimesFlagged;
    }

    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Date timestamp) {
        mTimestamp = timestamp;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public boolean hasPhoto() {
        return mHasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        mHasPhoto = hasPhoto;
    }
}
