package com.bitanga.android.lynkactivity;

public class User {

    private String mEmail;
    private String mUsername;
    private String mPassword; //will be unhashed for now
    private int mNumFriends;

    public User() {}

    public User(String email, String username, String password, int numFriends) {
        this.mEmail = email;
        this.mUsername = username;
        this.mPassword = password;
        this.mNumFriends = numFriends;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPassword (String password){mPassword = password; }

    public String getPassword () {return mPassword;}

    public int getNumFriends() {
        return mNumFriends;
    }

    public void setNumFriends(int numFriends) {
        mNumFriends = numFriends;
    }

}
