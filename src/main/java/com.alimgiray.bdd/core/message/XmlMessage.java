package com.alimgiray.bdd.core.message;


import org.xembly.Directives;
import org.xembly.Xembler;

import java.util.ArrayList;
import java.util.List;

public class XmlMessage {

    private List<XmlField> header;
    private List<XmlField> body;

    private Namespace soapenv;
    private static final String SOAP_NS_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    public XmlMessage() {
        this.soapenv = new Namespace("soapenv", SOAP_NS_URI);
        this.header = new ArrayList<>();
        this.body = new ArrayList<>();
    }

    public List<XmlField> getHeader() {
        return header;
    }

    public void addFieldToHeader(XmlField headerElement) {
        this.header.add(headerElement);
    }

    public void addFieldsToHeader(XmlField... headerElements) {
        for (XmlField headerElement : headerElements) {
            this.addFieldToHeader(headerElement);
        }
    }

    public List<XmlField> getBody() {
        return body;
    }

    public void addFieldToBody(XmlField bodyElement) {
        this.body.add(bodyElement);
    }

    public void addFieldsToBody(XmlField... bodyElements) {
        for (XmlField bodyElement : bodyElements) {
            this.addFieldToBody(bodyElement);
        }
    }

    @Override
    public String toString() {

        ComplexXmlField envelope = new ComplexXmlField("Envelope");
        ComplexXmlField header = new ComplexXmlField("Header");
        ComplexXmlField body = new ComplexXmlField("Body");

        for (XmlField headerElement : this.header) {
            header.addField(headerElement);
        }

        for (XmlField bodyElement : this.body) {
            body.addField(bodyElement);
        }

        envelope.setNamespace(this.soapenv);
        header.setNamespace(this.soapenv);
        body.setNamespace(this.soapenv);

        envelope.addField(header);
        envelope.addField(body);

        Directives directives = new Directives();
        build(envelope, directives);

        String soapEnvelope = "";

        try {
            soapEnvelope = new Xembler(directives).xml();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return soapEnvelope;
    }

    private void build(XmlField field, Directives directives) {

        if (field.getClass() == ComplexXmlField.class) {
            addWithNamespaceIfAvailable(field, directives);
            addAtributeIfAvailable(field, directives);
            List<XmlField> xmlFields = ((ComplexXmlField) field).getXmlFields();
            for (XmlField xmlField : xmlFields) {
                build(xmlField, directives);
            }
            directives.up();
        } else {
            addWithNamespaceIfAvailable(field, directives);
            addAtributeIfAvailable(field, directives);
            directives.set(((SimpleXmlField) field).getFieldValue())
                    .up();
        }
    }

    private void addAtributeIfAvailable(XmlField field, Directives directives) {
        List<Attribute> attributes = field.getAttributes();
        if (!attributes.isEmpty()) {
            for (Attribute attribute : attributes) {
                directives.attr(attribute.getKey(), attribute.getValue());
            }
        }
    }

    private void addWithNamespaceIfAvailable(XmlField field, Directives directives) {
        Namespace ns = field.getNamespace();
        if (ns != null) {
            directives.add(ns.getPrefix().concat(":").concat(field.getFieldName()));
            directives.attr("xmlns:".concat(ns.getPrefix()), ns.getUri());
        } else {
            directives.add(field.getFieldName());
        }
    }


}



