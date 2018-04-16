package integlab.IntegLabMidterms;

/**
 *
 * @author jessietabilisma
 */
import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
public interface Project extends Remote {
    public boolean userLogin(String usrname, String pswd) throws RemoteException, SQLException;
    public ArrayList displayAllProjects() throws RemoteException, SQLException;
    public boolean userType(String username, String project_name) throws RemoteException, SQLException;
    public ArrayList displayProjLead(String username) throws RemoteException, SQLException;
    public ArrayList displayProject(String username) throws RemoteException, SQLException;
    public int getProjectID(String project) throws RemoteException, SQLException;
    public String addMember(String username, int project_id) throws RemoteException, SQLException;
    public String removeMember(String username, int project_id) throws RemoteException, SQLException;
    public String completedProject(int project_id) throws RemoteException, SQLException;
    public String uploadFile(String file, String username, int project_id) throws RemoteException, SQLException, FileNotFoundException;
    public ArrayList viewUserProjects(String username) throws RemoteException, SQLException;
}
 