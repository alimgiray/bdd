package com.alimgiray.bdd.core.subject;

/**
 * @author aaytar
 * @since 30.05.2018 11:56
 */
public class Endpoint {

    private String name;
    private String url;

    public Endpoint(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
