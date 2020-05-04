package com.chris.generator.server;

import com.chris.generator.util.FreeMarkUtil;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @Author: Chris Zhang
 * @Date: 5/4/20 14:53
 */
public class ServerGenerator {

    public static String toPath = "generator/src/main/java/com/chris/generator/test/";

    public static void main(String[] args) throws IOException, TemplateException{

        FreeMarkUtil.initConfig("test.ftl");

        FreeMarkUtil.generator(toPath + "Test.java");
    }
}
