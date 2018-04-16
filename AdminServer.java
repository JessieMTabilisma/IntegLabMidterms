/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integlab.IntegLabMidterms;

/**
 *
 * @author jessietabilisma
 */
import java.io.FileNotFoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class AdminServer extends UnicastRemoteObject implements Project {
    
//    fields
    private Registry registry;
    private static Scanner usrInput;
    private static Connection connect;
    
    public AdminServer() throws RemoteException {
     super();   
    }
//    Starting the server
    public static String serverStart() throws RemoteException {
        Project proj = new AdminServer();
        Registry reg = LocateRegistry.createRegistry(2099);
        reg.rebind("groupFive", proj);
        return "Server connection established";
    }
//    menu
    public static void adminMenu() {
        System.out.println("=======================");
        System.out.println("\t Menu");
        System.out.println("=======================\n");
        System.out.println("[1] Start the server.");
        System.out.println("[2] Register user.");
        System.out.println("[3] Remove user.");
        System.out.println("[4] Update user status.");
        System.out.println("[5] Create/Add new project.");
        System.out.println("[6] Display on goig project/s.");
        System.out.println("[7] Display completed project/s.");
        System.out.println("[8] Exit session.");
    }
//    connect database
    public static void dbConnection(String usr, String pass, String port) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        String DB_Url = "jdbc:mysql://localhost:"+port+"/rmi_project?user="+usr+"&password="+pass;
        connect = DriverManager.getConnection(DB_Url);
        System.out.println("Database connection successfully established");
        System.out.println("Press ⏎ to continue");
        usrInput.nextLine();
        System.out.println("");
    }
//    Redister a user
    public static void registerUser() throws SQLException{
        System.out.println("Enter username: ");
        String usrname = usrInput.nextLine();
        System.out.println("Enter First name: ");
        String fname = usrInput.nextLine();
        System.out.println("Enter Last name: ");
        String lname = usrInput.nextLine();
        System.out.println("Enter password: ");
        String pswd = usrInput.nextLine();
        
        String query = "INSERT INTO users(usrname, fname, lname, pswd) VALUES(?,?,?,?)";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, usrname);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, pswd);
            
            int checkRowInserted = stmt.executeUpdate();
            if(checkRowInserted > 0){
                System.out.println("New user succesfully registered");
            }else{
                System.out.println("Registration failed!");
            }
            
            System.out.println("Press ⏎ to continue");
            usrInput.nextLine();
        } catch(SQLException sqlex){
        }
    }
}
