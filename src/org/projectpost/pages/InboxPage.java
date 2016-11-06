package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.projectpost.data.Database;
import org.projectpost.data.PostcardData;
import org.projectpost.sessions.UserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ddnaiman1 on 11/6/16.
 */
public class InboxPage extends Page {
    //an arraylist inside a map of all the postcards that a volunteer has ever received

    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {

        try {
            if (!session.isLoggedIn()) {
                return newRedirectResponse("/");
            }
            Map<String, Object> inboxMap = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            List<PostcardData> postcards = Database.getPostcardsForUser(session.getUID());
            for(PostcardData pd : postcards) {
                Map<String, Object> postcardMap = new HashMap<>();
                postcardMap.put("message", pd.message);
                postcardMap.put("sender", pd.fromUser);
                postcardMap.put("receiver", pd.toUser);
                postcardMap.put("projectName", pd.project);
                list.add(postcardMap);
            }
            inboxMap.put("postcards",list);
            String inboxTemplate = renderTemplate("inbox.html", inboxMap);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", inboxTemplate);
        } catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "failed to read template");
        }
    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        return null;
    }
}
