import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Creates dictionary based on got locale
 * Created by PD on 03.05.2017.
 */
class LocaleProvider {

    private String locale;
    private Map<String, String> data = new HashMap<>();

    LocaleProvider(SettingsProvider settingsProvider) {
//        TODO
//        locale = settingsProvider.get("defaultLocale");
        locale = "pl_PL";
        createDictionary();
    }

    void setLocale(String locale) {
        this.locale = locale;
    }

    String get(String key) {
        return data.get(key);
    }

    private void createDictionary() {
        BufferedReader file;
        Properties properties = new Properties();
        try {
            file = new BufferedReader(new FileReader("locale_"+locale+".properties"));
            properties.load(file);
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = properties.getProperty(key);
                data.put(key, value);
            }
            file.close();
        } catch (Exception e) {
            Debug.Log("Exception in LocaleProvider/createDictionary: " + String.valueOf(e));
        }
    }
}
