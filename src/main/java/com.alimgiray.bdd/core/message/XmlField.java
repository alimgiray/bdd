package com.alimgiray.bdd.core.message;

public class XmlField {

    private final String fieldName;
    private XmlField parent;


    public XmlField(String fieldName) {
        this.fieldName = fieldName;
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
}
