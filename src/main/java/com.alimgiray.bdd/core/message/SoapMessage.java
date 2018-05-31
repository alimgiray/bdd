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

/**
 * @author ahmyilmaz
 * @since 31.05.2018
 */
public class SoapMessage extends XmlMessage{
    private List<XmlField> header;
    private List<XmlField> body;

    private ComplexXmlField root;

    private Namespace soapenv;
    private static final String SOAP_NS_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    public SoapMessage() {
        this.soapenv = new Namespace("soapenv", SOAP_NS_URI);
        this.header = new ArrayList<>();
        this.body = new ArrayList<>();
        this.root = new ComplexXmlField("root");
    }

    public SoapMessage(String xml) throws IOException, JDOMException {
        this();
        SAXBuilder saxBuilder = new SAXBuilder();
        org.jdom2.Document document = saxBuilder.build(new InputSource(new StringReader(xml)));
        super.transformStringToXmlMessage(document.getRootElement(), this.root);

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
        envelope.setNamespace(this.soapenv);
        ComplexXmlField header = new ComplexXmlField("Header");
        header.setNamespace(this.soapenv);
        ComplexXmlField body = new ComplexXmlField("Body");
        body.setNamespace(this.soapenv);
        for (XmlField headerElement : this.header) {
            header.addField(headerElement);
        }
        for (XmlField bodyElement : this.body) {
            body.addField(bodyElement);
        }
        envelope.addField(header);
        envelope.addField(body);
        Directives directives = new Directives();
        super.buildDirectives(envelope, directives);
        String soapEnvelope = "";
        try {
            soapEnvelope = new Xembler(directives).xml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soapEnvelope;

    }





}
