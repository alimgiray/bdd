package com.alimgiray.bdd.core.message;

import com.alimgiray.bdd.core.message.fields.ComplexXmlField;
import com.alimgiray.bdd.core.message.fields.Namespace;
import com.alimgiray.bdd.core.message.fields.XmlField;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author ahmyilmaz
 * @since 31.05.2018
 */
public class SoapMessage extends XmlMessage {

    private ComplexXmlField envelope;
    private XmlField header;
    private XmlField body;
    private static final String SOAP_NS_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    public SoapMessage(XmlField header, XmlField body) {
        this.header = header;
        this.body = body;
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

        this.addField(root.getXmlFields().get(0));
    }

    private void setup() {
        this.envelope = new ComplexXmlField("Envelope");
        envelope.setNamespace(new Namespace("soapenv", SOAP_NS_URI));
    }
}
