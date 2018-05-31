import com.alimgiray.bdd.core.message.Attribute;
import com.alimgiray.bdd.core.message.ComplexXmlField;
import com.alimgiray.bdd.core.message.Namespace;
import com.alimgiray.bdd.core.message.SimpleXmlField;
import com.alimgiray.bdd.core.message.SoapDataType;
import com.alimgiray.bdd.core.message.SoapMessage;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * @author ahmyilmaz
 * @since 31.05.2018
 */
public class XmlMessageTest {

    @Test
    public void convert_string_to_xml_message() throws IOException, JDOMException {

        ComplexXmlField user = getUserField(getUsernameField(), getPasswordField());
        SimpleXmlField transTime = getTransTimeField();

        SoapMessage soapMessage = getSoapMessage(user, transTime);
        SoapMessage soapMessageFromString = new SoapMessage(soapMessage.toString());

        assertEquals("İki mesaj birebir aynı olmalı",
                soapMessage.toString(), soapMessageFromString.toString());
    }

    // TODO mesaj üzerinden çok test yapacağımız için, soap mesajı üreten bir factory'miz olabilir.

    private SoapMessage getSoapMessage(ComplexXmlField user, SimpleXmlField transTime) {

        SoapMessage soapMessage = new SoapMessage();
        soapMessage.addFieldToHeader(transTime);
        soapMessage.addFieldToBody(user);
        return soapMessage;
    }

    private SimpleXmlField getTransTimeField() {

        SimpleXmlField transTime = new SimpleXmlField("transtime", SoapDataType.STRING);
        transTime.setFieldValue(LocalDateTime.now().toString());
        return transTime;
    }

    private SimpleXmlField getPasswordField() {

        Namespace ns1 = new Namespace("ns1", "http://fizz.com");
        SimpleXmlField password = new SimpleXmlField("password", SoapDataType.STRING);
        Attribute credentials = new Attribute("credentials", "true");

        password.setNamespace(ns1);
        password.addAttribute(credentials);
        password.setFieldValue("Pass123");

        return password;
    }

    private SimpleXmlField getUsernameField() {

        Namespace ns1 = new Namespace("ns1", "http://fizz.com");
        SimpleXmlField username = new SimpleXmlField("username", SoapDataType.STRING);

        username.setNamespace(ns1);
        username.setFieldValue("Admin");

        return username;
    }

    private ComplexXmlField getUserField(SimpleXmlField username, SimpleXmlField password) {

        Namespace ns0 = new Namespace("ns0", "http://www.abc.com");
        ComplexXmlField user = new ComplexXmlField("user");

        user.setNamespace(ns0);
        user.addField(username);
        user.addField(password);

        return user;
    }

}
