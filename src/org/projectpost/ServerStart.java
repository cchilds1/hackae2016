package org.projectpost;


import freemarker.template.Configuration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.projectpost.data.Database;
import org.projectpost.pages.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

//        ServletContextHandler handler = new ServletContextHandler();
//        handler.
//        server.setHandler(handler);
//
//


        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(HomePage.class, "/");
        handler.addServletWithMapping(LoginPage.class, "/login");
        handler.addServletWithMapping(CreatePage.class, "/create");
        handler.addServletWithMapping(CSSEndpoint.class, "/css/*");
        handler.addServletWithMapping(ImagesEndpoint.class, "/images/*");
        handler.addServletWithMapping(ProjectDonatePage.class, "/project/donate/");
        handler.addServletWithMapping(ProjectManagePage.class, "/project/manage/");
        handler.addServletWithMapping(ProjectsPage.class, "/projects");
        handler.addServletWithMapping(ProjectPage.class, "/project/");
        handler.addServletWithMapping(ProjectVolunteerPage.class, "/project/volunteer/");
        handler.addServletWithMapping(RegisterPage.class, "/register");





        server.start();

        server.join();
    }

    public static class RouterServlet extends HttpServlet {

        private Page getPage(String url) {
            if (url.contains("?")) {
                url = url.substring(0, url.indexOf('?'));
            }

            if (url.startsWith("/login")) {
                return new LoginPage();
            }

            if (url.startsWith("/create")) {
                return new CreatePage();
            }

            if (url.startsWith("/register")) {
                return new RegisterPage();
            }

            if (url.startsWith("/css/")) {
                return new CSSEndpoint();
            }

            if (url.startsWith("/images/")) {
                return new ImagesEndpoint();
            }

            if (url.startsWith("/projects")) {
                return new ProjectsPage();
            }

            if (url.startsWith("/project/")) {
                url = url.replaceFirst("/project/", "");

                if (url.startsWith("donate")) {
                    return new ProjectDonatePage();
                }

                if (url.startsWith("manage")) {
                    return new ProjectManagePage();
                }

                if (url.startsWith("volunteer")) {
                    return new ProjectVolunteerPage();
                }
            }

            return new HomePage();
        }
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            getPage(req.getRequestURI()).doGetYeah(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            getPage(req.getRequestURI()).doPostYeah(req, resp);
        }
    }
}
