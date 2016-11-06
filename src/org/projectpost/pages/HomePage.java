package org.projectpost.pages;

import freemarker.template.TemplateException;
import org.eclipse.jetty.http.HttpStatus;
import org.projectpost.data.Database;
import org.projectpost.data.UserData;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class HomePage extends Page {

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        try {
            Map<String, Object> homeMap = new HashMap<>();
            if (session.isLoggedIn()) {
                System.out.println(session.getUID());
                UserData ud = Database.getUserData(session.getUID());
                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("name", ud.name);
                homeMap.put("user", userMap);
            }

            renderTemplate("Home.html", homeMap, resp.getWriter());
        } catch (IOException | TemplateException | SQLException e) {
            sendError(resp, "Internal error: " + e.getMessage());
        }

    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        resp.setStatus(HttpStatus.NOT_FOUND_404);
    }
}
