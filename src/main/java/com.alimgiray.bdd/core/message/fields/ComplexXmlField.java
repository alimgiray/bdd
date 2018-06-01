package com.alimgiray.bdd.core.message.fields;

import java.util.ArrayList;
import java.util.List;

public class ComplexXmlField extends XmlField {

    private List<XmlField> xmlFields;

    public ComplexXmlField(String fieldName) {
        super(fieldName);
        xmlFields = new ArrayList<>();
    }

    public List<XmlField> getXmlFields() {
        return xmlFields;
    }

    public void addField(XmlField xmlField) {
        if (xmlField == null) return;
        xmlField.setParent(this);
        xmlFields.add(xmlField);
    }
}
