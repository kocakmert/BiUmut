package com.example.blood;
public class Class_Admin_MessageBox_Users_Photo {
    private String userName;
    private String user_image;
    public Class_Admin_MessageBox_Users_Photo(String userName, String user_image) {
        this.userName = userName;
        this.user_image = user_image;
    }
    public String getUserName() {
        return userName;
    }
    public String getUserImage() {
        return user_image;
    }
}
