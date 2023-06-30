package com.webcreeper.core;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * control file
 */
public class FileOperate {

    

    /**
     * 
     * @param path the file's path
     * @param content 写入内容
     * @throws IOException
     */
    public void write(String path, String content) throws IOException{
        File file = new File(path);
        File fileParentPath = file.getParentFile();

        if(!fileParentPath.exists())
            fileParentPath.mkdirs();
        if(!file.exists())
            file.createNewFile();

        System.out.println(file.getAbsolutePath());

        try (BufferedWriter writer = new BufferedWriter(
            new FileWriter(file))) {
            writer.write(content);
            //writer.close();
        }catch (Exception e) {
            // TODO: handle exception
        }
    }
    
}
