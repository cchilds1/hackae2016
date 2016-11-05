package org.projectpost;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import freemarker.template.Configuration;
import org.projectpost.pages.HomePage;

import java.io.File;
import java.io.IOException;

public class Server extends RouterNanoHTTPD {

    private final static int PORT = 8080;
    public static Configuration config;

    public static void main(String[] args) {
        try {
            config = new Configuration(Configuration.VERSION_2_3_25);

            config.setDirectoryForTemplateLoading(new File("web"));

            Server s = new Server();
            s.start();
        } catch (IOException ioe) {
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
    }
}
