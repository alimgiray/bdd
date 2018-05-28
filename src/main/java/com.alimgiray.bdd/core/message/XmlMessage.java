package com.alimgiray.bdd.core.message;


import org.xembly.Directives;
import org.xembly.Xembler;

import java.util.List;

public class XmlMessage {

    private XmlField header;

    private XmlField body;

    public XmlMessage() {


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
        envelope.addField(this.header);
        envelope.addField(this.body);
        Directives directives = new Directives();
        build(envelope,directives);
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
            directives.add(field.getFieldName());
            List<XmlField> xmlFields = ((ComplexXmlField) field).getXmlFields();
            for (XmlField xmlField : xmlFields) {
                build(xmlField, directives);
            }
            directives.up();
        } else {
            directives.add(field.getFieldName())
                    .set(((SimpleXmlField) field).getFieldValue())
                    .up();
        }


    }


}



