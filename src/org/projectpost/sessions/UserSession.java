package org.projectpost.sessions;

import fi.iki.elonen.NanoHTTPD;

public class UserSession {

    private String uid;
    private NanoHTTPD.IHTTPSession session;

    public UserSession(String uid, NanoHTTPD.IHTTPSession session) {
        this.uid = uid;
        this.session = session;
    }

    public boolean isLoggedIn() {
        return uid != null;
    }

    public String getUID() {
        return uid;
    }

    public NanoHTTPD.IHTTPSession getSession() {
        return session;
    }
}
