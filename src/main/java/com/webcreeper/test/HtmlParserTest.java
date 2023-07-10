package com.webcreeper.test;

import java.io.File;
import java.io.IOException;

import com.webcreeper.core.HtmlParser;

public class HtmlParserTest {
    public static void main(String[] args) {
        processTest();
    }
    

    private static void processTest(){
        try {
            HtmlParser parser = new HtmlParser(new File("D:/temp/test.html"));
            System.out.println(parser.findAttriByTageName("source", "src"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
