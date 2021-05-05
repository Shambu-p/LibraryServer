/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryserver;

import java.rmi.*;
import java.rmi.server.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;;

/**
 *
 * @author Prince
 */
public class BooksImplementation extends UnicastRemoteObject implements BooksInterface {
    
    public BooksImplementation() throws RemoteException {
        super();
    }
    
    @Override
    public ArrayList<Books>search(String word) throws RemoteException{
        
        ArrayList result = new ArrayList<Books>();
        
        try{
            
            Connection con = null;
            Statement stmt = null;
            
            System.out.println("Connecting to a selected database..");
            con = this.getConnection();
            System.out.println("Connected database successfully..");
            
            System.out.println("Creating statement ....");
            stmt = con.createStatement();
            String sql = "select * from books where title like '%"+word+"%' or category like '%"+word+"%'";
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                result.add(new Books((int) rs.getInt("id"), (String) rs.getString("title"), (String) rs.getString("category"), (int) rs.getInt("shelf_number"), (String) rs.getString("image")));
                
            }
            
            rs.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return result;
        
    }
    
    @Override
    public Books find(int id) throws RemoteException{
        
        try{
            
            Connection con = null;
            Statement stmt = null;
            
            System.out.println("Connecting to a selected database..");
            con = this.getConnection();
            System.out.println("Connected database successfully..");
            
            System.out.println("Creating statement ....");
            stmt = con.createStatement();
            String sql = "select * from books where id = "+id;
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                return new Books((int) rs.getInt("id"), (String) rs.getString("title"), (String) rs.getString("category"), (int) rs.getInt("shelf_number"), (String) rs.getString("image"));
            }
            
            rs.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return null;
        
    }
    
    @Override
    public Boolean delete(int id) throws RemoteException{
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "delete from books where id = " + id;
            
            stmt.execute(query);
            return true;
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return false;
        
    }
    
    @Override
    public Boolean update(int id, String title, String category, int shelf_number, String image) throws RemoteException{
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "update books set title = '"+title+"', category = '"+category+"', shelf_number = "+shelf_number+", image = '"+image+"' where id = " + id;
            
            stmt.execute(query);
            return true;
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return false;
        
    }
    
    @Override
    public Boolean create(String title, String category, int shelf_number, String image) throws RemoteException{
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "insert into books (title, category, shelf_number, image) values( '" + title + "', '" + category + "', "+ shelf_number +", '"+image+"' ) ";
            
            stmt.execute(query);
            
            return true;
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return false;
        
    }
    
    private Connection getConnection() throws Exception, RemoteException{
        
        String db_url = "jdbc:mysql://"+LibraryServer.db_host+"/"+LibraryServer.db_name;
        String user = LibraryServer.user_name;
        String pass = LibraryServer.password;
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(db_url, user, pass);
        
    }
    
}
