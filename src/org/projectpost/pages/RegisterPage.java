package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import freemarker.template.TemplateException;
import org.projectpost.data.Database;
import org.projectpost.data.UserData;
import org.projectpost.sessions.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Claire on 11/5/2016.
 */
public class RegisterPage extends Page {
    @Override
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {
        if (session.isLoggedIn())
            return newRedirectResponse("/");
        try {
            String registerTemplate = renderTemplate("register.html", new HashMap<>());
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", registerTemplate);
        } catch (IOException | TemplateException e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        if (session.isLoggedIn())
            return newRedirectResponse("/");
        try {
            String registerTemplate = renderTemplate("register.html", new HashMap<>());
            //return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", registerTemplate);


            String username = formParams.get("username");
            String password = formParams.get("password");
            String rpassword = formParams.get("rpassword");
            String email = formParams.get("email");
            String phonenumber = formParams.get("phonenumber");
            String zipcode = formParams.get("zipcode");
            String name = formParams.get("name");


            if (username.equals("") || password.equals("") || rpassword.equals("") || email.equals("") || phonenumber.equals("")
                    || zipcode.equals("") || name.equals(""))

            {
                return errMap("Fill out all fields");
            }
            if (!(password.equals(rpassword))) {
                return errMap("Passwords do not match");
            }

            if (Database.userExists(username)) {
                return errMap("Username already taken");
            }
            UserData ud = Database.newUserData();
            ud.name = name;
            ud.email = email;
            ud.phoneNumber = phonenumber;
            ud.address = zipcode;
            ud.username = username;
            MessageDigest md = MessageDigest.getInstance("MD5");
            ud.passhash = new String(md.digest(password.getBytes()));
            Database.saveUserData(ud);
            return newRedirectResponse("/");
        } catch (
                Exception e)

        {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");


        }



    }



    public NanoHTTPD.Response errMap(String errorMes) {
        try {
            Map<String, Object> errorMap = new HashMap();
            errorMap.put("errorMessage", errorMes);
            String registerTemplate1 = renderTemplate("register.html", errorMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", registerTemplate1);
        } catch (IOException | TemplateException e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }

    }
}
