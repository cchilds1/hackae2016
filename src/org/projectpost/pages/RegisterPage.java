package org.projectpost.pages;

import freemarker.template.TemplateException;
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


public class RegisterPage extends Page {

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        if (session.isLoggedIn()) {
            resp.sendRedirect("/");
            return;
        }

        try {
            renderTemplate("register.html", new HashMap<>(), resp.getWriter());
        } catch (IOException | TemplateException e) {
            sendError(resp, "failed to read template");
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        if (session.isLoggedIn()) {
            resp.sendRedirect("/");
            return;
        }

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String rpassword = req.getParameter("rpassword");
            String email = req.getParameter("email");
            String phonenumber = req.getParameter("phonenumber");
            String zipcode = req.getParameter("zipcode");
            String name = req.getParameter("name");



            if (username.equals("") || password.equals("") || rpassword.equals("") || email.equals("") || phonenumber.equals("") || zipcode.equals("") || name.equals("")) {
                errMap(resp, "Fill out all fields");
                return;
            }
            if (!(password.equals(rpassword))) {
                errMap(resp, "Passwords do not match");
                return;
            }

            if (Database.userExists(username)) {
                errMap(resp, "Username already taken");
                return;
            }
            UserData ud = Database.newUserData();
            ud.name = name;
            ud.email = email;
            ud.phoneNumber = phonenumber;
            ud.address = zipcode;
            ud.username = username;
            MessageDigest md = MessageDigest.getInstance("MD5");
            ud.passhash = DatatypeConverter.printHexBinary(md.digest(password.getBytes()));
            Database.saveUserData(ud);

            resp.sendRedirect("/");
        } catch (Exception e) {
            sendError(resp, "failed to read template");
            e.printStackTrace();
        }
    }

    public void errMap(HttpServletResponse resp, String errorMes) throws IOException {
        try {
            Map<String, Object> errorMap = new HashMap();
            errorMap.put("errorMessage", errorMes);
            renderTemplate("register.html", errorMap, resp.getWriter());
        } catch (IOException | TemplateException e) {
            sendError(resp, "failed to read template");
        }

    }
}
