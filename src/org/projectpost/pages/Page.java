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
        UserSession us = new UserSession(null);
        return NanoHTTPD.newFixedLengthResponse(getPage(uriResource, urlParams, us));
    }

    public abstract String getPage(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, UserSession session);

    public String renderTemplate(String name, Map<String, Object> info) throws IOException, TemplateException {
        Template t = Server.config.getTemplate(name);
        StringWriter sw = new StringWriter();
        t.process(info, sw);

        return sw.toString();
    }
}