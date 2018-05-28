package com.alimgiray.bdd.core.message;

/**
 * @author ahmyilmaz
 * @since 28.05.2018
 */
public class Namespace {
    private final String uri;
    private final String prefix;

    public Namespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public String getPrefix() {
        return prefix;
    }
}
