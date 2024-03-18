import com.opencsv.CSVReader;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadCSV {

    public static void main(String[] args) {

        Map<String, String> csvMap = readCSV();

        String xmlPath = "src/main/resources/xml/%s/strings-en.xml";

        List<String> moduleList = getModuleList();
        for (String module : moduleList) {
            String xmlPathFormat = String.format(xmlPath, module);
            SAXBuilder saxBuilder = new SAXBuilder();
            try {
                Document document = saxBuilder.build(new File(xmlPathFormat));
                List<Element> children = document.getRootElement().getChildren();
                for (Element child : children) {
                    List<Attribute> attributes = child.getAttributes();
                    String childValue = child.getValue();
                    for (Attribute attribute : attributes) {
                        String attributeValue = attribute.getValue();
                        String translate = csvMap.get(attributeValue);
                        if (!translate.equals(childValue)) {
                            System.out.printf("数据有变：值：%s, old: %s, new: %s%n", attributeValue, childValue, translate);
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private static List<String> getModuleList() {
        List<String> moduleList = new ArrayList<>();
        moduleList.add("app");
        moduleList.add("backup");
        moduleList.add("base");
        moduleList.add("circle");
        moduleList.add("common");
        moduleList.add("fa-uikit");
        moduleList.add("login");
        moduleList.add("person");
        moduleList.add("transfer");
        return moduleList;
    }

    private static Map<String, String> readCSV() {
        String fileName = "src/main/resources/csv/android_lang.csv";
        Map<String, String> map = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(fileName);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             CSVReader reader = new CSVReader(isr)) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                map.put(nextLine[0], nextLine[2]);
            }
        } catch (Exception e) {

        }
        return map;
    }
}
