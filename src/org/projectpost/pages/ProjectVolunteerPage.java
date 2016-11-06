package org.projectpost.pages;

import org.eclipse.jetty.http.HttpStatus;
import org.projectpost.data.Database;
import org.projectpost.data.ProjectData;
import org.projectpost.data.VolunteerData;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectVolunteerPage extends Page {

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        if (!session.isLoggedIn()) {
            resp.sendRedirect("/");
            return;
        }

        try {
            String projectId = req.getParameter("pid");
            ProjectData pd = Database.getProjectData(projectId);
            if (pd == null) {
                resp.sendRedirect("/");
                return;
            }

            VolunteerData vd = Database.newVolunteerData();
            vd.user = session.getUID();
            vd.project = projectId;
            Database.saveVolunteerData(vd);

            Map<String, Object> volunteerMap = new HashMap();
            volunteerMap.put("username", pd.owner);
            volunteerMap.put("projectName", pd.name);
            volunteerMap.put("location", pd.location);
            volunteerMap.put("time", pd.time);
            renderTemplate("volunteer.html", volunteerMap, resp.getWriter());
        } catch (Exception e) {
            sendError(resp, "failed to read template");
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        resp.setStatus(HttpStatus.NOT_FOUND_404);
    }
}
