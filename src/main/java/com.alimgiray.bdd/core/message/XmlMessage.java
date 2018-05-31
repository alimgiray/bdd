package com.alimgiray.bdd.core.message;


import org.jdom2.Element;
import org.xembly.Directives;
import org.xembly.Xembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmlMessage {

    private List<XmlField> xmlFields;

    public XmlMessage() {
        this.xmlFields = new ArrayList<>();
    }

    public List<XmlField> getXmlFields() {
        return xmlFields;
    }

    public void addField(XmlField xmlField) {
        this.xmlFields.add(xmlField);
    }

    public void addFields(XmlField... xmlFields) {

        Collections.addAll(this.xmlFields, xmlFields);

    }

    @Override
    public String toString() {

        Directives directives = new Directives();
        for (XmlField field : this.xmlFields) {

            buildDirectives(field, directives);
        }
        String xmlString = "";
        try {
            xmlString = new Xembler(directives).xml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlString;

    }

    void buildDirectives(XmlField field, Directives directives) {

        if (field.getClass() == ComplexXmlField.class) {
            addTagAndSetNamespaceIfAvailable(field, directives);
            setAttributeIfAvailable(field, directives);
            List<XmlField> xmlFields = ((ComplexXmlField) field).getXmlFields();
            for (XmlField xmlField : xmlFields) {
                buildDirectives(xmlField, directives);
            }
            directives.up();
        } else {
            addTagAndSetNamespaceIfAvailable(field, directives);
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

    private void addTagAndSetNamespaceIfAvailable(XmlField field, Directives directives) {
        Namespace ns = field.getNamespace();
        if (ns != null) {
            directives.add(ns.getPrefix().concat(":").concat(field.getFieldName()));
            directives.attr("xmlns:".concat(ns.getPrefix()), ns.getUri());
        } else {
            directives.add(field.getFieldName());
        }
    }

    void transformStringToXmlMessage(Element element, XmlField parentField) {

        if (element.getChildren().isEmpty()) {
            SimpleXmlField simpleXmlField = new SimpleXmlField(element.getName(), SoapDataType.STRING);
            simpleXmlField.setFieldValue(element.getValue());
            addNamespaceIfAvailable(simpleXmlField, element);
            addAttributeIfAvailable(simpleXmlField, element);
            ((ComplexXmlField) parentField).addField(simpleXmlField);

        } else {
            ComplexXmlField complexXmlField = new ComplexXmlField(element.getName());
            ((ComplexXmlField) parentField).addField(complexXmlField);
            addNamespaceIfAvailable(complexXmlField, element);
            addAttributeIfAvailable(complexXmlField, element);
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

    private void addAttributeIfAvailable(XmlField field, Element element) {
        if (element.hasAttributes()) {
            for (org.jdom2.Attribute attribute : element.getAttributes()) {
                Attribute attributeToAdd = new Attribute(attribute.getName(),attribute.getValue());
                field.addAttribute(attributeToAdd);
            }
        }
    }
}



