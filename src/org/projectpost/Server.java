package org.projectpost;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import freemarker.template.Configuration;
import org.projectpost.data.Database;
import org.projectpost.pages.*;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

public class Server extends RouterNanoHTTPD {

    private final static int PORT = 8080;
    public static Configuration config;

    public static void main(String[] args) {
        try {
            config = new Configuration(Configuration.VERSION_2_3_25);

            config.setDirectoryForTemplateLoading(new File("."));

            Database.init();

            Server s = new Server();
            s.start();
        } catch (Exception ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }


    public Server() {
        super(PORT);
    }

    public void start() throws IOException {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public void addMappings() {
        super.addMappings();

        addRoute("/", HomePage.class);
        addRoute("/login", LoginPage.class);
        addRoute("/create", CreatePage.class);
        addRoute("/css/", CSSEndpoint.class);
        addRoute("/images/", ImagesEndpoint.class);
        addRoute("/projects/:id/donate", ProjectDonatePage.class);
        addRoute("/projects/:id/manage", ProjectManagePage.class);
        addRoute("/projects/:id", ProjectPage.class);
        addRoute("/projects", ProjectsPage.class);
        addRoute("/projects/:id/volunteer", ProjectVolunteerPage.class);
        addRoute("/register", RegisterPage.class);
    }
}
