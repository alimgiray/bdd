package com.alimgiray.bdd;

import com.alimgiray.bdd.core.message.Attribute;
import com.alimgiray.bdd.core.message.ComplexXmlField;
import com.alimgiray.bdd.core.message.Namespace;
import com.alimgiray.bdd.core.message.SimpleXmlField;
import com.alimgiray.bdd.core.message.SoapDataType;
import com.alimgiray.bdd.core.message.XmlMessage;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;

/**
 * @author ahmyilmaz
 * @since 28.05.2018
 */
public class Example {

    public static void main(String[] args) {
        Namespace ns0 = new Namespace("ns0", "http://10.10.10.71:8080/xporter/services");
        ComplexXmlField user = new ComplexXmlField("user");
        user.setNamespace(ns0);
        Namespace ns1 = new Namespace("ns1", "http://fizz.com");
        SimpleXmlField username = new SimpleXmlField("username", SoapDataType.STRING);
        username.setFieldValue("Admin");
        username.setNamespace(ns1);
        SimpleXmlField password = new SimpleXmlField("password", SoapDataType.STRING);
        Attribute credential = new Attribute("credential", "true");
        password.addAttribute(credential);
        password.setNamespace(ns1);
        password.setFieldValue("Pass123");
        user.addField(username);
        user.addField(password);

        SimpleXmlField transtime = new SimpleXmlField("transtime", SoapDataType.STRING);
        transtime.setFieldValue(LocalDateTime.now().toString());

        XmlMessage signup = new XmlMessage();
        ComplexXmlField header = new ComplexXmlField("Header");
        ComplexXmlField body = new ComplexXmlField("Body");
        header.addField(transtime);
        body.addField(user);
        signup.addFieldToHeader(transtime);
        signup.addFieldToBody(user);
        System.out.println(signup.toString());



    }

    public static Document build(String xml) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }


}
