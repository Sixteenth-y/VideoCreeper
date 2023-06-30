package com.webcreeper.core;

public class DBConnect {
    public static void main(String[] args) {
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            System.out.println("success!");
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            System.out.println(e);
        }
    }
}
