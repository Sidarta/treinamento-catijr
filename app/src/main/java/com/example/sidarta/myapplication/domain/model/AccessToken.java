package com.example.sidarta.myapplication.domain.model;

/**
 * Created by sidarta on 14/08/2017.
 */

public class AccessToken {
    public String access_token;
    public String token_type;

    public String getAuthCredential() {
        final char firstChar = token_type.charAt(0);
        if (!Character.isUpperCase(firstChar)) {
            final String first = Character.toString(firstChar).toUpperCase();
            token_type = first + token_type.substring(1);
        }
        return token_type + " " + access_token;
    }
}
