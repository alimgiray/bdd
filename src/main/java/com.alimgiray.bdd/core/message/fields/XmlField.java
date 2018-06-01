package com.alimgiray.bdd.core.message.fields;

import java.util.ArrayList;
import java.util.List;

public class XmlField {

    private final String fieldName;
    private XmlField parent;
    private Namespace namespace;
    private List<Attribute> attributes;


    public XmlField(String fieldName) {
        this.fieldName = fieldName;
        this.attributes = new ArrayList<>();
    }

    public String getFieldName() {
        return fieldName;
    }

    public XmlField getParent() {
        return parent;
    }

    protected void setParent(XmlField parent) {
        this.parent = parent;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
