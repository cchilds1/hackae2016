package org.projectpost.sessions;


public class UserSession {

    private String uid;

    public UserSession(String uid) {
        this.uid = uid;
    }

    public boolean isLoggedIn() {
        return uid != null;
    }

    public String getUID() {
        return uid;
    }
}
