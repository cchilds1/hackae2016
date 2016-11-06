package org.projectpost.pages;


import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD.*;
import fi.iki.elonen.NanoHTTPD.*;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.projectpost.Server;
import org.projectpost.data.Database;
import org.projectpost.sessions.UserSession;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class Page extends DefaultHandler {

    public Page() {
        super();
    }

    @Override
    public String getMimeType() {
        return "text/html";
    }

    @Override
    public InputStream getData() {
        return super.getData();
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public Response.IStatus getStatus() {
        return Response.Status.OK;
    }

    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        return getPage(uriResource, urlParams, getUserSession(session));
    }

    public abstract NanoHTTPD.Response getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session);

    @Override
    public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        Map<String, String> formParams = new HashMap<>();
        try {
            session.parseBody(formParams);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }

        return getPage(uriResource, formParams, getUserSession(session));
    }

    public abstract NanoHTTPD.Response postPage(UriResource uriResource, Map<String, String> formParams, UserSession session);

    private UserSession getUserSession(NanoHTTPD.IHTTPSession session) {
        String sessionID = session.getCookies().read("postSession");
        if (sessionID.isEmpty()) {
            return new UserSession(null, session);
        }
        String userID = null;
        try {
            userID = Database.getSessionUserUid(sessionID);
        } catch (SQLException e) {
        }
        return new UserSession(userID, session);
    }

    public String renderTemplate(String name, Map<String, Object> info) throws IOException, TemplateException {
        Template t = Server.config.getTemplate(name);
        StringWriter sw = new StringWriter();
        t.process(info, sw);

        return sw.toString();
    }

    public NanoHTTPD.Response newRedirectResponse(String redirectTo) {
        Response r = NanoHTTPD.newFixedLengthResponse(Response.Status.REDIRECT_SEE_OTHER, "text/html", "");
        r.addHeader("Location", redirectTo);
        return r;
    }
}
