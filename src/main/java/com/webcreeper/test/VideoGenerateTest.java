package com.webcreeper.test;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.webcreeper.core.DownloadTask;
import com.webcreeper.core.FileOperate;
import com.webcreeper.core.M3U8Crawler;
import com.webcreeper.core.TSVideoFileGenerate;

public class VideoGenerateTest {
    static TSVideoFileGenerate gen = new TSVideoFileGenerate();

    public static void main(String[] args) {
        //subString();
        processTest();
    }


    public static void subString(){
        String tse = "asd.ts?ss";
        System.out.println(tse.lastIndexOf(".ts"));
    }


    private static void generate(String m3u8Content, String tmpDir) throws Exception{

        // 将ts下载到本地
        TSVideoFileGenerate generate = new TSVideoFileGenerate();
        BlockingDeque<DownloadTask> deque = new LinkedBlockingDeque<>();
        
        M3U8Crawler.filterTsFiles(m3u8Content).stream().map(o-> {
            return new DownloadTask(o, tmpDir + TSVideoFileGenerate.tsFilesNameFix(o));
        }).forEach(deque::add);
        
        generate.excuteTask(deque);

    }

    public static void processTest(){
                // String[] arr = new String[]{
        //     "http://www.jacnfabu.xyz/video/m3u8//2023/06/04/dd662d71/",
        //     ".ts",
        //     "D:\\User\\Desktop\\良家极品眼镜妹，换上女仆装再干一炮，掰穴狂舔骚逼，各种姿势爆操.m3u8"
        // };
        
        // linkedGenerate(arr[0], arr[1], arr[2], 0, 469, 4);
        String m3u8Path="";
        m3u8Path = "D:\\temp\\test710.m3u8";
        // m3u8Path ="D:\\User\\Videos\\Desktop\\抖音网红.m3u8";

        String m3u8DirPath = "D:/temp/m3u8/";
        String tmpName = "video_" + System.currentTimeMillis();
        String tmpDir = m3u8DirPath + tmpName + "/";
        try {
            String m3u8Content = new FileOperate().read(m3u8Path);
            generate(m3u8Content, tmpDir);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
