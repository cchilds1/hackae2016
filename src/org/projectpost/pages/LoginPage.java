package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import freemarker.core.ReturnInstruction;
import freemarker.template.TemplateException;
import org.projectpost.data.Database;
import org.projectpost.data.UserData;
import org.projectpost.sessions.UserSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ddnaiman1 on 11/5/16.
 */
public class LoginPage extends Page {
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {

        try {
            if (session.isLoggedIn()) {
                return newRedirectResponse("/");
            }
            Map<String, Object> loginMap = new HashMap<>();
            String loginTemplate = renderTemplate("login.html", loginMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", loginTemplate);
        } catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        try {
            if (session.isLoggedIn()) {
                return newRedirectResponse("/");
            }
            UserData ud = Database.getUserByUsername(formParams.get("username"));
            if (ud == null || !checkHashPass(formParams.get("password"), ud.passhash)){
                Map<String,Object> errorMap = new HashMap<>();
                errorMap.put("errorMessage","Invalid Username Or Password");
                String loginTemplate = renderTemplate("login.html", errorMap);
                return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", loginTemplate);
            }else {
                String id = Database.newSession(ud.uid);
                session.getSession().getCookies().set("postSession",id,180000);
                return newRedirectResponse("/");
            }
        } catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");

        }
    }
    private boolean checkHashPass(String typedPassword, String storedPassHash){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(typedPassword.getBytes()), storedPassHash.getBytes());
        }catch (Exception e){
            return false;
        }
    }
}
