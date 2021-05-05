/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Prince
 */
public class DBInitialization {
    
    public static void init(){
        
        String[] queries = new String[3];
        
        queries[0] = "create table users (id int auto_increment primary key, name varchar(50) not null, image text)";
        queries[1] = "create table books (id int auto_increment primary key, title varchar(100) not null, category varchar(100) not null, shelf_number int not null, image text)";
        queries[2] = "create table borrowed (customer int not null, book int not null, status varchar(30) not null, date varchar(15) not null)";
        
        try{
            
            Connection con = DBInitialization.getConnection();
            Statement stmt = con.createStatement();
            //String query = "insert into borrowed (customer, book, status, date) values( " + user + ", " + book + ", "+status+", "+date+" )";
            for(String query:queries){
                stmt.execute(query);
            }
            
            JOptionPane.showMessageDialog(null, "Database tables are created!");
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
    }
    
    private static Connection getConnection() throws Exception {
        
        String db_url = "jdbc:mysql://"+LibraryServer.db_host+"/"+LibraryServer.db_name;
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(db_url, LibraryServer.user_name, LibraryServer.password);
        
    }
    
}
