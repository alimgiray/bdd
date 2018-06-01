package com.alimgiray.bdd.core.message.fields;

/**
 * @author ahmyilmaz
 * @since 28.05.2018
 */
public class Attribute {

    private final String key;
    private final String value;

    public Attribute(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
