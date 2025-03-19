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
        Map<String, Object> data = new HashMap<>();
        String className = "Test123";
        data.put("className", className);
        CustomFreemarkerUtil.generate(toPath + className + ".java", data);
    }
}
