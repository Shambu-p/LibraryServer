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
import javax.swing.JOptionPane;

/**
 *
 * @author Prince
 */
public class UsersImplementation extends UnicastRemoteObject implements UsersInterface {
    
    public UsersImplementation() throws RemoteException {
        super();
    }
    
    @Override
    public ArrayList<User>search(String name)throws RemoteException {
        
        ArrayList result = new ArrayList<User>();
        
        try{
            
            Connection con = null;
            Statement stmt = null;
            
            System.out.println("Connecting to a selected database..");
            con = this.getConnection();
            System.out.println("Connected database successfully..");
            
            System.out.println("Creating statement ....");
            stmt = con.createStatement();
            String sql = "select * from users where name like '%"+name+"%'";
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                result.add(new User((int) rs.getInt("id"), (String) rs.getString("name"), (String) rs.getString("image")));
                
            }
            
            rs.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return result;
        
    }
    
    @Override
    public User find(int id)throws RemoteException {
        
        try{
            
            Connection con = null;
            Statement stmt = null;
            
            System.out.println("Connecting to a selected database..");
            con = this.getConnection();
            System.out.println("Connected database successfully..");
            
            System.out.println("Creating statement ....");
            stmt = con.createStatement();
            String sql = "select * from users where id = "+id;
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                return new User((int) rs.getInt("id"), (String) rs.getString("name"), (String) rs.getString("image"));
            }
            
            rs.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return null;
        
    }
    
    @Override
    public Boolean delete(int id)throws RemoteException {
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "delete from users where id = " + id;
            
            stmt.execute(query);
            return true;
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return false;
        
    }
    
    @Override
    public Boolean update(int id, String name, String image)throws RemoteException {
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "update users set name = '"+name+"', image = '"+image+"' where id = " + id;
            
            stmt.execute(query);
            return true;
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return false;
        
    }
    
    @Override
    public Boolean create(String name, String image)throws RemoteException {
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "insert into users (name, image) values( '" + name + "', '"+image+"' ) ";
            
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
        System.out.println("Database connected");
        return DriverManager.getConnection(db_url, user, pass);
        
    }
    
}
