package org.projectpost.pages;

import org.projectpost.data.*;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectManagePage extends Page {

    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        try {
            if (!session.isLoggedIn()) {
                resp.sendRedirect("/");
                return;
            }

            ProjectData pd = Database.getProjectData(req.getParameter("id"));
            if (pd == null) {
                resp.sendRedirect("/");
                return;
            }
            if (!session.getUID().equals(pd.owner)) {
                resp.sendRedirect("/");
                return;
            }
            List<VolunteerData> volunteerDatas = Database.getVolunteerDataForProject(pd.uid);
            List<DonateData> donateDatas = Database.getDonateDataForProject(pd.uid);

            Map<String, Object> templateMap = new HashMap<>();
            List<Map<String, Object>> items = new ArrayList<>();

            for (VolunteerData vd : volunteerDatas) {
                Map<String, Object> item = new HashMap<>();
                item.put("isDonation", false);
                UserData ud = Database.getUserData(vd.user);
                item.put("fromName", ud.name);
                item.put("email", ud.email);
                item.put("phoneNumber", ud.phoneNumber);
                items.add(item);
            }

            for (DonateData dd : donateDatas) {
                Map<String, Object> item = new HashMap<>();
                item.put("isDonation", true);
                UserData ud = Database.getUserData(dd.user);
                item.put("fromName", ud.name);
                item.put("amount", dd.amount);
                item.put("donationUID", dd.uid);
                items.add(item);
            }

            templateMap.put("items", items);
            templateMap.put("projectUID", pd.uid);
            renderTemplate("manage.html", templateMap, resp.getWriter());
        } catch (Exception e) {
            sendError(resp, "failed to read template");
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        try {
            if (!session.isLoggedIn()) {
                resp.sendRedirect("/");
                return;
            }

            ProjectData pd = Database.getProjectData(req.getParameter("pid"));
            if (pd == null) {
                resp.sendRedirect("/");
                return;
            }

            if (!session.getUID().equals(pd.owner)) {
                resp.sendRedirect("/");
                return;
            }

            DonateData dd = Database.getDonateData(req.getParameter("donationUID"));

            String message = req.getParameter("message");

            PostcardData pod = Database.newPostcardData();
            pod.toUser = dd.user;
            pod.fromUser = pd.owner;
            pod.project = pd.uid;
            pod.message = message;
            Database.savePostcardData(pod);

            resp.sendRedirect("/");
        } catch (Exception e) {
            sendError(resp, "failed to read template");
        }
    }
}
