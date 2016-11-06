package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.projectpost.data.Database;
import org.projectpost.data.ProjectData;
import org.projectpost.data.VolunteerData;
import org.projectpost.sessions.UserSession;

import java.util.HashMap;
import java.util.Map;

public class ProjectVolunteerPage extends Page {
    @Override
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {
        if (!session.isLoggedIn())
            return newRedirectResponse("/");
        try {
            String projectId = urlParams.get("id");
            ProjectData pd = Database.getProjectData(projectId);
            if(pd==null)
                return newRedirectResponse("/");
            Map<String, Object> volunteerMap = new HashMap();
            volunteerMap.put("username", pd.owner);
            volunteerMap.put("projectName", pd.name);
            volunteerMap.put("location",pd.location);
            volunteerMap.put("time", pd.time);
            String volunteerTemplate = renderTemplate("volunteer.html", volunteerMap);
            VolunteerData vd = Database.newVolunteerData();
            String username = urlParams.get("username");
            vd.user = session.getUID();
            vd.project = projectId;
            Database.saveVolunteerData(vd);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", volunteerTemplate);
        }
        catch(Exception e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
        }

        @Override
        public NanoHTTPD.Response postPage (RouterNanoHTTPD.UriResource
        uriResource, Map < String, String > formParams, UserSession session){
            return null;
        }
    }
