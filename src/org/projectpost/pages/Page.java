package org.projectpost.pages;


import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.eclipse.jetty.http.HttpStatus;
import org.projectpost.ServerStart;
import org.projectpost.data.Database;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Map;

public abstract class Page extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getPage(req, resp, getUserSession(req));
    }

    public void doGetYeah(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public abstract void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        postPage(req, resp, getUserSession(req));
    }

    public void doPostYeah(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    public abstract void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException;

    private UserSession getUserSession(HttpServletRequest req) {
        String sessionID = getCookieValue(req, "postSession");
        if (sessionID.isEmpty()) {
            return new UserSession(null);
        }
        String userID = null;
        try {
            userID = Database.getSessionUserUid(sessionID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new UserSession(userID);
    }

    public String getCookieValue(HttpServletRequest req, String c) {
        if (req.getCookies() == null) {
            return "";
        }
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals(c)) {
                return cookie.getValue();
            }
        }

        return "";
    }

    public void setCookieValue(HttpServletResponse resp, String cn, String cv, int expires) {
        Cookie c = new Cookie(cn, cv);
        c.setMaxAge(expires);
        resp.addCookie(c);
    }

    public void renderTemplate(String name, Map<String, Object> info, Writer out) throws IOException, TemplateException {
        Template t = ServerStart.config.getTemplate(name);
        t.process(info, out);
    }

    public void sendError(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
        resp.getWriter().print(message);
    }
}
