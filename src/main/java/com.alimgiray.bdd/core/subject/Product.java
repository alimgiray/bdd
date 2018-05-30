package com.alimgiray.bdd.core.subject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aaytar
 * @since 30.05.2018 11:55
 */
public class Product {

    private String name;
    private List<Endpoint> endpoints;

    public Product(String name) {
        this.name = name;
        endpoints = new ArrayList<>();
    }

    public void addEndpoint(Endpoint endpoint) {
        endpoints.add(endpoint);
    }

}
