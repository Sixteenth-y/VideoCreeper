package com.webcreeper.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadTask implements Runnable{
    private String url;
    private String outputPath;
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getoutputPath() {
        return outputPath;
    }

    public void setoutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public DownloadTask(String url, String outputPath){
        this.url = url;
        this.outputPath = outputPath;

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            URL fileUrl = new URL(url);

            try (InputStream inputStream = new BufferedInputStream(fileUrl.openStream());
                 FileOutputStream outputStream = new FileOutputStream(outputPath)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("Download completed: " + outputPath);
        } catch (IOException e) {
            System.out.println("Download failed: " + url);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url = "https://cdn.bigcloud.click/hls/842100/index2.ts";
        String outputPath =  "D:\\CodeField\\CODE_Java\\Java_Single\\WebCreeper\\WEBCREEPER\\temp\\ts\\学姐\\";
        System.out.println(File.separator);
        outputPath += url.substring(url.lastIndexOf("/"));

        DownloadTask task = new DownloadTask(url, outputPath);
        new Thread(task).start();
    }
}
