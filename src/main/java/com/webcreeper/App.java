package com.webcreeper;

import com.webcreeper.core.Boot;
import com.webcreeper.core.TSVideoFileGenerate;

public class App {
    public static void main(String[] args) throws Exception {

        Boot boot = new Boot();

        String pre = "https://cdn.163cdn.net/hls/contents/videos/136000/136839/136839.mp4/seg-";
        String hed = "-v1-a1.ts";

        String filePath = "D:\\User\\Desktop\\test.m3u8";
        TSVideoFileGenerate.linkedGenerate(pre, hed, filePath, 0, 0);

        

        boot.downloadTsFile(filePath, null);
        // boot.myConnect();
        // boot.writerFile();
        
        System.out.println("End");
    }
}
