package org.projectpost.pages;

import org.eclipse.jetty.http.HttpStatus;
import org.projectpost.sessions.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImagesEndpoint extends Page {
    @Override
    public void getPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        String resource = uri.substring(uri.lastIndexOf("/") + 1);
        resource = resource.replaceAll("\\.\\.", "");

        try {
            Path p = Paths.get("images", resource);
            Files.copy(p, resp.getOutputStream());
            resp.setContentType(getServletContext().getMimeType(p.getFileName().toString()));
        } catch (FileNotFoundException e) {
            resp.setStatus(HttpStatus.NOT_FOUND_404);
        } catch (IOException e) {
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public void postPage(HttpServletRequest req, HttpServletResponse resp, UserSession session) throws ServletException, IOException {
        resp.setStatus(HttpStatus.NOT_FOUND_404);
    }
}
