package com.webcreeper.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class M3U8Crawler {

    public static final String M3U8_PATH = "temp\\m3u8";
    public static final String TS_PATH = "temp\\ts";


    /**
     * 获取资源的父级路径
     * @param urlPath
     * @return
     */
    public static String getParentsSourcePath (String urlPath){
        return urlPath.substring(0, urlPath.lastIndexOf("/") + 1);
    }

    /**
     * 筛选出ts文件
     * @param content
     * @return
     */
    public static List<String> filterTsFiles(String content) {
        String regex = ".*\\.ts";
        String regex1 = ".*\\.ts\\?";

        return Arrays.stream(content.split("\\r?\\n"))
                .filter(o -> o.matches(regex) || o.matches(regex1))
                .collect(Collectors.toList());
    }

    /**
     * 给缺少完整路径的ts文件添加完整路径
     * @param m3u8Content m3u8文件文本
     * @param parentPath 完整资源路径
     * @return
     */
    public static String fixMissingTsUrls(String m3u8Content, String parentPath) {
        StringBuilder builder = new StringBuilder();

        Pattern pattern = Pattern.compile(System.lineSeparator());
        Matcher matcher = pattern.matcher(m3u8Content);
        int start = 0;
        while (matcher.find(start)) {
            String line = m3u8Content.substring(start, matcher.start());
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

    public static void setHeader(HttpURLConnection conn){
        conn.setRequestProperty("User-Agent", "Mozilla/5.0"
            + " (Windows NT 10.0; Win64; x64) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) Firefox/89.0");
        
        conn.setRequestProperty("Accept", "text/html,"
            + "application/xhtml+xml,application/xml;"
            + "q=0.9,image/webp,*/*;q=0.8");
        
        //conn.setRequestProperty(TS_PATH, M3U8_PATH);
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
    public String fetchContent(String url) throws IOException {
        return fetchContent(url, null);
    }

    /**
     * 获取资源内容
     * @param urlPath url路径
     * @param proxy 代理信息
     * @return
     * @throws IOException
     */
    public String fetchContent(String urlPath, Proxy proxy) throws IOException {
        StringBuilder content = new StringBuilder();

        URL url = new URL(urlPath);
    
        //判断是否使用代理
        HttpURLConnection conn = (HttpURLConnection) (
            proxy != null ?  url.openConnection(proxy) :  url.openConnection());
        
        setHeader(conn);

        conn.setRequestMethod("GET");

        int responeseCode = conn.getResponseCode();

        // 获取响应编码
        String contentType = conn.getContentType();
        String charset = StandardCharsets.UTF_8.name(); // 默认使用 UTF-8 编码
        if (contentType != null) {
            String[] values = contentType.split(";");

            for (String value : values) {
                value = value.trim();
                if (value.toLowerCase().startsWith("charset=")) {
                    charset = value.substring("charset=".length());
                    break;
                }
            }
        }

        if (responeseCode == HttpURLConnection.HTTP_OK) {

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), charset))) {

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

}
