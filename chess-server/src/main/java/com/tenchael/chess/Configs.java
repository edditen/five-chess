package com.tenchael.chess;

import java.net.URISyntaxException;

public class Configs {

    public static final String INDEX = "index.html";
    public static String WEB_APP_BASE;

    static {
        try {
            WEB_APP_BASE = HttpRequestHandler.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().toString() + "webapp/";
            WEB_APP_BASE = !WEB_APP_BASE.contains("file:") ? WEB_APP_BASE : WEB_APP_BASE.substring(5);
            System.out.println(WEB_APP_BASE);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
