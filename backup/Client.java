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
import java.util.Scanner;

public class Client {
	private static Scanner usrInput;
	
	public static void main(String[] args){
		try{
			System.out.println("======================");
			System.out.println("\t LOG IN");
			System.out.println("======================");
			System.out.print("Enter your UserName: ");
			String usrname = usrInput.nextLine();
			System.out.print("Enter your password: ");
			String pswd = usrInput.nextLine();
			System.out.print("Enter Server IpAddress: ");
			String ipaddr = usrInput.nextLine();
			System.out.print("Enter Port Number: ");
			String port = usrInput.nextLine();
			int portnum = Integer.parseInt(port);
			
			Registry registry = LocateRegistry.getRegistry(ipaddr, portnum);
			Project proj = (Project) registry.lookup("groupfive");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
    
}
