package com.alimgiray.bdd.core.message;

import java.util.List;

public class XmlMessage {

    private List<XmlField> header;
    private List<XmlField> body;

    public XmlMessage() {

    }

    public String toSoapMessage() {
        return "";
    }

    public String toJson() {
        return "";
    }

    public void addFieldToHeader(XmlField xmlField) {
        header.add(xmlField);
    }

    public void addFieldToBody(XmlField xmlField) {
        body.add(xmlField);
    }

}
