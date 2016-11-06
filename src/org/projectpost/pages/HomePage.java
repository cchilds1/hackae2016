package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import freemarker.ext.util.IdentityHashMap;
import freemarker.template.TemplateException;
import org.projectpost.data.Database;
import org.projectpost.data.UserData;
import org.projectpost.sessions.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.projectpost.data.Database.getUserData;

/**
 * Created by ddnaiman1 on 11/5/16.
 */
public class HomePage extends Page {
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {

        try {
            Map<String, Object> homeMap = new HashMap<>();
            if (session.isLoggedIn()) {
                UserData ud = Database.getUserData(session.getUID());
                homeMap.put("user", new HashMap<String, Object>() {
                    {
                        put("name", ud.username);
                    }
                });
            }

            String homeTemplate = renderTemplate("Home.html", homeMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", homeTemplate);
        } catch (IOException | TemplateException | SQLException e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");

        }


    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        return null;
    }
}
