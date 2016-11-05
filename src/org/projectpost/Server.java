package org.projectpost;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class Server extends NanoHTTPD {

    private final static int PORT = 8080;

    public static void main(String[] args) {
        try {
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
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse("Hello World!");
    }


}
