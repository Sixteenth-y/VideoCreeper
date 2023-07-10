package com.webcreeper.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Callable;

public class TSVideoFileMerger {



    private static class FileNameComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            // TODO Auto-generated method stub
            // 提取字符串中的数字部分进行比较
            int num1 = extractNumber(o1.substring(
                    o1.lastIndexOf(File.separator)));
            int num2 = extractNumber(o2.substring(
                    o2.lastIndexOf(File.separator)));

            return Integer.compare(num1, num2);
        }

        /**
         * 提取字符串中数字
         * 
         * @param s
         * @return
         */
        private int extractNumber(String s) {
            // 正则表达式提取字符串中的数字部分
            String number = s.replaceAll("[^0-9]", "");

            if (number.isEmpty()) {
                return 0;
            } else {
                return Integer.parseInt(number);
            }
        }

    }

    /**
     * this a thread that receviec the messsage create by ffmpeg
     */
    private static class FFmpegProccessMess implements Callable<Integer>{
        private Process proc;

        public FFmpegProccessMess(Process proc){
            this.proc = proc;
        }

        @Override
        public Integer call() throws Exception {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // 输出子进程的输出信息
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return proc.waitFor();
        }
        

    } 

    /**
     * 
     * @param folderPath
     * @param outFilePath
     * @param fileName
     */
    public static void mergeFile(String folderPath, String outFilePath, String fileName) {
        
        try {
            String tsListTxtPath = createTsListTXT(folderPath, outFilePath);

            Process proc = startFFmpegProcess(tsListTxtPath, outFilePath, fileName);
            proc.waitFor();
            
            Thread outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line); // 输出子进程的输出信息
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputThread.start();

            // 等待子进程执行完成
            int exitCode = proc.waitFor();

            // 等待输出线程执行完成
            outputThread.join();

            if (exitCode == 0) {
                System.out.println("mission complete");
            } else {
                System.out.println("mission failed");
            }

        } catch (IOException | InterruptedException e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }

        /**
     * 创建ts列表文件
     * @param folderPath ts文件所在目录
     * @param outFilePath 输出目录
     * @return
     * @throws IOException
     */
    private static String createTsListTXT(String folderPath, String outFilePath) throws IOException {
        File folder = new File(folderPath);

        StringBuilder inputParam = new StringBuilder();

        FileOperate operate = new FileOperate();

        String tsListTxt = folderPath + File.separator + "tsList.txt";

        //筛选文件,提取文件名字,排序
        Arrays.stream(folder.listFiles((dir, name) -> {
                return name.toLowerCase().endsWith(".ts");
            }))
            .map(File::getAbsolutePath)
            .sorted(new FileNameComparator())
            .forEach(tsFilePath -> {
                inputParam.append("file '")
                        .append(tsFilePath)
                        .append("'")
                        .append(System.lineSeparator());
                System.out.println(tsFilePath);
            });
        
        //写入文件列表到tsList.txt文件
        operate.write(tsListTxt, inputParam.toString());

        return tsListTxt;
            
    }

    /**
     * 通过提供的文件列表和输出路径和文件名,产生ffmpeg进程
     * @param tsListTxtPath ts文件列表文件
     * @param outFilePath 输出文件夹
     * @param fileName 视频文件
     * @return
     * @throws IOException
     */
    private static Process startFFmpegProcess(String tsListTxtPath, String outFilePath, String fileName) throws IOException {
        // 启动 FFmpeg 进程并返回进程对象
        ProcessBuilder procBuilder = new ProcessBuilder(
                "ffmpeg",
                "-f",
                "concat",
                "-safe",
                "0",
                "-i",
                tsListTxtPath,
                "-c",
                "copy",
                outFilePath + File.separator + fileName);
        return procBuilder.start();
    }
}
