package org.projectpost.pages;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.projectpost.sessions.UserSession;

import java.util.Map;

public class ProjectsPage extends Page {
    @Override
    public NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session) {

    }

    @Override
    public NanoHTTPD.Response postPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> formParams, UserSession session) {
        return null;
    }
}
