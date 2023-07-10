package com.webcreeper.test;

import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.webcreeper.core.DownloadTask;
import com.webcreeper.core.FileOperate;
import com.webcreeper.core.HtmlParser;
import com.webcreeper.core.M3U8Crawler;
import com.webcreeper.core.TSVideoFileGenerate;
import com.webcreeper.core.TSVideoFileMerger;

public class Test {
    public static void main(String[] args) {

        //String pageUrl = "https://hsex.men/video-841150.htm";
        String pageUrl = "https://hsex.men/video-836619.htm";
        String videoName = "体制内换妻公务员和人民教师";
        String tmpName = "video_" + System.currentTimeMillis();
        
        String m3u8DirPath = "D:/temp/m3u8/";
        String tmpDir = m3u8DirPath + tmpName + "/";

        Proxy proxy = M3U8Crawler.creatProxyInfo(Type.HTTP, "127.0.0.1", 7890);

        try {
            // 首先获取视频网页
            M3U8Crawler creeper = new M3U8Crawler();
            String htmlContent = creeper.fetchContent(pageUrl, proxy);

            // 爬取视频的m3u8文件
            String m3u8SrcUrl = new HtmlParser(htmlContent).findAttriByTageName("source", "src");
            String m3u8Content = creeper.fetchContent(m3u8SrcUrl, proxy);
            m3u8Content = M3U8Crawler.fixMissingTsUrls(m3u8Content, M3U8Crawler.getParentsSourcePath(m3u8SrcUrl));
            FileOperate operate = new FileOperate();
            operate.write(tmpDir + "video.m3u8", m3u8Content);

            // 将ts下载到本地
            generate(m3u8Content, tmpDir);
            
            // TSVideoFileGenerate generate = new TSVideoFileGenerate();
            // BlockingDeque<DownloadTask> deque = new LinkedBlockingDeque<>();
            
            // M3U8Crawler.filterTsFiles(m3u8Content).stream().map(o-> {
            //     return new DownloadTask(o, tmpDir + o.substring(o.lastIndexOf("/") + 1));
            // }).forEach(deque::add);
            
            // generate.excuteTask(deque);

            // 合并ts文件
            TSVideoFileMerger.mergeFile(tmpDir, tmpDir, videoName + ".mp4");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }

    }

    public static void generate(String m3u8Content, String tmpDir) throws Exception{

        // 将ts下载到本地
        TSVideoFileGenerate generate = new TSVideoFileGenerate();
        BlockingDeque<DownloadTask> deque = new LinkedBlockingDeque<>();
        
        M3U8Crawler.filterTsFiles(m3u8Content).stream().map(o-> {
            return new DownloadTask(o, tmpDir + o.substring(o.lastIndexOf("/") + 1));
        }).forEach(deque::add);
        
        generate.excuteTask(deque);

    }

}
