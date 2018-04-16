/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integlab.IntegLabMidterms;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.FileNotFoundException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author jessietabilisma
 */
public class ProjectImpl extends UnicastRemoteObject implements Project{
	
	public ProjectImpl() throws RemoteException{
		super();
	}
	@Override
	public boolean loginUser(String username, String password) throws RemoteException, SQLException{
		return true;
	}
    
}
