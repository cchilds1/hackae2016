package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.projectpost.data.Database;
import org.projectpost.data.PostcardData;
import org.projectpost.data.ProjectData;
import org.projectpost.sessions.UserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectsPage extends Page {
    @Override
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {
        if (!session.isLoggedIn()) {
            return newRedirectResponse("/");
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
                list.add(pdMap);
            }
            projectsMap.put("pdList", list);
            String projectsTemplate = renderTemplate("projects.html", projectsMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", projectsTemplate);
        }catch(Exception e){
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        return null;
    }
}
