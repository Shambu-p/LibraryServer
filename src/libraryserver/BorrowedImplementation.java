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
public class BorrowedImplementation extends UnicastRemoteObject implements BorrowedInterface{
    
    public BorrowedImplementation() throws RemoteException {
        super();
    }

    @Override
    public ArrayList<Borrowed> searchByUser(int user) throws RemoteException {
        ArrayList result = new ArrayList<Borrowed>();
        
        try{
            
            Connection con = null;
            Statement stmt = null;
            
            System.out.println("Connecting to a selected database..");
            con = this.getConnection();
            System.out.println("Connected database successfully..");
            
            System.out.println("Creating statement ....");
            stmt = con.createStatement();
            String sql = "select * from borrowed where customer = "+user;
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                result.add(new Borrowed((int) rs.getInt("customer"), (int) rs.getInt("book"), (String) rs.getString("status"), (String) rs.getString("date")));
                
            }
            
            rs.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return result;
    }

    @Override
    public ArrayList<Borrowed> searchByBook(int book) throws RemoteException {
        
        ArrayList result = new ArrayList<Borrowed>();
        
        try{
            
            Connection con = null;
            Statement stmt = null;
            
            System.out.println("Connecting to a selected database..");
            con = this.getConnection();
            System.out.println("Connected database successfully..");
            
            System.out.println("Creating statement ....");
            stmt = con.createStatement();
            String sql = "select * from borrowed where book = "+book;
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                result.add(new Borrowed((int) rs.getInt("customer"), (int) rs.getInt("book"), (String) rs.getString("status"), (String) rs.getString("date")));
                
            }
            
            rs.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return result;
        
    }

    @Override
    public ArrayList<Borrowed> bookStatus(int book, String status) throws RemoteException {
        
        ArrayList result = new ArrayList<Borrowed>();
        
        try{
            
            Connection con = null;
            Statement stmt = null;
            
            System.out.println("Connecting to a selected database..");
            con = this.getConnection();
            System.out.println("Connected database successfully..");
            
            System.out.println("Creating statement ....");
            stmt = con.createStatement();
            String sql = "select * from borrowed where status = '"+status+"' and book = "+book;
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                result.add(new Borrowed((int) rs.getInt("customer"), (int) rs.getInt("book"), (String) rs.getString("status"), (String) rs.getString("date")));
                
            }
            
            rs.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return result;
        
    }

    @Override
    public ArrayList<Borrowed> customerStatus(int customer, String status) throws RemoteException {
        
        ArrayList<Borrowed> returned = new ArrayList();
        
        try{
            
            Connection con = null;
            Statement stmt = null;
            
            System.out.println("Connecting to a selected database..");
            con = this.getConnection();
            System.out.println("Connected database successfully..");
            
            System.out.println("Creating statement ....");
            stmt = con.createStatement();
            String sql = "select * from borrowed where customer = "+customer+" and status = '"+status+"'";
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                returned.add(new Borrowed((int) rs.getInt("customer"), (int) rs.getInt("book"), (String) rs.getString("status"), (String) rs.getString("date")));
                
            }
            
            rs.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return returned;
        
    }

    @Override
    public Boolean delete(int customer, int book) throws RemoteException {
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "delete from borrowed where customer = " + customer +" book = "+book;
            
            return stmt.execute(query);
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return false;
        
    }

    @Override
    public Boolean update(int user, int book, String status, String date) throws RemoteException{
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "update borrowed set date = '"+date+"', status = '"+status+"' where customer = " + user + " and book = "+book;
            
            return stmt.execute(query);
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return false;
        
    }

    @Override
    public Boolean create(int user, int book, String status, String date) throws RemoteException{
        
        try{
            
            Connection con = this.getConnection();
            Statement stmt = con.createStatement();
            String query = "insert into borrowed (customer, book, status, date) values( " + user + ", " + book + ", '"+status+"', '"+date+"' ) ";
            
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
