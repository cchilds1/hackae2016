package org.projectpost.pages;

import org.eclipse.jetty.http.HttpStatus;
import org.projectpost.data.Database;
import org.projectpost.data.ProjectData;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectsPage extends Page {

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        if (!session.isLoggedIn()) {
            resp.sendRedirect("/");
            return;
        }
        try {
            Map<String, Object> projectsMap = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList();
            List<ProjectData> pdList = Database.getProjectsByUser(session.getUID());
            for(ProjectData pd: pdList)
            {
                Map<String, Object> pdMap = new HashMap();
                pdMap.put("title", pd.name);
                pdMap.put("ownerName",pd.owner);
                pdMap.put("location",pd.location);
                pdMap.put("time", pd.time);
                pdMap.put("description", pd.description);
                pdMap.put("imageData",pd.image);
                pdMap.put("uid", pd.uid);
                list.add(pdMap);
            }
            projectsMap.put("projects", list);
            renderTemplate("projects.html", projectsMap, resp.getWriter());
        }catch(Exception e){
            sendError(resp, "failed to read template: " + e.getMessage());
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        resp.setStatus(HttpStatus.NOT_FOUND_404);
    }
}
