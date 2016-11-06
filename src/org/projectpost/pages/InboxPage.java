package org.projectpost.pages;

import org.eclipse.jetty.http.HttpStatus;
import org.projectpost.data.Database;
import org.projectpost.data.PostcardData;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InboxPage extends Page {

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        try {
            if (!session.isLoggedIn()) {
                resp.sendRedirect("/");
                return;
            }

            Map<String, Object> inboxMap = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            List<PostcardData> postcards = Database.getPostcardsForUser(session.getUID());
            for(PostcardData pd : postcards) {
                Map<String, Object> postcardMap = new HashMap<>();
                postcardMap.put("message", pd.message);
                postcardMap.put("sender", pd.fromUser);
                postcardMap.put("receiver", pd.toUser);
                postcardMap.put("projectName", pd.project);
                list.add(postcardMap);
            }
            inboxMap.put("postcards",list);
            renderTemplate("inbox.html", inboxMap, resp.getWriter());
        } catch (Exception e) {
            sendError(resp, "failed to read template");
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        resp.setStatus(HttpStatus.NOT_FOUND_404);
    }
}
