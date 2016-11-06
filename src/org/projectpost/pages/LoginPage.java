package org.projectpost.pages;

import org.projectpost.data.Database;
import org.projectpost.data.UserData;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends Page {

    private boolean checkHashPass(String typedPassword, String storedPassHash){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(typedPassword.getBytes()), DatatypeConverter.parseHexBinary(storedPassHash));
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        try {
            if (session.isLoggedIn()) {
                resp.sendRedirect("/");
                return;
            }
            Map<String, Object> loginMap = new HashMap<>();
            renderTemplate("login.html", loginMap, resp.getWriter());
        } catch (Exception e) {
            sendError(resp, "failed to read template");
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        try {
            if (session.isLoggedIn()) {
                resp.sendRedirect("/");
                return;
            }
            UserData ud = Database.getUserByUsername(req.getParameter("username"));
            if (ud == null || !checkHashPass(req.getParameter("password"), ud.passhash)){
                Map<String,Object> errorMap = new HashMap<>();
                errorMap.put("errorMessage","Invalid Username Or Password");
                renderTemplate("login.html", errorMap, resp.getWriter());
            }else {
                System.out.println("Uid of new session user: " + ud.uid);
                String id = Database.newSession(ud.uid);
                setCookieValue(resp, "postSession", id, 180000);
                resp.sendRedirect("/");
                return;
            }
        } catch (Exception e) {
            sendError(resp, "failed to read template");
        }
    }
}
