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
public class XmlParseTest {
    @Test
    public void xmlStringToXmlMessageTest() {

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

        SoapMessage signupMessage = new SoapMessage();
        signupMessage.addFieldToHeader(transtime);
        signupMessage.addFieldToBody(user);

        SoapMessage signupMessageFromString = null;
        try {
            signupMessageFromString = new SoapMessage(signupMessage.toString());
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        assertEquals("String Xml'den oluşturulan mesajla ilk mesaj uyuşmuyor",signupMessage.toString(),signupMessageFromString.toString());
    }

}
