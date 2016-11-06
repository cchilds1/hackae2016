package org.projectpost.pages;

import org.eclipse.jetty.http.HttpStatus;
import org.projectpost.data.Database;
import org.projectpost.data.DonateData;
import org.projectpost.data.ProjectData;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectDonatePage extends Page {

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        try {
            if (!session.isLoggedIn()) {
                resp.sendRedirect("/");
                return;
            }
            String projectId = req.getParameter("pid");
            ProjectData pd = Database.getProjectData(projectId);
            if (pd == null){
                resp.sendRedirect("/");
                return;
            }

            DonateData dd = Database.newDonateData();
            dd.user = session.getUID();
            dd.project = projectId;
            dd.amount = 0;//later if possible
            Database.saveDonateData(dd);

            Map<String, Object> donateMap = new HashMap<>();
            donateMap.put("title",pd.name);// if we have time add the amount
            donateMap.put("imageData",pd.image);
            renderTemplate("donate.html", donateMap, resp.getWriter());
        } catch (Exception e) {
            sendError(resp, "failed to read template");
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        resp.setStatus(HttpStatus.NOT_FOUND_404);
    }
}
