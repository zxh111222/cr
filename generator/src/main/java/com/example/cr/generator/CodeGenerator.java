package com.example.cr.generator;

import com.example.cr.generator.util.CustomFreemarkerUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerator {
    static String toPath = "generator/src/main/java/com/example/cr/generator/demo/";

    static String pomPath ="generator/pom.xml";

    static {
        new File(toPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
        CustomFreemarkerUtil.getTemplate("test.ftl");
//
//        String tableName = "user";
//        String c = tableName.charAt(0) + "";
//        String upperCase = c.toUpperCase();
//        System.out.println(upperCase);
//        String substring = tableName.substring(1);
//        System.out.println(substring);
//        String tableNameUpperCase = upperCase + substring;
//        System.out.println(tableNameUpperCase);
//        String className = String.format("%sService", tableNameUpperCase);
//        System.out.println(className);
//
//

        String configurationFile = readConfigurationFileFromPomXml();
        Document document = new SAXReader().read("generator/" + configurationFile);
        List<Node> nodes = document.selectNodes("//table");
        for (Node table : nodes) {
            Node tableNameNode = table.selectSingleNode("@tableName");
            Node domainObjectNameNode = table.selectSingleNode("@domainObjectName");
            String tableName = tableNameNode.getText();
            String domainObjectName = domainObjectNameNode.getText();
            System.out.println("表名: " + tableName + ",对应的实体名: " + domainObjectName);

            String className = String.format("%sService", domainObjectName);

            Map<String, Object> data = new HashMap<>();
            data.put("className", className);

            CustomFreemarkerUtil.generate(toPath + className + ".java", data);
        }

    }

    private static String readConfigurationFileFromPomXml() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        HashMap<String, String> map = new HashMap<>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        String configurationFile = node.getText();
        System.out.println("从 pom.xml 读取 mybatis-generator-maven-plugin 需要用到的 configurationFile=" + configurationFile);
        return configurationFile;
    }
}
