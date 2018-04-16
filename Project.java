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
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;
public interface Project extends Remote {
    public boolean loginUser(String usrname, String pswd) throws RemoteException, SQLException;
}
 