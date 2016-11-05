package org.projectpost.pages;


import fi.iki.elonen.router.RouterNanoHTTPD.*;
import fi.iki.elonen.NanoHTTPD.*;

import java.io.InputStream;

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
}
