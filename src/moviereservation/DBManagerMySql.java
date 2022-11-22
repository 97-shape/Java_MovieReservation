package moviereservation;

import java.sql.*;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author umsang-uk
 */
public class DBManagerMySql extends DBManager {
    public static DBManager getinstance() {
        System.out.println("DBManager:getinstance()");
        if (instance==null)
            instance = new DBManagerMySql();
        return instance;
    }

    public DBManagerMySql() {
        // 생략가능
        // super();
    }
    
    // DB 연결 함수
    @Override
    public void openConnection(){
        System.out.println("mysql");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/Movie?useSSL=false&allowPublicKeyRetrieval=true";
            String userId = "Movie";
            String userPw = "movie@1234";
            conn = DriverManager.getConnection(url, userId, userPw);
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace(); 
        }
    }
    
    public void closeConnection(){
        try {if(conn != null) conn.close();} catch (Exception e){}
    }
    
    public boolean execute (String sql){
        boolean result = false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            result = stmt.execute(sql);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {if(stmt != null) stmt.close();} catch (Exception e){}
        }
        return result;         
    }
    public int executeUpdate(String sql) {
        int result = 0;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {if(stmt != null) stmt.close();} catch (Exception e){}
        }
        return result;
    }
    
    public ArrayList executeQuery(String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<String[]> list = new ArrayList();
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            int count = rs.getMetaData().getColumnCount();
            
            while (rs.next()){
                String[] values = new String[count];
                for (int i = 0; i < count; i++){
                    values[i] = rs.getString(i+1);
                }
                list.add(values);
            }
        } catch (Exception e){
            e.printStackTrace();
            return list;
        } finally {
            try {if(rs != null) rs.close();} catch (Exception e){}
            try {if(stmt != null) stmt.close();} catch (Exception e){}
        }
        return list;
    }}
