package com.alimgiray.bdd.core.message;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xembly.Directives;
import org.xembly.Xembler;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ahmyilmaz
 * @since 31.05.2018
 */
public class SoapMessage extends XmlMessage {

    private List<XmlField> header;
    private List<XmlField> body;

    private ComplexXmlField root;

    private Namespace soapenv;
    private static final String SOAP_NS_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    public SoapMessage() {
        setup();
    }
    public SoapMessage(String xml) throws IOException, JDOMException {

        setup();

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new InputSource(new StringReader(xml)));

        createFromString(document.getRootElement(), this.root);

        //Çevirirken header body bakmadan çeviriyor, sonradan çomar gibi headerı bodyi set ediyorum burada indexiyle.
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

    private void setup() {
        this.soapenv = new Namespace("soapenv", SOAP_NS_URI);
        this.header = new ArrayList<>();
        this.body = new ArrayList<>();
        this.root = new ComplexXmlField("root");
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

        ComplexXmlField envelope = getField("Envelope");
        ComplexXmlField header = getField("Header");
        ComplexXmlField body = getField("Body");

        populateField(header, this.header);
        populateField(body, this.body);
        populateField(envelope, Arrays.asList(header, body));

        Directives directives = new Directives();
        buildDirectives(envelope, directives);

        try {
            return new Xembler(directives).xml();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void populateField(ComplexXmlField field, List<XmlField> childFields) {
        for (XmlField element : childFields) {
            field.addField(element);
        }
    }

    private ComplexXmlField getField(String fieldName) {
        ComplexXmlField complexXmlField = new ComplexXmlField(fieldName);
        complexXmlField.setNamespace(this.soapenv);
        return complexXmlField;
    }

}
