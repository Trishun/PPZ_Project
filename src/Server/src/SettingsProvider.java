import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings Provider
 * Created by PD on 15.04.2017.
 */

class SettingsProvider {

    private Map<String, String> data = new HashMap<>();

    SettingsProvider() {
        Document document = prepareDocument();
        if (document != null)
            makeDictionary(document);
    }

    private void put(String key, String value) {
        data.put(key, value);
    }

    String get(String key) {
        return data.get(key);
    }

    private Document prepareDocument() {
        try {
            File inputFile = new File("settings.xml");
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            System.out.println("Exception in SettingsProvider/prepareDocument: " + e);
        }
        return null;
    }

    private void makeDictionary(Document document) {
        try {
            NodeList settingsList = document.getElementsByTagName("setting");
            for (int i = 0; i < settingsList.getLength(); i++) {
                put(settingsList.item(i).getAttributes().item(0).toString().split("=")[1].split("\"")[1],
                        settingsList.item(i).getTextContent());
            }
        } catch (Exception e) {
            System.out.println("Exception in SettingsProvider/makeDictionary: " + e);
        }
    }

}
