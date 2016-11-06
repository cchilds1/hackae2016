package org.projectpost;


import freemarker.template.Configuration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.projectpost.data.Database;
import org.projectpost.pages.*;

import javax.swing.text.html.CSS;
import java.io.File;
import java.io.IOException;

public class ServerStart {

    private final static int PORT = 8080;
    public static Configuration config;

    public static void main(String[] args) {
        try {
            config = new Configuration(Configuration.VERSION_2_3_25);

            config.setDirectoryForTemplateLoading(new File("."));

            Database.init();

            ServerStart s = new ServerStart();
            s.start();
        } catch (Exception ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    public void start() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        server.setHandler(handler);

        handler.addServlet(HomePage.class, "/");
        handler.addServlet(LoginPage.class, "/login");
        handler.addServlet(CreatePage.class, "/create");
        handler.addServlet(CSSEndpoint.class, "/css/*");
        handler.addServlet(ImagesEndpoint.class, "/images/*");
        handler.addServlet(ProjectDonatePage.class, "/project/donate");
        handler.addServlet(ProjectManagePage.class, "/project/manage");
        handler.addServlet(ProjectPage.class, "/project");
        handler.addServlet(ProjectsPage.class, "/projects");
        handler.addServlet(ProjectVolunteerPage.class, "/project/volunteer");
        handler.addServlet(RegisterPage.class, "/register");

        server.start();

        server.join();
    }
}
