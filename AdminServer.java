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
        Registry reg = LocateRegistry.createRegistry(3003);
        reg.rebind("groupFive", proj);
        return "Server connection established";
    }
//    menu
    public static void adminMenu() {
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
        String query = "SELECT username, password FROM users WHERE username = ? AND password = ?";
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
            System.out.print("Press ENTER to continue...");
            usrInput.nextLine();
            System.out.println("");
        } catch (ClassNotFoundException cne) {
            cne.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
//    Title
    public static void titleized(String title) {
        System.out.println("======================");
        System.out.println(title);
        System.out.println("======================");
    }
    public static void pressToContinue(){
        System.out.println("Press ENTER to continue");
        usrInput.nextLine();
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
        
        String query = "INSERT INTO users(username, first_name, last_name, password) VALUES(?,?,?,?)";
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
            
            pressToContinue();
        } catch(SQLException sqlex){
        }
    }
//    Remove user
    public static void deleteUser() throws SQLException {
        titleized("Remove user");
        System.out.println("Enter username that you want to delete");
        String deleteUser = usrInput.nextLine();
        
        String deleteQuery = "DELETE FROM users WHERE username = ?";
        PreparedStatement stmt = connect.prepareStatement(deleteQuery);
        stmt.setString(1, deleteUser);
        
        int optStatus = stmt.executeUpdate();
        if(optStatus > 0){
            System.out.println("User " +deleteUser+ "succesfully deleted");
        } else{
            System.out.println("Username didnt match to the database");
        }
        stmt.close();
        pressToContinue();
        
    }
//    Update user status 
    public static void updateUserStatus() throws SQLException {
        titleized("Update user status");
        System.out.println("Enter username that you want to update the status: ");
        String usrname = usrInput.nextLine();
        
        String queryUpdateStatus = "UPDATE users SET status = ? WHERE username = ?";
        PreparedStatement stmt = connect.prepareStatement(queryUpdateStatus);
        stmt.setString(1, "inactive");
        stmt.setString(2, usrname);
        
        int optUpdate = stmt.executeUpdate();
        if(optUpdate > 0){
            System.out.println(usrname + " successfully update");
        } else { 
            System.out.println(usrname + " didnt match to record");
        }
        stmt.close();
        pressToContinue();
        
    }
//    Create/ Add project
    public static void createProject() throws SQLException {
        titleized("Create a project");
        System.out.println("Enter project name: ");
        String projName = usrInput.nextLine();
        System.out.println("Enter project leader username: ");
        String projLeadName = usrInput.nextLine();
        
        String addProjectQuery = "INSERT INTO projects(project_name, leader) VALUES(?,?)";
        PreparedStatement stmt = connect.prepareStatement(addProjectQuery);
        stmt.setString(1, projName);
        stmt.setString(2, projLeadName);
        
        int optCreate = stmt.executeUpdate();
        if(optCreate  > 0){
            System.out.println(projName + " succesfully added");
        } else{
            System.out.println("Fail to create project");
        }
        stmt.close();
        pressToContinue();
    }
//    Display ongoing project/s
    public static void displayOnGoingProjects() throws SQLException {
        String disOnGoinProj  = "SELECT proj_id, project_name, leader FROM projects WHERE status = ?";
        PreparedStatement stmt = connect.prepareStatement(disOnGoinProj);
        stmt.setString(1, "on-going");
        ResultSet rs = stmt.executeQuery();
        System.out.println("--------------------------------------------");
        System.out.println("On-going project/s");
        System.out.println("--------------------------------------------");
        System.out.printf("%-5s%-20s%-20s%n", "ID", "Project Name", "Project Leader");
        while(rs.next()){
            int projectID = rs.getInt("proj_id");
            String projectName = rs.getString("project_name");
            String projectLeader = rs.getString("leader");
            
           System.out.printf("%-5d%-20s%-20s%n", projectID, projectName, projectLeader);
        }
        
    }
//    Dsiplay completed project/s
    public static void displayCompletedProjects() throws SQLException {
        String disComProj  = "SELECT proj_id, project_name, leader FROM projects WHERE status = ?";
        PreparedStatement stmt = connect.prepareStatement(disComProj);
        stmt.setString(1, "completed");
        ResultSet rs = stmt.executeQuery();
        System.out.println("--------------------------------------------");
        System.out.println("On-going project/s");
        System.out.println("--------------------------------------------");
        System.out.printf("%-5s%-20s%-20s%n", "ID", "Project Name", "Project Leader");
        while(rs.next()){
            int projectID = rs.getInt("proj_id");
            String projectName = rs.getString("project_name");
            String projectLeader = rs.getString("leader");
            
            System.out.printf("%-5d%-20s%-20s%n", projectID, projectName, projectLeader);
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
            System.out.print("Enter username:  ");
            String usrname = usrInput.nextLine();
            System.out.print("Enter password:  ");
            String pswd = usrInput.nextLine();
           
            boolean login =  loginModule(usrname, pswd);
            String displayName = usrname.toUpperCase();
            if(!login){
                System.out.println("Your username or password didnt match !");
            } else{
                System.out.println("Welcome " + displayName + " your in control");
                int choice = 0;
                do{
                    titleized("Menu");
                    adminMenu();
                    System.out.println("Enter your choice: ");
                    choice = Integer.parseInt(usrInput.nextLine());
                    switch(choice) {
                        case 1:
                            System.out.println(serverStart());
                            break;
                        case 2:
                            registerUser();
                            break;
                        case 3:
                            deleteUser();
                            break;
                        case 4:
                            updateUserStatus();
                            break;
                        case 5:
                            createProject();
                            break;
                        case 6:
                            displayOnGoingProjects();
                            break;
                        case 7:
                            displayCompletedProjects();
                            break;
                        case 8:
                            System.exit(0);
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
        String queryUser = "SELECT username, passwordd FROM users WHERE username = ? AND password =?";
        PreparedStatement stmt = connect.prepareStatement(queryUser);
        stmt.setString(1, usrname);
        stmt.setString(2, pswd);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
}
