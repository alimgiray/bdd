package com.alimgiray.bdd.core.message;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xembly.Directives;
import org.xembly.Xembler;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

/**
 * @author ahmyilmaz
 * @since 31.05.2018
 */
public class SoapMessage extends XmlMessage {

    private ComplexXmlField envelope;
    private static final String SOAP_NS_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    public SoapMessage(XmlField header, XmlField body) {
        setup();

        envelope.addField(header);
        envelope.addField(body);
        this.addField(this.envelope);
    }

    public SoapMessage(String xml) throws IOException, JDOMException {
        setup();

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new InputSource(new StringReader(xml)));

        ComplexXmlField root = new ComplexXmlField("Envelope");
        createFromString(document.getRootElement(), root);

        this.envelope = root;
        this.addField(this.envelope);
    }

    private void setup() {
        this.envelope = new ComplexXmlField("Envelope");
        envelope.setNamespace(new Namespace("soapenv", SOAP_NS_URI));
    }
}
