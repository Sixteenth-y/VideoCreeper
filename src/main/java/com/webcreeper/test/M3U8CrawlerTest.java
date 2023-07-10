package com.webcreeper.test;

import java.io.IOException;
import java.net.Proxy;
import java.net.Proxy.Type;

import com.webcreeper.core.FileOperate;
import com.webcreeper.core.M3U8Crawler;

public class M3U8CrawlerTest {
    public static void main(String[] args) {


        

        //System.out.println(content);
        
    }

    private static void processTest(){
        M3U8Crawler creeper = new M3U8Crawler();
        String content = "";
        Proxy proxy = null;
        proxy = M3U8Crawler.creatProxyInfo(Type.HTTP, "127.0.0.1", 7890);
        String url = "";
        //url=https://hsex.men/video-837887.htm
        url = "https://p.hjpfe1.com/hjstore/video/20230517/3627606908e4012c1ebab9d63ca63774/5083746sS4wJpVQ_ia57089e87ab96cfcbae2e1bed2cdf0db.m3u8";
        try {
            content = creeper.fetchContent(url);
            FileOperate operation = new FileOperate();
            operation.write("D://temp//test710.m3u8", content); 
        
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
