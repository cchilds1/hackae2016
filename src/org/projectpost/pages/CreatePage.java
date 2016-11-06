package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import freemarker.template.TemplateException;
import org.projectpost.data.Database;
import org.projectpost.data.ProjectData;
import org.projectpost.sessions.UserSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jared on 11/5/2016.
 */

public class CreatePage extends Page {
        public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {
            try{
                String createTemplate = renderTemplate("create.html", new HashMap<>());
                return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", createTemplate);
            } catch(IOException | TemplateException e)
                {
                return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");

        }
        }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        if (session.isLoggedIn())
            return newRedirectResponse("/");
        try {
            String name = formParams.get("name");
            String owner = formParams.get("owner");
            int location  = Integer.parseInt( formParams.get("location") );
            String time = formParams.get("time");
            String description = formParams.get("description");
            String image = formParams.get("image");
            int maxFunds = Integer.parseInt( formParams.get("maxFunds") );

            if (name.equals("") || owner.equals("") || location == 0 || time.equals("") || image.equals("")
                    || maxFunds <= 0)

            {
                return errMap("Fill out all fields");
            }

            ProjectData pd = Database.newProjectData();
            pd.name = name;
            pd.owner = owner;
            pd.location = location;
            pd.time = time;
            pd.description = description;
            pd.currentFunds = 0;
            pd.maxFunds = maxFunds;

            Database.saveProjectData(pd);
            return newRedirectResponse("/");
        } catch (Exception e)

        {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }

    public NanoHTTPD.Response errMap(String errorMes) {
        try {
            Map<String, Object> errorMap = new HashMap();
            errorMap.put("errorMessage", errorMes);
            String registerTemplate1 = renderTemplate("register.html", errorMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", registerTemplate1);
        } catch (IOException | TemplateException e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }

    }
}
