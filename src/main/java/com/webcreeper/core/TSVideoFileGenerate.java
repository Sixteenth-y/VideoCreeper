package com.webcreeper.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


public class TSVideoFileGenerate {
    private static final long SINGAL_MAX_DOWNLOAD_TIME = 35L;
    private static final int MAX_THREAD_NUM = 5;



    public static void linkedGenerate(String linkedPart1, String linkedPart2, String filePath, int start, int end, int numFix){
        StringBuffer buf = new StringBuffer();


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for(int idx = start; idx <= end; idx++){
            

                buf.setLength(0);
                buf.append(linkedPart1).append(numFix(numFix, idx)).append(linkedPart2);
                writer.append(buf);
                writer.newLine();

        }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }

    public static void linkedGenerate(String linkedPart1, String linkedPart2, String filePath, int start, int end){
        linkedGenerate(linkedPart1, linkedPart2, filePath, start, end, 0);

    }

    public static String numFix(int len, int idx){
        if(len < 1)
            return String.valueOf(idx);

        return String.format("%0"+len+"d", idx);
        
    }

    public static String tsFilesNameFix(String name){
        return name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf(".ts")+4);

    }

    /**
     * 执行ts下载任务，使用线程池进行下载
     * @param deque
     * @throws Exception
     */
    public void excuteTask(BlockingDeque<DownloadTask> deque) throws Exception{
        int taskNum = deque.size();
        ExecutorService service = Executors.newFixedThreadPool(MAX_THREAD_NUM);
        Queue<DownloadTask> batchTasks = new LinkedList<>();
        
        while(!deque.isEmpty()){

            //执行队列中的任务,将结果和已执行的任务分别加入集合
            List<Future<?>> futures = deque.stream().map(o->{
                batchTasks.add(deque.poll());
                return service.submit(o);
            }).collect(Collectors.toList());
    
            futures.forEach(future -> {
                try {
                    
                    future.get(SINGAL_MAX_DOWNLOAD_TIME, TimeUnit.SECONDS);
                    batchTasks.poll();
                    
                } catch (ExecutionException e) {
                    DownloadTask task = batchTasks.poll();
                    Throwable cause = e.getCause(); // 获取实际的异常对象

                    if (cause instanceof SocketException) {
                        // 处理连接重置异常
                        System.out.println("Download failed: " + task);
                        cause.printStackTrace();
                    } else {
                        // 处理其他异常
                        System.out.println("Task execution failed: " + cause.getMessage());
                    }

                    // 任务执行失败，重新添加到队列
                    deque.offer(task);
                    System.out.println("Retrying task...");
                    
                } catch (TimeoutException e) {
                    DownloadTask task = batchTasks.poll();

                    // 处理超时异常
                    System.out.println("Task execution timed out: " + task.getUrl());
                    // 任务执行失败，重新添加到队列
                    deque.offer(task);
                    System.out.println("Retrying task...");
                } catch (InterruptedException e) {
                    DownloadTask task = batchTasks.poll();
                    deque.offer(task);
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }    

        service.shutdown();
        // 等待所有任务完成
        service.awaitTermination(taskNum * SINGAL_MAX_DOWNLOAD_TIME, TimeUnit.SECONDS); 
    }

 
}
