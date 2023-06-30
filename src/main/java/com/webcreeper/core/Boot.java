package com.webcreeper.core;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Boot {
    public static final String M3U8 = "D:/temp/m3u8/";
    public static final String TS_FILES = "D:/temp/ts/";

    public int defaultThreadNum = 5;
    public ExecutorService executor;

    private MyConnect conn;
    //private static ConcurrentHashMap<String, MyConnect> hub = new ConcurrentHashMap<>();

    public Boot() {
        executor = Executors.newFixedThreadPool(defaultThreadNum);

    }

    public Boot(int threadNum) {
        executor = Executors.newFixedThreadPool(threadNum);
    }

    public void myConnect() {
        conn = new MyConnect();
        conn.connect();
    }

    public void writerFile() {
        if (conn == null || conn.map.isEmpty()) {
            System.out.println("there is not msg got from req");
            return;
        }
        FileOperate op = new FileOperate();

        try {

            Set<Entry<String, String>> set = conn.map.entrySet();

            for (Map.Entry<String, String> entry : set) {
                String path = M3U8 + entry.getKey();
                op.write(path, entry.getValue());
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //
    public void downloadTsFile(String m3u8Path, String targetDir) {
        final String dir;
        if (targetDir == null)
            targetDir = TS_FILES + m3u8Path.substring(
                    m3u8Path.lastIndexOf(File.pathSeparatorChar))
                    + File.pathSeparator;

        dir = targetDir;

        try (BufferedReader reader = new BufferedReader(new FileReader(m3u8Path))) {
            reader.lines().forEach(o -> {
                executor.submit(() -> {
                    FileOperate op = new FileOperate();
                    String name = dir + o.substring(o.lastIndexOf(File.pathSeparator));

                    try {
                        op.write(name, Connect.sendGet(o));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                });
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        ;

    }

    /**
     * inner class
     * using in buli
     */
    private class MyConnect extends Connect {
        private LinkedList<String> urlList = new LinkedList<>();
        private HashMap<String, String> map = new HashMap<>();

        public MyConnect() {
            System.out.println("create Connect");
        }

        public void connect() {

            Scanner in = new Scanner(System.in);
            System.out.println(
                    "input url:(when you press --end,"
                            + " that will be ending)");
            while (in.hasNext()) {
                String order = in.nextLine();
                if ("--end".equals(order))
                    break;

                urlList.addLast(order);
                String tmp = this.regex(sendGet(order));
                map.put(order.substring(
                        order.lastIndexOf("/") + 1), tmp);

                System.out.println("---content---");
                System.out.println(tmp);
                System.out.println("---end---");

            }
            in.close();
        }

        public String regex(String text) {

            String url = urlList.getLast();
            int spiltIndex = url.lastIndexOf("/");

            String prefix = url.substring(0, spiltIndex + 1);

            StringBuilder sb = new StringBuilder();

            for (String val : textSpilt(text)) {
                sb.append(prefix)
                        .append(val)
                        .append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);

            return sb.toString();
        }

        private String[] textSpilt(String text) {
            String[] vals = text.split("\n");
            String regx = ".ts";
            return Arrays.stream(vals)
                    .filter(val -> val.contains(regx))
                    .toArray(String[]::new);

        }
    }

}
