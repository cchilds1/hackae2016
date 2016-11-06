package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import freemarker.ext.beans.HashAdapter;
import org.projectpost.data.*;
import org.projectpost.sessions.UserSession;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectManagePage extends Page {
    @Override
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {
        try {
            if (!session.isLoggedIn()) {
                return newRedirectResponse("/");
            }

            ProjectData pd = Database.getProjectData(urlParams.get("id"));
            if (pd == null) {
                return newRedirectResponse("/");
            }

            if (!session.getUID().equals(pd.owner)) {
                return newRedirectResponse("/");
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
            String template = renderTemplate("manage.html", templateMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", template);
        } catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        try {
            if (!session.isLoggedIn()) {
                return newRedirectResponse("/");
            }

            ProjectData pd = Database.getProjectData(formParams.get("projectUID"));
            if (pd == null) {
                return newRedirectResponse("/");
            }

            if (!session.getUID().equals(pd.owner)) {
                return newRedirectResponse("/");
            }

            DonateData dd = Database.getDonateData(formParams.get("donationUID"));

            String message = formParams.get("message");

            PostcardData pod = Database.newPostcardData();
            pod.toUser = dd.user;
            pod.fromUser = pd.owner;
            pod.project = pd.uid;
            pod.message = message;
            Database.savePostcardData(pod);

            return newRedirectResponse("/");
        } catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }
}
