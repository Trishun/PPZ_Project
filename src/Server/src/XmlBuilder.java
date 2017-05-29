import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;


/**
 * Created by PD on 15.04.2017.
 */
public class XmlBuilder {

    /**
     * Main.
     *
     * @param argv the argv
     */
    public static void main(String argv[]) {
        build();
//        SettingsProvider settingsProvider = new SettingsProvider();
    }

    private static void build() {
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder =
                    dbFactory.newDocumentBuilder();
            Document document = dBuilder.newDocument();
            // root element
            Element rootElement = document.createElement("settings");
            document.appendChild(rootElement);


            Element server = document.createElement("server");
            rootElement.appendChild(server);

            Element setting = document.createElement("setting");
            Attr attrType = document.createAttribute("type");
            attrType.setValue("port");
            setting.setAttributeNode(attrType);
            setting.appendChild(document.createTextNode("9090"));
            server.appendChild(setting);

            Element httpserver = document.createElement("httpserver");
            rootElement.appendChild(httpserver);

            setting = document.createElement("setting");
            attrType = document.createAttribute("type");
            attrType.setValue("httpport");
            setting.setAttributeNode(attrType);
            setting.appendChild(document.createTextNode("8080"));
            httpserver.appendChild(setting);

            Element database = document.createElement("database");
            rootElement.appendChild(database);

            setting = document.createElement("setting");
            attrType = document.createAttribute("type");
            attrType.setValue("dbname");
            setting.setAttributeNode(attrType);
            setting.appendChild(document.createTextNode("ppz"));
            database.appendChild(setting);
            setting = document.createElement("setting");
            attrType = document.createAttribute("type");
            attrType.setValue("username");
            setting.setAttributeNode(attrType);
            setting.appendChild(document.createTextNode("uname"));
            database.appendChild(setting);
            setting = document.createElement("setting");
            attrType = document.createAttribute("type");
            attrType.setValue("password");
            setting.setAttributeNode(attrType);
            setting.appendChild(document.createTextNode("upass"));
            database.appendChild(setting);





            // write the content into xml file
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(".\\settings.xml"));
            transformer.transform(source, result);
            // Output to console for testing
            StreamResult consoleResult =
                    new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}