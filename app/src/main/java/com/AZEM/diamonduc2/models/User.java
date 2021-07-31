package com.AZEM.diamonduc2.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userID;
    private Integer diamonds;
    private String email;
    private String imgUrl;
    private String username;
    private int inviteDiamond;

    @Ignore
    public User() {
    }

    public User(String userID, Integer diamonds, String imgUrl, String username) {
        this.userID = userID;
        this.diamonds = diamonds;
        this.imgUrl = imgUrl;
        this.username = username;
    }

    public Integer getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(Integer diamonds) {
        this.diamonds = diamonds;
    }


    public int getInviteDiamond() {
        return inviteDiamond;
    }

    public void setInviteDiamond(int inviteDiamond) {
        this.inviteDiamond = inviteDiamond;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
