package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.projectpost.data.Database;
import org.projectpost.data.ProjectData;
import org.projectpost.sessions.UserSession;

import java.util.HashMap;
import java.util.Map;

public class ProjectDonatePage extends Page {
    @Override
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {
        try {
            if (!session.isLoggedIn()) {
                return newRedirectResponse("/");
            }
            String projectId = urlParams.get("id");
            ProjectData pd = Database.getProjectData(projectId);
            if (pd == null){
                return newRedirectResponse("/");
            }
            Map<String, Object> donateMap = new HashMap<>();
            donateMap.put("projectName",pd.name);// if we have time add the amount
            String donateTemplate = renderTemplate("donate.html", donateMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", donateTemplate);
        } catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        return null;
    }
}
