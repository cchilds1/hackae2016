package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.projectpost.data.Database;
import org.projectpost.data.ProjectData;
import org.projectpost.sessions.UserSession;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProjectPage extends Page {
    @Override
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {
        String projectID = urlParams.get("id");
        try {

            ProjectData pd = Database.getProjectData(projectID);
            if(pd == null)
            {
                return newRedirectResponse("/");
            }

            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("title", pd.name);
            projectMap.put("owner", pd.owner);
            projectMap.put("imageData", pd.image);
            projectMap.put("description", pd.description);
            String projectTemplate = renderTemplate("project.html", projectMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", projectTemplate);
        } catch(Exception e){
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        return null;
    }
}
