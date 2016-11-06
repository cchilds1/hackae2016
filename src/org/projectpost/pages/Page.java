package org.projectpost.pages;


import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD.*;
import fi.iki.elonen.NanoHTTPD.*;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.projectpost.Server;
import org.projectpost.sessions.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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

        return getPage(uriResource, urlParams, getUserSession(session));
    }

    public abstract NanoHTTPD.Response postPage(UriResource uriResource, Map<String, String> formParams, UserSession session);

    private UserSession getUserSession(NanoHTTPD.IHTTPSession session) {
        return new UserSession(null);
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
