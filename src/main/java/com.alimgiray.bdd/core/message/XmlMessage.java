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
        try {
            return new Xembler(getDirectives()).xml();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private Directives getDirectives() {
        Directives directives = new Directives();

        for (XmlField field : this.xmlFields) {
            buildDirectives(field, directives);
        }

        return directives;
    }

    void buildDirectives(XmlField field, Directives directives) {

        createNamespace(field, directives);
        setAttributes(field, directives);

        if (ComplexXmlField.class.isInstance(field)) {
            for (XmlField xmlField : ((ComplexXmlField) field).getXmlFields()) {
                buildDirectives(xmlField, directives);
            }
        } else {
            directives.set(((SimpleXmlField) field).getFieldValue());
        }

        directives.up();
    }

    private void setAttributes(XmlField field, Directives directives) {

        for (Attribute attribute : field.getAttributes()) {
            directives.attr(attribute.getKey(), attribute.getValue());
        }
    }

    private void createNamespace(XmlField field, Directives directives) {

        if (field.getNamespace() == null) {
            directives.add(field.getFieldName());
            return;
        }

        directives.add(field.getNamespace().getPrefix().concat(":").concat(field.getFieldName()));
        directives.attr("xmlns:".concat(field.getNamespace().getPrefix()), field.getNamespace().getUri());
    }

    void createFromString(Element element, XmlField parentField) {

        if (isComplexField(element)) {

            ComplexXmlField complexXmlField = new ComplexXmlField(element.getName());
            ((ComplexXmlField) parentField).addField(complexXmlField);

            addNamespace(complexXmlField, element);
            addAttributes(complexXmlField, element);
            parentField = complexXmlField;

            for (Element childElement : element.getChildren()) {
                createFromString(childElement, parentField);
            }

            return;
        }

        SimpleXmlField simpleXmlField = new SimpleXmlField(element.getName(), SoapDataType.STRING);
        simpleXmlField.setFieldValue(element.getValue());

        addNamespace(simpleXmlField, element);
        addAttributes(simpleXmlField, element);

        ((ComplexXmlField) parentField).addField(simpleXmlField);
    }

    private boolean isComplexField(Element element) {
        return !element.getChildren().isEmpty();
    }

    private void addNamespace(XmlField field, Element element) {

        if (element.getNamespace() != org.jdom2.Namespace.NO_NAMESPACE) {
            field.setNamespace(new Namespace(element.getNamespacePrefix(), element.getNamespaceURI()));
        }
    }

    private void addAttributes(XmlField field, Element element) {

        if (element.hasAttributes()) {
            for (org.jdom2.Attribute attribute : element.getAttributes()) {
                field.addAttribute(new Attribute(attribute.getName(), attribute.getValue()));
            }
        }
    }

}
