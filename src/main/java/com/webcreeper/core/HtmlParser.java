package com.webcreeper.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlParser {

    private HashMap<String, Map<String, String>> tageMap = new HashMap<>();
    private Document doc;

    public HtmlParser(String htmlContent){
        this.doc = Jsoup.parse(htmlContent);
    }

    public HtmlParser(File html) throws IOException{
        this.doc = Jsoup.parse(html,"UTF-8");
    }

    public HtmlParser(File html, String charsetName) throws IOException{
        this.doc = Jsoup.parse(html, charsetName);
    } 
    
    /**
     * 通过标签名字以及属性名获取属性
     * @param tagName 标签名
     * @param attriName 属性名
     * @return
     */
    public String findAttriByTageName(String tagName, String attriName){

        //如果标签哈希表已有标签的相关信息直接查询
        if(tageMap.containsKey(tagName))
            return tageMap.get(tagName).get(attriName);
        
        Map<String, String> attriMap = new HashMap<>();

        //通过标签获取到Elements对象，再遍历Elements对象将属性添加到属性哈希表
        doc.getElementsByTag(tagName).forEach(elm->{
            elm.attributes().forEach(attr->{
                attriMap.put(attr.getKey(), attr.getValue());});    
        });

        tageMap.put(tagName, attriMap);
        
        return tageMap.get(tagName).get(attriName);
        
    }

    /**
     * 重置html文件对象
     * @param htmlContent html文件文本
     */
    public void resetHtml(String htmlContent){
        this.doc = Jsoup.parse(htmlContent);
        tageMap.clear();

    } 

    /**
     * 重置html文件对象
     * @param html html文件
     * @throws IOException
     */
    public void resetHtml(File html) throws IOException{
        this.doc = Jsoup.parse(html, "UTF-8");
        tageMap.clear();
    }
    
    /**
     * 重置html文件对象
     * @param html html文件
     * @param charsetName 编码格式
     * @throws IOException
     */
    public void resetHtml(File html, String charsetName) throws IOException{
        this.doc = Jsoup.parse(html, charsetName);
        tageMap.clear();
    }

}
