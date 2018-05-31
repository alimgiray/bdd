package com.alimgiray.bdd.core.message;


import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xembly.Directives;
import org.xembly.Xembler;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlMessage {

    private List<XmlField> header;
    private List<XmlField> body;

    private ComplexXmlField root;

    private Namespace soapenv;
    private static final String SOAP_NS_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    public XmlMessage() {
        this.soapenv = new Namespace("soapenv", SOAP_NS_URI);
        this.header = new ArrayList<>();
        this.body = new ArrayList<>();
        this.root = new ComplexXmlField("root");
    }

    public XmlMessage(String xml) throws IOException, JDOMException {
        this();
        SAXBuilder saxBuilder = new SAXBuilder();
        org.jdom2.Document document = saxBuilder.build(new InputSource(new StringReader(xml)));
        transformStringToXmlMessage(document.getRootElement(), this.root);

        //Çevirirken header body bakmadan çeviriyor, sonradan çomar gibi headerı soapı set ediyorum burada indexiyle.
        //FIXME: burayı düşüneceğim
        ComplexXmlField soapHeader = (ComplexXmlField) ((ComplexXmlField) this.root.getXmlFields().get(0)).getXmlFields().get(0);
        ComplexXmlField soapBody = (ComplexXmlField) ((ComplexXmlField) this.root.getXmlFields().get(0)).getXmlFields().get(1);
        for (XmlField headerElement : soapHeader.getXmlFields()) {
            addFieldToHeader(headerElement);
        }
        for (XmlField headerElement : soapBody.getXmlFields()) {
            addFieldToBody(headerElement);
        }
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
        buildDirectives(envelope, directives);
        String soapEnvelope = "";
        try {
            soapEnvelope = new Xembler(directives).xml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soapEnvelope;

    }

    private void buildDirectives(XmlField field, Directives directives) {

        if (field.getClass() == ComplexXmlField.class) {
            addAndSetNamespaceIfAvailable(field, directives);
            setAttributeIfAvailable(field, directives);
            List<XmlField> xmlFields = ((ComplexXmlField) field).getXmlFields();
            for (XmlField xmlField : xmlFields) {
                buildDirectives(xmlField, directives);
            }
            directives.up();
        } else {
            addAndSetNamespaceIfAvailable(field, directives);
            setAttributeIfAvailable(field, directives);
            directives.set(((SimpleXmlField) field).getFieldValue())
                    .up();
        }
    }

    private void setAttributeIfAvailable(XmlField field, Directives directives) {
        List<Attribute> attributes = field.getAttributes();
        if (!attributes.isEmpty()) {
            for (Attribute attribute : attributes) {
                directives.attr(attribute.getKey(), attribute.getValue());
            }
        }
    }

    private void addAndSetNamespaceIfAvailable(XmlField field, Directives directives) {
        Namespace ns = field.getNamespace();
        if (ns != null) {
            directives.add(ns.getPrefix().concat(":").concat(field.getFieldName()));
            directives.attr("xmlns:".concat(ns.getPrefix()), ns.getUri());
        } else {
            directives.add(field.getFieldName());
        }
    }

    private void transformStringToXmlMessage(Element element, XmlField parentField) {

        if (element.getChildren().isEmpty()) {
            SimpleXmlField simpleXmlField = new SimpleXmlField(element.getName(), SoapDataType.STRING);
            simpleXmlField.setFieldValue(element.getValue());
            addNamespaceIfAvailable(simpleXmlField, element);
            setAttributeIfAvailable(simpleXmlField, element);
            ((ComplexXmlField) parentField).addField(simpleXmlField);

        } else {
            ComplexXmlField complexXmlField = new ComplexXmlField(element.getName());
            ((ComplexXmlField) parentField).addField(complexXmlField);
            addNamespaceIfAvailable(complexXmlField, element);
            setAttributeIfAvailable(complexXmlField, element);
            parentField = complexXmlField;
            for (Element childElement : element.getChildren()) {
                transformStringToXmlMessage(childElement, parentField);
            }
        }
    }

    private void addNamespaceIfAvailable(XmlField field, Element element) {
        if (element.getNamespace() != org.jdom2.Namespace.NO_NAMESPACE) {
            Namespace namespace = new Namespace(element.getNamespacePrefix(), element.getNamespaceURI());
            field.setNamespace(namespace);
        }
    }

    private void setAttributeIfAvailable(XmlField field, Element element) {
        if (element.hasAttributes()) {
            for (org.jdom2.Attribute attribute : element.getAttributes()) {
                Attribute attributeToAdd = new Attribute(attribute.getName(),attribute.getValue());
                field.addAttribute(attributeToAdd);
            }
        }
    }
}



