package com.example.sidarta.myapplication.domain.model;

import android.net.Uri;

/**
 * Created by sidarta on 03/08/17.
 */

public class User {
    private String login;
    private String avatar_url;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
