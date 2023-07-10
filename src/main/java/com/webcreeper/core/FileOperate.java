package com.webcreeper.core;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;

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
    
    /**
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public String read(String path) throws IOException{
        File file = new File(path);
        StringBuilder content = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            
            reader.lines().forEach(o->{content.append(o).append(System.lineSeparator());});

        }catch(Exception e){

        }

        return content.toString();
    }

}
