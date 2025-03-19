package com.example.cr.generator.demo;

/**
 * @Author xinhao
 * @Description
 * @CreateTime 2025-03-19 20:49:26
 * @我的网站 www.luckyxinh.com
 */
public class TemplateEngineDemo {
    public static void main(String[] args) {
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
    }
}
