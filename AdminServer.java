
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
import java.util.ArrayList;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AdminServer extends UnicastRemoteObject implements Project {
    
//    fields
    private Registry registry;
    private static Scanner usrInput = new Scanner(System.in);
    private static Connection connect;
    private static PreparedStatement stmt;
    
    public AdminServer() throws RemoteException {
     super();   
    }
//    Starting the server
    public static String serverStart() throws RemoteException {
        Project proj = new AdminServer();
        Registry reg = LocateRegistry.createRegistry(3006);
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
        String query = "SELECT username, password FROM user WHERE username = ? AND password = ?";
        stmt = connect.prepareStatement(query);
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
        
        String query = "INSERT INTO user(username, fname, lname, password) VALUES(?,?,?,?)";
            stmt = connect.prepareStatement(query);
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
    }
//    Remove user
    public static void deleteUser() throws SQLException {
        titleized("Remove user");
        System.out.println("Enter username that you want to delete");
        String deleteUser = usrInput.nextLine();
        
        String deleteQuery = "DELETE FROM user WHERE username = ?";
        stmt = connect.prepareStatement(deleteQuery);
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
        
        String queryUpdateStatus = "UPDATE user SET status = ? WHERE username = ?";
        stmt = connect.prepareStatement(queryUpdateStatus);
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
        
        String addProjectQuery = "INSERT INTO project(projName, projLeader) VALUES(?,?)";
        stmt = connect.prepareStatement(addProjectQuery);
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
        String disOnGoinProj  = "SELECT projID, projName, projLeader FROM project WHERE status = ?";
        stmt = connect.prepareStatement(disOnGoinProj);
        stmt.setString(1, "on-going");
        ResultSet rs = stmt.executeQuery();
        System.out.println("--------------------------------------------");
        System.out.println("On-going project/s");
        System.out.println("--------------------------------------------");
        System.out.printf("%-5s%-20s%-20s%n", "ID", "Project Name", "Project Leader");
        while(rs.next()){
            int projectID = rs.getInt("projID");
            String projectName = rs.getString("projName");
            String projectLeader = rs.getString("projLeader");
            
           System.out.printf("%-5d%-20s%-20s%n", projectID, projectName, projectLeader);
        }
        
    }
//    Dsiplay completed project/s
    public static void displayCompletedProjects() throws SQLException {
        String disComProj  = "SELECT projID, projName, projLeader FROM project WHERE status = ?";
        stmt = connect.prepareStatement(disComProj);
        stmt.setString(1, "completed");
        ResultSet rs = stmt.executeQuery();
        System.out.println("--------------------------------------------");
        System.out.println("Completed project/s");
        System.out.println("--------------------------------------------");
        System.out.printf("%-5s%-20s%-20s%n", "ID", "Project Name", "Project Leader");
        while(rs.next()){
            int projectID = rs.getInt("projID");
            String projectName = rs.getString("projName");
            String projectLeader = rs.getString("projLeader");
            
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
    public boolean userLogin(String usrname, String pswd) throws RemoteException, SQLException {
        String queryUser = "SELECT username, password FROM user WHERE username = ? AND password =?";
        stmt = connect.prepareStatement(queryUser);
        stmt.setString(1, usrname);
        stmt.setString(2, pswd);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    public boolean validateUserPrivileges(String username, String project_name) throws RemoteException, SQLException {
        String query = "SELECT projName FROM project WHERE projLeader = ? AND projName = ?";
        stmt = connect.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, project_name);
        ResultSet result = stmt.executeQuery();
        if(result.next()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public ArrayList displayAllProjects() throws RemoteException, SQLException {
        String displayAllProj = "SELECT projName FROM project";
        stmt = connect.prepareStatement(displayAllProj);
        ResultSet result = stmt.executeQuery();
        ArrayList<String> projects = new ArrayList<>();
        while(result.next()) {
            String project = result.getString("projName");
            projects.add(project);
        }
        return projects;
    }
    @Override
    public boolean userType(String usrname, String projName) throws RemoteException, SQLException {
        String validateQuery = "SELECT projName FROM project WHERE projLeader = ? AND projName = ?";
        stmt = connect.prepareStatement(validateQuery);
        stmt.setString(1, usrname);
        stmt.setString(2, projName);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? true : false;
    }
    @Override
    public ArrayList displayProjLead(String usrname) throws RemoteException, SQLException {
        String projLeadQuery = "SELECT projName FROM project WHERE projLeader = ?";
        stmt = connect.prepareStatement(projLeadQuery);
        stmt.setString(1, usrname);
        ResultSet result = stmt.executeQuery();
        ArrayList<String> projects = new ArrayList<>();
        while(result.next()) {
            String pName = result.getString("projName");
            projects.add(pName);
        }
        return projects;
    }
    @Override
    public ArrayList displayProject(String usrname) throws RemoteException, SQLException {
        String query = "SELECT projName FROM project JOIN projectMem ON project.projID = projectMem.projID JOIN users ON user.username = projectMem.username WHERE users.username = ?";
        stmt = connect.prepareStatement(query);
        stmt.setString(1, usrname);
        ResultSet result = stmt.executeQuery();
        ArrayList<String> projects = new ArrayList<>();
        while(result.next()) {
            String p = result.getString("projName");
            projects.add(p);
        }
        return projects;
    }
    @Override
    public int getProjectID(String project) throws RemoteException, SQLException {
        String query = "SELECT projID FROM project WHERE projName = ?";
        stmt = connect.prepareStatement(query);
        stmt.setString(1, project);
        ResultSet result = stmt.executeQuery();
        if(result.next()) {
            return result.getInt("projID");
        } else {
            return -1;
        }
    }
    @Override
    public String addMember(String usrname, int project_id) throws RemoteException, SQLException {
        String insertMember = "INSERT INTO projectMem(username, projID) VALUES(?,?)";
        PreparedStatement stmtToo = connect.prepareStatement(insertMember);
        stmtToo.setString(1, usrname);
        stmtToo.setInt(2, project_id);
        int memberAdded = stmtToo.executeUpdate();
        return memberAdded > 0 ? "Member succesfully added" : "Member failed to add";
    }
    @Override
    public String removeMember(String username, int project_id) throws RemoteException, SQLException {
        String sql = "DELETE FROM projectMem WHERE username = ? AND projID = ?";
        stmt = connect.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setInt(2, project_id);     
        int memberDeleted = stmt.executeUpdate();
        return memberDeleted > 0 ? "Member successfully removed from this project!" : "Failed to removed";
    }
    @Override
    public String completedProject(int projID) throws RemoteException, SQLException {
        String sql = "UPDATE project SET status = ? WHERE projID = ?";
        stmt = connect.prepareStatement(sql);
        stmt.setString(1, "completed");
        stmt.setInt(2, projID);
        int rowUpdated = stmt.executeUpdate();
        return rowUpdated > 0 ? "Project completed" : "Project still in progress";
    }
    @Override
    public String uploadFile(String file, String usrname, int proID) throws RemoteException, SQLException, FileNotFoundException {
        String sql = "INSERT into files(file, user, p_id) VALUES(?,?,?)";
        stmt = connect.prepareStatement(sql);
        InputStream in = new FileInputStream(new File(file));
        stmt.setBlob(1, in);
        stmt.setString(2, usrname);
        stmt.setInt(3, proID);
        int addedFile = stmt.executeUpdate();
        return addedFile > 0 ? "File uploaded succesfully" : "Upload failed";
    }
    @Override
    public ArrayList viewUserProjects(String username) throws RemoteException, SQLException {
        String query = "SELECT projName FROM project JOIN projectMem ON project.projID = projectMem.projID JOIN user ON user.username = projectMem.username WHERE user.username = ? AND project.status = ?";
        stmt = connect.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, "on-going");
        ResultSet result = stmt.executeQuery();
        ArrayList<String> projects = new ArrayList<>();
        while(result.next()) {
            String p = result.getString("projName");
            projects.add(p);
        }
        return projects;
    }
}
