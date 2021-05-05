/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryserver;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Prince
 */
public class LibraryServer extends UnicastRemoteObject{

    public static String db_host = "localhost:3306";
    public static String db_name = "rmi_db";
    public static String user_name = "root";
    public static String password = "";
    public static int server_port = 1099;
    public static Registry rmi_registry = null;
    
    public LibraryServer() throws RemoteException{
        super();
    }
    
    public static void main(String args[]) {
        
        //File file = new File(classLoader.getResource(fileName).getFile());
        
        Intializer.main(args);
        
    }
    
    public static void stopServer() throws RemoteException{
        System.out.println("Stopping server");
        try {

            //Registry rmiRegistry = LocateRegistry.getRegistry(Server.server_port);
            //HelloInterface myService = (HelloInterface) rmiRegistry.lookup(serverName);

            LibraryServer.rmi_registry.unbind("Users");
            LibraryServer.rmi_registry.unbind("Books");
            LibraryServer.rmi_registry.unbind("Borrowed");

            UnicastRemoteObject.unexportObject(LibraryServer.rmi_registry, true);
            System.out.println("Server has stopped");
            JOptionPane.showMessageDialog(null, "Server has sttoped");

        } catch (NoSuchObjectException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Server Exception", JOptionPane.ERROR_MESSAGE);
        } catch (NotBoundException | RemoteException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Server Exception", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void startServer() throws RemoteException{
        
        try{
            
            LibraryServer.rmi_registry = LocateRegistry.createRegistry(LibraryServer.server_port);
            
            
            UsersImplementation instance1 = new UsersImplementation();
            BooksImplementation instance2 = new BooksImplementation();
            BorrowedImplementation instance3 = new BorrowedImplementation();
            
            LibraryServer.rmi_registry.rebind("Users", instance1);
            LibraryServer.rmi_registry.rebind("Books", instance2);
            LibraryServer.rmi_registry.rebind("Borrowed", instance3);
            
            System.out.println("Server Running ... ");
            JOptionPane.showMessageDialog(null, "Serer detected Running...");
        
        } catch(RemoteException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Server Exception", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
}
