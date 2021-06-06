package com.example.moviebuzz.Model;


import com.google.firebase.database.ServerValue;

public class CommentModel {
    String content,userId,userName,userImage;
    Object timeStamp;

    public CommentModel() {
    }

    public CommentModel(String content, String userId, String userName, String userImage) {
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;

        timeStamp = ServerValue.TIMESTAMP;
    }

    public CommentModel(String content, String userId, String userName, String userImage, Object timeStamp) {
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
