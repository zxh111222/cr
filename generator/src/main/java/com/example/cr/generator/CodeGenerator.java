package com.example.cr.generator;

import com.example.cr.generator.util.CustomFreemarkerUtil;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {
    static String toPath = "generator/src/main/java/com/example/cr/generator/demo/";

    static {
        new File(toPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
        CustomFreemarkerUtil.getTemplate("test.ftl");

        String tableName = "user";
        String c = tableName.charAt(0) + "";
        String upperCase = c.toUpperCase();
        System.out.println(upperCase);
        String substring = tableName.substring(1);
        System.out.println(substring);
        String tableNameUpperCase = upperCase + substring;
        System.out.println(tableNameUpperCase);
        String className = String.format("%sService", tableNameUpperCase);
        System.out.println(className);

        Map<String, Object> data = new HashMap<>();
        data.put("className", className);

        CustomFreemarkerUtil.generate(toPath + className + ".java", data);
    }
}
