package com.alimgiray.bdd.core.message.fields;

public class SimpleXmlField extends XmlField {

    private final SoapDataType type;
    private String fieldValue;

    public SimpleXmlField(String fieldName, SoapDataType type) {
        super(fieldName);
        this.type = type;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public SoapDataType getType() {
        return type;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
