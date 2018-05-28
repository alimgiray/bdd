package com.alimgiray.bdd.core.message;


import org.w3c.dom.Document;
import org.xembly.Directives;
import org.xembly.Xembler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

public class XmlMessage {

    private XmlField header;
    private XmlField body;

    private Namespace soapenv;
    private static final String SOAP_NS_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    public XmlMessage() {
        this.soapenv = new Namespace("soapenv", SOAP_NS_URI);
    }



    public XmlField getBody() {
        return body;
    }

    public void setBody(XmlField body) {
        this.body = body;
    }

    public XmlField getHeader() {
        return header;
    }

    public void setHeader(XmlField header) {
        this.header = header;
    }

    @Override
    public String toString() {
        ComplexXmlField envelope = new ComplexXmlField("Envelope");
        envelope.setNamespace(this.soapenv);
        this.header.setNamespace(this.soapenv);
        this.body.setNamespace(this.soapenv);
        envelope.addField(this.header);
        envelope.addField(this.body);
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



