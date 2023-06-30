package main.java.com.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class M3U8FileCreeper {

    public static final String M3U8_PATH = "temp\\m3u8";
    public static final String TS_PATH = "temp\\ts";


    public static String getParentsSourcePath (String urlPath){
        return urlPath.substring(0, urlPath.lastIndexOf("/") + 1);
    }

    public static ArrayList<String> tsfilter(String content) {
        String regx = ".*\\.ts";

        return Arrays.stream(content.split("\\r?\\n"))
                .filter(o -> o.matches(regx))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 给缺少完整路径的ts文件添加完整路径
     * @param content m3u8文件文本
     * @param parentPath 完整资源路径
     * @return
     */
    public static String tsUrlFixed(String content, String parentPath) {
        StringBuilder builder = new StringBuilder();

        Pattern pattern = Pattern.compile(System.lineSeparator());
        Matcher matcher = pattern.matcher(content);
        int start = 0;
        while (matcher.find(start)) {
            String line = content.substring(start, matcher.start());
            if (line.contains(".ts") && !line.startsWith(parentPath)) {
                builder.append(parentPath);
            }
            builder.append(line).append(System.lineSeparator());
            start = matcher.end();
        }
    
        return builder.toString();

    }

        /**
     * 
     * @param type Type.DIRECT,Type.HTTP,Type.SOCKS
     * @param host
     * @param port
     * @return
     */
    public static Proxy creatProxyInfo(Proxy.Type type, String host, int port){

        return new Proxy(type, new InetSocketAddress(host ,port));

    }

    // public void srcCreeper(String url) throws IOException{

    // String tmpName = File.separator + System.currentTimeMillis();
    // File newDirPath = new File(M3U8_PATH + tmpName );

    // if(!newDirPath.exists())
    // throw new FileNotFoundException("File can't found");

    // srcCreeper(url, newDirPath.getAbsolutePath());

    // }

    /**
     * 获取资源内容
     * @param url 文件url
     * @return
     * @throws IOException
     */
    public String srcCreeper(String url) throws IOException {
        return srcCreeper(url, null);
    }

    /**
     * 获取资源内容
     * @param urlStr url路径
     * @param proxy 代理信息
     * @return
     * @throws IOException
     */
    public String srcCreeper(String urlStr, Proxy proxy) throws IOException {
        StringBuilder content = new StringBuilder();

        URL url = new URL(urlStr);
    
        //判断是否使用代理
        HttpURLConnection conn = (HttpURLConnection) (
            proxy != null ?  url.openConnection(proxy) :  url.openConnection());

        conn.setRequestMethod("GET");

        int responeseCode = conn.getResponseCode();

        if (responeseCode == HttpURLConnection.HTTP_OK) {

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {

                    content.append(line).append(System.lineSeparator());
                
                }

            } catch (IOException e) {
                System.out.println(e);

            } finally {
                System.out.println("responeseCode: " + responeseCode);
            }
        }

        conn.disconnect();

        return content.toString();

    }


    public static void main(String[] args) {
        M3U8FileCreeper creeper = new M3U8FileCreeper();
        String content = "";
        Proxy proxy = null;
        proxy = creatProxyInfo(Type.HTTP, "127.0.0.1", 7890);

        try {
            content = creeper.srcCreeper("https://cdn.bigcloud.click/hls/841624/index.m3u8?t=1687966227&m=FZAdFWy3F-5NOFXQfUBF0w", proxy);
            FileOperate operation = new FileOperate();
            operation.write("D://temp//test.m3u8", content); 
        
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        

        //System.out.println(content);
        
    }

}
