package com.scodeid.firebaseauth;

/**
 * @Authors Created on 10/27/19 2:20 PM 2019
 * com.scodeid.firebaseauth
 * scode
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

public class User {

    String username;
    String email;
    String profileUrl; //gambar url

    public User() {

    }

    public User(String username, String email, String profileUrl) {
        this.username = username;
        this.email = email;
        this.profileUrl = profileUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
