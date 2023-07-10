package com.webcreeper.test;

import com.webcreeper.core.TSVideoFileMerger;

public class VideoMergerTest {
    public static void main(String[] args) {
        processTest();
    }

    private static void processTest(){
        // String folder = "D:\\temp\\m3u8\\video_1688045788789";
        // String outFilePath = "D:\\temp\\m3u8\\video_1688045788789";
        String folder = "D:\\temp\\m3u8\\teacher";
        String outFilePath = folder;
        String fileName = "video.mp4";

        TSVideoFileMerger.mergeFile(folder, outFilePath, fileName);
    }
}
