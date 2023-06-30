package com.webcreeper.core;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
//import java.util.Scanner;

public abstract class Connect {

    public static void main(String[] args) {
        // Connect conn= new Connect();
        // Scanner in = new Scanner(System.in);
        // System.out.println("input url:(when you press --end, that will be ending)");
        // while (in.hasNext()){
        //     String order = in.nextLine();
        //     if("--end".equals(order))
        //         break;
            
        //     System.out.println(conn.sendGet(order));
            System.out.println("kkk");
        }
  
    /*
     * get msg by GET req
     * url----url
     * return---- msg
     */
    public static String sendGet(String url){
        StringBuilder builder = new StringBuilder();
        BufferedReader in = null;
        try{
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.connect();
            in = new BufferedReader(
                new InputStreamReader(
                    conn.getInputStream()
                    , "UTF-8"));

            String line;
            while((line = in.readLine())!= null)
                builder.append(line).append('\n');
        }catch(Exception e){
            System.out.println("send req failed");
            e.printStackTrace();
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch(Exception e){
                System.out.println("failed  to close conn");
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    
}
