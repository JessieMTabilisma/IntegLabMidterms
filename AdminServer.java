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
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminServer extends UnicastRemoteObject implements Project {
    
//    fields
    private Registry registry;
    private static Scanner usrInput = new Scanner(System.in);
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
//    login module
    public static boolean loginModule(String usrname, String pswd) throws SQLException{
        String query = "SELECT usrname, pswd FROM users WHERE usrname = ? AND pswd = ?";
        PreparedStatement stmt = connect.prepareStatement(query);
        stmt.setString(1, usrname);
        stmt.setString(2, pswd);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
//    connect database
    public static void dbConnection(String port , String user, String pass) throws Exception{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost:"+port+"/rmi_project?user="+user+"&password="+pass;
            connect = DriverManager.getConnection(connectionUrl);
            System.out.println("Connection successful!");
            System.out.print("Press any key to continue...");
            usrInput.nextLine();
            System.out.println("");
        } catch (ClassNotFoundException cne) {
            cne.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
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
            
            System.out.println("Press ENTER to continue");
            usrInput.nextLine();
        } catch(SQLException sqlex){
        }
    }
//    main method
    public static void main(String[] args) throws RemoteException, SQLException{
        try {
        System.out.print("Enter mysql port number: ");
        String port = usrInput.nextLine();
        System.out.print("Enter mysql username: ");
        String user = usrInput.nextLine();
        System.out.print("Enter mysql password: ");
        String pass = usrInput.nextLine();
        dbConnection(port, user, pass);
            System.out.println("Enter username: ");
            String usrname = usrInput.nextLine();
            System.out.println("Enter password: ");
            String pswd = usrInput.nextLine();
            
            boolean login =  loginModule(usrname, pswd);
            String displayName = usrname.toUpperCase();
            if(!login){
                System.out.println("Your username and password didnt match !");
            } else{
                System.out.println("\tWelcome " + displayName + " your in control");
                int choice = 0;
                do{
                    adminMenu();
                    System.out.println("Enter your choice: ");
                    choice = Integer.parseInt(usrInput.nextLine());
                    switch(choice) {
                        case 1:
                            serverStart();
                        case 2:
                            registerUser();
                        default:
                            System.out.println("Your choice didnt match to menu !");
                    }
                }while(choice != 8);
            }
        } catch (Exception ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public boolean loginUser(String usrname, String pswd) throws RemoteException, SQLException {
        String queryUser = "SELECT usrname, pswd FROM users WHERE usrname = ? AND pswd =?";
        PreparedStatement stmt = connect.prepareStatement(queryUser);
        stmt.setString(1, usrname);
        stmt.setString(2, pswd);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
}
